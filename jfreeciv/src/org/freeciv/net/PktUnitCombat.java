package org.freeciv.net;

public class PktUnitCombat extends AbstractPacket
{
  public int attacker_unit_id;
  public int defender_unit_id;
  public int attacker_hp;
  public int defender_hp;
  public boolean make_winner_veteran;
  public PktUnitCombat() 
  {
    super();
  }
  public PktUnitCombat( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    attacker_unit_id = in.readShort();
    defender_unit_id = in.readShort();
    attacker_hp = in.readUnsignedByte();
    defender_hp = in.readUnsignedByte();
    make_winner_veteran = in.readUnsignedByte() != 0;
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( PACKET_UNIT_COMBAT );
    out.writeShort( attacker_unit_id );
    out.writeShort( defender_unit_id );
    out.writeUnsignedByte( attacker_hp );
    out.writeUnsignedByte( defender_hp );
    out.writeUnsignedByte( make_winner_veteran ? 1 : 0 );
    out.sendPacket();
  }
}
