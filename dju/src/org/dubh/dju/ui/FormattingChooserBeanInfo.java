// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormattingChooserBeanInfo.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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

package  org.dubh.dju.ui;

import java.beans.*;

/**
 * Bean Information class for the FormattingChooser.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [17/06/98]: Initial Revision
 * </UL>
 @author Brian Duff
 @version 0.1 [17/06/98]
 */
public class FormattingChooserBeanInfo extends DubhBasicBeanInfo {
  public FormattingChooserBeanInfo()  {
    beanClass = FormattingChooser.class;
    beanDescription =
        "Provides a panel that the user can choose a font and colour from";
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
     try {
        return new PropertyDescriptor[] {
           property("formatColor", "The color"),
           property("formatFont",  "The font"),
           property("enabled", "Whether the formatting chooser is enabled")
        };
     } catch (IntrospectionException e) {
        return super.getPropertyDescriptors();
     }
  }
}