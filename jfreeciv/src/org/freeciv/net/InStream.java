package org.freeciv.net;
import java.io.InputStream;
import java.io.IOException;
import org.freeciv.common.Logger;


/**
 * InStream is a wrapper round an InputStream that receives freeciv packets
 * and buffers them (a packet at a time).
 *
 * The recvPacket() method is called to receive a packet from the input
 * stream. This packet is held in a buffer, the contents of which can
 * be retrieved using the read...() methods on this class. 
 *
 * @author Arthur Biesadowski
 * @author Brian Duff
 */
public final class InStream
{
  /**
   * The actual input stream
   */
  private InputStream input;

  /**
   * A buffer for incoming data.
   */
  private byte[] inData = new byte[ 16000 ];

  /**
   * Index to the current offset in the buffer
   */
  private int inPtr;

  /**
   * The length of an incoming packet
   */
  private short inputPacketLength;

  /**
   * The type of an incoming packet. An unsigned byte, so stored as an int
   * in java.
   */
  private int inputPacketType;


  /**
   * Construct a stream for receiving Freeciv packets from a specified input
   * stream
   *
   * @param anInput an InputStream to read data from
   */
  public InStream( InputStream anInput ) 
  {
    input = anInput;
  }

  /**
   * Close the input stream this InStream is based on and clean up. You can
   * no longer use this InStream once it has been closed; it's therefore
   * recommended you immediately discard any references to the instance
   * after calling this method.
   *
   * @throws IOException if an exception occurs closing the stream
   */
  public void close() throws IOException
  {
    input.close();

    input = null;
    inData = null;
  }

  /**
   * Determine whether there is more data in the current packet.
   *
   * @return true if there is more data in the packet.
   */
  public boolean hasMoreData()
  {
    return inPtr < inputPacketLength;
  }

  /**
   * Get the number of remaining bytes in the packet
   *
   * @return the number of remaining bytes in the current packet
   */
  public int packIterRemaining()
  {
    return inputPacketLength - inPtr;
  }

  /**
   * Receive a packet (private?) into the internal packet buffer.
   *
   * @throws IOException if an exception occurs receiving the packet
   * @throws org.freeciv.net.NetworkProtocolException
   *    If the packet buffer is overrun or underrun.
   */
  public void recvPacket() throws IOException, NetworkProtocolException
  {
    if( inPtr != inputPacketLength )
    {
      throw new IncorrectPacketLengthException(
        "Packet length of " + inputPacketLength + " type " + inputPacketType + 
        " but read " + inPtr 
      );
    }
    inPtr = 0;
    int d0 = input.read();
    int d1 = input.read();
    inData[ 0 ] = (byte)d0;
    inData[ 1 ] = (byte)d1;
    inputPacketLength = (short)( ( d0 << 8 ) + d1 );
    int alreadyRead = 2;
    while( alreadyRead < inputPacketLength )
    {
      alreadyRead += 
        input.read( inData, alreadyRead, inputPacketLength - alreadyRead );
    }
    inputPacketType = inData[ 2 ];
    inPtr = 3;
  }

  /**
   * Get the packet type of the current packet
   *
   * @return the packet type of the current packet.
   */
  public int getInputPacketType()
  {
    return inputPacketType;
  }

  /**
   * Read a null terminated string from the packet, and return a java
   * String. The incoming (ANSI) stream is converted to unicode on the fly,
   * but (obviously) only characters supported by ANSI can be received (and
   * sent).
   *
   * @return a java String corresponding to all bytes in the current packet
   *    up to the first null byte (0)
   * @throws org.freeciv.net.PacketOverrunException
   *    if there is no more data in the packet
   */
  public String readZeroString()
    throws NetworkProtocolException
  {
    if( inPtr >= inputPacketLength )
    {
      throw new PacketOverrunException();
    }
    StringBuffer sb = new StringBuffer( 100 );
    byte b = inData[ inPtr++ ];
    while( b != 0 )
    {
      // byte is signed in java, but the bytes we receive from the server 
      // are actually c chars, which are unsigned bytes. Java doesn't have
      // unsigned bytes, and signed bytes can't be directly converted to chars.
      // The easiest way is to first convert the incoming byte into an
      // int (rendering the byte part unsigned again), then converting this
      // int to a java unicode jar.
      // 
      // The server will send us ANSI characters: it's ok to convert these
      // implicitly to unicode, because the lower 8 bits of unicode are the
      // same as ANSI anyway.
      //
      // Aren't standards fun?
      
      int i = ((int)b) & 0xff;  // Get the unsigned value of b into i
      sb.append( (char)i );     // And then convert i into a unicode character

      
      b = inData[ inPtr++ ];
    }
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "readString: " + sb.toString() );
    }
    return sb.toString();
  }

  /**
   * Read a (signed) byte.
   *
   * @return a signed byte from the packet. Java bytes are always signed.
   * @throws org.freeciv.net.PacketOverrunException if there is no more
   *    data in the packet
   */
  public byte readByte() throws NetworkProtocolException
  {
    if( inPtr >= inputPacketLength )
    {
      throw new PacketOverrunException();
    }
    byte b = inData[ inPtr++ ];
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "ReadByte: " + b );
    }
    return b;
  }

  /**
   * Read an unsigned byte.
   *
   * @return an integer containing an unsigned byte value. Because java bytes
   *    are always signed, we have to use an int (32 bit).
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet
   */
  public int readUnsignedByte() throws NetworkProtocolException
  {
    // actually, we could use a short.... but int is more common anyway.
    if( inPtr >= inputPacketLength )
    {
      throw new PacketOverrunException();
    }
    int i = ( (int)inData[ inPtr++ ] ) & 0xff;
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "ReadUnsignedByte: " + i );
    }
    return i;
  }

  /**
   * Read a short (unsigned 16 bit value)
   *
   * @return an integer containing the unsigned short. Because java short is
   *    signed, we use an int
   *
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet.
   */
  public int readShort() throws NetworkProtocolException
  {
    if( inPtr >= inputPacketLength )
    {
      throw new PacketOverrunException();
    }

    // reverse the bit order
    int s = ( ( inData[ inPtr++ ] << 8 ) & 0xff00 ) | 
            ( inData[ inPtr++ ] & 0xff );
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "ReadShort: " + s );
    }
    return s;
  }

  /**
   * Read a signed int value. 
   *
   * @return a signed int.
   *
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet.
   */
  public int readInt() throws NetworkProtocolException
  {
    if( inPtr >= inputPacketLength )
    {
      throw new PacketOverrunException();
    }

    // reverse the bit order
    int i = ( ( inData[ inPtr++ ] << 24 ) & 0xff000000 ) | 
            ( ( inData[ inPtr++ ] << 16 ) & 0xff0000 )   | 
            ( ( inData[ inPtr++ ] << 8 ) & 0xff00 )      | 
            ( inData[ inPtr++ ] & 0xff );
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "ReadInt: " + i );
    }
    return i;
  }

  /**
   * Read a bit string from the server.
   *
   * @return an array of booleans corresponding to whether each bit in the
   *    string is on or off
   *
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet
   */
  public boolean[] readBitString() throws NetworkProtocolException
  {
    int length = readUnsignedByte();
    
    boolean[] arr = new boolean[ length ];
    for( int i = 0;i < length; )
    {
      int data = readByte();
      for( int b = 0;b < 8 && i < length;b++,i++ )
      {
        if( ( data & ( 1 << b ) ) != 0 )
        {
          arr[ i ] = true;
        }
      }
    }
    return arr;
  }
  
  /**
   * Read an array of unsigned bytes from the server. 
   *
   * @return an array of integers representing unsigned bytes from the server
   *
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet.
   */
  public int[] readUnsignedByteVector() throws NetworkProtocolException
  {  
    //Equivalent to get_uint8_vec8() in common/packets.c  
    int size = readUnsignedByte();
    int[] result = new int[ size ];
    for( int i = 0;i < size;i++ )
    {
      result[ i ] = readUnsignedByte();
    }
    return result;
  }

  /**
   * Read an array of short (unsigned 16 bit ) values
   *
   * @return an array of integers representing unsigned 16 bit values
   *
   * @throws org.freeciv.net.PacketOverrunException if there is no more data
   *    in the packet.
   */
  public int[] readShortVector() throws NetworkProtocolException
  {
    int size = readUnsignedByte();
    int[] result = new int[ size ];
    for( int i = 0;i < size;i++ )
    {
      result[ i ] = readShort();
    }
    return result;
  }
  

  /**
   * Read the city map?
   *
   * @return ?
   */
  public char[] readCityMap()
  {
    char[] str = new char[ 26 ]; // ?? const
    final int index[] = 
    {
      1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 
      18, 19, 21, 22, 23
    };
    str[ 0 ] = '2';
    str[ 4 ] = '2';
    str[ 12 ] = '1';
    str[ 20 ] = '2';
    str[ 24 ] = '2';
    str[ 25 ] = '\0';
    for( int i = 0;i < 20; )
    {
      if( false ) // piter->short_packet > 0
      {
        for( int j = 0;j < 5;j++ )
        {
          str[ index[ i++ ] ] = '0';
        }
      }
      else
      {
        byte b = inData[ inPtr++ ];
        str[ index[ i++ ] ] = (char)( '0' + (char)b / 81 );
        b %= 81;
        str[ index[ i++ ] ] = (char)( '0' + (char)b / 27 );
        b %= 27;
        str[ index[ i++ ] ] = (char)( '0' + (char)b / 9 );
        b %= 9;
        str[ index[ i++ ] ] = (char)( '0' + (char)b / 3 );
        b %= 3;
        str[ index[ i++ ] ] = (char)( '0' + (char)b );
      }
    }
    return str;
  }


  /**
   * Internal debug method for dumping the buffer contents
   */
  private String dumpData()
  {
    StringBuffer sb = new StringBuffer( 4000 );
    sb.append( " type=" ).append( inputPacketType );
    sb.append( " length=" ).append( inputPacketLength );
    sb.append( " inPtr=" ).append( inPtr );
    sb.append( " data=" );
    int x = inPtr;
    while( x < inputPacketLength )
    {
      sb.append( inData[ x++ ] ).append( ',' );
    }
    return sb.toString();
  }

  /**
   * Consume the rest of the packet
   */
  public void consume()
  {
    inPtr = inputPacketLength;
  }

  /**
   * Rewind to the start of the packet
   */
  public void rewind()
  {
    inPtr = 3;
  }
}
