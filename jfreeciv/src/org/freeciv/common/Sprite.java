package org.freeciv.common;

import javax.swing.Icon;

public interface Sprite
{
   /**
    * Call this to load the specified sprite file. The file is not
    * processed until getIcon() or getSegmentIcon() is called.

    */
   public boolean loadFile(String filename);

   /**
    * Call this to retrieve an icon for the sprite. This will cause
    * the sprite object to instantiate and create a new object.
    */
   public Icon getIcon();

   /**
    * Call this to retrieve a portion of the image as an icon for
    * the sprite. This will cause the sprite object to instanitate
    * and create a new object.
    */
   public Icon getSegmentIcon(int x, int y, int w, int h);
}