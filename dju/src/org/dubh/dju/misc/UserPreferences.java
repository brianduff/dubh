// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: UserPreferences.java,v 1.4 1999-03-22 23:37:17 briand Exp $
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
/**
 * Represents a set of user preferences for an application.<P>
 * Version History: <UL>
 * <LI>0.1 [05/06/98]: Initial Revision
 * <LI>0.2 [13/06/98]: Added colour, font and string array preference methods.
 *   added property change event support.
 * <LI>0.3 [18/06/98]: Added empty constructor. Made serializable.
 * <LI>0.4 [08/12/98]: Changed to use a buffered output stream for saving props
 * <LI>1.0 [26/01/99]: Now requires JDK 1.2. (Properties.save() changed to 
 *                     Properties.store() )
 *</UL>
 @author Brian Duff
 @version 1.0 [26/01/99]
 */
public class UserPreferences implements Serializable {

  private           Properties m_props;
  private transient File       m_propsfile= null;
  private transient String     m_header = "";
  private transient Vector     m_proplisteners = new Vector();

  /**
   * Create a new UserPreferences object that can't be saved or loaded. This
   * is useful as a wrapper round a Properties object. Uses a new, empty
   * set of properties.
   */
  public UserPreferences() {
     this(new Properties());
  }

  /**
   * Create a new UserPreferences object that can't be saved or loaded. This
   * is useful as a wrapper round a Properties object.
   @param p the properties object to wrap.
   */
  public UserPreferences(Properties p) {
     m_props = p;
  } 

  /**
   * Create a new UserPreferences object using the specified filename as the
   * preferences file. If the file doesn't exist, the default preferences
   * (an empty preferences file) is used.
   @param filename The preferences filename
   @throws java.io.IOException the properties file couldn't be read
   */
  public UserPreferences(String filename) throws IOException {
     this(new File(filename), new Properties());
  }

  /**
   * Create a new UserPreferences object using the specified File as the
   * preferences file. If the file doesn't exist, the default preferences
   * (an empty preferences file) is used.
   @param file A file object
   @throws java.io.IOException the properties file couldn't be read
   */
  public UserPreferences(File file) throws IOException {
     this(file, new Properties());
  }

  /**
   * Create a new UserPreferences object using the specified filename as the
   * preferences file. If the file doesn't exist, the specified Properties
   * object is used for defaults.
   @param filename The preferences filename
   @param defaults The default properties to use if the file doesn't exist
   @throws java.io.IOException the properties file couldn't be read
   */
  public UserPreferences(String filename, Properties defaults) throws IOException {
     this(new File(filename), defaults);
  }

  /**
   * Create a new UserPreferences object using the specified File as the
   * preferences file. If the file doesn't exist, the specified Properties
   * object is used for defaults.
   @param file A file object
   @param defaults The default properties file if the file doesn't exist
   @throws java.io.IOException the properties file couldn't be read
   */
  public UserPreferences(File file, Properties defaults) throws IOException {
     m_propsfile = file;
     if (!m_propsfile.exists())
        m_props = defaults;
     else
        revert();
     //m_listeners = new PropertyChangeSupport(this);
  }

  /**
   * Reverts preferences to the last saved version. This discards all
   * changes to this UserPreferences object since the last call to save()
   * was made.
   @throws java.io.IOException the preferences file couldn't be read
   */
  public void revert() throws IOException {
     if (m_propsfile != null) {
        FileInputStream fis = new FileInputStream(m_propsfile);
        m_props = new Properties();
        m_props.load(fis); // throws IOException
        fis.close();
        firePropertyChange("", "", "");
     }
  }

  /**
   * Saves the preferences file.
   @throws java.io.IOException the preferences file couldn't be written
   */
  public void save() throws IOException {
     if (m_propsfile != null) {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(m_propsfile));
        
        m_props.store(bos, m_header);      // Requires JDK 1.2
        bos.flush();
        bos.close();
     }
  }

  /**
   * Sets the header to prepend to the preferences file. Blank by default.
   */
  public void setHeader(String header) { m_header = header; }

  /**
   * Gets an enumeration of preference key names.
   @return an Enumeration object of preference key names.
   */
  public Enumeration getKeys() { return m_props.propertyNames(); }

  /**
   * Gets a preference as a String.
   @param key The preference key to retrieve
   @return the string value of the specified preference
   */
  public String getPreference(String key) {
     return m_props.getProperty(key);
  }

  /**
   * Gets a preference as a String
   @param key The preference key to retrieve
   @param def The default value if the key doesn't exist
   */
  public String getPreference(String key, String def) {
     return m_props.getProperty(key, def);
  }

  /**
   * Sets a string preference
   @param key The preference key to set
   @param value The value to set
   */
  public void setPreference(String key, String value) {
     String oldVal = (String) m_props.get(key);
     m_props.put(key, value);
    // Debug.println("UserPreferences is firing a property change event for "+key);
     if (oldVal != null) firePropertyChange(key, oldVal, value);
     else firePropertyChange(key, "", value); 
  }

  /**
   * Gets a preference as an int.
   @param key The preference key to retrieve
   @return the converted integer value of the specified preference
   @throws java.lang.NumberFormatException the preference value is non-numeric
   */
  public int getIntPreference(String key) throws NumberFormatException {
     return StringUtils.stringToInt(getPreference(key));
  }

  /**
   * Gets a preference as an int.
   @param key The preference key to retrieve
   @param def The default value
   @return the converted integer value of the specified preference
   @throws java.lang.NumberFormatException the preference value is non-numeric
   */
  public int getIntPreference(String key, int def) {
     return StringUtils.stringToInt(getPreference(key,
                                               StringUtils.intToString(def)));
  }

  /**
   * Sets an integer preference.
   @param key The preference key to set
   @param value The integer value to set the key to
   */
  public void setIntPreference(String key, int value) {
     setPreference(key, StringUtils.intToString(value));
  }

  /**
   * Gets a boolean preference. Boolean preferences are: <BR>
   * <B>true</B> if they start with 't', 'T', 'y' or 'Y'<BR>
   * <B>false</B> if they start with any other character
   @param key the preference key to retrieve
   */
  public boolean getBoolPreference(String key) {
     return stringToBoolean(getPreference(key));
  }

  /**
   * Gets a boolean preference. Boolean preferences are: <BR>
   * <B>true</B> if they start with 't', 'T', 'y' or 'Y'<BR>
   * <B>false</B> if they start with any other character
   @param key the preference key to retrieve
   @param def the default value if the key doesn't exist.
   */
  public boolean getBoolPreference(String key, boolean def) {
     return stringToBoolean(getPreference(key, booleanToYesNoString(def)));
  }

  /**
   * Sets a boolean preference. By default, true preferences are set to the
   * character string "true" and false preferences are set to "false". You
   * can also use setYesNoPreference() to set the preferences to "yes" or "no".
   @param key the preference key to set
   @param value the value to set it to
   */
  public void setBoolPreference(String key, boolean value) {
     setPreference(key, booleanToTrueFalseString(value));
  }

  /**
   * Sets a boolean preference using "yes" for true and "no" for false.
   * Preferences set this way still follow the rules for getBooleanPreference,
   * so will work correctly with that function.
   @param key the preference key to set
   @param value the value to set it to
   @see getBooleanPreference()
   */
  public void setYesNoPreference(String key, boolean value) {
     setPreference(key, booleanToYesNoString(value));
  }

  /**
   * Sets a Font preference using Font.toString()
   */
  public void setFontPreference(String key, Font pref) {
     setPreference(key, pref.toString());
  }

  /**
   * Gets a Font preference
   */
  public Font getFontPreference(String key) {
     return Font.getFont(getPreference(key));
  }

  public Font getFontPreference(String key, Font def) {
     return Font.getFont(getPreference(key, def.toString()));
  }

  /**
   * Sets a colour preference
   */
  public void setColorPreference(String key, Color col) {
     setPreference(key, col.toString());
  }

  /**
   * Gets a color preference
   */
  public Color getColorPreference(String key) {
     return Color.getColor(getPreference(key));
  }

  /**
   * Gets a color preference using the specified default
   */
  public Color getColorPreference(String key, Color def) {
     return Color.getColor(getPreference(key, def.toString()));
  }

  /**
   * Sets an array preference. The individual array elements should be
   * strings that don't contain spaces.
   */
  public void setStringArrayPreference(String key, String[] value) {
     setPreference(key, StringUtils.createSentence(value));
  }

  /**
   * Gets a string array preference. The pref value should contain a set
   * of string values separated by spaces.
   */
  public String[] getStringArrayPreference(String key) {
     return StringUtils.getWords(getPreference(key));
  }

  /**
   * Gets a string array preference.
   */
  public String[] getStringArrayPreference(String key, String[] def) {
     return StringUtils.getWords(getPreference(key, StringUtils.createSentence(def)));
  }

   /**
    * Remove a key and its value from the user preferences.
    */
   public void removeKey(String key)
   {
      m_props.remove(key);
   }

// Event handling stuff

  public synchronized void addPropertyChangeListener(PropertyChangeListener p) {
     m_proplisteners.addElement(p);
  }

  public void removePropertyChangeListener(PropertyChangeListener p) {
     m_proplisteners.removeElement(p);
  }
 

  protected void firePropertyChange(String propname, Object oldVal, Object newVal) {
     Enumeration enum = m_proplisteners.elements();
     PropertyChangeEvent e = new PropertyChangeEvent(this, propname, oldVal, newVal);

     while (enum.hasMoreElements()) {
        ((PropertyChangeListener)enum.nextElement()).propertyChange(e);   
     }
  }

  



// PRIVATE

  /**
   * Utility method to convert "yes | no" to true | false
   */
  private boolean stringToBoolean(String s) {
     char c = s.toLowerCase().charAt(0);
   return (c == 'y' || c == 't' ? true : false);
  }

  /**
   * Utility method to convert true | false to "yes" | "no"
   */
  private String booleanToYesNoString(boolean b) {
   return (b ? "yes" : "no");
  }

  private String booleanToTrueFalseString(boolean b) {
   return (b ? "true" : "false");
  }


}