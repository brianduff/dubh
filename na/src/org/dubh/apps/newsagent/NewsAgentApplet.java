// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsAgentApplet.java,v 1.3 1999-11-09 22:34:40 briand Exp $
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

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import java.awt.BorderLayout;

import java.applet.Applet;
/**
 * NewsAgent applet. This brings up NewsAgent configured for an applet.
 * The applet version of NewsAgent will need to take certain preferences
 * on the command line. This is yet to be specified.
 *
 * NewsAgent can't currently be appletized because of applet restrictions
 * in reading resources. I've yet to test this with the Java plugin or 
 * Internet Explorer.
 *
 * @author Brian Duff
 * @since NewsAgent 1.1.0
 * @version $Id: NewsAgentApplet.java,v 1.3 1999-11-09 22:34:40 briand Exp $
 */
public class NewsAgentApplet extends JApplet 
{   
   private JLabel m_label;
   private JProgressBar m_progress;
   
   public void start()
   {
   
   }
   
   public void stop()
   {
   
   }
   
   public void init()
   {
      GlobalState.setApplet(true);
      GlobalState.appInit();
   }


   private void initProgress()
   {
      // TODO: Display splash
      setLayout(new BorderLayout());
      add(m_label, BorderLayout.NORTH);   
      add(m_progress, BorderLayout.SOUTH);
      repaint();
   }
   
   private void removeProgress()
   {
      remove(m_progress);
      m_label.setText("NewsAgent has initialised");
      repaint();
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/06/01 00:27:04  briand
// Add comment to explain that this doesn't work yet..
//
//