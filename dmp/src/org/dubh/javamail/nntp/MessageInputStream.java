// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: MessageInputStream.java,v 1.1 1999-06-08 22:44:32 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
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
//   Original Author: dog@dog.net.uk
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.mail.nntp;

import java.io.*;

/**
 * A utility class for feeding message contents to messages.
 *
 * @version $Id: MessageInputStream.java,v 1.1 1999-06-08 22:44:32 briand Exp $
 */
public class MessageInputStream extends FilterInputStream 
{

   /**
    * The stream termination octet.
    */
   public static final int END = 46;
   
   /**
    * The line termination octet.
    */
   public static final int LF = 10;
   
   boolean done = false;
   int last = -1; // register for the last octet read
   
   /**
    * Constructs a message input stream connected to the specified pushback input stream.
    */
   public MessageInputStream(PushbackInputStream in) 
   {
      super(in);
   }

   /**
    * Reads the next byte of data from this message input stream.
    * Returns -1 if the end of the message stream has been reached.
    * @exception IOException if an I/O error occurs
    */
   public int read() throws IOException 
   {
      if (done) return -1;
      int ch = in.read();
      if (ch==END && last==LF) 
      {
         int ch2 = in.read(); // look ahead for LF
         if (ch2==LF) 
         {
            done = true;
            return -1; // swallow the END and LF
         }
         else
            ((PushbackInputStream)in).unread(ch2);
      }
      last = ch;
      return ch;
   }
   
   /**
    * Reads up to b.length bytes of data from this input stream into
    * an array of bytes.
    * Returns -1 if the end of the stream has been reached.
    * @exception IOException if an I/O error occurs
    */
   public int read(byte[] b) throws IOException 
   {
      return read(b, 0, b.length);
   }
   
   /**
    * Reads up to len bytes of data from this input stream into an
    * array of bytes, starting at the specified offset.
    * Returns -1 if the end of the stream has been reached.
    * @exception IOException if an I/O error occurs
    */
   public int read(byte[] b, int off, int len) throws IOException 
   {
      if (done) return -1;
      int l = in.read(b, off, len);
      int i = indexOfEnd(b, off, l);
      if (i>-1) 
      {
         ((PushbackInputStream)in).unread(b, i+2, (l-(off+i+2)));
         done = true;
         return i-off;
      }
      return l;
   }

    // Discover the index of END in a byte array.
   int indexOfEnd(byte[] b, int off, int len) 
   {
      for (int i=off+2; i<(off+len); i++)
         if (b[i]==LF && b[i-1]==END && b[i-2]==LF)
            return i-1;
      return -1;
   }

}

//
// $Log: not supported by cvs2svn $
//