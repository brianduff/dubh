// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: TreeTableModelAdapter.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.ui.treetable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

/**
 * This is a wrapper class takes a TreeTableModel and implements
 * the table model interface. The implementation is trivial, with
 * all of the event dispatching support provided by the superclass:
 * the AbstractTableModel.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 * @author Scott Violet
 */


public class TreeTableModelAdapter extends AbstractTableModel
{
    JTree tree;
    TreeTableModel treeTableModel;

    public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

   tree.addTreeExpansionListener(new TreeExpansionListener() {
       // Don't use fireTableRowsInserted() here;
       // the selection model would get  updated twice.
       public void treeExpanded(TreeExpansionEvent event) {
         fireTableDataChanged();
       }
            public void treeCollapsed(TreeExpansionEvent event) {
         fireTableDataChanged();
       }
   });
    }

  // Wrappers, implementing TableModel interface.

    public int getColumnCount() {
   return treeTableModel.getColumnCount();
    }

    public String getColumnName(int column) {
   return treeTableModel.getColumnName(column);
    }

    public Class getColumnClass(int column) {
   return treeTableModel.getColumnClass(column);
    }

    public int getRowCount() {
   return tree.getRowCount();
    }

    protected Object nodeForRow(int row) {
   TreePath treePath = tree.getPathForRow(row);
   return treePath.getLastPathComponent();
    }

    public Object getValueAt(int row, int column) {
   return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    public boolean isCellEditable(int row, int column) {
         return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    public void setValueAt(Object value, int row, int column) {
   treeTableModel.setValueAt(value, nodeForRow(row), column);
    }
}
