package org.freeciv.common;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.ImageFilter;
import java.awt.image.FilteredImageSource;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A JavaImageSprite uses Java's built in image handlers to construct a
 * Sprite.
 *
 * @author Brian Duff
 */
public final class JavaImageSprite implements Sprite
{
  private ImageIcon m_baseImageIcon;

  /**
   * Construct a JavaImageSprite based on the specified URL
   *
   * @param u the URL of the image file
   */
  public JavaImageSprite( URL u )
  {
    m_baseImageIcon = new ImageIcon( u );
  }

  public Icon getIcon()
  {
    return m_baseImageIcon;
  }

  /**
   * Call this to retrieve a portion of the image as an icon for
   * the sprite.
   */
  public Icon getSegmentIcon( int x, int y, int w, int h )
  {
    Image srcImage = m_baseImageIcon.getImage();
    ImageFilter cropFilter = new CropImageFilter( x, y, w, h );
    FilteredImageSource fis = new FilteredImageSource(srcImage.getSource(),
      cropFilter
    );
    Image destImage = Toolkit.getDefaultToolkit().createImage( fis );

    return new ImageIcon( destImage );
  }
}