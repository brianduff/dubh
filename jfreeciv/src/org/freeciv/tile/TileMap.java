package org.freeciv.tile;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import java.awt.event.*;
import org.freeciv.common.Logger;

public class TileMap extends GenericMap implements ActionListener
{
  protected List /*FlashingIcon*/[][] tiles;
  protected int horizontalTiles;
  protected int verticalTiles;
  protected int singleTileWidth;
  protected int singleTileHeight;
  protected Timer timer = new Timer( 300, this );
  protected boolean flashVisible = true;
  protected ArrayList flashing = new ArrayList();
  protected ArrayList flashingCoords = new ArrayList();
  private TileMap() 
  {
    
  }
  public TileMap( int aHorizontalTiles, int aVerticalTiles, int aSingleTileWidth, int aSingleTileHeight, boolean aHorizontalWrap, boolean aVerticalWrap ) 
  {
    horizontalTiles = aHorizontalTiles;
    verticalTiles = aVerticalTiles;
    singleTileHeight = aSingleTileHeight;
    singleTileWidth = aSingleTileWidth;
    hWrap = aHorizontalWrap;
    vWrap = aVerticalWrap;
    tiles = new List[ horizontalTiles ][ verticalTiles ];
    totalWidth = horizontalTiles * singleTileWidth;
    totalHeight = verticalTiles * singleTileHeight;
    setMaximumSize( new java.awt.Dimension( totalWidth * 2, totalHeight * 2 ) );
    setPreferredSize( new Dimension( 15 * singleTileWidth, 12 * singleTileHeight ) );
    setMinimumSize( new Dimension( 3 * singleTileWidth, 3 * singleTileHeight ) );
    setOpaque( false );
    addMouseMotionListener( new MouseMotionListener() 
    {
      private int oldx, oldy;
      void changeToolTip( int x, int y )
      {
        if( oldx == x && oldy == y )
        {
          return ;
        }
        Coords co = toTileCoordinates( x, y );
        if (co == null) return;
        StringBuffer txt = new StringBuffer( 50 );
        txt.append( "<html>(" );
        txt.append( String.valueOf( co.x ) );
        txt.append( ", " );
        txt.append( String.valueOf( co.y ) );
        txt.append( ")<br>" );
        List l = getTileList( co.x, co.y );
        for( int i = 0;i < l.size();i++ )
        {
          Object o = l.get( i );
          if( o != null )
          {
            txt.append( i );
            txt.append( " " );
            txt.append( o.toString() );
            txt.append( "<br>" );
          }
        }
        txt.append( "</html>" );
        setToolTipText( txt.toString() );
      }
      public void mouseMoved( MouseEvent me )
      {
        changeToolTip( me.getX(), me.getY() );
      }
      public void mouseDragged( MouseEvent me )
      {
        changeToolTip( me.getX(), me.getY() );
      }
    } );
  }
  public synchronized void changeTileSize( int nSingleTileWidth, int nSingleTileHeight )
  {
    singleTileWidth = nSingleTileWidth;
    singleTileHeight = nSingleTileHeight;
    totalWidth = horizontalTiles * singleTileWidth;
    totalHeight = verticalTiles * singleTileHeight;
    setMaximumSize( new java.awt.Dimension( totalWidth * 2, totalHeight * 2 ) );
    repaint();
  }
  public Coords toTileCoordinates( int x, int y )
  {
    return absoluteToTileCoordinates( x + upperLeftX, y + upperLeftY );
  }
  public Coords absoluteToTileCoordinates( int x, int y )
  {
    if( !hWrap && ( x < 0 || x >= totalWidth ) )
    {
      return null;
    }
    if( !vWrap && ( y < 0 || y >= totalHeight ) )
    {
      return null;
    }
    return new Coords( ( x / singleTileWidth ) % horizontalTiles, ( y / singleTileHeight ) % verticalTiles );
  }
  public void centerOnTile( int x, int y )
  {
    int px = x * singleTileWidth + singleTileWidth / 2;
    int py = y * singleTileHeight + singleTileHeight / 2;
    setUpperLeftXY( px - ( getWidth() / 2 ), py - ( getHeight() / 2 ) );
  }
  public void weaklyCenterOnTile( int x, int y )
  {
    Coords p = getCentralTile();
    if( Math.abs( x - p.x ) >= ( getWidth() / singleTileWidth ) / 3 || Math.abs( y - p.y ) >= ( getHeight() / singleTileHeight ) / 3 )
    {
      centerOnTile( x, y );
    }
  }
  public Coords getCentralTile()
  {
    Coords p = toTileCoordinates( getWidth() / 2, getHeight() / 2 );
    if( p == null )
    {
      return new Coords( 0, 0 );
    }
    else
    {
      return p;
    }
  }
  public List getTileList( int x, int y )
  {
    if( hWrap )
    {
      while( x < 0 )
      {
        x += horizontalTiles;
      }
      while( x >= horizontalTiles )
      {
        x -= horizontalTiles;
      }
    }
    else
    {
      if( x < 0 || x >= horizontalTiles )
      {
        return null;
      }
    }
    if( vWrap )
    {
      while( y < 0 )
      {
        y += verticalTiles;
      }
      while( y >= verticalTiles )
      {
        y -= verticalTiles;
      }
    }
    else
    {
      if( y < 0 || y >= verticalTiles )
      {
        return null;
      }
    }
    return tiles[ x ][ y ];
  }
  public void setTileList( int x, int y, List l )
  {
    if( hWrap )
    {
      while( x < 0 )
      {
        x += horizontalTiles;
      }
      while( x >= horizontalTiles )
      {
        x -= horizontalTiles;
      }
    }
    if( vWrap )
    {
      while( y < 0 )
      {
        y += verticalTiles;
      }
      while( y >= verticalTiles )
      {
        y -= verticalTiles;
      }
    }
    tiles[ x ][ y ] = l;
  }
  public int getHorizontalTiles()
  {
    return horizontalTiles;
  }
  public int getVerticalTiles()
  {
    return verticalTiles;
  }
  public int getSingleTileWidth()
  {
    return singleTileWidth;
  }
  public int getSingleTileHeight()
  {
    return singleTileHeight;
  }
  public void setFlashInterval( int ms )
  {
    timer.setDelay( ms );
  }
  public void startFlashing( int x, int y, FlashingIcon i )
  {
    synchronized( flashing )
    {
      flashing.add( i );
      flashingCoords.add( new Coords( x, y ) );
    }
    i.setVisible( flashVisible );
    if( flashing.size() == 1 )
    {
      timer.start();
      if( flashVisible )
      {
        actionPerformed( null ); // hack to start flashing at once
      }
    }
  }
  public void stopFlashing( FlashingIcon i )
  {
    synchronized( flashing )
    {
      int index = flashing.indexOf( i );
      flashing.remove( index );
      Coords p = (Coords)flashingCoords.remove( index );
      i.setVisible( true );
      repaintTiles( p.x, p.y, 1, 1 );
    }
    if( flashing.size() == 0 )
    {
      timer.stop();
    }
  }
  protected void paintVoid( Graphics g, Rectangle bounds )
  {
    if( voidColor == null )
    {
      return ;
    }
    g.setColor( voidColor );
    g.fillRect( bounds.x, bounds.y, bounds.width, bounds.height );
  }
  protected void paintTiles( Graphics g, Rectangle bounds )
  {
    
    //	   Logger.log(Logger.LOG_NORMAL, "In paintTiles.");
    boolean layerLeft = true;
    for( int layer = 0;layerLeft;layer++ )
    {
      layerLeft = false;
      int firstXtile = ( ( upperLeftX + bounds.x ) / singleTileWidth ) % horizontalTiles;
      int firstYtile = ( ( upperLeftY + bounds.y ) / singleTileHeight ) % verticalTiles;
      int columnsToPaint = bounds.width / singleTileWidth;
      int rowsToPaint = bounds.height / singleTileHeight;
      int currentXtile = firstXtile;
      if( currentXtile >= horizontalTiles )
      {
        currentXtile -= horizontalTiles;
      }
      int xcoord = bounds.x;
      //			Logger.log(Logger.LOG_NORMAL, "There are "+columnsToPaint+" cols to paint");
      while( columnsToPaint > 0 )
      {
        int currentRowsToPaint = rowsToPaint;
        int currentYtile = firstYtile;
        if( currentYtile >= verticalTiles )
        {
          currentYtile = 0;
        }
        int ycoord = bounds.y;
        //	Logger.log(Logger.LOG_NORMAL, "There are "+currentRowsToPaint+" rows to paint");
        while( currentRowsToPaint > 0 )
        {
          List l = tiles[ currentXtile ][ currentYtile ];
          if( layer < l.size() )
          {
            // optimize
            layerLeft = true;
            Icon icon = (Icon)l.get( layer );
            if( icon != null )
            {
              icon.paintIcon( this, g, xcoord, ycoord );
            }
          }
          currentRowsToPaint--;
          currentYtile++;
          if( currentYtile >= verticalTiles )
          {
            currentYtile = 0;
          }
          ycoord += singleTileHeight;
        }
        columnsToPaint--;
        currentXtile++;
        if( currentXtile >= horizontalTiles )
        {
          currentXtile = 0;
        }
        xcoord += singleTileWidth;
      }
    }
  }
  protected void paintGridlines( Graphics g, Rectangle bounds )
  {
    if( gridlineColor == null )
    {
      return ;
    }
    g.setColor( gridlineColor );
    int px = bounds.x + bounds.width;
    int py = bounds.y + bounds.height;
    for( int x = bounds.x;x < px;x += singleTileWidth )
    {
      g.drawLine( x, bounds.y, x, py );
    }
    for( int y = bounds.y;y < py;y += singleTileHeight )
    {
      g.drawLine( bounds.x, y, px, y );
    }
  }
  protected void paintComponent( Graphics g )
  {
    Rectangle r = g.getClipBounds();
    Rectangle rcpy = new Rectangle( r );
    paintVoid( g, rcpy );
    if( upperLeftX < 0 )
    {
      r.x = Math.max( r.x, -upperLeftX );
    }
    if( upperLeftY < 0 )
    {
      r.y = Math.max( r.y, -upperLeftY );
    }
    int align = ( r.x + upperLeftX ) % singleTileWidth;
    r.x -= align;
    r.width += align;
    align = ( r.y + upperLeftY ) % singleTileHeight;
    r.y -= align;
    r.height += align;
    align = ( r.width + r.x + upperLeftX ) % singleTileWidth;
    if( align != 0 )
    {
      r.width += singleTileWidth - align;
    }
    align = ( r.height + r.y + upperLeftY ) % singleTileWidth;
    if( align != 0 )
    {
      r.height += singleTileHeight - align;
    }
    if( !hWrap )
    {
      r.width = Math.min( r.width, totalWidth - ( r.x + upperLeftX ) );
    }
    if( !vWrap )
    {
      r.height = Math.min( r.height, totalHeight - ( r.y + upperLeftY ) );
    }
    
    /*    rcpy.setBounds(r);
    paintBackground(g,rcpy);
    */
    rcpy.setBounds( r );
    paintTiles( g, rcpy );
    rcpy.setBounds( r );
    paintGridlines( g, rcpy );
  }
  public void repaintTiles( int x, int y, int w, int h )
  {
    int startx = x * singleTileWidth - upperLeftX;
    int starty = y * singleTileHeight - upperLeftY;
    repaint( startx, starty, w * singleTileWidth, h * singleTileHeight );
    int currx = startx;
    int curry = starty;
    if( hWrap )
    {
      while( ( currx += totalWidth ) < getWidth() )
      {
        repaint( currx, curry, w * singleTileWidth, h * singleTileHeight );
      }
    }
  // TODO vWrap  and both wrap
  }
  public void repaintTilesAround( int x, int y )
  {
    repaintTiles( x - 1, y - 1, 3, 3 );
  }
  public void repaintTwoTilesAround( int x, int y )
  {
    repaintTiles( x - 2, y - 2, 5, 5 );
  }
  public void repaintOneTile( int x, int y )
  {
    repaintTiles( x, y, 1, 1 );
  }
  
  // flash timer
  public void actionPerformed( ActionEvent e )
  {
    synchronized( flashing )
    {
      for( int i = 0;i < flashing.size();i++ )
      {
        Coords p = (Coords)flashingCoords.get( i );
        ( (FlashingIcon)flashing.get( i ) ).setVisible( flashVisible );
        repaintTiles( p.x, p.y, 1, 1 );
      }
      flashVisible = !flashVisible;
    }
  }
}
