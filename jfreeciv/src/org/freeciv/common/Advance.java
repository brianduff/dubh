package org.freeciv.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetTech;

/**
 * Represents an "advance" (i.e. technology) in freeciv.
 */
public class Advance implements GameObject
{

  private PktRulesetTech m_ruleset;
  private Factories m_factories;

  private List m_required;
  private GameObjectFactory m_factory;

  Advance(GameObjectFactory advanceFactory)
  {
    m_factory = advanceFactory;
  }

  /**
   * Initialize this objec from the specified packet
   *
   * @param rtech a technology ruleset packet
   */
  public void initFromPacket(Packet rtech)
  {
    m_ruleset = (PktRulesetTech) rtech;
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public String getName()
  {
    return m_ruleset.name;
  }

  public String getOriginalName()
  {
    return "";    // ??
  }

  /**
   * Get all advances required for this advance.
   *
   * @return an Interator, the values of which are Advances
   */
  public Iterator getRequiredAdvances()
  {
    if (m_required == null)
    {
      m_required = new ArrayList(2);
      for (int i=0; i < m_ruleset.req.length; i++)
      {
        int techId = m_ruleset.req[i];
        if (techId != -1)
        {
          m_required.add( (Advance) 
            m_factory.getParent().getAdvanceFactory().findById(techId) 
          );
        }
      }
    }

    return m_required.iterator();
  }

  public int getFlags()
  {
    return m_ruleset.flags;
  }

  public String getHelpText()
  {
    return m_ruleset.helpText;
  }
}