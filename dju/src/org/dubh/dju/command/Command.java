// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Command.java,v 1.1 1999-10-24 00:38:17 briand Exp $
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

import javax.swing.Icon;

/**
 * A command is any operation that can be performed in an
 * application. An instance of your command class is instantiated
 * each time the command is performed. Commands are stored up to
 * a particular undo limit and may be undone or redone.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Command.java,v 1.1 1999-10-24 00:38:17 briand Exp $
 */
public interface Command
{
   /**
    * This is the internal name of your command. It should contain
    * no spaces.
    */
   public String getName();
   /**
    * This is the name used to display your command to the user
    */
   public String getNiceName();
   /**
    * This is the long description used in tooltips etc.
    */
   public String getDescription();
   /**
    * This is the mnemonic character for your command. It should
    * be a character that exists somewhere in the command nice name
    */
   public char getMnemonic();
   /**
    * This is the icon for your command. The use of this icon
    * depends on how the command is represented in the UI, but this
    * is typically for toolbars or menu items.
    */
   public Icon getIcon();
   
   /**
    * Is your command currently enabled?
    */
   public boolean isEnabled();
   
   /**
    * Makes your command enabled or disabled.
    */
   public void setEnabled(boolean b);
   
   /**
    * Carry out your command on the specified object. The object
    * is usually the current selection in the context that your
    * command has been invoked.
    */
   public void doCommand(Object target);
   
   /**
    * Undoes the command. You can assume doCommand has been called
    * before this method.
    */
   public void undoCommand();
   /**
    * Redoes the command. You can assume doCommand and undoCommand
    * have been called before this method is called.
    */
   public void redoCommand();
   /**
    * You should return true or false depending on whether your
    * command can be undone. If you return false, your command
    * instance will most likely be unreferenced and eventually
    * garbage collected.
    */
   public boolean canUndo();
   /**
    * You should return true or false depending on whetehr your
    * command can be redone. If you return false and your
    * command has been undone, references to your command
    * will probably be removed and your instance will be 
    * garbage collected.
    */
   public boolean canRedo();
   /**
    * Get a description of undoing the command. This would typically
    * be displayed in the edit menu. You do not need to include
    * the word undo. E.g. your string might be "delete server 'wired'"
    * and would appear as Undo delete server 'wired'.
    */
   public String getUndoDescription();
   /**
    * Get a description of redoing the command. This would typically
    * be displayed in the edit menu. You do not need to include
    * the word redo in your description.
    */
   public String getRedoDescription();
}

//
// $Log: not supported by cvs2svn $
//