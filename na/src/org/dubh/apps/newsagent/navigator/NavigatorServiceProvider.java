// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NavigatorServiceProvider.java,v 1.5 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.navigator;

import java.io.IOException;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import javax.swing.Icon;
import javax.swing.tree.TreeNode;

import org.dubh.dju.misc.UserPreferences;
import org.dubh.dju.ui.LazyTreeNode;
import org.dubh.dju.diagnostic.Assert;

/**
 * A navigator service provider contains a (possibly nested)
 * set of folders which in turn contain objects of some kind.
 * The service provider provides information about which
 * folders to display.
 *
 * Folders are JavaMail Folder objects.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorServiceProvider.java,v 1.5 2001-02-11 02:51:00 briand Exp $
 */
public abstract class NavigatorServiceProvider extends LazyTreeNode implements NavigatorNode
{
   /**
    * Populate the service provider node.
    */
   protected void populate()
   {
      try
      {
         Folder root = getRootFolder();

         Folder[] kids = root.listSubscribed();


         if (kids != null)
         {
            for (int i=0; i < kids.length; i++)
            {
               addChild(new NavigatorFolderWrapper(this, kids[i]));
            }
         }
      }
      catch (MessagingException me)
      {
         // TODO: Handle this error
         me.printStackTrace();
      }
   }

   /**
    * The service provider should delete its storage (eg. the user
    * preferences file)
    */
   public abstract void delete() throws IOException;

   public abstract UserPreferences getPreferences();

   /**
    * Get the root folder, which is assumed to contain all other
    * folders on the service. The root folder is usually not displayed.
    */
   public abstract Folder getRootFolder() throws MessagingException;

   /**
    * Set the internal name of the service. This will be used by
    * the NavigatorService to set your service instance's name
    * when the service provider is instantiated.
    */
   public abstract void setName(String name);

   /**
    * Get the service for this service provider. This is usually the
    * parent node.
    */
   public NavigatorService getService()
   {
      TreeNode tnParent = getParent();
      if (Assert.ENABLED)
      {
         Assert.that((tnParent != null),
            "Service Provider doesn't have a parent!!!");
         Assert.that((tnParent instanceof NavigatorService),
            "Service Provider's parent is not a service!!");
      }

      return (NavigatorService)tnParent;
   }

   /**
    * Get the (internal) name of the service.
    */
   public abstract String getName();

   /**
    * Get the nice name of the service as it will be displayed to the user.
    * This can be the same as the internal name if you like.
    */
   public abstract String getNiceName();

}

//
// $Log: not supported by cvs2svn $
// Revision 1.4  2000/06/14 21:36:46  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.3  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.2  1999/10/24 00:44:38  briand
// Interface changes.
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//