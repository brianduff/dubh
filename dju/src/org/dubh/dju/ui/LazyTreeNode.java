// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: LazyTreeNode.java,v 1.1 2000-06-14 21:26:18 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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

package org.javalobby.dju.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.javalobby.dju.diagnostic.Assert;

/**
 * A lazy tree node is an abstract implementation of the TreeNode interface
 * that provides implementations for most methods and allows subclasses to
 * concentrate solely on populating their children.
 *
 * The lazy tree node can be lazy or non-lazy. A lazy node does not populate
 * until it is actually expanded in the tree. Because of this, it always
 * shows an expansion icon until it is actually expanded, regardless of
 * whether it has children. A non-lazy node shows the correct expansion icon
 * and populates when it is first displayed. You can override the isLazy()
 * method and return true or false to make the node lazy or not. The default
 * is lazy.
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 * @version $Id: LazyTreeNode.java,v 1.1 2000-06-14 21:26:18 briand Exp $
 */
public abstract class LazyTreeNode implements MutableTreeNode
{

   ///////////////////////////////////////////////////////////////////////////
   // Static Finals
   ///////////////////////////////////////////////////////////////////////////

   /**
    * By default the LazyTreeNode is lazy.
    */
   private static final boolean DEFAULT_LAZY = true;

   ///////////////////////////////////////////////////////////////////////////
   // Fields
   ///////////////////////////////////////////////////////////////////////////

   private ArrayList m_children;
   private TreeNode m_parent;

   /**
    * True after doPopulate() has been called
    */
   private boolean m_isPopulated;

   /**
    * Used to stop addChild & removeChild from trying to populate while
    * we're in populate(). This would cause inifinite recursion, which is
    * Bad.
    */
   private boolean m_isPopulating;

   private BusyTarget m_busyTarget;


   ///////////////////////////////////////////////////////////////////////////
   // Constructors
   ///////////////////////////////////////////////////////////////////////////

   /**
    * Construct a lazy tree node with no parent. This is usually the root
    * node.
    */
   public LazyTreeNode()
   {
      this(null);
   }

   /**
    * Construct a lazy tree node with the specified parent.
    *
    * @param parent the parent of the node. Can be null, which usually
    *   indicates that this is the root node.
    */
   public LazyTreeNode(TreeNode parent)
   {
      m_parent = parent;
   }

   ///////////////////////////////////////////////////////////////////////////
   // Methods
   ///////////////////////////////////////////////////////////////////////////

   /**
    * Set a busy target
    *
    * @see #BusyTarget
    */
   protected void setBusyTarget(BusyTarget bt)
   {
      m_busyTarget = bt;
   }

   private BusyTarget getBusyTarget()
   {
      return m_busyTarget;
   }

   private void setBusy(boolean isBusy)
   {
      BusyTarget b = getBusyTarget();
      if (b != null)
      {
         b.setBusy(isBusy);
      }
   }

   /**
    * If the tree node populates lazily, this method returns true. You can
    * override this in a subclass to return a different value.
    */
   protected boolean isLazy()
   {
      return DEFAULT_LAZY;
   }

   /**
    * Add a child at the end of the children of this node.
    *
    * @param n a child to add. Must not be null.
    */
   public final void addChild(MutableTreeNode n)
   {
      if (Assert.ENABLED)
      {
         Assert.that((n != null), "Child node must not be null.");
      }

      if (!m_isPopulated && !m_isPopulating)
      {
         doPopulate();
      }

      // say("Adding child "+n);
      m_children.add(n);
   }

   /**
    * Add a child to this node. You can call this method before the node
    * has populated, but this will cause the node to be populated first.
    *
    * @param n The child to add. Must not be null.
    */
   public final void insert(MutableTreeNode n, int index)
   {
      if (Assert.ENABLED)
      {
         Assert.that((n != null), "Child node must not be null.");
      }

      if (!m_isPopulated && !m_isPopulating)
      {
         doPopulate();
      }

      // say("Adding child "+n+" at "+index);
      m_children.add(index, n);
   }

   /**
    * Remove a child from this node. You can call this before the node
    * has populated, but this will cause the node to be populated first.
    *
    * @param n The node to remove. Must not be null.
    */
   public final void remove(MutableTreeNode n)
   {
      if (Assert.ENABLED)
      {
         Assert.that((n != null), "Attempted to removeChild(null)");
      }

      if (!m_isPopulated && !m_isPopulating)
      {
         doPopulate();
      }

      // say("Removing child "+n);
      m_children.remove(n);
   }

   /**
    * Remove the child node at a specified index from this node.
    *
    * @param index the index of the child to remove.
    */
   public final void remove(int index)
   {
      if (!m_isPopulated && !m_isPopulating)
      {
         doPopulate();
      }

      // say("Removing child at "+index);
      m_children.remove(index);
   }

   /**
    * Remove this node from its parent.
    */
   public void removeFromParent()
   {
      if (Assert.ENABLED)
      {
         Assert.that((m_parent != null),
            "Attempted to remove the root node from its parent"
         );
      }

      // say("Removing node from parent");
      ((MutableTreeNode)m_parent).remove(this);
   }

   /**
    * This is a stupid thing to have on the MutableTreeNode interface, because
    * it makes assumptions about how you are implementing your tree nodes;
    * specifically, it assumes you are wrapping up some object in your
    * tree nodes in the same way that DefaultMutableTreeNode does. This
    * is not how we are implementing our nodes, so we have to ignore this
    * method. Oy javasoft, no!
    */
   public void setUserObject(Object o)
   {
      // say("SetUserObject "+o);
      // IGNORED
   }

   /**
    * Actually populate the children of this node. This creates a new list
    * for the children and calls populate() to get the subclass to create its
    * children. It then sets the state of this node to populated.
    */
   private void doPopulate()
   {
      try
      {
         setBusy(true);
         m_children = new ArrayList();
         m_isPopulating = true;
         // say("Populating");
         populate();
         // say("Finished populating");
         m_isPopulating = false;
         m_isPopulated = true;
      }
      finally
      {
         setBusy(false);
      }
   }

   /**
    * Your subclass should populate itself in the implementation of this
    * method. This is normally done by calling addChild(TreeNode) to add
    * all the required children.
    */
   protected abstract void populate();

   /**
    * Convenience method that allows you to set the parent after construction.
    * You should only do this once; it's just here to allow you to use
    * introspection and construct with the default constructor.
    *
    * @param n the parent to set. This must not be null.
    */
   public final void setParent(MutableTreeNode n)
   {
      if (Assert.ENABLED)
      {
         Assert.that((m_parent == null || n == m_parent),
            "Attempt was made to reparent a lazy tree node"
         );

         Assert.that((n != null),
            "You can't set the parent to null"
         );
      }
      // say("Set parent to "+n);
      m_parent = n;
   }

   ///////////////////////////////////////////////////////////////////////////
   // Implementation of TreeNode interface

   /**
    * Get an enumeration of all the children of this node. If the node is
    * lazy, it is populated the first time this method is called.
    */
   public final Enumeration children()
   {
      if (!m_isPopulated)
      {
         doPopulate();
      }
      // say("Enumerating childen");
      return new IteratorEnumerator(m_children.iterator());
   }

   /**
    * Whether the node can have children. Always returns true.
    */
   public final boolean getAllowsChildren()
   {
      // say("Allows chilren? Yes");
      return true;
   }

   /**
    * Get the ith child of this node. If the node is lazy, it is populated
    * the first time this method is called.
    */
   public final TreeNode getChildAt(int i)
   {
      // If we've not populated already, populate now.
      if (!m_isPopulated)
      {
         doPopulate();
      }

      // say("Getting child "+i+" -  is "+m_children.get(i));
      return (TreeNode)m_children.get(i);

   }

   /**
    * Get the number of children this node has. If the node is lazy, this
    * method returns some value > 0. If the node is not lazy, it is populated
    * the first time this method is called and the actual number of children
    * are returned.
    */
   public final int getChildCount()
   {
      if (!m_isPopulated)
      {
         doPopulate();
      }

      // say("Getting child count - is "+m_children.size());
      return m_children.size();
   }

   /**
    * Get the index of a child node. Whether the node is lazy or not, it
    * will be populated at this point.
    */
   public final int getIndex(TreeNode n)
   {
      // Do this using the tree api to force population.
      // say("Get index of "+n);
      int size = getChildCount();
      for (int i=0; i < size; i++)
      {
         if (getChildAt(i).equals(n))
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * Get the parent of this node.
    *
    * @return the parent node. This may be null if the node has no
    * parent.
    */
   public final TreeNode getParent()
   {
      // say("Get parent");
      return m_parent;
   }

   /**
    * Is the node a leaf? If the node is lazy and hasn't been
    * populated yet, this will return false. Otherwise, it will
    * return true if the node has no children.
    */
   public final boolean isLeaf()
   {
      // say("Is leaf?");
      // This is where the "laziness" kicks in. We don't bother
      // populating yet if this is a lazy node.
      if (isLazy() && !m_isPopulated)
      {
         // say("Being lazy, // saying I'm not a leaf");
         return false;
      }
      else
      {
         // Calling getChildCount() will populate if need be.
         // say("Being non-lazy, counting children to find out if I'm a leaf...");
         return (getChildCount() == 0);
      }
   }


   /**
    * Sort the children of this node using the specified comparator.
    */
   protected void sortChildren(Comparator c)
   {
      Collections.sort(m_children, c);
   }

   ///////////////////////////////////////////////////////////////////////////
   // Inner classes
   ///////////////////////////////////////////////////////////////////////////


   /**
    * A busy target is any component which can display that it is in a busy
    * state. The lazy tree node will tell a busy target to get busy before
    * populating, and make it unbusy after populating. Most implementations
    * will display the wait cursor or something equally dull.
    */
   public static interface BusyTarget
   {
      /**
       * Set whether the target should display some kind of wait state to the
       * user.
       *
       * @param b If true, the target should look busy. If false, the target
       *   should stop looking busy.
       */
      void setBusy(boolean b);
   }

   /**
    * The new collections framework uses iterators, but javax.swing.TreeNode
    * still returns Enumerator from children(). So we provide a wrapper round
    * an iterator that implements the Enumerator interface.
    */
   class IteratorEnumerator implements Enumeration
   {
      private Iterator m_iterator;

      public IteratorEnumerator(Iterator i)
      {
         m_iterator = i;
      }

      public boolean hasMoreElements()
      {
         return m_iterator.hasNext();
      }

      public Object nextElement()
      {
         return m_iterator.next();
      }
   }

  /* private void // say(String s)
   {
      System.err.println("LTN on "+toString()+": "+s);
   }
   */

}


//
// $Log: not supported by cvs2svn $
//