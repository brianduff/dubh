// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JTextFieldFixedHeight.java,v 1.2 1999-11-11 21:24:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package org.javalobby.dju.ui;

import java.awt.*;
import javax.swing.*;

/**
 * A JTextField that has a fixed height and doesn't expand veritcally.
 * This makes it usable with vertical BoxLayouts.
 *
 * @author Brian Duff
 * @version $Id: JTextFieldFixedHeight.java,v 1.2 1999-11-11 21:24:35 briand Exp $
 */
public class JTextFieldFixedHeight extends JTextField 
{
   public JTextFieldFixedHeight()
   {
      super();
      setMaximumSize(new Dimension(getMaximumSize().width, getPreferredSize().height));
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/06/01 00:17:34  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//