// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhKawaPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
package org.javalobby.dju.dev.kawa.plugin;

import com.tektools.kawa.plugin.*;
import java.util.Enumeration;

/**
 * The base class providing common methods between all 
 * dubh kawa plugins.
 * @author Brian Duff
 * @version $Id: DubhKawaPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 */
public class DubhKawaPlugin
{
   /**
    * Necessary to look up the correct instance of a KawaFile for
    * the current file in the project. Would be nice if there
    * was a method in KawaApp or KawaProject to do this.
    */
   protected static KawaFile getFile(String filename)
   {
      try
      {
         Enumeration files = KawaApp.getCurrentProject().enumerateAllFiles();
         
         while (files.hasMoreElements())
         {
            KawaFile f = (KawaFile) files.nextElement();
            
            if (f.getPath().equals(filename))
            {
               return f;
            }
         }
         
         KawaApp.out.println("Can't find "+filename+" in the project.");
         return new KawaFile(filename);
      }
      catch (Throwable t)
      {
         KawaApp.out.println("Unable to getFile("+filename+"): "+t);
         return new KawaFile(filename);
      }
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//