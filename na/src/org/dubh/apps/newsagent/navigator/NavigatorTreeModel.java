// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorTreeModel.java,v 1.1 1999-10-17 17:03:58 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.apps.newsagent.navigator;

import java.util.ArrayList;

import javax.swing.tree.*;
import javax.swing.event.*;

import javax.mail.*;

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
 * serviceCommands.1 = dubh.apps.newsagent.navigator.services.nntp.command.service.AddServer
 *
 * providerCommands.count = 1
 * providerCommands.1 = dubh.apps.newsagent.navigator.services.nntp.command.provider.Subscriptions
 *
 * folderCommands.count = 1
 * folderCommands.1 = dubh.apps.newsagent.navigator.services.nntp.command.folder.Unsubscribe
 *
 * providers.count = 1
 * providers.1 = news.us.oracle.com
 * </pre>
 * Each provider, in turn, has a .properties file containing information
 * about the provider.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorTreeModel.java,v 1.1 1999-10-17 17:03:58 briand Exp $
 */
public class NavigatorTreeModel implements TreeModel
{  
   protected NavigatorServiceList m_nslServices;
   protected ArrayList m_alListeners;
   private static final String ROOT = ">>>NewsAgent Navigator<<<";
   
   /**
    * Construct a tree model for a navigator.
    * @param nsl An object which provides a list of services
    */
   public NavigatorTreeModel(NavigatorServiceList nsl)
   {
      m_nslServices = nsl;
      m_alListeners = new ArrayList();
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
      // For the root object, return a service.
      if (parent == ROOT)
      {
         return m_nslServices.getServices().get(index);
      }
      
      // For services, get a service provider
      if (parent instanceof NavigatorService)
      {
         return ((NavigatorService)parent).getServiceProviders().get(index);
      }
      
      try
      {
         // For service providers, get a folder
         if (parent instanceof NavigatorServiceProvider)
         {
            return ((NavigatorServiceProvider)parent).getRootFolder().list()[index];
         }
         
         // For a folder, get a sub folder
         if (parent instanceof Folder)
         {
            return ((Folder)parent).list()[index];
         }
      }
      catch (MessagingException me)
      {
         System.out.println("NavigatorTreeMode: add implementation for MessagingException");
      }
      
      throw new IllegalArgumentException(
         "Unknown node type in navigator "+parent+" (type: "+parent.getClass().getName()+")"
      );
   
   }
   
   /**
    * Get the number of children of the specified node
    */
   public int getChildCount(Object parent)
   {
      // For the root object, return a service.
      if (parent == ROOT)
      {
         return m_nslServices.getServices().size();
      }
      
      // For services, get a service provider
      if (parent instanceof NavigatorService)
      {
         return ((NavigatorService)parent).getServiceProviders().size();
      }
      
      try
      {
         // For service providers, get a folder
         if (parent instanceof NavigatorServiceProvider)
         {
            Folder[] subfolders = 
               ((NavigatorServiceProvider)parent).getRootFolder().list();
            if (subfolders == null)
               return 0;
            else
               return subfolders.length;
         }
         
         // For a folder, get a sub folder
         if (parent instanceof Folder)
         {
            Folder[] subfolders = ((Folder)parent).list();
            if (subfolders == null)
               return 0;
            else
               return subfolders.length;
         }
      }
      catch (MessagingException me)
      {
         System.out.println("NavigatorTreeMode: add implementation for MessagingException");
      }
            
      throw new IllegalArgumentException(
         "Unknown node type in navigator "+parent+" (type: "+parent.getClass().getName()+")"
      );
   
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


}

//
// $Log: not supported by cvs2svn $
//