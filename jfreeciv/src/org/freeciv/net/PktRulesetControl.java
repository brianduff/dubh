package org.freeciv.net;


/**
 * Ruleset control packet. Sent once at the start of the game
 */
public class PktRulesetControl extends AbstractPacket
{
  public int aqueduct_size;
  public int sewer_size;
  public int add_to_size_limit;
  public int num_unit_types;
  public int num_tech_types;
  public int num_impr_types;
  public class c_rtech
  {
    public int get_bonus_tech;
    public int cathedral_plus;
    public int cathedral_minus;
    public int colosseum_plus;
    public int temple_plus;
    public int partisan_req[];
  }
  public c_rtech rtech = new c_rtech();
  public int government_when_anarchy;
  public int default_government;
  public int government_count;
  public int nation_count;
  public int playable_nation_count;
  public int style_count;
  public PktRulesetControl() 
  {
    super();
  }
  public PktRulesetControl( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    aqueduct_size = in.readUnsignedByte();
    sewer_size = in.readUnsignedByte();
    add_to_size_limit = in.readUnsignedByte();
    rtech.get_bonus_tech = in.readUnsignedByte();
    rtech.cathedral_plus = in.readUnsignedByte();
    rtech.cathedral_minus = in.readUnsignedByte();
    rtech.colosseum_plus = in.readUnsignedByte();
    rtech.temple_plus = in.readUnsignedByte();
    government_count = in.readUnsignedByte();
    default_government = in.readUnsignedByte();
    government_when_anarchy = in.readUnsignedByte();
    num_unit_types = in.readUnsignedByte();
    num_impr_types = in.readUnsignedByte();
    num_tech_types = in.readUnsignedByte();
    nation_count = in.readUnsignedByte();
    playable_nation_count = in.readUnsignedByte();
    style_count = in.readUnsignedByte();
    rtech.partisan_req = getTechList( in, rtech.partisan_req );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
