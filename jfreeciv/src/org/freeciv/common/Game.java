package org.freeciv.common;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import org.freeciv.net.PktRulesetGame;
import org.freeciv.net.PktRulesetControl;
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

  /** Terrain rules. This really belongs on Map */
  private TerrainRules m_terrainRules;

  /** Ruleset control. Used for various properties */
  private PktRulesetControl m_rulesetControl;

  
  public Game() 
  {
    m_allConnections = new ArrayList();
    m_establishedConnections = new ArrayList();
    m_gameConnections = new ArrayList();
    m_rules = new GameRules();
    m_terrainRules = new TerrainRules();
  }

  public TerrainRules getTerrainRules()
  {
    return m_terrainRules;
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

  /**
   * Call this to initialize the parts of Game that are configured using 
   * rulesetControl packets.
   *
   * @param prc
   */
  public void setRulesetControl(PktRulesetControl prc)
  {
    m_rulesetControl = prc;
  }

  /**
   * Get the city size which requires aqueducts
   */
  public int getAqueductSize()
  {
    return m_rulesetControl.aqueduct_size;
  }

  /**
   * Get the city size that requires sewers
   */
  public int getSewerSize()
  {
    return m_rulesetControl.sewer_size;
  } 

  public int getAddToSizeLimit()
  {
    return m_rulesetControl.add_to_size_limit;
  }

  public int getBonusTech()
  {
    return m_rulesetControl.rtech.get_bonus_tech;
  }

  public int getCathedralPlus()
  {
    return m_rulesetControl.rtech.cathedral_plus;
  }

  public int getCathedralMinus()
  {
    return m_rulesetControl.rtech.cathedral_minus;
  }

  public int getColosseumPlus()
  {
    return m_rulesetControl.rtech.colosseum_plus;
  }

  public int getTemplePlus()
  {
    return m_rulesetControl.rtech.temple_plus;
  }

  public boolean isPartisanRequired(int techId) // Technology
  {
    return (m_rulesetControl.rtech.partisan_req[techId] != 0);
  }

  /** 
   * How many governments are there?
   */
  public int getGovernmentCount()
  {
    return m_rulesetControl.government_count;
  }

  /**
   * Which government is "anarchy"
   */
  public int getGovernmentWhenAnarchy() // return Government
  {
    return m_rulesetControl.government_when_anarchy;
  }

  /**
   * Which government is the default government
   */
  public int getDefaultGovernment() // return Government
  {
    return m_rulesetControl.default_government;
  }

  public int getNumberOfUnitTypes()
  {
    return m_rulesetControl.num_unit_types;
  }

  public int getNumberOfImprovementTypes()
  {
    return m_rulesetControl.num_impr_types;
  }

  public int getNumberOfTechnologyTypes()
  {
    return m_rulesetControl.num_tech_types;
  }

  public int getNationCount()
  {
    return m_rulesetControl.nation_count;
  }

  public int getPlayableNationCount()
  {
    return m_rulesetControl.playable_nation_count;
  }

  public int getCityStyleCount()
  {
    return m_rulesetControl.style_count;
  }
  
  public static class GameRules
  {
    private PktRulesetGame m_rulesetPacket;
    public void init( PktRulesetGame pkt )
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
