// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommanderCommand.java,v 1.2 1999-11-11 21:24:36 briand Exp $
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
package org.javalobby.dju.ui.commander;

import org.javalobby.dju.misc.ResourceManager;
import org.javalobby.dju.misc.UserPreferences;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * A command is any action in your application that can be invoked from
 * a toolbar or menu.
 * @author Brian Duff
 * @version $Id: CommanderCommand.java,v 1.2 1999-11-11 21:24:36 briand Exp $
 *
 */
public class CommanderCommand
{   
   private String          m_name;
   private ResourceManager m_res;
   private JButton         m_button;
   private JMenuItem       m_menuItem;
   private Action          m_action;
   private ActionChangeListener m_listener;
   
   
   public CommanderCommand(ResourceManager m, String cmdName)
   {
      m_name = cmdName;
      m_res  = m;
      m_action = null;
      m_button = null;
      m_action = null;
   }
   
   /**
    * Retrieve a button suitable for a toolbar corresponding to this
    * command. When the button is clicked, the command is automatically
    * fired.
    */
   public JButton getButton()
   {
      if (m_button == null)
      {
         createButton();
      }
      return m_button;
   }
   
   public JMenuItem getMenuItem()
   {
      if (m_menuItem == null)
      {
         createMenuItem();
      }
      
      return m_menuItem;
   }
   
   /**
    * Set the action that is performed.
    */
   public void setAction(Action a)
   {
      m_button.setActionCommand(m_name);
      if (m_action != null)
         m_button.removeActionListener(m_action);
      m_button.addActionListener(a);
      m_button.setEnabled(a.isEnabled());
      m_action = a;
      if (m_listener == null)
      {
         m_listener = new ActionChangeListener(m_button);
      }
      
      try
      {
         m_action.removePropertyChangeListener(m_listener);
      }
      catch (Throwable t)
      {
      }
      
      m_action.addPropertyChangeListener(m_listener);
   }
   
   public void doAction()
   {
      if (m_action != null)
      {
         ActionEvent ace = new ActionEvent(this, 
            ActionEvent.ACTION_PERFORMED, m_name
         );
         m_action.actionPerformed(ace);
      }
   }
   
   public void setEnabled(boolean b)
   {
      if (m_action != null)
         m_action.setEnabled(b);   
   }  
   
   public boolean isEnabled()
   {
      if (m_action == null) return false;
      return m_action.isEnabled();
   }
   
   private void createButton()
   {
      m_button = new JButton() {
         public float getAlignmentY() { return 0.5f; }
      };
      m_button.setRequestFocusEnabled(false);
      
      m_button.setMargin(new Insets(1,1,1,1));

      // Use the ResourceManager to initialise the button.
      m_res.doComponent(m_name, m_button);
      m_button.setText(""); // this should really be customizable
   }
   
   
   private void createMenuItem()
   {
      m_menuItem = new JMenuItem();
      m_res.doComponent(m_name, m_menuItem);
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

     public void propertyChange(PropertyChangeEvent e) {

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