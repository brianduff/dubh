// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: SpecifyableKeyStroke.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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
import java.awt.event.KeyEvent;
import java.awt.Event;
import org.dubh.dju.misc.StringUtils;

/**
 * Having browsed the source for the Swing KeyStroke class, it seems that our
 * friends at Sun didn't get round to implementing the
 * <CODE>getKeyStroke(String representation);</CODE> method.
 * This class provides an implementation of this function. It would be a
 * subclass of KeyStroke, but KeyStroke's constructor is private, so it
 * can't be subclassed. Note, Sun: it should be protected!!!!<P>
 * Version History: <UL>
 * <LI>0.1 [18/06/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [18/06/98]
 */
public class SpecifyableKeyStroke {

  public static final String SHIFT = "Shift";
  public static final String ALT   = "Alt";
  public static final String META  = "Meta";
  public static final String CTRL  = "Ctrl";
  public static final String DELETE = "Del";
  public static final String HELP   = "Help";

  /**
   * Get a KeyStroke for a string representation. The string representation
   * should be in the form: <PRE>
   *    [Modifier+Modifier+...+Modifier]+Key
   * </PRE>Modifiers are Shift, Alt, Ctrl or Meta. Key is any trivial single
   * key (A-Z) as well as some other keys (Del, Enter, F1-F12 etc.). Key should
   * <b>always</b> be the last part of the specification.
   @throws IllegalArgumentException if the representation string is dodgy.
   */
  public static KeyStroke getKeyStroke(String representation)
        throws IllegalArgumentException {
     int modifier = 0;
     int keyCode = 0;
     String[] tokens = StringUtils.getTokens(representation, "+");
     for (int i=0; i<tokens.length; i++) {
        if (tokens[i].equalsIgnoreCase(SHIFT)) {
           modifier = modifier | Event.SHIFT_MASK;

        }
        else if (tokens[i].equalsIgnoreCase(ALT)) {
           modifier = modifier | Event.ALT_MASK;

        }
        else if (tokens[i].equalsIgnoreCase(META)) {
           modifier = modifier | Event.META_MASK;

        }
        else if (tokens[i].equalsIgnoreCase(CTRL)) {
           modifier = modifier | Event.CTRL_MASK;

        }
        else if (tokens[i].length() == 1) {
           /* A single character token must be a letter. Convert it to upper
            * case and return the keystroke.
            */
           keyCode = Character.toUpperCase(tokens[i].charAt(0));
        } else {
           keyCode = keyCodeForString(tokens[i]);
        }
     }
     return KeyStroke.getKeyStroke(keyCode, modifier);
  }

  private static int keyCodeForString(String str)
     throws IllegalArgumentException {
     if (str.equalsIgnoreCase(HELP))
        return KeyEvent.VK_HELP;
     if (str.equalsIgnoreCase(DELETE))
        return KeyEvent.VK_DELETE;
     if (str.charAt(0) == 'F') {
        // string must have > 1 character, so this assumption is ok
        switch (str.charAt(1)) {
           case '1':
              if (str.length() == 2)
                 return KeyEvent.VK_F1;
              switch(str.charAt(2)) {
                 case '0': return KeyEvent.VK_F10;
                 case '1': return KeyEvent.VK_F11;
                 case '2': return KeyEvent.VK_F12;
              }
           case '2': return KeyEvent.VK_F2;
           case '3': return KeyEvent.VK_F3;
           case '4': return KeyEvent.VK_F4;
           case '5': return KeyEvent.VK_F5;
           case '6': return KeyEvent.VK_F6;
           case '7': return KeyEvent.VK_F7;
           case '8': return KeyEvent.VK_F8;
           case '9': return KeyEvent.VK_F9;
        }
     }
     throw new IllegalArgumentException(str);
  }

}