// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NFrame.java,v 1.3 1999-03-22 23:46:00 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.apps.newsagent.dialog;
import javax.swing.*;
import java.awt.*;
import dubh.apps.newsagent.GlobalState;
import dubh.utils.misc.StringUtils;

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
 *</UL>
 @author Brian Duff
 @version 0.3 [06/06/98]
 */
public class NFrame extends JFrame {


   /**
    * Default Constructor. Enter Description Here.
    */
    public NFrame() {
      super();
    }

   /**
   * Moves the frame to the centre of the screen.
   */
  public void moveToCentre() {
   Dimension screen = this.getToolkit().getScreenSize();
    Dimension frame  = getSize();
    setLocation(screen.width/2 - frame.width/2, screen.height/2 - frame.height/2);  
  }

  /**
   * Packs and shows the window at the centre of the screen.
   */
  public void showAtCentre() {
    pack();
    moveToCentre();
    show();
  }

  /**
   * Stores the position and size of this Frame in the preferences file. Ideally,
   * called just before program termination on all windows (?)
   */
  public void storeLocation(String id) {
   String left = "newsagent.windows."+id+".";
   GlobalState.setPreference(left+"x", StringUtils.intToString(getLocation().x));
    GlobalState.setPreference(left+"y", StringUtils.intToString(getLocation().y));
    GlobalState.setPreference(left+"w", StringUtils.intToString(getSize().width));
    GlobalState.setPreference(left+"h", StringUtils.intToString(getSize().height));
      GlobalState.savePreferences();
  }

  /**
   * Hides the window and stores it's position.
   */
  public void hideAndStore(String id) {
   storeLocation(id);
    setVisible(false);
  }

  /**
   * Moves the window to the position stored in the preferences file. If the
   * values are not stored, the window will be centered.
   */
  public void moveToStoredLocation(String id) {
   String left = "newsagent.windows."+id+".";
   int x = StringUtils.stringToInt(GlobalState.getPreference(left+"x", "-1000"));
    if (x < 0)
      moveToCentre();
    else {
      int y = StringUtils.stringToInt(GlobalState.getPreference(left+"y", "0"));
      int w = StringUtils.stringToInt(GlobalState.getPreference(left+"w", "100"));
      int h = StringUtils.stringToInt(GlobalState.getPreference(left+"h", "100"));
      setLocation(new Point(x, y));
      setSize(new Dimension(w, h));
    }

  }
         /**
     * Moves the window to the position stored in the preferences file, packs
     * and shows it. If the
     * values are not stored, the window will be centered.
     */
    public void showAtStoredLocation(String id) {
      pack();
      moveToStoredLocation(id);
      show();
    }


}