package org.dubh.apps.ewatch;

import java.util.Date;
import java.util.HashMap;

/**
 * A Simple implementation of the Event interface. The only method you have
 * to implement is getEventType(). The event is constructed with a name, and
 * a timestamp is placed on the event when the constructor is called. You
 * can use the setAttribute() method to set attributes on the event, which
 * can be retrieved using the getAttribute() method on the Event interface.
 * <P>
 * @author Brian Duff
 */
public abstract class BasicEvent implements Event
{
   private Date m_timeStamp;
   private String m_name;
   private HashMap m_attributes;

   public BasicEvent(String name)
   {
      m_timeStamp = new Date();
      m_name = name;
      m_attributes = new HashMap();
   }

   /**
    * Get a name for the event.
    */
   public String getName()
   {
      return m_name;
   }

   /**
    * Get an event attribute. This can be used by the EventRenderer to
    * change the way in which the event is displayed.
    */
   public Object getAttribute(Object attributeKey)
   {
      return m_attributes.get(attributeKey);
   }

   /**
    * Set an attribute on the event.
    */
   public void setAttribute(Object attributeKey, Object attributeValue)
   {
      m_attributes.put(attributeKey, attributeValue);
   }

   /**
    * Get a timestamp for the event. This can be used to determine when the
    * event occurred.
    */
   public Date getTimeStamp()
   {
      return m_timeStamp;
   }

   /**
    * Get the type of event. Each event has a corresponding type, which must
    * be registered.
    */
   public abstract EventType getEventType();

}

 