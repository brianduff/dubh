// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ActionCommand.java,v 1.1 1999-10-24 00:38:17 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
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
package dubh.utils.command;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.Icon;

import dubh.utils.misc.Debug;
/**
 * An action command wraps up one of DJU's undoable command
 * objects into a Swing Action object that can be used in buttons,
 * menu items, toolbars etc.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: ActionCommand.java,v 1.1 1999-10-24 00:38:17 briand Exp $
 */
public class ActionCommand extends AbstractAction
{
   protected Class m_commandClass;
   protected Command m_referenceCommand;
   protected CommandManager m_commandManager;
   
   /**
    * Create an ActionCommand with the specified command
    * manager.
    * @param commandClass the class of an object implementing
    *   the dubh.utils.command.Command interface.
    * @param mgr A command manager
    */
   public ActionCommand(Class commandClass, CommandManager mgr)
   {
      m_commandClass = commandClass;
      m_commandManager = mgr;
      m_referenceCommand = getCommand();
      
      putValue(Action.LONG_DESCRIPTION, m_referenceCommand.getDescription());
      putValue(Action.NAME, m_referenceCommand.getNiceName());
      putValue(Action.SHORT_DESCRIPTION, m_referenceCommand.getNiceName());
      Icon i = m_referenceCommand.getIcon();
      if (i != null)
      {
         putValue(Action.SMALL_ICON, i);
      }
   }
      
   /**
    * Make a new instance of the command associated with this
    * action
    */
   protected Command getCommand()
   {
      try
      {
         return (Command)m_commandClass.newInstance();
      }
      catch (InstantiationException ie)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Can't instantiate "+m_commandClass.getName()+" it is an interface or abstract class!");
            Debug.printException(1, this, ie);
         }
      }
      catch (IllegalAccessException iae)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Can't instantiate "+m_commandClass.getName()+". It is either public and in a different package, or the constructor is not accessible from here.");
            Debug.printException(1, this, iae);
         }
      
      }    
      
      return null;   
   } 
   
   public boolean isEnabled()
   {
      return m_referenceCommand.isEnabled();
   }
   
   public void setEnabled(boolean b)
   {
      m_referenceCommand.setEnabled(b);
   }
   
   /**
    * Perform an action. A new command will be instantiated. The
    * command will be performed in the command manager with the action
    * event's source object as the target.
    */
   public void actionPerformed(ActionEvent e)
   {
      if (Debug.TRACE_LEVEL_3)
      {
         Debug.println("Doing command "+m_referenceCommand.getName());
      }
      m_commandManager.doCommand(m_commandClass);
   }
}

//
// $Log: not supported by cvs2svn $
//