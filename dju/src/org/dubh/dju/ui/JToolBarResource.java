// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JToolBarResource.java,v 1.4 1999-11-11 21:24:36 briand Exp $
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
import java.util.*;
import org.javalobby.dju.misc.*;
import java.awt.*;
import java.beans.*;

/**
 * A JToolBar that can be constructed from a localized resource file. See
 * JMenuResource for more information.
 * @see JMenuResource
 *<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [30/06/98]: Initial Revision
 * <LI>0.2 [26/01/99]: Currently broken, because of changes to
 *    ResourceManager. Will fix before release.
 * <LI>0.3 [07/03/99]: Fixed!
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.3
 */
public class JToolBarResource extends JToolBar {

  private ResourceManager m;

  JToolBarResource(ResourceBundle b, String resourceName, Hashtable listeners) {
     m = new ResourceManager(b);
     String[] itemNames = StringUtils.getWords(b.getString(resourceName));
     for (int i = 0; i < itemNames.length; i++) {
        if (itemNames[i].equals("-")) {
           super.add(Box.createHorizontalStrut(5));
        } else {
           super.add(createToolBarButton(itemNames[i], listeners));
        }
     }
     super.add(Box.createHorizontalGlue());
  }


  public JToolBarResource(String bundleName, String resourceName, Hashtable listeners) {
     this(bundleName, Locale.getDefault(), resourceName, listeners);
  }


  public JToolBarResource(String bundleName, Locale locale, String resourceName, Hashtable listeners) {
     this(
        ResourceBundle.getBundle(bundleName, locale), resourceName, listeners
     );

  }  // constructor

  protected JButton createToolBarButton(String itemName, Hashtable listeners) {
     
     JButton b = new JButton() {
        public float getAlignmentY() { return 0.5f; }
     };
     b.setRequestFocusEnabled(false);
     b.setMargin(new Insets(1,1,1,1));

     // Use the ResourceManager to initialise the button.
     m.doComponent(itemName, b);
     b.setText(""); // this should really be customizable
     // Get the action and bind it to the button
     Action a = (Action) listeners.get(itemName);
     if (a != null) {
         b.setActionCommand(itemName);
         b.addActionListener(a);
         a.addPropertyChangeListener(new ActionChangeListener(b));
         b.setEnabled(a.isEnabled());
     } else {
         b.setEnabled(false); // The button is unknown
     }
     return b;
  }

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