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

  Map()
  {
    m_startPositions = new MapPosition[ MAX_NUM_NATIONS ];
    m_voidTile = new Tile();
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
          m_tiles[ getTileArrayIndex( x, y ) ] = new Tile();
        }
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
    synchronized( this )
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
 

  
}