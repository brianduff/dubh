package org.freeciv.common;

import java.util.Iterator;
import java.util.List;

/**
 * A tile on the freeciv map
 */
public class Tile 
{
  private int m_type;
  private int m_specialType;
  private City m_city;
  private List m_units;
  private int m_known;
  // sent - not used on client
  private int m_assigned;
  private List m_worked;  // city working tile, or null
  private int m_continent;
  private char[] m_moveCost; //?

  Tile()
  {
    m_type = CommonConstants.T_UNKNOWN; 
  //  m_special = null; // S_NO_SPECIAL
    m_city = null;
    m_worked = null;
    m_assigned = 0;
    m_known = CommonConstants.TILE_UNKNOWN;
    // todo unit_list_init().
  }

  public boolean isKnown()
  {
    return m_known >= CommonConstants.TILE_KNOWN_FOGGED;
  }

  public int getKnown()
  {
    return m_known;
  }

  public void setKnown(int knownState)
  {
    m_known = knownState;
  }

  public boolean hasCity()
  {
    return m_city != null;
  }

  public City getCity()
  {
    return m_city;
  }

  public Iterator getUnits()
  {
    return m_units.iterator();
  }

  public boolean hasVisibleUnit()
  {
    return false;
  }

  public Unit getVisibleUnit()
  {
    return null;
  }

  public int getTerrain()
  {
    return m_type;
  }

  public void setTerrain(int terrain)
  {
    m_type = terrain;
  }

  public int getSpecial()
  {
    return m_specialType;
  }

  public void setSpecial(int i)
  {
    m_specialType = i;
  }

  public int getContinent()
  {
    return m_continent;
  }

  public void setContinent( int continent )
  {
    m_continent = continent;
  }
}