// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractCommand.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
package org.javalobby.dju.command;

import java.io.IOException;
import java.util.MissingResourceException;
import java.net.URL;

import org.javalobby.dju.misc.ResourceManager;
import org.javalobby.dju.misc.Debug;

import javax.swing.Icon;
import javax.swing.ImageIcon;



/**
 * A basic implementation of the Command interface.
 * The command is associated with a ResourceManager that 
 * provides language independent descriptions of the command.
 *
 * The keys for the resourcemanager are:<pre>
 *    command.<i>commandName</i>.niceName= Nice name of command
 *    command.<i>commandName</i>.description = Description of command
 *    command.<i>commandName</i>.mnemonic = Mnemonic of command
 *    command.<i>commandName</i>.icon = Icon file name
 *    command.<i>commandName</i>.undoDescription = Undo description
 *    command.<i>commandName</i>.redoDescription = Redo description
 * </pre>
 *
 * Before the command is used, you must use the static init method
 * to specify a resource manager and name for the command.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: AbstractCommand.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 */
public abstract class AbstractCommand implements Command
{
   public static final String NICE_NAME = "niceName";
   public static final String DESCRIPTION = "description";
   public static final String MNEMONIC = "mnemonic";
   public static final String ICON = "icon";
   public static final String UNDO_DESCRIPTION = "undoDescription";
   public static final String REDO_DESCRIPTION = "redoDescription";
   
   private boolean m_bEnabled = true;
   
   /**
    * Get the resource manager that contains resources for this
    * command. You must not return null from this method.
    */
   protected abstract ResourceManager getResourceManager();
   /**
    * Get the name of this command. You must not return null from this
    * method.
    */
   public abstract String getName();
   
   /**
    * Get a resource from the resource manager associated with this
    * command. 
    * @param key The resource key. Cannot be null.
    * @param def The default value if the key doesn't exist. May be null.
    * @return The resource value if it exists, or the default value.
    * @throws java.lang.IllegalStateException if the command hasn't been initialized.
    */
   protected String getRes(String key, String def)
   {
      if (getResourceManager() == null)
      {
         throw new IllegalStateException("Command "+getClass().getName()+" does not have a resource manager.");
      }
      
      try
      {
         String fullKey = "command."+getName()+"."+key;
         return getResourceManager().getString(fullKey);
      }
      catch (MissingResourceException mre)
      {
         return def;
      }   
   }

   /**
    * This is the name used to display your command to the user
    */
   public String getNiceName()
   {
      return getRes(NICE_NAME, getName());
   }
   /**
    * This is the long description used in tooltips etc.
    */
   public String getDescription()
   {
      return getRes(DESCRIPTION, getName());
   }
   /**
    * This is the mnemonic character for your command. It should
    * be a character that exists somewhere in the command nice name
    */
   public char getMnemonic()
   {
      return getRes(MNEMONIC, " ").charAt(0);
   }
   /**
    * This is the icon for your command. The use of this icon
    * depends on how the command is represented in the UI, but this
    * is typically for toolbars or menu items.
    * @return an icon. This may be null if the icon resource 
    * doesn't exist on the CLASSPATH. Your icon will never be
    * displayed if this method returns null.
    */
   public Icon getIcon()
   {
      String fname = getRes(ICON, null);
      
      if (fname == null) 
         return null;
      
      URL icoURL = ClassLoader.getSystemResource(fname);
      
      if (icoURL != null)
      {
         return new ImageIcon(icoURL);
      }
      
      if (Debug.TRACE_LEVEL_1)
      {
         Debug.println(1, this, "Should really return a default icon here.");
      }
      
      return null;
   }
   
   /**
    * Is your command currently enabled? All commands are initially
    * enabled.
    */
   public boolean isEnabled()
   {
      return m_bEnabled;
   }
   
   /**
    * Makes your command enabled or disabled.
    */
   public void setEnabled(boolean b)
   {
      m_bEnabled = b;
   }
   
   /**
    * Carry out your command on the specified object. The object
    * is usually the current selection in the context that your
    * command has been invoked.
    */
   public abstract void doCommand(Object target);
   
   /**
    * Undoes the command. You can assume doCommand has been called
    * before this method.
    */
   public abstract void undoCommand();
   /**
    * Redoes the command. You can assume doCommand and undoCommand
    * have been called before this method is called.
    */
   public abstract void redoCommand();
   /**
    * You should return true or false depending on whether your
    * command can be undone. If you return false, your command
    * instance will most likely be unreferenced and eventually
    * garbage collected.
    */
   public abstract boolean canUndo();
   /**
    * You should return true or false depending on whetehr your
    * command can be redone. If you return false and your
    * command has been undone, references to your command
    * will probably be removed and your instance will be 
    * garbage collected.
    */
   public abstract boolean canRedo();
   /**
    * Get a description of undoing the command. This would typically
    * be displayed in the edit menu. You do not need to include
    * the word undo. E.g. your string might be "delete server 'wired'"
    * and would appear as Undo delete server 'wired'.
    */
   public String getUndoDescription()
   {
      return getRes(UNDO_DESCRIPTION, getName());
   }
   /**
    * Get a description of redoing the command. This would typically
    * be displayed in the edit menu. You do not need to include
    * the word redo in your description.
    */
   public String getRedoDescription()
   {
      return getRes(REDO_DESCRIPTION, getName());
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/24 00:38:17  briand
// New Command mechanism.
//
//