// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorCommand.java,v 1.2 1999-11-09 22:34:42 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
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
package org.javalobby.apps.newsagent.navigator;


/**
 * A navigator command is a command that can be performed on
 * a selected item in the navigator.
 * 
 * Commands are often stored in a command list and can be 
 * undone. If your command is not undoable, you should 
 * return false from the isUndoable() method.
 *
 * BD: This is very general, could probably be in dju.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorCommand.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public interface NavigatorCommand  
{  
   /**
    * Get the name of your command. This is an identifier with no spaces
    */
   public String getCommandName();
   
   /**
    * Get the nice name of your command. This is the name that will
    * appear in menus etc. and should be internationalized.
    */
   public String getCommandNiceName();
   
   /**
    * Get a longer description of what the command does. This will be
    * displayed to the user and should be internationalized.
    */
   public String getCommandDescription(); 
   
   /**
    * Perform the command on the specified object.
    */
   public void doCommand(Object selectedObject); 

   /**
    * Undo the command on the specified object. This will only be called
    * if your command is undoable.
    */
   public void undoCommand(Object selectedObject);

   /**
    * If your command can be undone, you should return true from this method.
    * Otherwise, return false.
    */
   public boolean isUndoable();
}



//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//