package org.freeciv.common;

import com.sixlegs.image.png.PngImage;

import java.awt.Image;
import java.awt.Toolkit;

import java.io.IOException;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * A PNGSprite uses the sixlegs PNG library to load sprites. Java's
 * builtin PNG handler doesn't seem to handle some of the PNG files
 * shipped with freeciv
 *
 * @author Brian Duff
 */
public final class PNGSprite extends JavaImageSprite
{
  /**
   * Construct a PNGSprite based on the specified URL
   *
   * @param u the URL of the image file
   */
  public PNGSprite( URL u )
  {
    try
    {
      Image i = Toolkit.getDefaultToolkit().createImage(
        new PngImage( u )
      );
      m_baseImageIcon = new ImageIcon( i );
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
      ErrorHandler.getHandler().error( null, "Failed to load PNG image from "+u );
    }
  }
}