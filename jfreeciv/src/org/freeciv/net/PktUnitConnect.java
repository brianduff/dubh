package org.freeciv.net;

public class PktUnitConnect extends AbstractPacket
{
  public int activity_type;
  public int unit_id;
  public int dest_x, dest_y;
  public String name;
  public PktUnitConnect() 
  {
    super();
  }
  public PktUnitConnect( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( activity_type );
    out.writeShort( unit_id );
    out.writeShort( dest_x );
    out.writeShort( dest_y );
    out.sendPacket();
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    activity_type = in.readUnsignedByte();
    unit_id = in.readShort();
    dest_x = in.readShort();
    dest_y = in.readShort();
  }
}
