
package org.dubh.apps.ewatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * The event queue is a simple list of events that are currently alive.
 * <P>
 * @author Brian Duff
 */
public class EventQueue
{
   /**
    * The queue is actually implemented as a list - we encapsulate it to make
    * it semi type safe and to hide the implementation.
    */
   private List m_queueList;

   /**
    * A list of listeners for notification of queue updates. These are usually
    * UI objects that display the list or whatever (e.g. swing components,
    * or maybe something that writes out an HTML page / servlet or whatever)
    * You might not need to use this if you can base your model on the
    * event queue directly.
    */
   private List m_queueChangeListeners = new ArrayList();

   public void addEventQueueUpdateListener(EventQueueUpdateListener equl)
   {
      m_queueChangeListeners.add(equl);
   }

   public void removeEventQueueUpdateListener(EventQueueUpdateListener equl)
   {
      m_queueChangeListeners.remove(equl);
   }

   private void notifyAdd(Event e)
   {
      for (int i=0; i < m_queueChangeListeners.size(); i++)
      {
         ((EventQueueUpdateListener)m_queueChangeListeners.get(i)).eventAdded(e);
      }
   }

   private void notifyRemove(Event e)
   {
      for (int i=0; i < m_queueChangeListeners.size(); i++)
      {
         ((EventQueueUpdateListener)m_queueChangeListeners.get(i)).eventRemoved(e);
      }
   }

   /**
    * How many events are in the queue?
    */
   public synchronized int getSize()
   {
      return getList().size();
   }

   /**
    * Get an iterator over the queue
    */
   public synchronized Iterator getIterator()
   {
      return getList().iterator();
   }

   /**
    * Get a specific event from the queue
    */
   public Event getEventAt(int position)
   {
      return (Event)getList().get(position);
   }

   /**
    * Add an event to the queue
    */
   public synchronized Event add(Event e)
   {
      getList().add(e);
      notifyAdd(e);
      return e;
   }

   /**
    * Remove an event from the queue
    */
   public synchronized Event remove(Event e)
   {
      getList().remove(e);
      notifyRemove(e);
      return e;
   }

   public synchronized boolean contains(Event e)
   {
      return getList().contains(e);
   }

   /**
    * Respond to an event. The event responder is invoked for the specified
    * event. If the event lifetime is LIFETIME_RESPOND or LIFETIME_TIMEOUT_OR_RESPOND,
    * the event will be removed from the queue.
    */
   public synchronized void respond(Event e)
   {
      EventType eType = e.getEventType();

      eType.getEventResponder().respond(e);
      if (eType.getLifetime() == EventType.LIFETIME_RESPOND ||
          eType.getLifetime() == EventType.LIFETIME_TIMEOUT_OR_RESPOND)
      {
         remove(e);
      }
   }

   /**
    * Slaughters any dead events from the queue. This generally applies to
    * events with a timeout - events that die only on respond must be killed
    * by the object invoking the Responder.
    */
   public synchronized void expungeExpiredEvents()
   {
      Date now = new Date();
      Calendar dateOfDeath = Calendar.getInstance();
      
      for (int i=0; i < getSize(); i++)
      {
         Event e = getEventAt(i);
         EventType etype = e.getEventType();

         if (etype.getLifetime() == EventType.LIFETIME_TIMEOUT ||
             etype.getLifetime() == EventType.LIFETIME_TIMEOUT_OR_RESPOND)
         {
            dateOfDeath.setTime(e.getTimeStamp());
            dateOfDeath.add(Calendar.MINUTE, etype.getLifespanMinutes());

            Date d = dateOfDeath.getTime();
            if (now.after(d))
            {
               remove(e);
            }
         }
      }
   }

   /**
    * Get the list for the queue
    */
   private List getList()
   {
      if (m_queueList == null)
      {
         m_queueList = new ArrayList();
      }

      return m_queueList;
   }


   public static interface EventQueueUpdateListener extends EventListener
   {
      public void eventAdded(Event e);

      public void eventRemoved(Event e);
   }
}

 