// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ColorSwatch.java,v 1.2 1999-11-11 21:24:35 briand Exp $
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

import javax.swing.JPanel;

import java.awt.Frame;
import java.awt.Color;
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
 * A component that represents a color and can be hover-selected
 * @author Brian Duff
 * @version $Id: ColorSwatch.java,v 1.2 1999-11-11 21:24:35 briand Exp $
 */
public class ColorSwatch extends JPanel
{
   public static final int STYLE_EXPLORER_POPUP = 0;
   public static final int STYLE_MSWORD_POPUP = 1;
   public static final int STYLE_EXPLORER_COLORDLG = 2;

   private Color m_colColor;
   protected boolean m_bSelected;
   protected boolean m_bHovering;
   private Vector m_vecListeners;
   private SwatchStyle m_ssDrawer;
   /**
    * The same size as the one from Windows
    */
   private static final int SIZE = 18;
   private static final Dimension SIZE_DIM = new Dimension(SIZE, SIZE);
   
   private  static SwatchStyle STYLE_EXPLORER_POPUP_IMPL = null;
   private static  SwatchStyle STYLE_MSWORD_POPUP_IMPL = null;
   private static  SwatchStyle STYLE_EXPLORER_COLORDLG_IMPL = null;



   /**
    * Construct a color swatch. The "selected" state indicates that this
    * swatch is currently selected. This changes whenever the mouse leaves
    * or enters the component.
    */
   public ColorSwatch(Color color, boolean selected)
   {
      this(color, selected, STYLE_EXPLORER_POPUP);
   }
   
   public ColorSwatch(Color color, boolean selected, int style)
   {
      m_colColor = color;
      m_bSelected = selected;
      m_bHovering = false;
      addMouseListener(new EntryListener());
      m_vecListeners = new Vector();
      setPresetSwatchStyle(style);
   }
   
   public void setPresetSwatchStyle(int type)
   {
      switch (type)
      {
         case STYLE_EXPLORER_POPUP:
            if (STYLE_EXPLORER_POPUP_IMPL == null) 
               STYLE_EXPLORER_POPUP_IMPL = new ExplorerPopupSwatchStyle();
            m_ssDrawer = STYLE_EXPLORER_POPUP_IMPL;
            break;
         case STYLE_MSWORD_POPUP:
            if (STYLE_MSWORD_POPUP_IMPL == null) 
               STYLE_MSWORD_POPUP_IMPL = new MSWordPopupSwatchStyle();
            m_ssDrawer = STYLE_MSWORD_POPUP_IMPL;
            break;
         case STYLE_EXPLORER_COLORDLG:
            if (STYLE_MSWORD_POPUP_IMPL == null) 
               STYLE_MSWORD_POPUP_IMPL = new ExplorerPopupSwatchStyle();
            m_ssDrawer = STYLE_EXPLORER_COLORDLG_IMPL;
            break;
      }
      repaint();   
   }
   
   public void setCustomSwatchStyle(SwatchStyle s)
   {
      m_ssDrawer = s;
      repaint();
   }
   
   /**
    * Add a listener. You will be informed when the swatch is clicked on. You
    * can call getSource() on the event to get the swatch instance that fired
    * the event.
    */
   public void addActionListener(ActionListener al)
   {
      m_vecListeners.addElement(al);
   }
   
   public void removeActionListener(ActionListener al)
   {
      m_vecListeners.removeElement(al);
   }   
   
   /**
    * Notify listeners that the swatch was clicked on
    */
   protected void fireActionEvent()
   {
      ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
      
      for (int i=0; i<m_vecListeners.size(); i++)
      {
         ((ActionListener)m_vecListeners.elementAt(i)).actionPerformed(ae);
      }
   }
   
   /**
    * Set the swatch selected
    */
   public void setHovering(boolean b)
   {
      m_bHovering = b;
      repaint();
   }
   
   /**
    * Find out if the swatch is selected
    */
   public boolean isHovering()
   {
      return m_bHovering;
   }
   
   public void setSelected(boolean b)
   {
      m_bSelected = b;
      repaint();

   }

   /**
    * Find out if the swatch is selected
    */
   public boolean isSelected()
   {
      return m_bSelected;
   }
   
   /**
    * Get the color that this swatch displays
    */
   public Color getColor()
   {
      return m_colColor;
   }
   
   
   /**
    * Returns the size of the swatch including its borders. Overridden
    * from the superclass
    */
   public Dimension getPreferredSize()
   {
      return SIZE_DIM;
   }

   /**
    * Returns the size of the swatch including its borders. Overridden
    * from the superclass
    */      
   public Dimension getMinimumSize()
   {
      return SIZE_DIM;
   }
   
   /**
    * Returns the size of the swatch including its borders. Overridden
    * from the superclass
    */      
   public Dimension getMaximumSize()
   {
      return SIZE_DIM;
   }
   
 
   /**
    * Paint the swatch and its borders. Overridden from the superclass
    */
   public void paintComponent(Graphics g)
   {
      g.setColor(SystemColor.control);
      g.fillRect(0, 0, SIZE_DIM.width, SIZE_DIM.height);
   
      if (m_bSelected && m_bHovering)
      {
         m_ssDrawer.paintHoverSelected(m_colColor, g);
      }
      else if (m_bSelected)
      {
         m_ssDrawer.paintSelected(m_colColor, g);
      }
      else if (m_bHovering)
      {
         m_ssDrawer.paintHover(m_colColor, g);
      } 
      else
      {
         m_ssDrawer.paintNormal(m_colColor, g);
      }
    
   }
   
   /**
    * Listener for mouse events
    */
   class EntryListener extends MouseAdapter
   {
      public void mouseEntered(MouseEvent e)
      {
         setHovering(true);
      }
      
      public void mouseExited(MouseEvent e)
      {
         setHovering(false);
      }
      
      public void mouseClicked(MouseEvent e)
      {
         fireActionEvent();
         setSelected(true);
      }
   }

   /**
    * Interface that swatch UIs should implement
    */
   public interface SwatchStyle
   {
      public void paintNormal(Color c, Graphics g);
      public void paintHover(Color c, Graphics g);
      public void paintSelected(Color c, Graphics g);
      public void paintHoverSelected(Color c, Graphics g);
   }
   
   class MSWordPopupSwatchStyle implements SwatchStyle
   {
      private final Color BORDER = SystemColor.controlShadow;
      private final Color LIGHT  = SystemColor.controlLtHighlight;
      private final Color DARK   = SystemColor.controlShadow;
   
      private void paintSwatch(Color c, Graphics g)
      {
         g.setColor(c);
         g.fillRect(4, 4, SIZE_DIM.width - 8, SIZE_DIM.height - 8);      
         g.setColor(BORDER);
         g.drawRect(3, 3, SIZE_DIM.width - 7, SIZE_DIM.height - 7);

      }
   
      public void paintNormal(Color c, Graphics g)
      {
         paintSwatch(c, g);
      }
      
      private void drawBorder(Graphics g, boolean raised)
      {
         g.setColor(raised ? LIGHT : DARK);
         g.drawLine(0, 0, SIZE_DIM.width-2, 0);
         g.drawLine(0, 0, 0, SIZE_DIM.height-2);
         g.setColor(raised ? DARK : LIGHT);
         g.drawLine(0, SIZE_DIM.height-1, SIZE_DIM.width-1, SIZE_DIM.height -1);
         g.drawLine(SIZE_DIM.width-1, SIZE_DIM.height -1, SIZE_DIM.width-1, 0);         
      }
      
      public void paintHover(Color c, Graphics g)
      {
         paintSwatch(c, g);
         drawBorder(g, true);
      }
      
      public void paintSelected(Color c, Graphics g)
      {
         g.setColor(LIGHT);
         g.fillRect(0, 0, SIZE_DIM.width, SIZE_DIM.height);
         paintSwatch(c, g);
         drawBorder(g, false);
      }
      
      public void paintHoverSelected(Color c, Graphics g)
      {
         paintSwatch(c, g);
         drawBorder(g, false);
      }
   }
   
   /**
    * The swatch style used by popup color menus in Windows explorer
    */
   class ExplorerPopupSwatchStyle implements SwatchStyle
   {
      private  final Color SEL_BORDER_OUTER = Color.black;
      private  final Color SEL_BORDER_INNER = Color.white;
      
      private  final Color NORMAL_BORDER_OUTER_TOP = SystemColor.controlShadow;
      private  final Color NORMAL_BORDER_INNER_TOP = SystemColor.controlDkShadow;
      
      private  final Color NORMAL_BORDER_OUTER_BOTTOM = SystemColor.controlLtHighlight;
      private  final Color NORMAL_BORDER_INNER_BOTTOM = SystemColor.controlHighlight;

      public void paintNormal(Color c, Graphics g)
      {
         paintDepressedBorder(g);
         paintSwatch(c, g);
      }
      
      public void paintHover(Color c, Graphics g)
      {
         paintSelectedBorder(g);
         paintSwatch(c, g);
      
      }
      
      public void paintSelected(Color c, Graphics g)
      {
         paintHover(c, g);
      }
      
      public void paintHoverSelected(Color c, Graphics g)
      {
         paintHover(c, g);
      }      
            
      private void paintSelectedBorder(Graphics g)
      {
         g.setColor(SEL_BORDER_OUTER);
         g.drawRect(0, 0, SIZE_DIM.width-1, SIZE_DIM.height-1);
         g.drawRect(2, 2, SIZE_DIM.width-5, SIZE_DIM.height-5);
         
         g.setColor(SEL_BORDER_INNER);
         g.drawRect(1, 1, SIZE_DIM.width-3, SIZE_DIM.height-3);
         
      }
      
      private void paintDepressedBorder(Graphics g)
      {
         g.setColor(NORMAL_BORDER_OUTER_TOP);
         g.drawLine(1, 1, SIZE_DIM.width-2, 1);
         g.drawLine(1, 1, 1, SIZE_DIM.height-2);
         
         g.setColor(NORMAL_BORDER_INNER_TOP);
         g.drawLine(2, 2, SIZE_DIM.width-3, 2);
         g.drawLine(2, 2, 2, SIZE_DIM.height-3);
         
         g.setColor(NORMAL_BORDER_OUTER_BOTTOM);
         g.drawLine(1, SIZE_DIM.height-1, SIZE_DIM.width-1, SIZE_DIM.height-1);
         g.drawLine(SIZE_DIM.width-1, SIZE_DIM.height-1, SIZE_DIM.width-1, 1);
         
         g.setColor(NORMAL_BORDER_INNER_BOTTOM);
         g.drawLine(2, SIZE_DIM.height-2, SIZE_DIM.width-2, SIZE_DIM.height-2);
         g.drawLine(SIZE_DIM.width-2, SIZE_DIM.height-2, SIZE_DIM.width-2, 2);
      }
      
      private void paintSwatch(Color c, Graphics g)
      {
         g.setColor(c);
         
         g.fillRect(3, 3, SIZE_DIM.width-6, SIZE_DIM.height-6);
      }
   
   
   }
}