// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: UserPreferences.java,v 1.7 1999-11-02 19:53:14 briand Exp $
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
 * Represents a set of user preferences for an application. The methods in this
 * class allow you to store preferences of a variety of different types in a 
 * properties file or object.
 * <br>
 * <b>The event handling for this class is not thread safe.</b>
 * @author Brian Duff
 * @version $Id: UserPreferences.java,v 1.7 1999-11-02 19:53:14 briand Exp $
 */
public class UserPreferences implements Serializable {

  private           Properties m_props;
  private transient File       m_propsfile= null;
  private transient String     m_header = "";
  private transient ArrayList  m_proplisteners = new ArrayList();
  private transient Hashtable  m_hashListCache = null;
  private transient Hashtable  m_hashListIndices = null;

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
     m_hashListCache = new Hashtable();
     m_hashListIndices = new Hashtable();
     //m_listeners = new PropertyChangeSupport(this);
  }     
  
  /**
   * Create user preferences based on a properties input stream.
   * You must call setFile at some point, or the properties will
   * never be saveable or revertable.
   */
  public UserPreferences(InputStream is)
     throws IOException
  {
     m_propsfile = null;
     revert(is);  
     m_hashListCache = new Hashtable();
     m_hashListIndices = new Hashtable();
  }
  
   /**
    * Compares keys in a properties list so that they can
    * be sorted
    */
   class KeyComparator implements Comparator
   {
      public int compare(Object o1, Object o2)
      {
         int dotIndex1 = ((String)o1).indexOf('.');
         int dotIndex2 = ((String)o2).indexOf('.');
         String seqNo1 = ((String)o1).substring(dotIndex1+1);
         String seqNo2 = ((String)o2).substring(dotIndex2+1);
         int int1 = Integer.parseInt(seqNo1);
         int int2 = Integer.parseInt(seqNo2);
         return (int1-int2);
      }
      
      public boolean equals(Object o)
      {
         return (o instanceof KeyComparator);
      }
   }   
   
   /**
    * Get a sorted list of keys from this UserPreferences object
    * that start with the specified baseKey and end with a number.
    * E.g. 
    * value.1 = 
    * value.5 = 
    * value.6 = 
    * @param baseKey the key that provides the base of the list.
    * @return a sorted list of keys conforming to the above description
    */
   protected ArrayList getSortedKeyList(String baseKey)
   {
      Enumeration keys = getKeys();
      ArrayList keyList = new ArrayList();
      System.out.println("Are there any keys in the props file? "+keys.hasMoreElements());
      while (keys.hasMoreElements())
      {
         String curKey = (String)keys.nextElement();
         System.out.println("Considering key "+curKey);
         if (curKey.startsWith(baseKey+"."))
         {
            keyList.add(curKey);
            System.out.println("Made it into the list: "+curKey);            
         }
      }
      
      Collections.sort(keyList, new KeyComparator());
      
      return keyList;
   }  
  
   /**
    * Provides support for a list of values in a properties file
    * that take the form: <pre>
    *  some.key.1 = value
    *  some.key.2 = value
    *  some.key.21 = value
    * </pre>
    * The indices don't have to be contiguous or ordered within
    * the properties file.
    *
    * The list is cached the first time you call this method, and
    * the cached version is returned on subsequent calls. If you
    * want to add or remove items from the list, you must use
    * addToMultiKeyList and removeFromMultiKeyList. Don't add
    * items to the returned lists or they will not be 
    * saved properly.
    *
    * Saving will cause all loaded lists to be set as properties
    * and stored in the properties file if applicable.
    *
    * @param baseKey The base part of the key e.g. some.key
    * @return a contiguous array list of list values in the correct order
    */
   public ArrayList getMultiKeyList(String baseKey)
   {
      if (m_hashListCache == null) 
         m_hashListCache = new Hashtable();
      if (m_hashListIndices == null)
         m_hashListIndices = new Hashtable();
            
      ArrayList values = (ArrayList)m_hashListCache.get(baseKey);
      
      if (values == null)
      {
         ArrayList keys = getSortedKeyList(baseKey);
         values = new ArrayList(keys.size()); 
         ArrayList indices = new ArrayList(keys.size());
         
         for (int i=0; i < keys.size(); i++)
         {
            String key = (String)keys.get(i);
            String index = key.substring(key.indexOf('.')+1);
            indices.add(new Integer(index));
            values.add(getPreference((String)keys.get(i)));
         }
         m_hashListCache.put(baseKey, values);
         m_hashListIndices.put(baseKey, indices);
      }
      return values;
   }  
  
   /**
    * Add to a multi key list
    */
   public int addToMultiKeyList(String baseKey, Object o)
   {
      getMultiKeyList(baseKey).add(o);
      // Get the indices for this key
      ArrayList indices = (ArrayList)m_hashListIndices.get(baseKey);
      // Find out the highest index
      Integer biggest = (Integer)indices.get(indices.size()-1);
      // Store the new item at highest_index+1
      Integer newIndex = new Integer(biggest.intValue()+1);
      indices.add(newIndex);
      return newIndex.intValue();
   }
  
   /**
    * Returns the index of the specified item in the multi
    * key list
    */
   public int getMultiKeyListIndex(String baseKey, Object o)
   {
      // First, get the lists
      ArrayList mkl = getMultiKeyList(baseKey);
      // and the index maps
      ArrayList indices = (ArrayList)m_hashListIndices.get(baseKey);
      
      // Now get the index of o
      int index = mkl.indexOf(o);
      return ((Integer)indices.get(index)).intValue();
   }
  
   /**
    * Remove from a multi key list
    */
   public void removeFromMultiKeyList(String baseKey, Object o)
   {
      ArrayList list = getMultiKeyList(baseKey);
      int itemIndex = list.indexOf(o);
      list.remove(itemIndex);
      Integer keyIndex = (Integer)((ArrayList)m_hashListIndices.get(baseKey)).get(itemIndex);
      ((ArrayList)m_hashListIndices.get(baseKey)).remove(itemIndex);
      removeKey(baseKey+"."+keyIndex);
   }
   
   /**
    * Dump down all cached multi key lists into the properties
    * object ready for saving.
    */
   protected void dumpMultiKeyLists()
   {
      Enumeration lists = m_hashListCache.keys();
      while (lists.hasMoreElements())
      {
         String listKey = (String)lists.nextElement();
         ArrayList alItems = (ArrayList)m_hashListCache.get(listKey);
         ArrayList alIndices = (ArrayList)m_hashListIndices.get(listKey);
         
         for (int i=0; i < alItems.size(); i++)
         {
            String fullKey = listKey+"."+alIndices.get(i);
            setPreference(fullKey, (String)alItems.get(i));
         }
      }
   }
  
  /**
   * Set the file that this UserPreferences object reads/writes from.
   */
  public void setFile(File f)
  {
     m_propsfile = f;
  }


  public void revert(InputStream i)
     throws IOException
  {
     m_props = new Properties();
     m_props.load(i); // throws IOException
     i.close();
     firePropertyChange("", "", "");  
  }
  /**
   * Reverts preferences to the last saved version. This discards all
   * changes to this UserPreferences object since the last call to save()
   * was made.
   @throws java.io.IOException the preferences file couldn't be read
   */
  public void revert() 
     throws IOException 
  {
     if (m_propsfile != null)
     {
        revert(new FileInputStream(m_propsfile));
     }
  }

  /**
   * Saves the preferences file.
   @throws java.io.IOException the preferences file couldn't be written
   */
  public void save() throws IOException {
     if (m_propsfile != null) {
        dumpMultiKeyLists();
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
   * Sets a Font preference. The font is stored as a string in the form
   * <pre><i>name</i>-{bold | italic | plain | bolditalic}-<i>size</i></pre>,
   * which is the format understood by {@link #java.awt.Font.decode(String)}.
   */
  public void setFontPreference(String key, Font pref) {
     setPreference(key, encodeFont(pref));
  }

  /**
   * Gets a Font preference.
   * @see setFontPreference(String, Font)
   */
  public Font getFontPreference(String key) {
     return Font.decode(getPreference(key));
  }

  /**
   * Get a Font preference with a default.
   * @see setFontPreference(String, Font)
   */
  public Font getFontPreference(String key, Font def) {
     return Font.decode(getPreference(key, encodeFont(def)));
  }

  /**
   * Sets a colour preference. The preference is stored as a 
   * six digit hexadecimal number prefixed by a hash character, i.e.
   * <pre>#rrggbb</pre>. 
   */
  public void setColorPreference(String key, Color col) {
     setPreference(key, encodeColor(col));
  }

  /**
   * Gets a color preference
   * @see setColorPreference(String, Color)
   */
  public Color getColorPreference(String key) {
     return Color.decode(getPreference(key));
  }

  /**
   * Gets a color preference using the specified default
   * @see setColorPreference(String, Color)
   */
  public Color getColorPreference(String key, Color def) {
     return Color.decode(getPreference(key, encodeColor(def)));
  }

  /**
   * Sets an array preference. The individual array elements should be
   * objects with sensible toString()s that contain no spaces.
   */
  public void setStringArrayPreference(String key, Object[] value) {
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

  /**
   * Add a listener that will be informed when a property value changes.
   */
  public void addPropertyChangeListener(PropertyChangeListener p) {
     m_proplisteners.add(p);
  }

  public void removePropertyChangeListener(PropertyChangeListener p) {
     m_proplisteners.remove(p);
  }
 

  protected void firePropertyChange(String propname, Object oldVal, Object newVal) {
     
     PropertyChangeEvent e = new PropertyChangeEvent(this, propname, oldVal, newVal);

     for (int i=0; i < m_proplisteners.size(); i++)
     {
        ((PropertyChangeListener)m_proplisteners.get(i)).propertyChange(e);
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
  
  /**
   * Can't seem to find a way to encode fonts in the format that Font.decode()
   * expects in the AWT toolkit. One would assume Font.toString() would do this, 
   * but it doesn't. D'oh!.
   * Another good example of the shoddy quality of documentation for Java, I had to 
   * actually look at the source code to figure out what format decode expects
   * the font string to be in. It would have been so easy for Sun to add a description to
   * the javadoc.
   */
  private String encodeFont(Font f)
  {
     StringBuffer sbFontCode = new StringBuffer(10);
     
     sbFontCode.append(f.getName());
     sbFontCode.append("-");
     
     int style = f.getStyle();
     
     if (style == Font.PLAIN) sbFontCode.append("plain");
     else if (style == Font.BOLD) sbFontCode.append("bold");
     else if (style == Font.ITALIC) sbFontCode.append("italic");
     else if (style == Font.BOLD + Font.ITALIC) sbFontCode.append("bolditalic");

     sbFontCode.append("-"+f.getSize());
     
     return sbFontCode.toString();
     
  }

  private String encodeColor(Color c)
  {
     StringBuffer sbColorCode = new StringBuffer(7);
     
     sbColorCode.append('#');
     
     sbColorCode.append(StringUtils.makeDoubleHex(c.getRed()));
     sbColorCode.append(StringUtils.makeDoubleHex(c.getGreen()));
     sbColorCode.append(StringUtils.makeDoubleHex(c.getBlue()));
     
     return sbColorCode.toString();
     
  }



}

//
// Old Log
//
/*
 * Version History: <UL>
 * <LI>0.1 [05/06/98]: Initial Revision
 * <LI>0.2 [13/06/98]: Added colour, font and string array preference methods.
 *   added property change event support.
 * <LI>0.3 [18/06/98]: Added empty constructor. Made serializable.
 * <LI>0.4 [08/12/98]: Changed to use a buffered output stream for saving props
 * <LI>1.0 [26/01/99]: Now requires JDK 1.2. (Properties.save() changed to 
 *                     Properties.store() )
 *</UL>
 */

//
// New Log
//
// $Log: not supported by cvs2svn $
// Revision 1.6  1999/10/24 00:39:11  briand
// Add MultiKeyList support.
//
// Revision 1.5  1999/06/01 17:56:55  briand
// Fix font / color preference methods; added two private encoders
// to convert fonts & colors into strings that Font.decode() and
// Color.decode() recognise.
//
//