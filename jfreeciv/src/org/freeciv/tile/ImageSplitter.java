package org.freeciv.tile;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.*;
import com.sixlegs.image.png.PngImage;
public class ImageSplitter implements ImageConsumer
{
  int width;
  int height;
  int tileWidth;
  int tileHeight;
  int hTiles;
  int vTiles;
  int[][][] tiles;
  ColorModel model;
  ArrayList imgList = new ArrayList();
  ArrayList list = new ArrayList();
  public ImageSplitter( int aWidth, int aHeight, int aTileWidth, int aTileHeight ) 
  {
    width = aWidth;
    height = aHeight;
    tileWidth = aTileWidth;
    tileHeight = aTileHeight;
    hTiles = width / tileWidth;
    vTiles = height / tileHeight;
    tiles = new int[ hTiles ][ vTiles ][ tileWidth * tileHeight ];
    System.out.println( "w=" + tileWidth + " h=" + tileHeight + " ht=" + hTiles + " vt=" + vTiles );
  }
  public void setDimensions( int width, int height )
  {
    
  }
  public void setProperties( Hashtable props )
  {
    
  }
  public void setColorModel( ColorModel model )
  {
    
  }
  public void setHints( int hintflags )
  {
    
  }
  public void setPixels( int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize )
  {
    throw new RuntimeException( "Byte data" );
  }
  public void setPixels( int x, int y, int w, int h, ColorModel aModel, int[] pixels, int off, int scansize )
  {
    if( ( h != 1 ) || ( w != scansize ) || ( x != 0 ) || ( off != 0 ) )
    {
      throw new RuntimeException( "Bad assertion in setPixels" );
    }
    for( int i = 0;i < scansize;i++ )
    {
      tiles[ i / tileWidth ][ y / tileHeight ][ ( y % tileHeight ) * tileWidth + ( i % tileWidth ) ] = pixels[ i ];
    }
    model = aModel;
  }
  public void imageComplete( int status )
  {
    
  }
  public Image[] getImageArray()
  {
    Image[] imgarr = new Image[ imgList.size() ];
    for( int i = 0;i < imgarr.length;i++ )
    {
      imgarr[ i ] = (Image)imgList.get( i );
    }
    return imgarr;
  }
  public FlashingImageIcon[] getFilteredIconArray( ImageFilter f )
  {
    FlashingImageIcon fi[] = new FlashingImageIcon[ vTiles * hTiles ];
    for( int y = 0;y < vTiles;y++ )
    {
      for( int x = 0;x < hTiles;x++ )
      {
        Image img = Toolkit.getDefaultToolkit().createImage( new FilteredImageSource( new MemoryImageSource( tileWidth, tileHeight, model, tiles[ x ][ y ], 0, tileWidth ), f ) );
        fi[ y * hTiles + x ] = new FlashingImageIcon( img );
      }
    }
    return fi;
  }
  public FlashingImageIcon[] getIconArray()
  {
    for( int y = 0;y < vTiles;y++ )
    {
      for( int x = 0;x < hTiles;x++ )
      {
        Image img = Toolkit.getDefaultToolkit().createImage( new MemoryImageSource( tileWidth, tileHeight, model, tiles[ x ][ y ], 0, tileWidth ) );
        list.add( new FlashingImageIcon( img ) );
        imgList.add( img );
      }
    }
    //    tiles = null;
    FlashingImageIcon[] imgarr = new FlashingImageIcon[ list.size() ];
    for( int i = 0;i < imgarr.length;i++ )
    {
      imgarr[ i ] = (FlashingImageIcon)list.get( i );
    }
    return imgarr;
  }
  public int[][][] getData()
  {
    return tiles;
  }




/*
public static void  main( String[] argv )throws java.io.IOException
{
PngImage img = new PngImage(argv[0]);
final ImageSplitter is = new ImageSplitter(img.getWidth(), img.getHeight(),
img.getWidth()/20, img.getHeight()/18);
img.startProduction(is);

final java.util.Random rand = new java.util.Random();

JFrame jf = new JFrame("Map test");
final TileMap map = new TileMap((short)50,(short)50,45,45,false,false);
map.setGridlineColor(Color.white);
//      map.setUpperLeftX(-100);
//      map.setUpperLeftY(-110);
for ( int x =0; x < 50; x++ )
for ( int y =0; y < 50; y++ )
{
ArrayList l = new ArrayList();
l.add(is.list.get((x%20 + y*20)%360));
l.add(new TileMap.CoordIcon(x,y));
map.tiles[x][y] = l;
if ( (x+y)%7 == 0 )
{
map.startFlashing(x,y,(FlashingIcon)is.list.get((x%20 + y*20)%360));
}
}
map.setPreferredSize(new Dimension(520,420));
map.addMouseListener( new MouseAdapter() {
public void mouseClicked(MouseEvent e )
{
if ( (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0 )
{
int x = e.getX() + map.upperLeftX;
int y = e.getY() + map.upperLeftY;
map.setUpperLeftX(x-(map.getWidth()/2));
map.setUpperLeftY(y-(map.getHeight()/2));
e.consume();
map.repaint();
}
else if ( (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 )
{
Point p = map.toTileCoordinates(e.getX(), e.getY());
if ( p == null )
{
System.out.println("Outside map");
}
else
{
java.util.List l = map.tiles[p.x][p.y];
l.set(0,is.list.get((Math.abs(rand.nextInt()))%360));
for ( int i =0; i < l.size(); i++ )
System.out.println("X=" + p.x + "    Y=" + p.y + "   "
+l.get(i));
map.repaintTiles(p.x,p.y,1,1);
}
}
}
} );
jf.getContentPane().add(map);
jf.pack();
jf.show();

}
*/
}
