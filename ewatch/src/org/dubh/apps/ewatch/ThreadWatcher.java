
// Copyright (c) 2000 
package org.dubh.apps.ewatch;


/**
 * A subclass of Watcher that is based on a thread running and performing
 * some kind of check at a fixed interval. This is just a utility class
 * that makes it easy to implement this kind of watcher.
 * <P>
 * @author Brian Duff
 */
public abstract class ThreadWatcher extends Watcher
{
   private Thread m_thread;
   private boolean m_running;
   private int m_checkInterval;

   public ThreadWatcher(int checkIntervalMillis)
   {
      m_checkInterval = checkIntervalMillis;
   }

   private Thread getThread()
   {
      if (m_thread == null)
      {
         m_thread = new Thread(new Runnable() {
            public void run()
            {
               while (m_running)
               {
                  doCheck();

                  try
                  {
                     Thread.sleep(m_checkInterval);
                  }
                  catch (InterruptedException ie)
                  {
                     if (!m_running)
                     {
                        return;
                     }
                  }
               }
            }
         });
      }

      return m_thread;
   }


   /**
    * Your watcher should start watching for events.
    */
   public void startWatching()
   {
      m_running = true;
      getThread().start();
   }

   /**
    * Your watcher should stop watching for events. There is no guarantee that
    * the watcher will ever be started again, likewise the watcher may well
    * be started again.
    */
   public void stopWatching()
   {
      m_running = false;
      getThread().interrupt();
   }

   /**
    * Return true if your watcher is currently watching.
    */
   public boolean isWatching()
   {
      return m_running;
   }

   /**
    * Your subclass should carry out whatever check it needs to do, and
    * call eventOccurred() if the check succeeds.
    */
   public abstract void doCheck();

}

 