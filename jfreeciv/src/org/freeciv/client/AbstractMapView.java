package org.freeciv.client;

import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.freeciv.common.Assert;
import org.freeciv.common.Map;

/**
 * Abstract superclass for map views. The two concrete subclasses of this
 * are the IsometricMapView and the GridMapView.
 *
 * @author Brian Duff
 */
abstract class AbstractMapView implements MapView, Constants
{

  private Client m_client;
  private BufferedImage m_introImage;
  private int m_introImageHeight = 0;
  private int m_introImageWidth = 0;
  private MapComponent m_component;

  private ImageIcon m_introImageIcon= null;

  /**
   * This buffered image always contains the current view port. This saves
   * excessive lookups on repaints. It is (re)allocated each time the view port 
   * changes size (in paint)
   */
  private BufferedImage m_bufferedImage;

  private static final Color SPLASH_TAGLINE_COLOR = new Color( 45, 113, 227 );
  private static final Font SPLASH_TAGLINE_FONT = 
    new Font( "Dialog", Font.BOLD, 16 );
  // Number of pixels above the bottom of the image for the tagline.
  private static final int SPLASH_TAGLINE_OFFSET = 25;

  /**
   * The first tile co-ordinate at the left of the visible rectangle.
   */
  private int m_x0;
  /**
   * The first tile co-ordinate at the top of the visible rectangle.
   */
  private int m_y0;

  /**
   * The number of tiles that can be displayed horizontally on the map canvas
   */
  private int m_canvasWidthTiles;


  /**
   * The map "container" component, which consists of the scrollbars and
   * the actual map component itself.
   */
  private JComponent m_scrollPane;

  private int NORMAL_TILE_WIDTH;
  private int NORMAL_TILE_HEIGHT;

  /**
   * The number of tiles that can be stored on the map canvas store horizontally
   */
  private int m_bufferTileWidth = 0;

  private int m_bufferTileHeight = 0;


  /**
   * Construct the abstract map view. Concrete subclasses should call the super
   * constructor to ensure the abstract map is initialized properly.
   *
   * @param c the client this view belongs to
   */
  protected AbstractMapView(Client c)
  {
    m_client = c;
    Icon intro = getClient().getTileSpec().getImage( "main_intro_file" );
    Assert.that( intro != null );

    //m_splashScreen = new BufferedImage( intro.getIconWidth(),
    //  intro.getIconHeight(), BufferedImage.TYPE_INT_RGB );

    NORMAL_TILE_WIDTH = c.getTileSpec().getNormalTileWidth();
    NORMAL_TILE_HEIGHT = c.getTileSpec().getNormalTileHeight();

    m_component = new MapComponent();

    m_scrollPane = new JScrollPane( m_component );
    
    //intro.paintIcon( m_component, m_splashScreen.getGraphics(), 0, 0 );
    //paintSplashTagline( );
  }

  public void refreshTileMapCanvas( int x, int y )
  {
    // TODO
  }

  public JComponent getComponent()
  {
    return m_scrollPane;
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
   * Get the size of the viewport
   *
   * @return a dimension that indicates the width and height of the visible
   *    part of the map in the scrollpane (the viewport)
   */
  private Dimension getViewArea()
  {
    return ((JScrollPane)m_scrollPane).getViewport().getExtentSize();
  }

  /**
   * Get the number of tiles that are displayed on the map horizontally
   */
  protected final int getBufferTileWidth()
  {
    return m_bufferTileWidth;
  }

  /**
   * Get the number of tiles that are displayed on the map vertically
   */
  protected final int getBufferTileHeight()
  {
    return m_bufferTileHeight;
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
   * Update the map buffer for the tiles at the specified locations.
   *
   * @param x the x-coord
   * @param y the y-coord
   * @param width the width
   * @param height the height
   * @param writeToScreen whether to actually write to screen or just update
   *    the in-memory buffer
   */
  protected abstract void updateMapBuffer( int x, int y, int width, int height,
    boolean writeToScreen );

  /**
   * Get a new BufferedImage containing the specified icon, scaled  to the
   * specified dimensions.
   *
   * @param i the icon to draw. Must be an ImageIcon.
   * @param targWidth the required width of the buffered image
   * @param targHeight the required height of the buffered image
   */
  protected final BufferedImage getScaledIcon( 
    ImageIcon i, int targWidth, int targHeight )
  {
    double xscale = 
      (double) targWidth / (double) i.getIconWidth();
    double yscale =
      (double) targHeight / (double) i.getIconHeight();

    BufferedImage bi = new BufferedImage( targWidth, targHeight, 
      BufferedImage.TYPE_INT_RGB
    );

    AffineTransform at = new AffineTransform();
    at.scale( xscale, yscale );

    bi.createGraphics().drawImage( i.getImage(), at, null );

    return bi;
  }

  /**
   * Paint "'Cause Civilization should be free!" into the splashscreen.
   * This is not scaled. it's just painted over an already scaled image.
   */
  private void paintSplashTagline(Graphics g, int width, int height)
  {
    String tagline = _("'Cause civilization should be free!");
    g.setFont( SPLASH_TAGLINE_FONT );
    g.setColor( SPLASH_TAGLINE_COLOR );
    FontMetrics fm = g.getFontMetrics();
    Rectangle2D.Float fontRect = ( Rectangle2D.Float )
      fm.getStringBounds( tagline.toCharArray(), 0, tagline.length(), g );


    int bottomOfText = height - SPLASH_TAGLINE_OFFSET;
    int leftOfText = (width / 2) - ((int)fontRect.width / 2);

    g.drawString( tagline, leftOfText, bottomOfText );
  }


  /**
   * Update the visible map canvas. Called on paint events on the map 
   * component when the map has been resized.
   */
  protected abstract void updateVisibleMap();

  /**
   * Update the scrollbars of the scrollpane. Called on paint events on the
   * map component when the map has been resized.
   */
  private void updateScrollbars()
  {
    // TODO
  }

  /**
   * Refresh the overview map
   */
  private void refreshOverviewWindow()
  {
    getClient().getMainWindow().getMapOverview().refresh();
  }

  /**
   * Show city descriptions ?
   */
  protected final void showCityDescriptions()
  {
    // TODO
  }


  protected final String _(String s)
  {
    return Localize.translation.translate( s );
  }

  /*
  protected final String _(String s, Object[] args)
  {
    return Localize.translate( s, args );
  }  
  */

  /**
   * The actual map component. A simple JComponent that paints the map contents
   */
  private class MapComponent extends JComponent
  {

    public void paintComponent(Graphics gfx)
    {
      // This is roughly equivalent to
      // gui-gtk/mapview.c:map_canvas_expose()
      // but obviously quite different.
      
      super.paintComponent( gfx );
      Graphics2D g = (Graphics2D) gfx;
      boolean mapResized = false;
      int tile_height, tile_width;

      Dimension d = getViewArea();
      tile_width =  ( d.width + NORMAL_TILE_WIDTH-1)/NORMAL_TILE_WIDTH;
      tile_height = ( d.height + NORMAL_TILE_HEIGHT-1)/NORMAL_TILE_HEIGHT;

      // BD: Shouldn't do this if we are not in GAME_RUNNING_STATE, just eats
      // cpu cycles.
      if ( m_bufferTileWidth != tile_width || m_bufferTileHeight != tile_height )
      {
        m_bufferTileWidth = tile_width;
        m_bufferTileHeight = tile_height;

        m_bufferedImage = new BufferedImage( 
          tile_width * NORMAL_TILE_WIDTH, tile_height * NORMAL_TILE_HEIGHT,
          BufferedImage.TYPE_INT_RGB
        );
        Graphics gbi = m_bufferedImage.createGraphics();
        gbi.setColor( Color.black );
        gbi.fillRect( 0, 0, NORMAL_TILE_WIDTH * m_bufferTileWidth,
          NORMAL_TILE_HEIGHT * m_bufferTileHeight );

        // gtk updates its scrollbars here. I think we don't need to do this
        // (JScrollPane takes care of it)
        mapResized = true;
      }

      // If the game is not running, we paint the freeciv screen image
      if ( getClient().getGameState() != CLIENT_GAME_RUNNING_STATE )
      {
        if ( d.height != m_introImageHeight || d.width != m_introImageWidth )
        {
          if (m_introImageIcon == null)
          {
            m_introImageIcon = 
              (ImageIcon)getClient().getTileSpec().getImage( "main_intro_file" );
          }
          Assert.that ( m_introImageIcon != null );
          m_introImage = getScaledIcon(
            m_introImageIcon,
            d.width,
            d.height
          );
          m_introImageWidth = d.width;
          m_introImageHeight = d.height;
        }

        if ( m_introImage != null )
        {
          // Check that this clips properly. Javadoc for drawImage() is a bit
          // obtuse.
          g.drawImage( m_introImage, null, 0, 0 );
          paintSplashTagline( g, d.width, d.height );
        }
      }
      else
      {
        // Allow the GC to collect these guys
        if (m_introImage != null)
        {
          m_introImageIcon = null;
          m_introImage = null;
          m_introImageWidth = 0; m_introImageHeight = 0;          
        }

        if ( getMap().getWidth() > 0 )
        {
          if ( mapResized )
          {
            updateVisibleMap();
            updateScrollbars();
            refreshOverviewWindow();
          }
          else
          {
            // Cool. Simply dump the buffer into the graphics context.
            g.drawImage( m_bufferedImage, null, 0, 0 );   // Maybe this should be the x, y of the viewrect rather than 0,0?
            showCityDescriptions();
          }
        }
        refreshOverviewWindow();
        
      }
    }
  }  
}