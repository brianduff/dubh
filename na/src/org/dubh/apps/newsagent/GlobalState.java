// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: GlobalState.java,v 1.13 2001-02-11 15:42:30 briand Exp $
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


package org.dubh.apps.newsagent;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.applet.Applet;
import javax.swing.JApplet;
import javax.swing.UIManager;
import java.net.URL;

import org.dubh.apps.newsagent.agent.AgentManager;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import org.dubh.apps.newsagent.dialog.main.MainFrame;
import org.dubh.apps.newsagent.nntp.StorageManager;

import org.dubh.dju.misc.*;

import org.dubh.dju.diagnostic.Assert;
import org.dubh.dju.version.ComponentInfo;
import org.dubh.dju.version.ComponentInfoFactory;


/**
 * Describes the global state of the application
 * @author Brian Duff
 * @version $Id: GlobalState.java,v 1.13 2001-02-11 15:42:30 briand Exp $
 */
public class GlobalState
{

   /**
    * Version information about the application
    */
   private static ComponentInfo m_appComponent;

   /**
    * Version information about our dependencies
    */
   private static ComponentInfo[] m_depComponents;

   /** The user's properties */
   private static UserPreferences m_userprefs;
   private static Properties m_signatures;
   /** The name of the bundle containing application strings */
   private static final String stringBundle = "org/dubh/apps/newsagent/res/Strings";
   private static ResourceManager m_resManager =
     new ResourceManager(ResourceBundle.getBundle(stringBundle));



// Public Attributes (THERE SHOULD BE NONE OF THESE)



  /**
   * The directory in which the application stores its data.
   */
  private static String dataDir;
  private static  String prefFile;

  /** The name of the bundle containing application menus. */
  private static final String menuBundle = "org/dubh/apps/newsagent/res/Menus";



  /** The main application window */
  private static MainFrame mainFrame;

  private static Locale m_locale;
  private static final String imgDir = "org/dubh/apps/newsagent/images/";

  /** Message Providers available to the user. */
  private static Vector m_providers = new Vector();

  /** The application HelpSystem */
  private static HelpSystem m_helpSystem;


  /**
   * Get the help system
   */
  public static HelpSystem getHelpSystem()
  {
     return m_helpSystem;
  }


  /**
   * Get the user preferences instance for this application.
   */
  public static UserPreferences getPreferences()
  {
        return m_userprefs;
  }


   /**
   * Global Application initialisation. This <b>must</b> be called to initialise
   * the global state. The application may be terminated if the application
   * user directory doesn't exist and cannot be created for some reason.
   */
   public static void appInit()
   {

      Package p = Package.getPackage("org.dubh.apps.newsagent");

      m_appComponent = ComponentInfoFactory.getComponentInfo(p);


      dataDir = System.getProperty("user.home") +
        System.getProperty("file.separator") +  "."+m_appComponent.getName().toLowerCase();
      prefFile = dataDir + File.separator + "user.properties";



      checkDataDir();
      initPreferences();
      
      m_helpSystem   = new HelpSystem();   /* Init the help system */
      initLookAndFeel();
      initUI();
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
     return null;
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

         File ddir = new File(dataDir);
         if (!ddir.exists())
         {
            if (!ddir.mkdir())
            {
               ErrorReporter.fatality("CantCreateDir",
               new String[] { dataDir, m_appComponent.getName() });
            }
            if (Debug.TRACE_LEVEL_1) Debug.println(1, GlobalState.class,"Created a new data directory: "+dataDir);
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

   /**
    * Get the name of the resource containing menus for the application
    */
   public static String getMenuRes()
   {
      return menuBundle;
   }

   /**
    * Get the name of the resource containing strings for the application
    */
   public static String getStringRes()
   {
      return stringBundle;
   }

   /**
    * This is the recommended way (for now) of getting information about the
    * version of NewsAgent that is running.
    *
    * @return a DJU ComponentInfo object for the application.
    */
   public static ComponentInfo getApplicationInfo()
   {
      if (Assert.ENABLED)
      {
         Assert.that(
            (m_appComponent != null),
            "Attempt to get application info before appInit()"
         );
      }

      return m_appComponent;
   }

   /**
    * Get a list of component infos on which this application depends.
    *
    * @return an array of ComponentInfo objects.
    */
   public static ComponentInfo[] getDependencyInfo()
   {
      if (m_depComponents == null)
      {
         // Evil hack. This will force the classloader to get stuff from JavaMail
         // so that we can get info about its packages.
         ClassLoader cl = ClassLoader.getSystemClassLoader();
         try
         {
            cl.loadClass("javax.mail.Folder");
            cl.loadClass("javax.activation.ActivationDataFlavor");
         }
         catch (Throwable t)
         {
            System.err.println(
               "Couldn't find javax.mail.Folder. Check the classpath"
            );
         }

         m_depComponents = new ComponentInfo[] {
            ComponentInfoFactory.getComponentInfo(Package.getPackage(
               "org.dubh.dju"
            )),
            ComponentInfoFactory.getComponentInfo(Package.getPackage(
               "java.lang"
            )),
            ComponentInfoFactory.getComponentInfo(Package.getPackage(
               "javax.mail"
            )),
            ComponentInfoFactory.getComponentInfo(Package.getPackage(
               "javax.activation"
            ))
         };
      }
      return m_depComponents;
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
// Revision 1.12  2001/02/11 02:50:58  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.11  2000/08/19 21:20:39  briand
// Use Java 2 JAR versioning.
//
// Revision 1.10  1999/12/13 22:32:43  briand
// Move to Javalobby changed the paths to various resources. Added fixes to that
// most things work again. Also patched the PropertyFileResolver to create parent
// directories properly. Managed to get NewsAgent to run with the brand new JRE
// 1.2.2 for Linux!!
//
// Revision 1.9  1999/12/12 03:31:51  briand
// More bugfixes necessary due to move to javalobby. Mostly changing path from
// dubh.apps.newsagent to org.dubh.apps.newsagent etc. and new locations of
// top level properties files.
//
// Revision 1.8  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
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