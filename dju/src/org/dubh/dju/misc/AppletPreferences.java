// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AppletPreferences.java,v 1.3 2001-02-11 02:52:11 briand Exp $
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

package org.dubh.dju.misc;

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
 * @version $Id: AppletPreferences.java,v 1.3 2001-02-11 02:52:11 briand Exp $
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
// Revision 1.2  1999/11/11 21:24:34  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/03/22 23:34:41  briand
// UserPreferences that come from applet parameters.
//
//