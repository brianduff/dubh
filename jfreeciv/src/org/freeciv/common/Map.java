package org.freeciv.common;

/**
 * Logical representation of information abou the map
 */
public final class Map implements CommonConstants
{
  // struct civ_map in map.h

  private int m_xsize;
  private int m_ysize;
  private int m_seed;
  private int m_riches;
  private boolean m_isEarth;
  private int m_huts;
  private int m_landPercent;
  private int m_grassSize;
  private int m_swampSize;
  private int m_deserts;
  private int m_mountains;
  private int m_riverLength;
  private int m_forestSize;
  private int m_generator;
  private int m_numStartPositions;
  private int m_fixedStartPositions;
  private int m_haveSpecials;
  private int m_numContinents;
  private Tile[] m_tiles;
  private MapPosition[] m_startPositions;
  private Tile m_voidTile;

  Map()
  {
    m_startPositions = new MapPosition[ MAX_NUM_NATIONS ];
    m_voidTile = new Tile();
  }

  public void setWidth(int xsize)
  {
    m_xsize = xsize;
  }

  public int getWidth()
  {
    return m_xsize;
  }

  public void setHeight(int ysize)
  {
    m_ysize = ysize;
  }

  public int getHeight()
  {
    return m_ysize;
  }

  public void setEarth(boolean isEarth)
  {
    m_isEarth = isEarth;
  }

  public boolean isEarth()
  {
    return m_isEarth;
  }

  /**
   * This should be called to allocate space for the tiles on the map.
   */
  public void allocate()
  {
    // map_allocate in map.c
    Logger.log( Logger.LOG_DEBUG, "map_allocate ("+getWidth()+", "+getHeight()+")" );

    m_tiles = new Tile[ getWidth() * getHeight() ];

    for (int y = 0; y < getHeight(); y++)
    {
      for (int x = 0; x < getWidth(); x++)
      {
        m_tiles[ getTileArrayIndex( x, y ) ] = new Tile();
      }
    }
  }

  private int getTileArrayIndex( int x, int y ) 
  {
    return adjustX( x ) + (y * getWidth());
  }

  /**
   * Get the specified tile.
   */
  public Tile getTile( int x, int y )
  {
    if (m_tiles == null)
    {
      throw new IllegalStateException(
        "Attempt to get tile before Map.allocate() has been called."
      );
    }

    if (y < 0 || y > getHeight())
    {
      return m_voidTile;
    }

    return m_tiles[ getTileArrayIndex( x, y ) ];
  }

  /**
   * "wrap" the x coordinate
   */
  public int adjustX( int x )
  {
    // map_adjust_x in map.h
    return ( (x < 0) ? x + getWidth() : ( (x >= getWidth()) ? x - getWidth() : x ) );
  }

  public int adjustY( int y )
  {
    return ( (y < 0) ? 0 : (( y >= getHeight() ) ? getHeight() - 1  : y ));
  }

  
}