// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: PreferencesDialog.java,v 1.1 1999-03-22 23:31:55 briand Exp $
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
package dubh.utils.ui.preferences;


import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;

import dubh.utils.misc.Debug;
import dubh.utils.ui.*;
import dubh.utils.event.*;
import dubh.utils.misc.*;
/**
 * A Tree style preferences dialog
 * @author Brian Duff
 * @version $Id: PreferencesDialog.java,v 1.1 1999-03-22 23:31:55 briand Exp $
 */
public class PreferencesDialog extends DubhOkCancelDialog 
{
   private JTree            m_pageList;
   private JScrollPane      m_pageListScroll;
   private JLabel           m_pageListLabel;
   private DefaultTreeModel m_treeModel;
   private DefaultMutableTreeNode m_rootNode;
   private Hashtable        m_nodes;
   private JPanel           m_dummy;
   
   private Container        m_mainPanel;
   
   private UserPreferences  m_preferences;
   
   private DefaultTreeCellRenderer m_renderer;
   
   private boolean         m_initialised;
   /**
    * Construct a new preferences dialog.
    * @param title   the titlebar text for the dialog
    * @param id      the id for storing dialog location
    * @param parent  parent frame for modality
    */
   public PreferencesDialog(String title, String id, Frame parent, UserPreferences p)
   {
      super(parent, title, true);
      m_preferences = p;
      m_initialised = false;
      setName(id);
      BorderLayout b = new BorderLayout();
      b.setHgap(2);
      b.setVgap(2);
      getPanel().setLayout(b);
      m_rootNode  = new DefaultMutableTreeNode("root");
      m_treeModel = new DefaultTreeModel(m_rootNode);
      m_pageList  = new JTree(m_rootNode);
      m_pageList.setModel(m_treeModel);
      m_pageList.setRootVisible(false);
      m_pageList.setShowsRootHandles(true);
      m_pageList.setCellRenderer(new NoIconRenderer());
      m_pageListScroll = new JScrollPane(m_pageList);
      m_pageListLabel = new JLabel("!NLS: Categories");
      m_nodes = new Hashtable(5);
      
      m_dummy = new JPanel();
      m_mainPanel = (Container) m_dummy;
      getPanel().add(m_pageListLabel, BorderLayout.NORTH);
      getPanel().add(m_pageListScroll, BorderLayout.WEST);
      getPanel().add(m_mainPanel, BorderLayout.CENTER);
      
      
      m_pageListScroll.setPreferredSize(new Dimension(150, 0));
      m_pageList.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent e)
         {
            treeSelectionChanged(
               (PreferencePage)((DefaultMutableTreeNode)
                  e.getPath().getLastPathComponent()).getUserObject()
            ); 
         }
      });
      
   }

   protected void addPage(DefaultMutableTreeNode parent, PreferencePage p)
   {
      DefaultMutableTreeNode n = new DefaultMutableTreeNode(p);
      parent.add(n);
      m_nodes.put(p.getTitle(), n);
      int index = parent.getIndex(n);
      //m_treeModel.nodesWereInserted(parent, new int[] { index });   
      m_treeModel.nodeStructureChanged(parent);
   }

   /**
    * Add a page. It will be added to the root
    */
   public void addPage(PreferencePage p)
   {
      if (m_nodes.containsKey(p.getTitle()))
         throw new IllegalArgumentException(
            "A page called "+p.getTitle()+" already exists");
      
      addPage(m_rootNode, p);
   }
   
   /**
    * Add a page with a specified parent. 
    */
   public void addPage(PreferencePage p, String parentPage)
   {
      if (m_nodes.containsKey(p.getTitle()))
         throw new IllegalArgumentException(
            "A page called "+p.getTitle()+" already exists");
            
      DefaultMutableTreeNode pnode = 
         (DefaultMutableTreeNode)m_nodes.get(parentPage);
      
      if (pnode == null)
         throw new IllegalArgumentException(
            "Parent page "+parentPage+" doesn't exist.");
      
      addPage(pnode, p);
   
   }
   
   private void treeSelectionChanged(PreferencePage pg)
   {
      getPanel().remove(m_mainPanel);
      getPanel().add(pg);
      m_mainPanel = pg;
      validate();
      repaint();
   }
   
   public PreferencePage getPage(String pageName)
   {
      return (PreferencePage)
         ((DefaultMutableTreeNode)m_nodes.get(pageName)).getUserObject();
   }
   
   public void selectPage(String pageName)
   {
      DefaultMutableTreeNode n = 
         (DefaultMutableTreeNode) m_nodes.get(pageName);
      
      if (n == null)
         return;
         
      TreeNode[] p = n.getPath();
      
      m_pageList.setSelectionPath(new TreePath(p));               
   }

   
   public void setVisible(boolean b)
   {
      if (b && !m_initialised)
      {
         revertAll();
         m_initialised = true;
      }
      super.setVisible(b);
   }
   
   public boolean okClicked()
   {
      try
      {
         saveAll();
         m_preferences.save();
         m_initialised = false;
         return true;
      }
      catch (IOException ioe)
      {
         // TODO: Display error UI
         return false;
      }
   }
   
   protected void saveAll()
   {
      Enumeration k = m_nodes.keys();
      
      while (k.hasMoreElements())
      {
         PreferencePage p = getPage((String)k.nextElement());
         p.save(m_preferences);
      }

   }
   
   protected void revertAll()
   {
      Enumeration k = m_nodes.keys();
      
      while (k.hasMoreElements())
      {
         PreferencePage p = getPage((String)k.nextElement());
         p.revert(m_preferences);
      }      
   }
   
   class NoIconRenderer extends DefaultTreeCellRenderer
   {
      public Component getTreeCellRendererComponent(JTree tree, Object value, 
         boolean sel, boolean expanded, boolean leaf, 
         int row, boolean hasFocus) 
      {
         JLabel l = (JLabel) super.getTreeCellRendererComponent(
            tree, value, sel, expanded, leaf, row, hasFocus
         );
         
         l.setIcon(null);
         
         return l;
      }
   }
}

// 
// $Log: not supported by cvs2svn $
//