// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: Navigator.java,v 1.4 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.navigator;

import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Icon;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;

import java.awt.BorderLayout;
import java.awt.Component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.dubh.dju.command.ActionCommand;
import org.dubh.dju.command.SelectionContextCommandManager;
import org.dubh.dju.command.CommandManager;

import org.dubh.apps.newsagent.NewsAgent;

/**
 * The navigator is the control on the left of the NewsAgent
 * main window. It contains a tree structure representing stores
 * and service providers that the user can use.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Navigator.java,v 1.4 2001-02-11 02:51:00 briand Exp $
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
    * Get the model for the tree in the navigator.
    */
   public TreeModel getTreeModel()
   {
      return m_model;
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
      if (path == null)
      {
         // Could display top level navigator popup here.
         return;
      }
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
// Revision 1.3  2000/06/14 21:36:45  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:46:04  briand
// FolderTreePanel's replacement.
//
//