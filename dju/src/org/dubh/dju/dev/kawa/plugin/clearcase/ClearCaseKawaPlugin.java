// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseKawaPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
import java.util.Enumeration;

import org.javalobby.dju.dev.kawa.plugin.DubhKawaPlugin;

/**
 * The base class providing common methods between all 
 * clearcase kawa plugins.
 * @author Brian Duff
 * @version $Id: ClearCaseKawaPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//