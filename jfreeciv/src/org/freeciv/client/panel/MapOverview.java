package org.freeciv.client.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.freeciv.client.Colors;
import org.freeciv.common.CommonConstants;
import org.freeciv.common.Game;
import org.freeciv.common.Tile;


/**
 * The map overview component. There is usually only one MapOverview component
 * on the screen at a time. It provides a miniature version of the map, and
 * provides eventing so that mouse clicks on the overview map translate to
 * jumps on the associated map view.
 *
 * @author Brian.Duff@dubh.org
 */
public final class MapOverview extends JComponent
{

  // Future enhancements:
  //
  // I'd like to use java2d scaling for this rather than just increasing the
  // size of the minimap as the main map gets bigger. The minimap looks
  // ridiculous at the maximum map size.

  private int m_width;
  private int m_height;

  private String m_version = "??";
  private Game m_game;

  private Icon m_introFile;

  private Font m_font;

  // The radar.xpm image is 160x100. The default size of the minimap is
  // half this.
  private static final int DEFAULT_WIDTH = 80;  // Change to 80
  private static final int DEFAULT_HEIGHT = 50;

  private static final int SCALE = 2;

  private List m_listeners;

  /**
   * Construct the map overview component
   *
   * @param game the game
   */
  public MapOverview( Game game, Icon introIcon )
  {
    setMapDimensions( DEFAULT_WIDTH, DEFAULT_HEIGHT );
    m_introFile = introIcon;
    m_font =  new Font( "sansserif", Font.BOLD, 13 ) ;

    m_game = game;

    addMouseListener( new OverviewMouseListener() );
  }

  /**
   * Add a listener for when the user requests a jump in the minimap
   *
   * @param l the listener to add
   */
  public void addJumpListener( MapOverviewJumpListener l )
  {
    if ( m_listeners == null )
    {
      m_listeners = new ArrayList( 1 );
    }
    m_listeners.add( l );
  }

  /**
   * Remove a listener previously added using addJumpListener()
   *
   * @param l a listener that was added
   */
  public void removeJumpListener( MapOverviewJumpListener l )
  {
    if ( m_listeners != null )
    {
      m_listeners.remove( l );
      if ( m_listeners.size() == 0 )
      {
        m_listeners = null;
      }
    }
  }

  /**
   * Fire out a jump event at the specified location
   *
   * @param x the x tile coordinate of the jump event
   * @param y the y tile coordinate of the jump event
   */
  private void fireJumpEvent( int x, int y )
  {
    if ( m_listeners == null || m_listeners.size() == 0 )
    {
      return;
    }

    MapOverviewJumpEvent event = new MapOverviewJumpEvent( this, x, y );
    Iterator i = m_listeners.iterator();
    while ( i.hasNext() )
    {
      ((MapOverviewJumpListener) i.next()).mapOverviewJumped( event );
    }
  }

  /**
   * A jump click was received at the specified pixel co-ordinates. 
   *
   * @param pixelX the pixel x coordinate
   * @param pixelY the pixel y coordinate
   */
  protected void jumpClick( int pixelX, int pixelY )
  {
    fireJumpEvent( pixelX / SCALE, pixelY / SCALE );
  }

  /**
   * Set the version string that is displayed in the minimap at startup before
   * the map has been initialized
   *
   * @param version a version string to display
   */
  public void setVersion( String version )
  {
    m_version = version;
  }

  /**
   * Set the map dimensions
   *
   * @param x the width of the map in tiles
   * @param y the height of the map in tiles
   */
  public void setMapDimensions(int x, int y)
  {
    // We should add a listener to the map instead of having this method.
    m_width = x*SCALE;
    m_height = y*SCALE;
    setPreferredSize(new Dimension( m_width, m_height ));
    setMinimumSize(new Dimension( m_width, m_height ));
    if ( getParent() != null )
    {
      // Force the parent to lay itself out again. Maybe we ought to reduce
      // the scale when the map gets over a certain size, as the minimap
      // sure makes the main window look ugly at the 200x100 maximum size.
      getParent().invalidate();
      getParent().validate();
    }
  }

  /**
   * Call this to repaint the overview map for a single tile.
   *
   * @param mapx the x-coordinate of the tile that has changed
   * @param mapy the y-coordinate of the tile that has changed
   */
  public void refresh(int mapx, int mapy )
  {
    refresh( mapx, mapy, 1, 1 );
  }

  /**
   * Call this to repaint a region of the map overview
   *
   * @param mapx the x tile coordinate of a rectangular region to repaint 
   * @param mapy the y tile coordinate of a rectangular region to repaint
   * @param w the width of a rectangular region to repaint, in tiles
   * @param h the height of a rectangular region to repaint, in tiles
   */
  public void refresh( int mapx, int mapy, int w, int h )
  {
    repaint( mapx * SCALE, mapy * SCALE, w * SCALE, h * SCALE ); 
  }

  /**
   * Repaint the entire map overview
   */
  public void refresh()
  {
    repaint();
  }

  private boolean isDrawingEnabled()
  {
    return !m_game.getMap().isEmpty();
  }

  private void paintShadowText( Graphics g, String str, int x, int y )
  {
    g.setColor( Color.black );
    g.drawString( str, x+1, y+1  );
    g.setColor( Color.white );
    g.drawString( str, x, y );
  }

  

  /**
   * Actually paint the map overview component
   */
  public void paintComponent( Graphics g )
  {
    ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
      RenderingHints.VALUE_ANTIALIAS_ON );
  
    if ( !isDrawingEnabled() )
    {
      // Draw the freeciv small intro screen.
      m_introFile.paintIcon( this, g, 0, 0 );

      // We should probably use TextLayout here, but I can't figure out
      // how to center text.
      FontMetrics fm = g.getFontMetrics( m_font );
      g.setColor( Color.white );
      g.setFont( m_font );

      String lastLine = "version "+m_version;
      String javaed = "Java Edition";

      int strWid = fm.stringWidth( lastLine );
      int tx = (getWidth() - strWid) / 2;
      int ty = getHeight() - 1 - fm.getDescent() - fm.getLeading();

      paintShadowText( g, lastLine, tx, ty );

      strWid = fm.stringWidth( javaed );
      tx = ( getWidth() - strWid ) / 2;
      ty = ty - fm.getHeight();

      paintShadowText( g, javaed, tx, ty );

    }
    else
    {

    
      int mapWidth = m_game.getMap().getWidth();
      int mapHeight = m_game.getMap().getHeight();
      int upperLeftX = 0;
      int upperLeftY = 0;

      Rectangle r = g.getClipBounds();
      Rectangle bounds = new Rectangle( r );


      int align = ( r.x + upperLeftX ) % SCALE;
      r.x -= align;
      r.width += align;
      align = ( r.y + upperLeftY ) % SCALE;
      r.y -= align;
      r.height += align;
      align = ( r.width + r.x + upperLeftX ) % SCALE;
      if( align != 0 )
      {
        r.width += SCALE - align;
      }
      align = ( r.height + r.y + upperLeftY ) % SCALE;
      if( align != 0 )
      {
        r.height += SCALE - align;
      }
      r.width = Math.min( r.width, m_width - ( r.x + upperLeftX ) );
      r.height = Math.min( r.height, m_height - ( r.y + upperLeftY ) );

      bounds.setBounds( r );


      int firstXtile = ( ( upperLeftX + bounds.x ) / SCALE ) % mapWidth;
      int firstYtile = ( ( upperLeftY + bounds.y ) / SCALE ) % mapHeight;
      int columnsToPaint = bounds.width / SCALE;
      int rowsToPaint = bounds.height / SCALE;

      int currentXtile = firstXtile;
      if( currentXtile >= mapWidth )
      {
        currentXtile -= mapWidth;
      }
      int xcoord = bounds.x;

      while( columnsToPaint > 0 )
      {
        int currentRowsToPaint = rowsToPaint;
        int currentYtile = firstYtile;
        if( currentYtile >= mapHeight )
        {
          currentYtile = 0;
        }
        int ycoord = bounds.y;

        while( currentRowsToPaint > 0 )
        {
          Color c = getTileColor( currentXtile, currentYtile );

          g.setColor( c );
          g.fillRect( xcoord, ycoord, SCALE, SCALE );

          currentRowsToPaint--;
          currentYtile++;
          if( currentYtile >= mapHeight )
          {
            currentYtile = 0;
          }
          ycoord += SCALE;
        }
        columnsToPaint--;
        currentXtile++;
        if( currentXtile >= mapWidth )
        {
          currentXtile = 0;
        }
        xcoord += SCALE;
      }
    }
  }


  public Color getTileColor( int x, int y )
  {
    // tilespec.c: overview_tile_color(int x, int y)
    Tile t = m_game.getMap().getTile( x, y );

    if ( !t.isKnown() )
    {
      return Colors.getStandardColor( Colors.COLOR_STD_BLACK );
    }
    else if ( t.hasCity() )
    {
      if (m_game.isCurrentPlayer(t.getCity().getOwner()))
      {
        return Colors.getStandardColor( Colors.COLOR_STD_WHITE );
      }
      else
      {
        return Colors.getStandardColor( Colors.COLOR_STD_CYAN );
      }
    }
    else if ( t.hasVisibleUnit() )
    {
      if (m_game.isCurrentPlayer(t.getVisibleUnit().getOwner()))
      {
        return Colors.getStandardColor( Colors.COLOR_STD_YELLOW );
      }
      else
      {
        return Colors.getStandardColor( Colors.COLOR_STD_RED );
      }
    }
    else if ( t.getTerrain() == CommonConstants.T_OCEAN )
    {
      return Colors.getStandardColor( Colors.COLOR_STD_OCEAN );
    }

    return Colors.getStandardColor( Colors.COLOR_STD_GROUND );

  }

  /**
   * mouse listener for clicks on the overview
   */
  private class OverviewMouseListener extends MouseAdapter
  {
    public void mousePressed( MouseEvent e )
    {
      // The C client uses button 2, but I'm sure the original CivII used
      // button 1, and am certain CTP uses 1. We support both...
      if ( e.getModifiers() == e.BUTTON1_MASK || 
           e.getModifiers() == e.BUTTON2_MASK )
      {
        jumpClick( e.getX(), e.getY() );
      }
    }
  }
}