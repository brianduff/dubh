// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommanderCommandList.java,v 1.1 1999-03-22 23:33:04 briand Exp $
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
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/**
 * A command list is a list of commands that can be modified. You can't
 * instantiate CommanderCommandList directly; instead, use one of its 
 * subclasses, {@link CommnanderMenu} or {@link CommanderToolbar}.
 * N.b. This class uses the new Java 1.2 collections framework and is
 * not thread safe.
 * @author Brian Duff
 * @since DJU 1.1.0
 */
abstract class CommanderCommandList
{   
   ResourceManager m_appResources;
   UserPreferences m_customisedResources;
   String          m_appId;
   ArrayList       m_items;
   String          m_listKey;
   CommanderCommands m_coco;
   
   /**
    * Construct a list. Commands are automatically added to this
    * list as specified in either the customised or application
    * resources.
    */
   CommanderCommandList(ResourceManager appRes, 
                        UserPreferences custRes,
                        String myKey,
                        CommanderCommands coco)
   {
      m_appResources = appRes;
      m_customisedResources = custRes;
      m_items        = new ArrayList();
      m_listKey      = myKey;
      m_coco         = coco;
      
      initList();
   }
   
   /**
    * Create the list according to information from the resources.
    */
   void initList()
   {
      //
      // First, see if there is a customised version of this list in
      // the customised resource file.
      //
      
      String lst = m_customisedResources.getPreference(
         myKey, null
      );
      
      if (lst == null)
      {
         //
         // If not, look in the application resource file
         //
         try
         {
            lst = m_appResources.getString(
               myKey
            );
         }
         catch (MissingResourceException mse)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.print(1, this, 
                  "Unable to retrieve menu resource "+myKey+": "+mse
               );
               Debug.printException(1, this, mse);
               
            }
            return;
         }
      }
      //
      // Now initialise the list with commands
      //
      String[] commands = StringUtils.getWords(lst);
      
      for (int i=0; i < commands.length; i++)
      {
         if (commands[i].charAt(0)=='-')
         {
            addSeparator();
         }
         else
         {
            addCommand(m_coco.getCommand(commands[i]));
         }
      }
   }
   
   /**
    * Write out the list contents in a suitable format for inclusion in
    * a .properties file.
    */
   public String toString()
   {
      int i=0;
      StringBuffer sb = new StringBuffer(); 
      //
      // This is an optimised for loop.
      //
      try
      {
         while (true)
         {
            Object o = m_items.get(i++);
            if (i != 1) sb.append(" ");
            if (o instanceof CommanderCommand)
            {
               sb.append(((CommanderCommand)o).getId());
            }
            else
            {
               // Must be a separator
               sb.append("-");
            }
         }
      }
      catch (IndexOutOfBoundsException ioobe)
      {
         // This is how we fall out of the loop.
      }
      
      return sb.toString();

   }
   
   ResourceManager getAppResources()
   {
      return m_appResources;
   }
   
   UserPreferences getCustomResources()
   {
      return m_customisedResources;
   }
   
   /**
    * Add a command to the list. This command will be added at the
    * end of the list. 
    */
   public void addCommand(CommanderCommand c)
   {
      m_items.add(c);
   }
   
   public void removeCommand(CommanderCommand c)
   {
      m_items.remove(indexOf(c));
   }
   
   public void insertCommand(int pos, CommanderCommand c)
   {
      m_items.add(pos, c);
   }
   
   public void insertSeparator(int pos)
   {
      m_items.add(pos, "-");
   }
   
   public void addSeparator()
   {
      m_items.add("-");
   }
   
   /**
    * Save preferences associated with this list.
    */
   public void save()
      throws IOException
   {
      m_customisedResources.save();
   }
   
   /**
    * Revert preferences associated with this list
    */
   public void revert()
      throws IOException
   {
      
      m_customisedResources.revert();
      
      m_items.clear();
      initList();
   }
   

}