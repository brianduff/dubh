/**********************************************************************
 Freeciv - Copyright (C) 1996 - A Kjeldberg, L Gregersen, P Unold
 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 Freeciv4j: Original Authors: ..., Brian Duff (bduff@uk.oracle.com)
 ***********************************************************************/
package org.freeciv.common;
import java.io.*;

/**
 * Provides logging functionality, similar to log.c.<P>
 *
 * You should call init() early on, and then use the log() methods
 * to display log messages. <P>
 *
 * e.g. <pre>
 *    Logger.init(null, Logger.LOG_NORMAL, null);
 *
 *    Logger.log(Logger.LOG_NORMAL, "A test message");
 *    Logger.log(Logger.LOG_VERBOSE, "With the class name", this);
 *    Logger.log(Logger.LOG_VERBOSE, "With a static class", Logger.class);
 *    if (Logger.DEBUG) {
 *       Logger.log(Logger.LOG_DEBUG, "Conditional debug");
 *    }
 * </pre>
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 */
public final class Logger
{
  
  /**
   * Fatal messages. The app will normally terminate after these. It's
   * not the job of the logger to do the actual termination.
   */
  public static final int LOG_FATAL = 0;
  /**
   * Normal messages. These are the lowest level messages that are
   * displayed by default.
   */
  public static final int LOG_NORMAL = 1;
  /**
   * Verbose messages that most users don't care about.
   */
  public static final int LOG_VERBOSE = 2;
  /**
   * Debug messages that are only of use to coders. Hey! Use a
   * debugger; it's much easier. <g>
   */
  public static final int LOG_DEBUG = 3;
  
  /**
   * You should use this as a compile time constant and surround all
   * debug output with if(Logger.DEBUG) { }, so that it gets compiled out
   * when debug is switched off. It is ignored in actual calls to log()
   */
  public static final boolean DEBUG = true;
  
  // The handlers (using interfaces as a callback substitute - Java is
  // so much more elegant compared to C :))
  private static LogHandler m_mainHandler = new StderrLogHandler();
  private static LogHandler m_fileHandler;
  
  // Flag that remembers if init() has been called
  private static boolean m_inited = false;
  
  // The current msg level
  private static int m_level = LOG_NORMAL;
  
  // Stuff needed for repetition detection
  private static int m_repeated = 0;
  private static int m_next = 2;
  private static int m_prev = 0;
  private static int m_prev_level = -1;
  private static String m_lastMessage = "";
  
  
  /**
   * Intialize the logger. This should only be called once and has no
   * effect on subsequent calls.
   *
   * @param filename The filename to log to. Can be null.
   * @param level The initial log level. Can be changed later.
   * @param handler Provides an equivalent to the callback mechanism in the
   *   original freeciv log.c. Any object implementing the LogHandler
   *   interface can be used to display log messages. If null, the
   *   standard handler (prints to stderr) is used.
   */
  public static void init( String filename, int level, LogHandler handler )
  {
    if( !m_inited )
    {
      if( filename != null )
      {
        try
        {
          m_fileHandler = new FileLogHandler( filename );
        }
        catch( IOException ioe )
        {
          m_fileHandler = null;
          System.err.println( "Failed to create log file " + filename );
        }
      }
      setLogLevel( level );
      if( handler != null )
      {
        m_mainHandler = handler;
      }
      else
      {
        m_mainHandler = new StderrLogHandler();
      }
      m_inited = true;
    }
  }
  
  /**
   * Unconditionally sends the message to the file handler (if it exists)
   * and the main handler.
   */
  private static void sendToHandlers( int logLevel, String message )
  {
    if( m_fileHandler != null )
    {
      m_fileHandler.log( logLevel, message );
    }
    m_mainHandler.log( logLevel, message );
  }
  
  /**
   * Like log(int, String, Class) but calls getClass() on the
   * Object obj to get the class. This makes it possible to do things
   * like: log(.., .., this) inside an instance.
   */
  public static void log( int loglevel, String message, Object obj )
  {
    log( loglevel, message, obj.getClass() );
  }
  
  /**
   * Handy for java; prints the class name at the start of the message.
   * Otherwise, identical to log(int, String). This version is
   * mostly useful for static classes. E.g. log(.., .., MyClass.class)
   */
  public static void log( int loglevel, String message, Class cls )
  {
    if( cls == null )
    {
      log( loglevel, message );
    }
    log( loglevel, cls.getName() + ": " + message );
  }
  
  /**
   * Main external interface. Prints the message at the specified log level.
   * As with original freeciv, saves up repeats.
   */
  public static void log( int logLevel, String message )
  {
    // Could do with using StringBuffers for string concat.
    if( message == null )
    {
      throw new IllegalArgumentException( "Log message can't be null." );
    }
    
    // Repetition code lifted wholesale from 1.10.0 log.c source code
    if( logLevel <= m_level )
    {
      if( logLevel == m_prev_level && m_lastMessage.equals( message ) )
      {
        m_repeated++;
        //System.out.println("-- Detected repeat");
        if( m_repeated == m_next )
        {
          String buf = "last message repeated " + ( m_repeated - m_prev ) + " times";
          if( m_repeated > 2 )
          {
            buf += " (total " + m_repeated + " repeats)";
          }
          sendToHandlers( logLevel, buf );
          m_prev = m_repeated;
          m_next *= 2;
        }
      }
      else
      {
        if( m_repeated > 0 && m_repeated != m_prev )
        {
          if( m_repeated == 1 )
          {
            sendToHandlers( logLevel, m_lastMessage );
          }
          else
          {
            String buf;
            if( m_repeated - m_prev == 1 )
            {
              buf = "last message repeated once";
            }
            else
            {
              buf = "last message repeated " + ( m_repeated - m_prev ) + " times";
            }
            if( m_repeated > 2 )
            {
              buf += " (total " + m_repeated + " repeats)";
            }
            sendToHandlers( logLevel, buf );
          }
        }
        m_prev_level = logLevel;
        m_repeated = 0;
        m_next = 2;
        m_prev = 0;
        sendToHandlers( logLevel, message );
      }
      m_lastMessage = message;
    }
  }
  
  /**
   * You can use this to dynamically change the log level.
   */
  public static void setLogLevel( int level )
  {
    m_level = level;
  }
  
  /**
   * You can use objects that implement this interface to display log
   * messages in a customized way. Just pass the object implementing this
   * interface into init().
   */
  public static interface LogHandler
  {
    void log( int level, String message );
  }
  
  //
  // Both our handlers print the same thing essentially; this
  // superclass prevents repeat code.
  //
  private static class BaseLogHandler
  {
    public void write( PrintWriter pw, int level, String message )
    {
      pw.println( level + ": " + message );
    }
    public void write( PrintStream ps, int level, String message )
    {
      ps.println( level + ": " + message );
    }
  }
  
  //
  // Boring old handler that just prints out to stderr
  //
  private static class StderrLogHandler extends BaseLogHandler implements LogHandler
  {
    public void log( int level, String message )
    {
      write( System.err, level, message );
    }
  }
  
  //
  // Handler that prints out to a file. I'm not sure if the
  // file will ever get closed -- need to check this.
  //
  private static class FileLogHandler extends BaseLogHandler implements LogHandler
  {
    PrintWriter bw;
    public FileLogHandler( String filename )
            throws IOException 
    {
      bw = new PrintWriter( new FileWriter( filename ) );
    }
    public void log( int level, String message )
    {
      write( bw, level, message );
    }
    public void finalize()
    {
      bw.flush();
      bw.close();
    }
  }
  public static void main( String[] args )
  {
    // Test harness code
    Logger.init( null, 3, null );
    Logger.log( Logger.LOG_VERBOSE, "Verbose test" );
    Logger.log( Logger.LOG_NORMAL, "Normal test" );
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "Debug test" );
    }
    Logger.setLogLevel( LOG_NORMAL );
    Logger.log( Logger.LOG_VERBOSE, "This shouldn't be printed" );
    Logger.setLogLevel( LOG_DEBUG );
    for( int i = 0;i < 2;i++ )
    {
      Logger.log( Logger.LOG_NORMAL, "Repetition test 1" );
    }
    for( int i = 0;i < 3;i++ )
    {
      Logger.log( Logger.LOG_NORMAL, "Repetition test 2" );
    }
    for( int i = 0;i < 55;i++ )
    {
      Logger.log( Logger.LOG_NORMAL, "Repetition test 3" );
    }
    Logger.log( Logger.LOG_NORMAL, "Repetition at different levels" );
    Logger.log( Logger.LOG_VERBOSE, "Repetition at different levels" );
    Logger.log( Logger.LOG_FATAL, "Repetition at different levels" );
    Logger.log( Logger.LOG_NORMAL, "Test of class name", Logger.class );
  }
}
