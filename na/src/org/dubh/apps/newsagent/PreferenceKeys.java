// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: PreferenceKeys.java,v 1.6 2000-08-19 21:21:23 briand Exp $
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

import java.io.IOException;

import org.javalobby.dju.misc.UserPreferences;
import org.javalobby.dju.misc.Debug;

/**
* This file contains constants that represent preference keys in the 
* preferences file. This also provides a converter from old versions
* of the preferences file.
* <p>
* You should use the static constants in this file rather than hard coded
* strings when referring to preference file keys.
*  
* @author Brian Duff
* @since NewsAgent 1.1.0
* @version $Id: PreferenceKeys.java,v 1.6 2000-08-19 21:21:23 briand Exp $
*/
public class PreferenceKeys
{
   //
   // Each preference is listed, along with the version in which is was first
   // introduced. Preferences marked with [*] have changed capitalisation (or
   // in a couple of cases, spelling) since 1.1.0. These will need to be
   // converted in old preference files.
   //

   public static final String
      PREFERENCES_VERSION       = "newsagent.preferences.Version",        // since 1.1.0
      
      AGENTS_SEND_ACTIVE        = "newsagent.agents.send.Active",         // since 1.0.1 [*]
      AGENTS_SEND_ALWAYSPREVIEW = "newsagent.agents.send.AlwaysPreview",  // since 1.0.1 [*]
      AGENTS_SEND_IGNOREERRORS  = "newsagent.agents.send.IgnoreErrors",   // since 1.0.2 [*]
      AGENTS_SEND_IGNOREWARNINGS= "newsagent.agents.send.IgnoreWarnings", // since 1.0.2 [*]
      AGENTS_SEND_INSTALLED     = "newsagent.agents.send.Installed",      // since 1.0.1 [*]
      
      AGENTS_LIST_ACTIVE        = "newsagent.agents.list.Active",         // since 1.0.1 [*]
      AGENTS_LIST_INSTALLED     = "newsagent.agents.list.Installed",      // since 1.0.1 [*]
      
      COMPOSER_ADDITIONALHEADERS= "newsagent.composer.AdditionalHeaders", // since 1.0.2
      
      DEBUG_CONSOLE             = "newsagent.debug.Console",              // since 1.0.2
      DEBUG_DEBUGMESSAGES       = "newsagent.debug.DebugMessages",        // since 1.0.2
      DEBUG_SERVERDUMP          = "newsagent.debug.ServerDump",           // since 1.0.2
      DEBUG_TRACELEVEL          = "newsagent.debug.TraceLevel",           // since 1.1.0
      DEBUG_ASSERT              = "newsagent.debug.Assert",               // since 1.1.0
      
      GENERAL_AUTOUPDATE        = "newsagent.general.AutoUpdate",         // since 1.0.1
      GENERAL_HELPBROWSERAPP    = "newsagent.general.HelpBrowserApp",     // since 1.0.2
      GENERAL_NEWSGROUPNOTIFY   = "newsagent.general.NewsgroupNotify",    // since 1.0.1
      GENERAL_UPDATEINTERVAL    = "newsagent.general.UpdateInterval",     // since 1.0.1
      GENERAL_NEWSRC            = "newsagent.general.NewsRc",             // since 1.1.0
      
      IDENTITY_EMAIL            = "newsagent.identity.Email",             // since 1.0.0 [*]
      IDENTITY_REPLYTO          = "newsagent.identity.ReplyTo",           // since 1.1.0
      IDENTITY_ORGANISATION     = "newsagent.identity.Organisation",      // since 1.0.0 [*]
      IDENTITY_REALNAME         = "newsagent.identity.RealName",          // since 1.0.0 [*]
      
      SIGNATURE_TEXT            = "newsagent.signature.Text",             // since 1.1.0
      SIGNATURE_FILENAME        = "newsagent.signature.FileName",         // since 1.1.0
      SIGNATURE_ECARD           = "newsagent.signature.ECard",            // since 1.1.0
      
      MSGVIEWER_HEADERS         = "newsagent.msgviewer.Headers",          // since 1.0.2
      
      SEND_ADDNEWSAGENTHEADERS  = "newsagent.send.AddNewsAgentHeaders",   // since 1.0.2
      SEND_HARDBREAKS           = "newsagent.send.HardBreaks",            // since 1.0.2
      SEND_INCLUDEBEHAVIOUR     = "newsagent.send.IncludeBehaviour",      // since 1.0.2
      SEND_INCLUDEHEADING       = "newsagent.send.IncludeHeading",        // since 1.0.2
      xSEND_INCLUDEMESSAGES     = "newsagent.send.IncludeMessages",       // deprecated since 1.0.2
      SEND_INCLUDEPREFIX        = "newsagent.send.IncludePrefix",         // since 1.0.2
      
      SERVERS_SMTPHOSTNAME      = "newsagent.servers.SMTPHostName",       // since 1.0.1
      SERVERS_SMTPPORT          = "newsagent.servers.SMTPPort",           // since 1.0.1
      
      UI_LOOKANDFEEL            = "newsagent.ui.LookAndFeel",             // since 1.0.2
      UI_METALTHEME             = "newsagent.ui.MetalTheme",              // since 1.1.0 nyi
      
      SCRIPTING_ENABLED         = "newsagent.scripting.Enabled",          // since 1.1.0 nyi
      SCRIPTING_INITSCRIPTS     = "newsagent.scripting.InitScripts",      // since 1.1.0 nyi
      SCRIPTING_EXITSCRIPTS     = "newsagent.scripting.ExitScripts",      // since 1.1.0 nyi
    
      VIEWER_DISPLAYEDHEADERS   = "newsagent.viewer.DisplayedHeaders",    // since 1.1.0
      VIEWER_SHOWHEADERS        = "newsagent.viewer.ShowHeaders",         // since 1.1.0
      VIEWER_NORMALFONT         = "newsagent.viewer.NormalFont",          // since 1.1.0
      VIEWER_NORMALCOLOR        = "newsagent.viewer.NormalColor",         // since 1.1.0
      VIEWER_QUOTEDFONT         = "newsagent.viewer.QuotedFont",          // since 1.1.0
      VIEWER_QUOTEDCOLOR        = "newsagent.viewer.QuotedColor",         // since 1.1.0
      VIEWER_SIGFONT            = "newsagent.viewer.SigFont",             // since 1.1.0
      VIEWER_SIGCOLOR           = "newsagent.viewer.SigColor",            // since 1.1.0
      VIEWER_WRAPPING           = "newsagent.viewer.Wrapping",            // since 1.1.0
      VIEWER_HIDESIG            = "newsagent.viewer.HideSig",             // since 1.1.0
      VIEWER_SHOWVCARD          = "newsagent.viewer.ShowVCard",           // since 1.1.0
      VIEWER_ALLOWHTML          = "newsagent.viewer.AllowHTML",           // since 1.1.0
      VIEWER_WRAP               = "newsagent.viewer.WrapLongLines",       // since 1.1.0
      VIEWER_HYPERLINKS         = "newsagent.viewer.Hyperlinks";          // since 1.1.0
      
      
   /**
    * Convert an old preferences file to one that works with the current
    * version of NewsAgent.
    */   
   public static void convertPreferences(UserPreferences up)
   {
      String pref = up.getPreference(PREFERENCES_VERSION, "?.?.?");
      String version = GlobalState.getVersion();

      //
      // In future, will need to check version for other versions.
      //
      if (!pref.equals(version))
      {
         if (Debug.TRACE_LEVEL_3)   
            Debug.println(3, PreferenceKeys.class, "Upgrading old preference file for version "+version);
         
         up.setPreference(PREFERENCES_VERSION, version);
       
         from0_0_0To1_1_0(up);  
         
         try
         {
            up.save();
         }
         catch (IOException ioe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.printException(1, PreferenceKeys.class, ioe);
            }
         }
      }
   }
   
   private static final void from0_0_0To1_1_0(UserPreferences up)
   {
      // Preferences that have changed capitalisation
      String[] capPref = new String[] {
         AGENTS_SEND_ACTIVE, AGENTS_SEND_ALWAYSPREVIEW, AGENTS_SEND_IGNOREERRORS,
         AGENTS_SEND_IGNOREWARNINGS, AGENTS_SEND_INSTALLED, AGENTS_LIST_ACTIVE,
         AGENTS_LIST_INSTALLED, IDENTITY_EMAIL, IDENTITY_REALNAME, IDENTITY_ORGANISATION
      };
      
      for (int i=0; i < capPref.length; i++)
      {
         String pref = up.getPreference(capPref[i].toLowerCase());
         if (pref != null)
         {
            up.removeKey(capPref[i].toLowerCase());
            up.setPreference(capPref[i], pref);
         }
      }
      
      // Deprecated preferences
      String incl = up.getPreference(xSEND_INCLUDEMESSAGES);
      if (incl != null)
      {
         boolean include = up.getBoolPreference(xSEND_INCLUDEMESSAGES);
         up.removeKey(xSEND_INCLUDEMESSAGES);
         
         String newInclude = up.getPreference(SEND_INCLUDEBEHAVIOUR, "NOTDEFINED");
         
         if (newInclude.equals("NOTDEFINED"))
         {
            newInclude = (include ? "all" : "none");
            up.setPreference(SEND_INCLUDEBEHAVIOUR, newInclude);            
         }  
      }
   }
   

}

//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 17:58:12  briand
// Add new preference keys required by Viewer preferences panel.
//
// Revision 1.3  1999/06/01 00:26:31  briand
// Add lots of new preferences. Many don't work yet, or can't be set through the UI.
//
//