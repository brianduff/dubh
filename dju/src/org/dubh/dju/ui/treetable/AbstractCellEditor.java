// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractCellEditor.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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


package org.dubh.dju.ui.treetable;

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