// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ComponentNode.java,v 1.1 2000-10-09 00:03:41 briand Exp $
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

import java.awt.Component;
import java.awt.Container;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.javalobby.dju.misc.ArrayEnumeration;

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

 