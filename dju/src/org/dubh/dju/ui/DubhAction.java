/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package dubh.utils.ui;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import dubh.utils.misc.Debug;
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
           Debug.println("DubhAction: Invocation Exception: "+ex.getTargetException());
        } catch (IllegalAccessException ill) {
           Debug.println("DubhAction: Illegal Access to "+srcMethod+": "+ill);
        }
     } else {
        //Debug.println("DubhAction: Action "+getCommand()+" not defined.");
     }
        
  }
}