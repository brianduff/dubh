// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: IconicPreferencePanel.java,v 1.2 1999-11-11 21:24:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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
package org.javalobby.dju.ui;

import java.awt.*;
import javax.swing.*;

/**
 * A panel that is headed by a {@link #org.javalobby.dju.ui.SeparatorBar}, indented
 * by a fixed amount with an optional icon. Icons should be 32x32.
 * <p>
 * Valid NLS resources are <<ippname>>.Header.text for the title
 * and <<ippname>>.Image.icon (for the icon to be displayed). 
 * All controls added to the panel are contained in a panel called 
 * <<ippname>>.Controls.
 *
 * @author Brian Duff
 * @version $Id: IconicPreferencePanel.java,v 1.2 1999-11-11 21:24:35 briand Exp $
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
// Revision 1.1  1999/06/01 00:17:34  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//