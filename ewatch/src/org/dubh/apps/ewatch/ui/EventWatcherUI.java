package org.dubh.apps.ewatch.ui;

import org.dubh.apps.ewatch.EventTypeRegistrar;

/**
 * This simple interface should be implemented by a component which
 * provides a UI for displaying events. In this way, the UI can be used
 * in conjunction with the generic configuration dialog UI to register
 * and configure event types.
 * <P>
 * @author Brian Duff (brian@dubh.co.uk)
 */
public interface EventWatcherUI
{
   /**
    * Get the event type registrar that the event watcher ui is using.
    * @return an instance of EventTypeRegistrar 
    */
   public EventTypeRegistrar getEventTypeRegistrar();
}

 