package org.freeciv.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implement me.
 */
public final class City implements CommonConstants
{
  private static HashMap s_cities = new HashMap();

  private List m_supportedUnits;
  private List m_presentUnits;
  private int m_id;
  private int m_x;
  private int m_y;

  // city.h: CITY_MAP_SIZE
  public static final int MAP_SIZE = 5;

  private Game m_game;

  /**
   * Find the city with the specified id
   */
  public static City findById(int id)
  {
    return (City) s_cities.get( new Integer(id) );
  }

  public City(Game g, int id)
  {
    m_game = g;
    s_cities.put( new Integer( id ), this );
    m_supportedUnits = new ArrayList();
    m_presentUnits = new ArrayList();
    m_id = id;
  }

  public String getName()
  {
    return "";
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

  /**
   * Returns true if this city has the speicifed building (B_*)
   *
   * @param a B_ constant referring to a type of building
   * @return true if the specified building exists in this city
   */
  public boolean hasBuilding( int id )
  {
    if ( !improvementExists( id ) )
    {
      return false;
    }
    return false;   // TODO
  }

  private boolean improvementExists( int id )
  {
    // improvement.c: improvement_exists()
  
    if ( id < 0 || id >= B_LAST || id >= m_game.getNumberOfImprovementTypes() )
    {
      return false;
    }

    if ( (id == B_SCOMP || id == B_SMODULE || id == B_SSTRUCTURAL )
         && !m_game.isSpaceRace() )
    {
      return false;
    }

    // TODO
    return true;
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