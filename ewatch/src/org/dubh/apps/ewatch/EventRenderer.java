
// Copyright (c) 2000 
package org.dubh.apps.ewatch;

import java.awt.Component;

/**
 * An EventRenderer is responsible for drawing an event. There is a single
 * method in this interface which must return a Component object which is
 * used to display the event. Like renderers for other Swing components, this
 * renderer is completely non interactive - e.g. if you return a JButton, it
 * will look like a button but won't act like one.
 * <P>
 * @author Brian Duff
 */
public interface EventRenderer
{
   /**
    * This method should return a Component that can be used to display the
    * specified event.
    */
   public Component getEventRendererComponent(Event e);
}

 