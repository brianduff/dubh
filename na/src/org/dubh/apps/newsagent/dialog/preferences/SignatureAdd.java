// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: SignatureAdd.java,v 1.6 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.dju.ui.DubhDialog;

/**
 * Displayed when the user clicks Add.. on the SignatureOptions pane.
 @author Brian Duff
 @version $Id: SignatureAdd.java,v 1.6 2001-02-11 02:51:00 briand Exp $
 */
public class SignatureAdd extends DubhDialog {
  JPanel panel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel2 = new JLabel();
  JTextArea jTextArea1 = new JTextArea();
  JPanel jPanel1 = new JPanel();
  JButton jButton1 = new JButton();
  JScrollPane jScroll1 = new JScrollPane(jTextArea1);
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  FlowLayout flowLayout1 = new FlowLayout();

  public SignatureAdd(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      add(panel1);
      pack();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SignatureAdd(Frame frame) {
    this(frame, "", false);
  }

  public SignatureAdd(Frame frame, boolean modal) {
    this(frame, "", modal);
  }

  public SignatureAdd(Frame frame, String title) {
    this(frame, title, false);
  }

  private void jbInit() throws Exception{
    jLabel1.setText(GlobalState.getRes().getString("SignatureAdd.Title"));
    jTextField1.setToolTipText(GlobalState.getRes().getString("SignatureAdd.TitleTip"));
    jLabel2.setText(GlobalState.getRes().getString("SignatureAdd.Sig"));
    jTextArea1.setRows(5);
    jScroll1.setPreferredSize(new Dimension(390, 75));
    jTextArea1.setToolTipText(GlobalState.getRes().getString("SignatureAdd.SigTip"));
    jPanel1.setLayout(flowLayout1);
    jButton1.setText(GlobalState.getRes().getString("GeneralOK"));
    jButton1.addActionListener(new SignatureAdd_jButton1_actionAdapter(this));
    panel1.setLayout(gridBagLayout1);
    panel1.add(jLabel1, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 5), 0, 0));
    panel1.add(jTextField1, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 5), 0, 0));
    panel1.add(jLabel2, new GridBagConstraints2(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 5), 0, 0));
    panel1.add(jScroll1, new GridBagConstraints2(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 5, 5, 5), -310, 111));
    panel1.add(jPanel1, new GridBagConstraints2(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(jButton1, null);
  }

  /**
   * Retieves the title entered by the user.
   */
  public String getTitle() {
   return jTextField1.getText();
  }

  /**
   * Retrieves the signature entered by the user.
   */
  public String getSig() {
   return jTextArea1.getText();
  }

  /**
   * Sets the title
   */
  public void setTitle(String t) {
   jTextField1.setText(t);
  }

  /**
   * Sets the signature
   */
  public void setSig(String t) {
   jTextArea1.setText(t);
  }

  /**
   * Tells this SignatureAdd that the title is fixed and cannot be edited.
   */
  public void setTitleEnabled(boolean b) {
   jTextField1.setEnabled(b);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }

}

class SignatureAdd_jButton1_actionAdapter implements java.awt.event.ActionListener{
  SignatureAdd adaptee;

  SignatureAdd_jButton1_actionAdapter(SignatureAdd adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:33:59  briand
// (this file was automatically updated, but is now obsolete)
//
//