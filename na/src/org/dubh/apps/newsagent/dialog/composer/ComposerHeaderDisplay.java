// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: ComposerHeaderDisplay.java,v 1.4 1999-11-09 22:34:41 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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
package org.javalobby.apps.newsagent.dialog.composer;

import java.util.*;
import javax.swing.*;
import org.javalobby.apps.newsagent.nntp.MessageHeader;
import org.javalobby.apps.newsagent.GlobalState;
import java.awt.event.*;
import org.javalobby.dju.ui.GridBagConstraints2;
import java.awt.*;

/**
 * Displays a customisable set of headers with text fields that the
 * user can change. Intended for the MessageComposer.<P>
 * Version History: <UL>
 * <LI>0.1 [11/06/98]: Initial Revision
 * <LI>0.2 [12/06/98]: Added visibility support. 
 * <LI>0.3 [13/06/98]: Bug fix to applyToHeader. Now sets the header field
 *   to a string, rather than a HeaderLine reference. (duh!). Also, the
 *   message header <b>does not</b> contain fields that are not visible.
 *</UL>
 @author Brian Duff
 @version 0.3 [13/06/98]
 */
class ComposerHeaderDisplay extends JPanel {

  /** Stores the JTextFields, indexed by header name */
  protected Hashtable m_lines;

  private GridBagLayout layoutMe = new GridBagLayout();

  public ComposerHeaderDisplay() {
     m_lines = new Hashtable();
     setLayout(layoutMe);
  }

// PUBLIC INTERFACE

  /**
   * Adds a header field to the component
   @param headerName the name of the header. Should not contain spaces, and
     is case insensitive. If the header already exists, a header field will
     not be added.
   @param initialValue the value to set the header field to initially. Can
     be the empty String ("").
   */
  public void addHeader(String headerName, String initialValue) {
     if (!m_lines.contains(headerName)) {
        int item = m_lines.size();
        HeaderLine line = new HeaderLine(
           addLabel(headerName+":", item),
           addField(initialValue, item, false),
           item
        );
        m_lines.put(headerName, line);
     } else {
        // if the hashtable already contains the header, make it visible
        HeaderLine line = (HeaderLine) m_lines.get(headerName);
        if (line.button != null) line.button.setVisible(true);
        line.field.setVisible(true);
        line.label.setVisible(true);
     }
     validateTree();       // force the panel to be packed
  }

  /**
   * Adds a header field with a button to the component. The button can be
   * used to display a dialogue or whatever.
   @param headerName the name of the header. Should not contain spaces, and
     is case insensitive. If the header already exists, a header field will
     not be added.
   @param initialValue the value to set the header field to initially. Can
     be the empty String ("").
   @param buttonIcon the image to display in the button.
   @param buttonListener an ActionListener that will recieve events when the
     button is clicked. The actionCommand of such events will be the name of
     the header (the headerName parameter).
   */
  public void addButtonHeader(String headerName, String initialValue,
     Icon buttonIcon, ActionListener buttonListener) {
     if (!m_lines.contains(headerName)) {
        int item = m_lines.size();
        HeaderLine line = new HeaderLine(
           addLabel(headerName+":", item),
           addField(initialValue, item, true),
           addButton(buttonIcon, item, buttonListener),
           item
        );
        m_lines.put(headerName, line);
     } else {
        HeaderLine line = (HeaderLine) m_lines.get(headerName);
        if (line.button != null) line.button.setVisible(true);
        line.field.setVisible(true);
        line.label.setVisible(true);
     }
     validateTree();
  }

  /**
   * Sets whether a header is visible.
   */
  public void setHeaderVisible(String headerName, boolean visible) {
     HeaderLine line = (HeaderLine) m_lines.get(headerName);

     if (line != null) {
        line.field.setVisible(visible);
        line.label.setVisible(visible);
        if (line.button != null) line.button.setVisible(visible);
        validateTree();
     }
  }

  /**
   * Removes a header field from the component.
   @param headerName the name of the header to remove. If the header doesn't
     exist, this method has no effect.
   */
  public void removeHeader(String headerName) {
     HeaderLine line =  (HeaderLine) m_lines.get(headerName);
     if (line != null) {
        remove(line.label);
        remove(line.field);
        if (line.button != null) remove(line.button);
        m_lines.remove(headerName);
        validateTree();
     }
  }

  /**
   * Removes all headers from the component.
   */
  public void removeAllHeaders() {
     m_lines.clear();
     removeAll();
     validateTree();
  }

  /**
   * Sets the value of a header.
   @param headerName the name of a header field. If it doesn't exist, this
     method has no effect.
   @param newValue the value to set the header to
   */
  public void setHeaderValue(String headerName, String newValue) {
     HeaderLine line = (HeaderLine) m_lines.get(headerName);
     if (line != null) {
        line.field.setText(newValue);
        repaint();
     }
  }

  /**
   * Retrieves the value of a header.
   @param headerName the name of a header field.
   @return the value of the specified header, or the empty String if it doesn't
     exist in the component.
   */
  public String getHeaderValue(String headerName) {
     HeaderLine line = (HeaderLine) m_lines.get(headerName);

     if (line != null) 
        return line.field.getText();
     else
        return "";

  }

  /**
   * Creates a new MessageHeader object with the contents of the component.
   @return a newly instantiated MessageHeader object, with all its fields
     set to the headers in the component. If fields are not visibile in the
     component, they are not included in the header.
   */
  public MessageHeader getMessageHeader() {
     MessageHeader hd = new MessageHeader();
     applyToHeader(hd);
     return hd;
  }

  /**
   * Modifies an existing MessageHeader object by adding or setting any headers
   * in the component. Doesn't remove any fields from the header, so if there
   * are fields in the original MessageHeader that are not present in the
   * component, they are not affected by this method.  If fields are not
   * visible in the component, they are not added or modified in the header.
   @param originalHeader the header object to modify.
   */
  public void applyToHeader(MessageHeader originalHeader) {
     Enumeration en = m_lines.keys();
     HeaderLine thisline;

     while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        thisline = (HeaderLine) m_lines.get(key);
        if (thisline.field.isVisible())
           originalHeader.setField(key, thisline.field.getText());
     }
  }

  /**
   * Sets whether a header field is editable.
   @param fieldName the name of the field
   @param editable if true, the field's input box  is enabled
   */
  public void setFieldEnabled(String fieldName, boolean editable) {
     HeaderLine line = (HeaderLine) m_lines.get(fieldName);

     if (line != null) {
        line.field.setEnabled(editable);
        repaint();
     }
  }

  /**
   * Sets whether a header field button is enabled
   @param fieldName the name of the field, which should be a ButtonHeader.
   @param enabled whether the button is enabled.
   */
  public void setButtonEnabled(String fieldName, boolean enabled) {
     HeaderLine line = (HeaderLine) m_lines.get(fieldName);

     if (line != null) {
        if (line.button != null) line.button.setEnabled(enabled);
        repaint();
     }
  }


  /**
   * Return a list of all visible headers in the control
   @return a vector containing string names of all headers that are visible
     in the control.
   */
  public Vector getHeaderList() {
     Enumeration e = m_lines.keys();
     Vector result = new Vector();

     while (e.hasMoreElements()) {
        String header = (String) e.nextElement();
        HeaderLine ln = (HeaderLine) m_lines.get(header);
        if (ln.field.isVisible()) {
           result.addElement(header);
        }
     }
     return result;
  }

// PRIVATE IMPLEMENTATION

  private JLabel addLabel(String label, int y) {
     JLabel lbl = new JLabel();

     lbl.setText(label);
     
     this.add(lbl, new GridBagConstraints2(0, y, 1, 1, 0.0, 0.0,
        GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(1,5,1,1), 0, 0));

     return lbl;
  }

  private JButton addButton(Icon icon, int y, ActionListener listener) {
     JButton cmd = new JButton();
     cmd.setIcon(icon);
     cmd.addActionListener(listener);
     cmd.setPreferredSize(new Dimension(icon.getIconWidth()+ 4, icon.getIconHeight()+4));

     this.add(cmd, new GridBagConstraints2(1, y, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(1, 1, 1, 1), 0, 0));
     return cmd;
  }

  private JTextField addField(String init, int y, boolean hasButton) {
     JTextField tf = new JTextField();
     tf.setText(init);

     this.add(tf, new GridBagConstraints2(hasButton ? 2 : 1, y, hasButton ? 1 : 2,
        1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets(1, 1, 1, 5), 0, 0));
     return tf;
  }

  /**
   * A utility class that conceptually represents each line of the header
   * component. Instances of this object are stored in a hashtable indexed by
   * message header field name.
   */
  class HeaderLine {
     /** The label component */
     public JLabel label;
     /** the editable text field component */
     public JTextField field;
     /** the button. Will be null if this line doesn't have a button. */
     public JButton button;
     /** The y pos */
     public int ypos;

     /**
      * Construct a header line
      */
     HeaderLine(JLabel lbl, JTextField field, JButton button, int y) {
        this.label = lbl; this.field = field; this.button = button; ypos = y;
     }

     /**
      * Construct a header line
      */
     HeaderLine(JLabel lbl, JTextField field, int y) {
        this(lbl, field, null, y);
     }

  }
}