// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractTreeTableModel.java,v 1.4 2000-10-09 00:04:24 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//
//  Copyright 1997, 1998 by Sun Microsystems, Inc.,
//  901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
//  All rights reserved.
//
//  This software is the confidential and proprietary information
//  of Sun Microsystems, Inc. ("Confidential Information").  You
//  shall not disclose such Confidential Information and shall use
//  it only in accordance with the terms of the license agreement
//  you entered into with Sun.
//
// ---------------------------------------------------------------------------
//   Original Author: Philip Milne, Sun Microsystems
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package org.javalobby.dju.ui.treetable;
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