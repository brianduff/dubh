// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsAgent.java,v 1.15 2000-08-19 21:21:02 briand Exp $
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
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.javalobby.apps.newsagent.nntp.NNTPServer;
import org.javalobby.dju.misc.*;
import org.javalobby.dju.ui.*;

import org.javalobby.javamail.client.ClientRegistry;


/**
 * NewsAgent main program. This class initialises NewsAgent, reading in
 * various preferences and parsing the command line. It creates the main
 * window, and displays a splash screen until it appears.
 *
 * @author Brian Duff
 * @see org.javalobby.apps.newsagent.GlobalState
 * @version $Id: NewsAgent.java,v 1.15 2000-08-19 21:21:02 briand Exp $
 */
 public class NewsAgent {

   public  static final String DEBUG_FLAG = "-debug";
   public  static final String CONSOLE_FLAG = "-console";
   public  static final String DEBUG_NET_FLAG = "-debugnet";
   private static final String DEBUG_PREFIX = "NewsAgent Debug: ";
   private static final String DEBUG_CONSOLE = "NewsAgent Debug Console";
   private static final String DEBUG_NETID = "debugnet";
   private static final String DEBUG_NET = "NewsAgent Network Debug Console";
   private static final String SPLASH_IMAGE = "org/javalobby/apps/newsagent/images/splash.gif";

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

   /** Temporary frame for parenting the splashscreen */
   private static Frame frmTemp;
   /** The splashscreen */
   private static Window winSplash;

   /** Network debugging frame */
   private static DebugFrame m_netDebug;


   public NewsAgent() {
   }

   /**
    * Initialise the NewsAgent application.
    */
   public static void init()
   {
      //
      // Set up debugging before we do appInit(), in case there are any debug
      // messages during application initialisation
      //

      setupDebugging();
      if (flagDebug)
         Debug.println("Debugging output was enabled with the "+DEBUG_FLAG+" command line flag.");

      GlobalState.appInit();

      checkDebugPreferences();
      if (debugFromPrefs) setupDebugging();

      if (flagDebugNet)
      {
         setupNetDebugging();
         if (netDebugFromPrefs)
           NNTPServer.debugStream.println("Network debugging output was enabled with the "+PreferenceKeys.DEBUG_SERVERDUMP+" user preference.");
         else
           NNTPServer.debugStream.println("Network debugging output was enabled with the "+DEBUG_NET_FLAG+" command line flag.");
      }

      if (debugFromPrefs)
      {
         Debug.println("Debugging output was enabled with the "+PreferenceKeys.DEBUG_DEBUGMESSAGES+" user preference.");
      }

   }



   /**
    * The main NewsAgent application.
    */
   public static void main(String[] args) throws Exception {

    NewsAgent newsAgent = new NewsAgent();

    Package naPackage = Package.getPackage("org.javalobby.apps.newsagent");
    if (naPackage != null)
    {
      System.out.println("This is "+naPackage.getSpecificationTitle()+" version "+naPackage.getSpecificationVersion()+" "+naPackage.getImplementationVersion());
    }
    else
    {
      System.err.println("Couldn't find package org.javalobby.apps.newsagent");
    }
     try {
        displaySplashScreen();
        checkCommandLine(args);
        init();

        // Register the NNTP client. Probably doesn't belong here.
        ClientRegistry.setStoreClient("news", "org.javalobby.javamail.client.news.NetworkNewsClient");


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
     UserPreferences p = GlobalState.getPreferences();

     if (!flagDebug) {
        flagDebug = p.getBoolPreference(PreferenceKeys.DEBUG_DEBUGMESSAGES, false);
        debugFromPrefs = true;
     }
     if (!flagConsole)
        flagConsole = p.getBoolPreference(PreferenceKeys.DEBUG_CONSOLE, false);
     if (!flagDebugNet)  {
        flagDebugNet = p.getBoolPreference(PreferenceKeys.DEBUG_SERVERDUMP, false);
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
}

//
// $Log: not supported by cvs2svn $
// Revision 1.14  2000/06/14 21:36:45  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.13  1999/12/16 22:09:11  briand
// Yet another delta to test JFABuilder.
//
// Revision 1.12  1999/12/16 21:57:14  briand
// Testing builder again.
//
// Revision 1.11  1999/12/16 21:55:52  briand
// Testing builder.
//
// Revision 1.10  1999/12/12 03:04:57  briand
// Changed hard-coded image filename to point to new jfa directory. (splash
// screen)
//
// Revision 1.9  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.8  1999/11/09 22:04:57  briand
// Moved to Javalobby.
//
// Revision 1.7  1999/10/24 00:41:58  briand
// Nothing has changed. Honest guv.
//
// Revision 1.6  1999/06/01 00:27:28  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//
//
