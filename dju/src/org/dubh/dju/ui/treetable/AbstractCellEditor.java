// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractCellEditor.java,v 1.2 1999-03-22 23:37:19 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//
//  Copyright 1997, 1998 by Sun Microsystems, Inc.,
//  901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
//  All rights reserved.
//
//  This software is the confidential and proprietary information
//  of Sun Microsystems, Inc. ("Confidential Information").  You
//  shall not disclose such Confidential Information and shall use
//  it only in accordance with the terms of the license agreement
//  you entered into with Sun.
//
// ---------------------------------------------------------------------------
//   Original Author: Philip Milne, Sun Microsystems
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package dubh.utils.ui.treetable;

import java.awt.Component;
import java.awt.event.*;
import java.awt.AWTEvent;
import javax.swing.*;
import javax.swing.event.*;
import java.util.EventObject;
import java.io.Serializable;

/**
 * @version %I% %G% 
 * 
 * A base class for CellEditors, providing default implementations for all 
 * methods in the CellEditor interface and support for managing a series 
 * of listeners. 
 *
 * @author Philip Milne
 */

public class AbstractCellEditor implements CellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    public Object getCellEditorValue() { return null; }
    public boolean isCellEditable(EventObject e) { return true; }
    public boolean shouldSelectCell(EventObject anEvent) { return false; }
    public boolean stopCellEditing() { return true; }
    public void cancelCellEditing() {}

    public void addCellEditorListener(CellEditorListener l) {
   listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
   listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length-2; i>=0; i-=2) {
       if (listeners[i]==CellEditorListener.class) {
      ((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
       }        
   }
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length-2; i>=0; i-=2) {
       if (listeners[i]==CellEditorListener.class) {
      ((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
       }        
   }
    }
}