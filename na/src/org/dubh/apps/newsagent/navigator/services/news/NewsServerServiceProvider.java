// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsServerServiceProvider.java,v 1.1 1999-10-24 00:47:57 briand Exp $
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
package dubh.apps.newsagent.navigator.services.news;

import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.MessagingException;

import javax.swing.Icon;

import java.util.Properties;
import java.util.MissingResourceException;

import java.io.File;

import dubh.utils.misc.UserPreferences;
import dubh.utils.misc.ResourceManager;

import dubh.apps.newsagent.navigator.NavigatorServiceProvider;
import dubh.apps.newsagent.navigator.NavigatorService;
import dubh.apps.newsagent.navigator.PropertyFileResolver;
/**
 * This represents a news server in the navigator 
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NewsServerServiceProvider.java,v 1.1 1999-10-24 00:47:57 briand Exp $
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

   private final static String RES = "dubh.apps.newsagent.navigator.services.news.res.Command";

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
//