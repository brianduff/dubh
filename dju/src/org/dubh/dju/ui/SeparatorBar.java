// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: SeparatorBar.java,v 1.3 2001-02-11 02:52:12 briand Exp $
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

import java.awt.*;
import javax.swing.*;

/**
 * A UI component that displays a label and a horizontal etched bar. Can
 * be used to separate groups of components in a dialog (similar to Office 97
 * preference dialogs)
 *
 * @author Brian Duff
 * @version $Id
 */
public class SeparatorBar extends JLabel
{
   private final static int PADDING=5;


   public SeparatorBar(String s)
   {
     super(s);
     setOpaque(false);
   }

   public SeparatorBar()
   {
     super();
     setOpaque(false);
   }

   public void paint(Graphics g)
   {
     int startPos = g.getFontMetrics().stringWidth(getText())+PADDING;

     g.setColor(SystemColor.controlLtHighlight);
     g.drawLine(startPos,getSize().height/2+1,getSize().width, getSize().height/2+1);
     g.setColor(SystemColor.controlDkShadow);
     g.drawLine(startPos,(getSize().height/2),getSize().width, (getSize().height/2));
     super.paint(g);
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/11/11 21:24:36  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/06/01 00:17:34  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//