// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: TextPlainViewer.java,v 1.1 1999-10-17 17:03:27 briand Exp $
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

package dubh.apps.newsagent.mailviewer.content;

import javax.activation.*;
import javax.swing.*;

import java.awt.*;

import java.io.*;

/**
 * A bean that can be used to display the body text of messages that 
 * have content type text/plain.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: TextPlainViewer.java,v 1.1 1999-10-17 17:03:27 briand Exp $
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
//