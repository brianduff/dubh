// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MultiLineJLabelBeanInfo.java,v 1.4 1999-11-11 21:24:36 briand Exp $
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
package  org.javalobby.dju.ui;

import java.beans.*;
/**
 * Bean Information class for the Muli.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [17/06/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [17/06/98]
 */
public class MultiLineJLabelBeanInfo extends DubhBasicBeanInfo {
  public MultiLineJLabelBeanInfo()  {
    beanClass = MultiLineJLabel.class;
    beanDescription =
        "Provides a label that understands newline characters";
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
     try {
        return new PropertyDescriptor[] {
           property("alignment", "The alignment of the text"),
           property("marginHeight",  "The height of the margin"),
           property("marginWidth", "The width of the margin"),
           property("text", "The text contained in the label")
        };
     } catch (IntrospectionException e) {
        return super.getPropertyDescriptors();
     }
  }
}