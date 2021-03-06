// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Shuttle.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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
 * @version $Id: Shuttle.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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


      lstLeft.setFixedCellWidth(75);
      lstLeft.setModel(new ShuttleListModel());
      cmdLeftToRight.setEnabled(false);
      cmdAllLeftToRight.setEnabled(false);

      lstRight.setFixedCellWidth(75);
      lstRight.setModel(new ShuttleListModel());
      cmdRightToLeft.setEnabled(false);
      cmdAllRightToLeft.setEnabled(false);
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
      cmdLeftToRight.setEnabled((v.length > 0));
      cmdAllLeftToRight.setEnabled((v.length > 0));
   }

   /**
    * Label above left list
    */
   public void setLeftListDescription(String s)
   {
      labLeft.setText(s);
   }

   /**
    * Set the list of items displayed on the right of the shuttle. Items
    * in this list that are already included on the left will be removed
    * from the left automatically by this method.
    */
   public void setRightListItems(Object[] v)
   {
      lstRight.setModel(new ShuttleListModel(v));
      for (int i=0; i < v.length; i++)
      {
         try
         {
            ((ShuttleListModel)lstLeft.getModel()).removeElement(v[i]);
         }
         catch (Exception e)
         {
            ;
         }
      }
      cmdLeftToRight.setEnabled((lstLeft.getModel().getSize() > 0));
      cmdAllLeftToRight.setEnabled((lstLeft.getModel().getSize() > 0));

      cmdRightToLeft.setEnabled((lstRight.getModel().getSize() > 0));
      cmdAllRightToLeft.setEnabled((lstRight.getModel().getSize() > 0));
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

   /**
    * A model for items in the lists, based on a Collections
    * List object.
    */
   class ShuttleListModel extends AbstractListModel
   {
      private List m_vec;

      public ShuttleListModel(List v)
      {
         m_vec = v;
      }

      public ShuttleListModel(Object[] items)
      {
         this();

         for (int i=0; i < items.length; i++)
         {
            m_vec.add(items[i]);
         }

      }

      /**
       * Create a model with an empty list
       */
      public ShuttleListModel()
      {
         m_vec = new ArrayList();
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
// Revision 1.3  1999/11/11 21:24:36  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.2  1999/06/01 17:54:57  briand
// Fix enablement of buttons when lists are empty. Automatically remove
// items from LHS when RHS list is set.
//
// Revision 1.1  1999/06/01 00:17:35  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//