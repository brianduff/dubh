// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Shuttle.java,v 1.1 1999-06-01 00:17:35 briand Exp $
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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * A shuttle is a control consisting of two lists and some buttons between
 * them. Items can be moved from one list to the other. The left list is
 * usually a list of all available items for something, the right list is
 * the list of items the user has selected. 
 *
 * @author Brian Duff
 * @version $Id: Shuttle.java,v 1.1 1999-06-01 00:17:35 briand Exp $
 */
public class Shuttle extends JPanel 
{
   private JLabel labLeft = new JLabel();
   private JLabel labRight = new JLabel();
   private JList lstLeft = new JList();
   private JList lstRight = new JList();
   private JScrollPane slstLeft = new JScrollPane(lstLeft);
   private JScrollPane slstRight = new JScrollPane(lstRight);
   private JPanel panLeft = new JPanel();
   private JPanel panRight = new JPanel();
   
   private JButton cmdLeftToRight = new JButton(">");
   private JButton cmdRightToLeft = new JButton("<");
   private JButton cmdAllLeftToRight = new JButton(">>");
   private JButton cmdAllRightToLeft = new JButton("<<");
   private VerticalFlowPanel panButtons = new VerticalFlowPanel();
   
   private ButtonListener listener = new ButtonListener();
   
   private EventListenerList lstShuttleListeners = new EventListenerList();

   /**
    * Construct a shuttle control
    */
   public Shuttle()
   {
      doMainLayout();     
   }
   
   private void doMainLayout()
   {
      setLayout(new GridBagLayout());
   
      doListPanelLayout(panLeft, slstLeft, labLeft, 0);
      doListPanelLayout(panRight, slstRight, labRight, 2);
      doButtonPanelLayout();
      
   }
   
   private void doListPanelLayout(JPanel panel, JScrollPane scroll, JLabel label, int gridx)
   {
      panel.setLayout(new BorderLayout());
      panel.add(scroll, BorderLayout.CENTER);
      panel.add(label, BorderLayout.NORTH);
      
      //
      // Add the list panel to the top panel
      //
      this.add(panel, new GridBagConstraints2
         (
         gridx, 0, 1, 1,
         0.5, 1.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.BOTH, new Insets(1,1,1,1),
         0, 0
         ));
      
      lstLeft.setFixedCellWidth(75);
      lstRight.setFixedCellWidth(75);
      
         
   }
   
   private void doButtonPanelLayout()
   {
      // Heinous layout hack...
      
      panButtons.addFixedGap((new JLabel("X")).getPreferredSize().height);
   
      panButtons.addRow(cmdLeftToRight);
      cmdLeftToRight.setMargin(new Insets(0, 0, 0, 0));
      cmdLeftToRight.setFocusPainted(false);
      cmdLeftToRight.addActionListener(listener);
      
      panButtons.addRow(cmdAllLeftToRight);
      cmdAllLeftToRight.setMargin(new Insets(0, 0, 0, 0));
      cmdAllLeftToRight.setFocusPainted(false);
      cmdAllLeftToRight.addActionListener(listener);
      
      // Add some vertical spacing
      panButtons.addRow(new JPanel() 
         { 
            public Dimension getMinimumSize() 
            { 
               return new Dimension(0, 10);
            }
         });
      panButtons.addRow(cmdRightToLeft);
      cmdRightToLeft.setMargin(new Insets(0, 0, 0, 0));
      cmdRightToLeft.setFocusPainted(false);
      cmdRightToLeft.addActionListener(listener);
      
      panButtons.addRow(cmdAllRightToLeft);
      cmdAllRightToLeft.setMargin(new Insets(0, 0, 0, 0));
      cmdAllRightToLeft.setFocusPainted(false);
      cmdAllRightToLeft.addActionListener(listener);
      //
      // Keep buttons at the top
      //
      panButtons.addSpacerRow(new JPanel());
      
      this.add(panButtons, new GridBagConstraints2
         (
         1, 0, 1, 1,
         0.0, 1.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.BOTH, new Insets(1,1,1,1),
         0, 0
         ));
   }
   
   public Dimension getPreferredSize()
   {
      return new Dimension(super.getPreferredSize().width, panButtons.getPreferredSize().height);
   }

   public Dimension getMinimumSize()
   {
      return getPreferredSize();
   }

   
   /**
    * Set the list of items that are displayed on the left of the shuttle.
    */
   public void setLeftListItems(Object[] v)
   {
      lstLeft.setModel(new ShuttleListModel(v));
   }
   
   /**
    * Label above left list
    */
   public void setLeftListDescription(String s)
   {
      labLeft.setText(s);
   }
   
   /**
    * Set the list of items displayed on the right of the shuttle.
    */
   public void setRightListItems(Object[] v)
   {
      lstRight.setModel(new ShuttleListModel(v));
   }
   
   /**
    * Label above right list
    */
   public void setRightListDescription(String s)
   {
      labRight.setText(s);
   }
   
   /**
    * Get the list of items on the left. This creates a new array on each
    * call, so use it sparingly.
    */
   public Object[] getLeftListItems()
   {
      return ((ShuttleListModel)lstLeft.getModel()).getItems();
   }
   
   /**
    * Get the list of items on the right. This creates a new array on each
    * call, so use it sparingly.
    */
   public Object[] getRightListItems()
   {
      return ((ShuttleListModel)lstRight.getModel()).getItems();
   }
   
   
   private void shuttle(JList from, JList to, Object item)
   {
      ((ShuttleListModel)(from.getModel())).removeElement(item);
      ((ShuttleListModel)(to.getModel())).addElement(item);
      to.addSelectionInterval(to.getModel().getSize()-1, to.getModel().getSize()-1);
   }
   
   
   private void shuttle(JList from, JList to, Object[] items)
   {
      for (int i=0; i < items.length; i++)
      {
         shuttle(from, to, items[i]);
      }   
   }
   
   public void shuttleLeftToRight(Object[] items)
   {
      shuttle(lstLeft, lstRight, items);
   }
   
   public void shuttleRightToLeft(Object[] items)
   {
      shuttle(lstRight, lstLeft, items);
   }
   
   class ShuttleListModel extends AbstractListModel
   {
      private List m_vec;
      
      public ShuttleListModel(List v)
      {
         m_vec = v;
      }
      
      public ShuttleListModel(Object[] items)
      {
         m_vec = new ArrayList();
         
         for (int i=0; i < items.length; i++)
         {
            m_vec.add(items[i]);
         }
      
      }
   
      public int getSize()
      {
         return m_vec.size();
      }
      
      
      public Object getElementAt(int i)
      {
         return m_vec.get(i);
      }
      
      public void addElement(Object o)
      {
         m_vec.add(o);
         fireIntervalAdded(this, m_vec.size(), m_vec.size());
      }
      
      public void removeElement(Object o)
      {
         m_vec.remove(o);
         fireIntervalRemoved(this, m_vec.size()+1, m_vec.size()+1);
      }
      
      public Object[] getItems()
      {
         return m_vec.toArray();
      }

   }
   
   class ButtonListener implements ActionListener
   {
      /**
       * Move the items selected in the first list into the second list.
       */
      private void moveSelectedItems(JList from, JList to)
      {
         // Get the selection
         int[] sel = from.getSelectedIndices();
         Object[] items = new Object[sel.length];
         
         for (int i=0; i < sel.length; i++)
         {
            items[i] = from.getModel().getElementAt(sel[i]);
         }
         shuttle(from, to, items);
         
      }
      
      /**
       * Move all items from the first list into the second list.
       */
      private void moveAllItems(JList from, JList to)
      {
         Object[] items = ((ShuttleListModel)from.getModel()).getItems();
         
         shuttle(from, to, items);

      }
   
      public void actionPerformed(ActionEvent e)
      {
         if (e.getSource() == cmdLeftToRight)
         {
            moveSelectedItems(lstLeft, lstRight);
         }
         else if (e.getSource() == cmdRightToLeft)
         {
            moveSelectedItems(lstRight, lstLeft);
         }
         else if (e.getSource() == cmdAllLeftToRight)
         {
            moveAllItems(lstLeft, lstRight);
         }
         else if (e.getSource() == cmdAllRightToLeft)
         {
            moveAllItems(lstRight, lstLeft);
         }
         
         boolean itemsLeft  = (lstLeft.getModel().getSize() > 0);
         boolean itemsRight = (lstRight.getModel().getSize() > 0);
         
         cmdLeftToRight.setEnabled(itemsLeft);
         cmdAllLeftToRight.setEnabled(itemsLeft);
         cmdRightToLeft.setEnabled(itemsRight);
         cmdAllRightToLeft.setEnabled(itemsRight);
      }
   }

}

//
// $Log: not supported by cvs2svn $
//