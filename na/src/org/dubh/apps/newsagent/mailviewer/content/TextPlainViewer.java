// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: TextPlainViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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

package org.javalobby.apps.newsagent.mailviewer.content;

import javax.activation.*;
import javax.swing.*;

import java.awt.*;

import java.io.*;

/**
 * A bean that can be used to display the body text of messages that 
 * have content type text/plain.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: TextPlainViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class TextPlainViewer extends JPanel implements CommandObject
{
   protected JTextArea m_taMessage;
   protected DataHandler m_dhHandler;
   protected String m_strVerb;
   
   /**
    * Construct a viewer for text/plain content type
    */
   public TextPlainViewer()
   {
      super(new BorderLayout());
      
      // TODO: Use configuration to alter wrap settings etc.
      m_taMessage = new JTextArea();
      m_taMessage.setEditable(false);
      JScrollPane s = new JScrollPane(m_taMessage);
      
      add(s, BorderLayout.CENTER);
   }
   
   /**
    * Set the activation command context
    */
   public void setCommandContext(String verb, DataHandler dh)
      throws IOException 
   {

      m_strVerb = verb;
      m_dhHandler = dh;
   
      setInputStream( dh.getInputStream() );
   }


  /**
   * Set the data stream. 
   */
  public void setInputStream(InputStream ins) 
     throws IOException
  {
      m_taMessage.read(new InputStreamReader(ins), null);
  }   
   
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/17 17:03:27  briand
// Initial revision.
//
//