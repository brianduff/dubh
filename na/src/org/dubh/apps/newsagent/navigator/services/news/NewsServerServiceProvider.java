// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsServerServiceProvider.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
package org.javalobby.apps.newsagent.navigator.services.news;

import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.MessagingException;

import javax.swing.Icon;

import java.util.Properties;
import java.util.MissingResourceException;

import java.io.File;

import org.javalobby.dju.misc.UserPreferences;
import org.javalobby.dju.misc.ResourceManager;

import org.javalobby.apps.newsagent.navigator.NavigatorServiceProvider;
import org.javalobby.apps.newsagent.navigator.NavigatorService;
import org.javalobby.apps.newsagent.navigator.PropertyFileResolver;
/**
 * This represents a news server in the navigator 
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NewsServerServiceProvider.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class NewsServerServiceProvider implements NavigatorServiceProvider 
{ 
   protected UserPreferences m_properties = null;
   protected String m_strName;
   protected NavigatorService m_nsParent;
   protected final static String STORAGE_PATH = "navigator"+File.separator;
   protected Store m_stoServer = null;
   protected Folder m_folRoot = null;
   
   protected final static String 
      PREF_USERNAME = "username",
      PREF_PASSWORD = "password",
      PREF_NICENAME = "nicename",
      PREF_PORT     = "port";

   private final static String RES = "org.javalobby.apps.newsagent.navigator.services.news.res.Command";

   /**
    * Get internationalized resources for News
    */
   public static ResourceManager getResourceManager()
   {
      try
      {
         return ResourceManager.getManagerFor(RES);
      }
      catch (MissingResourceException mre)
      {
         throw new IllegalStateException(
            "Resource "+RES+" is missing."
         );
      }
   }


   /**
    * Get the user preferences for this provider. 
    */
   public UserPreferences getPreferences()
   {
      if (m_properties == null)
      {
         m_properties = PropertyFileResolver.getPreferences(
            STORAGE_PATH+m_nsParent.getServiceProtocolName()+"."+getName()+".properties"
         );
      }
      
      return m_properties;
   }
  
  
   /**
    * Get the root folder, which is assumed to contain all other
    * folders on the service. The root folder is usually not displayed.
    */
   public Folder getRootFolder()
      throws MessagingException
   {
      
      // Connect if necessary
      
      if (m_stoServer != null)
      {
         if (!m_stoServer.isConnected())
         {
            connect();
            m_folRoot = null;
         }
      }
      else
      {
         Properties props = System.getProperties();
         Session session = Session.getDefaultInstance(props, null);
         m_stoServer = session.getStore(m_nsParent.getServiceProtocolName());  
         connect();       
      }
      
      // Get the root folder if necessary
      
      if (m_folRoot == null)
      {
         m_folRoot = m_stoServer.getDefaultFolder();
      }   
      
      return m_folRoot;
   
   }

   /**
    * Set the internal name of the service. This will be used by
    * the NavigatorService to set your service instance's name
    * when the service provider is instantiated.
    */
   public void setName(String name)
   {
      m_strName = name;
   }
   
   
   public void setService(NavigatorService service)
   {
      m_nsParent = service;
   }
   
   public NavigatorService getService()
   {
      return m_nsParent;
   }

   public Class[] getCommandList()
   {
      return getService().getProviderCommandList();
   }
   
   public String getDisplayedNodeName()
   {
      return getNiceName();
   }
   
   public Icon getDisplayedNodeIcon()
   {
      return getService().getProviderIcon();
   }

   /**
    * Get the (internal) name of the service.
    */
   public String getName()
   {
      return m_strName;
   }
   
   
   public void connect()
      throws MessagingException
   {
      m_stoServer.connect(getName(), getPort(), getUserName(), getPassword());      
   }
   
   public void close()
      throws MessagingException
   {
      m_stoServer.close();
   }
   
   /**
    * Get the nice name of the service as it will be displayed to the user.
    * This can be the same as the internal name if you like.
    */
   public String getNiceName()
   {
      return getPreferences().getPreference(PREF_NICENAME);
   } 
   
   public String getUserName()
   {
      return getPreferences().getPreference(PREF_USERNAME, null);      
   }

   public String getPassword()
   {
      return getPreferences().getPreference(PREF_PASSWORD, null);   
   }
   
   public int getPort()
   {
      return getPreferences().getIntPreference(PREF_PORT, 119);   
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/24 00:47:57  briand
// Initial revision.
//
//