// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ComponentTreeModel.java,v 1.2 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.uiexplorer;

import org.dubh.dju.ui.treetable.AbstractTreeTableModel;
import org.dubh.dju.ui.treetable.TreeTableModel;

import java.awt.Component;

import javax.swing.tree.DefaultTreeModel;

/**
 * This class provides the model for a component tree using the treetable.
 * <P>
 * @author Brian Duff
 */
public class ComponentTreeModel extends AbstractTreeTableModel
{
   private final static int
      COL_CLASS = 0,
      COL_NAME = 1,
      COL_POSITION = 2,
      COL_SIZE = 3;

   private final static String[] COLUMN_NAMES =
   {
      "Class",
      "Name",
      "Position",
      "Size"
   };

   private final static Class[] COLUMN_TYPES =
   {
      TreeTableModel.class,
      String.class,
      String.class,
      String.class
   };


   /**
    * Construct a component tree model with the specified AWT component as
    * the root.
    */
   public ComponentTreeModel(Component rootComponent)
   {
      super(new DefaultTreeModel(new ComponentNode(rootComponent)));
   }

   /**
    * Get the number of columns
    */
   public int getColumnCount()
   {
      return COLUMN_NAMES.length;
   }

   /**
    * Get the name of the specified column
    */
   public String getColumnName(int column)
   {
      return COLUMN_NAMES[column];
   }

   /**
    * Get the class of the specified column
    */
   public Class getColumnClass(int column)
   {
      return COLUMN_TYPES[column];
   }

   /**
    * Get the value of the specified cell
    */
   public Object getValueAt(Object node, int column)
   {
      Component c = ((ComponentNode)node).getComponent();

      switch(column)
      {
         case COL_NAME:
            return c.getName();
         case COL_CLASS:
            return node;
         case COL_SIZE:
            return c.getSize().width+", "+c.getSize().height;
         case COL_POSITION:
            return c.getLocation().x+", "+c.getLocation().y;
      }
      return null;
   }
}

