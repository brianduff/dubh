package org.freeciv.common;

import org.freeciv.net.ImprovementEffect;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetBuilding;

/**
 * A building type in freeciv
 *
 * @author Brian.Duff@dubh.org
 */
public class Building implements GameObject
{
  private GameObjectFactory m_buildingFactory;

  private PktRulesetBuilding m_ruleset;

  private Advance m_requiredAdvance;
  private Building m_requiredBuilding;
  private Advance m_obsoletedBy;


  Building(GameObjectFactory buildingFactory)
  {
    m_buildingFactory = buildingFactory;
  }

  public void initFromPacket(Packet p)
  {
    m_ruleset = (PktRulesetBuilding) p;
  }

  public String getName()
  {
    return m_ruleset.name;
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public Advance getRequiredAdvance()
  {
    if (m_requiredAdvance == null)
    {
      m_requiredAdvance = (Advance)
        m_buildingFactory.getParent().getAdvanceFactory().findById(
          m_ruleset.tech_req
        );
    }
    return m_requiredAdvance;
  }

  public Building getRequiredBuilding()
  {
    if (m_requiredBuilding == null)
    {
      m_requiredBuilding = (Building)
        m_buildingFactory.findById(m_ruleset.bldg_req);
    }
    return m_requiredBuilding;
  }

  /** ? */
  public int[] getTerrGate()
  {
    return m_ruleset.terr_gate;
  }

  /** ? */
  public int[] getSpecGate()
  {
    return m_ruleset.spec_gate;
  }

  public int getEquivRange()
  {
    return m_ruleset.equiv_range;
  }

  public int[] getEquivDupl()
  {
    return m_ruleset.equiv_dupl;
  }

  public int[] getEquivRepl()
  {
    return m_ruleset.equiv_repl;
  }

  public Advance getObsoletedBy()
  {
    if (m_obsoletedBy == null)
    {
      m_obsoletedBy = (Advance)
        m_buildingFactory.getParent().getAdvanceFactory().findById(
          m_ruleset.obsolete_by
        );
    }
    return m_obsoletedBy;
  }

  public boolean isWonder()
  {
    return m_ruleset.is_wonder;
  }

  /** 
   * Is this improvement an obsolete wonder?
   *
   * @return true if this improvement is a wonder and is obsolete
   */
  public boolean isWonderObsolete()
  {
    // TODO: Unsure
    return false;
  }

  public int getBuildCost()
  {
    return m_ruleset.build_cost;
  }

  public int getUpkeep()
  {
    return m_ruleset.upkeep;
  }

  public int getSabotage()
  {
    return m_ruleset.sabotage;
  }

  // FIXME
  public ImprovementEffect[] getEffects()
  {
    return m_ruleset.effect;
  } 

  public int getVariant()
  {
    return m_ruleset.variant;
  }

  public String getHelpText()
  {
    return m_ruleset.helptext;
  }
  
}