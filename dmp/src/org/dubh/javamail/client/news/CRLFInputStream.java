// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: CRLFInputStream.java,v 1.2 2001-02-11 02:52:48 briand Exp $
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
 * An input stream that filters out CR/LF pairs into LFs.
 */
class CRLFInputStream extends PushbackInputStream
{

   /**
    * The CR octet.
    */
   public static final int CR = 13;

   /**
    * The LF octet.
    */
   public static final int LF = 10;

   /**
    * Constructs a CR/LF input stream connected to the specified input stream.
    */
   public CRLFInputStream(InputStream in)
   {
      super(in);
   }

   /**
    * Reads the next byte of data from this input stream.
    * Returns -1 if the end of the stream has been reached.
    * @exception IOException if an I/O error occurs
    */
   public int read() throws IOException
   {
      int c = super.read();
      if (c==CR) // skip CR
         return super.read();
      return c;
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
      int l = super.read(b, off, len);
      return removeCRs(b, off, l);
   }

   /**
    * Reads a line of input terminated by LF.
    * @exception IOException if an I/O error occurs
    */
   public String readLine() throws IOException
   {
      byte[] acc = new byte[4096];
      int count = 0;
      for (byte b=(byte)read(); b!=LF && b!=-1; b=(byte)read())
         acc[count++] = b;
      if (count>0)
      {
         byte[] bytes = new byte[count];
         System.arraycopy(acc, 0, bytes, 0, count);
         return new String(bytes);
      }
      else
         return null;
   }

   int removeCRs(byte[] b, int off, int len) {
      for (int index = indexOfCR(b, off, len); index>-1; index = indexOfCR(b, off, len)) {
         for (int i=index; i<b.length-1; i++)
            b[i] = b[i+1];
         len--;
      }
      return len;
   }

   int indexOfCR(byte[] b, int off, int len) {
      for (int i=off; i<off+len; i++)
         if (b[i]==CR) return i;
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