package org.freeciv.net;
public class PktRulesetGovernment extends AbstractPacket
{
  public int id;
  public int required_tech;
  public int max_rate;
  public int civil_war;
  public int martial_law_max;
  public int martial_law_per;
  public int empire_size_mod;
  public int empire_size_inc;
  public int rapture_size;
  public int unit_happy_cost_factor;
  public int unit_shield_cost_factor;
  public int unit_food_cost_factor;
  public int unit_gold_cost_factor;
  public int free_happy;
  public int free_shield;
  public int free_food;
  public int free_gold;
  public int trade_before_penalty;
  public int shields_before_penalty;
  public int food_before_penalty;
  public int celeb_trade_before_penalty;
  public int celeb_shields_before_penalty;
  public int celeb_food_before_penalty;
  public int trade_bonus;
  public int shield_bonus;
  public int food_bonus;
  public int celeb_trade_bonus;
  public int celeb_shield_bonus;
  public int celeb_food_bonus;
  public int corruption_level;
  public int corruption_modifier;
  public int fixed_corruption_distance;
  public int corruption_distance_factor;
  public int extra_corruption_distance;
  public int flags;
  public int hints;
  public int num_ruler_titles;
  public String name;
  public String graphic_str;
  public String graphic_alt;
  public String helptext;
  public PktRulesetGovernment() 
  {
    super();
  }
  public PktRulesetGovernment( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    id = in.readUnsignedByte();
    required_tech = in.readUnsignedByte();
    max_rate = in.readUnsignedByte();
    civil_war = in.readUnsignedByte();
    martial_law_max = in.readUnsignedByte();
    martial_law_per = in.readUnsignedByte();
    empire_size_mod = in.readUnsignedByte();
    empire_size_inc = in.readUnsignedByte();
    rapture_size = in.readUnsignedByte();
    unit_happy_cost_factor = in.readUnsignedByte();
    unit_shield_cost_factor = in.readUnsignedByte();
    unit_food_cost_factor = in.readUnsignedByte();
    unit_gold_cost_factor = in.readUnsignedByte();
    free_happy = in.readUnsignedByte();
    free_shield = in.readUnsignedByte();
    free_food = in.readUnsignedByte();
    free_gold = in.readUnsignedByte();
    trade_before_penalty = in.readUnsignedByte();
    shields_before_penalty = in.readUnsignedByte();
    food_before_penalty = in.readUnsignedByte();
    celeb_trade_before_penalty = in.readUnsignedByte();
    celeb_shields_before_penalty = in.readUnsignedByte();
    celeb_food_before_penalty = in.readUnsignedByte();
    trade_bonus = in.readUnsignedByte();
    shield_bonus = in.readUnsignedByte();
    food_bonus = in.readUnsignedByte();
    celeb_trade_bonus = in.readUnsignedByte();
    celeb_shield_bonus = in.readUnsignedByte();
    celeb_food_bonus = in.readUnsignedByte();
    corruption_level = in.readUnsignedByte();
    corruption_modifier = in.readUnsignedByte();
    fixed_corruption_distance = in.readUnsignedByte();
    corruption_distance_factor = in.readUnsignedByte();
    extra_corruption_distance = in.readUnsignedByte();
    flags = in.readUnsignedByte();
    hints = in.readUnsignedByte();
    num_ruler_titles = in.readUnsignedByte();
    name = in.readZeroString();
    graphic_str = in.readZeroString();
    graphic_alt = in.readZeroString();
    if( in.hasMoreData() )
    {
      helptext = in.readZeroString();
    }
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
