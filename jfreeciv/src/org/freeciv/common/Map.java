package org.freeciv.common;

/**
 * Logical representation of information about the map
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

  private Game m_game;

  public static final int[] DIR_DX = new int[] 
  {
    -1, 0, 1, -1, 1, -1, 0, 1
  };

  public static final int[] DIR_DY = new int[]
  {
    -1, -1, -1, 0, 0, 1, 1, 1
  };

  public static final int[] DIR_DX2 = new int[]
  {
    0, 1, 1, 1, 0, -1, -1, -1
  };
  public static final int[] DIR_DY2 = new int[]
  {
    -1, -1, 0, 1, 1, 1, 0, -1
  };

  Map( Game game )
  {
    m_game = game;
    m_startPositions = new MapPosition[ MAX_NUM_NATIONS ];
    m_voidTile = new Tile( m_game );
  }

  public MapPosition normalizeMapPosition( int x, int y )
  {
    MapPosition mp = new MapPosition( x, y );
    if (normalizeMapPosition( mp ))
    {
      return mp;
    }
    else
    {
      return null;
    }
  }

  public boolean normalizeMapPosition( MapPosition mp )
  {
    if ( mp.y < 0 || mp.y > getHeight() )
    {
      return false;
    }

    mp.x %= getWidth();

    if ( mp.x < 0 )
    {
      mp.x += getWidth();
    }

    return true;
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
   * Is the map empty? The map is empty if it is size (0,0)
   */
  public boolean isEmpty()
  {
    return (getWidth() == 0 && getHeight() == 0);
  }

  /**
   * This should be called to allocate space for the tiles on the map.
   */
  public void allocate()
  {
    // map_allocate in map.c
    Logger.log( Logger.LOG_DEBUG, "map_allocate ("+getWidth()+", "+getHeight()+")" );

    synchronized( this )
    {
      m_tiles = new Tile[ getWidth() * getHeight() ];

      for (int y = 0; y < getHeight(); y++)
      {
        for (int x = 0; x < getWidth(); x++)
        {
          m_tiles[ getTileArrayIndex( x, y ) ] = new Tile( m_game );
        }
      }
    }
  }

  /**
   * Get the square of the distance between two points on the map
   * This is roughly equivalent to ((abs(x0-x1))^2 + (abs(y0-y1))^2)
   * with adjustments
   *
   * @param x0 the x coordinate of the first point
   * @param y0 the y coordinate of the first point
   * @param x1 the x coordinate of the second point
   * @param y1 the y coordinate of the second point
   *
   * @return the square of the absolute distance between the two points
   */
  public int getSquareMapDistance( int x0, int y0, int x1, int y1 )
  {
    int xd = getXDistance( x0, x1 );
    int yd = getYDistance( y0, y1 );
    return ( xd*xd + yd*yd );
  }

  /**
   * Get the distance (in tiles) along the x-axis between two points. Because
   * the map will wrap, this will return the minimum distance between the 
   * two points in either direction.
   *
   * @param x0 the first point
   * @param x1 the second point
   *
   * @return the minimum distance along the x axis between the two points
   *    in tiles, in either direction. equivalent to
   *    min( abs(x0-x1), (width-abs(x0-x1)) )
   */
  private int getXDistance( int x0, int x1 )
  {
    int dist = ( x0 > x1 ) ? x0-x1 : x1-x0;
    return Math.min( dist, getWidth()-dist );
  }

  /**
   * Get the distance (in tiles) along the y axis between two points.
   *
   * @param y0 the first point
   * @param y1 the second point
   *
   * @return the absolute distance along the y axis between the two points, 
   *    in tiles. equivalent to abs( y0-y1 )
   */
  private int getYDistance( int y0, int y1 )
  {
    return ( y0 > y1 ) ? y0 - y1 : y1 - y0;
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
    synchronized( this )
    {
      if (m_tiles == null)
      {
        throw new IllegalStateException(
          "Attempt to get tile before Map.allocate() has been called."
        );
      }

      if (y < 0 || y >= getHeight())
      {
        return m_voidTile;
      }

      x = adjustX( x );

      try
      {
        return m_tiles[ getTileArrayIndex( x, y ) ];
      }
      catch (ArrayIndexOutOfBoundsException aioobe)
      {
        System.err.println(" Attempted to get invalid tile at "+x+", "+y);
        System.err.println(" Map size is "+getWidth()+", "+getHeight());
        return m_voidTile;
      }
    }
  }

  /**
   * "wrap" the x coordinate
   */
  public int adjustX( int x )
  {
    // map_adjust_x in map.h
    return (  (x < 0) ? 
                ( x % getWidth() != 0 ? 
                  x % getWidth() + getWidth() : 0 ) :
                ( x >= getWidth() ?
                  x % getWidth() :
                  x )
    );
  }

  public int adjustY( int y )
  {
    return ( (y < 0) ? 0 : (( y >= getHeight() ) ? getHeight() - 1  : y ));
  }

  public boolean isSamePosition( int x, int y, int x1, int y1 )
  {
    return (adjustX( x ) == adjustX( x1 ) && adjustY( y ) == adjustY( y1 ) );
  }

  /**
   * @return true if the positions described are adjacent on the map.
   */
  public boolean isTilesAdjacent( int x, int y, int x1, int y1 )
  {
    //TODO
    return false;
  }

  /**
   * Get the city at the specified co-ordinates
   */
  public City getCity( int x, int y )
  {
    return getTile( x, y ).getCity();
  }

  public int getSpecial( int x, int y )
  {
    if ( y < 0 || y > getHeight() )
    {
      return S_NO_SPECIAL;
    }
    else
    {
      return getTile( x, y ).getSpecial();
    }
  }

  public int getTerrain( int x, int y )
  {
    if ( y < 0 || y > getHeight() )
    {
      return T_UNKNOWN;
    }
    return getTile( x, y ).getTerrain();
  }

  public void resetMoveCosts( int x, int y )
  {
      // TODO
  }


  /**
   * Iterate a city radius in map co-ordinates, skip non-existant squares
   *
   * @param startx the grid pos to start
   * @param starty the grid pos to start
   * @param iter the iterator
   */
  void iterateMapCityRadius( int startx, int starty, 
    MapPositionIterator iter )
  {
    // city.h: map_city_radius_iterate

    int x_itr, y_itr;
    int MCMI_x, MCMI_y;
    MapPosition mp = new MapPosition();

    for ( MCMI_x = 0;  MCMI_x < City.MAP_SIZE; MCMI_x++ )
    {
      for ( MCMI_y = 0; MCMI_y < City.MAP_SIZE; MCMI_y++ )
      {
        if ( iter.isFinished() ) return;
      
        if ( !( ( MCMI_x == 0 || MCMI_x == City.MAP_SIZE-1 ) &&
                ( MCMI_y == 0 || MCMI_y == City.MAP_SIZE-1 ) ) )
        {
          y_itr = startx + MCMI_y - CITY_MAP_SIZE/2;
          if ( y_itr < 0 || y_itr >= getHeight() )
          {
            continue;
          }
          x_itr = adjustX( startx + MCMI_x - City.MAP_SIZE/2 );
          mp.x = x_itr;
          mp.y = y_itr;
          iter.iteratePosition( mp );
        }
      }
    }
  }  

  /**
    * This iterates outwards from the starting point (Duh?).
    *    Every tile within max_dist will show up exactly once. (even takes
    *     into account wrap). All positions given correspond to real tiles.
    *    The values given are adjusted.
    *    You should make sure that the arguments passed to the macro are adjusted,
    *    or you could have some very nasty intermediate errors.
    * 
    * @param startX the start x-coordinate
    * @param startY the start y-coordinate
    * @param maxDist the maximum distance to travel out from the starting point
    *   in any direction
    *
    * @return an Iterator, the values of which are MapPosition instances. You
    *   should not modify the returned MapPosition if you intend to keep 
    *   iterating.
    * 
  */
  public void iterateOutwards( 
    int m_startX, int m_startY, int m_maxDist, MapPositionIterator i )
  {

    // iterate_outward in map.h
    MapPosition m_mapPos = new MapPosition( m_startX, m_startY );

    int m_x_itr;
    int m_y_itr;

    int m_max_dx = getWidth() / 2;
    int m_min_dx = -( m_max_dx - ( (getWidth() % 2 != 0) ? 0 : 1 ) );
    boolean m_xcycle = true;
    boolean m_positive = false; 
    int m_dxy = 0;
    int m_do_xy;

    while ( m_dxy <= m_maxDist )
    {
      for ( m_do_xy = -m_dxy; m_do_xy <= m_dxy; m_do_xy++ )
      {

        if ( m_xcycle )
        {
          m_x_itr = m_startX + m_do_xy;
          if ( m_positive )
          {
            m_y_itr = m_startY  + m_dxy;
          }
          else
          {
            m_y_itr = m_startY - m_dxy;
          }
        }
        else
        {
          if ( m_dxy == m_do_xy || m_dxy == - m_do_xy )
          {
            continue;
          }

          m_y_itr = m_startY + m_do_xy;
          if ( m_positive )
          {
            m_x_itr = m_startX + m_dxy;
          }
          else
          {
            m_x_itr = m_startX - m_dxy;
          }
        }

        if ( m_y_itr < 0  || m_y_itr >= getHeight() )
        {
          continue;
        }

        int dx = m_startX - m_x_itr;
        if ( dx > m_max_dx || dx < m_min_dx )
        {
          continue;
        }

        if ( m_x_itr >= getWidth() )
        {
          m_x_itr -= getWidth();
        }
        else if ( m_x_itr < 0 )
        {
          m_x_itr += getWidth();
        }

        if ( i.isFinished() )
        {
          return;
        }
        else
        {
          m_mapPos.x = m_x_itr;
          m_mapPos.y = m_y_itr;
          i.iteratePosition( m_mapPos );

          if ( i.isFinished() )
          {
            return;
          }
        }
      }

      if ( !m_positive )
      {
        if ( !m_xcycle )
        {
          m_dxy++;
        }
        m_xcycle = !m_xcycle;
      }
      m_positive = !m_positive;
    }
  }
 
  /**
   * from map.c:can_reclaim_ocean()
   * 
   * This function returns true if the tile at the given location can be
   * "reclaimed" from ocean into land.  This is the case only when there are
   * a sufficient number of adjacent tiles that are not ocean.
   */
  public boolean canReclaimOcean( int x, int y )
  {
    //TODO
    return false;
  }

  /**
   * from map.c:can_channel_land()
   * 
   * This function returns true if the tile at the given location can be
   * "channeled" from land into ocean.  This is the case only when there are
   * a sufficient number of adjacent tiles that are ocean.
   */
  public boolean canChannelLand( int x, int y )
  {
    //TODO
    return false;
  }

  public boolean isWaterAdjacentTo( int x, int y ) 
  {
    Tile tile = getTile( x, y );
    if ( tile.getTerrain() == T_OCEAN
        || tile.getTerrain() == T_RIVER
        || ( tile.getSpecial() & S_RIVER ) != 0
        || ( tile.getSpecial() & S_IRRIGATION ) != 0 )
    {
      return true;
    }
    //TODO: check adjacent squares
    return false;
  }
  
  private boolean m_cityTooNear;
  
  /**
   * Returns true if a city can be built at the specified position
   */
  public boolean canCityBeBuiltAt( int x, int y )
  {
    if ( getTerrain( x, y ) == T_OCEAN )
    {
      return false;
    }
    
    m_cityTooNear = false;

    m_game.getMap().iterateOutwards( x, y, 
      m_game.getGameRules().getMinDistanceBetweenCities() - 1, 
      new MapPositionIterator() 
      {
        public void iteratePosition( MapPosition pos )
        {
          
          if ( getCity( pos.x, pos.y ) != null )
          {
            m_cityTooNear = true;
            setFinished( true );
          }
        }
      }
    );

    return !m_cityTooNear;
  }
  
}
