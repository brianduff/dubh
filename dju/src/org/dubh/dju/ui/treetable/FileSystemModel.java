// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FileSystemModel.java,v 1.4 2000-10-09 00:04:57 briand Exp $
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

import java.io.File;
import java.util.Date;

import javax.swing.tree.*;

import java.util.Enumeration;

import org.javalobby.dju.misc.ArrayEnumeration;

/**
 * FileSystemModel is a TreeTableModel representing a hierarchical file 
 * system. Nodes in the FileSystemModel are FileNodes which, when they 
 * are directory nodes, cache their children to avoid repeatedly querying 
 * the real file system. 
 * 
 * @author Brian Duff - rewritten to use a delegate tree model
 */

public class FileSystemModel extends AbstractTreeTableModel 
{

   // Names of the columns.
   static protected String[]  cNames = {"Name", "Size", "Type", "Modified"};

   // Types of the columns.
   static protected Class[]  cTypes = {TreeTableModel.class, Integer.class, String.class, Date.class};

   // The the returned file length for directories. 
   public static final Integer ZERO = new Integer(0); 

   public FileSystemModel()
   {
      super(new DefaultTreeModel(new FileNode(new File(File.separator))));
   }
    //
    //  The TreeTableNode interface. 
    //

   public int getColumnCount()
   {
      return cNames.length;
   }

   public String getColumnName(int column)
   {
      return cNames[column];
   }

   public Class getColumnClass(int column)
   {
      return cTypes[column];
   }
 
   public Object getValueAt(Object node, int column)
   {
      File file = ((FileNode)node).getFile();

      switch(column)
      {
         case 0:
            return file.getName();
         case 1:
            return file.isFile() ? new Integer((int)file.length()) : ZERO;
         case 2:
            return file.isFile() ?  "File" : "Directory";
         case 3:
            return new Date(file.lastModified());
      }

      return null;
   }
}

