// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ColorListCellRenderer.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
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