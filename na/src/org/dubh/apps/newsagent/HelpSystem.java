// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: HelpSystem.java,v 1.4 1999-06-01 00:28:20 briand Exp $
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

package dubh.apps.newsagent;

import dubh.utils.help.HelpFrame;
import dubh.utils.misc.*;
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

//
// Old Log:
// 0.1 [08/06/98]: Initial Revision           
// 0.2 [03/07/98]: Changed to use HelpFrame
// 0.3 [04/07/98]: Minor change to filenames in static constants to reflect
//   documentation updates in NA 1.02
