// $Id: DAVResourceOutputStream.java,v 1.1.1.1 2000-09-17 17:38:15 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import java.net.URL;
import java.net.MalformedURLException;

import java.io.IOException;
import java.io.OutputStream;

import org.dubh.jdav.WebDAVURLConnection;

/**
 * Provides an output stream to a WebDAV resource. This class tries to make it
 * transparent that you are using WebDAV at all, and should theoretically be
 * pretty much like FileOutputStream.
 * <P>
 * Important: I've noticed that the close() method of other output stream
 * classes doesn't propagate exceptions. This means if you use something like
 * new PrintWriter(new DAVResourceOutputStream(...)) and call close(), no
 * exceptions will be thrown, even if the close() method on DAVResourceOutputStream
 * throws an exception. This is even more important if you are using locking,
 * because the lock is not released until close() is called.
 * <P>
 * Since we don't know whether a write operation succeeds until we close
 * the output stream and receive a response from the server, you should
 * always properly close the DAVResourceOutputStream and check for exceptions.
 * <P>
 *
 * @author Brian Duff (<a href="brian@dubh.org">brian@dubh.org</a>)
 */
public class DAVResourceOutputStream extends OutputStream
{
   private WebDAVURLConnection m_davConnection;
   private OutputStream m_os;
   private URL m_resource;
   private boolean m_isLocked;

   private DAVLockToken m_lockToken;

   /**
    * Construct an output stream to a WebDAV resource with the specified URL.
    *
    * @param u The url to output to
    * @throws org.dubh.jdav.DAVException if an output stream could not be
    *         constructed.
    */
   public DAVResourceOutputStream(URL u)
      throws DAVException
   {
      this(u, false, "");
   }

   /**
    * Construct an output stream to a WebDAV resource with the specified URL.
    *
    * @param url the url to output to
    * @throws org.dubh.jdav.DAVException if the output stream could not be
    *         constructed.
    */
   public DAVResourceOutputStream(String url)
      throws DAVException, MalformedURLException
   {
         this(new URL(url));
   }

   /**
    * Construct an output stream to a WebDAV resource and try to obtain a lock
    * on the resource
    *
    * @param url the url to output to
    * @param lock Whether to try to obtain a lock on the resource
    * @param lockOwner The owner of the lock
    * @throws org.dubh.jdav.DAVException if the output stream could not be
    *         constructed.
    */
   public DAVResourceOutputStream(URL url, boolean lock, String lockOwner)
      throws DAVException
   {
      try
      {
         m_resource= url;
         if (lock)
         {
            m_lockToken = DAVUtils.lockResource(url, true, lockOwner);
         }
         m_isLocked = lock;
         m_davConnection = new WebDAVURLConnection(url);
         m_davConnection.setDoOutput(true);
         m_davConnection.setRequestMethod("PUT");
         if (lock)
         {
            String lockToken = m_lockToken.getTokenURIs()[0].getURI();
            m_davConnection.setRequestProperty("If",
               "(<"+lockToken+">)"
            );
         }
         m_davConnection.connect();

         m_os = m_davConnection.getOutputStream();
      }
      catch (IOException ioe)
      {
         throw new DAVException(url, ioe);
      }
   }

   public DAVResourceOutputStream(String url, boolean lock, String lockOwner)
      throws DAVException, MalformedURLException
   {
      this(new URL(url), lock, lockOwner);
   }

   /**
    * Closes this output stream and releases any system resources associated
    * with this stream.
    *
    * @throws org.dubh.jdav.DAVException if writing the resource fails.
    */
   public void close()
      throws DAVException
   {

      
      try
      {
         m_os.close();

         int statusCode = m_davConnection.getResponseCode();

         m_davConnection.disconnect();

         /**
          * Status codes in the 200 range suggest success
          */
         if (statusCode > 299)
         {
            throw new DAVException(
               m_davConnection.getURL(),
               m_davConnection.getResponseCode(),
               m_davConnection.getResponseMessage()
            );
         }

         if (m_isLocked)
         {
            DAVUtils.unlockResource(m_resource, m_lockToken);
         }
      }
      catch (IOException ioe)
      {
         if (ioe instanceof DAVException)
         {
            throw (DAVException)ioe;
         }
         throw new DAVException(m_davConnection.getURL(), ioe);
      }

   }

   /**
    * Flushes this output stream and forces any buffered output bytes to be
    * written out.
    */
   public void flush()
      throws IOException
   {
      m_os.flush();
   }

   /**
    * Writes b.length bytes from the specified byte array to this output stream.
    */
   public void write(byte[] b)
      throws IOException
   {
      m_os.write(b);
   }

   /**
    * Writes len bytes from the specified byte array starting at offset off
    * to this output stream.
    */
   public void write(byte[] b, int off, int len)
      throws IOException
   {
      m_os.write(b, off, len);
   }

   /**
    * Writes the specified byte to this output stream
    */
   public void write(int b)
      throws IOException
   {
      m_os.write(b);
   }
}

 