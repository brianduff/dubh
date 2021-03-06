// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: FolderTreePanel.java,v 1.6 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.dialog.main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import org.dubh.dju.misc.Debug;

import org.dubh.apps.newsagent.nntp.NNTPServer;
import org.dubh.apps.newsagent.nntp.NNTPServerException;
import org.dubh.apps.newsagent.nntp.Newsgroup;
import org.dubh.apps.newsagent.Folder;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.apps.newsagent.dialog.ErrorReporter;

import javax.swing.tree.DefaultTreeCellRenderer;

import org.dubh.dju.ui.GridBagConstraints2;
/**
 * A panel containing a tree control, which corresponds to the folders, news
 * servers and newsgroups available to the user.<P>
 * @author Brian Duff
 * @version $Id: FolderTreePanel.java,v 1.6 2001-02-11 02:51:00 briand Exp $
 */
public class FolderTreePanel extends JPanel {


// Private instance variables

  private DefaultMutableTreeNode sandf;
  private DefaultMutableTreeNode servers;
  private DefaultMutableTreeNode folders;
  private final static ImageIcon icoNewsagent =
         GlobalState.getRes().getImage("NewsAgent.newsAgentSmall");
  private JTree jtreeFolders = new JTree();
  private JScrollPane scroll = new JScrollPane(jtreeFolders);
  private JLabel lblImage = new JLabel();
  BorderLayout layout = new BorderLayout();
  private NNTPServer m_lastServer= null;
  private Newsgroup m_lastNewsgroup = null;

  public FolderTreePanel() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    this.setLayout(layout);
    lblImage.setIcon(icoNewsagent);
    lblImage.setBackground(Color.white);
    jtreeFolders.addMouseListener(new FolderTreePanel_jtreeFolders_mouseAdapter(this));
    jtreeFolders.addTreeSelectionListener(new FolderTreePanel_jtreeFolders_valueAdapter(this));
    jtreeFolders.setRootVisible(false);
    jtreeFolders.setShowsRootHandles(true);
    jtreeFolders.addTreeExpansionListener(new ExpansionListener());
    this.add(lblImage, BorderLayout.NORTH);
    this.add(scroll, BorderLayout.CENTER);
    jtreeFolders.setCellRenderer(new FolderTreeRenderer());
    treeUpdate();

  }
// Interface

  /**
   * Add a listener for selection events in the tree
   */
  public void addTreeSelectionListener(TreeSelectionListener l) {
     jtreeFolders.addTreeSelectionListener(l);
  }

  public void removeTreeSelectionListener(TreeSelectionListener l) {
     jtreeFolders.removeTreeSelectionListener(l);
  }

  /**
   * Add a listener for expansion / collapse events in the tree
   */
  public void addTreeExpansionListener(TreeExpansionListener l) {
     jtreeFolders.addTreeExpansionListener(l);
  }

  public void removeTreeExpansionListener(TreeExpansionListener l) {
     jtreeFolders.removeTreeExpansionListener(l);
  }

  /**
   * Forces the panel to update. Should be called whenever newsgroups / servers
   * are added or updated in some way.
   */
  public void treeUpdate() {
     constructTree();
     jtreeFolders.setModel(new DefaultTreeModel(sandf));
     repaint();

  }

  /**
   * Returns the currently selected Tree item as an Object. You will have to
   * use the instanceof operator to check what it is and cast it appropriately.
   */
  public Object getSelectedItem() {
     TreePath path = jtreeFolders.getSelectionPath();
     if (path == null) return null;
     return ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
  }

  /**
   * Sets the selected row number. Should be called if you delete the existing
   * selection, or an exception will occur when the UIManager tries to redraw.
   */
  public void selectItem(int row) {
     jtreeFolders.setSelectionRow(row);
  }

  /**
   * Returns the last news server to be selected. If a newsgroup was the last
   * item selected, the server on which it resides will be returned by this
   * method. If no server has ever been selected, this method will return null.
   */
  public NNTPServer getLastSelectedServer() {
   return m_lastServer;
  }

  /**
   * Returns the last selected Newsgroup.
   */
  public Newsgroup getLastSelectedNewsgroup() {
   return m_lastNewsgroup;

  }

  /**
   * Sets whether this UI component is allowed to respond to user selections
   */
  public void setActive(boolean active) {
    jtreeFolders.setEnabled(active);
    jtreeFolders.repaint();

  }

  /**
   * Constructs the Tree
   */
  private void constructTree() {
     sandf = new DefaultMutableTreeNode(null);
     servers = new DefaultMutableTreeNode(GlobalState.getRes().getString("Servers"));
     folders = new DefaultMutableTreeNode(GlobalState.getRes().getString("Folders"));

     // Add servers, using user properties.
     Enumeration server_enum = GlobalState.getStorageManager().getServers();
     while (server_enum.hasMoreElements()) {
           NNTPServer this_server = (NNTPServer) server_enum.nextElement();


           DefaultMutableTreeNode server = new DefaultMutableTreeNode(this_server);
           // Add all this server's newsgroups
           Enumeration e = this_server.getSubscriptions().elements();
           while (e.hasMoreElements()) {
              try {
                 server.add(new DefaultMutableTreeNode((Newsgroup)e.nextElement()));
              } catch (Exception ex) {
              }
           }
           servers.add(server); // Add this server
     }
     sandf.add(servers);        // Add the servers folder to the tree
     Enumeration folder_enum = GlobalState.getStorageManager().getFolders();
     while (folder_enum.hasMoreElements())
        folders.add(new DefaultMutableTreeNode((Folder)folder_enum.nextElement()));
     sandf.add(folders);
  }

  void treeMouseEvent(MouseEvent e) {
     if (e.isPopupTrigger()) {
        doPopupMenu(e.getX(), e.getY());
     }
  }

  /**
   * Fired when the user selects a node
   */
  void treeSelectionEvent(TreeSelectionEvent e) {
   if (e.isAddedPath()) {
     TreePath path = e.getPath();
     Object ourobject = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
     if (ourobject instanceof Newsgroup) {
        TreeNode parent = ((DefaultMutableTreeNode)path.getLastPathComponent()).getParent();
        if (parent == null) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "In FolderTreePanel.treeSelectionEvent, a newsgroup appears to have no parent server.");
           return;
        }
        NNTPServer server = (NNTPServer) ((DefaultMutableTreeNode)parent).getUserObject();
        // set the serverconnected action item depending on whether the server is connected.
    //    GlobalState.getMainFrame().getAction("serverconnected").putValue("checked", new Boolean(server.isConnected()));
        // Update actions
     //   GlobalState.getMainFrame().setActions(MainFrame.ACTION_NEWSGROUP);
        GlobalState.getMainFrame().setThreadTreeVisible(true);
        newsgroupSelected((Newsgroup)ourobject, server);
     } else if (ourobject instanceof Folder) {
   //     GlobalState.getMainFrame().setActions(MainFrame.ACTION_FOLDER);
        folderSelected((Folder)ourobject);
        GlobalState.getMainFrame().setThreadTreeVisible(true);
     } else if (ourobject instanceof NNTPServer) {
   //    GlobalState.getMainFrame().setActions(MainFrame.ACTION_SERVER);
   //    GlobalState.getMainFrame().getAction("serverconnected").putValue("checked", new Boolean(((NNTPServer)ourobject).isConnected()));
        GlobalState.getMainFrame().setThreadTreeVisible(false);
     } else {
   //    GlobalState.getMainFrame().setActions(MainFrame.ACTION_NOSEL);
        GlobalState.getMainFrame().setThreadTreeVisible(false);
     }
    }
  }

  /**
   * Called when the user selects a Newsgroup.
   */
  private void newsgroupSelected(Newsgroup ng, NNTPServer server) {
    // if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Newsgroup "+ng+" selected on "+server);
    GlobalState.getStorageManager().connectIfNeeded(server);
    m_lastNewsgroup = ng;
    // enable the post action
    try {
     server.selectGroup(ng);
     GlobalState.getMainFrame().getThreadTree().setProvider(server);
     m_lastServer = server;
    } catch (java.io.IOException e) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IO Exception selecting group "+ng);
    } catch (NNTPServerException e) {
        if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "NNTPServerException selecting group "+ng);
    }

  }

  /**
   * Called when the user selects a Folder.
   */
  private void folderSelected(Folder folder) {
   // disable the post action
   GlobalState.getMainFrame().getThreadTree().setProvider(folder);
  }

  /**
   * Handle a popup menu display.
   */
  private void doPopupMenu(int x, int y) {
     // Determine which type of menu to display
     TreePath path = jtreeFolders.getPathForLocation(x,y);
     Object ourobject = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
     JPopupMenu theMenu;
     if (ourobject != null) {
         if (ourobject instanceof String) {
           if (ourobject.equals(GlobalState.getRes().getString("Folders")))
              theMenu = GlobalState.getMainFrame().popupFolders;
           else
              theMenu = GlobalState.getMainFrame().popupServers;
         } else if (ourobject instanceof NNTPServer) {
           theMenu = GlobalState.getMainFrame().popupServer;
         } else if (ourobject instanceof Newsgroup)  {
           theMenu = GlobalState.getMainFrame().popupNewsgroup;
         } else if (ourobject instanceof Folder)     {
           theMenu = GlobalState.getMainFrame().popupFolder;
         } else {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Unknown tree item: "+ourobject+" in FolderTreePanel.doPopupMenu");
           theMenu = new JPopupMenu();
         }
        // show the popup menu
        theMenu.show(this, x, y);
     }
  }

  class ExpansionListener implements TreeExpansionListener {
     public void treeExpanded(TreeExpansionEvent e) {
        setMinimumSize(new Dimension(0, 100));
     }
     public void treeCollapsed(TreeExpansionEvent e) {
        setMinimumSize(new Dimension(0, 100));
     }
  }

}

/**
 * Draws individual tree items
 */
class FolderTreeRenderer extends DefaultTreeCellRenderer {
      private final static ImageIcon icoServers =
         GlobalState.getRes().getImage("FolderTreePanel.servers");
     private final static ImageIcon icoFolders =
         GlobalState.getRes().getImage("FolderTreePanel.folders");
     private final static ImageIcon icoFolder =
         GlobalState.getRes().getImage("FolderTreePanel.folder");
     private final static ImageIcon icoServer =
         GlobalState.getRes().getImage("FolderTreePanel.server");
     private final static ImageIcon icoServerDis =
           GlobalState.getRes().getImage("FolderTreePanel.serverDisconnected");
     private final static ImageIcon icoNewsgroup =
         GlobalState.getRes().getImage("FolderTreePanel.newsgroup");

  public FolderTreeRenderer() {
     super();
  }

  /**
   * Returns the renderer.
   */
   public synchronized Component getTreeCellRendererComponent(JTree tree,
      Object v, boolean selected, boolean expanded, boolean leaf, int row,
    boolean hasfocus) {
     JLabel supComp = (JLabel) super.getTreeCellRendererComponent(tree, v, selected, expanded, leaf, row,
        hasfocus);

     Object value = ((DefaultMutableTreeNode)v).getUserObject();
     if (value instanceof String) {
        // Either "Servers" or "Folders"
        if (((String)value).equals(GlobalState.getRes().getString("Folders"))) {
           supComp.setText((String)value);
           supComp.setIcon(icoFolders);
        } else { // Ought to be "Servers"
           supComp.setText((String)value);
           supComp.setIcon(icoServers);
        }
     } else if (value instanceof Folder) {
        supComp.setText(((Folder)value).getName());
        supComp.setIcon(icoFolder);
     } else if (value instanceof NNTPServer) {
        supComp.setText(((NNTPServer)value).getNiceName());
        if (((NNTPServer)value).isConnected())
           supComp.setIcon(icoServer);
        else
           supComp.setIcon(icoServerDis);
     } else if (value instanceof Newsgroup) {
        supComp.setText(((Newsgroup)value).getName());
        supComp.setIcon(icoNewsgroup);
     } else {
        supComp.setText("Unknown Tree Item!!!");
     }

    return this;
  }

}

class FolderTreePanel_jtreeFolders_valueAdapter implements javax.swing.event.TreeSelectionListener {
  FolderTreePanel adaptee;
  FolderTreePanel_jtreeFolders_valueAdapter(FolderTreePanel adaptee) {
   this.adaptee = adaptee;
  }

  public void valueChanged(TreeSelectionEvent e) {
     adaptee.treeSelectionEvent(e);
  }

}


class FolderTreePanel_jtreeFolders_mouseAdapter implements java.awt.event.MouseListener {
   FolderTreePanel adaptee;

  FolderTreePanel_jtreeFolders_mouseAdapter(FolderTreePanel adaptee) {
   this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
     adaptee.treeMouseEvent(e);
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {
     adaptee.treeMouseEvent(e);
  }

  public void mouseReleased(MouseEvent e) {
     adaptee.treeMouseEvent(e);
  }
}

//
// Old History:
//
// <LI>0.1 [22/03/98]: Initial Revision
// <LI>0.2 [23/03/98]: Implemented tree cell renderer and update methods.
// <LI>0.3 [24/03/98]: Added popup menus. Added image.
// <LI>0.4 [30/03/98]: Changed server icon rendering to use a different icon for
//   disconnected servers.
// <LI>0.5 [31/03/98]: Added tree selection events.
// <LI>0.6 [01/04/98]: Added getSelectedServer().
// <LI>0.7 [02/04/98]: Added binding for Connected pull down menu item.
// <LI>0.8 [04/04/98]: Added getLastSelectedNewsgroup()
// <LI>0.9 [07/04/98]: Added setActive()
// <LI>0.10 [18/04/98]: Fixed a bug in tree Selection event handling.
// <LI>0.11 [20/04/98]: Changed so that the thread tree is made visible /
//   invisible depending on the selection.
// <LI>0.12 [30/06/98]: Now setting the minimum width to 0 on tree expansion /
//   collapse events, so that the folder tree panel can be resized smaller than
//   the width of the widest tree node (a really annoying bug that's been
//   around for ages). Added event hooks to make the panel more "beanlike"
// <LI>0.13 [03/07/98]: Changed the tree renderer to subclass Swing's (un-
//   documented) default tree renderer. It should look a bit more natural now.
// <LI>0.14 [31/01/99]: Changed old default tree cell renderer to new Swing 1.1
//   version.

//
// New History:
//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:32:08  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//