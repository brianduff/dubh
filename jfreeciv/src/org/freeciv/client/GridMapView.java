package org.freeciv.client;

/**
 * A map view that uses square grid tiles.
 *
 * @author Brian Duff
 */
class GridMapView extends AbstractMapView
{
  public GridMapView( Client c )
  {
    super( c );
  }

  /**
   * Update the visible map canvas. Called on paint events on the map component
   * when the map has been resized
   */
  protected final void updateVisibleMap()
  {
    updateMapBuffer( 
      getViewTileOriginX(), getViewTileOriginY(), 
      getBufferTileWidth(), getBufferTileHeight(), 
      true 
    ); 
    showCityDescriptions();      
  }

  protected void updateMapBuffer( int x, int y, int width, int height,
    boolean writeToScreen )
  {
    // TODO
  }
}