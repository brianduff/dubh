// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: TestHarness.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.ui.test;

import org.dubh.dju.ui.*;
import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;

/**
 * Dubh Utils UI test harness
 *
 * @author Brian Duff
 * @version $Id: TestHarness.java,v 1.4 2001-02-11 02:52:12 briand Exp $
 */
public class TestHarness
{
   public static void main(String[] args)
   {
      try
      {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (Throwable t)
      {}
      //testColorSwatch();
      //testHSB();
      //testJava12Fonts();
      testTable();
   }

   public static void testTable()
   {
      JFrame f = new JFrame();



      TableModel dataModel = new AbstractTableModel() {
          public int getColumnCount() { return 2; }
          public int getRowCount() { return 10;}
          public Object getValueAt(int row, int col) {
            if (col == 0) return "From";
            if (col == 1) return "Brian Duff <dubh@btinternet.com>";
            return "Erm";
          }
      };
      JTable tab = new JTable(dataModel);
      tab.setTableHeader(null);
      JScrollPane scrollpane = new JScrollPane(tab);

      // Calculate the widest renderer in the first column
      int rows = tab.getModel().getRowCount();

      tab.getColumnModel().getColumn(0).setCellRenderer(new HeaderFieldNameRenderer());
      int maxWidth = 15;
      for (int i=0; i < rows; i++)
      {
         TableCellRenderer r = tab.getCellRenderer(i, 0);
         Component c = r.getTableCellRendererComponent(
            tab, tab.getModel().getValueAt(i, 0), false, false, i, 0
         );
         maxWidth = Math.max(maxWidth, c.getPreferredSize().width);
      }

      TableColumnModel tcm = tab.getColumnModel();

      TableColumn c = tcm.getColumn(0);
      c.setMaxWidth(maxWidth);
      c.setMinWidth(maxWidth);
      c.setPreferredWidth(maxWidth);
      c.setResizable(false);

      f.getContentPane().add(scrollpane, BorderLayout.CENTER);
      f.pack();
      f.setVisible(true);

   }

   /**
    * Renderer for header field names
    */
   static class HeaderFieldNameRenderer extends JLabel implements TableCellRenderer
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


   public static void testJava12Fonts()
   {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

      String[] f = ge.getAvailableFontFamilyNames();

      for (int i=0; i < f.length; i++)
      {
         System.out.println("Font "+i+": "+f[i]);
      }
   }

   public static void testHSB()
   {
      JPanel pan = new JPanel();

      pan.setLayout(new FlowLayout());

      Color c = Color.red;

      pan.add(new ColorSwatch(c, false));

      float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

      System.out.println("HSB of red: "+hsb[0]+", "+hsb[1]+", "+hsb[2]);



      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
      pan.add(new ColorSwatch(c, false));

      hsb[1] = (float)1.0;

      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
      pan.add(new ColorSwatch(c, false));

      hsb[1] = (float)0.1;

      c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
      pan.add(new ColorSwatch(c, false));



      showFrame(pan);
   }

   public static void showFrame(JPanel p)
   {
      JFrame f = new JFrame();

      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(p, BorderLayout.CENTER);

      f.pack();
      f.setVisible(true);
   }

   public static void testColorSwatch()
   {
      JFrame fraSwatchFrame = new JFrame();
      JPanel pan = new JPanel();

      Color[][] winColors = ColorPopup.generateDefaultColorSet(ColorPopup.DEFAULT_BASECOLORS);

      /*{
         {
         new Color(255, 255, 255),
         new Color(0, 0, 0),
         new Color(192, 192, 192),
         new Color(128, 128, 128)
         },
         {
         new Color(255, 0, 0),
         new Color(128, 0, 0),
         new Color(255, 255, 0),
         new Color(128, 128, 0)
         },
         {
         new Color(0, 255, 0),
         new Color(0, 128, 0),
         new Color(0, 255, 255),
         new Color(0, 128, 128)
         },
         {
         new Color(0, 0, 255),
         new Color(0, 0, 128),
         new Color(255, 0, 255),
         new Color(128, 0, 128)
         },
         {
         new Color(127, 127, 127),
         new Color(166, 202, 240),
         new Color(224, 224, 224),
         new Color(160, 160, 164)
         }
      };
      */

      fraSwatchFrame.getContentPane().setLayout(new BorderLayout());

      GridLayout gl = new GridLayout(winColors.length, winColors[0].length, 0, 0);
      pan.setLayout(gl);

      for (int i=0; i < winColors.length; i++)
      {
         for (int j=0; j < winColors[0].length; j++)
         {
            pan.add(new ColorSwatch(winColors[i][j], false, ColorSwatch.STYLE_MSWORD_POPUP));
         }
      }

      fraSwatchFrame.getContentPane().add(pan, BorderLayout.WEST);

      fraSwatchFrame.pack();
      fraSwatchFrame.setVisible(true);
   }

}