package org.dubh.apps.ewatch;

/**
 * A watcher watches for events. When those events occur, the watcher adds
 * events to the event queue. 
 * <P>
 * @author Brian Duff
 */
public abstract class Watcher
{
   /**
    * The event queue
    */
   private EventQueue m_queue;

   /**
    * When an event occurs, you must call this method. It simply adds your
    * event to the queue.
    */
   public final void eventOccurred(Event e)
   {
      if (m_queue != null)
      {
         m_queue.add(e);
      }
   }

   /**
    * You can use this in your watcher to determine if an event already
    * exists in the event queue. This will return true if the equals method
    * returns true for any event in the queue and the specified event.
    */
   public boolean isEventInQueue(Event e)
   {
      return (m_queue.contains(e)); // BD: Check this uses equals and not ==
   }

   /**
    * Your watcher should start watching for events.
    */
   public abstract void startWatching();

   /**
    * Your watcher should stop watching for events. There is no guarantee that
    * the watcher will ever be started again, likewise the watcher may well
    * be started again.
    */
   public abstract void stopWatching();

   /**
    * Return true if your watcher is currently watching.
    */
   public abstract boolean isWatching();

   /**
    * This is guaranteed to be called on all watchers by the EventTypeRegistrar
    */
   void setEventQueue(EventQueue q)
   {
      m_queue = q;
   }
}

 