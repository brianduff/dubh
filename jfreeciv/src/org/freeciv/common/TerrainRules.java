package org.freeciv.common;

import org.freeciv.net.PktRulesetTerrainControl;

/**
 * Game rules pertaining to terrain.
 */
public class TerrainRules 
{
  private PktRulesetTerrainControl m_ruleset;

  // matches the terrain_misc struct in common/map.h
  public TerrainRules()
  {
  }

  public void initFromPacket(PktRulesetTerrainControl terrain)
  {
    m_ruleset = terrain;
  }

  public int getRiverStyle()
  {
    return m_ruleset.river_style;
  }

  public boolean isRoadAllowed()
  {
    return m_ruleset.may_road;
  }

  public boolean isIrrigateAllowed()
  {
    return m_ruleset.may_irrigate;
  }

  public boolean isMineAllowed()
  {
    return m_ruleset.may_mine;
  }

  public boolean isTransformAllowed()
  {
    return m_ruleset.may_transform;
  }

  public int getOceanReclaimRequirement()
  {
    return m_ruleset.ocean_reclaim_requirement;
  }

  public int getLandChannelRequirement()
  {
    return m_ruleset.land_channel_requirement;
  }

  public int getRiverMoveMode()
  {
    return m_ruleset.river_move_mode;
  }

  public int getRiverDefenseBonus()
  {
    return m_ruleset.river_defense_bonus;
  }

  public int getRiverTradeIncr()
  {
    return m_ruleset.river_trade_incr;
  }

  public String getRiverHelpText()
  {
    return m_ruleset.river_help_text;
  }

  public int getFortressDefenseBonus()
  {
    return m_ruleset.fortress_defense_bonus;
  }

  public int getRoadSuperhighwayTradeBonus()
  {
    return m_ruleset.road_superhighway_trade_bonus;
  }

  public int getRailFoodBonus()
  {
    return m_ruleset.rail_food_bonus;
  }

  public int getRailShieldBonus()
  {
    return m_ruleset.rail_shield_bonus;
  }

  public int getRailTradeBonus()
  {
    return m_ruleset.rail_trade_bonus;
  }

  public int getFarmlandSupermarketFoodBonus()
  {
    return m_ruleset.farmland_supermarket_food_bonus;
  }

  public int getPollutionFoodPenalty()
  {
    return m_ruleset.pollution_food_penalty;
  }

  public int getPollutionShieldPenalty()
  {
    return m_ruleset.pollution_shield_penalty;
  }

  public int getPollutionTradePenalty()
  {
    return m_ruleset.pollution_trade_penalty;
  }

  public int getFalloutFoodPenalty()
  {
    return m_ruleset.fallout_food_penalty;
  }

  public int getFalloutShieldPenalty()
  {
    return m_ruleset.fallout_shield_penalty;
  }

  public int getFalloutTradePenalty()
  {
    return m_ruleset.fallout_trade_penalty;
  }
}