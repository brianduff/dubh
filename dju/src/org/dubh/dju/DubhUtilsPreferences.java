// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhUtilsPreferences.java,v 1.4 1999-03-22 23:27:45 briand Exp $
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
package dubh.utils;

import java.io.IOException;
import java.io.File;
import java.util.Properties;
import java.security.AccessControlException;

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

   

   private static boolean m_isApplet;   
   private static String s_propsFile;
   
   static
   {
      try
      {
         s_propsFile = 
            System.getProperty("user.home") + File.separator + "dubhutils.properties";
         m_isApplet = false;
      }
      catch (AccessControlException ace)
      {
         m_isApplet = true;
         
      }
   }
   private static DubhUtilsPreferences m_instance = null;
   
   protected DubhUtilsPreferences() throws IOException
   {
         super(s_propsFile);
   }

   protected DubhUtilsPreferences(Properties p) throws IOException
   {
         super(p);
   }

   
   public static DubhUtilsPreferences getPreferences()
   {
      if (m_instance == null)
      {
         try
         {
            if (m_isApplet) 
               m_instance = new DubhUtilsPreferences(new Properties());
            else
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