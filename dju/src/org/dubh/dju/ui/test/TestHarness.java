// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: TestHarness.java,v 1.1 1999-08-03 19:14:14 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package dubh.utils.ui.test;

import dubh.utils.ui.*;
import javax.swing.*;

import java.awt.*;

/**
 * Dubh Utils UI test harness
 *
 * @author Brian Duff
 * @version $Id: TestHarness.java,v 1.1 1999-08-03 19:14:14 briand Exp $
 */
public class TestHarness
{
   public static void main(String[] args)
   {
      try
      {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");   
      }
      catch (Throwable t)
      {}
      //testColorSwatch();
      //testHSB();
      testJava12Fonts();
   }
   
   public static void testJava12Fonts()
   {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      
      String[] f = ge.getAvailableFontFamilyNames();
      
      for (int i=0; i < f.length; i++)
      {
         System.out.println("Font "+i+": "+f[i]);
      }
   }
   
   public static void testHSB()
   {
      JPanel pan = new JPanel();
      
      pan.setLayout(new FlowLayout());
      
      Color c = Color.red;
      
      pan.add(new ColorSwatch(c, false));
      
      float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
      
      System.out.println("HSB of red: "+hsb[0]+", "+hsb[1]+", "+hsb[2]);
      
      
      
      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);    
      pan.add(new ColorSwatch(c, false));
      
      hsb[1] = (float)1.0;
            
      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);    
      pan.add(new ColorSwatch(c, false));

      hsb[1] = (float)0.1;
      
      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);    
      pan.add(new ColorSwatch(c, false));
            
      
      
      showFrame(pan);
   }

   public static void showFrame(JPanel p)
   {
      JFrame f = new JFrame();
      
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(p, BorderLayout.CENTER);
      
      f.pack();
      f.setVisible(true);
   }

   public static void testColorSwatch()
   {
      JFrame fraSwatchFrame = new JFrame();
      JPanel pan = new JPanel();
      
      Color[][] winColors = ColorPopup.generateDefaultColorSet(ColorPopup.DEFAULT_BASECOLORS);
      
      /*{
         {
         new Color(255, 255, 255),
         new Color(0, 0, 0),
         new Color(192, 192, 192),
         new Color(128, 128, 128)
         },
         {
         new Color(255, 0, 0),
         new Color(128, 0, 0),
         new Color(255, 255, 0),
         new Color(128, 128, 0)
         },
         {
         new Color(0, 255, 0),
         new Color(0, 128, 0),
         new Color(0, 255, 255),
         new Color(0, 128, 128)
         },
         {
         new Color(0, 0, 255),
         new Color(0, 0, 128),
         new Color(255, 0, 255),
         new Color(128, 0, 128)
         },
         {
         new Color(127, 127, 127),
         new Color(166, 202, 240),
         new Color(224, 224, 224),
         new Color(160, 160, 164)
         }
      };
      */
      
      fraSwatchFrame.getContentPane().setLayout(new BorderLayout());
      
      GridLayout gl = new GridLayout(winColors.length, winColors[0].length, 0, 0);
      pan.setLayout(gl);
      
      for (int i=0; i < winColors.length; i++)
      {
         for (int j=0; j < winColors[0].length; j++)
         {
            pan.add(new ColorSwatch(winColors[i][j], false, ColorSwatch.STYLE_MSWORD_POPUP));
         }
      }
      
      fraSwatchFrame.getContentPane().add(pan, BorderLayout.WEST);
      
      fraSwatchFrame.pack();
      fraSwatchFrame.setVisible(true);
   }

}