package org.freeciv.common;

import javax.swing.Icon;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetNation;

/**
 * A nation in freeciv
 */
public class Nation implements GameObject
{
  private GameObjectFactory m_nationFactory;
  private PktRulesetNation m_ruleset;
  private Icon m_flagSprite;

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

  public Icon getFlagSprite()
  {
    return m_flagSprite;
  }

  public void setFlagSprite( Icon i )
  {
    m_flagSprite = i;
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
  
  /**
   * Nation is equal to any other object if the object is also a nation
   * and it has the same id.
   */
  public boolean equals( Object o )
  {
    return o instanceof Nation && ( (Nation)o ).getId() == getId();
  }
  
}