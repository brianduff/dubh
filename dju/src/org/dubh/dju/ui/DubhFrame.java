/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.utils.ui;
import javax.swing.*;
import java.awt.*;
import dubh.utils.misc.*;
import java.awt.event.*;
import java.io.*;

import dubh.utils.DubhUtilsPreferences;

/**
 * Subclass of JFrame with some useful utiltity methods.
 * Version History: <UL>
 * <LI>0.1 [08/03/98]: Initial Revision
 * <LI>0.2 [07/04/98]: Sometimes, there was a bug when saving the window
 *   position: -32000 was written to the file instead of the correct posn,
 *   making windows restore offscreen. Under WinNT, there's no way to
 *   get the window to appear without editing the properties file and
 *   restarting.
 *   Fixed so that the window now pops up in centre if its x-coordinate < 0.
 * <LI>0.3 [06/06/98]: Added dubh utils import for StringUtils
 * <LI>0.4 [17/06/98]: Moved to Dubh Utils (from NewsAgent) and renamed to
 *   DubhFrame.
 * <LI>0.5 [18/06/98]: Added checking for m_id == null.
 * <LI>1.0 [08/12/98]: Changed to use class as restore location key.
 *</UL>
 @author Brian Duff
 @version 1.0 [08/12/98]
 */
public class DubhFrame extends JFrame {

  private static final String s_DJUDLGKEY    = "FrameLocation.";


   /**
    * Default Constructor. Constructs a frame and moves it to a restored
    * location
    */
  public DubhFrame() {
     super();
     restoreLocation();
     addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e)
        {
           storeLocation();
        }
     });
  }

  /**
   * Constructs a DubhFrame with the specified string id. This ID will be used
   * to store the position of the window when it is closed. This constructor
   * uses the default property file to store the window's position -
   * dubhutils.properties in the user's home directory. This constructor packs
   * and moves the window to the correct position (or the centre of the screen
   * if there is a problem) but doesn't display it.
   * @param id the id to store the window's position with.
   * @deprecated Windows now use the default dubh utils preferences file, and 
   *   use their class name as an id.

   */
  public DubhFrame(String id) {
     this();
  }

  /**
   * Constructs a DubhFrame with the specified string id. This constructor packs
   * and moves the window to the correct position (or the centre of the screen
   * if there is a problem) but doesn't display it.
   * @param id the id to store the window's position with.
   * @param preffile the preferences file to use
   * @deprecated Windows now use the default dubh utils preferences file, and 
   *   use their class name as an id.
   */
  public DubhFrame(String id, String preffile) {
     this();
  }

   /**
   * Moves the frame to the centre of the screen.
   */
  public void moveToCenter() {
     Dimension screen = this.getToolkit().getScreenSize();
     Dimension frame  = getSize();
     setLocation(screen.width/2 - frame.width/2, screen.height/2 - frame.height/2);
  }

  /**
   * Packs and shows the window at the centre of the screen.
   */
  public void showAtCentre() {
     super.pack();
     moveToCenter();
     setVisible(true);
  }


  /**
   * Sets whether the frame is visible. If you are making the frame invisible,
   * it's location is stored.
   */
  public void setVisible(boolean b) {
     if (!b) storeLocation();
     super.setVisible(b);
  }

  /**
   * Hides the window and stores it's position. N.b. unlike previous versions
   * of this class, you don't need to call this method: it is done automatically
   * when the window is closing.
   * @deprecated No need to ever call this any more.
   */
  public void hideAndStore() {
     setVisible(false);
  }

  /**
   * Move to a stored location
   * @deprecated window is automatically moved to a stored location on pack
   * @see pack()
   */
  public void moveToStoredLocation() {
     restoreLocation();
  }

  public void pack()
  {
     super.pack();
     restoreLocation();
  }
  

   /**
    * store our location in the dubhutils properties file.
    */
   private void storeLocation()
   {
      DubhUtilsPreferences dup = DubhUtilsPreferences.getPreferences();
      String basekey = s_DJUDLGKEY+getClass().toString().substring(6);
      
      dup.getPreferences().setIntPreference(
         basekey+".x", getLocation().x
      );
      dup.getPreferences().setIntPreference(
         basekey+".y", getLocation().y
      );
      dup.getPreferences().setIntPreference(
         basekey+".w", getSize().width
      );
      dup.getPreferences().setIntPreference(
         basekey+".h", getSize().height
      );
      try
      {
         dup.save();
      }
      catch (IOException ioe)
      {
         Debug.println("Can't save dialog location in dubh utils properties.");
      }
   }
   
   /**
    * restore our location from the dubhutils properties file
    */
   private void restoreLocation()
   {
      DubhUtilsPreferences dup = DubhUtilsPreferences.getPreferences();
      String basekey = s_DJUDLGKEY+getClass().toString().substring(6);
      try
      {
         int x = Math.max(0, dup.getIntPreference(
            basekey+".x"
         ));
         int y = Math.max(0, dup.getIntPreference(
            basekey+".y"
         ));
         int w = Math.max(0, dup.getIntPreference(
            basekey+".w"
         ));
         int h = Math.max(0, dup.getIntPreference(
            basekey+".h"
         ));
      
      
         setLocation(new Point(x, y));
         setSize(new Dimension(w, h));
      }
      catch (NumberFormatException nfe)
      {
         moveToCenter();
      }
   }  

}