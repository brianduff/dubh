// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: PropertiesService.java,v 1.3 1999-11-09 22:34:42 briand Exp $
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

import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.dju.misc.Debug;
import org.javalobby.dju.misc.UserPreferences;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.util.Properties;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

/**
 * A service based on a Properties object.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: PropertiesService.java,v 1.3 1999-11-09 22:34:42 briand Exp $
 */
public class PropertiesService implements NavigatorService
{
   protected final static String NICE_NAME = "niceName";
   protected final static String PROVIDERS = "providers";
   protected final static String PROVIDER_CLASS = "providerClass";
   protected final static String COUNT = "count";
   protected final static String SERVICE_COMMANDS = "serviceCommands";
   protected final static String PROVIDER_COMMANDS = "providerCommands";
   protected final static String FOLDER_COMMANDS = "folderCommands";
   protected final static String ICON = "icon";
   protected final static String PROVIDER_ICON = "providerIcon";
   protected final static String FOLDER_ICON = "folderIcon";
   
   protected UserPreferences m_properties;
   protected String m_strProtocol;
   
   protected ArrayList m_alProviders = null;
   protected Class[] m_alCommands = null;
   protected Class[] m_alProviderCommands = null;
   protected Class[] m_alFolderCommands = null;
   
   protected Icon m_icoService = null;
   protected Icon m_icoProvider = null;
   protected Icon m_icoFolder = null;
   
   /**
    * Construct a navigator service based on a properties object.
    * @param name the protocol name of the service
    * @param p the properties object
    */
   public PropertiesService(String name, UserPreferences p)
   {
      m_strProtocol = name;
      m_properties = p;
   }

   /**
    * Get the properties file for this provider. 
    */
   public UserPreferences getPreferences()
   {
      return m_properties;
   }
   
   /**
    * Adds the specified service provider to the service. This causes
    * the service's preference file to be written.
    */
   public void addProvider(String name, NavigatorServiceProvider nsp)
   {
   
      nsp.setService(this);
      nsp.setName(name);
      ArrayList l = getServiceProviders();
      l.add(nsp);
      
      m_properties.addToMultiKeyList(PROVIDERS, nsp);
      try
      {
         m_properties.save();
      }
      catch (IOException ioe)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Failed to save properties ");
            Debug.printException(1, this, ioe);
         }
      }
   }
   
   /**
    * Get a property from the properties object.
    */
   protected String getProperty(String key)
   {
      return m_properties.getPreference(key);
   }
   
   protected ArrayList getList(String key)
   {
      return m_properties.getMultiKeyList(key);
   }
   
   /**
    * Get a list of class names, instantiate those classes
    * and return a list of instantiated objects.
    */
   protected ArrayList getObjectList(String key)
   {
      ArrayList classNames = getList(key);
      if (classNames == null) return null;
      ArrayList alReturn = new ArrayList();
      
      for (int i=0; i < classNames.size(); i++)
      {
         String className="";
         try
         {
            className = (String)classNames.get(i);   
            Class c = Class.forName(className);
            alReturn.add(c.newInstance());
         }
         catch (Exception e)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "Failed to instantiate "+className);
               Debug.printException(1, this, e);                  
            }
         }
         
      }
      return alReturn;
   }
   
   
   // NavigatorService interface
   
   /**
    * Return the protocol name of your service from this method. 
    * For instance, a service providing news would have the service
    * name "news" or "nntp".
    */
   public String getServiceProtocolName()
   {
      return m_strProtocol;
   } 

   /**
    * Return a nice (preferrably internationalized) name for your
    * service. This will be the name displayed in the navigator.
    */
   public String getServiceNiceName()
   {
      return getProperty(NICE_NAME);
   }
   
   /**
    * Return a list of objects which implement the ServiceProvider
    * interface. For the news example, these would be nntp servers.
    */
   public ArrayList getServiceProviders()
   {
      try
      {
         Class provClass = Class.forName(getProperty(PROVIDER_CLASS));
         
         if (m_alProviders == null)
         {
            ArrayList provNames = getList(PROVIDERS);
            m_alProviders = new ArrayList(provNames.size());

            for (int i=0; i < provNames.size(); i++)
            {
               String provName = (String)provNames.get(i);
               NavigatorServiceProvider prov = (NavigatorServiceProvider)
                  provClass.newInstance();
               
               if (Debug.TRACE_LEVEL_3)
               {
                  Debug.println("Constructed provider with name "+provName);
               }
               prov.setService(this);
               prov.setName(provName);
               m_alProviders.add(prov);
            }
         }
         
         return m_alProviders;
      }
      catch (Exception e)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Failed to get providers for "+getServiceNiceName());
            Debug.printException(1, this, e);         
         }
      }
      return new ArrayList();
   }
   
   /**
    * Get a list of class objects.
    */
   protected ArrayList getClassList(String key)
   {
      ArrayList classNames = getList(key);
      ArrayList classes = new ArrayList(classNames.size());
      for (int i=0; i < classNames.size(); i++)
      {
         String thisClass = (String)classNames.get(i);
         try
         {
            classes.add(Class.forName(thisClass));
         }
         catch (LinkageError le)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "Linkage error for class "+thisClass);
               Debug.printException(1, this, le);
            }
         }
         catch (ExceptionInInitializerError eiie)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "Failed to initialize "+thisClass);
               Debug.printException(1, this, eiie);
               Debug.printException(1, this, eiie.getException());
            }
         }
         catch (ClassNotFoundException cnfe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "Class not found: "+thisClass);
               Debug.printException(1, this, cnfe);
            }         
         }

      }
      return classes;
   }

   private Class[] getClassArray(ArrayList al)
   {
      Class[] c = new Class[al.size()];
      for (int i=0; i < al.size(); i++)
      {
         c[i] = (Class)al.get(i);
      }
      return c;
   }

   /**
    * Return a list of command objects that are invokable on your
    * service. These will be used to populate the drop down list for
    * right clicks over the service in the navigator.
    */
   public Class[] getCommandList()
   {
      if (m_alCommands == null)
      {
         ArrayList al = getClassList(SERVICE_COMMANDS);
         m_alCommands = getClassArray(al);
      }
      
      
      return m_alCommands;
   }
   
   /**
    * Get a list of commands that are invokable on service providers
    * contained in this service. It is assumed that the list of commands
    * is the same for all providers, but you can always disable commands
    * depending on the selected service.
    */
   public Class[] getProviderCommandList()
   {
      if (m_alProviderCommands == null)
      {
         ArrayList al = getClassList(PROVIDER_COMMANDS);
         m_alProviderCommands = getClassArray(al);
      }
      
      
      return m_alProviderCommands;   
   }
   
   /**
    * Get a list of commands that are invokable on folders contained
    * in service providers on this service.
    */
   public Class[] getFolderCommandList()
   {
      if (m_alFolderCommands == null)
      {
         ArrayList al = getClassList(FOLDER_COMMANDS);
         m_alFolderCommands = getClassArray(al);
      }
      
      
      return m_alFolderCommands;     
   }
   
   public Icon getIcon(String res)
   {
      return PropertyFileResolver.getIcon(getPreferences(), res);   
   }
   
   /**
    * Get the icon to display for this service.
    */
   public Icon getIcon()
   {
      if (m_icoService == null)
         m_icoService =  getIcon(ICON);
      
      return m_icoService;
   }
   
   /**
    * Get the icon to display for providers in this service.
    */
   public Icon getProviderIcon()
   {
      if (m_icoProvider == null)
         m_icoProvider = getIcon(PROVIDER_ICON);
      
      return m_icoProvider;
   }
   
   /**
    * Get the icon to display for folders in this service.
    */
   public Icon getFolderIcon()
   {
      if (m_icoFolder == null)
         m_icoFolder = getIcon(FOLDER_ICON);
      
      return m_icoFolder;
   }
   
   public String getDisplayedNodeName()
   {
      return getServiceNiceName();
   }
   
   public Icon getDisplayedNodeIcon()
   {
      return getIcon();
   }
 
   
}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/10/24 00:45:40  briand
// Added implementation.
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//