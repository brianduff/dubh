// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: VerticalFlowPanel.java,v 1.3 2000-06-14 21:25:22 briand Exp $
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
 * A panel that lays out its contents in a vertical flow. Each row fills to take
 * all available space horizontally, and its preferred size vertically. You can
 * add a row that takes all remainaing space if required, but you should only
 * add one of these.
 *
 * @author Brian Duff
 * @version $Id: VerticalFlowPanel.java,v 1.3 2000-06-14 21:25:22 briand Exp $
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
    * Adds an empty row that expands vertically to fill remaining space. It
    * takes all available space horizontally and all available space
    * vertically, and does not have any contents.
    */
   public void addSpacerPadding()
   {
      addSpacerRow(new JPanel());
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
// Revision 1.2  1999/11/11 21:24:36  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/06/01 00:17:35  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//