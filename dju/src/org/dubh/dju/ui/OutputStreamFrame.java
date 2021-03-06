// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: OutputStreamFrame.java,v 1.6 2001-02-11 02:52:12 briand Exp $
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