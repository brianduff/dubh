// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NewsServerServiceProvider.java,v 1.4 2001-02-11 02:51:01 briand Exp $
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

package org.dubh.apps.newsagent.navigator.services.news;

import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.MessagingException;

import javax.swing.Icon;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.MissingResourceException;

import java.io.File;
import java.io.IOException;

import org.dubh.dju.misc.UserPreferences;
import org.dubh.dju.misc.ResourceManager;
import org.dubh.dju.diagnostic.Assert;

import org.dubh.apps.newsagent.GlobalState;

import org.dubh.apps.newsagent.navigator.NavigatorServiceProvider;
import org.dubh.apps.newsagent.navigator.NavigatorService;
import org.dubh.apps.newsagent.navigator.PropertyFileResolver;

// By doing this, we break the ability to switch in a different javamail
// nntp provider. I'm not too bothered about this, and at any rate, javamail's
// interface is not rich enough to let us get all the information we need about
// the server.
// Nevertheless, try to use methods on Store rather than NewsStore, this
// makes it easier for NewsAgent code to branch and use other javamail providers.
import org.dubh.javamail.news.NewsStore;

/**
 * This represents a news server in the navigator
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NewsServerServiceProvider.java,v 1.4 2001-02-11 02:51:01 briand Exp $
 */
public class NewsServerServiceProvider extends NavigatorServiceProvider
{
   protected UserPreferences m_properties = null;
   protected String m_strName;
   protected final static String STORAGE_PATH = "navigator"+File.separator+"services"+File.separator+"news";
   protected Store m_stoServer = null;
   protected Folder m_folRoot = null;

   protected final static String
      PREF_USERNAME = "username",
      PREF_PASSWORD = "password",
      PREF_NICENAME = "nicename",
      PREF_PORT     = "port",
      PREF_HOST     = "host",
      PREF_GROUP_UPDATE = "groupupdate";

   private final static String RES = "org.dubh.apps.newsagent.navigator.services.news.res.Command";

   /**
    * Default constructor
    */
   public NewsServerServiceProvider()
   {

   }



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
    * Get the JavaMail store for this server.
    */
   public Store getStore()
   {
      // TODO

      return m_stoServer;
   }

   /**
    * Convenience method that gets all newsgroups from the root folder. Folders
    * in the context of news are NewsGroups.
    */
   public Folder[] getAllFolders()
      throws MessagingException
   {
      return getRootFolder().list();
   }


   /**
    * Get the user preferences for this provider.
    */
   public UserPreferences getPreferences()
   {
      if (m_properties == null)
      {
         String prefsPath = STORAGE_PATH+getService().getServiceProtocolName()+
            "."+getName()+".properties";

         m_properties = PropertyFileResolver.getPreferences(prefsPath);

         if (Assert.ENABLED)
         {
            Assert.that((m_properties != null),
               "Failed to find News Service Provider prefs at "+prefsPath
            );
         }
      }

      return m_properties;
   }


   /**
    * Get the root folder, which is assumed to contain all other
    * folders on the service. The root folder is usually not displayed.
    * This command will cause a connection to be opened to the host.
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
         m_stoServer = session.getStore(getService().getServiceProtocolName());
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

   public NavigatorService getService()
   {
      return (NavigatorService)getParent();
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

   public String toString()
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

   /**
    * Set the nice name of the service.
    */
   public void setNiceName(String niceName)
   {
      getPreferences().setPreference(PREF_NICENAME, niceName);
   }

   /**
    * Get the username that is used to connect to the news server.
    * This is null if none has been specified
    */
   public String getUserName()
   {
      return getPreferences().getPreference(PREF_USERNAME, null);
   }

   /**
    * Set the username that is used to connect to the news server.
    */
   public void setUserName(String userName)
   {
      getPreferences().setPreference(PREF_USERNAME, userName);
   }

   /**
    * Get the password used to connect to the server
    * This is null if none has been specified
    */
   public String getPassword()
   {
      return getPreferences().getPreference(PREF_PASSWORD, null);
   }

   /**
    * Set the password that is used to connect to the server
    */
   public void setPassword(String password)
   {
      getPreferences().setPreference(PREF_PASSWORD, password);
   }

   /**
    * Get the port number that the news server is on. Returns
    * the default (119) if none has been set.
    */
   public int getPort()
   {
      return getPreferences().getIntPreference(PREF_PORT, 119);
   }

   /**
    * Set the port number that the news server is on.
    */
   public void setPort(int portNum)
   {
      getPreferences().setIntPreference(PREF_PORT, 119);
   }

   /**
    * Get the host name that the server is on. This is just
    * the (internal) name of this service provider if none has been
    * set.
    */
   public String getHost()
   {
      String host = getPreferences().getPreference(PREF_HOST);
      if (host == null)
      {
         return getName();
      }
      return host;
   }

   /**
    * Set the hostname to connect to.
    */
   public void setHost(String hostName)
   {
      getPreferences().setPreference(PREF_HOST, hostName);
      if (getName() == null)
      {
         setName(hostName);
      }
   }

   /**
    * Save the preferences stored in this object.
    */
   public void save() throws IOException
   {
      getPreferences().save();
   }

   /**
    * The service provider should delete its physical sroage
    */
   public void delete() throws IOException
   {
      getPreferences().delete();
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.3  2000/06/14 21:36:46  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:47:57  briand
// Initial revision.
//
//