// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: MessageInputStream.java,v 1.2 2001-02-11 02:52:48 briand Exp $
//   Copyright (C) 1999 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1999 - 2001 Brian Duff
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

package org.dubh.javamail.client.news;

import java.io.*;

/**
 * A utility class for feeding message contents to messages.
 *
 * @version $Id: MessageInputStream.java,v 1.2 2001-02-11 02:52:48 briand Exp $
 */
class MessageInputStream extends FilterInputStream
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
// Revision 1.1  2000/02/22 23:47:35  briand
// News client implementation initial revision.
//
// Revision 1.2  1999/11/11 21:26:38  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/06/08 22:44:32  briand
// Initial Revision
//
//