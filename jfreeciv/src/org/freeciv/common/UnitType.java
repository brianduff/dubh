package org.freeciv.common;

import javax.swing.Icon;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetUnit;

/**
 * Represents a unit in freeciv
 */
public class UnitType implements GameObject
{

  private GameObjectFactory m_unitFactory;
  private PktRulesetUnit m_ruleset;

  private Advance m_techRequirement = null;
  private Advance m_obsoletedBy = null;

  private Icon m_sprite;
  
  UnitType(GameObjectFactory unitFactory)
  {
    m_unitFactory = unitFactory;
  }

  public Icon getSprite()
  {
    return m_sprite;
  }

  public void setSprite( Icon i )
  {
    m_sprite = i;
  }

  public void initFromPacket(Packet p)
  {
    m_ruleset = (PktRulesetUnit)p;  
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public String getName()
  {
    return m_ruleset.name;
  }

  public String getGraphicStr()
  {
    return m_ruleset.graphic_str;
  }

  public String getGraphicAlt()
  {
    return m_ruleset.graphic_alt;
  }

  public int getMoveType()
  {
    return m_ruleset.move_type;
  }

  public int getBuildCost()
  {
    return m_ruleset.build_cost;
  }

  public int getAttackStrength()
  {
    return m_ruleset.attack_strength;
  }

  public int getDefenseStrength()
  {
    return m_ruleset.defense_strength;
  }

  public int getMoveRate()
  {
    return m_ruleset.move_rate;

  }

  /**
   * Changed from getTechRequirement() for consistancy with Building -- BenM
   */
  public Advance getRequiredAdvance()
  {
    if (m_techRequirement == null)
    {
      m_techRequirement = (Advance)
        m_unitFactory.getParent().getAdvanceFactory().findById(
          m_ruleset.tech_requirement
        );
    }
    return m_techRequirement;
  }

  public int getRequiredAdvanceId()
  {
    return m_ruleset.tech_requirement;
  }

  public int getVisionRange()
  {
    return m_ruleset.vision_range;
  }

  public int getTransportCapacity()
  {
    return m_ruleset.transport_capacity;
  }

  public int getHitPoints()
  {
    return m_ruleset.hp;
  }

  public int getFirepower()
  {
    return m_ruleset.firepower;
  }

  public Advance getObsoletedBy()
  {
    if (m_obsoletedBy == null)
    {
      m_obsoletedBy = (Advance)
        m_unitFactory.getParent().getAdvanceFactory().findById(
          m_ruleset.obsoleted_by
        );
    }
    return m_obsoletedBy;
  }

  public int getFuel()
  {
    return m_ruleset.fuel;
  }

  public int getFlags()
  {
    return m_ruleset.flags;

  }

  public int getRoles()
  {
    return m_ruleset.roles;
  }

  public int getHappyCost()
  {
    return m_ruleset.happy_cost;
  }

  public int getShieldCost()
  {
    return m_ruleset.shield_cost;
  }

  public int getFoodCost()
  {
    return m_ruleset.food_cost;
  }

  public int getGoldCost()
  {
    return m_ruleset.gold_cost;
  }

  public int getParatroopersRange()
  {
    return m_ruleset.paratroopers_range;
  }

  public int getParatroopersMrReq()
  {
    return m_ruleset.paratroopers_mr_req;
  }

  public int getParatroopersMrSub()
  {
    return m_ruleset.paratroopers_mr_sub;
  }

  public String getHelpText()
  {
    return m_ruleset.helptext;
  }
}