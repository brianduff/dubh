// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhAction.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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


package org.dubh.dju.ui;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.dubh.dju.misc.Debug;
/**
 * A convenience Action subclass that uses reflection to simplify programming
 * Action interfaces. The Action is associated with an actionCommand string.
 * When the action is fired, a parameterless method called the same as the
 * actionCommand in a specified object instance is executed.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [18/06/98]: Initial Revision
 * <LI>0.2 [30/06/98]: Minor bugfix - if an invokation exception ocurred during
     dispatching of the action, the event (rather than the exception) was
     printed to the debug channel.
 * <LI>0.3 [06/06/98]: Slight mod to debug code, 'cos Swing 1.0.2 removed the
 *   AbstractAction.getCommand() method.
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.3 [06/06/98]
 */
public class DubhAction extends AbstractAction {
  protected Object srcObject;
  protected Method srcMethod;

  /**
   * Create a DubhAction for the specified object and action command
   @param forObject the object instance to invoke methods in when the action
     is fired (usually this).
   @param actionCommand the actionCommand of the Action. This is also the name
     of a parameterless public method that will be invoked in forObject when
     the action is fired.
   */
  public DubhAction(Object forObject, String actionCommand) {
     super(actionCommand);
     srcObject = forObject;
     try {
        srcMethod = forObject.getClass().getMethod(actionCommand, null);
     } catch (NoSuchMethodException e) {
        srcMethod = null;
        setEnabled(false);
     }
  }

  /**
   * Called when the action is fired
   */
  public void actionPerformed(ActionEvent e) {
     /** Invoke the method */
     if (srcMethod != null) {
        try {
           srcMethod.invoke(srcObject, new Object[] {});
        } catch (InvocationTargetException ex) {
           if (Debug.TRACE_LEVEL_1)
           {
              Debug.println(1, this, "DubhAction: Invocation Exception");
              Debug.printException(1, this, ex.getTargetException());
           }
        } catch (IllegalAccessException ill) {
           Debug.println("DubhAction: Illegal Access to "+srcMethod+": "+ill);
        }
     } else {
        //Debug.println("DubhAction: Action "+getCommand()+" not defined.");
     }

  }
}