package org.dubh.apps.ewatch.type.file;

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

/**
 * This is an event type that listens for changes in the contents of one or
 * more files. It does this by computing a checksum for the file(s) and
 * periodically recalculating the checksum to see if it has changed. Although
 * this is not a guaranteed way of determining whether a file has changed, it
 * is more reliable than checking date changes.
 *
 * @author Brian Duff
 */
public class FileChangeEventType extends AbstractFileEventType
{
   // Note to self; this will be superficially the same for any input stream,
   // not just files; oh for multiple inheritance - it may be evil, but I keep
   // finding nifty uses for it :)
   private static final String NAME = "FileUpdated";
   public static final String FILENAME_LIST_PROPERTY = "filenameList";
   public static final String EVENT_MESSAGE_PROPERTY = "eventMessage";
   private static final String EVENT_MESSAGE_DEFAULT = "{0} was updated";

   private List m_fileList;
   private List m_checksums;

   private Checksum m_checksum;

   class ChecksumValue
   {
      long checksum;
   }

   public FileChangeEventType(String uniqueID)
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
      return new FileDeleteWatcher();
   }

   private long calculateChecksum(File f)
   {
      m_checksum.reset();
      if (!f.exists())
      {
         return m_checksum.getValue();
      }

      long fileSize = f.length();

      BufferedInputStream bis = null;
      try
      {
         byte[] allBytes = new byte[(int)fileSize];

         bis = new BufferedInputStream(new FileInputStream(f));

         bis.read(allBytes);

         m_checksum.update(allBytes, 0, allBytes.length);

         return m_checksum.getValue();
      }
      catch (IOException ioe)
      {
         return m_checksum.getValue();
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

   class FileDeleteWatcher extends ThreadWatcher
   {
      public FileDeleteWatcher()
      {
         super(getCheckFrequency());
      }

      public void startWatching()
      {
         // Initially, we check to see if the file exists. If not, we add
         // it to the detected delete list. This stops events being fired if
         // files don't exist when the watcher starts.
         List l = getListProperty(FILENAME_LIST_PROPERTY);
         m_checksums = new ArrayList(l.size());
         
         for (int i=0; i < l.size(); i++)
         {
            File f = new File((String) l.get(i));
            
            ChecksumValue cv = new ChecksumValue();
            cv.checksum = calculateChecksum(f);
            m_checksums.add(cv);
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

            long checksum = calculateChecksum(f);
            ChecksumValue cv = (ChecksumValue)m_checksums.get(i);
            if (checksum != cv.checksum)
            {
               cv.checksum = checksum;
               eventOccurred(new TypedEvent(getEventMessage(theFile)));
            }
         }
      }
   }   
}
 
