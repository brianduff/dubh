// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseCheckIn.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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

/**
 * Kawa plugin that checks ClearCase files in
 * @author Brian Duff, bduff@uk.oracle.com
 * @version $Id: ClearCaseCheckIn.java,v 1.1 1999-04-10 20:38:53 briand Exp $
 */
public class ClearCaseCheckIn extends ClearCaseKawaPlugin
{

   /**
    * Args as follows:
    *  0: the full path to cleartool.exe (including filename)
    *  1: the fully qualified name of the file to check in
    */
   public static void main(String[] args)
   {
      if (args.length < 2)
      {
         KawaApp.out.println("Not enough arguments to the ClearCase checkin plugin.");
         return;
      }
      
      String comments = ClearCaseComments.doDialog("");
   
      if (ClearCaseControl.getInstance(args[0]).checkIn(args[1], comments) == 0)   
         setCheckedIn(args[1]);
      else
         KawaApp.out.println("Unable to check in.");
   }


}


//
// $Log: not supported by cvs2svn $
//