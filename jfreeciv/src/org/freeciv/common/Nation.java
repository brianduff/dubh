package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetNation;

/**
 * A nation in freeciv
 */
public class Nation implements GameObject
{
  private GameObjectFactory m_nationFactory;
  private PktRulesetNation m_ruleset;

  private CityStyle m_cityStyle = null;

  Nation(GameObjectFactory factory)
  {
    m_nationFactory = factory;
  }

  public void initFromPacket(Packet p)
  {
    m_ruleset = (PktRulesetNation) p;
  }

  public int getId()
  {
    return m_ruleset.id;
  }

  public String getName()
  {
    return m_ruleset.name;
  }

  public String toString()
  {
    return getName();
  }

  public String getPluralName()
  {
    return m_ruleset.name_plural;
  }

  public String getFlagGraphicStr()
  {
    return m_ruleset.graphic_str;
  }

  public String getFlagGraphicAlt()
  {
    return m_ruleset.graphic_alt;
  }

  public int getLeaderCount()
  {
    return m_ruleset.leader_count;
  }

  public String getLeaderName(int i)
  {
    try
    {
      return m_ruleset.leader_name[i];
    }
    catch (ArrayIndexOutOfBoundsException ai)
    {
      throw new IllegalArgumentException("Unrecognized leader "+i);
    }
  }

  public boolean isLeaderMale(int i)
  {
    try
    {
      return m_ruleset.leader_sex[i];
    }
    catch (ArrayIndexOutOfBoundsException ai)
    {
      throw new IllegalArgumentException("Unrecognized leader "+i);
    }
  }

  public CityStyle getCityStyle()
  {
    if (m_cityStyle == null)
    {
      m_cityStyle = (CityStyle)
        m_nationFactory.getParent().getCityStyleFactory().findById(
          m_ruleset.city_style
        );
    }

    return m_cityStyle;
  }
  
}