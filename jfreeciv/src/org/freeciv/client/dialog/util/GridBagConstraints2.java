// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: GridBagConstraints2.java,v 1.2 2001-07-08 01:36:51 briand Exp $
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
package org.freeciv.client.dialog.util;
import java.awt.*;
/**
 * Provides a simpler way to initialise a GridBagConstaints object. (from an
 * original Borland JBuilder class)<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [07/06/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [07/06/98]
 */
public class GridBagConstraints2 extends GridBagConstraints
{
  public GridBagConstraints2( int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady ) 
  {
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridheight;
    this.weightx = weightx;
    this.weighty = weighty;
    this.anchor = anchor;
    this.fill = fill;
    this.insets = insets;
    this.ipadx = ipadx;
    this.ipady = ipady;
  }
}
