package org.freeciv.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freeciv.common.Assert;

/**
 * The client map class is responsible for creating MapView instances and for
 * keeping track of all active map views.
 *
 * There is always a main map view, but multiple map views can be created which
 * scroll independently etc.
 *
 * @author Brian Duff
 */
public final class MapViewManager
{
  private List m_mapViews;
  private Client m_client;
  private MapView m_mainView;

  /**
   * The client constructs an instance of this class at startup
   *
   * @param c the client
   */
  MapViewManager( Client c )
  {
    m_client = c;
    m_mapViews = new ArrayList();
  }

  /**
   * Refresh the map canvas of all views
   */
  void refreshTileMapCanvas( int x, int y )
  {
    Iterator i = iterator();
    while ( i.hasNext() )
    {
      ((MapView)i.next()).refreshTileMapCanvas( x, y );
    }
  }

  /**
   * Create or return the main map view
   *
   * @return the main map view
   */
  MapView getMainMapView()
  {
    if ( m_mainView == null )
    {
      MapView mv = createMapView();
      m_mainView = mv;
      m_mapViews.add( mv );
    }
    return m_mainView;
  }

  /**
   * Create a default map view
   */
  MapView createMapView()
  {
    MapView mv;
    
    if ( m_client.getTileSpec().isIsometric() )
    {
      mv = new IsometricMapView( m_client );
    }
    else
    {
      mv = new GridMapView( m_client );
    }

    m_mapViews.add( mv );
    
    return mv;
  }

  /**
   * Get an iterator over all map views, including the main map view
   *
   * @return an Iterator, the values of which are MapView
   */
  Iterator iterator()
  {
    return m_mapViews.iterator();
  }

  /**
   * Get a map view by index.
   *
   * @param idx the index of the map view. This index has no semantical meaning
   */
  MapView getMapView( int idx )
  {
    return (MapView) m_mapViews.get( idx );
  }

  /**
   * Remove a map view.
   *
   * @param mv the map view to remove
   */
  void removeMapView( MapView mv )
  {
    if ( mv == m_mainView )
    {
      // ? bother doing this - maybe the client is allowed to do this on
      // shutdown?
      Assert.fail( "You cannot remove the main map view" );
    }
    m_mapViews.remove( mv );
  }
  
}