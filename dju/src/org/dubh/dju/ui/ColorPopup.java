// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ColorPopup.java,v 1.1 1999-08-03 19:13:47 briand Exp $
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
package dubh.utils.ui;

import javax.swing.JPanel;

import java.awt.Frame;
import java.awt.Color;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Vector;

/**
 * A color popup menu.
 *
 * @author Brian Duff
 * @version $Id: ColorPopup.java,v 1.1 1999-08-03 19:13:47 briand Exp $
 */
public class ColorPopup extends Window 
{
   private Color m_colCurrent;

   // The default color set has varying shades of the main java colors.

   public static final Color[] DEFAULT_BASECOLORS = 
      {
      Color.gray,
      Color.red,
      Color.green, 
      Color.blue,
      Color.cyan,
      Color.magenta,
      Color.yellow,
      Color.orange
      };
   
   private static int COLUMNS = 5;
   
   public static Color[][] generateDefaultColorSet(Color[] baseColors)
   {
      if ((COLUMNS % 2) == 0) 
         throw new IllegalStateException("The number of columns must be odd!");
   
      Color[][] c = new Color[baseColors.length][COLUMNS];
            
      float[] hsbstore = new float[3];      
      for (int i=0; i < baseColors.length; i++)
      {
         c[i][(COLUMNS/2)+1] = baseColors[i];
         // Special handling for grey to make sure we get white and black.
         
         int firstCol = 0; 
         int lastCol = COLUMNS-1;
         Color current = baseColors[i];
         hsbstore = Color.RGBtoHSB(current.getRed(), current.getGreen(), current.getBlue(), hsbstore);
         
         if (current.equals(Color.gray))
         {
            
            for (int j=0; j < COLUMNS; j++)
            {
               hsbstore[2] = (float)j/COLUMNS;   
               
               c[i][j] = Color.getHSBColor(hsbstore[0], hsbstore[1], hsbstore[2]);
            }
            c[i][COLUMNS/2+1] = Color.gray;
         }
         else
         {
            System.out.println("Row "+i+" base brightness = "+hsbstore[2]);
            float downdiff = (float)(hsbstore[2]-0.1 / (COLUMNS/2));
            float updiff   = (float)(0.9 - hsbstore[2] / (COLUMNS/2));
            float fBrightness = (float)0.5;
            for (int k=COLUMNS/2; k >= 0; k--)
            {
               fBrightness -= downdiff;
               c[i][k] = Color.getHSBColor(hsbstore[0], hsbstore[1], fBrightness);
               System.out.println("["+i+", "+k+"] HSB: "+hsbstore[0]+" "+hsbstore[1]+" "+fBrightness);
            }
            
            fBrightness = (float) 0.5;
            for (int l=COLUMNS/2+2; l < COLUMNS; l++)
            {
               fBrightness += updiff;
               c[i][l] = Color.getHSBColor(hsbstore[0], hsbstore[1], fBrightness);
               System.out.println("["+i+", "+l+"] HSB: "+hsbstore[0]+" "+hsbstore[1]+" "+fBrightness);

            }
         
         }
        
      }
            
      return c;
      
   }


   /**
    * Construct a color popup
    */
   public ColorPopup(Window owner, Color current)
   {
      super(owner);
      m_colCurrent = current;
   }

   /**
    * Construct a color popup
    */
   public ColorPopup(Frame owner, Color current)
   {
      super(owner);
      m_colCurrent = current;
   }



}