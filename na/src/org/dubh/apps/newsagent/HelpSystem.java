// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: HelpSystem.java,v 1.6 2001-02-11 02:50:58 briand Exp $
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

import org.dubh.dju.help.HelpFrame;
import org.dubh.dju.misc.*;
/**
 * NewsAgent's Hypertext help system (at last!!!).
 * Version History: <UL>

 @author Brian Duff
 @version 0.3 [04/07/98]
 */
public class HelpSystem {

  private HelpFrame helpBrowser;
  private static final String BROWSER_TITLE = "Help";

  /*
   * Quick links to topics
   */
  public static final String TOPIC_CONTENTS    = "toplevel.html";
  public static final String TOPIC_CONTREE     = "contents.html";
  public static final String TOPIC_WHATSNEW    = "about/whatsnew.html";
  public static final String TOPIC_USINGHELP   = "about/usinghelp.html";
  public static final String TOPIC_GETSTART    = "gettingstarted/index.html";
  public static final String TOPIC_ABOUT       = "about/index.html";
  public static final String TOPIC_ADVANCED    = "advanced/index.html";
  public static final String TOPIC_DIALOGS     = "dialogs/index.html";
  public static final String TOPIC_MENUS       = "menus/index.html";

  public HelpSystem() {
     try {
        ResourceManager m = GlobalState.getRes();
        helpBrowser = new HelpFrame(BROWSER_TITLE,
           ClassLoader.getSystemResource(TOPIC_CONTREE));
        helpBrowser.pack();
        helpBrowser.setFolderIcons(
           m.getImage("hlpFolder"), m.getImage("hlpOpenFolder")
        );
        helpBrowser.setNewFolderIcons(
           m.getImage("hlpNewFolder"), m.getImage("hlpNewOpenFolder")
        );
        helpBrowser.setTopicIcons(
           m.getImage("hlpTopic"), m.getImage("hlpNewTopic")
        );
        helpBrowser.setWebIcons(
           m.getImage("hlpWeb"), m.getImage("hlpNewWeb")
        );
     } catch (Exception e) {
        Debug.println("Exception initialising help system: "+e);
     }

  }

  public void jumpToTopic(String topic) {
     try {
        helpBrowser.setURL(ClassLoader.getSystemResource(topic));
        helpBrowser.setVisible(true);
     } catch (Exception e) {
        Debug.println("HelpSystem.jumpToTopic: Error loading "+topic);
     }
  }



}

//
// New (CVS) Log:
//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:28:20  briand
// Comment mangling.
//

//
// Old Log:
// 0.1 [08/06/98]: Initial Revision
// 0.2 [03/07/98]: Changed to use HelpFrame
// 0.3 [04/07/98]: Minor change to filenames in static constants to reflect
//   documentation updates in NA 1.02
