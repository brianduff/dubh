// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormatBar.java,v 1.2 1999-11-11 21:24:35 briand Exp $
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
package org.javalobby.dju.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * A format bar is a component that displays some sample text in the 
 * current format, along with a button that allows the user to change the
 * format (it brings up the {@link #org.javalobby.dju.ui.FormattingChooser} dialog)
 *
 * @author Brian Duff
 * @version $Id: FormatBar.java,v 1.2 1999-11-11 21:24:35 briand Exp $
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
// Revision 1.1  1999/06/01 00:17:34  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
//