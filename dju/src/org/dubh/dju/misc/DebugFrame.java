// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DebugFrame.java,v 1.4 2001-02-11 02:52:11 briand Exp $
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

package org.dubh.dju.misc;

import java.io.*;
import org.dubh.dju.ui.StreamTextArea;
import org.dubh.dju.ui.DubhFrame;
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