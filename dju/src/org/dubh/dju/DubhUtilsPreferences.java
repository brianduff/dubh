/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Revision History:
 *   File  DJU  Date       Who    Changes
 *   ----  ---  ----       ---    -------
 */
package dubh.utils;

import java.io.IOException;
import java.io.File;
import java.util.Properties;

import dubh.utils.misc.UserPreferences;
import dubh.utils.misc.Debug;
/**
 * Internal class for providing singleton access to our preferences file.
 * Version History: <UL>
 * <LI>0.1 [08/12/98]: Initial Revision
 * <LI>0.2 [12/12/98]: Added support for alternative location
 *</UL>
 @author Brian Duff
 @version 0.2 [12/12/98]
 */
public class DubhUtilsPreferences extends UserPreferences {
   
   
   /** The default properties file for DubhUtils' preferences */
   public static final String s_DJUPROPS = 
      System.getProperty("user.home") + File.separator + "dubhutils.properties";
   private static final String s_DJUDLGKEY    = 
      "DialogLocation.";
   
   private static DubhUtilsPreferences m_instance = null;

   private static String s_propsFile = s_DJUPROPS;
   
   protected DubhUtilsPreferences() throws IOException
   {
         super(s_propsFile);
   }
   
   public static DubhUtilsPreferences getPreferences()
   {
      if (m_instance == null)
      {
         try
         {
            m_instance = new DubhUtilsPreferences();
         }
         catch (IOException e)
         {
            Debug.println("Unable to instantiate dubh utils preferences");
         }
      }
      return m_instance;
   }
   
   /**
    * Set a different properties file to use for dubh utils preferences. 
    * this is useful if your application wants to keep its utility settings
    * (e.g. window positions) separate from other apps using the utilities.
    * For the change to take effect, you must refresh any instances of 
    * DubhUtilsPreferences by calling getPreferences() again.
    */
   public static void setPropertiesFile(String fileName)
   {
      m_instance = null;
      s_propsFile = fileName;
   }
      
      
}