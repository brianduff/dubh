// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AppletPreferences.java,v 1.1 1999-03-22 23:34:41 briand Exp $
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
package dubh.utils.misc;

import java.util.*;
import java.io.*;
import java.beans.*;
import java.awt.*;
import java.applet.Applet;

/**
 * A UserPreferences subclass that holds preferences initialised from
 * an applet's parameters.
 * @author Brian Duff
 * @since DJU 1.1.0
 * @version $Id: AppletPreferences.java,v 1.1 1999-03-22 23:34:41 briand Exp $
 */
public class AppletPreferences extends UserPreferences {

   private Applet    m_applet;
   private Hashtable m_altered;
   private Vector    m_paramVec;

  /**
   * 
   */
  public AppletPreferences(Applet a) {
     super();
     m_altered = new Hashtable();
     m_applet = a;
  }

  /**
   * Reverts preferences to the last saved version. This discards all
   * changes to this UserPreferences object since the last call to save()
   * was made.
   @throws java.io.IOException the preferences file couldn't be read
   */
  public void revert() throws IOException 
  {
     m_altered = new Hashtable();
  }

  /**
   * Saves the preferences file. This has no effect in the applet version
   @throws java.io.IOException the preferences file couldn't be written
   */
  public void save() throws IOException {
     // do nothing
  }


  /**
   * Gets an enumeration of preference key names.
   @return an Enumeration object of preference key names.
   */
  public Enumeration getKeys() 
  {
     String[][] param = m_applet.getParameterInfo();
     
     //
     // Return an empty enumeration if no parameter information
     //
     if (param == null) 
        return super.getKeys(); 
        
     if (m_paramVec == null)
     {
        m_paramVec = new Vector(param.length);
        
        for (int i=0; i < param.length; i++)
        {
           m_paramVec.addElement(param[i][0]);   
        }
     }
     
     return m_paramVec.elements();
     
  }

  /**
   * Gets a preference as a String.
   @param key The preference key to retrieve
   @return the string value of the specified preference
   */
  public String getPreference(String key) {
     String obj = (String) m_altered.get(key);
     if (obj == null)
     {
        try
        {
           obj = m_applet.getParameter(key);
        }
        catch (Throwable t)
        {
           obj = null;
        }
        if (obj == null) return null;
        m_altered.put(key, obj);
     }
     return obj;
  }

  /**
   * Gets a preference as a String
   @param key The preference key to retrieve
   @param def The default value if the key doesn't exist
   */
  public String getPreference(String key, String def) {
     String result = getPreference(key);
     
     return (result == null ? def : result);
  }

  /**
   * Sets a string preference
   @param key The preference key to set
   @param value The value to set
   */
  public void setPreference(String key, String value) {
     String oldVal = getPreference(key);
     m_altered.put(key, value);
     if (oldVal != null) firePropertyChange(key, oldVal, value);
     else firePropertyChange(key, "", value); 
  }


   /**
    * Remove a key and its value from the user preferences.
    */
   public void removeKey(String key)
   {
     //
   }

}

//
// $Log: not supported by cvs2svn $
//