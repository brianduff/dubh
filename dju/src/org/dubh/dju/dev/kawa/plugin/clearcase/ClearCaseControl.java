// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseControl.java,v 1.2 1999-11-11 21:24:34 briand Exp $
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

import java.text.MessageFormat;

import java.io.IOException;

import com.tektools.kawa.plugin.*;

/**
 * This provides a wrapper to clearcase, spawning cleartool.exe
 * to do the real work of checking in and out files.
 * @author Brian Duff, dubh@btinternet.com
 * @version $Id: ClearCaseControl.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 * 
 */
class ClearCaseControl
{
   private static ClearCaseControl m_singleton = null;

   private String m_cleartool;

   /**
    * Template for the checkout command
    */
   private static final String s_CO_COMMAND =
      "{0} co -c \"{1}\" -reserved {2}";
      
   /**
    * Template for the checkin command
    */   
   private static final String s_CI_COMMAND = 
      "{0} ci -c \"{1}\" {2}";
   
   /**
    * Template for the undo checkout command
    */   
   private static final String s_UNCO_COMMAND = 
      "{0} unco -rm {2}";

   /**
    * Don't directly instantiate ClearCaseControl, instead use
    * the static {@link getInstance(String)} method. This returns
    * a shared static singleton instance.
    */
   protected ClearCaseControl(String cleartool)
   {
      m_cleartool = cleartool;
   }
   
   
   /**
    * Get a shared singleton instance of this class.
    */
   public static ClearCaseControl getInstance(String cleartool)
   {
      if (m_singleton == null)
      {
         m_singleton = new ClearCaseControl(cleartool);
      }
      
      return m_singleton;
   }
   
   /**
    * Checkout the specified file.
    * @param name full pathname of file
    * @param comments comments for source control
    * @return the exit status of cleartool.exe.
    */
   public int checkOut(String name, String comments)
   {
      String cmd = getCommand(s_CO_COMMAND, name, comments);
      
      return runProcess(cmd);
   }

   /**
    * Checkin the specified file.
    * @param name full pathname of file
    * @param comments comments for source control
    * @return the exit status of cleartool.exe.
    */   
   public int checkIn(String name, String comments)
   {
      String cmd = getCommand(s_CI_COMMAND, name, comments);
      
      return runProcess(cmd);
      
   }
   
   /**
    * Undo checkout of the specified file.
    * @param name full pathname of file
    * @return the exit status of cleartool.exe.
    */      
   public int undoCheckOut(String name)
   {
      String cmd = getCommand(s_UNCO_COMMAND, name, "");
      
      return runProcess(cmd);
   }
  
   
   /**
    * Run an external process and wait for it to complete.
    */
   private int runProcess(String command)
   {
      try
      {
         KawaApp.out.println("Running command: "+command);
      
         Process proc = Runtime.getRuntime().exec(command);
      
         KawaApp.out.println("Waiting for command to complete");
         
         proc.waitFor();
      
         KawaApp.out.println("Command completed with exit status "+proc.exitValue());
      
         return proc.exitValue(); 
      }
      catch (InterruptedException ie)
      {
         KawaApp.out.println("Process was interrupted:"+ie);
         return -1;
      }
      catch (IOException ioe)
      {
         KawaApp.out.println("IO Exception executing process: "+ioe);
         return -1;
      }
   }
   
   /**
    * Construct the command line
    */
   private String getCommand(String base, String name, String comments)
   {
      return MessageFormat.format(base, new String[] { m_cleartool, comments, name });
   }   
      
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//