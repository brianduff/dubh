// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: OutputStreamFrame.java,v 1.4 1999-03-22 23:37:18 briand Exp $
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

import java.io.*;
import java.awt.*;
import javax.swing.*;

/**
 * An output stream which displays its output in a window with a scrollbar.
 * Convenient for systems without a console for stdout, for instance.
 * Version History: <UL>
 * <LI>0.1 [08/06/98]: Initial Revision
 * <LI>0.2 [03/07/98]: Now using a fixed text area for output
 *</UL>
 @author Brian Duff
 @version 0.2 [03/07/98]
 */
public class OutputStreamFrame extends OutputStream {
  private DubhFrame fraStream;
  private FixedTextArea taStream;
  private JScrollPane scrollStream;

  /**
   * Construct an outputstream frame with no caption and a default width of
   * 80 columns and 24 rows height.
   */
  public OutputStreamFrame(String name) {
     this(name, name, 24, 80);
  }

  /**
   * Construct an outputstream frame with a default width of
   * 80 columns and 24 rows height.
   @param caption the title of the window
   */
  public OutputStreamFrame(String name, String caption) {
     this(name, caption, 24, 80);
  }

  /**
   * Construct an output stream frame with no caption.
   @param rows the number of rows to display
   @param cols the number of columns to display
   */
  public OutputStreamFrame(String name, int rows, int cols) {
     this(name, "", rows, cols);
  }

  /**
   * Construct an output stream frame.
   @param name    the name of the frame. This is used for saving the window
                  position.
   @param caption the title of the window
   @param rows the number of rows
   @param cols the number of columns
   */
  public OutputStreamFrame(String name, String caption, int rows, int cols) {
     super();
     fraStream = new DubhFrame(caption);
     taStream  = new FixedTextArea(cols, rows);
     taStream.setEditable(false);
     scrollStream = new JScrollPane(taStream);
     fraStream.getContentPane().add("Center", scrollStream);
     fraStream.pack();
     fraStream.setVisible(true);
     fraStream.setName(name);
  }

  /**
   * Close the stream. Causes the window containing the stream text to be
   * disposed and removed from the display.
   */
  public void close() throws IOException {
     super.close();
     fraStream.setVisible(false);
     fraStream.dispose();
  }

  /**
   * Gets the frame that is being used to display the output stream
   @return a frame object
   */
  public Frame getFrame() {
     return fraStream;
  }

  public void write(int b) {
     taStream.append(new String("" + (char) b));
     taStream.setSelectionStart(taStream.getText().length());
  }

  public void write(byte[] b) {
     taStream.append(new String(b));
     taStream.setSelectionStart(taStream.getText().length());

  }

  public void write(byte[] b, int off, int len) {
     taStream.append(new String(b, off, len));
     taStream.setSelectionStart(taStream.getText().length());

  }


}