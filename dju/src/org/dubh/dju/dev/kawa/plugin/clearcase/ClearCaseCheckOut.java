// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseCheckOut.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
package org.javalobby.dju.dev.kawa.plugin.clearcase;

import com.tektools.kawa.plugin.*;

/**
 * Kawa plugin that checks clearcase files in
 * @author Brian Duff, dubh@btinternet.com
 * @version $Id: ClearCaseCheckOut.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 */
public class ClearCaseCheckOut extends ClearCaseKawaPlugin
{

   /**
    * Args as follows:
    *  0: the full path to cleartool.exe (including filename)
    *  1: Full path of file
    */ 
   public static void main(String[] args)
   {
      if (args.length < 2)
      {
         KawaApp.out.println("Not enough arguments to the ClearCase checkin plugin."); 
         return;
      }
      
      String comments = ClearCaseComments.doDialog("");
   
      if (ClearCaseControl.getInstance(args[0]).checkOut(args[1], comments) == 0)
         setCheckedOut(args[1]);
      else
         KawaApp.out.println("Unable to check out");
   }


}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//