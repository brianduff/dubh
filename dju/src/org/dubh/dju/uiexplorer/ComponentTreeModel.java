// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ComponentTreeModel.java,v 1.1 2000-10-09 00:03:41 briand Exp $
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
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package org.javalobby.dju.uiexplorer;

import org.javalobby.dju.ui.treetable.AbstractTreeTableModel;
import org.javalobby.dju.ui.treetable.TreeTableModel;

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

 