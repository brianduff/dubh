// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhBasicBeanInfo.java,v 1.4 1999-11-11 21:24:35 briand Exp $
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
package org.javalobby.dju.ui;

import java.beans.*;
import java.lang.reflect.*;

/**
 * Subclass of SimpleBeanInfo with utilities. Subclass this class to provide
 * a JavaBean BeanInfo class for beans. Example: <PRE>
 *   public class SampleBeanInfo extends DubhBasicBeanInfo {
 *
 *      public SampleBeanInfo() {
 *         beanClass = SimpleBean.class;
 *         beanDescription = "This is a sample Java Bean";
 *      }
 *
 *      public Image getIcon(int kind) {
 *         return loadImage("myicon.gif");
 *      }
 *
 *      public PropertyDescriptor[] getPropertyDescriptors() {
 *         try {
 *            return new PropertyDescriptor[] {
 *               property("title", "The title"),
 *               property("size",  "the size (in points)")
 *            };
 *         } catch (Exception e) {
 *            return super.getPropertyDescriptors();
 *         }
 *      }
 *
 *      public MethodDescriptor[] getMethodDescriptors() {
 *         try {
 *            return new MethodDescriptor[] {
 *               method("play", "Plays the tune"),
 *               method("track", "Goes to the specified track number",
 *                  new ParameterDescriptor[] {
 *                     parameter("tracknum", "The track number to go to")
 *                  },
 *                  new Class[] {
 *                     Integer.TYPE
 *                  }
 *               )
 *            }
 *         } catch (Exception e) {
 *            return super.getMethodDescriptors();
 *         }
 *      }
 *   }
 * </PRE>
 * Version History: <UL>
 * <LI>0.1 [17/06/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [17/06/98]
 */
public class DubhBasicBeanInfo extends SimpleBeanInfo {

  /** The class that this BeanInfo object is providing information about. */
  protected Class beanClass;
  /** A description of this bean */
  protected String beanDescription;

  /**
   * Return a PropertyDescriptor object for the given property. You should
   * define the beanClass field before calling this method.
   */
  protected PropertyDescriptor property(String name, String description)
    throws IntrospectionException {
     PropertyDescriptor p = new PropertyDescriptor(name, beanClass);
     p.setShortDescription(description);
     return p;
  }

  /**
   * Return a PropertyDescriptor object for a property which has a
   * customizer class.
   */
  protected PropertyDescriptor property(String name, String description,
        Class editor)
    throws IntrospectionException {
     PropertyDescriptor p = property(name, description);
     p.setPropertyEditorClass(editor);
     return p;
  }

  /**
   * Return a MethodDescriptor object for the given method. You should define
   * the beanClass field before calling this method. This version of
   * method() creates a parameterless method.
   */
  protected MethodDescriptor method(String name, String description)
    throws NoSuchMethodException, SecurityException {
     Method m = beanClass.getMethod(name, new Class [] {});
     MethodDescriptor md = new MethodDescriptor(m);
     md.setShortDescription(description);
     return md;
  }

  /**
   * Return a MethodDescriptor object for the given method. The method
   * can have parameters, as defined by an array of parameter descriptor
   * objects (use the parameter() method to create these) and parameter
   * types.
   */
  protected MethodDescriptor method(String name, String description,
        ParameterDescriptor[] parameters, Class[] parameterTypes)
    throws NoSuchMethodException, SecurityException {
     Method m = beanClass.getMethod(name, parameterTypes);
     MethodDescriptor md = new MethodDescriptor(m, parameters);
     md.setShortDescription(description);
     return md;
  }

  /**
   * Return a ParameterDescriptor object for a method parameter.
   */
  protected ParameterDescriptor parameter(String name, String description) {
     ParameterDescriptor pd = new ParameterDescriptor();
     pd.setName(name);
     pd.setShortDescription(description);
     return pd;
  }

  /**
   * Get a description of this bean.
   */
  public BeanDescriptor getBeanDescriptor() {
     BeanDescriptor bd = new BeanDescriptor(beanClass);
     bd.setShortDescription(beanDescription);
     return bd;
  }


}