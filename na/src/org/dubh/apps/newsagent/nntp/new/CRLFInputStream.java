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
 * An input stream that filters out CR/LF pairs into LFs.
 *
 * @author dog@dog.net.uk
 * @version 0.1
 */
public class CRLFInputStream extends PushbackInputStream {

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
	public CRLFInputStream(InputStream in) {
		super(in);
	}

	/**
	 * Reads the next byte of data from this input stream.
	 * Returns -1 if the end of the stream has been reached.
	 * @exception IOException if an I/O error occurs
	 */
	public int read() throws IOException {
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
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	/**
	 * Reads up to len bytes of data from this input stream into an
	 * array of bytes, starting at the specified offset.
	 * Returns -1 if the end of the stream has been reached.
	 * @exception IOException if an I/O error occurs
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		int l = super.read(b, off, len);
		return removeCRs(b, off, l);
	}

	/**
	 * Reads a line of input terminated by LF.
	 * @exception IOException if an I/O error occurs
	 */
	public String readLine() throws IOException {
		byte[] acc = new byte[4096];
		int count = 0;
		for (byte b=(byte)read(); b!=LF && b!=-1; b=(byte)read())
			acc[count++] = b;
		if (count>0) {
			byte[] bytes = new byte[count];
			System.arraycopy(acc, 0, bytes, 0, count);
			return new String(bytes);
		} else
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