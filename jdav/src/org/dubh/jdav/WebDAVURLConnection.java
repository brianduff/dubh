// $Id: WebDAVURLConnection.java,v 1.1.1.1 2000-09-17 17:38:15 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

// WARNING: This class imports HttpURLConnection from the
// sun.net.www.protocol.http package. This could potentially prevent this
// code from working on non-reference versions of the JDK and may harm
// future compatibility. Nevertheless, I'd have to rewrite the entire Java
// HTTP support if I didn't use this class, and this is something I really
// don't want to have to do.

/**
 * A URL connection for WebDAV specific features. See the
 * <a href="http://www.webdav.org">spec</a> for details (RFC 2518)<p>
 *
 * WebDAV extends the HTTP protocol to allow management of resource properties,
 * creation and management of resource collections, namespace manipulation
 * and resource locking (collision avoidance).
 *
 * Note that because WebDAV and HTTP share the same URI prefix (http), you can't
 * use the standard Java mechanism for getting a URL connection. Instead, you
 * should directly instantiate this class with the correct details.
 *
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org</a>)
 * @version $Version$
 */
public class WebDAVURLConnection extends java.net.HttpURLConnection
{
   static final String METHOD_PROPFIND = "PROPFIND";
   static final String METHOD_PROPPATCH = "PROPPATCH";
   static final String METHOD_MKCOL = "MKCOL";
   static final String METHOD_GET = "GET";
   static final String METHOD_HEAD = "HEAD";
   static final String METHOD_POST = "POST";
   static final String METHOD_DELETE = "DELETE";
   static final String METHOD_PUT = "PUT";
   static final String METHOD_COPY = "COPY";
   static final String METHOD_MOVE = "MOVE";
   static final String METHOD_LOCK = "LOCK";
   static final String METHOD_UNLOCK = "UNLOCK";
   static final String METHOD_TRACE = "TRACE";
   static final String METHOD_OPTIONS = "OPTIONS";

   /**
    * HTTP methods defined in RFC2518
    */
   private static final String[] dav_methods =
   {
      METHOD_PROPFIND, METHOD_PROPPATCH, METHOD_MKCOL, METHOD_GET, METHOD_HEAD,
      METHOD_POST, METHOD_DELETE, METHOD_PUT, METHOD_COPY, METHOD_MOVE,
      METHOD_LOCK, METHOD_UNLOCK, METHOD_TRACE, METHOD_OPTIONS
    };

   private HackedHttpConnection m_httpConnection;
   
   /**
    * Construct a WebDAV connection to a specified URL, using a specified
    * proxy host and port. This can be used by other protocol handlers to
    * fetch http web dav resources.
    *
    * @param u The URL to connect to
    * @param host The proxy host to use. Can be null if no proxy is to be used
    * @param port The proxy port to use. Can be -1 if no proxy is to be used
    * @throws java.io.IOException if the connection could not be established
    */
   public WebDAVURLConnection(URL u, String host, int port)
      throws IOException
   {
      super(u);
      m_httpConnection = new HackedHttpConnection(
         u, host, port
      );
   }

   /**
    * Construct a WebDAV connection to a specified URL. If the
    * system properties http.proxyHost and http.proxyPort are set, they
    * are used to determine the proxy to use. If these properties are not
    * set, a direct connection to the URL is attempted
    *
    * @param u The URL to connect to
    * @throws java.io.IOException if the connection could not be established
    */
   public WebDAVURLConnection(URL u)
      throws IOException
   {
      super(u);
      // Try to get the proxy port & host from system properties.
      String host = (String) java.security.AccessController.doPrivileged(
         new sun.security.action.GetPropertyAction("http.proxyHost")
      );
      String port = (String) java.security.AccessController.doPrivileged(
         new sun.security.action.GetPropertyAction("http.proxyPort")
      );
      int nPort = -1;
      if (port != null)
      {
         try
         {
            nPort = Integer.parseInt(port);
         }
         catch (NumberFormatException nfe)
         {
         }
      }
      m_httpConnection = new HackedHttpConnection(
         u, host, nPort
      );
   }

   public void setDoOutput(boolean b)
   {
      m_httpConnection.setDoOutput(b);
   }

   /**
    * Connect to the server. If a connection is already opened, this method
    * returns immediately.
    *
    * @throws java.io.IOException if the connection could not be established
    */
   public void connect()
      throws IOException
   {
      m_httpConnection.connect();
   }

   /**
    * Get an output stream for the connection
    *
    * @return an output stream that can be used to write to the server
    * @throws java.io.IOException if an outputstream could not be retreived.
    */
   public synchronized OutputStream getOutputStream()
      throws IOException
   {
      return m_httpConnection.getOutputStream();
   }

   /**
    * Get an input stream for the connection.
    *
    * @returns an InputStream for reading from the connection.
    * @throws java.io.IOException if the stream could not be retreieved
    */
   public synchronized InputStream getInputStream()
      throws IOException
   {
      return m_httpConnection.getInputStream();
   }


   /**
    * Disconnect from the server
    */
   public void disconnect()
   {
      m_httpConnection.disconnect();
   }

   public boolean usingProxy()
   {
      return m_httpConnection.usingProxy();
   }

   /**
    * Gets a header field by name. Returns null if not known.
    * @param name the name of the header field.
    */
   public String getHeaderField(String name)
   {
      return m_httpConnection.getHeaderField(name);
   }

   public String getHeaderField(int n)
   {
      return m_httpConnection.getHeaderField(n);
   }

   public String getHeaderFieldKey(int n)
   {
      return m_httpConnection.getHeaderFieldKey(n);
   }

   /**
    * Set an HTTP request property
    */
   public void setRequestProperty(String key, String value)
   {
      m_httpConnection.setRequestProperty(key, value);
   }

   public String getRequestProperty(String key)
   {
      return m_httpConnection.getRequestProperty(key);
   }

   public void setInstanceFollowRedirects(boolean shouldFollow)
   {
      m_httpConnection.setInstanceFollowRedirects(shouldFollow);
   }

   /**
   * Set the method for the WebDAV request, one of:
   * <UL>
   *  <LI>PROPFIND
   *  <LI>PROPPATCH
   *  <LI>MKCOL
   *  <LI>GET
   *  <LI>HEAD
   *  <LI>POST
   *  <LI>DELETE
   *  <LI>PUT
   *  <LI>COPY
   *  <LI>MOVE
   *  <LI>LOCK
   *  <LI>UNLOCK
   *  <LI>TRACE
   *  <LI>OPTIONS
   * </UL>
   *
   * are legal, subject to protocol restrictions.  The default
   * method is GET.
   * 
   * @param method the HTTP / WebDAV method
   * @exception ProtocolException if the method cannot be reset or if
   *              the requested method isn't valid for HTTP or WebDAV.
   * @see #getRequestMethod()
   */
   public void setRequestMethod(String method)
      throws ProtocolException
   {
      boolean isValid = false;
      for (int i=0; i < dav_methods.length; i++)
      {
         if (dav_methods[i].equals(method))
         {
            isValid = true;
            break;
         }
      }
      if (!isValid)
      {
         throw new ProtocolException("Invalid WebDAV method: "+method);
      }
      m_httpConnection.setRequestMethod(method);
   }

   /**
    * A hacked version of HttpURLConnection that allows you to set methods
    * other than the basic HTTP ones
    */
   class HackedHttpConnection extends
      sun.net.www.protocol.http.HttpURLConnection
   {
      public HackedHttpConnection(URL u, String host, int port)
         throws IOException
      {
         super(u, host, port);
      }

      /**
       * This is the core of our hack. The standard HTTP client will barf
       * on request methods that it doesn't recognize. Obviously, we need to
       * circumvent this. The easiest way to do this is to fool the super
       * class into thinking it is dealing with a PUT then shiftily switch
       * the request method back to it's previous value.
       */
      public synchronized OutputStream getOutputStream()
         throws IOException
      {
         OutputStream os;
         try
         {
            os = super.getOutputStream();
         }
         catch (ProtocolException pe)
         {
            // This is hacky. We temporarily change the request method to
            // PUT so that the super call will work, then set it back to
            // the real request method.
            String request = this.getRequestMethod();
            HackedHttpConnection.this.setRequestMethod("PUT");
            os = super.getOutputStream();
            HackedHttpConnection.this.setRequestMethod(request);
         }
         return os;
      }
   }
}

 