// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DebugFrame.java,v 1.2 1999-03-22 23:37:17 briand Exp $
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
package dubh.utils.misc;

import java.io.*;
import dubh.utils.ui.StreamTextArea;
import dubh.utils.ui.DubhFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A frame for displaying debug output. Can be used in place of stderr/
 * stdout
 * @author Brian Duff
 */
public class DebugFrame extends DubhFrame
{
   private StreamTextArea m_text;
   private JPanel         m_topPanel;
   private JLabel         m_traceLabel;
   private JSlider        m_traceLevel;
   private JCheckBox      m_asserts;
   
   public DebugFrame(String id, String title)
   {
      super(title);
      setName(id);
      initComponents();
   }
   
   public OutputStream getStream()
   {
      return m_text.getStream();
   }
   
   /**
    * Set whether the top panel is visible.
    */
   public void setControlsVisible(boolean b)
   {
      m_topPanel.setVisible(b);
      getContentPane().invalidate();
   }
   
   public void clear()
   {
      m_text.setText("");
   }
   
   private void initComponents()
   {
      m_text = new StreamTextArea();
      m_topPanel = new JPanel();
      m_topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      m_traceLabel = new JLabel("Trace level");
      m_topPanel.add(m_traceLabel);
      m_traceLevel = new JSlider(0, 3, Debug.getTraceLevel());
      m_traceLevel.setSnapToTicks(true);
      m_traceLevel.setPaintTicks(true);
      m_traceLevel.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e)
         {
            Debug.setTraceLevel(m_traceLevel.getValue());   
         }
      });   
      
      m_topPanel.add(m_traceLevel);
      
      m_asserts = new JCheckBox("Asserts Enabled");
      m_asserts.setSelected(Debug.isAssertEnabled());
     
      m_asserts.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            Debug.setAssertEnabled(m_asserts.isSelected());
         } 
      });
      
      m_topPanel.add(m_asserts);
      
      getContentPane().add(m_topPanel, BorderLayout.NORTH);
      getContentPane().add(new JScrollPane(m_text), BorderLayout.CENTER);
   }
     
}