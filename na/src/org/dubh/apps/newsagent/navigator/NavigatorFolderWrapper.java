// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorFolderWrapper.java,v 1.3 2000-06-14 21:36:45 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
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
package org.javalobby.apps.newsagent.navigator;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import javax.mail.Folder;
import javax.mail.MessagingException;

import org.javalobby.dju.ui.LazyTreeNode;

/**
 * This class wraps up a JavaMail folder and implements
 * the NavigatorNode interface
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorFolderWrapper.java,v 1.3 2000-06-14 21:36:45 briand Exp $
 */
public class NavigatorFolderWrapper extends LazyTreeNode implements NavigatorNode
{
   protected Folder m_folder;

   /**
    * Construct a folder wrapper
    *
    * @param np The parent node
    * @param f The folder to wrap
    */
   public NavigatorFolderWrapper(TreeNode parent, Folder f)
   {
      super(parent);
      m_folder = f;
   }

   /**
    * Populate the folder wrapper
    */
   protected void populate()
   {
      try
      {
         Folder[] children = m_folder.listSubscribed();

         for (int i=0; i < children.length; i++)
         {
            addChild(new NavigatorFolderWrapper(this, children[i]));
         }
      }
      catch (MessagingException me)
      {
         // There are no children.
      }
   }

   /**
    * Folder wrappers aren't lazy.
    */
   protected boolean isLazy()
   {
      return false;
   }

   /**
    * Get the folder wrapped up in this node
    */
   public Folder getFolder()
   {
      return m_folder;
   }

   /**
    * Get the provider for this folder
    */
   public NavigatorServiceProvider getProvider()
   {
      // Go up the tree hierarchy and try to find a service
      // provider. Return null if we reach the root.
      TreeNode cur = getParent();

      do
      {
         if (cur instanceof NavigatorServiceProvider)
         {
            return (NavigatorServiceProvider)cur;
         }
         cur = cur.getParent();
      } while (cur != null);

      return null;
   }

   /**
    * Get the list of valid commands for folders
    */
   public Class[] getCommandList()
   {
      return getProvider().getService().getFolderCommandList();
   }

   /**
    * Get the name of the node as displayed in the navigator
    */
   public String getDisplayedNodeName()
   {
      return getFolder().getName();
   }

   /**
    * get the icon to display in the navigator
    */
   public Icon getDisplayedNodeIcon()
   {
      return getProvider().getService().getFolderIcon();
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//