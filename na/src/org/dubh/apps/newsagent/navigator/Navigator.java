// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: Navigator.java,v 1.1 1999-10-24 00:46:04 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
package dubh.apps.newsagent.navigator;

import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Icon;
import javax.swing.tree.TreePath;

import java.awt.BorderLayout;
import java.awt.Component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dubh.utils.command.ActionCommand;
import dubh.utils.command.SelectionContextCommandManager;
import dubh.utils.command.CommandManager;

import dubh.apps.newsagent.NewsAgent;

/**
 * The navigator is the control on the left of the NewsAgent
 * main window. It contains a tree structure representing stores
 * and service providers that the user can use.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Navigator.java,v 1.1 1999-10-24 00:46:04 briand Exp $
 */
public class Navigator
{   
   protected JTree m_tree;
   protected JScrollPane m_scroll;
   protected NavigatorTreeModel m_model;
   protected JLabel m_labBanner;
   protected JPanel m_panel;
   protected SelectionContextCommandManager m_manager;

   /**
    * Construct a navigator with the specified list of services.
    */
   public Navigator(NavigatorServiceList nsl)
   {
      m_tree = new JTree();
      m_tree.setRootVisible(false);
      m_tree.setCellRenderer(new NavigatorRenderer());
      m_tree.setShowsRootHandles(true);
      m_scroll = new JScrollPane(m_tree);
      m_model = new NavigatorTreeModel(nsl);
      m_tree.setModel(m_model);
      m_labBanner = new JLabel("NewsAgent Navigator");
      m_panel = new JPanel();
      m_panel.setLayout(new BorderLayout());
      m_panel.add(m_labBanner, BorderLayout.NORTH);
      m_panel.add(m_scroll, BorderLayout.CENTER);
      m_manager = new SelectionContextCommandManager();
      m_tree.addTreeSelectionListener(m_manager);
      m_tree.addMouseListener(new TreeRightClickListener());
   }
   
   public CommandManager getCommandManager()
   {
      return m_manager;
   }

   /**
    * Get the UI component for the navigator.
    */
   public Component getComponent()
   {
      return m_panel;
   } 

   /**
    * The navigator has a banner at the top. You can
    * set the image displayed in this banner
    */
   public void setBannerImage(Icon i)
   {
      m_labBanner.setIcon(i);
   }
   
   /**
    * You can set whether the banner at the top of the navigator
    * is displayed or not.
    */
   public void setBannerVisible(boolean b)
   {
      m_labBanner.setVisible(b);
   }
   
   /**
    * Use this to find out if the banner at the top of the navigator
    * is currently visible
    */
   public boolean isBannerVisible()
   {
      return m_labBanner.isVisible();
   }
   
   /**
    * Gets a menu for the specified list of commands
    */
   private JPopupMenu getMenuFor(Class[] commands)
   {
      JPopupMenu popup = new JPopupMenu();
      for (int i=0; i < commands.length; i++)
      {
         popup.add(new ActionCommand(commands[i], getCommandManager()));
      }
   
      return popup;
   }
   
   
   /**
    * Handle a popup menu display. In response to right mouse
    * clicks, this selects the item and pops up the correct
    * popup menu for it.
    */
   private void doPopupMenu(int x, int y) 
   {
      TreePath path = m_tree.getPathForLocation(x,y);
      NavigatorNode ourobject = (NavigatorNode)path.getLastPathComponent();
      m_tree.setSelectionPath(path);
      
      Class[] cmd = ourobject.getCommandList();
      
      JPopupMenu menu = getMenuFor(cmd);
      menu.show(m_tree, x, y);
   }

   /**
    * Listen for right mouse clicks and bring up the context menu
    */
   class TreeRightClickListener extends MouseAdapter
   {
      private void doClick(MouseEvent e)
      {
         if (e.isPopupTrigger()) 
         {
            doPopupMenu(e.getX(), e.getY());
         }               
      }
      
      public void mouseClicked(MouseEvent e)
      {
         doClick(e);
      }   

      public void mousePressed(MouseEvent e)
      {
         doClick(e);
      }   

      public void mouseReleased(MouseEvent e)
      {
         doClick(e);
      }   

   }
}

//
// $Log: not supported by cvs2svn $
//