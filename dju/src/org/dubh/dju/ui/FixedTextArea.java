// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FixedTextArea.java,v 1.3 1999-03-22 23:37:18 briand Exp $
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