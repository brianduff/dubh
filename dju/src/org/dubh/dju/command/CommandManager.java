// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommandManager.java,v 1.1 1999-10-24 00:38:17 briand Exp $
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

import java.util.ArrayList;

import dubh.utils.command.Command;
import dubh.utils.misc.Debug;

/**
 * A command manager is responsible for maintaining a list 
 * of commands that have been invoked and providing undo / redo
 * facilities.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: CommandManager.java,v 1.1 1999-10-24 00:38:17 briand Exp $
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
//