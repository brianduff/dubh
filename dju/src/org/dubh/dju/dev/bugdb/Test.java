// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Test.java,v 1.5 1999-11-11 21:24:34 briand Exp $
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

package org.javalobby.dju.dev.bugdb;

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