// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: AgentConfigDialogue.java,v 1.4 1999-06-01 00:25:16 briand Exp $
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
package dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dubh.apps.newsagent.GlobalState;

import dubh.utils.ui.DubhOkCancelDialog;

/**
 * Configuration Dialogue for Agents. Consists of a Panel (which can be set to
 * anything you like) and two buttons, OK and Cancel. <P>
 * To use the dialog, construct it with an agent configuration panel and
 * call the {@link #dubh.utils.ui.DubhDialog.showAtCentre()} method. 
 * It will block modally until the user clicks on OK
 * or cancel. Upon exit, call {@link #dubh.utils.ui.DubhOkCancelDialog.getPanel()}
 * to retrive the panel and {@link #dubh.utils.ui.DubhOkCancelDialog.isCancelled()}
 * to determine whether the user cancelled. Finally, remember to dispose the
 * dialog after use.<P>
 * 
 * @author Brian Duff
 * @version $Id: AgentConfigDialogue.java,v 1.4 1999-06-01 00:25:16 briand Exp $
 */
public class AgentConfigDialogue extends DubhOkCancelDialog {

   /**
   * Construct a new AgentConfigDialogue.
   * @param parent the Parent frame
   * @param panel the configuration panel to use
   * @param title the title for the dialog
   */
   public AgentConfigDialogue(Frame parent, JPanel panel, String title) {
      super(parent, title, true);
      setPanel(panel);
   }
}

//
// Old Version History:
// 0.1 [28/04/98]: Initial Revision
// 0.2 [04/05/98]: Fixed labels on Ok and Cancel buttons.
//
// New Version History:
// $Log: not supported by cvs2svn $