
package org.dubh.apps.ewatch;

/**
 * An EventResponder is used to determine what to do when events of a particular
 * type are responded to by the user. 
 * <P>
 * @author Brian Duff
 */
public interface EventResponder
{
   /**
    * Respond to the specified event.
    * @param e the event to respond to.
    */
   public void respond(Event e);
}

 