// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsAgentPreferences.java,v 1.3 1999-11-09 22:34:41 briand Exp $
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
package org.javalobby.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.javalobby.dju.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.event.*;
import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.dju.ui.preferences.*;

/**
 * The NewsAgent Preferences dialog.
 * @author Brian Duff
 * @version $Id: NewsAgentPreferences.java,v 1.3 1999-11-09 22:34:41 briand Exp $
 */
public class NewsAgentPreferences extends PreferencesDialog {

   private static NewsAgentPreferences m_singleton;

   private NewsAgentPreferences()
   {
      super(GlobalState.appName+" "+GlobalState.getRes().getString("OptionsFrame.Preferences"),
            "naprefs", GlobalState.getMainFrame(), GlobalState.getPreferences());
   
      GeneralOptionsPanel general = new GeneralOptionsPanel();
      SendOptionsPanel send = new SendOptionsPanel();
      ServerOptionsPane servers = new ServerOptionsPane();
      IdentityOptionsPanel identity = new IdentityOptionsPanel();
      SendAgentsOptionsPanel sendagents = new SendAgentsOptionsPanel();
      ListAgentsOptionsPanel listagents = new ListAgentsOptionsPanel();
      UIViewerPreferences uiviewer = new UIViewerPreferences();

      addPage(general);
      addPage(send);
      addPage(servers);
      addPage(identity);
      addPage(sendagents);
      addPage(listagents);
      addPage(uiviewer);
  }
  
  
   public static void showDialog()
   {
      if (m_singleton == null)
         m_singleton = new NewsAgentPreferences();
         
      m_singleton.revertAll();
      
      m_singleton.pack();
      
      m_singleton.setVisible(true);
   }  

   /**
    * Show the dialog open at a specific page.
    */
   public static void showDialog(String pageName)
   {
      if (m_singleton == null)
         m_singleton = new NewsAgentPreferences();
      
      m_singleton.revertAll();
      m_singleton.pack();
      m_singleton.selectPage(pageName);
      m_singleton.setVisible(true);
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/06/01 00:36:25  briand
// Add method to display the dialog at a specific page.
//
// Revision 1.1  1999/03/22 23:44:27  briand
// The new NewsAgent preferences dialog.
//
//