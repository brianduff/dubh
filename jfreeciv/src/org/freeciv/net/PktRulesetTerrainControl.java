package org.freeciv.net;

public class PktRulesetTerrainControl  extends AbstractPacket  // should also be a help item
{
  public int river_style;
  public boolean may_road;
  public boolean may_irrigate;
  public boolean may_mine;
  public boolean may_transform;
  public int ocean_reclaim_requirement;
  public int land_channel_requirement;
  public int river_move_mode;     // boolean?
  public int river_defense_bonus;
  public int river_trade_incr;
  public int fortress_defense_bonus;
  public int road_superhighway_trade_bonus;
  public int rail_food_bonus;
  public int rail_shield_bonus;
  public int rail_trade_bonus;
  public int farmland_supermarket_food_bonus;
  public int pollution_food_penalty;
  public int pollution_shield_penalty;
  public int pollution_trade_penalty;
  public int fallout_food_penalty;
  public int fallout_shield_penalty;
  public int fallout_trade_penalty;  
  public String river_help_text;


	public PktRulesetTerrainControl()
	{
	  super();
	}
	
	public PktRulesetTerrainControl(InStream in)
	{
	  super(in);
	}
	
	public void receive(InStream in)
	{
    river_style = in.readUnsignedByte();
    may_road  = in.readUnsignedByte() !=0;
    may_irrigate = in.readUnsignedByte() != 0;
    may_mine = in.readUnsignedByte()!=0;
    may_transform = in.readUnsignedByte()!=0;
    ocean_reclaim_requirement = in.readUnsignedByte();
    land_channel_requirement = in.readUnsignedByte();
    river_move_mode = in.readUnsignedByte();
    river_defense_bonus = in.readShort();
    river_trade_incr = in.readShort();
    fortress_defense_bonus = in.readShort();
    road_superhighway_trade_bonus = in.readShort();
    rail_food_bonus = in.readShort();
    rail_shield_bonus = in.readShort();
    rail_trade_bonus = in.readShort();
    farmland_supermarket_food_bonus = in.readShort();
    pollution_food_penalty = in.readShort();
    pollution_shield_penalty = in.readShort();
    pollution_trade_penalty = in.readShort();

    fallout_food_penalty = in.readShort();
    fallout_shield_penalty = in.readShort();
    fallout_trade_penalty = in.readShort();

    if (in.hasMoreData())
    {
      river_help_text = in.readZeroString();
    }

	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	
	}
}