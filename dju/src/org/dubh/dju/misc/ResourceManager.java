// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ResourceManager.java,v 1.11 2001-02-11 03:40:42 briand Exp $
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
import javax.swing.*;
import java.awt.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import java.text.MessageFormat;

import org.dubh.dju.nls.*;

import org.dubh.dju.misc.Debug;

/**
 * A ResourceManager is associated with a specific resource bundle. You can
 * use the methods of an instance of this class to get locale independent
 * strings, as well as initialise components from a resource bundle.<P>
 *
 * @author Brian Duff
 */
public class ResourceManager
{


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
  public ResourceManager(String bundleName) throws MissingResourceException
  {
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
        throws MissingResourceException
  {
     this(ResourceBundle.getBundle(bundleName, locale));
  }

  public ResourceManager(ResourceBundle b)
  {
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
  public String getString(String key) throws MissingResourceException
  {
     return m_bundle.getString(key);
  }

  /**
   * Get a string from the resource, substituting variables from an array
   */
  public String getString(String key, Object[] substitutions) throws MissingResourceException
  {
     return MessageFormat.format(getString(key), substitutions);
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
  public ImageIcon getImage(String key) throws MissingResourceException
  {
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
   * the org.dubh.dju.nls.ComponentHandler interface.
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
     String methodName = "set"+property.substring(0, 1).toUpperCase() +
      property.substring(1);
     int oldLength = prefix.length();
     prefix.append(".");
     prefix.append(property);
     try
     {

        Object parameter;
        Method method = null;
        Class methodParamType = null;

        // We look up the method by finding the first matching method with
        // a single parameter. Next, we attempt to retrieve an object of the
        // correct type. We can only deal with certain types, specifically,
        // Icons, chars and Strings. But this can easily be extended.

        Method[] allMethods = c.getClass().getMethods();

        for (int i=0; i < allMethods.length; i++)
        {
            if (methodName.equals(allMethods[i].getName()))
            {
               Class[] params = allMethods[i].getParameterTypes();

               if (params.length == 1)
               {
                  method = allMethods[i];
                  methodParamType = params[0];
                  break;
               }
            }
        }

        // If we don't have a method at this point, it's time to bail.
        if (method == null)
        {
            return;
        }

        parameter = getSetterParameter(methodParamType, prefix.toString());

        method.invoke(c, new Object[] { parameter });

     }
     catch (Throwable t)
     {
       // if (Debug.TRACE_LEVEL_1)
       // {
       //    Debug.println(1, this, "Resource key "+prefix+"."+property+" isn't defined");
       // }
     }

     prefix.setLength(oldLength);
  }

   private Object getSetterParameter(Class type, String key)
   {
      if (javax.swing.Icon.class.isAssignableFrom(type))
      {
         return getImage(getString(key));
      }

      if (java.lang.Character.TYPE.isAssignableFrom(type))
      {
         return new Character(getString(key).charAt(0));
      }

      return getString(key);
   }


}

//
// $Log: not supported by cvs2svn $
//