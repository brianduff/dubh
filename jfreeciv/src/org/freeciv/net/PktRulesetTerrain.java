package org.freeciv.net;

public class PktRulesetTerrain  extends AbstractPacket  // should also be a help item
{
// BD: Add impl at some point
	public int id;

	public String terrain_name;
  public String graphic_alt;
  public String graphic_str;

	public int movement_cost;
	public int defense_bonus;

	public int food;
	public int shield;
	public int trade;

	public String special_1_name;
	public int graphic_special_1;
	public int food_special_1;
	public int shield_special_1;
	public int trade_special_1;

	public String special_2_name;
	public int graphic_special_2;
	public int food_special_2;
	public int shield_special_2;
	public int trade_special_2;

	public int road_trade_incr;
	public int road_time;

	public int irrigation_result;
	public int irrigation_food_incr;
	public int irrigation_time;

	public int mining_result;
	public int mining_shield_incr;
	public int mining_time;

	public int transform_result;
	public int transform_time;
	
	public String helptext;
	
	public class c_special
	{
	  public String graphic_str;
	  public String graphic_alt;
	}
	
	public c_special[] special = new c_special[2];

	public PktRulesetTerrain()
	{
	  super();
	}
	
	public PktRulesetTerrain(InStream in)
	{
	  super(in);
	}
	
	public void receive(InStream in)
	{
	  id = in.readUnsignedByte();
	  terrain_name = in.readZeroString();
	  movement_cost = in.readUnsignedByte();
	  defense_bonus = in.readUnsignedByte();
	  food = in.readUnsignedByte();
	  shield = in.readUnsignedByte();
	  trade = in.readUnsignedByte();
	  special_1_name = in.readZeroString();
	  food_special_1 = in.readUnsignedByte();
	  shield_special_1 = in.readUnsignedByte();
	  trade_special_1 = in.readUnsignedByte();
	  special_2_name = in.readZeroString();
	  food_special_2 = in.readUnsignedByte();
	  shield_special_2 = in.readUnsignedByte();
	  trade_special_2 = in.readUnsignedByte();
	  road_trade_incr = in.readUnsignedByte();
	  road_time = in.readUnsignedByte();
	  irrigation_result = in.readUnsignedByte();
	  irrigation_food_incr = in.readUnsignedByte();
	  irrigation_time = in.readUnsignedByte();
	  mining_result = in.readUnsignedByte();
	  mining_shield_incr = in.readUnsignedByte();
	  mining_time = in.readUnsignedByte();
	  transform_result = in.readUnsignedByte();
	  transform_time = in.readUnsignedByte();
	  graphic_str = in.readZeroString();
	  graphic_alt = in.readZeroString();
	  for (int i=0; i < 2; i++)
	  {
	    special[i] = new c_special();
	    special[i].graphic_str = in.readZeroString();
	    special[i].graphic_alt = in.readZeroString();
	  }
	
	  if (in.hasMoreData())
	  {
	    helptext = in.readZeroString();
	  }
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	
	}
}