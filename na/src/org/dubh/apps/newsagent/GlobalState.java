// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: GlobalState.java,v 1.8 1999-11-09 22:34:40 briand Exp $
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

package org.javalobby.apps.newsagent;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.applet.Applet;

import org.javalobby.apps.newsagent.agent.AgentManager;
import org.javalobby.apps.newsagent.dialog.ErrorReporter;
import org.javalobby.apps.newsagent.dialog.main.MainFrame;
import org.javalobby.apps.newsagent.nntp.StorageManager;

import javax.swing.JApplet;
import javax.swing.UIManager;
import java.net.URL;
import org.javalobby.dju.misc.*;

/**
 * Describes the global state of the application
 * @author Brian Duff
 * @version $Id: GlobalState.java,v 1.8 1999-11-09 22:34:40 briand Exp $
 */
public class GlobalState {

// Public Static Constants

// Private Static Constants


// Private / Protected Attributes

  /** The user's properties */
   private static UserPreferences m_userprefs;
  private static Properties m_signatures;
  private static AgentManager m_agentManager;
   /** The name of the bundle containing application strings */
  public static final String stringBundle = "Strings";
  private static ResourceManager m_resManager = 
     new ResourceManager(ResourceBundle.getBundle(stringBundle));


   private static ReadOnlyVersion m_myVersion;

// Public Attributes
 
   /** Whether NewsAgent is currently in debug mode. This field is no longer
    *  in use, and should be ignored.*/
   public static boolean  debugOn = true;

  /** The name of this application. */
   public static  String appName;
   
   
  /** The version of the app */
  public static  String appVersion;
  
  
  /** The "long" version of this app */
  public static  String appVersionLong;
  
  
  /** The directory in which the application stores its data. This is
   * currently defined as $HOME/.appname/ (where appname is the lowercase
   * version of GlobalState.appName). E.g. ~bd/.newsagent/ or
   *  C:\progra~1\jbuilder\java\.newsagent\
   */
  public static String dataDir;
  
  /** The full pathname to the preferences file.
      Currently dataDir/appname.ini. (e.g. ~bd/.newsagent/properties)*/
  public static  String prefFile;
  
  /** The full pathname to the signatures file. */
  public static  String sigFile; 
  
  /** The full pathname to the servers file. */
  public static  String serversFile;
  
  /** The full pathname to the folders directory. */
  public static  String foldersDir;
  /** The full pathname to the servers directory */
  public static  String serversDir;
  /** The full pathname to the agents directory */
  public static  String agentDir;

  /** The name of the bundle containing application menus. */
  public static final String menuBundle = "Menus";
  /** The string to send for x-mailer headers. */
  public static String xmailer;
  /** The URL for NewsAgent. */
  public static final String appURL = "http://wired.st-and.ac.uk/~briand/newsagent";

  /** The main application window */
  private static MainFrame mainFrame;

  private static Locale m_locale;
  private static final String imgDir = "dubh/apps/newsagent/images/";

  /** Message Providers available to the user. */
  private static Vector m_providers = new Vector();

  /** The application StorageManager */
  private static StorageManager m_storageManager;

  /** The application HelpSystem */
  private static HelpSystem m_helpSystem;
    
    
   private static boolean m_isApplet = false;    
// Static Methods

   public static boolean isApplet()
   {
      return m_isApplet;
   }
   
   public static void setApplet(boolean b)
   {
      m_isApplet = b;
   }

   /**
    * Get the current version
    */
   public static ReadOnlyVersion getVersion()
   {
      return m_myVersion;
   }

  /**
   * Get the help system
   */
  public static HelpSystem getHelpSystem() {
     return m_helpSystem;
  }


  /**
   * Get the user preferences instance for this application. In
   * Applet mode, this is a session only user preference set,
   * which is initialised from the parameters passed into the
   * applet. 
   */
  public static UserPreferences getPreferences() {
        return m_userprefs;
  }



   public static void appInit()
   {
      appInit(null);
   }

  /**
   * Global Application initialisation. This <b>must</b> be called to initialise
   * the global state. The application may be terminated if the application
   * user directory doesn't exist and cannot be created for some reason.
   */
  public static void appInit(Applet a) {
      if (a != null) 
      {  
         setApplet(true);
      }
      m_myVersion = null;
      try
      {
         m_myVersion =
            VersionManager.getInstance().getBundleVersion("org.javalobby.apps.newsagent");
        appName = m_myVersion.getProductName();
        appVersion = m_myVersion.getShortDescription();
        appVersionLong = m_myVersion.getLongDescription();
      } catch (Throwable e) {
        appName="Unversioned NewsAgent";
        appVersion="X.X.X";
        appVersionLong="Unversioned (Early dev)";
      }

      if (!isApplet())
      {
      
         dataDir = System.getProperty("user.home") +
           System.getProperty("file.separator") +  "."+appName.toLowerCase();
         prefFile = dataDir + File.separator + "user.properties";
         sigFile = dataDir + File.separator + "signatures.properties";
         serversFile = dataDir + File.separator + "servers.dat";
      
        /** The full pathname to the folders directory. */
        foldersDir = dataDir + File.separator + "folders" + File.separator;
        /** The full pathname to the servers directory */
        serversDir = dataDir + File.separator + "servers";
        /** The full pathname to the agents directory */
        agentDir  = dataDir + File.separator + "agents";
     }
     xmailer = appName+" for Java Version "+appVersion;
     
     checkDataDir();
     if (isApplet())
        initPreferences(a);
     else
        initPreferences();
 //    initSignatures();
     m_agentManager = new AgentManager(); /* Init the agent manager */
     m_helpSystem   = new HelpSystem();   /* Init the help system */
     initStorage();
     initLookAndFeel();
     initUI();
     getStorageManager().deserializeCaches();
  }



  /**
   * Gets the agent manager
   */
  public static AgentManager getAgentManager() {
   return m_agentManager;
  }


  /**
   * Get the resource manager
   */
  public static ResourceManager getRes() {
     return m_resManager;
  }
    
  /**
   * Get the NewsAgent configuration directory root.
   */  
  public static String getDataDirectory()
  {
     return dataDir;
  }  

  /**
   * Set the application default Locale (Locale.setDefault() doesn't work).
   @param l a Locale to use for all calls to getResString
   @see getResString(String)
   */
  public static void setDefaultLocale(Locale l) {
   if (l == null) { m_locale = Locale.getDefault(); }
    else m_locale = l;
  }


  /**
   * Retrieves the application StorageManager.
   */
  public static StorageManager getStorageManager() {
     return m_storageManager;
  }

  /**
   * Retrieves the application's main window.
   */
  public static MainFrame getMainFrame() {
     return mainFrame;
  }

  /**
   * Creates the default user interface components and displays the main window.
   */
  private static void initUI() {
     mainFrame = new MainFrame();
     mainFrame.pack();
     mainFrame.setVisible(true);

  }

  /**
   * Initialises the StorageManager. The application will terminate (after
   * informing the user) if the storage directory doesn't exist, and can't be
   * created.
   */
  private static void initStorage() {
     if (!isApplet())
     {
        // Check the storage folder exists. If not, create it.
        File storageDir = new File(foldersDir);
        if (!storageDir.exists()) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, GlobalState.class,"Storage folder doesn't exist: Creating.");
           if (!storageDir.mkdir()) {
              ErrorReporter.fatality("CantCreateStorage", new String[] {foldersDir});
           }
        }
        File fserversDir = new File(serversDir);
        if (!fserversDir.exists()) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, GlobalState.class,"Servers folder doesn't exist: Creating.");
           if (!fserversDir.mkdir()) {
              ErrorReporter.fatality("CantCreateStorage", new String[] {serversDir});
           }
        }
     }
     
     m_storageManager = new StorageManager();

  }

  /**
   * Initialise the preferences. If no preferences file exists, create a new
   * prefs object and use defaults for everything. the preferences will be
   * stored on the next call to savePreferences.
   * If a preference file already exists, check it is up to date with the
   * current version of NewsAgent and upgrade it if necessary.
   */
  private static void initPreferences() {

     File pf = new File(prefFile);
    // setPreferences(new Properties());
     if (!pf.exists()) {
         Debug.println("Preference file doesn't exist. Using defaults.");
     }
     try {
        m_userprefs = new UserPreferences(pf, new Properties());
        if (pf.exists()) PreferenceKeys.convertPreferences(m_userprefs);
     } catch (IOException e) {
        ErrorReporter.error("CantReadProps");
     }
  }
  
  /**
   * Initialise preferences from the parameters to an applet
   */
  private static void initPreferences(Applet applet)
  {
     m_userprefs = new AppletPreferences(applet);
  }

  /**
   * Checks to make sure the dataDir exists, and creates it if not.
   * Program <b>will terminate</b> if the dataDir cannot be found or created.
   */
  private static void checkDataDir() 
  {
      if (!isApplet())
      {
         File ddir = new File(dataDir);
         if (!ddir.exists()) 
         {
            if (!ddir.mkdir()) 
            {
               ErrorReporter.fatality("CantCreateDir",
               new String[] { dataDir, GlobalState.appName });
            }
            if (Debug.TRACE_LEVEL_1) Debug.println(1, GlobalState.class,"Created a new data directory: "+dataDir);
         }
      }
   }

  /**
   * Initialises the Swing User Interface
   */
  private static void initLookAndFeel() 
  {
     String laf="";
     try
     {
        laf = getPreferences().getPreference(PreferenceKeys.UI_LOOKANDFEEL);
        if (laf == null) 
           laf = UIManager.getSystemLookAndFeelClassName();   
        
        UIManager.setLookAndFeel(laf);
        
     }
     catch (Exception e)
     {
        if (Debug.TRACE_LEVEL_2)
        {
           Debug.println(2, GlobalState.class, "Unable to set look and feel to "+laf);
           Debug.printException(2, GlobalState.class, e);
        }
     } 

  }

}

//
// Old Log:
// 0.1 [03/03/98]: Initial Revision
// 0.2 [05/03/98]: Added getResource, getResString
// 0.3 [06/03/98]: Added signatures preferences
// 0.4 [07/03/98]: Added getImage
// 0.5 [22/03/98]: Added support for StorageManager
// 0.6 [23/03/98]: Added support for getMainFrame()
// 0.7 [31/03/98]: Included initialisation for servers directory [initStorage]
// 0.8 [04/04/98]: Added xmailer, appURL.
// 0.9 [14/04/98]: Added agentDir and agents initialisation, getAgentManager
// 0.10 [23/04/98]: Removed some defunct methods (for final release)
// 0.11 [29/04/98]: Added debug support
// 0.12 [08/05/98]: Added getResStringBasic, getResString with subst.
// 0.13 [08/06/98]: Sent the main window to the back when it is first
// created so that the splash screen stays on top. Changed the way debugging
// works slightly. Added support for help.
// 0.14 [10/06/98]: Removed the final remnants of debuggging support from
// this class (it's all in NewsAgent.java now)
// 0.15 [13/06/98]: Changed to use the UserPreferences dubh utility class
// for preferences, and deprecated all old preference methods. Added
// getPreferences().
// 0.16 [30/06/98]: Changed to use the new ResourceManager dubh utility
// class for bundle resources, and deprecated old resource methods. Added
// getRes()
// 0.17 [23/11/98]: Added support for new VersionManager dubh utility for
// program information.
//
//
// New (CVS) Log:
//
//
// $Log: not supported by cvs2svn $
// Revision 1.7  1999/10/24 00:41:30  briand
// Add static getDataDirectory() method.
//
// Revision 1.6  1999/06/01 00:30:25  briand
// Removed all static methods that get resources of various types.
// These were delegating to the ResourceManager. The preferred
// way to get resources now is to call GlobalState.getRes() which
// returns a ResourceManager. This was a major interface change
// with impact on almost every file in NewsAgent.
//
// Revision 1.5  1999/03/23 01:29:59  briand
// Fix to comments (checking autobuild works).
//
//
// Revision 1.4  1999/3/22 23:47:56  briand
// Ill fated attempt to appletize.
//
// Revision 1.3  1999/3/13 1:26:42  briand
// Removed some commented out old code for restoring mainframe location.
//
// Revision 1.2  1999/3/7 22:56:25  briand
// Changed to use bundled version rather than serialised. Added
// support for look and feel user preference. N.b. This changes the
// default look and feel to the system look and feel if the user
// doesn't have a preference set (i.e. Windows LAF will now be the
// default under Windows).
//
// Revision 1.1 1999/3/1 23:57:45  briand
// Initial revision