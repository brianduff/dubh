// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JMenuResource.java,v 1.4 1999-11-11 21:24:35 briand Exp $
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

import javax.swing.*;
import java.beans.*;
import java.util.*;
import org.javalobby.dju.misc.StringUtils;

/**
 * A JMenu that can be constructed from a resource file. The resource file
 * is known as a bundle, and contains key=value mappings, just like a
 * properties file. When you specify the name of a bundle in the constructor
 * of this function, the extension ".properties" is appended to it, and
 * the system searches in the CLASSPATH for the file. You can also create
 * bundles of the form bundleName_XX.properties where XX is a two letter
 * country or language code. In Java systems in that locale, the correct
 * bundle will be used if available, or the default if no locale specific
 * bundle exists.<P>
 * The format of the bundle file is as follows. Define top level menu bars
 * as: <PRE>
 *   mainmenubar=file edit help
 * </PRE>
 * Then individual menus like: <PRE>
 *   file=open save saveas - checkme - quit
 *   file.text=File
 *   file.mnemonic=F
 * </PRE>
 * And individual menu items. For these, you can specify a mnemonic (the
 * underlined letter in the menu item) and optionally an accelerator (another
 * key mapping that activates the menu item). You can also specify whether
 * the item is a checkbox menu item by prefixing a * to its label. For the
 * accelerator, the format is {modifier+modifier+...+}key, where modifier
 * can be Shift, Ctrl, Meta or Alt and key can be any single letter from
 * A to Z, or Del, F1..F12, Help
 * <PRE>
 *   open.text=Open...
 *   open.mnemonic=O
 *   open.accelerator=Ctrl+O
 *   save.text=Save
 *   save.mnemonic=S
 *   save.accelerator=Shift+Ctrl+Meta+Alt+F12
 * </PRE>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [28/06/98]: Initial Revision
 * <LI>0.2 [30/06/98]: Changed .label to .text.
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.2 [30/05/98]
 */
public class JMenuResource extends JMenu {

  /**
   * Package level constructor, intended for use by JMenuBarResource when it
   * needs to maintain the bundle (for efficiency)
   */
  JMenuResource(ResourceBundle b, String menuName, Hashtable listeners) {
     String[] itemNames = StringUtils.getWords(b.getString(menuName));
     String menuLabel, menuMnemonic;
     try {
         menuLabel = b.getString(menuName + ".text");
     } catch (MissingResourceException e) {
         menuLabel = menuName;
     }

     try {
        menuMnemonic = b.getString(menuName + ".mnemonic");
        setMnemonic(menuMnemonic.charAt(0));
     } catch (Exception e) {
     }


     //JMenu j = new JMenu(menuLabel);
     setText(menuLabel);

     for (int i = 0; i< itemNames.length; i++) {
        String itemLabel;
        if (itemNames[i].charAt(0) == '-') {
            // Item is a separator
           addSeparator();
        } else {
           // Look up and set title
            try {
              itemLabel = b.getString(itemNames[i] + ".text");
           } catch (MissingResourceException e) {
              itemLabel = itemNames[i];
           }
           // Look up and set mnemonic
           char mnemonic;
           try {
            mnemonic = b.getString(itemNames[i] + ".mnemonic").charAt(0);
           } catch (Exception e) {
            mnemonic = 0;
           }
           // Look up and set accelerator
           KeyStroke ms = null;
           String acceleratorString;
           try {
              acceleratorString = b.getString(itemNames[i] + ".accelerator");
           } catch (Exception e) {
              acceleratorString = null;
           }
           if (acceleratorString != null) {
              try {
                 ms = SpecifyableKeyStroke.getKeyStroke(acceleratorString);
              } catch (IllegalArgumentException e) {
                 ms = null;
              }
           }

           // Create the menu item
           JMenuItem mi;
           if (itemLabel.charAt(0) == '*') {
              mi = new JCheckBoxMenuItem(itemLabel.substring(1));
           } else {
              mi = new JMenuItem(itemLabel);
           }
           if (ms != null) mi.setAccelerator(ms);
           if (mnemonic != 0) mi.setMnemonic(mnemonic);

           // Hook the action into the Action object.
           mi.setActionCommand(itemNames[i]);
           if (listeners != null) {
            AbstractAction a = (AbstractAction) listeners.get(itemNames[i]);
              if (a != null) {
               mi.addActionListener(a);
                 a.addPropertyChangeListener(new ActionChangeListener(mi));
                 mi.setEnabled(a.isEnabled());
              } else {
                 // We don't know anything about this command, so disable it.
                  mi.setEnabled(false);
              }
           }
           // add item to the menu.
           add(mi);
        }   // if
     } // for     
  }

  public JMenuResource(String bundleName, String menuName, Hashtable listeners) {
     this(bundleName, Locale.getDefault(), menuName, listeners);
  }

   /**
   * Creates a localized JMenu using a properties file for the user's current
   * locale. This means that menus can be internationalized, or changed without
   * recompilation.
   @param bundleName The prefix of the bundle (properties file) containing the
   menu specification. See class comments for more information.
   @param menuName The name of the menu to construct. This should be contained
   in the bundle somewhere.
   @param listeners A hashtable of String->Action mappings. If the command
   doesn't exist in the hashtable, it will be set disabled.
   */
  public JMenuResource(String bundleName, Locale locale, String menuName, Hashtable listeners) {
     this(
        ResourceBundle.getBundle(bundleName, locale), menuName, listeners
     );

  }  // constructor

  /**
   * This class listens out for property change events on Action objects
   * and changes the menu item associated with the action accordingly.
   */
  class ActionChangeListener implements PropertyChangeListener {
     private AbstractButton myButton;

     ActionChangeListener(AbstractButton b) {
        myButton = b;
     }

     public void propertyChange(java.beans.PropertyChangeEvent e) {

        String propertyName = e.getPropertyName();
        if (e.getPropertyName().equals(Action.NAME)) {
           String text = (String) e.getNewValue();
           myButton.setText(text);
        } else if (propertyName.equals("enabled")) {
           Boolean enabledState = (Boolean) e.getNewValue();
           myButton.setEnabled(enabledState.booleanValue());
        }
     }
  }
}