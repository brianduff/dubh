// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: ThreadTree.java,v 1.7 2001-02-11 15:48:07 briand Exp $
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
import org.dubh.dju.ui.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.dubh.apps.newsagent.nntp.MessageProvider;
import org.dubh.apps.newsagent.nntp.MessageHeader;
import org.dubh.apps.newsagent.nntp.MessageBody;
import org.dubh.apps.newsagent.nntp.NNTPServerException;
import org.dubh.apps.newsagent.nntp.StorageManager;

import org.dubh.apps.newsagent.GlobalState;

import org.dubh.apps.newsagent.dialog.ErrorReporter;

import org.dubh.dju.misc.Debug;

/**
 * Displays a multicolumn tree control.
 * @author Brian Duff
 * @version $Id: ThreadTree.java,v 1.7 2001-02-11 15:48:07 briand Exp $
 */
public class ThreadTree extends JPanel {

  protected BorderLayout borderLayout1 = new BorderLayout();
  protected HeaderControl m_header = new HeaderControl(5);
  final JTree m_tree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode(null)));
  protected JScrollPane jScroll1;
  private MessageProvider m_provider;

  protected int[] colXPos;  // The x position of each column
  protected int   numCols;  // Number of columns

  public ThreadTree() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Initialise the ThreadTree
   */
  private void jbInit() throws Exception {
     m_tree.setRootVisible(false);
     m_tree.setBackground(Color.white);
     jScroll1 = new JScrollPane(m_tree);
     jScroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
     m_tree.setShowsRootHandles(true);

     m_header.setPreferredSize(new Dimension(10, 20));
     jScroll1.setMinimumSize(new Dimension(0, 0));
     m_header.setColumnName(1, GlobalState.getRes().getString("ThreadTree.Subject"));
     m_header.setColumnName(2, GlobalState.getRes().getString("ThreadTree.From"));
     m_header.setColumnName(3, GlobalState.getRes().getString("ThreadTree.Sent"));
     m_header.setColumnName(4, GlobalState.getRes().getString("ThreadTree.AgentIcons"));
     m_header.setBoundComponent(m_tree);

     m_tree.setCellRenderer(new MyTreeRenderer(m_header));
     m_tree.addComponentListener(new ThreadTree_m_tree_componentAdapter(this));
     m_tree.addTreeExpansionListener(new ThreadTree_m_tree_treeExpansionAdapter(this));
     m_tree.addTreeSelectionListener(new ThreadTree_m_tree_valueAdapter(this));
     m_tree.addMouseListener(new ThreadTree_m_tree_mouseAdapter(this));

     this.setLayout(borderLayout1);

     jScroll1.setColumnHeaderView(m_header);
     this.add(jScroll1, BorderLayout.CENTER);
     this.setMinimumSize(new Dimension(50,100));

  }

  public void addTreeSelectionListener(TreeSelectionListener l) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Added a thread tree selection listener : "+l);
     m_tree.addTreeSelectionListener(l);
  }

  public void removeTreeSelectionListener(TreeSelectionListener l) {
     m_tree.removeTreeSelectionListener(l);
  }

  public void addTreeExpansionListener(TreeExpansionListener l) {
     m_tree.addTreeExpansionListener(l);
  }

  public void removeTreeExpansionListener(TreeExpansionListener l) {
     m_tree.removeTreeExpansionListener(l);
  }

  /**
   * Sets whether this UI component is allowed to respond to user selections
   @deprecated since NewsAgent 1.02, use setEnabled instead.
   */
  public void setActive(boolean active) { setEnabled(active); }

  public void setEnabled(boolean enabled) {
     m_tree.setEnabled(enabled);
     m_tree.repaint();
  }

  public boolean isEnabled() {
     return m_tree.isEnabled();
  }


  private int calcFirstColWidth() {
    // Go through all rows of the tree, check their minimum
    // width.
    int biggest = 0;
    int thiswidth;
    for (int i=0; i< m_tree.getRowCount(); i++) {
 // TODO: Fix for Swing 1.1     thiswidth = m_tree.getUI().getRowBounds(i).x;
    thiswidth = 0;
      if (thiswidth > biggest) biggest = thiswidth;
    }
    return biggest;
  }

  void m_tree_treeExpanded(TreeExpansionEvent e) {
    // When the tree is expanded, the first column width must
    // be changed.
    int newwidth = calcFirstColWidth();

   // if (newwidth > m_header.getColumnWidth(1))
    try {
      m_header.setMinimumWidth(1, newwidth);
    } catch (Exception exc) {
      if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Caught exception: "+exc);
    }
        }

  void m_tree_treeCollapsed(TreeExpansionEvent e) {
    // Similar to treeExpanded.
    int newwidth = calcFirstColWidth();
    m_header.setMinimumWidth(1, newwidth);
  }

  /**
   * Triggered when the user selects a new tree item.
   */
  void treeSelectionEvent(TreeSelectionEvent e) {
   if (e.isAddedPath()) {
     TreePath path = e.getPath();
     Object ourobject = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
     if (!(ourobject instanceof MessageHeader)) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Selection of non header item "+ourobject+" in ThreadTree.");
       return;
     }
     // get the message body from the provider..
     MessageBody body;
     if (m_provider == null) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "No server or folder selected in Folder Server Tree");
       return;
     }
     if (!m_provider.ensureConnected()) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "No connection to server: can't show message body.");
       return;
     }
     try {
       GlobalState.getMainFrame().setStatus(GlobalState.getRes().getString("ThreadTree.Retrieving"));
       body = m_provider.getBody((MessageHeader)ourobject);
       GlobalState.getMainFrame().getMsgDisplayPanel().setMessage((MessageHeader)ourobject, body);
        // Set the actions in the main frame
 //      GlobalState.getMainFrame().setActions(MainFrame.ACTION_MESSAGE);
       // Mark the message as read
       ((MessageHeader)ourobject).setRead(true);
       // Reapply agents to the message
       //GlobalState.getAgentManager().callListAgents((MessageHeader)ourobject);
     } catch (java.io.IOException ioe) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IO Exception selecting message "+ourobject);
       // GlobalState.getMainFrame().setStatus();
       GlobalState.getMainFrame().getMsgDisplayPanel().clear();
       m_tree.clearSelection();
     } catch (NNTPServerException nntpe) {
        StorageManager.nntpException(nntpe,
               GlobalState.getRes().getString("Action.Retrieving"),
               m_provider.getProviderName());
        GlobalState.getMainFrame().getMsgDisplayPanel().clear();
        m_tree.clearSelection();
     }
     GlobalState.getMainFrame().setStatus();
   }



  }

  public void treeMouseEvent(MouseEvent e) {
     if (e.isPopupTrigger()) {
         doPopupMenu(e.getX(), e.getY());
     }

  }


    /**
   * Handle a popup menu display.
   */
  private void doPopupMenu(int x, int y) {
     // Determine which type of menu to display
     TreePath path = m_tree.getPathForLocation(x,y);
     /* Adjust the x and y co-ordinates depending on the position of the
     * ScrollPane. It's annoying that we have to do this. */
     int mx = x - jScroll1.getHorizontalScrollBar().getValue();
     int my = y - jScroll1.getVerticalScrollBar().getValue();
     Object ourobject = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
     JPopupMenu theMenu = GlobalState.getMainFrame().popupMessage;
     if (ourobject != null) {
        theMenu.show(this, mx, my);
     }
  }

// Public Interface

 /**
  * Clears the contents of the headers list
  */
  public void clearList() {
   m_tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(null)));
   m_tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(null)));
   m_tree.repaint();
  }

  /**
   * Sets the MessageProvider to use for the headers.
   */
  public void setProvider(MessageProvider provider) {
     m_provider = provider;
     GlobalState.getMainFrame().setStatus(
        GlobalState.getRes().getString("ThreadTree.ReadingHeaders", new String[] {
           provider.getProviderName()}));
    // final ProgressDialog pm = new ProgressDialog(GlobalState.getMainFrame(),"Retrieving Headers", true);
     final MessageProvider innerprovider = provider;

     Thread headerDownloader = new Thread() {
       public void run() {
         StatusBar status = GlobalState.getMainFrame().getStatusBar();
         status.setProgressVisible(true);
         GlobalState.getMainFrame().setNetworkActionsEnabled(false);
        try {
           DefaultTreeModel tm = (DefaultTreeModel) innerprovider.getHeaders(status.getProgressBar());
           /*
            * Have to really pansie about to get the tree to update. God only
            * knows what hideous things Swing is doing, but you have to set
            * the model twice before it will work properly. I assume this *is*
            * a Swing bug. (Spent ~2.5 hrs trying to figure out why the tree
            * wasn't updating, which was a bit frustrating...:))
            */
           m_tree.setModel(null);
           m_tree.setModel(tm);
           m_tree.setModel(tm);
           m_tree.treeDidChange();
           m_tree.repaint();
           repaint();
           m_tree.clearSelection();
           GlobalState.getMainFrame().getMsgDisplayPanel().clear();
           // Update the app titlebar to show the new provider.
           GlobalState.getMainFrame().setTitle(GlobalState.getApplicationInfo().getName()+" - ["+innerprovider.getProviderName()+"]");
           GlobalState.getMainFrame().setStatus();
/*           repaint();
           invalidate();
           repaint();
           validate();
           doLayout();
*/
       } catch (java.io.IOException e) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IOException setting threadtree provider");
       } catch (NNTPServerException ne) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "NNTP Exception setting threadtree provider");
      // } catch (Exception other) {
       //    if (Debug.TRACE_LEVEL_1) Debug.println(1, this, ("Got an exception "+other);
       } // try
       GlobalState.getMainFrame().setNetworkActionsEnabled(true);
       status.setProgressVisible(false);
      } // run
     }; // thread

     headerDownloader.start();


  }

  /**
   * Gets the currently selected MessageHeader, or null if no header is
   * currently selected.
   */
  public MessageHeader getSelectedHeader() {
     TreePath path = m_tree.getSelectionPath();
     if (path == null) return null;
     return (MessageHeader)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();

  }

}

/**
 * Alternative tree row renderer. Should behave better than the old version.
 */
class ThreadTreeCellRenderer extends JPanel implements TreeCellRenderer {
// basically a panel with several jlabels.
  private JLabel[] labels;
  private HeaderControl m_headers;
  private int m_row;
  private JTree m_tree;
  private MessageHeader m_head;

  public ThreadTreeCellRenderer(int columns, HeaderControl cntr) {
     setLayout(null);     // have to use absolute positioning.
     labels = new JLabel[columns];
     m_headers = cntr;
     for (int i=0;i<columns;i++) {
        labels[i] = new JLabel();
        labels[i].setOpaque(true);
        this.add(labels[i]);
     }
  }


  /**
   * Sets the position and text of all the JLabels. Should be called when the
   * header control changed.
   */
  public void  updateBounds() {
     for (int i=0;i<labels.length;i++) {
        labels[i].setBounds(getColumnBounds(i));
     }
     labels[0].setText(m_head.getFieldValue("subject"));
     labels[1].setText(m_head.getRealName());
     labels[2].setText(m_head.getFieldValue("date"));
     labels[3].setText("");
 //    super.paint(g);
//     paint(getGraphics());
  }

  /**
   * Gets the bounds of an individual column item.
   */
  public Rectangle getColumnBounds(int colnum) {
//  if (Debug.TRACE_LEVEL_1) Debug.println(1, this, ("Column "+colnum+", row "+m_row);
     if (m_row >=0) {
// TODO: Fix for Swing 1.1     int indent = m_tree.getUI().getRowBounds(m_row).x;
    int indent =0;
       int x;
     if (colnum == 0) {
        x = 0;
     } else {
        x = m_headers.getColumnPos(colnum+1) - indent;
     }
      int y = 0;
      int w;
      if (colnum == 0) {
        w = m_headers.getColumnWidth(colnum+1) - indent;
      } else {
        w = m_headers.getColumnWidth(colnum+1);
      }
      int h = getHeight();
      return new Rectangle(x,y,w,h);
     } else {

        if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "row < 0");
                return new Rectangle(0,0,0,0);
     }
  }

  /**
   * Returns the renderer. the tree node value must be a MessageHeader object.
   */
  public synchronized Component getTreeCellRendererComponent(JTree tree,
    Object value, boolean selected, boolean expanded, boolean leaf, int row,
    boolean hasfocus) {
    //  System.out("Value is "+value.getUserObject());
      m_tree = tree;
      m_row = row;
      DefaultMutableTreeNode v = (DefaultMutableTreeNode)value;
      m_head = (MessageHeader)v.getUserObject();
      //setOpaque(true);
     // m_selected = selected;
 //     setSize(getPreferredSize());
 //     setSize(new Dimension(tree.getSize().width, 15));
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Trying to repaint");
     updateBounds();
      return this;
  }

   public Dimension getPreferredSize() {
     int total = 0;
    for (int i=1; i<=4; i++) {
      total = total + m_headers.getColumnWidth(i);
    }
    return new Dimension(total, 15);
  }

  public Dimension getMinimumSize() {
     return getPreferredSize();
  }



}

/**
 * Draws individual tree rows. Should *really* move this into a different
 * file!!!
 @version 0.3
 @author Brian Duff
 */
class MyTreeRenderer extends Component implements TreeCellRenderer {

  protected HeaderControl m_header;
  protected MessageHeader m_messagehead;
  protected JTree m_tree;
  protected int m_row;
  protected boolean m_selected;
  protected static final Color bgSelection = UIManager.getColor("textHighlight");
  protected static final Color fgSelection = UIManager.getColor("textHighlightText");
  private Color bgNonSelection;
  private Color fgNonSelection;
  private Font  myfont;

  public MyTreeRenderer() {

  }

  public MyTreeRenderer(HeaderControl cntr) {
    m_header = cntr;
  }

  /**
   * Sets the HeaderControl object to get the column widths from
   */
  public void setColumnHeader(HeaderControl cntr) {
    m_header = cntr;
  }

  public void updateFields() {

  }

  public Dimension getPreferredSize() {
     int total = 0;
    for (int i=1; i<=4; i++) {
      total = total + m_header.getColumnWidth(i);
    }
    return new Dimension(total, 15);

  }

  /**
   * Returns the renderer. the tree node value must be a MessageHeader object.
   */
  public synchronized Component getTreeCellRendererComponent(JTree tree,
    Object value, boolean selected, boolean expanded, boolean leaf, int row,
    boolean hasfocus) {

     if (value == null || tree == null) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "null tree object in TheadTree Cell Renderer row "+row);
       JLabel dummy = new JLabel();
       dummy.setText("Invalid Tree Item!");
       return dummy;
     }
     m_tree = tree;
     m_row = row;
      DefaultMutableTreeNode v = (DefaultMutableTreeNode)value;
     if (v.getUserObject() instanceof MessageHeader) {
        m_messagehead = (MessageHeader)v.getUserObject();
       m_selected = selected;
       bgNonSelection = m_messagehead.getBackground();
       fgNonSelection = m_messagehead.getForeground();
       myfont         = m_messagehead.getFont();
       setSize(getPreferredSize());
       setSize(new Dimension(tree.getSize().width, 15));
     }
      return this;
  }

  /**
   * Sets the clipping rectangle to the specified column number's bounds.
   */
  private void clipToColumn(Graphics g, int colnum, int indent) {
      //g.setClip(0,0, getSize().width, getSize().height);
      int x;
      g.setFont(myfont);
      if (colnum == 1) {
        x = 0;
      } else {
        x = m_header.getColumnPos(colnum) - indent;
      }
      int y = 0;
      int w;
      if (colnum == 1) {
        w = m_header.getColumnWidth(colnum) - indent;
      } else {
        w = m_header.getColumnWidth(colnum);
      }
      int h = getSize().height;
      g.setClip(x, y, w, h);

      if (m_selected) {
        g.setColor(bgSelection);
        g.fillRect(x, y, w, h);
        g.setColor(fgSelection);
      } else {
        g.setColor(bgNonSelection);
        g.fillRect(x, y, w, h);
        g.setColor(fgNonSelection);
      }

  }

  public void paint(Graphics g) {
    // Paint subject heading.
   if (m_header != null && m_tree != null) {
     int indent;
     try {
//TODO: FIx for Swing 1.1       indent = m_tree.getUI().getRowBounds(m_row).x;
     } catch (NullPointerException ex) {
       indent = 0;
     }
     indent =0;
     clipToColumn(g, 1, indent);
     g.drawString(m_messagehead.getFieldValue("subject"),
      m_header.getColumnPos(1), getSize().height-2);
     // Paint from heading.
     clipToColumn(g, 2, indent);
     g.drawString(m_messagehead.getRealName(),
      m_header.getColumnPos(2)-indent, getSize().height-2);
     // Paint sent heading.
     clipToColumn(g, 3, indent);
     g.drawString(m_messagehead.getFieldValue("date"),
      m_header.getColumnPos(3)-indent, getSize().height-2);
     // Paint relevance heading.
     clipToColumn(g, 4, indent);
     g.drawString("Agents",
      m_header.getColumnPos(4)-indent, getSize().height-2);
   }

  }

}

class ThreadTree_m_tree_treeExpansionAdapter implements javax.swing.event.TreeExpansionListener{
  ThreadTree adaptee;

  ThreadTree_m_tree_treeExpansionAdapter(ThreadTree adaptee) {
    this.adaptee = adaptee;
  }

  public void treeExpanded(TreeExpansionEvent e) {
    try {
    adaptee.m_tree_treeExpanded(e);
    } catch (Exception ex) {
      ;
    }
  }

  public void treeCollapsed(TreeExpansionEvent e) {
    adaptee.m_tree_treeCollapsed(e);
  }
}

class ThreadTree_m_tree_componentAdapter extends java.awt.event.ComponentAdapter {
  ThreadTree adaptee;

  ThreadTree_m_tree_componentAdapter(ThreadTree adaptee) {
    this.adaptee = adaptee;
  }

  public void componentResized(ComponentEvent e) {
   ;
  }
}

class ThreadTree_m_tree_valueAdapter implements javax.swing.event.TreeSelectionListener {
  ThreadTree adaptee;
  ThreadTree_m_tree_valueAdapter(ThreadTree adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(TreeSelectionEvent e) {
     adaptee.treeSelectionEvent(e);
  }

}


class ThreadTree_m_tree_mouseAdapter implements java.awt.event.MouseListener {
  ThreadTree adaptee;

  ThreadTree_m_tree_mouseAdapter(ThreadTree adaptee) {
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
// Old History
//
 // <UL>
 // <LI>0.1 [18/02/98]: Initial Revision
 // <LI>0.2 [23/02/98]: Added column width checking. First properly working
 //    version (has a few horrible hacks).
 // <LI>0.3 [26/02/98]: Removed nasty hacks, now making the selection the
 //    clipping region for each column. Seems to have an exception
 //    during event dispatching under Windows, but not X. Platform
 //    independence? Hmmm.
 // <LI>0.4 [03/03/98]: Updated colours to Swing 1.0.
 // <LI>0.5 [10/03/98]: Set minimum size.
 // <LI>0.6 [31/03/98]: Implemented setProvider. Changed renderer to use a
 //   component. Forced the horizontal scrollbar to always be visible.
 // <LI>0.7 [01/04/98]: Added tree selection events. Altered setProvider to
 //   change the main application window's titlebar.
 // <LI>0.8 [02/04/98]: Added popup menu over header items. Implemented
 //   getSelectedHeader(). Cleared MsgDisplayPanel and tree selection when
 //   the provider changes. Updated to display status message during header
 //   retrieval.
 // <LI>0.9 [04/04/98]: Added support for a ProgressMonitor during header
 //   retrieval. Fixed location of popup menu to adjust depending on scrollbar
 //   values.
 // <LI>0.10 [05/04/98]: Changed to use a ProgressDialog instead of the
 //   ProgressMonitor (PM has problems under UNIX)
 // <LI>0.11 [06/04/98]: Worked around tree update bug (Apparently a Swing bug).
 //   now uses ProgressMonitor.showAfterDelay() to avoid unnecessary clutter.
 // <LI>0.12 [07/04/98]: Added setActive()
 // <LI>0.13 [18/04/98]: Fixed status bar and selection issues with tree
 //   selection event, when an error occurs.
 // <LI>0.14 [21/04/98]: Added clearList(). Fixed null pointer problem in cell
 //   renderer.
 // <LI>0.15 [28/04/98]: Added support for displaying headers with a variety of
 //   fonts and colours.
 // <LI>0.16 [29/04/98]: Added marking messages as read (this should be more
 //   configurable, maybe e.g. a certain time before marking the message)
 // <LI>0.17 [04/05/98]: Changed column header implementation so that scrolling
 //   works properly.
 // <LI>0.18 [30/06/98]: Added event handling for selections, making the thread
 //   tree more "Beanlike", so that it interfaces with the new MainFrame better.
 //   this is still not complete: A lot of code from this class needs to be moved
 //   elsewhere (probably into MainFrame). This might mean a complete rewrite of
 //   this class (new version). So, MainFrame now has deprecated methods...
//
// New History:
//
// $Log: not supported by cvs2svn $
// Revision 1.6  2001/02/11 02:51:00  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:32:08  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//