// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ErrorManager.java,v 1.2 2001-02-11 02:52:10 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
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


package org.dubh.dju.error;


import javax.swing.JPanel;
import java.text.MessageFormat;

import org.dubh.dju.misc.Debug;

/**
 * The error manager can be used to display error messages. This class has not
 * been fully implemented yet.
 *
 * @author Brian Duff
 * @version $Id: ErrorManager.java,v 1.2 2001-02-11 02:52:10 briand Exp $
 */
public class ErrorManager
{
   /**
    * Display an error message
    */
   public static void display(JPanel pan, String mess, Object[] subst)
   {
      String substMess = MessageFormat.format(mess, subst);
      display(pan, substMess);

   }

   public static void display(JPanel pan, String mess)
   {

      if (Debug.TRACE_LEVEL_1)
      {
         Debug.println(1, ErrorManager.class,
            "Should display UI Error Message: "+mess);
      }
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/12/12 00:47:41  briand
// nitial revision
//
//