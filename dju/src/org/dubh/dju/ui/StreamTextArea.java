// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StreamTextArea.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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


package org.dubh.dju.ui;

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