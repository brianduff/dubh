
package org.dubh.apps.ewatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Event types must be registered with an EventTypeRegistrar in order to
 * become active. The Registrar is effectively just a list of event types
 * that are active, however the registrar is also responsible for creating
 * the EventQueue and notifying 
 * <P>
 * @author Brian Duff
 */
public class EventTypeRegistrar
{
   /**
    * The event queue
    */
   private EventQueue m_queue;

   /**
    * The list of registered event types
    */
   private List m_registeredTypes;

   /**
    * The expirer is a special watcher that is responsible for expunging
    * expired events from the queue.
    */
   private Watcher m_expirer;

   /**
    * Construct the registrar.
    */
   public EventTypeRegistrar()
   {
      m_queue = new EventQueue();
      m_registeredTypes = new ArrayList();
      m_expirer = new ExpirerWatcher();
   }

   /**
    * Get the event queue
    */
   public EventQueue getEventQueue()
   {
      return m_queue;
   }

   /**
    * Register an event type with the registrar.
    */
   public void registerEventType(EventType et)
   {
      m_registeredTypes.add(et);
      et.getWatcher().setEventQueue(m_queue);
   }

   /**
    * Unregister an event type with the registrar.
    */
   public void unregisterEventType(EventType et)
   {
      m_registeredTypes.remove(et);
      if (et.getWatcher().isWatching())
      {
         et.getWatcher().stopWatching();
      }
      et.getWatcher().setEventQueue(null);
   }

   /**
    * Get an iterator over all currently registered event types.
    */
   public Iterator getTypeIterator()
   {
      return m_registeredTypes.iterator();
   }

   /**
    * Start all watchers.
    */
   public void setWatching(boolean isWatching)
   {
      for (int i=0; i < m_registeredTypes.size(); i++)
      {
         Watcher w = ((EventType)m_registeredTypes.get(i)).getWatcher();

         if (isWatching != w.isWatching())
         {
            if (isWatching)
            {
               w.startWatching();
            }
            else
            {
               w.stopWatching();
            }
         }
      }

      //
      // Run the expirer when we start watching, if it's not already watching.
      // we never stop the expirer, otherwise events would no longer be expired
      // after their life came to an end.
      if (isWatching && !m_expirer.isWatching())
      {
         m_expirer.startWatching();
      }
   }

   /**
    * The expirer watcher just expires dead events every thirty seconds.
    */
   class ExpirerWatcher extends ThreadWatcher
   {
      public ExpirerWatcher()
      {
         super(30000);
         setEventQueue(m_queue);
      }

      public void doCheck()
      {
         m_queue.expungeExpiredEvents();
      }
   }
}

 