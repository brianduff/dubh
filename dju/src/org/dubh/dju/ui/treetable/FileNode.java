// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FileNode.java,v 1.2 2001-02-11 02:52:12 briand Exp $
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

import org.dubh.dju.misc.ArrayEnumeration;

import javax.swing.tree.TreeNode;

import java.io.File;
import java.util.Enumeration;

/**
 * A FileNode is a derivative of the File class - though we delegate to
 * the File object rather than subclassing it. It is used to maintain a
 * cache of a directory's children and therefore avoid repeated access
 * to the underlying file system during rendering.
 */
class FileNode implements TreeNode
{
   File     file;
   Object[] children;
   ArrayEnumeration m_childEnum;
   TreeNode m_parent;

   public FileNode(File file)
   {
      this(null, file);
   }

   public FileNode(TreeNode parent, File file)
   {
      m_parent = parent;
      this.file = file;
   }



   /**
    * Returns the the string to be used to display this leaf in the JTree.
    */
   public String toString()
   {
      return file.getName();
   }

   public File getFile()
   {
      return file;
   }

   private void loadChildren()
   {
      String[] files = file.list();
      if(files != null)
      {
         children = new FileNode[files.length];
         String path = file.getPath();
         for(int i = 0; i < files.length; i++)
         {
            File childFile = new File(path, files[i]);
            children[i] = new FileNode(childFile);
         }
      }
      else
      {
         children = new FileNode[0];
      }
   }

   public boolean getAllowsChildren()
   {
      return true;
   }

   public TreeNode getChildAt(int index)
   {
      if (children == null)
      {
         loadChildren();
      }
      return (TreeNode)children[index];
   }

   /**
    * Loads the children, caching the results in the children ivar.
    */
   public Enumeration children()
   {
      if (children == null)
      {
         loadChildren();
         m_childEnum = new ArrayEnumeration(children);
      }
      ((ArrayEnumeration)m_childEnum).reset();
      return m_childEnum;
   }

   public int getChildCount()
   {
      if (children == null)
      {
         loadChildren();
      }
      return children.length;
   }

   public TreeNode getParent()
   {
      return m_parent;
   }

   public boolean isLeaf()
   {
      return getChildCount() == 0;
   }

   public int getIndex(TreeNode child)
   {
      for (int i=0; i < getChildCount(); i++)
      {
         if (getChildAt(i) == child)
         {
            return i;
         }
      }
      return -1;
   }
}
