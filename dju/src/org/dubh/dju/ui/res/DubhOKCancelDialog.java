/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please note that this software is not in any way endorsed by
 * Oracle Corporation
 * Version History:
 *  FV   DUV    Date          Who    What
 *  ======================================================================
 *  0.0  1.1.0 [08/Dec/1998] BD     Initial Revision
 *
 */

package dubh.utils.ui.res;

import java.util.ListResourceBundle;

/**
 * <p>
 * NLS resources for DubhOKCancelDialog
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.0) [08/Dec/1998]
 */
public class DubhOKCancelDialog extends ListResourceBundle
{
   public static final Object[][] m_contents = 
   {
      {"OKCancelPanel.cmdOK.text", "OK"},
      {"OKCancelPanel.cmdOK.toolTipText", "Click to dismiss this dialog and accept changes"},
      {"OKCancelPanel.cmdCancel.text", "Cancel"},
      {"OKCancelPanel.cmdCancel.toolTipText", "Click to dismiss this dialog and discard changes"}
   };
   
   public final Object[][] getContents()
   {
      return m_contents;
   }
}
