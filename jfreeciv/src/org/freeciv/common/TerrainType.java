package org.freeciv.common;

import javax.swing.Icon;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetTerrain;

/**
 * Terrain
 */
public class TerrainType implements GameObject
{

  // the tile_type struct in map.h
  
  private GameObjectFactory m_terrainFactory;

  private PktRulesetTerrain m_ruleset;

  private Icon[] m_icons = new Icon[16];    // was 8. hmm.
  private Icon[] m_specialIcons = new Icon[2];
  
  TerrainType(GameObjectFactory terrainFactory)
  {
    m_terrainFactory = terrainFactory;
  }

  public void initFromPacket(Packet p)
  {
    m_ruleset = (PktRulesetTerrain) p;
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public String getName()
  {
    return m_ruleset.terrain_name;
  }

  public String getGraphicStr()
  {
    return m_ruleset.graphic_str;
  }

  public String getGraphicAlt()
  {
    return m_ruleset.graphic_alt;
  }

  public int getMovementCost()
  {
    return m_ruleset.movement_cost;
  }

  public int getDefenseBonus()
  {
    return m_ruleset.defense_bonus;
  }

  public int getFood()
  {
    return m_ruleset.food;
  }

  public int getShield()
  {
    return m_ruleset.shield;
  }

  public int getTrade()
  {
    return m_ruleset.trade;
  }

  // Sigh, mainly to avoid having minging method names...>

  public String getSpecialName(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");
    return number == 0 ? m_ruleset.special_1_name : m_ruleset.special_2_name;
  }

  public int getFoodSpecial(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");
        
    return number == 0 ? m_ruleset.food_special_1 : m_ruleset.food_special_2;
  }

  public int getShieldSpecial(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");
        
    return number == 0 ? m_ruleset.shield_special_1 : m_ruleset.shield_special_2;
  }

  public int getTradeSpecial(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");
        
    return number == 0 ? m_ruleset.trade_special_1 : m_ruleset.trade_special_2;
  }

  public String getSpecialGraphicStr(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");

    return m_ruleset.special[number].graphic_str;
  } 

  public String getSpecialGraphicAlt(int number)
  {
    if (number != 0 && number != 1) 
      throw new IllegalArgumentException("Must be 0 or 1");

    return m_ruleset.special[number].graphic_alt;
  }

  public int getRoadTime()
  {
    return m_ruleset.road_time;
  }

  public int getRoadTradeIncr()
  {
    return m_ruleset.road_trade_incr;
  }

  public int getIrrigationResult()
  {
    return m_ruleset.irrigation_result;
  }

  public int getIrrigationFoodIncr()
  {
    return m_ruleset.irrigation_food_incr;
  }

  public int getIrrigationTime()
  {
    return m_ruleset.irrigation_time;
  }

  public int getMiningResult()
  {
    return m_ruleset.mining_result;
  }

  public int getMiningShieldIncr()
  {
    return m_ruleset.mining_shield_incr;
  }

  public int getMiningTime()
  {
    return m_ruleset.mining_time;
  }

  public int getTransformResult() // terrain?
  {
    return m_ruleset.transform_result;
  }

  public int getTransformTime()
  {
    return m_ruleset.transform_time;
  }

  public String getHelpText()
  {
    return m_ruleset.helptext;
  }

  public void setSprite( int index, Icon icon )
  {
    m_icons[ index ] = icon;
  }

  public Icon getSprite( int index )
  {
    return m_icons[ index ];
  }

  public void setSpecialSprite( int index, Icon icon )
  {
    m_specialIcons[ index ] = icon;
  }

  public Icon getSpecialSprite( int index )
  {
    return m_specialIcons[ index ];
  }
}