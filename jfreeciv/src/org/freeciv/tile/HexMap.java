package org.freeciv.tile;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;


import org.freeciv.common.Assert;

/*
* Hexes vertically flattened (fit in square), rows are aligned
*/
public class HexMap extends TileMap
{

  // hex fits in square 4*sh x 4*sh
  int sh, sh2, sh3, sh4, sh5, sh6;
  int iconULXoffset;
  int iconULYoffset;
  int iconBRXoffset;
  boolean upperRowLeft;
  int leftCondition;
  int rightCondition;
  Polygon hexShape;


  // offsets can be only very specific, and BRY offset must be 0...

  /*
  There is not BRYoffset. In fact it defaults to tileSmallLength and cannot
  be larger nor smaller.
  */
  public HexMap( int aHorizontalTiles, int aVerticalTiles, int tileSmallLength, boolean aHorizontalWrap, boolean aVerticalWrap, boolean anUpperRowLeft, int anIconULXOffset, int anIconULYOffset, int anIconBRXOffset )
  {
    super( aHorizontalTiles, aVerticalTiles, tileSmallLength * 4, tileSmallLength * 3, aHorizontalWrap, aVerticalWrap );
    sh = tileSmallLength;
    sh2 = sh * 2;
    sh3 = sh * 3;
    sh4 = sh * 4;
    sh5 = sh * 5;
    sh6 = sh * 6;
    Assert.that( !hWrap || ( aHorizontalTiles % 2 == 0 ) );
    Assert.that( !vWrap || ( aVerticalTiles % 2 == 0 ) );
    upperRowLeft = anUpperRowLeft;
    if( upperRowLeft )
    {
      leftCondition = 0;
      rightCondition = 1;
    }
    else
    {
      leftCondition = 1;
      rightCondition = 0;
    }
    iconULXoffset = anIconULXOffset;
    iconULYoffset = anIconULYOffset;
    iconBRXoffset = anIconBRXOffset;
    int[] xcoords = new int[]
    {
      0, sh2, sh4 - 1, sh4 - 1, sh2, 0
    };
    int[] ycoords = new int[]
    {
      sh, 0, sh, sh3 - 1, sh4 - 1, sh3 - 1
    };
    hexShape = new Polygon( xcoords, ycoords, 6 );
  }
  protected final boolean isLeft( int ycell )
  {
    return ( ycell % 2 ) == leftCondition;
  }
  protected final boolean isRight( int ycell )
  {
    return ( ycell % 2 ) == rightCondition;
  }
  public Coords absoluteToTileCoordinates( int x, int y )
  {
    int y1 = y / sh;
    if( y < 0 )
    {
      y1--;
    }
    boolean differ = ( ( y1 % 3 ) == 0 );
    int ycell1, ycell2, xcell1, xcell2;
    ycell1 = y1 / 3;
    if( y1 < 0 )
    {
      ycell1--;
    }
    ycell2 = differ ? ycell1 - 1 : ycell1;
    if( isLeft( ycell1 ) ) // row moved left
    {
      xcell1 = x / sh4;
      if( x < 0 )
      {
        xcell1--;
      }
      xcell2 = ( x - sh2 );
      if( xcell2 < 0 )
      {
        xcell2 = ( xcell2 / sh4 ) - 1;
      }
      else
      {
        xcell2 = xcell2 / sh4;
      }
    }
    else
    {
      xcell1 = ( x - sh2 );
      if( xcell1 < 0 )
      {
        xcell1 = ( xcell1 / sh4 ) - 1;
      }
      else
      {
        xcell1 = xcell1 / sh4;
      }
      xcell2 = x / sh4;
      if( x < 0 )
      {
        xcell2--;
      }
    }
    if( differ )
    {
      int xoffs, yoffs;
      yoffs = y % sh3;
      Assert.that( yoffs < sh );
      if( isLeft( ycell2 ) ) // upper row moved left
      {
        xoffs = ( x + sh2 ) % sh4; // it is opposite to condition on purpose
      }
      else
      {
        xoffs = x % sh4;
      }

      // add check for negative and too large cells (wrap or return null)
      if( yoffs - ( Math.abs( xoffs - sh2 ) / 2 ) < 0 )
      {
        xcell1 = xcell2;
        ycell1 = ycell2;
      }
    }
    if( !hWrap && ( xcell1 < 0 || xcell1 >= horizontalTiles ) )
    {
      return null;
    }
    while( xcell1 < 0 )
    {
      xcell1 += horizontalTiles;
    }
    if( !vWrap && ( ycell1 < 0 || ycell1 >= verticalTiles ) )
    {
      return null;
    }
    while( ycell1 < 0 )
    {
      ycell1 += verticalTiles;
    }
    return new Coords( xcell1 % horizontalTiles, ycell1 % verticalTiles );
  }
  public int adjustX( int x )
  {
    if( !hWrap )
    {
      if( x < 0 || x >= horizontalTiles )
      {
        return -1;
      }
    }
    else
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
    return x;
  }
  public int adjustY( int y )
  {
    if( !vWrap )
    {
      if( y < 0 || y >= verticalTiles )
      {
        return -1;
      }
    }
    else
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
    return y;
  }
  public Coords normalize( Coords crd )
  {
    int x = adjustX( crd.x );
    int y = adjustY( crd.y );
    if( x < 0 || y < 0 )
    {
      return null;
    }
    crd.x = x;
    crd.y = y;
    return crd;
  }
  public Coords nextHex( int hexx, int hexy, int dx, int dy, Coords in )
  {
    in.y = adjustY( hexy + dy );
    if( in.y < 0 )
    {
      return null;
    }
    if( isLeft( in.y ) )
    {
      if( dx == 1 )
      {
        dx = 0;
      }
    }
    else
    {
      if( dx == -1 )
      {
        dx = 0;
      }
    }
    in.x = adjustX( hexx + dx );
    if( in.x < 0 )
    {
      return null;
    }
    return in;
  }
  public Coords nextHex( int hexx, int hexy, int dx, int dy )
  {
    return nextHex( hexx, hexy, dx, dy, new Coords() );
  }
  public Coords nextHex( Coords hex, int dx, int dy, Coords in )
  {
    return nextHex( hex.x, hex.y, dx, dy, in );
  }
  public Coords nextHex( Coords hex, int dx, int dy )
  {
    return nextHex( hex.x, hex.y, dx, dy, new Coords() );
  }
  public void centerOnTile( int xcell, int ycell )
  {
    int px = xcell * sh4 + sh2;
    int py = ycell * sh3 + sh3 / 2;
    if( isRight( ycell ) )
    {
      px += sh2;
    }
    setUpperLeftXY( px - ( getWidth() / 2 ), py - ( getHeight() / 2 ) );
  }
  protected void paintVoid( Graphics g, Rectangle r )
  {
    g.setColor( voidColor );
    g.fillRect( r.x, r.y, r.width, r.height );
  }


  // x and y are topleft of 4x4 hex square
  protected void paint3DHexOutline( Graphics g, int x, int y )
  {
    g.translate( x, y );
    Color c = g.getColor();
    g.setColor( c.brighter() );
    g.drawLine( sh4, sh3 - 1, sh2, sh4 - 1 );
    g.drawLine( sh2, sh4 - 1, 0, sh3 - 1 );
    g.drawLine( 0, sh3, 0, sh );
    g.setColor( c.darker() );
    g.drawLine( 0, sh, sh2, 0 );
    g.drawLine( sh2, 0, sh4, sh );
    g.drawLine( sh4 - 1, sh, sh4 - 1, sh3 );
    g.setColor( c );
    g.translate( -x, -y );
  }
  protected void paintHexOutline( Graphics g, int x, int y )
  {
    paint3DHexOutline( g, x, y );
  }


  /*
  Bounds have x,y always at top of 4x5 tile
  Width = sh4 + n*sh2 (n=0,1,...)
  Height = sh6 + n*sh3 (n=0,1,...)
  */

  // NOT USED
  protected void paintOneTileStack( Graphics g, List list, int x, int y )
  {
    throw new UnsupportedOperationException();
  /*
  Iterator iter = list.iterator();
  while ( iter.hasNext() )
  {
  Icon icon = (Icon)iter.next();
  if ( icon != null )
  {
  icon.paintIcon(this,g,x,y);
  }
  }
  if ( gridlineColor != null )
  {
  g.setColor(gridlineColor);
  paintHexOutline(g,x,y+sh);
  }
  */
  }
  protected void paintGridlines( Graphics g, Rectangle r )
  {
    throw new UnsupportedOperationException( "Grids are painted together with tiles" );
  }



  // old method left for testing - now use paintTilesByLayer
  protected void paintTilesByStack( Graphics g, Rectangle r )
  {
    long start = System.currentTimeMillis();
    int endX = r.x + r.width;
    int endY = r.y + r.height;
    boolean odd = false;
    for( int y = r.y;y <= endY;y += sh3 )
    {
      int currentStartX = odd ? r.x + sh2 : r.x;
      int currentEndX = odd ? endX + sh2 : endX;
      for( int x = currentStartX;odd ? ( x < currentEndX ) : ( x <= currentEndX );x += sh4 )
      {
        Coords p = absoluteToTileCoordinates( x, y );
        if( p == null )
        {
          continue;
        }
        paintOneTileStack( g, tiles[ p.x ][ p.y ], x - sh2 + iconULXoffset, y - sh2 + iconULYoffset );
      }
      odd = !odd;
    }
    System.out.println( "Repaint in " + ( System.currentTimeMillis() - start ) );
  }
  Random rnd = new Random();
  protected void paintTilesByLayer( Graphics g, Rectangle r )
  {
    long start = System.currentTimeMillis();
    int endX = r.x + r.width;
    int endY = r.y + r.height;
    boolean layerLeft = true;
    /*
    g.setColor(new Color(rnd.nextInt()));
    g.fillRect(r.x,r.y,r.width,r.height);
    */
    for( int layer = 0;layerLeft;layer++ )
    {
      boolean odd = false;
      layerLeft = false;
      for( int y = r.y;y <= endY;y += sh3 )
      {
        int currentStartX = odd ? r.x + sh2 : r.x;
        int currentEndX = odd ? endX + sh2 : endX;
        for( int x = currentStartX;( odd ? ( x < currentEndX ) : ( x <= currentEndX ) );x += sh4 )
        {
          Coords p = absoluteToTileCoordinates( x, y );
          if( p == null )
          {
            continue;
          }
          List l = tiles[ p.x ][ p.y ];
          if( layer < l.size() )
          {
            Icon icon = (Icon)l.get( layer );
            if( icon != null )
            {
              icon.paintIcon( this, g, x - sh2 + iconULXoffset, y - sh2 + iconULYoffset );
            }
            // optimize ?
            layerLeft = true;
          }
        }
        odd = !odd;
      }
    }
    if( gridlineColor != null )
    {
      g.setColor( gridlineColor );
      //       ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.50f));
      boolean odd = false;
      for( int y = r.y;y <= endY;y += sh3 )
      {
        int currentStartX = odd ? r.x + sh2 : r.x;
        int currentEndX = odd ? endX + sh2 : endX;
        for( int x = currentStartX;( odd ? ( x < currentEndX ) : ( x <= currentEndX ) );x += sh4 )
        {
          Coords p = absoluteToTileCoordinates( x, y );
          if( p == null )
          {
            continue;
          }
          paintHexOutline( g, x - sh2, y - sh2 );
        }
        odd = !odd;
      }
    //         g.setPaintMode();
    }
    System.out.println( "Repaint in " + ( System.currentTimeMillis() - start ) );
  }
  public void repaintOneTile( int xcell, int ycell )
  {
    int x = ( isLeft( ycell ) ? xcell * sh4 : xcell * sh4 + sh2 ) + iconULXoffset - upperLeftX;
    while( x < -sh4 )
    {
      x += totalWidth;
    }
    int y = ( ycell * sh3 ) + iconULYoffset - upperLeftY;
    while( y < -sh4 )
    {
      y += totalHeight;
    }
    repaint( x, y, sh4 - iconULXoffset + iconBRXoffset, sh5 - iconULYoffset );
  }
  public void repaintTiles( int xcell, int ycell, int width, int height )
  {
    throw new UnsupportedOperationException( "use repaintOneTile or repaintTilesAround" );
  }
  public void repaintTilesAround( int xcell, int ycell )
  {
    repaintOneTile( xcell - 1, ycell );
    repaintOneTile( xcell + 1, 0 );
    if( isLeft( ycell ) )
    {
      repaintOneTile( xcell - 1, ycell - 1 );
      repaintOneTile( xcell - 1, ycell + 1 );
      repaintOneTile( xcell, ycell - 1 );
      repaintOneTile( xcell, ycell + 1 );
    }
    else
    {
      repaintOneTile( xcell, ycell - 1 );
      repaintOneTile( xcell, ycell + 1 );
      repaintOneTile( xcell + 1, ycell - 1 );
      repaintOneTile( xcell + 1, ycell + 1 );
    }
  }


  // not tested
  public void repaintTwoTilesAround( int xcell, int ycell )
  {
    int x = ( isLeft( ycell ) ? xcell * sh4 : xcell * sh4 + sh2 ) + iconULXoffset - upperLeftX;
    while( x < -sh4 )
    {
      x += totalWidth;
    }
    int y = ( ycell * sh3 ) + iconULYoffset - upperLeftY;
    while( y < -sh4 )
    {
      y += totalHeight;
    }
    repaint( x - sh6, y - sh6, sh4 + 2 * sh6 - iconULXoffset + iconBRXoffset, sh5 + 2 * sh6 - iconULYoffset );
  }
  public void paintComponent( Graphics g )
  {
    g.translate( -upperLeftX, -upperLeftY );
    Rectangle r = g.getClipBounds();
    //      Insets insets = getInsets();
    //      System.out.println(r);
    //      System.out.println(insets);
    paintVoid( g, r );
    if( hWrap )
    {
      while( r.x < 0 )
      {
        r.x += totalWidth;
      }
    }
    else
    {
      if( r.x < 0 )
      {
        r.width += r.x;
        r.x = 0;
      }
    }
    if( vWrap )
    {
      while( r.y < 0 )
      {
        r.y += totalHeight;
      }
    }
    else
    {
      if( r.y < 0 )
      {
        r.height += r.y;
        r.y = 0;
      }
    }
    int align;
    align = ( r.y + sh ) % sh3;
    r.y -= align;
    r.height += align;
    align = ( sh3 - ( r.y + r.height + sh ) % sh3 ) % sh3;
    r.height += align;

    // maybe should compute max change from ul and bl corners ?
    if( isLeft( ( r.y + sh2 ) / sh3 ) )
    {
      align = ( r.x ) % sh4;
    }
    else
    {
      align = ( r.x + sh2 ) % sh4;
    }
    r.x -= align;
    r.width += align;

    // BUGS ???
    if( isLeft( ( r.y ) / sh3 ) )
    {
      align = ( r.x + r.width + sh2 ) % sh4;
    }
    else
    {
      align = ( r.x + r.width ) % sh4;
    }
    r.width += align;
    r.width = Math.max( r.width, sh4 );
    g.setClip( Math.max( r.x, r.x ), Math.max( r.y, upperLeftY ), r.width, r.height );
    paintTilesByLayer( g, r );
    g.translate( upperLeftX, upperLeftY );
  }

  static class HCoordIcon implements Icon
  {
    int aa;
    int bb;
    HCoordIcon( int a, int b )
    {
      aa = a;
      bb = b;
    }
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      g.setColor( Color.black );
      g.drawString( "" + aa + "," + bb, x + 15, y + 60 );
    }
    public int getIconWidth()
    {
      return 60;
    }
    public int getIconHeight()
    {
      return 75;
    }
  }
  static class HTripletIcon implements Icon
  {
    int aa;
    int bb;
    int cc;
    HTripletIcon( int a, int b, int c )
    {
      aa = a;
      bb = b;
      cc = c;
    }
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
      g.setColor( Color.blue );
      g.drawString( "" + aa + "," + bb + "," + cc, x + 5, y + 42 );
    }
    public int getIconWidth()
    {
      return 60;
    }
    public int getIconHeight()
    {
      return 75;
    }
  }




  // SOME TRIPLET COORD CODE - in work
  int tripletSum = horizontalTiles + verticalTiles - 2;
  //  int verticalTiles1_2 = verticalTiles/2;
  int tripletSumLessvt1_2 = tripletSum - verticalTiles / 2;

  // x,y has to be already adjusted for wrapping
  public TripletCoords coordsToTriplet( int x, int y, TripletCoords answer )
  {
    int b = y;

    //      int c = tripletSum +1 -(2*x+y+1)/2 -verticalTiles1_2;
    //    int c = tripletSum -(2*x+y-1)/2 -verticalTiles1_2;
    //      int c = tripletSum - x -(y-1)/2 -verticalTiles1_2; // error for 0,0
    //      int c = tripletSumLessvt1_2 - x -(y-1)/2; // error for 0,0
    int c = tripletSumLessvt1_2 + 1 - ( 2 * x + y + 1 ) / 2;
    int a = tripletSum - c - b;
    answer.a = a;
    answer.b = b;
    answer.c = c;
    return answer;
  }
  public Coords tripletToCoords( int a, int b, int c, Coords answer )
  {
    int x;
    int y;
    y = b;
    //      x = (tripletSum-c+a-verticalTiles+2)/2;
    x = ( horizontalTiles - c + a ) / 2;
    answer.x = x;
    answer.y = y;
    return answer;
  }
  public TripletCoords normalize( TripletCoords tcrd )
  {
    Coords crd = new Coords();
    crd = tripletToCoords( tcrd.a, tcrd.b, tcrd.c, crd );
    crd = normalize( crd );
    if( crd == null )
    {
      return null;
    }
    return coordsToTriplet( crd.x, crd.y, tcrd );
  }
}
