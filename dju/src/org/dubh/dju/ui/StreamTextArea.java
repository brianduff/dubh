// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StreamTextArea.java,v 1.2 1999-03-22 23:37:19 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
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

package dubh.utils.ui;

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