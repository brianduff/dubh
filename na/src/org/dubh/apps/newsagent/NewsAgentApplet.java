// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsAgentApplet.java,v 1.2 1999-06-01 00:27:04 briand Exp $
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
 * @version $Id: NewsAgentApplet.java,v 1.2 1999-06-01 00:27:04 briand Exp $
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
//