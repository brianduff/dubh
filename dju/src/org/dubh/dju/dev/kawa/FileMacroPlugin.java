// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FileMacroPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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
import java.io.*;
import java.util.*;

import org.javalobby.dju.dev.kawa.plugin.DubhKawaPlugin;
/**
 * Kawa plugin that reads from a specified file and pastes
 * its contents into the Kawa editor.
 * @author Brian Duff, dubh@btinternet.com
 * @version $Id: FileMacroPlugin.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 */
public class FileMacroPlugin extends DubhKawaPlugin
{
   private static Hashtable m_storedText = new Hashtable();

   /**
    * Args as follows:
    *  0: the full path to the file to insert
    *  1: current path to file
    */
   public static void main(String[] args)
   {
      if (args.length < 1)
      {
         KawaApp.out.println("Not enough arguments to the FileMacroPlugin plugin.");
         System.exit(1);  
      }
   
      String text = (String) m_storedText.get(args[0]);
      
      if (text == null)
      {
         text = "";
         try
         {
            String line=" ";
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            while (line != null)
            {
               line = br.readLine();
               if (line != null) text = text + line + "\n";
            }
            m_storedText.put(args[0], text);
            br.close();
         }
         catch (IOException ioe)
         {
            KawaApp.out.println("Unable to read macro file: "+args[0]);
            return;
         }
      }   
      
      //
      // This is unpleasant, kawa plugin still has no way of getting the
      // current editor (only getting the editor for a particular file).
      //
      KawaEditor ed = getFile(args[1]).getEditor(true);
      
      ed.paste(text);
      
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//