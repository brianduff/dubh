// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommanderCommand.java,v 1.1 1999-03-22 23:33:04 briand Exp $
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
package dubh.utils.ui.commander;

import dubh.utils.misc.ResourceManager;
import dubh.utils.misc.UserPreferences;

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
 * @version $Id: CommanderCommand.java,v 1.1 1999-03-22 23:33:04 briand Exp $
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