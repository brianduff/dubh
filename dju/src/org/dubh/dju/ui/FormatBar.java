// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormatBar.java,v 1.1 1999-06-01 00:17:34 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package dubh.utils.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * A format bar is a component that displays some sample text in the 
 * current format, along with a button that allows the user to change the
 * format (it brings up the {@link #dubh.utils.ui.FormattingChooser} dialog)
 *
 * @author Brian Duff
 * @version $Id: FormatBar.java,v 1.1 1999-06-01 00:17:34 briand Exp $
 */
public class FormatBar extends JPanel 
{
   public static final String DEFAULT_SAMPLE = "Sample Text";
   public static final String DEFAULT_CHANGELBL = "Change...";

   private JLabel lblSample;
   private JButton cmdChange;
   
   public FormatBar()
   {
      this(DEFAULT_SAMPLE);
   }
   
   public FormatBar(String sampleText)
   {
      lblSample = new JLabel();
      lblSample.setBackground(Color.white);
      lblSample.setBorder(BorderFactory.createEtchedBorder());
      lblSample.setOpaque(true);
      cmdChange = new JButton();

      setSampleLabel(sampleText);
      cmdChange.setText(DEFAULT_CHANGELBL);
      cmdChange.addActionListener(new ButtonListener());
      
      initLayout();      
   }
   
   public FormatBar(String sampleText, Color c, Font f)
   {
      this(sampleText);
      setFormatFont(f);
      setFormatColor(c);
   }
   
   public void setSampleLabel(String s)
   {
      lblSample.setText(s);
      validate();
      
   }
   
   private void initLayout()
   {
      this.setLayout(new BorderLayout());
      this.add(lblSample, BorderLayout.CENTER);
      this.add(cmdChange, BorderLayout.EAST);
   }
   
   public void setFormatFont(Font f)
   {
      lblSample.setFont(f);
      validate();
      repaint();
   }
   
   public void setFormatColor(Color c)
   {
      lblSample.setForeground(c);
      repaint();
   }
   
   public Font getFormatFont()
   {
      return lblSample.getFont();
   }
   
   public Color getFormatColor()
   {
      return lblSample.getForeground();
   }
   
   class ButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JFrame temp = new JFrame();
         FormattingChooser c = FormattingChooser.doDialog(temp, getFormatFont(), getFormatColor());
         
         if (c != null)
         {
            setFormatFont(c.getFormatFont());
            setFormatColor(c.getFormatColor());
         }
         
         temp.dispose();
      }
   }
   
   public static void main(String[] args)
   {
      JFrame test = new JFrame();
      
      test.getContentPane().setLayout(new BorderLayout());
      test.getContentPane().add(new FormatBar());
      test.pack();
      test.setVisible(true);
      
   }
   
}

//
// $Log: not supported by cvs2svn $
//