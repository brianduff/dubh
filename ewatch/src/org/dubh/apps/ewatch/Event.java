
// Copyright (c) 2000 Brian Duff
package org.dubh.apps.ewatch;


import java.util.Date;
/**
 *
 *
 * @author Brian.Duff@oracle.com
 */
public interface Event
{
   /**
    * Get a name for the event.
    */
   public String getName();

   /**
    * Get an event attribute. This can be used by the EventRenderer to
    * change the way in which the event is displayed.
    */
   public Object getAttribute(Object attributeKey);

   /**
    * Get a timestamp for the event. This can be used to determine when the
    * event occurred.
    */
   public Date getTimeStamp();

   /**
    * Get the type of event. Each event has a corresponding type, which must
    * be registered.
    */
   public EventType getEventType();
}

 