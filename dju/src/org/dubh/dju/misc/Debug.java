/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.utils.misc;

import java.io.*;
import dubh.utils.ui.OutputStreamFrame;
/**
 * Class containing static methods for displaying debug strings on the console.
 * The output can be switched on or off.<P>
 * You should construct all calls to Debug like this:
 * <PRE>
 *    if (Debug.TRACE_LEVEL_X)
 *    {
 *       Debug.println(x, this, "Message");
 *    }
 * </PRE>
 * or: <PRE>
 *    if (Debug.ASSERT)
 *    {
 *       Debug.assert(condition, this, "Message");
 *    }
 * </PRE>
 * This will make sure debugging is stripped out in release versions.
 * Version History: <UL>
 * <LI>0.1 [05/06/98]: Initial Revision
 * <LI>0.2 [08/06/98]: Added support for windowed stream
 * Version history from 07 March 1999 is in the CVS logs.
 *</UL>
 @author Brian Duff
 @version 1.2
 */
public class Debug {  

   private static final String DEBUG_WINDOW = "debugout";

   /**
    * Level 3 is for low level, unimportant debug (e.g. diagnostics)
    */
   public static final boolean TRACE_LEVEL_3 = true;
   /**
    * Level 2 is for information that might affect the user, but not
    * adversely
    */
   public static final boolean TRACE_LEVEL_2 = true;
   /**
    * Level 1 is for when something has gone horribly wrong.
    */
   public static final boolean TRACE_LEVEL_1 = true;

   /**
    * Are asserts on?
    */
   public static final boolean ASSERT = true;

   private static boolean m_assert = true;
   private static boolean m_showmessage = true;
   private static PrintWriter m_outstream = new PrintWriter(System.err);
   private static String m_outputprefix = "";
   private static boolean m_usingFrame = false;

   private static int m_traceLevel = 3;

   private static final String ASSERT_FAILED_MSG = "ASSERTION FAILED: ";
   
   private static DebugFrame m_frame;
   
   /**
   * Sets whether debugging is currently enabled. This has no effect if the 
   * TRACE_LEVEL_X constants are all false.
   * @param enabled if true, debuggging messages are displayed. If false, all
   *  debugging output is suppressed.
   */
   public static void setEnabled(boolean enabled) { m_showmessage = enabled; }

   /**
   * Determine whether debugging is currently enabled.
   * @return true if debugging messages are currently being displayed
   */
   public static boolean isEnabled() { return (TRACE_LEVEL_1 || TRACE_LEVEL_2 || TRACE_LEVEL_3) && m_showmessage; }

  /**
   * Sets the debug output to use a window for output messages.
   */
  public static void useWindow(String title) {
     m_usingFrame = true;
     if (m_frame == null)
        m_frame =  new DebugFrame(DEBUG_WINDOW, title);
     else
        m_frame.clear();
     m_outstream = new PrintWriter(m_frame.getStream());
     m_frame.pack();
     m_frame.setVisible(true);
  }
  
  public static void closeWindow()
  {
     if (m_usingFrame)
     {
        m_frame.setVisible(false);   
     }
  }

   /**
    * Set the current trace level. You should use this to change the level of
    * output at runtime. 
    * @param trace all messages of level <= trace are displayed.
    */
   public static synchronized void setTraceLevel(int trace)
   { 
      println("DEBUG: Set trace level = "+trace);
      m_traceLevel = trace;
   }
   
   public static synchronized int getTraceLevel()
   {
      return m_traceLevel;
   }

   public static synchronized void setAssertEnabled(boolean b)
   {
      println("DEBUG: Set asserts enabled = "+b);
      m_assert = b;
   }
   
   public static synchronized boolean isAssertEnabled()
   {
      return m_assert && ASSERT;
   }

  /**
   * Set the stream to which debugging messages will be sent. By default,
   * debugging output messages are sent to System.err. You can send output to
   * a file using something like:<PRE>
   *    FileOutputStream fos = new FileOutputStream("debug.out");
   *    Debug.setStream(fos);
   * </PRE>
   @param os an output stream to print debug messages to
   */
  public static void setStream(OutputStream os) {
     if (m_usingFrame) {
        m_outstream.close();
        m_usingFrame = false;
     }
     m_outstream = new PrintWriter(os);
  }

  /**
   * Set some text that is prefixed to all output messages. For example:<PRE>
   *    Debug.setPrefix("Debug Message: ");
   *    Debug.println("This is a test");
   *    Debug.println("This is another test");
   * </PRE>
   * Produces the output: <PRE>
   *    Debug Message: This is a test
   *    Debug Message: This is another test
   * </PRE>
   @param prefix the prefix text to use
   */
  public static void setPrefix(String prefix) { m_outputprefix = prefix; }

  /**
   * Display a debug message (if debugging is enabled) followed by a carriage
   * return and flush the buffer.
   * @param mess The message to display
   * @deprecated See the class description for more information
   */
  public static void println(String mess) {
     if (m_showmessage) {
        m_outstream.print(m_outputprefix);
        m_outstream.println(mess);
        m_outstream.flush();
     }
  }


  /**
   * Display a debug message followed by a carriage return and flush the 
   * buffer.
   * @param trace The trace level to use
   * @param mess The message to display
   */
   public static void println(int trace, Class caller, Object mess)
   {
      if (trace <= m_traceLevel)
      {
         println(getClassName(caller)+": "+mess.toString());
      }
   }

  /**
   * Display a debug message followed by a carriage return and flush the 
   * buffer.
   * @param trace The trace level to use
   * @param mess The message to display
   */
   public static void println(int trace, Object caller, Object mess)
   {
      println(trace, caller.getClass(), mess);
   }
   
   public static void printException(int trace, Object caller, Throwable t)
   {
      println(trace, caller.getClass(), "Caught exception "+t);
      t.printStackTrace(getWriter());
   }

  /**
   * Display a debug message (if debugging is enabled) followed by a carriage
   * return and flush the buffer.
   * @param mess The message to display
   * @deprecated See the class description for more information
   */
  public static void print(String mess) {
     if (m_showmessage) {
        m_outstream.print(mess);
        m_outstream.flush();
     }
  }
  
  /**
   * Do an assertion. If the assertion fails, your message will be displayed.
   * See the class description for details on how to call this method.
   */
  public static void assert(boolean condition, Class caller, String message)
  {
     if (m_assert && !condition)
     {
        String oldPrefix = m_outputprefix;
        m_outputprefix = ASSERT_FAILED_MSG;
        println(getClassName(caller)+": "+message);
        m_outputprefix = oldPrefix;
     }
  }
  
  /**
   * Do an assertion. If the assertion fails, your message will be displayed.
   * See the class description for details on how to call this method.
   */
  public static void assert(boolean condition, Object caller, String message)
  {
     assert(condition, caller.getClass(), message);
  }  
  
  private static String getClassName(Class c)
  {
     String fullName = c.getName();
     
     int lastDot = fullName.lastIndexOf('.');
     
     if (lastDot > 0)
     {
        return fullName.substring(lastDot+1);
     }
     return fullName;
  }
  
  public static PrintWriter getWriter() {
     return m_outstream;
  }

  public static void main(String args[]) {
     Debug.useWindow("test");
     Debug.println("Hello");
     Debug.println("Albsdflknweoifn");
  }
  


}