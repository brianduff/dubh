package org.freeciv.net;

public class PktUnitRequest extends AbstractPacket
{
  public int unit_id;
  public int city_id;
  public int x, y;
  public String name;
  public PktUnitRequest() 
  {
    super();
  }
  public PktUnitRequest( InStream in ) 
  {
    super( in );
  }
  int type;
  public void setType( int aType )
  {
    type = aType;
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( type );
    out.writeShort( unit_id );
    out.writeShort( city_id );
    out.writeUnsignedByte( x );
    out.writeUnsignedByte( y );
    out.writeZeroString( name );
    out.sendPacket();
  }
  public void receive( InStream in )
  {
    type = in.getInputPacketType();
    unit_id = in.readShort();
    city_id = in.readShort();
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    name = in.readZeroString();
  }
}
