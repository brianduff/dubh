// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StreamTextArea.java,v 1.3 1999-11-11 21:24:36 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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

package org.javalobby.dju.ui;

import java.io.OutputStream;
import java.io.IOException;

/**
 * A text area that is associated with an output stream. The text area
 * receives all information printed to the output stream and displays
 * it.
 * @author Brian Duff
 */
public class StreamTextArea extends FixedTextArea 
{

   private StreamTextAreaStream m_stream;

   /**
    * Construct a stream text area
    */
   public StreamTextArea()
   {
      super();
      m_stream = new StreamTextAreaStream();
      setEditable(false);
   }
   
   public OutputStream getStream()
   {
      return m_stream;
   }

   /**
    * Inner class that implements the stream
    */
   class StreamTextAreaStream extends OutputStream
   {
      public void write(int b) 
      {
         append(new String("" + (char) b));
         setSelectionStart(getText().length());
      }
      
      public void write(byte[] b) 
      {
         append(new String(b));
         setSelectionStart(getText().length());
      }
      
      public void write(byte[] b, int off, int len) 
      {
         append(new String(b, off, len));
         setSelectionStart(getText().length());
      }
   } 

}