package org.freeciv.net;
import java.io.IOException;
import org.freeciv.util.Enum;

/**
 * Packet representing information about a connection.
 *
 * Verified 1.12.cvs
 * @author Brian.Duff@dubh.org
 */
public class PktConnInfo extends AbstractPacket
{
  
  /** user may issue no commands at all */
  public static final int ALLOW_NONE = Enum.start();
  /** user may issue informational commands            */
  public static final int ALLOW_INFO = Enum.get();
  /** user may issue commands that affect game & users */
  public static final int ALLOW_CTRL = Enum.get();
  /** user may issue *all* commands - dangerous!       */
  public static final int ALLOW_HACK = Enum.get();
  /** the number of levels                             */
  public static final int ALLOW_NUM = Enum.get();
  /** used as a failure return code */
  public static final int ALLOW_UNRECOGNIZED = Enum.get();
  public int id;
  public boolean used; // false means client should forget its info about this connection 
  public boolean established;
  public int player_num;
  public boolean observer;
  public int access_level;
  public String name;
  public String addr;
  public String capability;
  public PktConnInfo() 
  {
    super();
  }
  public PktConnInfo( InStream in ) 
  {
    super( in );
  }
  public void send( OutStream out )
               throws IOException
  {
    out.writeInt( id );
    int data;
    data = used ? 1 : 0;
    data |= established ? 2 : 0;
    data |= observer ? 4 : 0;
    out.writeUnsignedByte( data );
    out.writeUnsignedByte( player_num );
    out.writeUnsignedByte( access_level );
    out.writeZeroString( name );
    out.writeZeroString( addr );
    out.writeZeroString( capability );
  }
  public void receive( InStream in )
  {
    id = in.readInt();
    int data = in.readUnsignedByte();
    used = ( ( data & 1 ) != 0 );
    established = ( ( ( data >>= 1 ) & 1 ) != 0 );
    observer = ( ( ( data >>= 1 ) & 1 ) != 0 );
    player_num = in.readUnsignedByte();
    access_level = in.readUnsignedByte();
    name = in.readZeroString();
    addr = in.readZeroString();
    capability = in.readZeroString();
  }
}
