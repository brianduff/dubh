// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseKawaPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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
package dubh.utils.dev.kawa.plugin.clearcase;

import com.tektools.kawa.plugin.*;
import java.util.Enumeration;

import dubh.utils.dev.kawa.plugin.DubhKawaPlugin;

/**
 * The base class providing common methods between all 
 * clearcase kawa plugins.
 * @author Brian Duff
 * @version $Id: ClearCaseKawaPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
 */
class ClearCaseKawaPlugin extends DubhKawaPlugin
{
   /**
    * Set the editor status of a file to "checked in"
    */
   static void setCheckedIn(String filename)
   {
      KawaFile f = getFile(filename);
      f.setSCMStatus(KawaApp.SCMSTATUS_CHECKEDIN);   
   }
   
   /**
    * Set the editor status of a file to "checked out"
    */
   static void setCheckedOut(String filename)
   {
   
      KawaFile f = getFile(filename);
      f.setSCMStatus(KawaApp.SCMSTATUS_CHECKEDOUT);   
   }
}

//
// $Log: not supported by cvs2svn $
//