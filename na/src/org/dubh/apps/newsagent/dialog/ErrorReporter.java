// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: ErrorReporter.java,v 1.7 2001-02-11 15:40:18 briand Exp $
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

package org.dubh.apps.newsagent.dialog;

import javax.swing.*;
import java.awt.*;
import org.dubh.apps.newsagent.GlobalState;
import java.text.MessageFormat;
import org.dubh.dju.misc.Debug;

/**
 * Responsible for communicating errors to the user.<P>
 * @author Brian Duff
 * @version $Id: ErrorReporter.java,v 1.7 2001-02-11 15:40:18 briand Exp $
 */
public class ErrorReporter {

// Public Static Constants

   private static final String dlgTitle = GlobalState.getApplicationInfo().getName();

// Private Static Constants

// Private / Protected Attributes

// Public Constructors

   /**
    * Default Constructor. Enter Description Here.
    */
    public ErrorReporter() {

    }

    private static void dodialog(String title, String message, int type) {
      JOptionPane.showMessageDialog(new JFrame(), message, title, type);
      }

     /**
      * Displays a dialog to the user where he/she can enter a string.
      */
     public static String getInput(String key) {
        return JOptionPane.showInputDialog(GlobalState.getRes().getString(key));
     }

    /**
     * Displays a yes/no question to the user.
     @return true if the user said yes, no otherwise.
     */
    public static boolean yesNo(String key) {

     return ((JOptionPane.showConfirmDialog(new JFrame(), GlobalState.getRes().getString(key),
        dlgTitle+" "+GlobalState.getRes().getString("question"),
        JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION);


    }

    /**
     * Displays a yes/no question to the user.
     @return true if the user said yes, no otherwise.
     */
    public static boolean yesNo(String key, Object[] subst) {

     return ((JOptionPane.showConfirmDialog(new JFrame(), MessageFormat.format(GlobalState.getRes().getString(key), subst),
        dlgTitle+" "+GlobalState.getRes().getString("question"),
        JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION);


    }

    /**
     * Displays a warning to the user.
     @param key The key of the message from the String Bundle resource to use
     in this warning dialog.
     */
    public static void warn(String key) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("warning"),
                   GlobalState.getRes().getString(key),
               JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays an error to the user.
     @param key The key of the message from the String bundle resource to use
     in this error dialog.
     */
    public static void error(String key) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("error"),
                   GlobalState.getRes().getString(key),
               JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a fatal error to the user and terminates the application.
     * Should be used for serious internal errors only.
     @param key The key of the message from the String bundle resource to use
     in this fatal error dialog.
     */
    public static void fatality(String key) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("fatal"),
                   GlobalState.getRes().getString(key),
               JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }

    /**
     * Displays a warning to the user.
     @param key The key of the message from the String Bundle resource to use
     in this warning dialog.
     @param subst An array of Objects to replace %? placeholders in resource
     strings.
     */
    public static void warn(String key, Object[] subst) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("warning"),
                   MessageFormat.format(GlobalState.getRes().getString(key), subst),
               JOptionPane.WARNING_MESSAGE);
    }
    /**
     * Displays an error to the user.
     @param key The key of the message from the String bundle resource to use
     in this error dialog.
     @param subst An array of Objects to replace %? placeholders in resource
     strings.
     */
    public static void error(String key, Object[] subst) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("error"),
                   MessageFormat.format(GlobalState.getRes().getString(key), subst),
               JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a fatal error to the user and terminates the application.
     * Should be used for serious internal errors only.
     @param key The key of the message from the String bundle resource to use
     in this fatal error dialog.
     @param subst An array of Objects to replace %? placeholders in resource
     strings.
     */
    public static void fatality(String key, Object[] subst) {
      dodialog(dlgTitle+" "+GlobalState.getRes().getString("fatal"),
                   MessageFormat.format(GlobalState.getRes().getString(key), subst),
               JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }

}

//
// Old Version History:
// <LI>0.1 [17/02/98]: Initial Revision
// <LI>0.2 [27/02/98]: Made methods static. Changed methods for Swing 1.0, you
//    <b>must</b> provide a parent JFrame for all static calls (or deadlock).
//    Addendum: Providing a parent doesn't actually work. The deadlock problem
//    is a known bug in JDK < 1.1.4 (and not Swing's fault). The problem goes
//    away on newer JREs. JBuilder uses an old JRE, which is why I noticed it.
// <LI>0.3 [03/03/98]: Updated to use GlobalState for application name.
// <LI>0.4 [05/03/98]: Now takes a string resource bundle key for the message,
//       and no longer takes parents for any message box.
// <LI>0.5 [23/03/98]: Added Yes/No dialog.
// <LI>0.6 [24/03/98]: Added Input dialog.
// <LI>0.7 [23/04/98]: Disabled debug output for release version.
// <LI>0.8 [29/04/98]: Reenabled debug output if preference is set or -debug
//     flag used (see GlobalState.debugOn)
// <LI>0.9 [08/06/98]: Replaced debug() method with a call to the dubh utils
//      Debug.println method. <b>Use the Dubh Utils method directly in all
//      code from now on!!!</b>, this will help to make debug output
//      consistent.
//
// New version history:
//
// $Log: not supported by cvs2svn $
// Revision 1.6  2001/02/11 02:50:59  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:23:40  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog.
//
//