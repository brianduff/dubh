// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhFrame.java,v 1.6 2001-02-11 02:52:11 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package org.dubh.dju.ui;
import javax.swing.*;
import java.awt.*;
import org.dubh.dju.misc.*;
import java.awt.event.*;
import java.io.*;

import org.dubh.dju.DubhUtilsPreferences;

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
 * Version history since March 1999 is in the CVS repository
 * @author Brian Duff
 */
public class DubhFrame extends JFrame {

  private static final String s_DJUDLGKEY    = "FrameLocation.";


   /**
    * Default Constructor. Constructs a frame and moves it to a restored
    * location
    */
  public DubhFrame()
  {
     this("");
  }

  public DubhFrame(String title)
  {
     super(title);
     restoreLocation();
     addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e)
        {
           storeLocation();
        }
     });
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

      //
      // If the frame has a name, use that as part of the key
      //
      String name = getName();
      if (name != null && !name.equals(""))
      {
         basekey = basekey+"."+name;
      }

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

      String name = getName();
      String nameKey;

      //
      // If this frame has a name, and their is a preference for that named
      // frame, use this preference. Otherwise, use the generic preference
      // for all frames of this class.
      //
      if (name != null && !name.equals(""))
      {
         nameKey = basekey+"."+name;
         Object p = dup.getPreference(nameKey+".x");
         if (p != null)   basekey = nameKey;
      }

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