// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ResourceManager.java,v 1.5 1999-03-22 23:37:17 briand Exp $
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
import javax.swing.*;
import java.awt.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import dubh.utils.nls.*;

import dubh.utils.misc.Debug;
/**
 * A ResourceManager is associated with a specific resource bundle. You can
 * use the methods of an instance of this class to get locale independent
 * strings, as well as initialise components from a resource bundle.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [19/06/98]: Initial Revision
 * <LI>0.2 [30/06/98]: Added ResourceManager(ResourceBundle b) constructor.
 * <LI>1.0 [03/12/98]: Changed to go through the panel setting properties.
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 1.0 [03/12/98]
 */
public class ResourceManager {


   /** A cache for storing shared instances of images */
   private Hashtable m_cachedImages;
   /** The resource bundle */
   private ResourceBundle m_bundle;
   
   /** Vector of component handlers */
   private Vector m_handlers;
  
   private static ResourceManager m_shared = null;
  
  /**
   * Construct a ResourceManager for the specified bundle. The name of the
   * bundle is prefixed to ".properties" and the CLASSPATH is searched for
   * the file. For locales other than the default (the one under which the
   * application is developed), use filenames of the form
   * bundleName_XX.properties, where XX is the country code of the locale (e.g
   * de, fr, us, uk, etc.).
   @throws MissingResourceException if the bundle can't be located.
   */
  public ResourceManager(String bundleName) throws MissingResourceException {
     this(ResourceBundle.getBundle(bundleName));
  }

  /**
   * Construct a ResourceManager for the specified bundle. The name of the
   * bundle is prefixed to ".properties" and the CLASSPATH is searched for
   * the file. For locales other than the default (the one under which the
   * application is developed), use filenames of the form
   * bundleName_XX.properties, where XX is the country code of the locale (e.g
   * de, fr, us, uk, etc.).
   @param locale the locale to use for all resources.
   @throws MissingResourceException if the bundle can't be located.
   */
  public ResourceManager(String bundleName, Locale locale)
        throws MissingResourceException {
     this(ResourceBundle.getBundle(bundleName, locale));
  }

  public ResourceManager(ResourceBundle b) {
     m_handlers = new Vector();
     m_cachedImages = new Hashtable();
     m_bundle = b;
     addComponentHandler(JLabelHandler.getInstance());
     addComponentHandler(JComponentHandler.getInstance());
     addComponentHandler(AbstractButtonHandler.getInstance());
  }
  
  /**
   * Get a shared instance of a resource manager for the specified bundle.
   */
  public static ResourceManager getManagerFor(String bundleName)
     throws MissingResourceException
  {
     ResourceBundle b = ResourceBundle.getBundle(bundleName);
  
     if (m_shared == null)
     {
        m_shared = new ResourceManager(b);
        m_shared.addComponentHandler(JLabelHandler.getInstance());
        m_shared.addComponentHandler(JComponentHandler.getInstance());
        m_shared.addComponentHandler(AbstractButtonHandler.getInstance());
     }
     else
     {
        m_shared.m_bundle = b;
     }
     return m_shared;
  }
 

  /**
   * Get a string from the resource bundle.
   @param key the key of the string to retrieve
   @return the value of the string in your bundle.
   @throws MissingResourceException if the key doesn't exist in the bundle.
   */
  public String getString(String key) throws MissingResourceException {
     return m_bundle.getString(key);
     //return "Testing";
  }

  /**
   * Get an image from the resource bundle. Images are .gif or .jpg files
   * located somewhere on the CLASSPATH. To get an image, define a key in
   * the resource bundle, whose value is the filename of the image. For
   * example create a file called myresource.properties and put this in it:<PRE>
   *    okImage=images/ok.gif
   * </PRE>
   * Then retrieve the image with:
   * <PRE>
   *    ResourceManager r = new ResourceManager("myresource");
   *    ImageIcon icon = r.getImage("okImage");
   * </PRE>
   * Whenever possible, this method returns a shared instance of an image. This
   * means that an image is actually only loaded the first time you call this
   * method. All subsequent calls for the same image key return a shared
   * instance of the image. <b>this means that if you modify the image, all
   * shared instances will be affected</b>.
   @param key the key of the image in your resource bundle.
   @throws MissingResourceException if the resource doesn't exist.
   */
  public ImageIcon getImage(String key) throws MissingResourceException {
     /*
      * Maintain a cache of images whenever one is loaded so that whenever
      * possible, we return a shared instance of an image. If an image is
      * used lots of times, it will only be loaded once and re-used.
      */
      
     ImageIcon ico = (ImageIcon) m_cachedImages.get(key);
     if (ico == null) {
        String filename = getString(key);
        java.net.URL imgResource = ClassLoader.getSystemResource(filename);
        if (imgResource != null) {
           ico = new ImageIcon(imgResource);
           m_cachedImages.put(key, ico);
        } else {
           if (Debug.TRACE_LEVEL_1)
           {
              Debug.println(1, this, "Can't load image: "+key+"="+filename);
           }
        }
     }
     return ico; 
  }
   
  /**
   * Initialise the components in a container. This enumerates all the
   * components in a container, and sets their NLS properties. You can
   * register handlers for specific components using the 
   * registerComponentHandler() method, passing an object that implements
   * the dubh.utils.nls.ComponentHandler interface.
   */
  public void initComponents(Container c)
  {
     StringBuffer key = new StringBuffer();
     doComponent(key, c);     
  }

  
  protected void addComponentHandler(ComponentHandler h)
  {
     m_handlers.addElement(h);
  }
  
  
  protected void removeComponentHandler(ComponentHandler h)
  {
     m_handlers.removeElement(h);
  }
   
  
  private void doComponents(StringBuffer prefix, Component[] components)
  {     
     for (int i=0; i < components.length; i++)
     {
        doComponent(prefix, components[i]);
     }
  }   
  
  /**
   * Handle a component. This can be used in place of the old ResourceManager.initButton,
   * but should be restricted to situations where it is really necessary to
   * manually set resources for a component. Normally, use the initComponents() 
   * method on a top level container.
   * </p><p>
   */
  public void doComponent(String key, Component c)
  {
     String oldName = c.getName();
     c.setName(key);
     
     doComponent(new StringBuffer(""), c);
     
     c.setName(oldName);
  }
  
  /**
   * Handle a component. Prefix should consist of the full path to 
   * the component eg. MyPanel.MyInnerPanel.MyJButton
   */ 
  private void doComponent(StringBuffer prefix, Component c)
  {
     int oldValue = prefix.length();
     prefix.append(c.getName());
     
     
     if (c instanceof Container &&
         ((Container)c).getComponentCount() > 0 && c.getName() != null)
     {
     
        int pf = prefix.length();
        prefix.append(".");
        doComponents(prefix, ((Container)c).getComponents());
        prefix.setLength(pf);
     }
     else
     {
        if (c.getName() != null)
        {
           for (int i=0; i < m_handlers.size(); i++)
           {
              ComponentHandler handler = 
                 (ComponentHandler) m_handlers.elementAt(i);
                 
              if (handler.getHandledClass().isInstance(c))
              {
                 for (int j=0; j < handler.getSupportedProperties().length; j++)
                 {
                    setComponentProperty(prefix, c, handler.getSupportedProperties()[j]);
                 }
              }
           
           }
        }
     }
     prefix.setLength(oldValue);
  }
  
  private void setComponentProperty(StringBuffer prefix, Component c, String property)
  {
     String methodName = "set"+property.substring(0, 1).toUpperCase()+property.substring(1);
     int oldLength = prefix.length();
     prefix.append(".");
     prefix.append(property);
     try
     {

        Object parameter;
        Method method;
        //
        // Special handling for icon properties.
        //
        if (property.equals("icon"))
        {
           parameter = getImage(getString(prefix.toString()));
           method    = c.getClass().getMethod(methodName, new Class[] { 
              Class.forName("javax.swing.Icon")
           });
        }
        else if (property.toLowerCase().indexOf("mnemonic") >= 0)
        {
           parameter = new Character(getString(prefix.toString()).charAt(0));
           method    = c.getClass().getMethod(methodName, new Class[] {
              Character.TYPE
           });
        
        }
        else
        { 
          parameter = getString(prefix.toString());
           method    = c.getClass().getMethod(methodName, new Class[] {
              Class.forName("java.lang.String")
           });
        }
        method.invoke(c, new Object[] { parameter });
        
     }
     catch (Throwable t)
     {
        if (Debug.TRACE_LEVEL_1)
        {
           Debug.println(1, this, "Resource key "+prefix+"."+property+" isn't defined");
        }
     }
  
     prefix.setLength(oldLength);
  }
 /* 
  public static void main(String[] args)
  {
     ResourceManager test = new ResourceManager();
     test.addComponentHandler(JLabelHandler.getInstance());
     test.addComponentHandler(JComponentHandler.getInstance());
     test.addComponentHandler(AbstractButtonHandler.getInstance());
     
     JPanel pan = new JPanel();
     pan.setName("panel");
     JButton but = new JButton();
     but.setName("button");
     JLabel lbl = new JLabel();
     lbl.setName("label");
     pan.add(but);
     pan.add(lbl, BorderLayout.NORTH);
     
     test.doComponent(new StringBuffer(), pan);
     
     JFrame tast = new JFrame();
     tast.getContentPane().add(pan);
     tast.pack();
     tast.setVisible(true);
  }
*/


}