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
 * Version History: <UL>
 * <LI>0.1 [05/06/98]: Initial Revision
 * <LI>0.2 [08/06/98]: Added support for windowed stream
 *</UL>
 @author Brian Duff
 @version 0.2 [08/06/98]
 */
public class Debug {

  private static boolean m_showmessage = true;
  private static PrintWriter m_outstream = new PrintWriter(System.err);
  private static String m_outputprefix = "";
  private static boolean m_usingFrame = false;

  /**
   * Sets whether debugging is currently enabled.
   @param enabled if true, debuggging messages are displayed. If false, all
     debugging output is suppressed.
   */
  public static void setEnabled(boolean enabled) { m_showmessage = enabled; }

  /**
   * Determine whether debugging is currently enabled.
   @return true if debugging messages are currently being displayed
   */
  public static boolean isEnabled() { return m_showmessage; }

  /**
   * Sets the debug output to use a window for output messages. The Debug
   * object will take care of this window internally, but doesn't know when
   * to remove the window. If you call setStream() with a new stream, the
   * window is removed, so calling setStream(null) at the end of your
   * program is proabably a good idea if you use a windowed output stream.
   */
  public static void useWindow(String title) {
     m_usingFrame = true;
     m_outstream = new PrintWriter(new OutputStreamFrame(title));
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
   @param mess The message to display
   */
  public static void println(String mess) {
     if (m_showmessage) {
        m_outstream.print(m_outputprefix);
        m_outstream.println(mess);
        m_outstream.flush();
     }
  }

  public static void print(String mess) {
     if (m_showmessage) {
        m_outstream.print(mess);
        m_outstream.flush();
     }
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