package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

/**
 * An abstract toggle action is an action that has an internal
 * state (true or false). Triggering the action causes the internal
 * state to toggle to the other value. It can be used for things
 * like checkbox menu items
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 */
public abstract class AbstractToggleAction extends AbstractClientAction
{
   public static final String TOGGLE_STATE = "togglestate";

   public AbstractToggleAction()
   {
      super();
      setToggledOn(getInitialToggleState());
   }

   /**
    * You can override this to return a different inital toggle
    * state. The default is false.
    */
   protected boolean getInitialToggleState()
   {
      return false;
   }

   /**
    * Subclasses can use this to determine if the toggle action is currently
    * toggled on.
    */
   public boolean isToggledOn()
   {
      try
      {
         return ((Boolean)getValue(TOGGLE_STATE)).booleanValue();
      }
      catch (NullPointerException npe)
      {
         throw new IllegalStateException("Toggle is neither on or off!");
      }
   }

   /**
    * Programmatically changes the toggle state. You must call
    * toggleStateChanged yourself, as it is only called if the user
    * changes the toggle state of the action.
    */
   protected void setToggledOn(boolean b)
   {
      putValue(TOGGLE_STATE, b?Boolean.TRUE:Boolean.FALSE);
   }


   /**
    * Subclasses should not override this; it just changes the toggle
    * state of the action. You should override toggleStateChanged()
    * if you actually want to do something when the toggle state
    * changes
    */
   final public void actionPerformed(ActionEvent e)
   {
      setToggledOn(!isToggledOn());
      toggleStateChanged();
   }


   /**
    * Subclasses can override this to do whatever they want whenever
    * the toggle state changes. By default, does nothing.
    */
   protected void toggleStateChanged()
   {

   }
}