// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StatusBarBeanInfo.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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


package org.dubh.dju.ui;

import java.beans.*;
/**
 * Bean Information class for the StatusBar.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [17/06/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [17/06/98]
 */
public class StatusBarBeanInfo extends DubhBasicBeanInfo {
  public StatusBarBeanInfo()  {
    beanClass = StatusBar.class;
    beanDescription =
        "Dubh Software Status Bar";
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
     try {
        return new PropertyDescriptor[] {
           property("icon", "The icon to display in the status bar"),
           property("maximumProgress",  "The maximum value of the progress bar"),
           property("minimumProgress", "The minimum value of the progress bar"),
           property("progress", "The current value of the progress bar"),
           property("progressVisible", "Whether the progress bar is visible"),
           property("text", "The text to display")
        };
     } catch (IntrospectionException e) {
        return super.getPropertyDescriptors();
     }
  }

  public MethodDescriptor[] getMethodDescriptors() {
     try {
        return new MethodDescriptor[] {
           method("clearText", "Clears the text in the status bar")
        };
     } catch (Exception e) {
        return super.getMethodDescriptors();
     }
  }

}