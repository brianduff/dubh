// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ActionCommand.java,v 1.3 2001-02-11 02:52:10 briand Exp $
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

package org.dubh.dju.command;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.dubh.dju.misc.Debug;
/**
 * An action command wraps up one of DJU's undoable command
 * objects into a Swing Action object that can be used in buttons,
 * menu items, toolbars etc.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: ActionCommand.java,v 1.3 2001-02-11 02:52:10 briand Exp $
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
    *   the org.dubh.dju.command.Command interface.
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
// Revision 1.2  1999/11/11 21:24:34  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/10/24 00:38:17  briand
// New Command mechanism.
//
//