// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: OperationProgressEvent.java,v 1.5 2001-02-11 02:52:11 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package org.dubh.dju.event;
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