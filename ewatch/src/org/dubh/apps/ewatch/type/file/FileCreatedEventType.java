
// Copyright (c) 2000 
package org.dubh.apps.ewatch.type.file;

import org.dubh.apps.ewatch.*;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import java.text.MessageFormat;

/**
 * This is an event type that listens for the creation of specific files. An
 * event is triggered whenever one of any number of associated file names
 * begins existance.
 *
 * @author Brian Duff
 */
public class FileCreatedEventType extends AbstractFileEventType
{
   private static final String NAME = "FileCreated";
   public static final String FILENAME_LIST_PROPERTY = "filenameList";
   public static final String EVENT_MESSAGE_PROPERTY = "eventMessage";
   private static final String EVENT_MESSAGE_DEFAULT = "{0} was created";

   private List m_detectedCreateList;

   private List m_fileList;

   public FileCreatedEventType(String uniqueID)
   {
      super(uniqueID);
      m_detectedCreateList = new ArrayList();
   }
   
   /**
    * Get the name of this event type
    */
   public String getName()
   {
      return NAME;
   }

   private String getEventMessage(String fileName)
   {
      String eventMessage = getProperty(EVENT_MESSAGE_PROPERTY);
      if (eventMessage == null) eventMessage = EVENT_MESSAGE_DEFAULT;
      return MessageFormat.format(eventMessage, new Object[] { fileName } );
   }

   /**
    * Get an object that provides a UI component to customize the event type.
    */
   public EventTypeCustomizer getEventTypeCustomizer()
   {
      return null;
   }

   /**
    */
   public Watcher createWatcher()
   {
      return new FileCreateWatcher();
   }


   class FileCreateWatcher extends ThreadWatcher
   {
      public FileCreateWatcher()
      {
         super(getCheckFrequency());
      }

      public void startWatching()
      {
         // Initially, we check to see if the file exists. If so, we add
         // it to the detected create list. This stops events being fired if
         // files already exist when the watcher starts.
         List l = getListProperty(FILENAME_LIST_PROPERTY);
         for (int i=0; i < l.size(); i++)
         {
            String theFile = (String) l.get(i);
            File f = new File(theFile);

            if (f.exists())
            {
               if (!m_detectedCreateList.contains(theFile))
               {
                  m_detectedCreateList.add(theFile);
               }
            }
         }

         m_fileList = l;
         super.startWatching();
      }

      public void doCheck()
      {
         List l = m_fileList;
         for (int i=0; i < l.size(); i++)
         {
            String theFile = (String) l.get(i);
            File f = new File(theFile);

            boolean isDetected = m_detectedCreateList.contains(theFile);
            boolean fileExists = f.exists();

            if (fileExists && !isDetected)
            {
               eventOccurred(new TypedEvent(getEventMessage(theFile)));
               // Or would keep firing this. The proper solution should
               // move it onto a different list and check for it being
               // removed.

               m_detectedCreateList.add(theFile);
            }
            else if (!fileExists && isDetected)
            {
               // File creation was previously detected, but the file no longer
               // exists - it has been deleted from the FS and can be requeued
               // for creation detection again.
               m_detectedCreateList.remove(theFile);
            }
         }
      }
   }   
}

