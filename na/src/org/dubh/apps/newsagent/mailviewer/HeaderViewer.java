// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: HeaderViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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

package org.javalobby.apps.newsagent.mailviewer;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.javalobby.dju.ui.GridBagConstraints2;

/**
 * Displays the headers of a message. Can be used either in composition
 * mode or display mode. You can customize the way any headers are
 * edited and displayed by calling registerCustomEditor with an object
 * that implements the HeaderEditor interface.
 *
 * @author Brian Duff
 * @version $Id: HeaderViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class HeaderViewer extends JPanel 
{

   protected final static String DELIMITER = ";";

   protected String[] m_headers = new String[0];
   protected JTable m_table;
   protected HeaderModel m_hmDataModel;
   protected JScrollPane m_scroll;
   protected Hashtable m_hash;
   protected Hashtable m_hashEditors;
   
   protected MimeMessage m_mmMessage;
   
   protected static final String[] DEFAULT_HEADERS = {
      "From",
      "Subject"
   }; 

   /**
    * Construct the default message header display, which just shows the
    * From: and Subject: headers.
    */
   public HeaderViewer() 
   {
     this(DEFAULT_HEADERS);
   }

  /**
   * Construct a message header display, consisting of the specified headers
   * @param headers the header fields to display, if they exist in the message
   */
  public HeaderViewer(String[] headers) 
  {
     
     m_table = createNewTable();
     m_scroll = new JScrollPane(m_table);
     this.setLayout(new BorderLayout());
     this.add(m_scroll, BorderLayout.CENTER);
     m_hashEditors = new Hashtable();
     setHeaderFields(headers);
  }
  
  /**
   * Create a new table, set up defaults.
   */
  protected JTable createNewTable()
  {
     JTable table = new JTable();
     m_hmDataModel = new HeaderModel();
     table.setModel(m_hmDataModel);
     
     table.setTableHeader(null);
     table.setShowGrid(false);
     
     // Set up renderers
     table.getColumnModel().getColumn(0).setCellRenderer(new HeaderFieldNameRenderer());
     table.getColumnModel().getColumn(1).setCellRenderer(new HeaderFieldValueRenderer());
          
     return table;
  }
  
  /**
   * This calculates the column width of the first column based on 
   * the width of the field name renderers in that column, and
   * makes it non-resizable.
   */
  protected void calculateColumnWidths()
  {
      // Calculate the widest renderer in the first column
      int rows = m_table.getModel().getRowCount();
  
      int maxWidth = 100;
      
      for (int i=0; i < rows; i++)
      {
         TableCellRenderer r = m_table.getCellRenderer(i, 0);
         Component c = r.getTableCellRendererComponent(
            m_table, m_table.getModel().getValueAt(i, 0), false, false, i, 0
         );
         maxWidth = Math.max(maxWidth, c.getPreferredSize().width);
      }
      
      TableColumnModel tcm = m_table.getColumnModel();
      
      TableColumn c = tcm.getColumn(0);
      c.setMaxWidth(maxWidth);
      c.setMinWidth(maxWidth);
      c.setPreferredWidth(maxWidth);
      c.setResizable(false);
  }

   

  /**
   * Set the headers to use
   * @param headers An array of header fields to be displayed in this panel
   */
  public void setHeaderFields(String[] headers) 
  {
     m_headers = headers;
     m_hmDataModel.notifyChange();
     calculateColumnWidths();
     invalidate();
  }

  /**
   * Get the headers this panel is displaying
   * @return an array of header fields
   */
  public String[] getHeaderFields() 
  {
     return m_headers;
  }

  /**
   * Set the message who's header we are to display.
   */
  public void setMessage(MimeMessage msg) 
     throws MessagingException
  {
     m_mmMessage = msg;
     
     m_hmDataModel.notifyChange();
  }

  /**
   * Use this method to register a custom editor for a header field
   */
  public void registerCustomEditor(String header, HeaderEditor ed)
  {
     m_hashEditors.put(header, ed);
  }
  
  /**
   * Get a component responsible for editing the specified header.
   * Creates a new default editor if a custom one hasn't been 
   * registered
   */
  protected HeaderEditor getEditorFor(String header)
  {
     HeaderEditor h = (HeaderEditor)m_hashEditors.get(header);
     
     if (h == null)
     {
        h = new DefaultHeaderEditor();
        m_hashEditors.put(header, h);
     }
     
     return h;
  }

  /**
   * Used by the table model to get a header value. Should return
   * an empty string if there is no current message.
   */  
  protected String getHeaderValue(String headerName)
  {
     try
     {
        if (m_mmMessage == null) return "";
        return m_mmMessage.getHeader(headerName, ";");
     }
     catch (MessagingException me)
     {
        // what to do with this?
        System.err.println(me);
     }
     return "";
  }
  


   /**
    * This header model is the model used in the table that
    * contains all the headers
    */
   class HeaderModel extends AbstractTableModel
   {
   
      void notifyChange()
      {
         fireTableDataChanged();
      }
      /**
       * This corresponds to the number of headers being
       * displayed
       */
      public int getRowCount()
      {
         return m_headers.length;
      }
      
      /**
       * This is always 2
       */
      public int getColumnCount()
      {
         return 2;
      }
      
      /**
       * Gets the header name (for column 0) or the header
       * value (for column 1)
       */
      public Object getValueAt(int row, int column)
      {   
         if (column == 0) return m_headers[row];
         else if (column == 1)
         {
            return getHeaderValue(m_headers[row]);
         }
         
         throw new IllegalArgumentException("Column "+column+" doesn't exist in table!");
      }
      
      /**
       * Set the header value
       */
      public void setValueAt(int row, int column, Object value)
      {
         if (column != 1) 
            throw new IllegalArgumentException("Can only edit header values!");
         try
         {
            m_mmMessage.setHeader(m_headers[row], value.toString());
         }
         catch (MessagingException me)
         {
            System.out.println(me);
         }
      }
   }

   /**
    * Delegates through to custom HeaderEditor instances to 
    * provide rendering services for a header field.
    */
   class HeaderFieldValueRenderer implements TableCellRenderer
   {
      public Component getTableCellRendererComponent(JTable table, 
         Object value, 
         boolean isSelected, 
         boolean hasFocus, 
         int row, 
         int column) 
      {
         if (column != 1) 
            throw new IllegalStateException("This renderer is for the value column");
         HeaderEditor ed = getEditorFor(m_headers[row]);
         ed.setReadOnly(true);
         ed.setHeaderValue(value.toString());
         return ed.getComponent();
      }
   
   }

   /**
    * Renderer for header field names
    */
   class HeaderFieldNameRenderer extends JLabel implements TableCellRenderer
   {
      public Component getTableCellRendererComponent(JTable table, 
         Object value, 
         boolean isSelected, 
         boolean hasFocus, 
         int row, 
         int column) 
      {
         Font f = getFont();
         if (f.getStyle() != Font.BOLD)
         {
            setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
         }
         setForeground(Color.white);
         setBackground(Color.gray);
         setText(value.toString());
         setOpaque(true);
         return this;
      }
      
      
   }

}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/17 17:02:59  briand
// Initial revision.
//
//