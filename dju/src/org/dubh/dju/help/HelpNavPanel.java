// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HelpNavPanel.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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

package org.dubh.dju.help;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import org.dubh.dju.misc.Debug;
import org.dubh.dju.html.HTMLParseException;

// *** Undocumented Plaf package may change in future Swing releases ***
import javax.swing.plaf.basic.*;

/**
 * The navigator panel that contains a tree control, tab pane and box for
 * searching. At the moment, only the contents tab is implemented.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [03/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [03/07/98]
 */
class HelpNavPanel extends JPanel {
  protected BorderLayout layoutMain      = new BorderLayout();
  protected JTabbedPane  tpaneTabs       = new JTabbedPane();
  protected JTree        treeContents    = new JTree();
  protected JScrollPane  scrollContents  = new JScrollPane(treeContents);
  protected Vector       m_listeners     = new Vector();
  /** Package local icons */
  Icon                   m_icoTopic = null, m_icoNewTopic = null,
                         m_icoOpenFolder = null, m_icoOpenNewFolder = null,
                         m_icoFolder = null, m_icoNewFolder = null,
                         m_icoWeb = null, m_icoNewWeb = null;

  protected static final String
     CLASS_TOPIC = "dhTopic",      CLASS_NEWTOPIC = "dhNewTopic",
     CLASS_FOLDER = "dhFolder",    CLASS_NEWFOLDER = "dhNewFolder",
     CLASS_WEB    = "dhWebLink",   CLASS_NEWWEB   = "dhNewWebLink";


  public HelpNavPanel() {
     treeContents.addTreeSelectionListener(new TreeListener());
     treeContents.setCellRenderer(new TreeRenderer());
     treeContents.setRootVisible(false);
     treeContents.setShowsRootHandles(true);
     this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
     tpaneTabs.addTab("Contents", scrollContents);
     this.setLayout(layoutMain);
     this.add(tpaneTabs, BorderLayout.CENTER);
  }

  /**
   * Set the URL to get contents from
   */
  public void setContentsURL(URL contents) throws HTMLParseException, java.io.IOException {
     ContentsParser p = new ContentsParser(contents);
     setModel(p.getTree());
  }


  /**
   * Set the tree model
   */
  protected void setModel(TreeModel m) {
     treeContents.setModel(m);
     validate();
  }

  public Dimension getMinimumSize() {
        return new Dimension(100, 500);
  }

  public Dimension getPreferredSize() {
        return getMinimumSize();
  }

  /**
   * Add an action listener to listen out for clicks on individual tree items.
   * the source object of the event will be a HelpContentsNode instance.
   */
  public void addActionListener(ActionListener l) {
     m_listeners.addElement(l);
  }

  public void removeActionListener(ActionListener l) {
     m_listeners.removeElement(l);
  }

  protected void fireActionPerformed(ActionEvent e) {
     Enumeration listeners = m_listeners.elements();
     while (listeners.hasMoreElements()) {
        ((ActionListener)listeners.nextElement()).actionPerformed(e);
     }
  }

  class TreeListener implements TreeSelectionListener {
     public void valueChanged(TreeSelectionEvent e) {
        try {
           DefaultMutableTreeNode sel = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
           HelpContentsNode node = (HelpContentsNode) sel.getUserObject();
           fireActionPerformed(new ActionEvent(node, ActionEvent.ACTION_PERFORMED, ""));
        } catch (ClassCastException ex) {
           Debug.println("HelpNavPanel.TreeListener: Unknown tree item.");
        }
     }
  }

  class TreeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
      public  Component getTreeCellRendererComponent(JTree tree,
         Object v, boolean selected, boolean expanded, boolean leaf, int row,
        boolean hasfocus) {
        // get the superclass renderer (a JLabel)
        JLabel lab = (JLabel) super.getTreeCellRendererComponent(
           tree, v, selected, expanded, leaf, row, hasfocus
        );
        // determine the correct type of icon for the tree object
        Object userObject = ((DefaultMutableTreeNode)v).getUserObject();
        if (userObject instanceof HelpContentsNode) {
           String cls = ((HelpContentsNode)userObject).getCls();
           if (cls != null) {
              if (cls.equals(CLASS_FOLDER) && m_icoFolder != null) {
                 if (!expanded || m_icoOpenFolder == null) lab.setIcon(m_icoFolder);
                 else lab.setIcon(m_icoOpenFolder);
              } else if (cls.equals(CLASS_NEWFOLDER) && m_icoNewFolder != null) {
                 if (!expanded || m_icoOpenNewFolder == null) lab.setIcon(m_icoNewFolder);
                 else lab.setIcon(m_icoOpenNewFolder);
              } else if (cls.equals(CLASS_TOPIC) && m_icoTopic != null) {
                 lab.setIcon(m_icoTopic);
              } else if (cls.equals(CLASS_NEWTOPIC) && m_icoNewTopic != null) {
                 lab.setIcon(m_icoNewTopic);
              } else if (cls.equals(CLASS_WEB) && m_icoWeb != null) {
                 lab.setIcon(m_icoWeb);
              } else if (cls.equals(CLASS_NEWWEB) && m_icoNewWeb != null) {
                 lab.setIcon(m_icoNewWeb);
              } // if cls.equals
           }  // if cls != null
        }  // if userObject instanceof
        return lab;
     }  // getTreeCellRendererComponent

  }  // inner class

} // outer class