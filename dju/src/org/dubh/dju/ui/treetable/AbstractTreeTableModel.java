// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractTreeTableModel.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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
import javax.swing.tree.*;
import javax.swing.event.*;

/**
 * A tree table model that delegates to another TreeModel. This is handy, as
 * it means you can use a DefaultTreeModel or whatever.
 *
 * @version %I% %G%
 *
 * @author Brian Duff
 */
public abstract class AbstractTreeTableModel implements TreeTableModel
{
   private TreeModel m_delegateModel;


   /**
    * Create an abstract tree table model with the specified model as its
    * delegate.
    */
   public AbstractTreeTableModel(TreeModel delegate)
   {
      m_delegateModel = delegate;
   }

   /**
    * Retrieve the delegate model
    */
   protected TreeModel getDelegateModel()
   {
      return m_delegateModel;
   }

   public Object getRoot()
   {
      return getDelegateModel().getRoot();
   }

   public boolean isLeaf(Object node)
   {
      return getDelegateModel().isLeaf(node);
   }

   public void valueForPathChanged(TreePath path, Object newValue)
   {
      getDelegateModel().valueForPathChanged(path, newValue);
   }

   public int getIndexOfChild(Object parent, Object child)
   {
      return getDelegateModel().getIndexOfChild(parent, child);
   }


   public void addTreeModelListener(TreeModelListener l)
   {
      getDelegateModel().addTreeModelListener(l);
   }

   public void removeTreeModelListener(TreeModelListener l)
   {
      getDelegateModel().removeTreeModelListener(l);
   }

   public Object getChild(Object parent, int index)
   {
      return getDelegateModel().getChild(parent, index);
   }

   public int getChildCount(Object parent)
   {
      return getDelegateModel().getChildCount(parent);
   }


    //
    // Default impelmentations for methods in the TreeTableModel interface.
    //

    public Class getColumnClass(int column) { return Object.class; }

   /** By default, make the column with the Tree in it the only editable one.
    *  Making this column editable causes the JTable to forward mouse
    *  and keyboard events in the Tree column to the underlying JTree.
    */
    public boolean isCellEditable(Object node, int column) {
         return getColumnClass(column) == TreeTableModel.class;
    }

    public void setValueAt(Object aValue, Object node, int column) {}


    // Left to be implemented in the subclass:

    /*
     *   public int getColumnCount()
     *   public String getColumnName(Object node, int column)
     *   public Object getValueAt(Object node, int column)
     */

}