// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: VersionEditorPanel.java,v 1.4 1999-11-11 21:24:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package org.javalobby.dju.misc;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.text.DateFormat;

/**
 * <p>
 * Panel for editing Version objects. Not NLSed.
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
 * @version 0.0 (DJU 0.0.01) [22/Nov/1998]
 */
public class VersionEditorPanel extends JPanel
{

   private JScrollPane m_scroll;
   private JTable      m_table;
   
   
   private static boolean m_wasCancelled;
   
   
   private static void setCancelled(boolean b)
   {
      m_wasCancelled = b;
   }
   
   private static boolean isCancelled()
   {
      return m_wasCancelled;
   }
   
   
   protected VersionEditorPanel(Version v)
   {
      m_table = new JTable(new VersionTableModel(v));
      m_scroll = new JScrollPane(m_table);
      add(m_scroll, BorderLayout.CENTER);
   }

   public static Version edit(Version v)
   {
      JFrame parent = new JFrame();
      final JDialog fra = new JDialog(parent, "Version Editor", true);
      JPanel but = new JPanel();
      JButton ok = new JButton("OK");
      JButton cancel = new JButton("Cancel");
      VersionEditorPanel vedit = new VersionEditorPanel(v);

      
      ok.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            fra.setVisible(false);
            fra.dispose();
            setCancelled(false);
         }
      });
      
      cancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            fra.setVisible(false);
            fra.dispose();
            setCancelled(true);
         }
      });
   
      but.setLayout(new FlowLayout(FlowLayout.RIGHT));
      but.add(ok);
      but.add(cancel);
      
      fra.getContentPane().add(but, BorderLayout.SOUTH);
      fra.getContentPane().add(vedit, BorderLayout.CENTER);
      
      fra.setResizable(false);
      fra.pack();
      fra.setVisible(true);
      
      parent.dispose();
      if (isCancelled()) return null;
      else return v;
   }

   class VersionTableModel implements TableModel
   {
      private Version v;
   
      private final int
         ROW_PRODUCTNAME = 0,
         ROW_PRODUCTCOPY = 1,
         ROW_RELEASEDATE = 2,
         ROW_MAJOR       = 3,
         ROW_MINOR       = 4,
         ROW_MICRO       = 5,
         ROW_BUILDNUM    = 6,
         ROW_BUILDLABEL  = 7;
         
      private final int ROW_COUNT = 8;
   
      public VersionTableModel(Version v)
      {
         this.v = v;
      }
   
      public void addTableModelListener(TableModelListener l)
      {
         //
      }
      
      public Class getColumnClass(int columnIndex)
      {
         try {
            return Class.forName("java.lang.String");
         } catch (Throwable t)
         {
            return null;
         }
      }
      
      public int getColumnCount() 
      {
         return 2;
      }
      
      public String getColumnName(int columnIndex)  
      {
         if (columnIndex == 0)
            return "Property";
         else
            return "Value";
      }
      
       
      public int getRowCount()
      {
         return ROW_COUNT;
      }
      
      public Object   getValueAt(int rowIndex, int columnIndex)  
      {
         switch (rowIndex)
         {
            case ROW_PRODUCTNAME:
               if (columnIndex == 0)
               {
                  return "Product Name";
               }
               else
               {
                  return v.getProductName();
               }
            case ROW_PRODUCTCOPY:
               if (columnIndex == 0)
               {
                  return "Product Copyright";
               }
               else
               {
                  return v.getProductCopyright();
               }
            case ROW_RELEASEDATE:
               if (columnIndex == 0)
               {
                  return "Release Date";
               }
               else
               {
                  return 
                    DateFormat.getDateInstance(DateFormat.SHORT).format(
                        v.getReleaseDate()
                    );
               }
            case ROW_MAJOR:
               if (columnIndex == 0)
               {
                  return "Major Version";
               }
               else
               {
                  return ""+v.getMajorVersion();
               }           
            case ROW_MINOR:
               if (columnIndex == 0)
               {
                  return "Minor Version";
               }
               else
               {
                  return ""+v.getMinorVersion();
               }       
            case ROW_MICRO:
               if (columnIndex == 0)
               {
                  return "Micro Version";
               }
               else
               {
                  return ""+v.getMicroVersion();
               }
            case ROW_BUILDNUM:
               if (columnIndex == 0)
               {
                  return "Build Number";
               }
               else
               {
                  return ""+v.getBuildNumber();
               }
            case ROW_BUILDLABEL:
               if (columnIndex == 0)
               {
                  return "Build Label";
               }
               else
               {
                  return v.getBuildLabel();
               }
         }
         return "";
      }
      
       
      public boolean   isCellEditable(int rowIndex, int columnIndex)  
      {
         return (columnIndex == 1);
      }
      
       
      public void   removeTableModelListener(TableModelListener l)  
      {
         //
      }
      
       
      public void setValueAt(java.lang.Object aValue, int rowIndex, int columnIndex) 
      {

         switch (rowIndex)
         {
            case ROW_PRODUCTNAME:
               v.setProductName((String)aValue);
            break;
            case ROW_PRODUCTCOPY:
               v.setProductCopyright((String)aValue);
            break;
            case ROW_RELEASEDATE:
               try {
                  v.setReleaseDate(
                     DateFormat.getDateInstance(DateFormat.SHORT).parse((String)aValue)
                  );
               }
               catch (java.text.ParseException pex)
               {
                  System.err.println("Parse Exception "+pex);
                  
               }
            break;
            case ROW_MAJOR:
               v.setMajorVersion(Integer.parseInt((String)aValue));
            break;            
            case ROW_MINOR:
               v.setMinorVersion(Integer.parseInt((String)aValue));
            break;       
            case ROW_MICRO:
               v.setMicroVersion(Integer.parseInt((String)aValue));
            break;
            case ROW_BUILDNUM:
               v.setBuildNumber(Integer.parseInt((String)aValue));
            break;
            case ROW_BUILDLABEL:
               v.setBuildLabel((String)aValue);
            break;
         }      
      }
   }
 
 
   public static void main(String[] args)
   {
      Version blah = new Version();
      blah = VersionEditorPanel.edit(blah);
      if (blah != null)
      {
         System.out.println(blah.getLongDescription());
      }
   }
 
}