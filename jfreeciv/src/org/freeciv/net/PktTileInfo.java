package org.freeciv.net;

public class PktTileInfo extends AbstractPacket
{
  public int x;
  public int y;
  public int type;
  public int special;
  public int known;
  public PktTileInfo() 
  {
    super();
  }
  public PktTileInfo( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    type = in.readUnsignedByte();
    special = in.readShort();
    known = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( x );
    out.writeUnsignedByte( y );
    out.writeUnsignedByte( type );
    out.writeShort( special );
    out.writeUnsignedByte( known );
    out.sendPacket();
  }
}
