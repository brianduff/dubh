// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhDialog.java,v 1.6 2001-02-11 02:52:11 briand Exp $
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
import java.io.*;

import java.awt.event.*;

import org.dubh.dju.misc.Debug;
import org.dubh.dju.DubhUtilsPreferences;

/**
 * Subclass of JDialog with some useful utiltity methods.
 * Version History: <UL>
 * <LI>0.1 [05/04/98]: Initial Revision [NewsAgent]
 * <LI>0.2 [08/05/98]: Moved to Dubh Utils.
 * <LI>0.3 [08/12/98]: Add support for storing / restoring location
 *</UL>
 @author Brian Duff
 @version 0.3 [08/12/98]
 */
public class DubhDialog extends JDialog {

   private Frame m_parent;

   private static final String s_DJUDLGKEY    =
      "DialogLocation.";


   public DubhDialog(Frame frame, String title, boolean modal) {
      super(frame, title, modal);
      m_parent = frame;
      restoreLocation();
      addWindowListener(new WindowAdapter() {
         public void windowClosing()
         {
            storeLocation();
         }
      });
   }

   public DubhDialog(Frame frame) {
      this(frame, "", false);
   }

   public DubhDialog(Frame frame, boolean modal) {
      this(frame, "", modal);
   }

   public DubhDialog(Frame frame, String title) {
      this(frame, title, false);
   }

  /**
   * Moves the dialog to the centre of the screen.
   */
   public void moveToCentre() {
      Dimension screen = this.getToolkit().getScreenSize();
      Dimension frame  = getSize();
      setLocation(screen.width/2 - frame.width/2, screen.height/2 - frame.height/2);
   }

  /**
   * Packs and shows the dialog at the centre of the screen.
   */
   public void showAtCentre() {
      super.pack();
      moveToCentre();
      show();
   }

   /**
    * Shows this dialog in the centre of its parent window.
    */
   public void showAtCentreOfParent() {
      Dimension screen = m_parent.getSize();
      Dimension frame  = getSize();
      setLocation(screen.width/2 - frame.width/2, screen.height/2 - frame.height/2);
      //pack();
      setVisible(true);
   }

   public void pack()
   {
      super.pack();
      restoreLocation();
   }

  public void setVisible(boolean b)
  {
     if (!b)
        storeLocation();
     super.setVisible(b);
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
         moveToCentre();
      }
   }

   public static void main(String[] args)
   {
      FunkyDialog test = new FunkyDialog();
      test.pack();
      test.setVisible(true);
   }
}

class FunkyDialog extends DubhDialog
{
   public FunkyDialog()
   {
      super(new JFrame(), "test", false);
      getContentPane().add(new JButton("Test"));
   }
}