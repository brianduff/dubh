// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Test.java,v 1.4 1999-03-22 23:37:17 briand Exp $
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

package dubh.utils.dev.bugdb;

import java.sql.*;
import postgresql.util.UnixCrypt;

/**
 * Class to test JDBC connectivity to the database on wired
 * @author Brian Duff
 * @version 1.0
 */
class Test
{
   public static void main(String[] args)
   {
      try
      {
         Class.forName("postgresql.Driver");
         
         Connection db = DriverManager.getConnection("jdbc:postgresql://wired.dcs.st-and.ac.uk/briand",
                                                     "briand", "buniba65");
                                                     
         Statement st = db.createStatement();
         
         ResultSet res = st.executeQuery("select * from bg_operating_systems");
         
         while (res.next())
         {
            System.out.println(res.getString(1));
         }
         res.close();
         st.close();
      }
      catch (Throwable cnfe)
      {
         System.err.println("Got exception: "+cnfe);
      }   
   }
}