package org.freeciv.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.freeciv.net.PktRulesetGame;

/**
 * A freeciv game.
 */
public final class Game 
{
  // game.h struct civ_game
  
  /** Including not yet established */
  private List m_allConnections;
  /** All established client connections */
  private List m_establishedConnections;
  /** Involved in game, send map etc. */
  private List m_gameConnections;

  /** Game ruleset. struct rgame in game.h */
  private GameRules m_rules;

  public Game()
  {
    m_allConnections = new ArrayList();
    m_establishedConnections = new ArrayList();
    m_gameConnections = new ArrayList();

    m_rules = new GameRules();
  }

  public Collection getAllConnections()
  {
    return m_allConnections;
  }

  public Collection getEstablishedConnections()
  {
    return m_establishedConnections;
  }

  public Collection getGameConnections()
  {
    return m_gameConnections;
  }

  public GameRules getGameRules()
  {
    return m_rules;
  }



  public static class GameRules
  {
    private PktRulesetGame m_rulesetPacket;

    public void init(PktRulesetGame pkt)
    {
      m_rulesetPacket = pkt;
    }
    
    public int getMinCityCenterFood()
    {
      return m_rulesetPacket.min_city_center_food;
    }

    public int getMinCityCenterShield()
    {
      return m_rulesetPacket.min_city_center_shield;
    }

    public int getMinCityCenterTrade()
    {
      return m_rulesetPacket.min_city_center_trade;
    }

    public int getMinDistanceBetweenCities()
    {
      return m_rulesetPacket.min_dist_bw_cities;
    }

    public int getInitialVisualRadius()
    {
      return m_rulesetPacket.init_vis_radius_sq;
    }

    public boolean isHutOverflightAllowed()
    {
      return m_rulesetPacket.hut_overflight != 0;
    }

    public int getPillageSelect()
    {
      return m_rulesetPacket.pillage_select;
    }

    public boolean isNuclearContaminationPossible()
    {
      return m_rulesetPacket.nuke_contamination != 0;
    }

    public int getInitialGranaryFood()
    {
      return m_rulesetPacket.granary_food_ini;
    }

    public int getGranaryFoodIncrement()
    {
      return m_rulesetPacket.granary_food_inc;
    }


  }
}