package org.freeciv.common;

import javax.swing.Icon;

public interface Sprite
{

  
  /**
   * Call this to retrieve an icon for the sprite.
   */
  public Icon getIcon();
  
  /**
   * Call this to retrieve a portion of the image as an icon for
   * the sprite.
   */
  public Icon getSegmentIcon( int x, int y, int w, int h );
}
