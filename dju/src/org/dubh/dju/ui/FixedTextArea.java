// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FixedTextArea.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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

import javax.swing.*;

/**
 * A trivial subclass of TextArea that uses a fixed font, has word wrap
 * properly set up and has a border that doesn't look loony inside a
 * scrollpane. <P>
 * Version History: <UL>
 * <LI>0.1 [28/06/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [28/06/98]
 */
public class FixedTextArea extends JTextArea {

  public FixedTextArea() {
     this(80);
  }

  public FixedTextArea(int columns) {
     this(columns, 1);
  }

  public FixedTextArea(int columns, int rows) {
    setFont(new java.awt.Font("Courier", java.awt.Font.PLAIN, 12));
    setColumns(columns);
    setRows(rows);
    setWrapStyleWord(true);
    setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    setLineWrap(true);
  }

}