// $Id: DAVException.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff
package org.dubh.jdav;

import java.io.IOException;

import java.net.URL;

/**
 * A WebDAV exception class. This is helpful in determining exactly why a
 * WebDAV operation failed. 
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org</a>)
 */
public class DAVException extends IOException
{

   private int m_statusCode;
   private Throwable m_baseException;

   private String m_message;


// These are all the HTTP and WebDAV error codes.

   /**
    * Indicates that the error was caused by another exception. The exception
    * can be retrieved with the getBaseException() method.
    */
   public static final int ERR_BASE_EXCEPTION = -1;

   /**
    * Indicates that this error does not have an associated error code or
    * base exception.
    */
   public static final int ERR_UNSPECIFIED = -2;

    /**
     * Indicates that the syntax of the request was incorrect.
     */
    public static final int ERR_BAD_REQUEST = 400;

    /**
     * Indicates that authorization is required for the specified resource but
     * that no suitable credentials have been supplied.
     */
    public static final int ERR_UNAUTHORIZED = 401;

    /**
     * Indicates that payment is required for the specified resource.
     */
    public static final int ERR_PAYMENT_REQUIRED = 402;

    /**
     * Indicates that the client does not have permission to view the specified
     * resource.
     */
    public static final int ERR_FORBIDDEN = 403;

    /**
     * Indicates that the specified resource does not exist on the server.
     */
    public static final int ERR_NOT_FOUND = 404;

    /**
     * Indicates that the specified operation cannot be performed on the
     * specified resource
     */
    public static final int ERR_BAD_METHOD = 405;

    /**
     * Not Acceptable.
     */
    public static final int ERR_NOT_ACCEPTABLE = 406;

    /**
     * Indicates that authentication through a proxy is required to access the
     * specified resource.
     */
    public static final int ERR_PROXY_AUTH = 407;

    /**
     * Indicates that the server timed out waiting for a request from the
     * client.
     */
    public static final int ERR_CLIENT_TIMEOUT = 408;

    /**
     * Conflict.
     */
    public static final int ERR_CONFLICT = 409;

    /**
     * Indicates that a resource is no longer available.
     */
    public static final int ERR_GONE = 410;

    /**
     * Indicates that the length of a resource is required and was not supplied
     * by the client.
     */
    public static final int ERR_LENGTH_REQUIRED = 411;

    /**
     * Indicates that there is a precondition for the resource which has not
     * been met by the client.
     */
    public static final int ERR_PRECON_FAILED = 412;

    /**
     * Indicates that the request sent by the client is too large for the
     * server to process.
     */
    public static final int ERR_ENTITY_TOO_LARGE = 413;

    /**
     * Indicates that the address (URI) of the resource is too large for the
     * server to process.
     */
    public static final int ERR_REQ_TOO_LONG = 414;

    /**
     * Indicates that the media type of the resource is unsupported by the
     * server.
     */
    public static final int ERR_UNSUPPORTED_TYPE = 415;

   /**
    * Indicates that the server was unable to process instructions contained
    * in the request. (WebDAV)
    */
   public static final int ERR_UNPROCESSABLE_ENTITY = 422;

   /**
    * Indicates that the source or destination of a method is locked (WebDAV)
    */
   public static final int ERR_LOCKED = 423;

   /**
    * Indicates that an operation could not be performed on a resource because
    * the requested action depended on another action and that action failed.
    * (WebDAV)
    */
   public static final int ERR_FAILED_DEPENDENCY = 424;


    /* 5XX: server error */

    /** 
     * Indicates that there was an internal error on the server which prevented
     * it from performing a method.
     */
    public static final int ERR_INTERNAL_ERROR = 500;

    /** 
     * Indicates that the specified method is not implemented on the server.
     */
    public static final int ERR_NOT_IMPLEMENTED = 501;

    /**
     * Bad Gateway
     */
    public static final int ERR_BAD_GATEWAY = 502;

    /**
     * Indicates that a service is currently unavailable on the server.
     */
    public static final int ERR_UNAVAILABLE = 503;

    /**
     * Gateway Timeout.
     */
    public static final int ERR_GATEWAY_TIMEOUT = 504;

    /**
     * Indicates that the version of the HTTP protocol used by the client is not
     * supported by the server.
     */
    public static final int ERR_VERSION = 505;


   /**
    * Indicates that a method could not be performed on a resource because the
    * server was unable to store the representation needed to successfully
    * complete the request. This condition is considered to be temporary.
    * If the request which received this error was the result of a user
    * action, the request MUST NOT be repeated until it is requested by
    * a separate user action. (WebDAV)
    */
   public static final int ERR_INSUFFICIENT_STORAGE = 507;    

   /**
    * Constructs a DAV Exception
    * @param url The URL on which the operation failed. Must not be null.
    * @param code The status code that was received
    * @param message The status message that was received. Must not be null.
    */
   DAVException(URL url, int code, String message)
   {
      if (url == null)
      {
         throw new IllegalArgumentException("url must not be null");
      }
      if (message == null)
      {
         throw new IllegalArgumentException("message must not be null");
      }
      setMessage("WebDAV operation on "+url.toString()+" failed: "+message);
      m_statusCode = code;
   }

   /**
    * Constructs a DAV Exception that has been caused by another Exception
    *
    * @param url The URL on which the operation failed. Must not be null.
    * @param exception The exception that caused this DAV exception. Must not
    *        be null.
    */
   DAVException(URL url, Throwable exception)
   {
      if (exception == null)
      {
         throw new IllegalArgumentException("Exception must not be null");
      }
      if (url == null)
      {
         throw new IllegalArgumentException("url must not be null");
      }
      exception.printStackTrace(); // REmove this.
      setMessage("WebDAV operation on "+url.toString()+" failed: "+
         exception.toString());
      m_statusCode = ERR_BASE_EXCEPTION;
   }

   /**
    * Constructs a DAV exception that does not have an associated exception
    * or error code.
    */
   DAVException(String message)
   {
      setMessage(message);
   }


   /**
    * Set the message
    */
   private void setMessage(String s)
   {
      m_message = s;
   }

   /**
    * Get a message for this exception
    */
   public String getMessage()
   {
      return m_message;
   }

   /**
    * Get the type of exception this was. You can compare this number against
    * the ERR_ constants in this class.
    */
   public int getType()
   {
      return m_statusCode;
   }

   /**
    * Get the base exception that caused this error. This will return null
    * unless getType() returns the constant ERR_BASE_EXCEPTION
    * @returns null if getType() != ERR_BASE_EXCEPTION, or the exception that
    *          caused this DAVException.
    */
   public Throwable getBaseException()
   {
      return m_baseException;
   }
}

 