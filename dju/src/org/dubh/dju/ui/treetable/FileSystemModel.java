// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FileSystemModel.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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

import java.io.File;
import java.util.Date;

import javax.swing.tree.*;

import java.util.Enumeration;

import org.dubh.dju.misc.ArrayEnumeration;

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

