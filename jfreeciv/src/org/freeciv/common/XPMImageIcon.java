package org.freeciv.common;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * An image icon that is an XPM image file.
 */
public class XPMImageIcon extends ImageIcon
{
  private XPMFile m_file;
  
  /**
   * Creates an XPMImageIcon from the specified file.
   */
  public XPMImageIcon( String filename ) 
  {
    m_file = new XPMFile( filename );
    createImage();
  }
  
  /**
   * Create an image icon from a previously loaded file.
   */
  public XPMImageIcon( XPMFile file ) 
  {
    m_file = file;
    createImage();
  }
  
  /**
   * Create an image icon from a previously loaded file, selecting
   * a specific area of the original image.
   */
  public XPMImageIcon( XPMFile file, int x, int y, int w, int h ) 
  {
    m_file = file;
    createImageSegment( x, y, w, h );
  }
  public XPMImageIcon( String filename, int x, int y, int w, int h ) 
  {
    m_file = new XPMFile( filename );
    createImageSegment( x, y, w, h );
  }
  private void createImageTest()
  {
    int w = 100;
    int h = 100;
    int pix[] = new int[ w * h ];
    int index = 0;
    for( int y = 0;y < h;y++ )
    {
      int red = ( y * 255 ) / ( h - 1 );
      for( int x = 0;x < w;x++ )
      {
        int blue = ( x * 255 ) / ( w - 1 );
        pix[ index++ ] = ( 255 << 24 ) | ( red << 16 ) | blue;
      }
    }
    setImage( Toolkit.getDefaultToolkit().createImage( new MemoryImageSource( w, h, pix, 0, w ) ) );
  }
  private void createImage( int[] pix, int w, int h )
  {
    Image i = Toolkit.getDefaultToolkit().createImage( new MemoryImageSource( w, h, pix, 0, w ) );
    setImage( i );
  }
  private void createImage()
  {
    createImage( m_file.getData(), m_file.getColumnCount(), m_file.getRowCount() );
  }
  private void createImageSegment( int x, int y, int w, int h )
  {
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "Getting image segment " + x + ", " + y + " " + w + ", " + h + "." );
    }
    int[] fullPix = m_file.getData();
    
    // Work out which bit of the array to slurp from.
    int[] segPix = new int[ w * h ];
    int destOffset = 0;
    int origW = m_file.getColumnCount();
    int origH = m_file.getRowCount();
    int startArray = ( origW * y ) + x;
    for( int i = 0;i < h;i++ )
    {
      try
      {
        System.arraycopy( fullPix, startArray, segPix, destOffset, w );
      }
      catch( ArrayIndexOutOfBoundsException ioe )
      {
        // trident/explosions.xpm is 2 pixels shorter vertically than it should be.
        // this makes us more resilient to such problems.
        Logger.log( Logger.LOG_NORMAL, "Warning: XPM File " + m_file.getFilename() + " is not the size I expected it to be" );
      }
      destOffset += w;
      startArray += origW; // next row.
    }
    createImage( segPix, w, h );
  }
  public static void main( String[] args )
  {
    JFrame test = new JFrame();
    test.setLocation( 100, 100 );
    JLabel lab = new JLabel();
    int x, y, w, h;
    String fname = args[ 0 ];
    if( args.length > 1 )
    {
      x = Integer.parseInt( args[ 1 ] );
      y = Integer.parseInt( args[ 2 ] );
      w = Integer.parseInt( args[ 3 ] );
      h = Integer.parseInt( args[ 4 ] );
      lab.setIcon( new XPMImageIcon( args[ 0 ], x, y, w, h ) );
    }
    else
    {
      lab.setIcon( new XPMImageIcon( args[ 0 ] ) );
    }
    test.getContentPane().setLayout( new java.awt.BorderLayout() );
    test.getContentPane().add( lab, java.awt.BorderLayout.CENTER );
    test.pack();
    test.setVisible( true );
  }
}
