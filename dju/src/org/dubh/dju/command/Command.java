// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Command.java,v 1.3 2001-02-11 02:52:10 briand Exp $
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

import javax.swing.Icon;

/**
 * A command is any operation that can be performed in an
 * application. An instance of your command class is instantiated
 * each time the command is performed. Commands are stored up to
 * a particular undo limit and may be undone or redone.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Command.java,v 1.3 2001-02-11 02:52:10 briand Exp $
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
// Revision 1.2  1999/11/11 21:24:34  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/10/24 00:38:17  briand
// New Command mechanism.
//
//