package org.freeciv.client;

/**
 * A map view that uses isometric tiles. 
 *
 * @author Brian Duff
 */
class IsometricMapView extends AbstractMapView
{
  IsometricMapView( Client c )
  {
    super( c );
  }

  /**
   * Update the visible map canvas. Called on paint events on the map component
   * when the map has been resized
   */
  protected final void updateVisibleMap()
  {
    int width, height;
    width = height = getBufferTileWidth() + getBufferTileHeight();
    updateMapBuffer( 
      getViewTileOriginX(), getViewTileOriginY() - getBufferTileWidth(), 
      width, height, true 
    );

    showCityDescriptions();      
  }
  
  protected void updateMapBuffer( int x, int y, int width, int height,
    boolean writeToScreen )
  {
    // TODO
  }
  
}