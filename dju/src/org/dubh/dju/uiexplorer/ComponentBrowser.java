
// Copyright (c) 2000 Dubh 
package org.javalobby.dju.uiexplorer;

import org.javalobby.dju.ui.treetable.JTreeTable;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JComponent;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Color;

/**
 * A component browser displays a tree of components in a hierarchy and allows
 * you to do some useful stuff with them
 * <P>
 * @author Brian Duff
 */
public class ComponentBrowser
{
   JTreeTable m_table;
   ComponentTreeModel m_model;
   ComponentControlPanel m_controlPanel;

   JFrame m_frame;

   private Color m_oldBackground;
   private Component m_oldComponent;
   private boolean m_oldOpaque;

   public ComponentBrowser()
   {
      Component dummy = new JFrame();
      m_model = new ComponentTreeModel(dummy);
      m_table = new JTreeTable(m_model);
      m_controlPanel = new ComponentControlPanel();
   }
   
   /**
    * Set the component that is displayed at the root of the tree.
    */
   public void setRootComponent(Component c)
   {
      m_model = new ComponentTreeModel(c);
      m_table = new JTreeTable(m_model);
      if (m_frame != null && m_frame.isVisible())
      {
         putTableInFrame();
      }
      initSelectionListener();
   }

   public void show()
   {
      m_frame = new JFrame("Component Browser");
      putTableInFrame();
      m_frame.pack();
      m_frame.setVisible(true);
   }

   public void hide()
   {
      m_frame.setVisible(false);
      m_frame.dispose();
      m_frame = null;
   }

   private void putTableInFrame()
   {
      if (m_frame != null)
      {
         JPanel p = new JPanel();
         p.setLayout(new BorderLayout());
         p.add(new JScrollPane(m_table), BorderLayout.CENTER);
         p.add(m_controlPanel.getPanel(), BorderLayout.SOUTH);
         m_frame.setContentPane(p);
      }
   }

   private void initSelectionListener()
   {
      m_table.getSelectionModel().addListSelectionListener(
         new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse)
            {
               if (m_table.getSelectedRow() >= 0)
               {
                  ComponentNode cn = (ComponentNode)m_table.getModel().getValueAt(
                     m_table.getSelectedRow(), 0
                  );
                  Component c= cn.getComponent();
                  m_controlPanel.setTargetComponent(c);

                  if (m_oldComponent != null)
                  {
                     m_oldComponent.setBackground(m_oldBackground);
                     if (m_oldComponent instanceof JComponent)
                     {
                        ((JComponent)m_oldComponent).setOpaque(m_oldOpaque);
                     }
                  }

                  m_oldComponent = c;
                  m_oldBackground = c.getBackground();
                  if (c instanceof JComponent)
                  {
                     m_oldOpaque = ((JComponent)c).isOpaque();
                     ((JComponent)c).setOpaque(true);
                  }

                  c.setBackground(Color.pink);

               }
            }
         }
      );
      
   }
}

 