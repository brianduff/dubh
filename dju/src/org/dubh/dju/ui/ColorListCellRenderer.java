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
 *
 * Please note that this software is not in any way endorsed by
 * Oracle Corporation
 * Version History:
 *  FV   DUV    Date          Who    What
 *  ======================================================================
 *  0.0  1.1.00 [12/Dec/1998] BD     Initial Revision
 *
 */

package dubh.utils.ui;

// Core Java Imports
import java.awt.*;
import java.util.*;

// Swing Imports
import javax.swing.*;


// Dubh Utils Imports

// FESI Imports

/**
 * <p>
 * A List cell renderer that can be used for lists of colors; uses a 
 * colored block next to the string. If the color is one of the standard
 * constant colors, its name is also displayed.
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.00) [12/Dec/1998]
 */
public class ColorListCellRenderer extends DefaultListCellRenderer
{
   
   private Hashtable m_colorMapping;

   /************************************************************************
   *** CONSTRUCTORS
   *************************************************************************/
   public ColorListCellRenderer()
   {
      m_colorMapping = new Hashtable();
      m_colorMapping.put(Color.black, "Black");
      m_colorMapping.put(Color.blue,  "Blue"); 
      m_colorMapping.put(Color.cyan,  "Cyan");
      m_colorMapping.put(Color.darkGray, "Dark Gray"); 
      m_colorMapping.put(Color.gray, "Gray");
      m_colorMapping.put(Color.green, "Green");
      m_colorMapping.put(Color.lightGray, "Light Gray");
      m_colorMapping.put(Color.magenta, "Magenta");
      m_colorMapping.put(Color.orange, "Orange"); 
      m_colorMapping.put(Color.pink, "Pink");
      m_colorMapping.put(Color.red, "Red");
      m_colorMapping.put(Color.white, "White"); 
      m_colorMapping.put(Color.yellow, "Yellow");
   }



   /************************************************************************
   *** PUBLIC INTERFACE
   *************************************************************************/
   
   public Component getListCellRendererComponent(JList list,
                                              Object value,
                                              int index,
                                              boolean isSelected,
                                              boolean cellHasFocus)
   {
      JLabel lab = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      lab.setIcon(new ColorBlockIcon((Color)value));
      lab.setText(getNameForColor((Color)value));
      return lab;
   }

   
   public String getNameForColor(Color c)
   {
      try
      {
         return (String) m_colorMapping.get(c);
      }
      catch (Throwable t)
      {
         return "";
      }
   }

   /************************************************************************
   *** INNER CLASSES
   *************************************************************************/
   
   class ColorBlockIcon implements Icon
   {
      private final int s_SIZE = 15;
      private Color m_color;
      
      public ColorBlockIcon(Color c)
      {
         m_color = c;
      }
      
      
      public int getIconWidth()
      {
         return s_SIZE;   
      }
      
      public int getIconHeight()
      {
         return s_SIZE;
      }
      
      public void paintIcon(Component c, Graphics g, int x, int y)
      {
         g.setColor(m_color);
         g.fillRect(x, y, x+s_SIZE, y+s_SIZE-1);
         g.setColor(Color.black);
         g.drawRect(x, y, x+s_SIZE, y+s_SIZE-1);
      }
   
   }

}