package org.freeciv.net;
public class PktUnitInfo extends AbstractPacket
{
  // Updated for 1.12.cvs
  public int id;
  public int owner;
  public int x;
  public int y;
  public boolean veteran;
  public int homecity;
  public int type;
  public int movesleft;
  public int hp;
  public int upkeep;
  public int upkeep_food;
  public int upkeep_gold;
  public int activity;
  public int activity_count;
  public int activity_target;
  public int unhappiness;
  public boolean ai;
  public boolean paradropped;
  public boolean connecting;
  public boolean carried;
  public boolean select_it;
  public int fuel;
  public int goto_dest_x;
  public int goto_dest_y;
  public int packet_use;
  public int info_city_id;
  public int serial_num;
  public PktUnitInfo() 
  {
    super();
  }
  public PktUnitInfo( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    id = in.readShort();
    int pack = in.readUnsignedByte();
    veteran = ( ( pack & 0x10 ) != 0 );
    ai = ( ( pack & 0x20 ) != 0 );
    paradropped = ( ( pack & 0x40 ) != 0 );
    connecting = ( ( pack & 0x80 ) != 0 );
    carried = ( ( pack & 0x08 ) != 0 );
    select_it = ( ( pack * 0x04 ) != 0 );
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    homecity = in.readShort();
    type = in.readUnsignedByte();
    movesleft = in.readUnsignedByte();
    hp = in.readUnsignedByte();
    upkeep = in.readUnsignedByte();
    upkeep_food = in.readUnsignedByte();
    upkeep_gold = in.readUnsignedByte();
    unhappiness = in.readUnsignedByte();
    activity = in.readUnsignedByte();
    activity_count = in.readUnsignedByte();
    goto_dest_x = in.readUnsignedByte();
    goto_dest_y = in.readUnsignedByte();
    activity_target = in.readShort();
    packet_use = in.readUnsignedByte();
    info_city_id = in.readShort();
    serial_num = in.readShort();
    if( in.hasMoreData() )
    {
      fuel = in.readByte();
    }
    else
    {
      fuel = 0;
    }
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( PACKET_UNIT_INFO );
    out.writeShort( id );
    out.writeUnsignedByte( ( veteran ? 0x10 : 0 ) | ( ai ? 0x20 : 0 ) | ( paradropped ? 0x40 : 0 ) | ( connecting ? 0x80 : 0 ) | ( carried ? 0x08 : 0 ) | ( select_it ? 0x04 : 0 ) );
    out.writeUnsignedByte( x );
    out.writeUnsignedByte( y );
    out.writeShort( homecity );
    out.writeUnsignedByte( type );
    out.writeUnsignedByte( movesleft );
    out.writeUnsignedByte( hp );
    out.writeUnsignedByte( upkeep );
    out.writeUnsignedByte( upkeep_food );
    out.writeUnsignedByte( upkeep_gold );
    out.writeUnsignedByte( unhappiness );
    out.writeUnsignedByte( activity );
    out.writeUnsignedByte( activity_count );
    out.writeUnsignedByte( goto_dest_x );
    out.writeUnsignedByte( goto_dest_y );
    out.writeShort( activity_target );
    out.writeUnsignedByte( packet_use );
    out.writeShort( info_city_id );
    out.writeShort( serial_num );
    if( fuel != 0 )
    {
      out.writeUnsignedByte( fuel );
    }
    out.sendPacket();
  }
  public boolean equals( Object obj )
  {
    return ( obj != null && obj instanceof PktUnitInfo && ( (PktUnitInfo)obj ).id == id );
  }
}
