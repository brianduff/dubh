package org.freeciv.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tile on the freeciv map
 */
public final  class Tile 
{
  private int m_type;
  private int m_specialType;
  private City m_city;
  private List m_units;
  private int m_known;
  // sent - not used on client
  private int m_assigned;
  private City m_worked;  // city working tile, or null
  private int m_continent;
  private char[] m_moveCost; //?

  private Object m_data;

  private Game m_game;

  Tile(Game g)
  {
    m_type = CommonConstants.T_UNKNOWN; 
  //  m_special = null; // S_NO_SPECIAL
    m_city = null;
    m_worked = null;
    m_assigned = 0;
    m_known = CommonConstants.TILE_UNKNOWN;
    // todo unit_list_init().
    m_units = new ArrayList();

    m_game = g;
  }

  public Object getData()
  {
    return m_data;
  }

  public void setData(Object o)
  {

  }

  public void addUnit( Unit u )
  {
    m_units.add( u );
  }

  public void removeUnit( Unit u )
  {
    m_units.remove( u );
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

  public void setCity( City c )
  {
    m_city = c;
  }

  /**
   * Is there at least one unit on this tile?
   */
  public boolean hasUnits()
  {
    return (m_units.size() > 0);
  }

  /**
   * Is there a stack of (i.e. two or more) units on this tile?
   */
  public boolean hasUnitStack()
  {
    return (m_units.size() > 1);
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

  /**
   * Get the terrain type for this tile
   */
  public TerrainType getTerrainType()
  {
    return (TerrainType)
      m_game.getFactories().getTerrainTypeFactory().findById( getTerrain() );
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

  public boolean isPolluted()
  {
    return ( getSpecial() & CommonConstants.S_POLLUTION ) != 0;
  }

  public boolean hasFallout()
  {
    return ( getSpecial() & CommonConstants.S_FALLOUT ) != 0;
  }

  /**
   * Mark this tile as being worked by the specified city
   *
   * @param c a city which is working this tile. Can be null to clear the
   *    working city.
   */
  public void setWorkedBy( City c )
  {
    m_worked = c;
  }

  /**
   * Gets the city which is working this tile
   *
   * @return a city which is working this tile, or null if no city is working
   *    it
   */
  public City getWorkedBy( )
  {
    return m_worked;
  }
}