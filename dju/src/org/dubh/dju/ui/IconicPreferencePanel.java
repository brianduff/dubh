// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: IconicPreferencePanel.java,v 1.1 1999-06-01 00:17:34 briand Exp $
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
 * A panel that is headed by a {@link #dubh.utils.ui.SeparatorBar}, indented
 * by a fixed amount with an optional icon. Icons should be 32x32.
 * <p>
 * Valid NLS resources are <<ippname>>.Header.text for the title
 * and <<ippname>>.Image.icon (for the icon to be displayed). 
 * All controls added to the panel are contained in a panel called 
 * <<ippname>>.Controls.
 *
 * @author Brian Duff
 * @version $Id: IconicPreferencePanel.java,v 1.1 1999-06-01 00:17:34 briand Exp $
 */
public class IconicPreferencePanel extends JPanel 
{
   private SeparatorBar m_sbHeading;
   private JLabel       m_labIcon;
   private VerticalFlowPanel m_controls;
   private int          m_currentRow;
   
   private final static int SPACE=40;

   public IconicPreferencePanel(String text, ImageIcon icon)
   {
      m_sbHeading = new SeparatorBar(text);
      m_sbHeading.setName("Header");
      m_labIcon = new JLabel();
      m_labIcon.setName("Image");
      m_labIcon.setMinimumSize(new Dimension(SPACE, SPACE));
      m_labIcon.setPreferredSize(new Dimension(SPACE, SPACE));
      m_controls = new VerticalFlowPanel();
      m_controls.setName("Controls");
      m_currentRow = 0;
      
      setLayout(new BorderLayout());
      add(m_sbHeading, BorderLayout.NORTH);
      add(m_labIcon, BorderLayout.WEST);
      add(m_controls, BorderLayout.CENTER);
      
      if (icon != null) setIcon(icon);
   
   }
   
   public IconicPreferencePanel(String text)
   {
      this(text, null);
   }
   
   public IconicPreferencePanel()
   {
      this("", null);
   }
   
   
   public void setText(String text)
   {
      m_sbHeading.setText(text);
   }
   
   public String getText()
   {
      return m_sbHeading.getText();
   }
   
   public void setIcon(ImageIcon icon)
   {
      m_labIcon.setIcon(icon);
   }
   
   public Icon getIcon()
   {
      return m_labIcon.getIcon();
   }
   
   /**
    * Return the control area of the panel. You should add rows of 
    * components to this using the add() method.
    */
   public VerticalFlowPanel getContainer()
   {
      return m_controls;
   }   
}

//
// $Log: not supported by cvs2svn $
//