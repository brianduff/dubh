//----------------------------------------------------------------------------
//   NewsAgent: Java Newsreader
//   CRLFInputStream.java
//
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
//
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
//
// Revision History:
//   File  DJU    Date       Who    Changes
//   ----  ---    ----       ---    -------
//   0.0   1.1.0  5 Feb 1999 BD     Initial Revision
//
//----------------------------------------------------------------------------
package dubh.apps.newsagent.nntp;

import java.io.*;

/**
 * An output stream that escapes any dots on a line by themself with
 * another dot, for the purposes of sending messages to SMTP and NNTP servers.
 *
 * @author dog@dog.net.uk
 * @version 0.1
 */
public class MessageOutputStream extends FilterOutputStream {

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
	public MessageOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * Writes a character to the underlying stream.
	 * @exception IOException if an I/O error occurred
	 */
	public void write(int ch) throws IOException {
		if ((last==LF || last==-1) && ch==END)
			out.write(END); // double any lonesome dots
		super.write(ch);
	}

	/**
	 * Writes a portion of a byte array to the underlying stream.
	 * @exception IOException if an I/O error occurred
	 */
	public void write(byte b[], int off, int len) throws IOException {
		int k = last != -1 ? last : LF;
		int l = off;
		len += off;
		for (int i = off; i < len; i++) {
			if (k==LF && b[i]==END) {
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