package org.freeciv.net;

// BAD IMPORT
import org.freeciv.client.Constants;
import java.io.IOException;

/**
 * All packets subclass from abstract packet and implement the send
 * and recieve methods. AbstractPacket provides some utility methods.
 * @author Brian Duff
 * @since 1.10.0
 */
abstract class AbstractPacket implements Packet,Constants
{
  private int m_type = -1;
  
  /**
   * Construct an abstract packet that is going to be used to
   * send.
   */
  public AbstractPacket() 
  {
    
  }
  public AbstractPacket( int type ) 
  {
    m_type = type;
  }
  
  /**
   * Get the packet type. This will be -1 if it has never been set.
   * Dodgy hacky stuff. tidy me up one day.
   */
  public int getType()
  {
    return m_type;
  }
  public void setType( int type )
  {
    
  }
  
  /**
   * Construct an abstract packet and initialize it by reading from
   * the in stream
   */
  public AbstractPacket( InStream is ) 
  {
    receive( is );
  }
  public static void main( String[] args )
  {
    printBits( 256 );
    printBits( 65536 );
    printBits( 2097152 );
    printBits( 1048576 );
  }
  public static void printBits( int val )
  {
    int mask = 1;
    long[] bitVals = new long[ 32 ];
    for( int i = 0;i < 32;i++ )
    {
      bitVals[ i ] = ( val & mask );
      mask <<= 1;
    }
    for( int i = 31;i >= 0;i-- )
    {
      if( ( i % 8 ) == 7 )
      {
        System.out.print( " " );
      }
      System.out.print( bitVals[ i ] != 0 ? "1" : "0" );
    }
    System.out.print( " = " + val );
    System.out.println();
  }
  
  /**
   * Recieve a worklist from the peer.
   *
   * @param is the in stream
   * @param wl if null, a new WorkList instance is created, otherwise
   *    the passed worklist is used.
   * @return either a new WorkList instance or the wl parameter, with its
   *    values set based on recieved data from the peer.
   */
  protected WorkList getWorkList( InStream is, WorkList wl )
  {
    boolean valid = ( is.readByte() > 0 );
    String name = is.readZeroString();
    int[] ids = new int[ MAX_LEN_WORKLIST ];
    for( int i = 0;i < MAX_LEN_WORKLIST;i++ )
    {
      ids[ i ] = is.readShort();
    }
    if( wl == null )
    {
      wl = new WorkList( valid, name, ids );
    }
    else
    {
      wl.setValid( valid );
      wl.setName( name );
      wl.setIds( ids );
    }
    return wl;
  }
  
  /**
   * Send a worklist to the peer.
   */
  protected void putWorkList( OutStream os, WorkList wl )
  {
    os.writeByte( wl.isValid() ? 1 : 0 );
    os.writeZeroString( wl.getName() );
    for( int i = 0;i < MAX_LEN_WORKLIST;i++ )
    {
      os.writeShort( wl.getIds()[ i ] );
    }
  }
  protected int[] getTechList( InStream is, int[] tl )
  {
    if( tl == null )
    {
      tl = new int[ MAX_NUM_TECH_LIST ];
    }
    int i = 0;
    for( ;i < MAX_NUM_TECH_LIST;i++ )
    {
      tl[ i ] = is.readUnsignedByte();
      if( tl[ i ] == A_LAST )
      {
        break;
      }
    }
    for( ;i < MAX_NUM_TECH_LIST;i++ )
    {
      tl[ i ] = A_LAST;
    }
    return tl;
  }
  protected void putTechList( OutStream os, int[] list )
  {
    for( int i = 0;i < MAX_NUM_TECH_LIST;i++ )
    {
      os.writeByte( list[ i ] );
      if( list[ i ] == A_LAST )
      {
        break;
      }
    }
  }
}
