// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorCommand.java,v 1.1 1999-10-17 17:03:58 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
package dubh.apps.newsagent.navigator;


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
 * @version $Id: NavigatorCommand.java,v 1.1 1999-10-17 17:03:58 briand Exp $
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
//