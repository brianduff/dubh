package org.freeciv.net;
import java.io.IOException;
import java.io.OutputStream;
public class OutStream
{
  OutputStream output;
  byte[] outData = new byte[ 16000 ];
  int outPtr = 3;
  public OutStream( OutputStream anOutput )
  {
    output = anOutput;
  }
  public void writeBitString( boolean[] bs )
  {


  // TODO> Whew. pointer arithmetic. ouch.
  }
  public void writeInt( int i )
  {
    outData[ outPtr++ ] = (byte)( i >> 24 );
    outData[ outPtr++ ] = (byte)( i >> 16 );
    outData[ outPtr++ ] = (byte)( i >> 8 );
    outData[ outPtr++ ] = (byte)( i );
  }
  /*
  public void writeShort( short i )
  {
  outData[outPtr++] = (byte)(i>>8);
  outData[outPtr++] = (byte)(i);
  }
  */
  public void writeShort( int i )
  {
    outData[ outPtr++ ] = (byte)( i >> 8 );
    outData[ outPtr++ ] = (byte)( i );
  }
  public void writeByte( int i )
  {
    outData[ outPtr++ ] = (byte)i;
  }
  public void writeUnsignedByte( int i )
  {
    outData[ outPtr++ ] = (byte)i;
  }
  public void writeZeroString( String str )
  {
    if( str != null )
    {
      for( int i = 0;i < str.length();i++ )
      {
        char c = str.charAt( i );
        if( c > 127 )
        {
          c = '?';
        }
        outData[ outPtr++ ] = (byte)c;
      }
    }
    outData[ outPtr++ ] = 0;
  }
  public void writeCityMap( char[] str )
  {
    final int index[] =
    {
      1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17,
      18, 19, 21, 22, 23
    };
    for( int i = 0;i < 20;i += 5 )
    {
      outData[ outPtr++ ] = (byte)( ( str[ index[ i ] ] - '0' ) * 81 + ( str[ index[ i + 1 ] ] - '0' ) * 27 + ( str[ index[ i + 2 ] ] - '0' ) * 9 + ( str[ index[ i + 3 ] ] - '0' ) * 3 + ( str[ index[ i + 4 ] ] - '0' ) * 1 );
    }
  }
  public void setType( int i )
  {
    outData[ 2 ] = (byte)i;
  }
  public void clearOut()
  {
    outPtr = 3;
  }
  public void sendPacket()
               throws IOException
  {
    try
    {
      outData[ 0 ] = (byte)( outPtr >> 8 );
      outData[ 1 ] = (byte)( outPtr );
      output.write( outData, 0, outPtr );
      output.flush();
    }
    finally
    {
      clearOut();
    }
  }


  public void close()
  {
    try
    {
      output.close();
    }
    catch (IOException ioe)
    {
      // Ignore for now
    }
  }

}
