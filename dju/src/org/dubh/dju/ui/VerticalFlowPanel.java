// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: VerticalFlowPanel.java,v 1.1 1999-06-01 00:17:35 briand Exp $
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

import java.awt.*;
import javax.swing.*;

/**
 * A panel that lays out its contents in a vertical flow. Each row fills to take
 * all available space horizontally, and its preferred size vertically. You can
 * add a row that takes all remainaing space if required, but you should only
 * add one of these.
 *
 * @author Brian Duff
 * @version $Id: VerticalFlowPanel.java,v 1.1 1999-06-01 00:17:35 briand Exp $
 */
public class VerticalFlowPanel extends JPanel 
{
   private int m_currentRow = 0;
   private int m_padding    = 1;
   
   private static final int INDENT_AMOUNT = 20;

   public VerticalFlowPanel()
   {
      setLayout(new GridBagLayout());
   }
   
   /**
    * Add a normal row. This row takes all available space horizontally, its preferred
    * size vertically.
    */
   public void addRow(Component c)
   {
      add(c, new GridBagConstraints2(0, m_currentRow++, 1, 1,  1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, 
         new Insets(m_padding, m_padding, m_padding, m_padding), 0, 0));  
   }
   
   /**
    * Add a row that is indented by a certain amount. This is for groups of radio buttons or
    * other items that are related.
    */
   public void addIndentRow(Component c)
   {
      add(c, new GridBagConstraints2(0, m_currentRow++, 1, 1,  1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, 
         new Insets(m_padding, m_padding+INDENT_AMOUNT, m_padding, m_padding), 0, 0));     
   }
   
   /**
    * Add a row that expands vertically to fill remaining space. It takes all available
    * space horizontally and all available space vertically.
    */
   public void addSpacerRow(Component c)
   {
      add(c, new GridBagConstraints2(0, m_currentRow++, 1, 1,  1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, 
         new Insets(m_padding, m_padding, m_padding, m_padding), 0, 0));  
   
   }
   
   /**
    * Add a blank area of a specific height.
    */
   public void addFixedGap(int pixels)
   {
      addRow(Box.createRigidArea(new Dimension(0, pixels)));
   }
   
   /**
    * Set the space around components. Affects all components added after you call this
    * method.
    */
   public void setSpacing(int spacing)
   {
      m_padding = spacing;
   }
   

}

//
// $Log: not supported by cvs2svn $
//