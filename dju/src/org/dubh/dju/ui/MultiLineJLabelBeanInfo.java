// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MultiLineJLabelBeanInfo.java,v 1.3 1999-03-22 23:37:18 briand Exp $
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
package  dubh.utils.ui;

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