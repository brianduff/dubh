
// Copyright (c) 2000 
package org.dubh.apps.ewatch;

/**
 * Represents a type of event that can occur.
 *
 * @author Brian Duff
 */
public interface EventType
{
   /**
    * Events of this type live forever if the getLifetime() method returns
    * this value. You wouldn't want to use this very often, as it would
    * permanently prevent an event from disappearing.
    */
   public static final Lifetime LIFETIME_FOREVER = new Lifetime();

   /**
    * Events of this type have a fixed lifespan in minutes. It is guaranteed
    * only that the event will eventually die some time after this fixed
    * number of minutes has passed.
    */
   public static final Lifetime LIFETIME_TIMEOUT = new Lifetime();

   /**
    * Events of this type live until the user responds to them.
    */   
   public static final Lifetime LIFETIME_RESPOND = new Lifetime();

   /**
    * Events of this type live until the user responds to them or a fixed
    * lifespan has elapsed since they were triggered, whichever happens first.
    * This is effectively a combination of LIFESPAN_TIMEOUT and
    * LIFESPAN_RESPOND
    */
   public static final Lifetime LIFETIME_TIMEOUT_OR_RESPOND = new Lifetime();


   /**
    * Get the name of this event type
    */
   public String getName();

   /**
    * Get a renderer that is used to display events of this type
    */
   public EventRenderer getEventRenderer();

   /**
    * Get an object that can be used when the user responds to an event. This
    * method may return null if there is no responder.
    */
   public EventResponder getEventResponder();

   /**
    * How long do events of this type live? Use one of the three
    * LIFETIME_ constants.
    */
   public EventType.Lifetime getLifetime();

   /**
    * Get the timeout if this event type has a LIFETIME_TIMEOUT. This is
    * expressed in minutes from the timestamp of the event. The value of this
    * method is ignored if the lifetime of this event type is not
    * LIFETIME_TIMEOUT
    */
   public int getLifespanMinutes();

   /**
    * Every type of event must have an object that knows how to detect events
    * of this type and add them to the queue. The object that does this is
    * known as a Watcher.
    */
   public Watcher getWatcher();

   /**
    * Every type of event has a customizer that is used to display UI that
    * modifies the state of the event type. This allows the user to customize
    * the behaviour of event types.
    */
   public EventTypeCustomizer getEventTypeCustomizer();

   /**
    * Typed enumerator for Lifetime
    */
   public static final class Lifetime {}
}

 