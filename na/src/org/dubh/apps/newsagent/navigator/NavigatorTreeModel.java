// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorTreeModel.java,v 1.5 2000-06-14 21:36:46 briand Exp $
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.*;
import javax.swing.event.*;

import javax.swing.Icon;

import javax.mail.*;

import org.javalobby.dju.misc.Debug;

import org.javalobby.dju.ui.LazyTreeNode;

/**
 * This is the data model for the navigator tree. A number of
 * .properties files are used to persist the navigator tree.
 * These are stored in the NewsAgent directory.
 *
 * The root file is navigator.properties. This is generated
 * from a default file if it doesn't already exist. It contains
 * a number of lines as follows:
 * <pre>
 * services.count = n
 * services.1 = news
 * services.2 = mailto
 * </pre>
 * These specify the services for the navigator.
 * Each service has a .properties file containing information about
 * the providers it contains: e.g. news.properties:
 * <pre>
 * service.niceName = Network News
 * service.icon = dubh/apps/newsagent/navigator/services/nntp/images/service.gif
 * service.providerIcon = dubh/apps/newsagent/navigator/services/nntp/images/provider.gif
 * service.folderIcon = dubh/apps/newsagent/navigator/services/nntp/images/folder.gif
 *
 * serviceCommands.count = 1
 * serviceCommands.1 = org.javalobby.apps.newsagent.navigator.services.nntp.command.service.AddServer
 *
 * providerCommands.count = 1
 * providerCommands.1 = org.javalobby.apps.newsagent.navigator.services.nntp.command.provider.Subscriptions
 *
 * folderCommands.count = 1
 * folderCommands.1 = org.javalobby.apps.newsagent.navigator.services.nntp.command.folder.Unsubscribe
 *
 * providers.count = 1
 * providers.1 = news.us.oracle.com
 * </pre>
 * Each provider, in turn, has a .properties file containing information
 * about the provider.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorTreeModel.java,v 1.5 2000-06-14 21:36:46 briand Exp $
 */
public class NavigatorTreeModel extends DefaultTreeModel
{

   /**
    * Construct a tree model for a navigator.
    * @param nsl An object which provides a list of services
    */
   public NavigatorTreeModel(NavigatorServiceList nsl)
   {
      super(new NavigatorRoot(nsl.getServices()));
   }

/* What is this used for?

   public NavigatorServiceProvider getProviderForFolder(Folder f)
      throws MessagingException
   {
      Store srcStore = f.getStore();
      // Now go through all services
      ArrayList services = getChildrenFromCache(ROOT);
      for (int i=0; i < services.size(); i++)
      {
         NavigatorService ns = (NavigatorService)services.get(i);
         // get all providers
         ArrayList providers = getChildrenFromCache(ns);
         for (int j=0; j < providers.size(); j++)
         {
            NavigatorServiceProvider nsp = (NavigatorServiceProvider)providers.get(i);
            if (srcStore.equals(nsp.getRootFolder().getStore()))
            {
               return nsp;
            }
         }
      }

      return null;
   }
*/

   /**
    * The root node.
    */
   static class NavigatorRoot extends LazyTreeNode implements NavigatorNode
   {
      private List m_services;

      public NavigatorRoot(List services)
      {
         m_services = services;
      }
      /**
       * Populate the children of the root node.
       */
      protected void populate()
      {
         for (int i=0; i < m_services.size(); i++)
         {
            NavigatorService ns = (NavigatorService)m_services.get(i);
            ns.setParent(NavigatorRoot.this);
            addChild(ns);
         }
      }

      public Class[] getCommandList()
      {
         return new Class[0];
      }

      public Icon getDisplayedNodeIcon()
      {
         return null;
      }

      public String getDisplayedNodeName()
      {
         return "<<ROOT>>";
      }

      public String toString()
      {
         return getDisplayedNodeName();
      }
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.4  1999/12/13 22:32:43  briand
// Move to Javalobby changed the paths to various resources. Added fixes to that
// most things work again. Also patched the PropertyFileResolver to create parent
// directories properly. Managed to get NewsAgent to run with the brand new JRE
// 1.2.2 for Linux!!
//
// Revision 1.3  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.2  1999/10/24 00:45:09  briand
// Many changes. Works properly now.
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//