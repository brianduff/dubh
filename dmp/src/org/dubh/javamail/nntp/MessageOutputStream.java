// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: MessageOutputStream.java,v 1.1 1999-06-08 22:44:32 briand Exp $
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
 * An output stream that escapes any dots on a line by themself with
 * another dot, for the purposes of sending messages to SMTP and NNTP servers.
 * @version $Id: MessageOutputStream.java,v 1.1 1999-06-08 22:44:32 briand Exp $
 */
public class MessageOutputStream extends FilterOutputStream 
{

   /**
    * The stream termination octet.
    */
   public static final int END = 46;
   
   /**
    * The line termination octet.
    */
   public static final int LF = 10;

   int last = -1; // the last character written to the stream
   
   /**
    * Constructs a message output stream connected to the specified output stream.
    * @param out the target output stream
    */
   public MessageOutputStream(OutputStream out) 
   {
      super(out);
   }

   /**
    * Writes a character to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void write(int ch) throws IOException 
   {
      if ((last==LF || last==-1) && ch==END)
         out.write(END); // double any lonesome dots
      super.write(ch);
   }

   /**
    * Writes a portion of a byte array to the underlying stream.
    * @exception IOException if an I/O error occurred
    */
   public void write(byte b[], int off, int len) throws IOException 
   {
      int k = last != -1 ? last : LF;
      int l = off;
      len += off;
      for (int i = off; i < len; i++) 
      {
         if (k==LF && b[i]==END) 
         {
            super.write(b, l, i-l);
            out.write(END);
            l = i;
         }
         k = b[i];
      }
      if (len-l > 0)
         super.write(b, l, len-l);
   }
   
}

//
// $Log: not supported by cvs2svn $
//