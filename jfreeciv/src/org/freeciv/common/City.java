package org.freeciv.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implement me.
 */
public final class City 
{
  private static HashMap s_cities = new HashMap();

  private List m_supportedUnits;
  private List m_presentUnits;
  private int m_id;
  private int m_x;
  private int m_y;

  // city.h: CITY_MAP_SIZE
  public static final int MAP_SIZE = 5;

  /**
   * Find the city with the specified id
   */
  public static City findById(int id)
  {
    return (City) s_cities.get( new Integer(id) );
  }

  public City(int id)
  {
    s_cities.put( new Integer( id ), this );
    m_supportedUnits = new ArrayList();
    m_presentUnits = new ArrayList();
    m_id = id;
  }

  public int getX()
  {
    return m_x;
  }

  public int getY()
  {
    return m_y;
  }

  public void clearAllSupportedUnits()
  {
    m_supportedUnits = new ArrayList();
  }

  public void addSupportedUnit( Unit u )
  {
    m_supportedUnits.add( u );
  }

  public void removeSupportedUnit( Unit u )
  {
    m_supportedUnits.remove( u );
  }

  public void clearAllPresentUnits()
  {
    m_presentUnits = new ArrayList();
  }

  public void addPresentUnit( Unit u )
  {
    m_presentUnits.add( u );
  }

  public int getPopulation()
  {
    return 0;
  }

  public Player getOwner()
  {
    return null;
  }

  public int getId()
  {
    return m_id;
  }

  public boolean hasWalls() 
  {
    // TODO
    return false;
  }

  public boolean isUnhappy() 
  {
    return false;
  }

  public int getSize()
  {
    return 1;// todo
  }

  public CityStyle getStyle()
  {
    return null;
  }

  
}