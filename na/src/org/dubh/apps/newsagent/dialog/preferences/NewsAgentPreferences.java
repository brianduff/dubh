// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsAgentPreferences.java,v 1.1 1999-03-22 23:44:27 briand Exp $
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
package dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.event.*;
import dubh.apps.newsagent.dialog.NFrame;
import dubh.apps.newsagent.GlobalState;
import dubh.utils.ui.preferences.*;

/**
 * The NewsAgent Preferences dialog.
 * @author Brian Duff
 * @version $Id: NewsAgentPreferences.java,v 1.1 1999-03-22 23:44:27 briand Exp $
 */
public class NewsAgentPreferences extends PreferencesDialog {

   private static NewsAgentPreferences m_singleton;

   private NewsAgentPreferences()
   {
      super(GlobalState.appName+" "+GlobalState.getResString("OptionsFrame.Preferences"),
            "naprefs", GlobalState.getMainFrame(), GlobalState.getPreferences());
   
      GeneralOptionsPanel general = new GeneralOptionsPanel();
      SendOptionsPanel send = new SendOptionsPanel();
      ServerOptionsPane servers = new ServerOptionsPane();
      IdentityOptionsPanel identity = new IdentityOptionsPanel();
      SendAgentsOptionsPanel sendagents = new SendAgentsOptionsPanel();
      ListAgentsOptionsPanel listagents = new ListAgentsOptionsPanel();

      addPage(general);
      addPage(send);
      addPage(servers);
      addPage(identity);
      addPage(sendagents);
      addPage(listagents);
  }
  
   public static void showDialog()
   {
      if (m_singleton == null)
         m_singleton = new NewsAgentPreferences();
         
      m_singleton.revertAll();
      
      m_singleton.pack();
      
      m_singleton.setVisible(true);
   }  

}

//
// $Log: not supported by cvs2svn $
//