// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: TextHtmlViewer.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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


package org.dubh.apps.newsagent.mailviewer.content;

import javax.activation.*;
import javax.swing.*;

import java.awt.*;

import java.io.*;

/**
 * A bean that can be used to display the body text of messages that
 * have content type text/html
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: TextHtmlViewer.java,v 1.3 2001-02-11 02:51:00 briand Exp $
 */
public class TextHtmlViewer extends JPanel implements CommandObject
{
   protected JTextPane m_taMessage;
   protected DataHandler m_dhHandler;
   protected String m_strVerb;

   /**
    * Construct a viewer for text/plain content type
    */
   public TextHtmlViewer()
   {
      super(new BorderLayout());

      // TODO: Use configuration to alter wrap settings etc.
      m_taMessage = new JTextPane();
      m_taMessage.setContentType("text/html");
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
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/17 17:03:27  briand
// Initial revision.
//
//