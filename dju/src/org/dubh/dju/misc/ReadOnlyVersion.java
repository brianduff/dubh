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
 *  0.0  0.0.01 [22/Nov/1998] BD     Initial Revision
 *
 */

package dubh.utils.misc;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;

/**
 * <p>
 * Represents a product's name, version and copyright information.
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 0.0.01) [22/Nov/1998]
 */
public interface ReadOnlyVersion
{
   public int    getMajorVersion();   
   public int    getMinorVersion();   
   public int    getMicroVersion();   
   public int    getBuildNumber();   
   public String getBuildLabel();   
   public String getProductName();   
   public String getProductCopyright();   
   public Date   getReleaseDate();
   public String getVersionDescription(String format);
   public String getShortDescription();
   public String getLongDescription();   
}