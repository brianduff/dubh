// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: LongOperationDialog.java,v 1.5 1999-11-09 22:34:41 briand Exp $
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
package org.javalobby.apps.newsagent.dialog;

import java.awt.*;
import java.awt.event.*;
import org.javalobby.dju.ui.GridBagConstraints2;
import javax.swing.*;
import org.javalobby.dju.ui.DubhDialog;



/**
 * Displays and animation and a message to the user during long operations.<BR>
 @author Brian Duff
 @version $Id: LongOperationDialog.java,v 1.5 1999-11-09 22:34:41 briand Exp $
 */
public class LongOperationDialog extends DubhDialog implements Runnable {
  JPanel panMain = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel lblSideImage = new JLabel();
  JLabel lblMessage = new JLabel();
  JLabel lblBottomImage = new JLabel();
  JButton cmdCancel = new JButton();
  boolean m_userCancelled;

  public LongOperationDialog(Frame parent, boolean modal) {
   super(parent, modal);
    try {
      jbInit();
      getContentPane().add(panMain);
      pack();

      m_userCancelled = false;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void run() {


     setVisible(true);
  }

  private void jbInit() throws Exception{
   // lblSideImage.setText("Icon");
   lblSideImage.setDoubleBuffered(true);
   lblSideImage.setOpaque(true);
    lblMessage.setText("User Message");
    lblMessage.setOpaque(true);
   // lblBottomImage.setText("Icon");
    cmdCancel.setText("Cancel");
    cmdCancel.addActionListener(new LongOperationDialog_cmdCancel_actionAdapter(this));
    panMain.setLayout(gridBagLayout1);
    panMain.add(lblSideImage, new GridBagConstraints2(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 50, 50));
    panMain.add(lblMessage, new GridBagConstraints2(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 200, 50));
    panMain.add(lblBottomImage, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panMain.add(cmdCancel, new GridBagConstraints2(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
  }

  void cmdCancel_actionPerformed(ActionEvent e) {
     m_userCancelled = true;
  }

  public void setLeftIcon(Icon i) {
     lblBottomImage.setIcon(null);
     lblSideImage.setIcon(i);
  }

  public void setBottomIcon(Icon i) {
     lblBottomImage.setIcon(i);
     lblSideImage.setIcon(null);
  }

  public void setText(String s) {
     lblMessage.setText(s);
    // repaint();
   //  lblMessage.repaint();
  }

  public boolean userCancelled() {
     return m_userCancelled;
  }

}

class LongOperationDialog_cmdCancel_actionAdapter implements java.awt.event.ActionListener{
  LongOperationDialog adaptee;

  LongOperationDialog_cmdCancel_actionAdapter(LongOperationDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdCancel_actionPerformed(e);
  }
}

/**
 * Old History
  * Version History: <UL>
 * <LI>0.1 [25/03/98]: Initial Revision
 * <LI>0.2 [05/04/98]: Now extends NDialog
 * <LI>0.3 [20/04/98]: Changed to JPanel
 *</UL>
 */
//
// New history
// 
// $Log: not supported by cvs2svn $
// Revision 1.4  1999/06/01 00:23:40  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog.
//
//