// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NavigatorService.java,v 1.5 2001-02-11 02:51:00 briand Exp $
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

import java.util.List;

import javax.swing.Icon;

import javax.swing.tree.TreeNode;

import javax.mail.Store;

import org.dubh.dju.misc.UserPreferences;

import org.dubh.dju.ui.LazyTreeNode;

/**
 * A NavigatorService is a top level branch in the navigator.
 * A service consists of one or more ServiceProviders which in turn
 * contain folders.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorService.java,v 1.5 2001-02-11 02:51:00 briand Exp $
 */
public abstract class NavigatorService extends LazyTreeNode implements NavigatorNode
{

   /**
    * The node populates its children.
    */
   protected void populate()
   {
      populateServiceProviders();
   }

   /**
    * Add a new service provider to the service and make it persistent.
    */
   public abstract void addProvider(String name, NavigatorServiceProvider nsp);


   /**
    * Remove the specified service provider permanently.
    */
   public abstract void removeProvider(NavigatorServiceProvider nsp);

   /**
    * Any NavigatorService can have a set of UserPreferences
    * associated with it.
    */
   public abstract UserPreferences getPreferences();

   /**
    * Return the protocol name of your service from this method.
    * For instance, a service providing news would have the service
    * name "news" or "nntp".
    */
   public abstract String getServiceProtocolName();

   /**
    * Return a nice (preferrably internationalized) name for your
    * service. This will be the name displayed in the navigator.
    */
   public abstract String getServiceNiceName();

   /**
    * Populate the node with service providers for this service. You
    * should construct objects that subclass NavigatorServiceProvider
    * and use addChild() to add them to the service node.
    */
   protected abstract void populateServiceProviders();

   /**
    * Return a list of command objects that are invokable on your
    * service. These will be used to populate the drop down list for
    * right clicks over the service in the navigator.
    */
   public abstract Class[] getCommandList();

   /**
    * Get a list of commands that are invokable on service providers
    * contained in this service. It is assumed that the list of commands
    * is the same for all providers, but you can always disable commands
    * depending on the selected service.
    */
   public abstract Class[] getProviderCommandList();

   /**
    * Get a list of commands that are invokable on folders contained
    * in service providers on this service.
    */
   public abstract Class[] getFolderCommandList();

   /**
    * Get the icon to display for this service.
    */
   public abstract Icon getIcon();

   /**
    * Get the icon to display for providers in this service.
    */
   public abstract Icon getProviderIcon();

   /**
    * Get the icon to display for folders in this service.
    */
   public abstract Icon getFolderIcon();
}

//
// $Log: not supported by cvs2svn $
// Revision 1.4  2000/06/14 21:36:45  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.3  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.2  1999/10/24 00:43:55  briand
// Change to use new DJU command mechanism
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//