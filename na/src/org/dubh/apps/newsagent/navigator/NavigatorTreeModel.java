// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorTreeModel.java,v 1.3 1999-11-09 22:34:42 briand Exp $
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
import java.util.Hashtable;

import javax.swing.tree.*;
import javax.swing.event.*;

import javax.swing.Icon;

import javax.mail.*;

import org.javalobby.dju.misc.Debug;

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
 * @version $Id: NavigatorTreeModel.java,v 1.3 1999-11-09 22:34:42 briand Exp $
 */
public class NavigatorTreeModel implements TreeModel
{  
   protected NavigatorServiceList m_nslServices;
   protected ArrayList m_alListeners;
   private static NavigatorNode ROOT = new NavigatorRoot();
   private Hashtable m_hashChildren;
   
   /**
    * Construct a tree model for a navigator.
    * @param nsl An object which provides a list of services
    */
   public NavigatorTreeModel(NavigatorServiceList nsl)
   {
      m_nslServices = nsl;
      m_alListeners = new ArrayList();
      m_hashChildren = new Hashtable();
   }
 
   public void throwAwayCache()
   {
      m_hashChildren.clear();
   }
   
   protected ArrayList getChildrenFromCache(Object parent)
   {
      ArrayList kids = (ArrayList)m_hashChildren.get(parent);
      if (kids == null)
      {
         // For the root object, return a service.
         if (parent == ROOT)
         {
            kids =  m_nslServices.getServices();
         }
         
         // For services, get a service provider
         if (parent instanceof NavigatorService)
         {
            kids =  ((NavigatorService)parent).getServiceProviders();
          //  if (Debug.TRACE_LEVEL_3)
          //  {
          //     Debug.println(3, this, "First provider is "+kids.get(0)+" with name "+((NavigatorServiceProvider)kids.get(0)).getName());
          //  }
         }
         
         try
         {
            // For service providers, get a folder
            if (parent instanceof NavigatorServiceProvider)
            {
               Folder[] f = ((NavigatorServiceProvider)parent).getRootFolder().list();
               kids = new ArrayList(f.length);
               for (int i=0; i < f.length; i++)
               {
                  kids.add(new NavigatorFolderWrapper(
                     (NavigatorServiceProvider)parent,
                     f[i]
                  ));
               }
            }
            
            // For a folder, get a sub folder
            if (parent instanceof NavigatorFolderWrapper)
            {
               NavigatorFolderWrapper parentWrapper = (NavigatorFolderWrapper)parent;
               Folder f = parentWrapper.getFolder();
               if ((f.getType() & Folder.HOLDS_FOLDERS) > 0)
               {   
                  Folder[] folds = f.list();
                  kids = new ArrayList(folds.length);
                  for (int i=0; i < folds.length; i++)
                  {
                     kids.add(new NavigatorFolderWrapper(
                        getProviderForFolder(f), 
                        folds[i]
                     ));
                  }
               }
               else
               {
                  kids = new ArrayList(0);
               }
            }
         }
         catch (MessagingException me)
         {
            System.out.println("NavigatorTreeMode: add implementation for MessagingException");
         }
         
         
         if (kids == null)
         {
            throw new IllegalArgumentException(
               "Unknown node type in navigator "+parent+" (type: "+parent.getClass().getName()+")"
            );
         }
         
         
         m_hashChildren.put(parent, kids);
      }
      
      return kids;
      
   }
 
   public void addTreeModelListener(TreeModelListener tml)
   {
      m_alListeners.add(tml);
   }
   
   /**
    * Get the child of the specified node
    */
   public Object getChild(Object parent, int index)
   {
      return getChildrenFromCache(parent).get(index);
   }
   
   /**
    * Get the number of children of the specified node
    */
   public int getChildCount(Object parent)
   {
      return getChildrenFromCache(parent).size();
   }
   
   /**
    * Get the index of a child node within its parent
    */
   public int getIndexOfChild(Object parent, Object child)
   {
      int numChildren = getChildCount(parent);
      for (int i=0; i < numChildren; i++)
      {
         if (getChild(parent, i).equals(child))
         {
            return i;
         }
      }
      
      return -1;
   }
   
   /**
    * Returns the root. This should never be displayed for the 
    * navigator.
    */
   public Object getRoot()
   {
      return ROOT;
   }
   
   /**
    * Returns true if the specified node is a leaf node
    */
   public boolean isLeaf(Object node)
   {
      return (getChildCount(node) == 0);
   }
   
   public void removeTreeModelListener(TreeModelListener tml)
   {
      m_alListeners.remove(tml);
   }
   
   /**
    * Called when a tree value has been edited
    */
   public void valueForPathChanged(TreePath tp, Object newValue)
   {
      // TODO
   }
   
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


   static class NavigatorRoot implements NavigatorNode
   {
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
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/10/24 00:45:09  briand
// Many changes. Works properly now.
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//