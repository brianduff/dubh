// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: AgentConfigDialogue.java,v 1.5 1999-11-09 22:34:40 briand Exp $
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
package org.javalobby.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.javalobby.apps.newsagent.GlobalState;

import org.javalobby.dju.ui.DubhOkCancelDialog;

/**
 * Configuration Dialogue for Agents. Consists of a Panel (which can be set to
 * anything you like) and two buttons, OK and Cancel. <P>
 * To use the dialog, construct it with an agent configuration panel and
 * call the {@link #org.javalobby.dju.ui.DubhDialog.showAtCentre()} method. 
 * It will block modally until the user clicks on OK
 * or cancel. Upon exit, call {@link #org.javalobby.dju.ui.DubhOkCancelDialog.getPanel()}
 * to retrive the panel and {@link #org.javalobby.dju.ui.DubhOkCancelDialog.isCancelled()}
 * to determine whether the user cancelled. Finally, remember to dispose the
 * dialog after use.<P>
 * 
 * @author Brian Duff
 * @version $Id: AgentConfigDialogue.java,v 1.5 1999-11-09 22:34:40 briand Exp $
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
// Revision 1.4  1999/06/01 00:25:16  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//