// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseControl.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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

import java.text.MessageFormat;

import java.io.IOException;

import com.tektools.kawa.plugin.*;

/**
 * This provides a wrapper to clearcase, spawning cleartool.exe
 * to do the real work of checking in and out files.
 * @author Brian Duff, bduff@uk.oracle.com
 * @version $Id: ClearCaseControl.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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
//