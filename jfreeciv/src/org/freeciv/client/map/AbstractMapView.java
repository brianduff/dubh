package org.freeciv.client.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.client.Options;
import org.freeciv.client.panel.MapOverviewJumpEvent;
import org.freeciv.common.City;
import org.freeciv.common.Map;
import org.freeciv.common.Player;
import org.freeciv.common.TerrainType;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;
import org.freeciv.util.Localize;

/**
 * Abstract superclass for map views. The two concrete subclasses of this
 * are the IsometricMapView and the GridMapView.
 *
 * @author Brian Duff
 */
public abstract class AbstractMapView implements MapView, Constants
{

  private Client m_client;
  private MapComponent m_component;

  /**
   * The first tile co-ordinate at the left of the visible rectangle.
   */
  private int m_x0;
  /**
   * The first tile co-ordinate at the top of the visible rectangle.
   */
  private int m_y0;

  /**
   * The map "container" component, which consists of the scrollbars and
   * the actual map component itself.
   */
  private JScrollPane m_scrollPane;
  
  private boolean m_isCityView;
  private City m_city;

  private Dimension m_tileSize;
  private Dimension m_mapSize;

  /**
   * Construct the abstract map view. Concrete subclasses should call the super
   * constructor to ensure the abstract map is initialized properly.
   *
   * @param c the client this view belongs to
   */
  protected AbstractMapView(Client c)
  {
    m_client = c;
    m_tileSize = new Dimension( c.getTileSpec().getNormalTileWidth(),
      c.getTileSpec().getNormalTileHeight()
    );
    m_scrollPane = new JScrollPane();    
    
    m_component = new MapComponent( getLayers(), new InnerMapViewInfo() );


    Icon intro = getClient().getTileSpec().getImage( "main_intro_file" );
    assert( intro != null );
    m_scrollPane.setViewportView( new JLabel( intro ) );
  }

  /**
   * Construct a city view.
   */
  protected AbstractMapView( Client c, City city )
  {
    this( c );
    m_isCityView = true;
    m_city = city;
    m_scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER );
    m_scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

  }

  protected MapComponent getMapComponent()
  {
    return m_component;
  }

  /**
   * Get the MapViewInfo of this MapView.
   *
   * @return a MapViewInfo instance
   */
  public MapViewInfo getMapViewInfo()
  {
    return m_component.getMapViewInfo();
  }

  public final void mapOverviewJumped( MapOverviewJumpEvent moje )
  {
    // TODO: some map views will optionally not be slaved to the overview
    // component????
    centerOnTile( moje.getPosition().x, moje.getPosition().y );
  }

  /**
   * Get the map layer painters.
   *
   * @return a collection of MapLayer instances which are requested to paint
   *  in order
   */
  protected abstract Collection getLayers();

  //abstract void paintTile( Graphics2D g, Point tilePos, Point screenPos );

  public final void refreshTileMapCanvas( int x, int y )
  {
    x = getMap().adjustX( x );
    y = getMap().adjustY( y );

    if ( isTileVisible( x, y ) )
    {
      updateMapBuffer(x, y, 1, 1, true);
    }
  }

  public void updateMapBuffer( int tilex, int tiley, int tilew, int tileh, 
    boolean repaint )
  {
    tilex = getMap().adjustX( tilex );
    tiley = getMap().adjustY( tiley );

    m_component.repaintBufferLayer( tilex, tiley, tilew, tileh);

    if (repaint)
    {
      m_component.repaintLayers(tilex, tiley, tilew, tileh);
    }

    //if ( isTileVisible( tilex, tiley ) )
    //{
     // m_component.updateTileAt( tilex, tiley, tilew, tileh, repaint );
    //}
  }
  
  public void updateMapBufferVisible()
  {
    updateMapBuffer( getViewTileOriginX(), getViewTileOriginY(), 
                     getBufferTileWidth(), getBufferTileHeight(), true );
  }

  public final void initialize()
  {

    m_mapSize = new Dimension( getMap().getWidth(), getMap().getHeight() );
    m_component.setPreferredSize(  new Dimension(
      getMap().getWidth() * getNormalTileWidth(),
      getMap().getHeight() * getNormalTileHeight()
    ));
    m_scrollPane.setViewportView( m_component );
    m_scrollPane.invalidate();
    m_scrollPane.validate();


  }

  public final JComponent getComponent()
  {
    return m_scrollPane;
  }


  public final void centerOnTile( int tilex, int tiley )
  {
    // center_tile_mapcanvas
    centerOnTileImpl( tilex, tiley );

    updateScrollbars();
  }
  
  public final boolean isCityView()
  {
    return m_isCityView;
  }
  
  protected final City getCity()
  {
    return m_city;
  }

  public final void setCity( City city )
  {
    m_city = city;
  }

  /**
   * Get the client
   */
  protected final Client getClient()
  {
    return m_client;
  }

  /**
   * Convenience method to get the data for the map, equivalent to
   * getClient().getGame().getMap()
   *
   * @return the map "data model"
   */
  protected final Map getMap()
  {
    return getClient().getGame().getMap();
  }

  /**
   * Get information about a tile.
   *
   * @param mapx the map position to get tile information for
   * @param mapy the map position to get tile information for
   * @return a Tile object for the specified position
   */
  protected final Tile getTileAtMapPos(int mapx, int mapy)
  {
    return getMap().getTile( mapx, mapy );
  }

  /**
   * Get the size of the viewport
   *
   * @return a dimension that indicates the width and height of the visible
   *    part of the map in the scrollpane (the viewport)
   */
  Dimension getViewArea()
  {
    return ((JScrollPane)m_scrollPane).getViewport().getExtentSize();
  }

  Point getViewOffset()
  {
    return ((JScrollPane)m_scrollPane).getViewport().getViewPosition();
  }


  /**
   * Get the number of tiles that are displayed on the map horizontally
   */
  protected final int getBufferTileWidth()
  {
    return ( getViewArea().width + getNormalTileWidth()-1 ) / getNormalTileWidth();
  }

  /**
   * Get the number of tiles that are displayed on the map vertically
   */
  protected final int getBufferTileHeight()
  {
    return ( getViewArea().height + getNormalTileHeight()-1 ) / getNormalTileHeight();
  }


  /**
   * Get the x-index of the first tile visible in the viewrect
   */
  protected final int getViewTileOriginX()
  {
    return m_x0;
  }

  /**
   * Get the y-index of the first tile visible in the viewrect
   */
  protected final int getViewTileOriginY()
  {
    return m_y0;
  }

  /**
   * Set the x index of the first tile visible in the viewrect. You must
   * call updateScrollbars() after calling this to ensure that the
   * scrollpane's viewport is shifted to the correct location
   *
   * @param x the new x tile origin
   */
  protected final void setViewTileOriginX( int x )
  {
    m_x0 = x;
  }

  /**
   * Set the y index of the first tile visible in the viewrect. You must
   * call updateScrollbars() after calling this to ensure that the
   * scrollpane's viewport is shifted to the correct location
   *
   * @param y the new y tile origin
   */
  protected final void setViewTileOriginY( int y )
  {
    m_y0 = y;
  }

  protected final int getNormalTileWidth()
  {
    return m_tileSize.width;
  }

  protected final int getNormalTileHeight()
  {
    return m_tileSize.height;
  }

  Dimension getMapSizeInTiles()
  {
    return m_mapSize;
  }

  Dimension getTileSize()
  {
    return m_tileSize;
  }


  /**
   * Is the specified tile visible in the viewrect?
   *
   * @param x the tile x-coord
   * @param y the tile y-coord
   *
   * @return true if the specified tile is visible or partially visible
   *    in the view rectangle, false otherwise
   */
  protected abstract boolean isTileVisible( int x, int y );

  /**
   * Find the pixel co-ordinates of a tile.
   *
   * @param map_x the x-coordinate of the tile
   * @param map_y the y-coordinate of the tile
   * @param canvasPos on exit, contains the pixel co-ordinates of the tile.
   * @return true if the specified tile is inside the visible map
   */
  protected abstract boolean getCanvasPosition( int map_x, int map_y,
    Point canvasPos );

  /**
   * Center on the specified tile
   */
  protected abstract void centerOnTileImpl( int tilex, int tiley );






  /**
   * Update the visible map canvas.
   */
  //protected abstract void updateVisibleMap();

  /**
   * Update the scrollbars of the scrollpane. The scrollpane is scrolled
   * so that the origin tile is fully in view. This is probably completely
   * wrong, but we'll give it a shot.
   */
  void updateScrollbars()
  {
    // Figure out prospective pixel coordinates on the map
    int spX = getViewTileOriginX() * getNormalTileWidth();
    int spY = getViewTileOriginY() * getNormalTileHeight();

    // Now adjust these values so as to avoid falling off the end of the map
    // Not sure if we should alias to a tile boundary here... <ulp>
    m_scrollPane.getViewport().setViewPosition( new Point( spX, spY ) );
  }

  /**
   * Refresh the overview map
   */
  void refreshOverviewWindow()
  {
    getClient().getMainWindow().getMapOverview().refresh();
  }



  protected final String _(String s)
  {
    return Localize.translate( s );
  }

  /*
  protected final String _(String s, Object[] args)
  {
    return Localize.translate( s, args );
  }
  */

  private class InnerMapViewInfo implements MapViewInfo
  {
    /**
     * Get the portion of the map component which is visible to the user through
     * this view.
     *
     * @return a rectangle indicating (in pixels) the portion of the map 
     *    component which the user can currently see
     */
    public Rectangle getVisibleRectangle()
    {
      return m_component.getVisibleRect();
    }

    /**
     * Get the size of a tile
     *
     * @return a Dimension indicating the pixel width and height of a tile
     */
    public Dimension getTileSize()
    {
      return m_tileSize;
    }

    /**
     * Get the size of the map in tiles.
     *
     * @return a Dimension indicating how big the map is in tiles
     */
    public Dimension getMapSizeInTiles()
    {
      return m_mapSize;
    }

    /**
     * Get options for the view.
     *
     * @return an Options object that provides information about the options
     *    set for this view
     */
    public Options getOptions()
    {
      return getClient().getOptions();  /// TODO Options per view
    }

    /**
     * Get an image from the tilespec used by this view
     *
     * @param imageKey the key of an image
     * @return an image
     */
    public Icon getImage( String key )
    {
      return getClient().getTileSpec().getImage( key );
    }

    /**
     * Get information about a tile.
     *
     * @param mapPos the map position to get tile information for
     * @return a Tile object for the specified position
     */
    public Tile getTileAtMapPos( Point mapPos )
    {
      return getClient().getGame().getMap().getTile( mapPos.x, mapPos.y );
    }

    /**
     * Get the map
     *
     * @return Map information about the map
     */
    public Map getMap()
    {
      return getClient().getGame().getMap();
    }

    /**
     * Get terrain type of the specified id
     */
    public TerrainType getTerrainType( int id )
    {
      return (TerrainType)
        getClient().getGame().getFactories().getTerrainTypeFactory().findById( id );
    }

    /**
     * Get the unit in focus
     */
    public Unit getUnitInFocus()
    {
      return getClient().getUnitInFocus();
    }

    /**
     * Are flags in the tilespec transparent?
     */
    public boolean areFlagsTransparent()
    {
      return getClient().getTileSpec().areFlagsTransparent();
    }

    public Player getCurrentPlayer()
    {
      return getClient().getGame().getCurrentPlayer();
    }

  }


}
