package org.freeciv.net;
public class PktCityRequest extends AbstractPacket
{
  public int city_id;
  public int build_id;
  public boolean is_build_id_unit_id;
  public int worker_x, worker_y;
  public int specialist_from, specialist_to;
  public WorkList worklist;
  public String name;
  public PktCityRequest() 
  {
    super();
  }
  public PktCityRequest( InStream in ) 
  {
    super( in );
  }
  private int pktType;
  public void setPktType( int type )
  {
    pktType = type;
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( pktType );
    out.writeShort( city_id );
    out.writeUnsignedByte( build_id );
    out.writeUnsignedByte( is_build_id_unit_id ? 1 : 0 );
    out.writeUnsignedByte( worker_x );
    out.writeUnsignedByte( worker_y );
    out.writeUnsignedByte( specialist_from );
    out.writeUnsignedByte( specialist_to );
    putWorkList( out, worklist );
    out.writeZeroString( name );
    out.sendPacket();
  }
  public void receive( InStream in )
  {
    pktType = in.getInputPacketType();
    city_id = in.readShort();
    build_id = in.readUnsignedByte();
    is_build_id_unit_id = in.readUnsignedByte() > 0 ? true : false;
    worker_x = in.readUnsignedByte();
    worker_y = in.readUnsignedByte();
    specialist_from = in.readUnsignedByte();
    specialist_to = in.readUnsignedByte();
    worklist = getWorkList( in, worklist );
    name = in.readZeroString();
  }
}
