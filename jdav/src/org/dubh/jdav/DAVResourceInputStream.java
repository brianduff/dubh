// $Id: DAVResourceInputStream.java,v 1.1.1.1 2000-09-17 17:38:12 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import java.net.URL;

import java.io.IOException;
import java.io.InputStream;

import org.dubh.jdav.WebDAVURLConnection;

/**
 * Provides an input stream to a WebDAV resource. This class tries to make it
 * completely transparent that you are using WebDAV at all, and should
 * theoretically be usable pretty much like a FileInputStream.
 * E.g.
 * <pre>
   BufferedReader br = new BufferedReader(new InputStreamReader(new
     DAVFileInputStream("http://www.dubh.org/dav/test.txt")));
   String line = br.readLine();
   while (line != null)
   {
      System.out.println(line);
      line = br.readLine();
   }
   br.close();
 * </pre>
 * <P>
 * @author Brian Duff (<a href="brian@dubh.org">brian@dubh.org</a>)
 */
public class DAVResourceInputStream extends InputStream
{
   private WebDAVURLConnection m_davConnection;
   private InputStream m_is;
     
   /**
    * Construct a DAVResourceInputStream for a specified URL.
    *
    * @param url the URL to create a stream for
    * @throws java.io.IOException if it is not possible to connect to the
    *   specified URL
    */
   public DAVResourceInputStream(URL u)
      throws DAVException
   {
      try
      {
         // TODO: Support proxy
         m_davConnection = new WebDAVURLConnection(u);

         // There's nothing exciting here, it's just a standard HTTP GET request.
         m_davConnection.connect();

         m_is = m_davConnection.getInputStream();

         int statusCode = m_davConnection.getResponseCode();
         if (statusCode > 400 && statusCode <= 499)
         {
            throw new DAVException(u, statusCode,
               m_davConnection.getResponseMessage()
            );
         }
      }
      catch (IOException ioe)
      {
         if (ioe instanceof DAVException)
         {
            throw (DAVException)ioe;
         }

         throw new DAVException(u, ioe);
      }
   }

   public DAVResourceInputStream(String url)
      throws IOException
   {
      this(new URL(url));
   }

   public int available()
      throws IOException
   {
      return m_is.available();
   }

   /**
    * Closes this input stream and releases any system resources associated with
    * the stream.
    */
   public void close()
      throws IOException
   {
      m_is.close();
      m_davConnection.disconnect();
   }

   /**
    * Marks the current position in this input stream
    */
   public void mark(int readlimit)
   {
      m_is.mark(readlimit);
   }

   /**
    * Tests if this input stream supports the mark and reset methods.
    */
   public boolean markSupported()
   {
      return m_is.markSupported();
   }

   /**
    * Reads the next byte of data from the input stream.
    */
   public int read()
      throws IOException
   {
      return m_is.read();
   }

   /**
    * Reads some number of bytes from the input stream and stores them into
    * the buffer array b.
    */
   public int read(byte[] b)
      throws IOException
   {
      return m_is.read(b);
   }

   /**
    * Reads up to len bytes of data from the input stream into an array of
    * bytes.
    */
   public int read(byte[] b, int off, int len)
      throws IOException
   {
      return m_is.read(b, off, len);
   }

   /**
    * Repositions this stream to the position at the time the mark method was
    * last called on this input stream.
    */
   public void reset()
      throws IOException
   {
      m_is.reset();
   }

   /**
    *
    */
   public long skip(long n)
      throws IOException
   {
      return m_is.skip(n);
   }

}

 