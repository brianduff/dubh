// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: CommanderCommands.java,v 1.2 1999-11-11 21:24:36 briand Exp $
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
package org.javalobby.dju.ui.commander;

import org.javalobby.dju.misc.ResourceManager;
import org.javalobby.dju.misc.UserPreferences;
import org.javalobby.dju.misc.Debug;
import org.javalobby.dju.misc.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.Box;

/**
 * The CommanderCommands object contains information about all available
 * action commands available to an application. This is usually initialised
 * from a ResourceManager.
 * @author Brian Duff
 * @version $Id: CommanderCommands.java,v 1.2 1999-11-11 21:24:36 briand Exp $
 *
 */
public class CommanderCommands
{   
   private static final String s_PREFSFILE = System.getProperty("user.home")+
      File.separator+"dubh_commander.properties";

   private static final String 
      COMMANDER_MENUBARS = "commander.menubar",
      COMMANDER_TOOLBARS = "commander.toolbar",
      COMMANDER_MENUS    = "commander.menu",
      COMMANDER_COMMANDS = "commander.commands,
      VALUE_SEPARATOR    = "-";
   

   private ResourceManager m_appResources;
   private String          m_appKey;
   private UserPreferences m_commanderProperties;
   
   private String          m_appCommanderMenus;
   private String          m_appCommanderToolbars;
   private String          m_appCommanderAddins;
   private String          m_appMenu;

   private Hashtable       m_commands;
   private Hashtable       m_menus;
   
   private Hashtable       m_toolBars;
   private Hashtable       m_menuBars;

   /**
    * Construct a commander commands object for your application. The resource
    * manager should point to a set of resources with the following
    * mandatory properties:
    * <p>
    * <table><tr>
    * <th>Key</th><th>Description</th>
    * </tr><tr>
    * <td>commander.menubar.Menu</td>
    * <td>
    *     A list of keys in your resource file that are taken to be menus
    *     in your application. <p>
    *     You must define a key for each item specifying its properties
    *     (e.g. file.text = "File" file.mnemonic=F) <p>
    *     You must also define the default items for each menu, in the form
    *     <menu name> = <item> <item> <item> where items are actions defined
    *     as described above. You can use the - character to indicate a 
    *     separator (e.g. file = fileOpen fileClose - fileSave)
    * </td></tr>
    * <td>commander.toolbar.Tool</td>
    * <td>
    *     Toolbars for your application
    * </td></tr>
    * </table></p>
    * You may also want to define several keys for images. See the NewsAgent
    * CommanderRes.java file for an example.
    * @param r      your resource manager
    * @param appKey a unique id for your app. This is used to store any
    *   changes the user makes to the command structure of your application
    *   in the dubh_commander.properties file.
    */
   public CommanderCommands(ResourceManager r, String appKey)
   {
      m_commands = new Hashtable();
      m_menus    = new Hashtable();
      m_menuBars = new Hashtable();
      m_toolBars = new Hashtable();
      m_appResources = r;
      m_appKey = appKey;
      try
      {
         m_commanderProperties = new UserPreferences(s_PREFSFILE);
      }
      catch (IOException ioe)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Unable to construct commander properties");
            Debug.printException(1, this, ioe);
         }
      }
      //
      // These are keys into the commander properties file for
      // customised menus & toolbars for this application
      //
      m_appCommanderMenus = m_appKey+"."+COMMANDER_MENUS;
      m_appCommanderToolbars = m_appKey+"."+COMMANDER_TOOLBARS;
      m_appMenu = m_appKey+".menu.";     
      
     // initProperties();
      
      createCommands();
      
   }
   
   /**
    * Get a command. If the command doesn't exist, this method will
    * return null.
    */
   public Command getCommand(String name)
   {
      return (Command)m_commands.get(name);
   }


   /**
    * Get a (potentially cusomised) toolbar
    */
   public JToolBar getToolBar(String id)
   {
      JToolBar bar = (JToolBar) m_toolBars.get(id);
      if (bar == null)
         bar = createToolBar(id);
         
      return bar;
   }
   
   /**
    * Get a menu bar
    */
   public JMenuBar getMenuBar(String id)
   {
      JMenuBar bar = (JMenuBar) m_menuBars.get(id);
      
      if (bar == null)
         bar = createMenuBar(id);
         
      return bar;

   }
   
   /**
    * Get a popup menu
    */
   public JPopupMenu getPopupMenu(String popupKey)
   {
      return getMenu(popupKey).getPopupMenu();
   }
   
   /**
    * Get a menu identified by a key.
    */
   public JMenu getMenu(String menuKey)
   {
      JMenu m = (JMenu) m_menus.get(menuKey);
      
      if (m == null)
      {
         return createMenu(menuKey);
      }
      
      return m;
   }
   
   private JMenu createMenu(String menuKey)
   {
      JMenu m = new JMenu();
      
      m_appResources.doComponent(menuKey, m);
      
      String[] items = m_commanderProperties.getStringArrayPreference(
         m_appMenu+menuKey
      );
      
      for (int i=0; i < items.length; i++)
      {
         if (items[i].equals("-"))
         {
            m.addSeparator();
         }
         else
         {
            m.add(getCommand(items[i]).getMenuItem());
         }
      }
      
      m_menus.put(menuKey, m);
      
      return m;
      
   }

   //
   // Create the menu bar
   //
   private JMenuBar createMenuBar(String id)
   {
      JMenuBar m = new JMenuBar();
      String[] menus;
      try
      {
        menus = m_commanderProperties.getStringArrayPreference(
           COMMANDER_MENUS+"."+id, null);
      }
      catch (Exception e)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "No "+COMMANDER_MENUS+" key in commander properties");
            Debug.printException(1, this, e);
         }
         return null;
      } 
      
      for (int i=0; i < menus.length; i++)
      {
         m.add(getMenu(menus[i]));
      }
      
      m_menuBars.put(id, m);
      
      return m;
   }

   //
   // Create a toolbar
   //
   private JToolBar createToolBar(String id)
   {
      JToolBar b = new JToolBar();
   
      String[] itemNames =  m_commanderProperties.getStringArrayPreference(
         m_appCommanderToolbars+"."+id
      );

      for (int i = 0; i < itemNames.length; i++) 
      {
         if (itemNames[i].equals(VALUE_SEPARATOR)) 
         {
            b.add(Box.createHorizontalStrut(5));
         } else {
            b.add(getCommand(itemNames[i]).getButton());
         }
      }
      
      b.add(Box.createHorizontalGlue());
      
      return b;
   }

   //
   // Create all commands for this commander
   //
   private void createCommands()
   {
      m_commands = new Hashtable();
      String cmdList = m_appResources.getString(COMMANDER_COMMANDS);
      
      if (cmdList == null)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, 
               "No "+COMMANDER_COMMANDS+" key specified in commander bundle."
            );
         }
         return;
      }   
      
      String allCommands[] = StringUtils.getWords(cmdList);
      
      for (int i=0; i < allCommands.length; i++)
      {
         m_commands.put(allCommands[i], new Command(
            m_appResources, allCommands[i]
         ));
      }
      
   }
   
   //
   // If we don't already have preferences for our toolbar and
   // menus, read in the defaults and set our preferences.
   //
   private void initProperties()
   {
      boolean mustSave = false;
      //
      // Check if already set up
      //
      String menus = m_commanderProperties.getPreference(
         m_appCommanderMenus, null
      );
      
      if (menus == null)
      {
         menus = m_appResources.getString(COMMANDER_MENUS);
         m_commanderProperties.setPreference(m_appCommanderMenus,
            menus
         );
         
         // Copy each menu across
         
         String[] allmenu = StringUtils.getWords(menus);
         
         for (int i=0; i < allmenu.length; i++)
         {
            String current = m_commanderProperties.getPreference(
               m_appCommanderMenus+"."+allmenu[i], null
            );
            
            if (current == null)
               m_commanderProperties.setPreference(
                  m_appCommanderMenus+"."+allmenu[i], 
                  m_appResources.getString(COMMANDER_MENUS+"."+allmenu[i])
               );
         }
         
         mustSave = true;
      }
      
      String tb    = m_commanderProperties.getPreference(
         m_appCommanderToolbars, null
      );
      
      if (tb == null)
      {
         tb = m_appResources.getString(COMMANDER_TOOLBARS);
         m_commanderProperties.setPreference(m_appCommanderToolbars,
            tb
         );
         
         // Copy each toolbar across
         
         String[] tools = StringUtils.getWords(tb);
         
         for (int i=0; i < tools.length; i++)
         {
            String current = m_commanderProperties.getPreference(
               m_appCommanderToolbars+"."+tools[i], null
            );
            
            if (current == null)
               m_commanderProperties.setPreference(
                  m_appCommanderToolbars+"."+tools[i], 
                  m_appResources.getString(COMMANDER_TOOLBARS+"."+tools[i])
               );
         }         
         mustSave = true;
      }  
      
      //
      // Get all menu bars and save them.
      //
      String[] allMenus = StringUtils.getWords(menus);
      
      for (int i=0; i < allMenus.length; i++)
      {
         String thismenu = m_commanderProperties.getPreference(
            m_appMenu+allMenus[i], null
         );
         
         if (thismenu == null)
         {
            thismenu = m_appResources.getString(allMenus[i]);
            m_commanderProperties.setPreference(
               m_appMenu+allMenus[i], thismenu
            );
            mustSave = true;
         }
      }
      
      
      if (mustSave)
      {
         try
         {
            m_commanderProperties.save();
         }
         catch (IOException ioe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "Can't save commander properties");
               Debug.printException(1, this, ioe);
            }
         }
      }
   }

   
   /**
    * Retrieve a string property. First attempts to get it from the
    * dubh utilities customised UserPreferences. Failing that, the
    * applications ResourceManager is used. This method should be used
    * to load in any properties that can potentially be customised.
    */
   private String getStringProperty(String custKey, String resKey, String id)
   {
      String res = m_commanderProperties.getPreference(
         custKey+"."+id, null
      );
      
      if (res == null)
      {
         try
         {
            res = m_appResources.getString(resKey+"."+id);
         }
         catch (MissingResourceException mse)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, 
                  "Unable to retrieve "+resKey+"."+id+" from application resource file.");
            }
            
            res = "";
         }
      }
      
      return res;
         
   }
   
   //
   // Customisation. The following can be customised at runtime and will
   // remain persitently customised for subsequent sessions:
   //
   //  o Add / Remove custom commands.
   //  o Add / Remove custom menu bars.
   //  o Add / Remove custom tool bars
   //  o Add / Remove commands from menus
   //  o Add / Remove menus from menu bars
   //  o Add / Remove commands from toolbars
   
   
   /**
    * Adds a custom command to the commander. This command will not be
    * available in any menus or toolbars by default, you will have to
    * use {@link #addCommandToMenu(...)} or {@link #addCommandToToolBar(...)}
    * to make the command available.
    * You can't add a command with the same id as an existing custom command
    * or application command.
    */
   public void addCustomCommand(Command c)
   {
   
   }
   
   /**
    * Removes a custom command from the commander. You can't remove
    * application commands, only commands previously added to the 
    * commander with {@link #addCustomCommand(Command)}. The command
    * will automatically be removed from any menus or toolbars in which
    * it is currently being displayed.
    */
   public void removeCustomCommand(String id)
   {
      
   }
   
   public void add
   
   public void removeCustomCommand
   
   public static void main(String[] args)
   {
      
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/03/22 23:33:04  briand
// New menu / toolbar utilties (doesn't compile yet)
//
//