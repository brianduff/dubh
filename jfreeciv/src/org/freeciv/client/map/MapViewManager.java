package org.freeciv.client.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;

import org.freeciv.client.Client;
import org.freeciv.common.Assert;
import org.freeciv.client.map.grid.GridMapView;
import org.freeciv.client.map.iso.IsometricMapView;

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

  private static final boolean DEBUG_MODE = false;

  /**
   * The client constructs an instance of this class at startup
   *
   * @param c the client
   */
  public MapViewManager( Client c )
  {
    m_client = c;
    m_mapViews = new ArrayList();
  }

  /**
   * Refresh the map canvas of all views
   */
  public void refreshTileMapCanvas( final int x, final int y )
  {
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        Iterator i = iterator();
        while ( i.hasNext() )
        {
          ((MapView)i.next()).refreshTileMapCanvas( x, y );
        }
        m_client.getMainWindow().getMapOverview().refresh( x, y );        
      }
    });
  }

  public void updateMapBuffer( final int tilex, final int tiley, 
    final int tilew, final int tileh, final boolean repaint )
  {
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        Iterator i = iterator();
        while ( i.hasNext() )
        {
          ((MapView)i.next()).updateMapBuffer( 
            tilex, tiley, tilew, tileh, repaint 
          );
        }
        m_client.getMainWindow().getMapOverview().refresh( tilex, tiley,
          tilew, tileh
        );
      }
    });
  }

  /**
   * Initialize the map. You should call this after the MapInfo packet
   * has been received and the game map knows what size the map is
   */
  public void initialize()
  {
    Iterator i = iterator();
    while ( i.hasNext() )
    {
      ((MapView)i.next()).initialize();
    }
  }

  /**
   * Center the map view on the specified tile.
   *
   * @param tilex the x-coordinate of the tile
   * @param tiley the y-coordinate of the tile
   */
  public void centerOnTile( int tilex, int tiley )
  {
    // This only updates the main map view. Maybe in future, this will be a
    // view-by-view property (only slaved views will auto-center)

    m_mainView.centerOnTile( tilex, tiley );
  }

  /**
   * Create or return the main map view
   *
   * @return the main map view
   */
  public MapView getMainMapView()
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

    //if ( DEBUG_MODE )
    //{
    //  mv = new DebugMapView( m_client );
    //}
    //else 
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