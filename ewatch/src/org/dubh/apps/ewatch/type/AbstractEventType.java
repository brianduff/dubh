package org.dubh.apps.ewatch.type;

import org.dubh.apps.ewatch.*;

import java.awt.Font;
import java.awt.Color;


import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.javalobby.dju.misc.UserPreferences;

/**
 * A base class for event types. You don't have to subclass from this, but
 * it provides many useful utilities.
 *
 * @author Brian Duff
 */
public abstract class AbstractEventType implements EventType
{
   private static final EventRenderer m_renderer = new SimpleEventRenderer();
   protected UserPreferences m_properties;
   private String m_uniqueID;
   private Watcher m_watcher;

   public static final String LIFETIME_PROPERTY = "lifetime";
   public static final String LIFETIME_PROPVAL_FOREVER = "forever";
   public static final String LIFETIME_PROPVAL_RESPOND = "respond";
   public static final String LIFETIME_PROPVAL_TIMEOUT = "timeout";
   public static final String LIFETIME_PROPVAL_TIMEOUTRESPOND = "timeoutrespond";
   private static final String LIFETIME_PROPVAL_DEFAULT = LIFETIME_PROPVAL_TIMEOUTRESPOND;

   public static final String TIMEOUT_SPAN_PROPERTY = "lifespan";
   private static final String TIMEOUT_SPAN_PROPERTY_DEFAULT = "1";  // changeit beavis.

   public static final String CHECK_FREQUENCY_PROPERTY = "checkFrequencyMillis";
   private static final String CHECK_FREQUENCY_PROPERTY_DEFAULT = "1000"; // ==1s: changeit beavis.

   public static final String EVENT_FONT_PROPERTY = "eventFont";
   public static final String EVENT_FONT_PROPERTY_DEFAULT = "dialog-plain-12";

   public static final String EVENT_COLOR_PROPERTY = "eventColor";
   public static final String EVENT_COLOR_PROPERTY_DEFAULT="#000000";

   public AbstractEventType(String uniqueID)
   {
      m_uniqueID = uniqueID;
      defaultProperties();
   }

   /**
    * Default Properties is responsible for creating the Properties object and
    * setting all default properties. You can override, but remember to call
    * super.defaultProperties()
    */
   protected void defaultProperties()
   {
      try
      {
         m_properties = new UserPreferences(getPropertiesFileName());
      }
      catch (IOException ioe)
      {}
      m_properties.setPreference(m_uniqueID+"."+LIFETIME_PROPERTY,
         LIFETIME_PROPVAL_DEFAULT);
      m_properties.setPreference(m_uniqueID+"."+CHECK_FREQUENCY_PROPERTY,
         CHECK_FREQUENCY_PROPERTY_DEFAULT);
      m_properties.setPreference(m_uniqueID+"."+EVENT_FONT_PROPERTY,
         EVENT_FONT_PROPERTY_DEFAULT);
      m_properties.setPreference(m_uniqueID+"."+EVENT_COLOR_PROPERTY,
         EVENT_COLOR_PROPERTY_DEFAULT);

   }

   /**
    * Get the name of the file that is used to persist properties for this
    * instance of the file type. This ought to be in part based on the
    * uniqueID in order to preserve the uniqueness of the settings for this
    * type.
    */
   protected String getPropertiesFileName()
   {
      return System.getProperty("user.home")+File.separator+
         getName()+".properties";
   }

   /**
    * Set a property on this event type.
    */
   public final void setProperty(String propName, String propValue)
   {
      m_properties.setPreference(m_uniqueID+"."+propName, propValue);
   }

   /**
    * Get a property of this event type
    */
   public final String getProperty(String propName)
   {
      return (String)m_properties.getPreference(m_uniqueID+"."+propName);
   }

   public final void setFontProperty(String propName, Font propFont)
   {
      m_properties.setFontPreference(m_uniqueID+"."+propName, propFont);
   }

   public final void setColorProperty(String propName, Color propColor)
   {
      m_properties.setColorPreference(m_uniqueID+"."+propName, propColor);
   }

   public final Font getFontProperty(String propName)
   {
      return m_properties.getFontPreference(m_uniqueID+"."+propName);
   }

   public final Color getColorProperty(String propName)
   {
      return m_properties.getColorPreference(m_uniqueID+"."+propName);
   }
   /**
    * Set a property whose value is a list of strings.
    */
   public final void setListProperty(String propName, List propList)
   {
      String listBase = m_uniqueID+"."+propName;
      List l = m_properties.getMultiKeyList(listBase);

      for (int i=0; i < l.size(); i++)
      {
         m_properties.removeFromMultiKeyList(listBase,
            l.get(i)
         );
      }
      for (int i=0; i < propList.size(); i++)
      {
         m_properties.addToMultiKeyList(listBase,
            propList.get(i)
         );
      }
   }

   /**
    * Get a list property. Note that the list is not saved back into the
    * properties object until setListProperty is called.
    */
   public final List getListProperty(String propName)
   {
      return m_properties.getMultiKeyList(m_uniqueID+"."+propName);
   }

   /**
    * This is a shortcut for Integer.parseInt(getProperty(CHECK_FREQUENCY_PROPERTY))
    */
   public final int getCheckFrequency()
   {
      String cf = getProperty(CHECK_FREQUENCY_PROPERTY);
      if (cf == null)
      {
         cf = CHECK_FREQUENCY_PROPERTY_DEFAULT;
      }
      return Integer.parseInt(cf);
   }

   /**
    * Save the properties of thie event type. The validateProperties method
    * is called first; you should throw an exception if validation fails.
    */
   public final void saveProperties() throws Exception
   {
      validateProperties();

      try
      {
         m_properties.save();
      }
      catch (IOException ioe)
      {
         throw new Exception("Failed to read from "+getPropertiesFileName());
      }

   }

   /**
    * Revert the properties of the event type based on the contents of the
    * properties file. If the properties file doesn't exist, revert to
    * defaults.
    */
   public final void revertProperties() throws Exception
   {
      File f = new File(getPropertiesFileName());

      if (f.exists() && f.isFile() && f.canRead())
      {
         FileInputStream fis = new FileInputStream(f);

         UserPreferences p = m_properties;
         m_properties = new UserPreferences();
         m_properties.revert();

         try
         {
            validateProperties();
         }
         catch (Exception e)
         {
            m_properties = p;
            throw e;
         }
      }
      else
      {
         defaultProperties();
      }
   }

   /**
    * Validates the properties of your event type. You can override this in
    * a subclass, but remember to call super.validateProperties()
    */
   public void validateProperties() throws Exception
   {
      String lifeTime = getProperty(LIFETIME_PROPERTY);

      boolean isValid = (lifeTime == null) ||
                (LIFETIME_PROPVAL_FOREVER.equalsIgnoreCase(lifeTime)) ||
                (LIFETIME_PROPVAL_RESPOND.equalsIgnoreCase(lifeTime)) ||
                (LIFETIME_PROPVAL_TIMEOUT.equalsIgnoreCase(lifeTime)) ||
                (LIFETIME_PROPVAL_TIMEOUTRESPOND.equalsIgnoreCase(lifeTime));

      if (!isValid)
      {
         throw new Exception(m_uniqueID+": value "+lifeTime+" is not valid for property "+LIFETIME_PROPERTY+". Value must be one of ("+
            LIFETIME_PROPVAL_FOREVER+", "+LIFETIME_PROPVAL_RESPOND+", "+LIFETIME_PROPVAL_TIMEOUT+", "+LIFETIME_PROPVAL_TIMEOUTRESPOND+").");
      }

      validateIntegerProperty(TIMEOUT_SPAN_PROPERTY, 1, false);

      validateIntegerProperty(CHECK_FREQUENCY_PROPERTY, 1, false);
   }

   protected void validateIntegerProperty(String propName, int min, boolean isMandatory)
      throws Exception
   {
      String value = getProperty(propName);

      boolean isValid = (value == null);

      if (isValid && isMandatory)
      {
         throw new Exception(m_uniqueID+": property "+propName+" is mandatory. Please provide a value.");
      }

      if (!isValid)
      {
         try
         {
            int i = Integer.parseInt(value);
            isValid = (i > min);
         }
         catch (NumberFormatException nfe)
         {
         }
      }

      if (!isValid)
      {
         throw new Exception(m_uniqueID+": value "+value+
            " is not valid for property "+propName+
            ". Value must be an integer between "+min+" and (2^31)-1."
         );
      }
   }

   /**
    * Get the name of this event type
    */
   public abstract String getName();

   /**
    * Get an object that provides a UI component to customize the event type.
    */
   public abstract EventTypeCustomizer getEventTypeCustomizer();

   /**
    * Get a renderer that is used to display events of this type
    */
   public EventRenderer getEventRenderer()
   {
      return m_renderer;
   }

   /**
    * Get an object that can be used when the user responds to an event. This
    * method may return null if there is no responder.
    */
   public abstract EventResponder getEventResponder();

   /**
    * How long do events of this type live? Use one of the three
    * LIFETIME_ constants.
    */
   public EventType.Lifetime getLifetime()
   {
      String lifeTime = getProperty(LIFETIME_PROPERTY);

      if (lifeTime == null)
      {
         lifeTime = LIFETIME_PROPVAL_DEFAULT;
      }

      if (LIFETIME_PROPVAL_FOREVER.equalsIgnoreCase(lifeTime))
      {
         return EventType.LIFETIME_FOREVER;
      }
      else if (LIFETIME_PROPVAL_RESPOND.equalsIgnoreCase(lifeTime))
      {
         return EventType.LIFETIME_RESPOND;
      }
      else if (LIFETIME_PROPVAL_TIMEOUT.equalsIgnoreCase(lifeTime))
      {
         return EventType.LIFETIME_TIMEOUT;
      }
      else if (LIFETIME_PROPVAL_TIMEOUTRESPOND.equalsIgnoreCase(lifeTime))
      {
         return EventType.LIFETIME_TIMEOUT_OR_RESPOND;
      }
      else
      {
         throw new IllegalStateException("Lifetime property has not been validated: "+lifeTime);
      }
   }

   /**
    * Get the timeout if this event type has a LIFETIME_TIMEOUT. This is
    * expressed in minutes from the timestamp of the event. The value of this
    * method is ignored if the lifetime of this event type is not
    * LIFETIME_TIMEOUT
    */
   public int getLifespanMinutes()
   {
      String lifeSpan = getProperty(TIMEOUT_SPAN_PROPERTY);

      if (lifeSpan == null)
      {
         lifeSpan = TIMEOUT_SPAN_PROPERTY_DEFAULT;
      }

      return Integer.parseInt(lifeSpan);

   }

   /**
    * Every type of event must have an object that knows how to detect events
    * of this type and add them to the queue. The object that does this is
    * known as a Watcher.
    *
    * Subclasses should implement createWatcher to create a new watcher
    * of the correct type. This method calls createWatcher the first time
    * it is called.
    */
   public final Watcher getWatcher()
   {
      if (m_watcher == null)
      {
         m_watcher = createWatcher();
      }
      return m_watcher;
   }

   /**
    * Subclasses should instantiate and return a watcher
    */
   public abstract Watcher createWatcher();

   /**
    * This is just a simple subclass of BasicEvent which implements the
    * getEventType() method to return an instance of the enclosing class; your
    * subclass can just instantiate these directly and use them as events.
    */
   protected class TypedEvent extends BasicEvent
   {
      public TypedEvent(String message)
      {
         super(message);
         setAttribute(SimpleEventRenderer.COLOR_ATTRIBUTE,
            m_properties.getColorPreference(m_uniqueID+"."+EVENT_COLOR_PROPERTY)
         );
         setAttribute(SimpleEventRenderer.FONT_ATTRIBUTE,
            m_properties.getFontPreference(m_uniqueID+"."+EVENT_FONT_PROPERTY)
         );


      }

      /**
       * Get the type of event. Each event has a corresponding type, which must
       * be registered.
       */
      public EventType getEventType()
      {
         return AbstractEventType.this;
      }
   }
}
 
