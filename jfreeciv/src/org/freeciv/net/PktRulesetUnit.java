package org.freeciv.net;

import org.freeciv.common.CommonConstants;

public class PktRulesetUnit extends AbstractPacket
{
  public int id;
  public String name;
  public int move_type;
  public int build_cost;
  public int attack_strength;
  public int defense_strength;
  public int move_rate;
  public int tech_requirement; 
  public int vision_range;
  public int transport_capacity;
  public int hp;
  public int firepower;
  public int obsoleted_by;
  public int fuel;
  public int flags;
  public int roles;
  public int happy_cost;
  public int shield_cost;
  public int food_cost;
  public int gold_cost;
  public String graphic_str;
  public String graphic_alt;
  public int paratroopers_range;
  public int paratroopers_mr_req;
  public int paratroopers_mr_sub;
  public String helptext;
  
  public PktRulesetUnit( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  
  public PktRulesetUnit() 
  {
    super();
  }
  
  public void receive( InStream in ) throws NetworkProtocolException
  {
    id = in.readUnsignedByte();
    move_type = in.readUnsignedByte();
    build_cost = in.readShort();
    attack_strength = in.readUnsignedByte();
    defense_strength = in.readUnsignedByte();
    move_rate = in.readUnsignedByte();
    tech_requirement = in.readUnsignedByte();
    vision_range = in.readUnsignedByte();
    transport_capacity = in.readUnsignedByte();
    hp = in.readUnsignedByte();
    firepower = in.readUnsignedByte();
    obsoleted_by = in.readUnsignedByte();
    if( obsoleted_by > 127 )
    {
      obsoleted_by -= 256;
    }
    fuel = in.readUnsignedByte();
    flags = in.readInt();
    roles = in.readInt();
    happy_cost = in.readUnsignedByte();
    shield_cost = in.readUnsignedByte();
    food_cost = in.readUnsignedByte();
    gold_cost = in.readUnsignedByte();
    name = in.readZeroString();
    graphic_str = in.readZeroString();
    graphic_alt = in.readZeroString();
    if( ( flags & ( 1L << CommonConstants.F_PARATROOPERS ) ) != 0 ) // check this
    {
      paratroopers_range = in.readShort();
      paratroopers_mr_req = in.readUnsignedByte();
      paratroopers_mr_sub = in.readUnsignedByte();
    }
    else
    {
      paratroopers_range = paratroopers_mr_req = paratroopers_mr_sub = 0;
    }
    if( in.hasMoreData() )
    {
      helptext = in.readZeroString();
    }
  }
  
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
  
  public String toString()
  {
    return name;
  
  /*"" + attack_strength + "/" + defense_strength + "/" +
  (move_rate/3) + " " +  name + "   " + build_cost;
  */
  }

  // TODO: Move these to Unit.java
  
  public boolean isSettler()
  {
    return ( flags & ( 1 << CommonConstants.F_SETTLERS ) ) != 0;
  }
  
  public boolean isMilitary()
  {
    return ( flags & ( 1 << CommonConstants.F_NONMIL ) ) == 0;
  }
  public boolean isGroundMoving()
  {
    return move_type == CommonConstants.LAND_MOVING;
  }

  protected boolean unitFlag( int flag )
  {
    return ( ( flags & ( 1 << flag ) ) != 0 );
  }
}
