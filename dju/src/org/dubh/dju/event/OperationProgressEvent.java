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

package dubh.utils.event;
import java.util.EventObject;
/**
 * An OperationProgressEvent is fired by a lengthy operation. This is useful
 * for when you want to use a progress bar or similar way of indicating that
 * an operation is progressing normally.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [29/06/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [29/06/98]
 @see OperationProgressListener
 */
public class OperationProgressEvent extends EventObject {
  protected transient int m_id;
  protected transient int m_value;
  protected transient String m_description;

  /** Fired when the long operation wants to set the minimum progress value. */
  public static final int SET_MINIMUM  = 0;
  /** Fired when the long operation wants to set the maximum progress value. */
  public static final int SET_MAXIMUM  = 1;
  /** Fired when the long operation wants to change the current progress. */
  public static final int SET_PROGRESS = 2;
  /** Fired when the long operation is finished. */
  public static final int FINISHED     = 3;
  /** Fired when the long operation wants to set a description of its progress */
  public static final int SET_DESCRIPTION=4;

  public OperationProgressEvent(Object source, int id, int value) {
     super(source);
     m_id = id;
     m_value = value;
  }

  public OperationProgressEvent(Object source, int id) {
     this(source, id, 0);
  }

  /**
   * Create a description progress event.
   */
  public OperationProgressEvent(Object source, String description) {
     super(source);
     m_id = SET_DESCRIPTION;
     m_value = 0;
     m_description = description;
  }


  /**
   * Get the type of OperationProgressEvent.
   @return one of the static constants SET_MINIMUM, SET_MAXIMUM, SET_PROGRESS
     or FINISHED.
   */
  public int getID() {
     return m_id;
  }

  /**
   * Get the value associated with this event.
   @return a valid value if getType() returns SET_MINIMUM, SET_MAXIMUM or
     SET_PROGRESS. Undefined if type is FINISHED.
   */
  public int getValue() {
     return m_value;
  }

  public String getDescription() {
     return m_description;
  }
  
}