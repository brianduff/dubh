/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.apps.newsagent;

import java.util.*;
import java.io.*;
import java.awt.*;

import dubh.apps.newsagent.agent.AgentManager;
import dubh.apps.newsagent.dialog.ErrorReporter;
import dubh.apps.newsagent.dialog.main.MainFrame;
import dubh.apps.newsagent.nntp.StorageManager;


import javax.swing.UIManager;
import java.net.URL;
import dubh.utils.misc.*;

/**
 * Describes the global state of the application
 * Version History: <UL>
 * <LI>0.1 [03/03/98]: Initial Revision
 * <LI>0.2 [05/03/98]: Added getResource, getResString
 * <LI>0.3 [06/03/98]: Added signatures preferences
 * <LI>0.4 [07/03/98]: Added getImage
 * <LI>0.5 [22/03/98]: Added support for StorageManager
 * <LI>0.6 [23/03/98]: Added support for getMainFrame()
 * <LI>0.7 [31/03/98]: Included initialisation for servers directory [initStorage]
 * <LI>0.8 [04/04/98]: Added xmailer, appURL.
 * <LI>0.9 [14/04/98]: Added agentDir and agents initialisation, getAgentManager
 * <LI>0.10 [23/04/98]: Removed some defunct methods (for final release)
 * <LI>0.11 [29/04/98]: Added debug support
 * <LI>0.12 [08/05/98]: Added getResStringBasic, getResString with subst.
 * <LI>0.13 [08/06/98]: Sent the main window to the back when it is first
 *   created so that the splash screen stays on top. Changed the way debugging
 *   works slightly. Added support for help.
 * <LI>0.14 [10/06/98]: Removed the final remnants of debuggging support from
 *   this class (it's all in NewsAgent.java now)
 * <LI>0.15 [13/06/98]: Changed to use the UserPreferences dubh utility class
 *   for preferences, and deprecated all old preference methods. Added
 *   getPreferences().
 * <LI>0.16 [30/06/98]: Changed to use the new ResourceManager dubh utility
 *   class for bundle resources, and deprecated old resource methods. Added
 *   getRes()
 * <LI>0.17 [23/11/98]: Added support for new VersionManager dubh utility for
 *   program information.
 *</UL>
 * @author Brian Duff
 * @version 1.3
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
  public static final String appURL = "http://st-and.compsoc.org.uk/~briand/newsagent";

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

// Static Methods

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
   * Return a user preference.
   @param prefname the name of the preference
   @returns a string containing the preference
   @deprecated use getPreferences().getPreference(prefname) instead.
   */
   public static String getPreference(String prefname) {
   return getPreferences().getPreference(prefname);
  }

  /**
   * Return a user preference.
   @param prefname the name of the preference
   @param defaultValue the default value if the preference doesn't exist
   @returns a string containing the preference
   @deprecated use getPreferences().getPreference(prefname, default) instead.
   */
  public static String getPreference(String prefname, String defaultValue) {
   return getPreferences().getPreference(prefname, defaultValue);
  }

  /**
   * Return a boolean user preference.
   @param prefname the name of the preference.
   @returns a boolean value
   @deprecated use getPreferences().getBoolPreference(prefName) instead.
   */
  public static boolean getBoolPreference(String prefname) {
   return getPreferences().getBoolPreference(prefname);
   }

  /**
   * Return a boolean user preference.
   @param prefname the name of the preference.
   @param defVal the default value if the preference doesn't exist
   @returns a boolean value
   @deprecated use getPreferences().getBoolPreference(prefName) instead.
   */
  public static boolean getBoolPreference(String prefname, boolean defVal) {
   return getPreferences().getBoolPreference(prefname, defVal);
  }

  /**
   * Change or set a user preference.
   @param prefname the name of the preference.
   @param value the value to set
   @deprecated use getPreferences().setPreference(prefname, value) instead.
   */
  public static void setPreference(String prefname, String value) {
   getPreferences().setPreference(prefname, value);
  }

  /**
   * Change or set a boolean user preference.
   @param prefname the name of the preference.
   @param value the value to set
   @deprecated use getPreferences().setBoolPreference() instead.
   */
  public static void setPreference(String prefname, boolean value) {
   getPreferences().setBoolPreference(prefname, value);
  }

  /**
   * Sets the properties object from which user preferences are taken.
   @param p a Properties object contiaining user preferences.
   @deprecated since newsagent 1.02
   */
  private static void setPreferences(Properties p) {
   
  }

  /**
   * Get the user preferences instance for this application
   */
  public static UserPreferences getPreferences() {
     return m_userprefs;
  }

  /**
   * Saves the user's preferences. Will display an error dialog if the prefs
   * file could not be opened for writing.
   @returns false if the preferences couldn't be saved. The user will already
      have been notified.
   @deprecated use getPreferences().save() instead.
   */
  public static boolean savePreferences() {
    // FileOutputStream out;
   //  String header = appName + " version " + appVersion + " properties.";
     try {
        getPreferences().save();
        return true;
     } catch (IOException e) {
         ErrorReporter.error("CantWriteProps");
        Debug.println(e.toString());
        return false;
     }
  }

  /**
   * Returns the Properties object containing the user's collection of message
   * signatures.
   @returns a Properties object containing a collection of signatures.
   */
  public static Properties getSignatures() {
   return m_signatures;
  }

  /**
   * Sets the Properties object which contains the user's collection of message
   * signatures
   @param sigs A Properties object containing a collection of signatures
   */
  public static void setSignatures(Properties sigs) {
   m_signatures = sigs;
  }

  /**
   * Saves the user's collection of signatures to disk. Will display an error
   * dialog if the signatures couldn't be written for some reason.
   @returns true if the signatures were successfully saved
   */
  public static boolean saveSignatures() {
    FileOutputStream out;
    String header = appName + " version " + appVersion + " signatures.";
    
   try {
      out = new FileOutputStream(sigFile);
      m_signatures.save(out, header);
      out.close();
      return true;
    } catch (IOException e) {
      ErrorReporter.error("CantWriteSigs");
      ErrorReporter.debug(e.toString());
      return false;
    }
  }



  /**
   * Global Application initialisation. This <b>must</b> be called to initialise
   * the global state. The application may be terminated if the application
   * user directory doesn't exist and cannot be created for some reason.
   */
  public static void appInit() {
      m_myVersion = null;
      try
      {
         m_myVersion =
            VersionManager.getInstance().getBundleVersion("dubh.apps.newsagent");
        appName = m_myVersion.getProductName();
        appVersion = m_myVersion.getShortDescription();
        appVersionLong = m_myVersion.getLongDescription();
      } catch (IllegalArgumentException e) {
        //
        //ErrorReporter.error("errorVersion");
        // System.exit(1);
        //
        appName="Unversioned NewsAgent";
        appVersion="X.X.X";
        appVersionLong="Unversioned (Early dev)";
      }

      
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
     
     xmailer = appName+" for Java Version "+appVersion;
     
     checkDataDir();
     initPreferences();
     initSignatures();
     m_agentManager = new AgentManager(); /* Init the agent manager */
     m_helpSystem   = new HelpSystem();   /* Init the help system */
     initStorage();
     initLookAndFeel();
     initUI();
     getStorageManager().deserializeCaches();
  }

  /**
   * Global Application Initialisation. Use this version to specify debug mode
   * on or off. If you specify true, debugging will be turned on, otherwise
   * debugging will only be switched on if the user specified the
   * newsagent.debug.DebugMessages user preference as yes.
   @param on Whether to show debug messages
   @deprecated This method should not be used: Debugging is now controlled from
        the main NewsAgent class.
   */
  public static void appInit(boolean on) {
   appInit();
  }


  /**
   * Gets the agent manager
   */
  public static AgentManager getAgentManager() {
   return m_agentManager;
  }

  /**
   * Gets a "resource" from the classpath (usually inside the application JAR
   * file). Useful for reading in images which are stored in the JAR file, for
   * example.
   @param key The name of the resource to fetch. can include a directory.
   @returns a URL object corresponding to the resource, or null if the resource
   doesn't exist.
   @deprecated since NewsAgent 1.02, move to the new ResourceManager class
  */
  public static URL getResource(String key) {
       URL url = ClassLoader.getSystemResource(key);
       return url;
  }

  /**
   * Gets an image resource from the classpath.
   @param key the filename of the image to load.
   @returns a URL object corresponding to that image, or null
   @deprecated since NewsAgent 1.02, move to the new ResourceManager class

   */
  public static URL getImage(String key) {
   return getResource(imgDir+key);
  }

  /**
   * Get the resource manager
   */
  public static ResourceManager getRes() {
     return m_resManager;
  }

  /**
   * Gets a string from the application wide Strings bundle. This allows
   * internationalisation.
   @param key The name of the string to fetch.
   @returns the value of the string from the bundle, or the key if there was
   a problem.
   @deprecated since version 1.02, use getRes().getString(key) instead.
  */
  public static String getResString(String key) {
   //ResourceBundle b = ResourceBundle.getBundle(stringBundle, Locale.FRENCH);
   // ResourceBundle b = ResourceBundle.getBundle(stringBundle);
    try {
      return getRes().getString(key);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  /**
   * Gets a string from the application wide Strings bundle. This allows
   * internationalisation. This version of the function substitutes text
   * into the message.
   @param key The name of the string to fetch.
   @param subst An array of values to substitute into the string
   @returns the value of the string from the bundle, or the key if there was
   a problem.
  */
  public static String getResString(String key, Object[] subst) {
     return java.text.MessageFormat.format(getResString(key), subst);
  }

  /**
   * Gets the resource string directly: doesn't catch any exceptions.
   @param key The key to look up
   @returns the value of the string from the internationalised bundle.
   @throws MissingResourceException if the key is missing
   */
  public static String getResStringBasic(String key) throws MissingResourceException {
   // ResourceBundle b = ResourceBundle.getBundle(stringBundle);
     return m_resManager.getString(key);
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
     //mainFrame.showAtStoredLocation("main");
     // Send to the back (i.e. behind the splash screen)
     //mainFrame.toBack();
  }

  /**
   * Initialises the StorageManager. The application will terminate (after
   * informing the user) if the storage directory doesn't exist, and can't be
   * created.
   */
  private static void initStorage() {
     // Check the storage folder exists. If not, create it.
     File storageDir = new File(foldersDir);
     if (!storageDir.exists()) {
        ErrorReporter.debug("Storage folder doesn't exist: Creating.");
        if (!storageDir.mkdir()) {
           ErrorReporter.fatality("CantCreateStorage", new String[] {foldersDir});
        }
     }
     File fserversDir = new File(serversDir);
     if (!fserversDir.exists()) {
        ErrorReporter.debug("Servers folder doesn't exist: Creating.");
        if (!fserversDir.mkdir()) {
           ErrorReporter.fatality("CantCreateStorage", new String[] {serversDir});
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
   * Initialise the signatures preferences. If no signatures file exists, create
   * a new properties object which is initially empty.
   */
  private static void initSignatures() {
   File test = new File(sigFile);
    m_signatures = new Properties();
    if(test.exists()) {
      try {
         m_signatures.load(new FileInputStream(sigFile));
      } catch (IOException e) {
         ErrorReporter.error("CantReadSigs");
      }
    }
  }

  /**
   * Checks to make sure the dataDir exists, and creates it if not.
   * Program <b>will terminate</b> if the dataDir cannot be found or created.
   */
  private static void checkDataDir() {
   File ddir = new File(dataDir);
    if (!ddir.exists()) {
      if (!ddir.mkdir()) {
         ErrorReporter.fatality("CantCreateDir",
          new String[] { dataDir, GlobalState.appName });
      }
      ErrorReporter.debug("Created a new data directory: "+dataDir);
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