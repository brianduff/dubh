// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ComponentNode.java,v 1.2 2001-02-11 02:52:12 briand Exp $
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

import java.awt.Component;
import java.awt.Container;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.dubh.dju.misc.ArrayEnumeration;

/**
 * Represents a UI component.
 * <P>
 * @author Brian Duff
 */
public class ComponentNode implements TreeNode
{
   private Component m_component;
   private ComponentNode m_parent;

   private ArrayEnumeration m_childEnum;

   private TreeNode[] m_children;

   /**
    * Construct a component node for the specified component. This is the
    * root node constructor.
    */
   public ComponentNode(Component c)
   {
      this(null, c);
   }

   /**
    * Construct a component node for the specified component, with the
    * specified parent. This is used internally to construct child nodes
    */
   protected ComponentNode(ComponentNode parent, Component c)
   {
      m_parent = parent;
      m_component = c;
   }

   public Component getComponent()
   {
      return m_component;
   }

   /**
    * Loads the children of this node if necessary.
    */
   private void loadChildrenIfNecessary()
   {
      if (m_children == null)
      {
         if (getComponent() instanceof Container)
         {
            Component[] allChildren =
               ((Container)getComponent()).getComponents();
            int childCount = (allChildren == null ? 0 : allChildren.length);
            m_children = new TreeNode[childCount];

            for (int i=0; i < childCount; i++)
            {
               m_children[i] = new ComponentNode(this, allChildren[i]);
            }
         }
         else
         {
            m_children = new TreeNode[0];
         }
      }
   }

   public Enumeration children()
   {
      loadChildrenIfNecessary();
      if (m_childEnum == null)
      {
         m_childEnum = new ArrayEnumeration(m_children);
      }
      m_childEnum.reset();
      return m_childEnum;
   }

   public boolean getAllowsChildren()
   {
      return true;
   }

   public TreeNode getChildAt(int childIndex)
   {
      loadChildrenIfNecessary();
      return m_children[childIndex];
   }

   public int getChildCount()
   {
      if (getComponent() instanceof Container)
      {
         return ((Container)getComponent()).getComponentCount();
      }
      return 0;
   }

   public int getIndex(TreeNode node)
   {
      loadChildrenIfNecessary();
      for (int i=0; i < getChildCount(); i++)
      {
         if (node == getChildAt(i))
         {
            return i;
         }
      }
      return -1;
   }

   public TreeNode getParent()
   {
      return m_parent;
   }

   public boolean isLeaf()
   {
      return getChildCount() == 0;
   }

   public String toString()
   {
      return getComponent().getClass().getName();
   }

}

