// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
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

package dubh.apps.newsagent;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import dubh.apps.newsagent.nntp.NNTPServer;
import dubh.utils.misc.*;
import dubh.utils.ui.*;

/**
 * NewsAgent main program
 * Version History: <UL>
 * <LI>0.1 [20/04/98]: Initial Revision
 * <LI>0.2 [29/04/98]: Added -debug flag support
 * <LI>0.3 [08/05/98]: Whoops. Now checking for number of arguments. Ahem.
 * <LI>0.4 [08/06/98]: Added splash screen support. Added console debugging
 *   support. Nb. the debug user preference has been retired: the command
 *   line flags should be used instead. (?? is this evil for Mac users?)
 * <LI>0.5 [09/06/98]: Added network debugging console support (-debugnet
 *   command line option). Removed -debugcon and added -console.
 *</UL>
 @author Brian Duff
 @version 0.5 [09/06/98]
 */
 public class NewsAgent {

   public  static final String DEBUG_FLAG = "-debug";
   public  static final String CONSOLE_FLAG = "-console";
   public  static final String DEBUG_NET_FLAG = "-debugnet";
   private  static final String DEBUG_PREFIX = "NewsAgent Debug: ";
   private  static final String DEBUG_CONSOLE = "NewsAgent Debug Console";   
   private static final String DEBUG_NETID = "debugnet";
   private static final String DEBUG_NET = "NewsAgent Network Debug Console";
   private static final String SPLASH_IMAGE = "dubh/apps/newsagent/images/splash.gif";

   /** User preference that sets debugging on (equivalent to -debug) */
   public static final String DEBUG_PREFERENCE = PreferenceKeys.DEBUG_DEBUGMESSAGES;

   /** User preference that sets network debugging on (-debugnet) */
   public static final String DEBUGNET_PREFERENCE = PreferenceKeys.DEBUG_SERVERDUMP;

   /** User preference that causes debugging output (from -debug and -debugnet)
       to go to a windowed console rather than stderr  (-console)*/
   public static final String CONSOLE_PREFERENCE = PreferenceKeys.DEBUG_CONSOLE;
   

   /** Whether the user specified -debug on the command line */
   public static boolean flagDebug;
   /** Whether the user specified -console on the command line */
   public static boolean flagConsole;
   /** Whether the user specified -debugnet on the command line */
   public static boolean flagDebugNet;
   /** Whether net debugging was enabled from the command line or from prefs */
   private static boolean netDebugFromPrefs;
   private static boolean debugFromPrefs;
   
   /** The output stream for network debugging. */
   public static PrintWriter pwDebugNet;

   private static Frame frmTemp;
   private static Window winSplash;
   
   private static DebugFrame m_netDebug;

  public NewsAgent() {
  }

  public static void init() {
 /*
     * Set up debugging before we do appInit(), in case there are any debug
     * messages during application initialisation
     */
    setupDebugging();
    if (flagDebug)
        Debug.println("Debugging output was enabled with the "+DEBUG_FLAG+" command line flag.");

    GlobalState.appInit();

    checkDebugPreferences();
    if (debugFromPrefs) setupDebugging();

    if (flagDebugNet) {
     setupNetDebugging();
     if (netDebugFromPrefs)
        NNTPServer.debugStream.println("Network debugging output was enabled with the "+DEBUGNET_PREFERENCE+" user preference.");
     else
        NNTPServer.debugStream.println("Network debugging output was enabled with the "+DEBUG_NET_FLAG+" command line flag.");
    }
    
    if (debugFromPrefs)
    {
        Debug.println("Debugging output was enabled with the "+DEBUG_PREFERENCE+" user preference.");
    }

  }

  public static void main(String[] args) throws Exception {

    NewsAgent newsAgent = new NewsAgent();
     try {
        displaySplashScreen();
        checkCommandLine(args);
        newsAgent.invokedStandalone = true;
        init();
        hideSplashScreen();
     } catch (ExceptionInInitializerError e) {
        if (Debug.TRACE_LEVEL_1)
        {
           Debug.println(1, NewsAgent.class, "Exception during initialisation.");
           Debug.printException(1, NewsAgent.class, e.getException());
        }
     }
  }

  /**
   * Set up (ordinary) debugging, depending on command line options. Call
   * checkCommandLine() first.
   */
  private static void setupDebugging() {
     Debug.setEnabled(flagDebug);
     Debug.setPrefix(DEBUG_PREFIX);
     if (flagConsole && flagDebug) {
        Debug.useWindow(DEBUG_CONSOLE);
     }

  }

   public static void closeNetDebug()
   {
      if (m_netDebug != null)
      {
         m_netDebug.setVisible(false);
      }
   }

  /**
   * Set up networking debugging
   */
  private static void setupNetDebugging() {
     if (flagDebugNet) {
        if (flagConsole) {
           /*
            * Create a new Auto-flushing printwriter with the output frame
            */
           m_netDebug = new DebugFrame(DEBUG_NETID, DEBUG_NET);
           m_netDebug.setControlsVisible(false);
           NNTPServer.debugStream = new PrintWriter(m_netDebug.getStream(), true);
           m_netDebug.pack();
           m_netDebug.setVisible(true);
        } else
           NNTPServer.debugStream = new PrintWriter(System.err, true);
     }
  }

  /**
   * Check the debugging preferences and set the flag... member variables
   * accordingly.
   */
  private static void checkDebugPreferences() {
     if (!flagDebug) {
        flagDebug = GlobalState.getBoolPreference(DEBUG_PREFERENCE, false);
        debugFromPrefs = true;
     }
     if (!flagConsole)
        flagConsole = GlobalState.getBoolPreference(CONSOLE_PREFERENCE, false);
     if (!flagDebugNet)  {
        flagDebugNet = GlobalState.getBoolPreference(DEBUGNET_PREFERENCE, false);
        netDebugFromPrefs = true;
     }
     
     if (flagDebug)
     {
        Debug.setTraceLevel(GlobalState.getPreferences().getIntPreference(PreferenceKeys.DEBUG_TRACELEVEL, 3));
        Debug.setAssertEnabled(GlobalState.getPreferences().getBoolPreference(PreferenceKeys.DEBUG_ASSERT, true));
     }

  }

  private static void checkCommandLine(String[] args) {
     flagDebug = flagConsole = flagDebugNet = false;
     for (int i=0; i<args.length; i++) {
        if (args[i].equals(DEBUG_FLAG))
           flagDebug = true;
        else if (args[i].equals(CONSOLE_FLAG))
           flagConsole = true;
        else if (args[i].equals(DEBUG_NET_FLAG))
           flagDebugNet = true;
     }
  }

  private static void displaySplashScreen() {
     ImageIcon img;
     img = new ImageIcon(ClassLoader.getSystemResource(SPLASH_IMAGE));

     // Create a new JLabel to act as our splash screen image
     JLabel labImg = new JLabel();
     labImg.setIcon(img);

     // We need a temp frame as a parent for the splash screen. The temp frame
     // never gets displayed.
     frmTemp = new Frame();
     winSplash = new Window(frmTemp);

     // Add the label to the window and pack the window
     winSplash.add(labImg, BorderLayout.CENTER);
     winSplash.pack();

     // Centre the window on the screen
     Dimension screenSize = winSplash.getToolkit().getScreenSize();
     Dimension winSize = winSplash.getSize();
     winSplash.setLocation(screenSize.width/2 - winSize.width/2,
                           screenSize.height/2 - winSize.height/2);

     winSplash.setVisible(true);
    

  }

  private static void hideSplashScreen() {
     winSplash.setVisible(false);
     winSplash.dispose();
     frmTemp.dispose();
  }

  private boolean invokedStandalone = false;
}