package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetGovernment;
import org.freeciv.net.PktRulesetGovernmentRulerTitle;

/**
 * A freeciv government
 */
public class Government implements GameObject 
{
  private PktRulesetGovernment m_ruleset;
  private GameObjectFactory m_govFactory;
  private Advance m_requiredAdvance;

  private RulerTitle[] m_rulerTitles;

  Government(GameObjectFactory govFactory)
  {
    m_govFactory = govFactory;
  }

  public RulerTitle createRulerTitle(PktRulesetGovernmentRulerTitle ruleset)
  {
    RulerTitle r = new RulerTitle(m_govFactory.getParent(), this);
    r.initFromPacket(ruleset);
    m_rulerTitles[r.getId()] = r;

    return r;
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public void initFromPacket(Packet prg)
  {
    m_ruleset = (PktRulesetGovernment) prg;
    m_rulerTitles = new RulerTitle[getNumberOfRulerTitles()];
  }

  public RulerTitle getRulerTitle(int i)
  {
    return m_rulerTitles[i];
  }

  public Advance getRequiredAdvance()
  {
    if (m_requiredAdvance == null)
    {
      int advanceId = m_ruleset.required_tech;
      m_requiredAdvance = (Advance) 
        m_govFactory.getParent().getAdvanceFactory().findById(advanceId);
    }

    return m_requiredAdvance;
  }

  public int getMaxRate()
  {
    return m_ruleset.max_rate;
  }

  public int getCivilWar()
  {
    return m_ruleset.civil_war;
  }

  public int getMartialLawMax()
  {
    return m_ruleset.martial_law_max;
  }

  public int getMartialLawPer()
  {
    return m_ruleset.martial_law_per;
  }

  public int getEmpireSizeMod()
  {
    return m_ruleset.empire_size_mod;
  }

  public int getEmpireSizeInc()
  {
    return m_ruleset.empire_size_inc;
  }

  public int getRaptureSize()
  {
    return m_ruleset.rapture_size;
  }

  public int getUnitHappyCostFactor()
  {
    return m_ruleset.unit_happy_cost_factor;
  }

  public int getUnitShieldCostFactor()
  {
    return m_ruleset.unit_shield_cost_factor;
  }

  public int getUnitFoodCostFactor()
  {
    return m_ruleset.unit_food_cost_factor;
  }

  public int getUnitGoldCostFactor()
  {
    return m_ruleset.unit_gold_cost_factor;
  }

  public int getFreeHappy()
  {
    return m_ruleset.free_happy;  
  }

  public int getFreeShield()
  {
    return m_ruleset.free_shield;  
  }

  public int getFreeFood()
  {
    return m_ruleset.free_food;  
  }

  public int getFreeGold()
  {
    return m_ruleset.free_gold;  

  }

  public int getTradeBeforePenalty()
  {
    return m_ruleset.trade_before_penalty;  
  }

  public int getShieldsBeforePenalty()
  {
    return m_ruleset.shields_before_penalty;  
  }

  public int getFoodBeforePenalty()
  {
    return m_ruleset.food_before_penalty;  
  }

  public int getCelebTradeBeforePenalty()
  {
    return m_ruleset.celeb_trade_before_penalty;  
  }

  public int getCelebShieldsBeforePenalty()
  {
    return m_ruleset.celeb_shields_before_penalty;  
  }

  public int getCelebFoodBeforePenalty()
  {
    return m_ruleset.celeb_food_before_penalty;  
  }

  public int getTradeBonus()
  {
    return m_ruleset.trade_bonus;  
  }

  public int getShieldBonus()
  {
    return m_ruleset.shield_bonus;  
  }

  public int getFoodBonus()
  {
    return m_ruleset.food_bonus;  
  }

  public int getCelebTradeBonus()
  {
    return m_ruleset.celeb_trade_bonus;  
  }

  public int getCelebShieldBonus()
  {
    return m_ruleset.celeb_shield_bonus;  
  }

  public int getCelebFoodBonus()
  {
    return m_ruleset.celeb_food_bonus;  
  }

  public int getCorruptionLevel()
  {
    return m_ruleset.corruption_level;  
  }

  public int getCorruptionModifier()
  {
    return m_ruleset.corruption_modifier;  
  }

  public int getFixedCorruptionDistance()
  {
    return m_ruleset.fixed_corruption_distance;  
  }

  public int getCorruptionDistanceFactor()
  {
    return m_ruleset.corruption_distance_factor;  
  }

  public int getExtraCorruptionDistance()
  {
    return m_ruleset.extra_corruption_distance;  
  }

  public int getFlags()
  {
    return m_ruleset.flags;  
  }

  public int getHints()
  {
    return m_ruleset.hints;  
  }

  public int getNumberOfRulerTitles()
  {
    return m_ruleset.num_ruler_titles;  
  }

  public String getName()
  {
    return m_ruleset.name;  

  }

  public String toString()
  {
    return getName();
  }

  public String getGraphicStr()
  {
    return m_ruleset.graphic_str;
  }

  public String getGraphicAlt()
  {
    return m_ruleset.graphic_alt;  
  }
  
  
}