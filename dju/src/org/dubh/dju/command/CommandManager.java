// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommandManager.java,v 1.3 2001-02-11 02:52:10 briand Exp $
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

import java.util.ArrayList;

import org.dubh.dju.command.Command;
import org.dubh.dju.misc.Debug;

/**
 * A command manager is responsible for maintaining a list
 * of commands that have been invoked and providing undo / redo
 * facilities.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: CommandManager.java,v 1.3 2001-02-11 02:52:10 briand Exp $
 */
public class CommandManager
{
   private ArrayList m_commandList;
   int m_lastCommand = -1;

   public CommandManager()
   {
      m_commandList = new ArrayList();
   }

   protected void actuallyDoCommand(Command c)
   {
      c.doCommand(null);
   }

   /**
    * Instantiate a new command and execute it.
    */
   public void doCommand(Class commandClass)
   {
      try
      {
         Command c = (Command)commandClass.newInstance();
         actuallyDoCommand(c);
         if (m_lastCommand < m_commandList.size()-1)
         {
            for (int i=m_lastCommand+1; i < m_commandList.size(); i++)
            {
               m_commandList.remove(i);
            }
         }

         m_commandList.add(c);

         m_lastCommand = m_commandList.size()-1;
      }
      catch (InstantiationException ie)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Can't instantiate "+commandClass.getName()+" it is an interface or abstract class!");
            Debug.printException(1, this, ie);
         }
      }
      catch (IllegalAccessException iae)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Can't instantiate "+commandClass.getName()+". It is either public and in a different package, or the constructor is not accessible from here.");
            Debug.printException(1, this, iae);
         }

      }
   }

   /**
    * Redo. You must not call this before checking canRedo is true
    */
   public void redo()
   {
      getNextCommand().redoCommand();
      m_lastCommand++;
   }

   /**
    * Undo the last command. You must not call this before checking canUndo
    */
   public void undo()
   {
      getLastCommand().undoCommand();
      m_lastCommand--;
   }

   /**
    * Get a description of undo
    */
   public String getUndoDescription()
   {
      if (canUndo())
      {
         return getLastCommand().getUndoDescription();
      }
      return "Can't Undo";
   }

   /**
    * Can the last command be undone?
    * @return true if it can
    */
   public boolean canUndo()
   {
      Command c = getLastCommand();
      if (c==null) return false;
      return c.canUndo();

   }

   /**
    * Get a description of what redoing the next command
    * means
    */
   public String getRedoDescription()
   {
      if (canRedo())
      {
         return getNextCommand().getRedoDescription();
      }

      return "Can't Redo";
   }

   /**
    * Can the next command be redone?
    * @return true if so.
    */
   public boolean canRedo()
   {
      // Must be at least one command left on the redo list
      if (m_lastCommand == m_commandList.size()-1)
      {
         return false;
      }

      return getNextCommand().canRedo();
   }

   /**
    * Get the previous command in the command list
    */
   protected Command getLastCommand()
   {
      if (m_lastCommand >= 0)
      {
         return (Command)m_commandList.get(m_lastCommand);
      }

      return null;
   }

   /**
    * Get the next command in the command list
    */
   protected Command getNextCommand()
   {
      return (Command)m_commandList.get(m_lastCommand+1);
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