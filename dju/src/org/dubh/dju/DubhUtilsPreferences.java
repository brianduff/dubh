// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhUtilsPreferences.java,v 1.6 2001-02-11 02:52:10 briand Exp $
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

package org.dubh.dju;

import java.io.IOException;
import java.io.File;
import java.util.Properties;
import java.security.AccessControlException;

import org.dubh.dju.misc.UserPreferences;
import org.dubh.dju.misc.Debug;
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