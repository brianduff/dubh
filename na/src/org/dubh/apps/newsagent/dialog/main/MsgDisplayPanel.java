// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MsgDisplayPanel.java,v 1.3 1999-03-22 23:46:00 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.apps.newsagent.dialog.main;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;

import dubh.apps.newsagent.dialog.ErrorReporter;

/**
 * Displays the body of a message
 * <LI>0.1 [05/03/98]: Initial Revision
 * <LI>0.2 [10/03/98]: Set minimum size
 * <LI>0.3 [01/04/98]: Implemented setMessage.
 * <LI>0.4 [02/04/98]: Implemented getMessageBody. Made non editable. Forced
 *   text pane to jump back to start when text is set. Added clear method.
 * <LI>0.5 [07/04/98]: Added setActive();
 * <LI>0.6 [08/05/98]: Added clipboard operations. Well copy anyway.
 * <LI>1.0 [09/05/98]: Major change - now supports a configurable, scrolling
 *   message header part. This is part of my attempts to become more conformant
 *   with the suggested guidelines for USENET readers, the user can now
 *   configure which headers are displayed in this section.
 * <LI>1.1 [10/06/98]: Added getSelectedText() method so that reply quoting
 *   works properly.
 @author Brian Duff
 @version 1.1 [10/06/98]
 */
public class MsgDisplayPanel extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel topPanel = new JPanel();
  MsgDisplayHeaders panHeader = new MsgDisplayHeaders();
  JTextPane textBody = new JTextPane();
  JScrollPane scrollBody = new JScrollPane(textBody);
  JScrollPane scrollHead = new JScrollPane(panHeader);

  JPanel panHeaderFields = new JPanel();

  GridBagLayout mainLayout  = new GridBagLayout();

  private MessageBody m_body;

  public MsgDisplayPanel() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    this.setLayout(borderLayout1);
    topPanel.setMinimumSize(new Dimension(100, 50));
    topPanel.setPreferredSize(new Dimension(100, 50));
    topPanel.setLayout(borderLayout2);
    topPanel.add(scrollHead, BorderLayout.CENTER);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(scrollBody, BorderLayout.CENTER);
    textBody.setEditable(false);
    clear();
  }

  /**
   * Sets whether this UI component is allowed to respond to user selections
   */
  public void setActive(boolean active) {
   // jtreeFolders.setEnabled(active);
   // jtreeFolders.repaint();

  }

  /**
   * Sets the message to be displayed in the panel.
   */
  public void setMessage(MessageHeader head, MessageBody body) {
     textBody.setText(body.getText());
     textBody.select(0,0);    // Go back to start of message.
     m_body = body;
     panHeader.setHeader(head);
     panHeader.invalidate();
  }

  /**
   * Retrieves the body of the message currently being displayed in the panel.
   */
  public MessageBody getMessageBody() {
   return m_body;
  }

  /**
   * Clears the panel
   */
  public void clear() {
     textBody.setText("");
     m_body = null;
     panHeader.clear();
  }

  /**
   * Copies the current selection into the clipboard
   */
  public void copy() {
     textBody.copy();
  }

  /**
   * Gets the currently selected text in the display
   @return a string containing all text that is currently selected
   */
  public String getSelectedText() {
     return textBody.getSelectedText();
  }


  /**
   * Displays a collection of headers at the top of the message text. It would
   * be sensible to put this in a ScrollPane, or it might get a bit big if the
   * user configures NewsAgent to have lots of headers here.
   */
  class MsgDisplayHeaders extends JPanel {
     private GridBagLayout myLayout  = new GridBagLayout();
     private String[] m_headers = new String[0];
     private JLabel[] m_labels;
     private java.util.Hashtable m_hash;

     /**
      * Construct the default message header display, which just shows the
      * From: and Subject: headers.
      */
     public MsgDisplayHeaders() {
      //  this(new String[] {"From", "Subject"});
        this(new String[] {"From", "Subject", "Followup-To", "References"});
     }

     /**
      * Construct a message header display, consisting of the specified headers
      @param headers the header fields to display, if they exist in the message
      */
     public MsgDisplayHeaders(String[] headers) {
        setLayout(myLayout);
        setHeaderFields(headers);
     }

     /**
      * Clear the contents of all header fields
      */
     public void clear() {
        for (int i=1; i<m_headers.length; i++)
           clearLabelText(m_headers[i]);
        repaint();
     }

     /**
      * Set the headers to use
      @param headers An array of header fields to be displayed in this panel
      */
     public void setHeaderFields(String[] headers) {
        removeAllHeaders();
        m_headers = headers;
        createLabels();
     }

     /**
      * Get the headers this panel is displaying
      @return an array of header fields
      */
     public String[] getHeaderFields() {
        return m_headers;
     }

     /**
      * Set the header to display
      */
     public void setHeader(MessageHeader hd) {
        for(int i=0; i<m_headers.length; i++) {
           String val;
           try {
              val = hd.getFieldValue(m_headers[i]);
           } catch (IllegalArgumentException e) {
              val = "<no such header>";
           }
           setHeaderText(m_headers[i], val);
           repaint();
        }
     }


  // Private parts

     /**
      * Create labels for all the headers.
      */
     private void createLabels() {
        m_hash = new java.util.Hashtable();
        m_labels = new JLabel[m_headers.length];

        for (int i=0; i<m_headers.length; i++) {
           m_labels[i] = new JLabel();
           m_hash.put(m_headers[i], m_labels[i]);
           clearLabelText(m_headers[i]);
           if (i == m_headers.length-1)
              this.add(m_labels[i], new GridBagConstraints2(0, i, 1, 1, 1.0, 1.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
              new Insets(2, 5, 1, 5), 0, 0));
           else
              this.add(m_labels[i], new GridBagConstraints2(0, i, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
              new Insets(2, 5, 1, 5), 0, 0));
        }
        this.invalidate();
        this.revalidate();   // force scrollbars to update
     }

     /**
      * Clear the contents of a label, leaving the header field and a colon
      * intact.
      */
     private void clearLabelText(String labelName) {
        try {
           ((JLabel)m_hash.get(labelName)).setText(labelName+":");
        } catch (Exception e) {
           ErrorReporter.debug("Can't find the "+labelName+" header to delete!");
        }
     }

     /**
      * Removes all headers from the panel
      */
     private void removeAllHeaders() {
        m_hash = new java.util.Hashtable();
        m_labels = new JLabel[m_headers.length];
        removeAll();
     }

     /**
      * Set the text of a label (the part *after* the colon)
      @param header the header to set
      @param text the value of the header
      */
     private void setHeaderText(String header, String text) {
        try {
           ((JLabel)m_hash.get(header)).setText(header+": "+text);
        } catch (Exception e) {
           ErrorReporter.debug("Can't find the "+header+" header.");
        }
     }

  }

}