
// Copyright (c) 2000 
package org.dubh.apps.ewatch.type.url;

import org.dubh.apps.ewatch.type.*;
import org.dubh.apps.ewatch.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import java.util.zip.Checksum;
import java.util.zip.Adler32;

import java.text.MessageFormat;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * This event type listens for changes in documents which can be accessed via a
 * protocol that the java URL class knows about.
 * <P>
 * @author Brian Duff (brian@dubh.co.uk)
 */
public class URLChangeEventType extends AbstractEventType
{
   private static final String NAME = "URLUpdated";
   public static final String URL_LIST_PROPERTY = "urlList";
   public static final String EVENT_MESSAGE_PROPERTY = "eventMessage";
   private static final String EVENT_MESSAGE_DEFAULT = "{0} was updated";

   /**
    * I don't like doing this, but not sure how else to implement this.
    */
   private static final int BIG_BUFFER_SIZE = 1000000;

   private static final byte[] BIG_BUFFER = new byte[BIG_BUFFER_SIZE];

   private List m_urlList;
   private List m_checksums;

   private Checksum m_checksum;

   class ChecksumValue
   {
      long checksum;
   }

   public URLChangeEventType(String uniqueID)
   {
      super(uniqueID);
      m_checksum = new Adler32();
   }

   /**
    * Get the name of this event type
    */
   public String getName()
   {
      return NAME;
   }

   private String getEventMessage(String url)
   {
      String eventMessage = getProperty(EVENT_MESSAGE_PROPERTY);
      if (eventMessage == null) eventMessage = EVENT_MESSAGE_DEFAULT;
      return MessageFormat.format(eventMessage, new Object[] { url } );
   }

   /**
    * Get an object that provides a UI component to customize the event type.
    */
   public EventTypeCustomizer getEventTypeCustomizer()
   {
      return null;
   }

   public EventResponder getEventResponder()
   {
      return null;
   }

   /**
    */
   public Watcher createWatcher()
   {
      return new URLChangeWatcher();
   }

   private long calculateChecksum(URL u)
      throws Throwable
   {
      m_checksum.reset();

      BufferedInputStream bis = null;
      try
      {

         bis = new BufferedInputStream(u.openStream());

         int count = bis.read(BIG_BUFFER);

         m_checksum.update(BIG_BUFFER, 0, count);

         return m_checksum.getValue();
      }
      catch (Throwable ioe)
      {
         // Need to improve this.
         throw ioe;
      }
      finally
      {
         if (bis != null)
         {
            try
            {
               bis.close();
            }
            catch (IOException ioe) {}
         }
      }
   }

   class URLChangeWatcher extends ThreadWatcher
   {
      public URLChangeWatcher()
      {
         super(getCheckFrequency());
      }

      public void startWatching()
      {
         // Initially, we check to see if the file exists. If not, we add
         // it to the detected delete list. This stops events being fired if
         // files don't exist when the watcher starts.
         List l = getListProperty(URL_LIST_PROPERTY);
         m_checksums = new ArrayList(l.size());
         
         for (int i=0; i < l.size(); i++)
         {
            URL u;
            try
            {
               u = new URL((String) l.get(i));
            }
            catch (MalformedURLException mue)
            {
               System.err.println(getName()+": Invalid URL: "+(String)l.get(i));
               //mue.printStackTrace();
               continue;
            }
             
            ChecksumValue cv = new ChecksumValue();
            try
            {
               cv.checksum = calculateChecksum(u);
            }
            catch (Throwable e)
            {
               System.err.println(getName()+": Failed to contact "+u+": "+e.getMessage());
               //e.printStackTrace();
               cv.checksum = m_checksum.getValue();
            }
            m_checksums.add(cv);
         }

         m_urlList = l;

         super.startWatching();
      }

      public void doCheck()
      {
         List l = m_urlList;
         for (int i=0; i < l.size(); i++)
         {
            URL u;
            try
            {
               u = new URL((String) l.get(i));
            }
            catch (MalformedURLException mue)
            {
               mue.printStackTrace();
               continue;
            }

            long checksum;
            try
            {
               checksum = calculateChecksum(u);
            }
            catch (Throwable e)
            {
               System.err.println(getName()+": Failed to contact "+u+": "+e.getMessage());
               checksum = m_checksum.getValue();
            }
            ChecksumValue cv = (ChecksumValue)m_checksums.get(i);
            if (checksum != cv.checksum)
            {
               cv.checksum = checksum;
               eventOccurred(new TypedEvent(getEventMessage((String)l.get(i))));
            }
         }
      }
   }   
}

 