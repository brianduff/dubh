// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: CRLFOutputStream.java,v 1.2 1999-11-11 21:26:38 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
//   Original Author: dog@dog.net.uk
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package org.javalobby.javamail.nntp;

import java.io.*;

/**
 * An output stream that filters LFs into CR/LF pairs.
 *
 * @version $Id: CRLFOutputStream.java,v 1.2 1999-11-11 21:26:38 briand Exp $
 */
public class CRLFOutputStream extends FilterOutputStream {

   /**
    * The CR octet.
    */
   public static final int CR = 13;
   
   /**
    * The LF octet.
    */
   public static final int LF = 10;
   
   /**
    * The CR/LF pair.
    */
   public static final byte[] CRLF = { CR, LF };
   
   /**
    * The last byte read.
    */
   protected int last;

   /**
    * Constructs a CR/LF output stream connected to the specified output stream.
    */
   public CRLFOutputStream(OutputStream out) 
   {
      super(out);
      last = -1;
   }

   /**
    * Writes a character to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void write(int ch) throws IOException 
   {
      if (ch==CR)
         out.write(CRLF);
      else
         if (ch==LF) 
         {
            if (last!=CR)
               out.write(CRLF);
         }
         else
         {
            out.write(ch);
         }
      last = ch;
   }

   /**
    * Writes a byte array to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void write(byte b[]) throws IOException
   {
      write(b, 0, b.length);
   }

   /**
    * Writes a portion of a byte array to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void write(byte b[], int off, int len) throws IOException
   {
      int d = off;
      len += off;
      for (int i=off; i<len; i++)
         switch (b[i])
         {
           default:
            break;
           case CR:
            if (i+1<len && b[i+1]==LF)
            {
               i++;
            }
            else
            {
               out.write(b, d, (i-d)+1);
               out.write(LF);
               d = i+1;
            }
            break;
           case LF:
            out.write(b, d, i-d);
            out.write(CRLF, 0, 2);
            d = i+1;
            break;
         }
      if (len-d>0)
         out.write(b, d, len-d);
   }

   /**
    * Writes a newline to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void writeln() throws IOException
   {
      out.write(CRLF);
   }
   
   public void println(String s) throws IOException
   {
      out.write(s.getBytes());
      writeln();
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/06/08 22:44:32  briand
// Initial Revision
//
//