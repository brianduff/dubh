/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import dubh.apps.newsagent.GlobalState;

/**
 * Displayed when the user clicks Add.. on the SignatureOptions pane.
 *
 * Version History: <UL>
 * <LI>0.1 [06/03/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [06/03/98]
 */
public class SignatureAdd extends JDialog {
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
    jLabel1.setText(GlobalState.getResString("SignatureAdd.Title"));
    jTextField1.setToolTipText(GlobalState.getResString("SignatureAdd.TitleTip"));
    jLabel2.setText(GlobalState.getResString("SignatureAdd.Sig"));
    jTextArea1.setRows(5);
    jScroll1.setPreferredSize(new Dimension(390, 75));
    jTextArea1.setToolTipText(GlobalState.getResString("SignatureAdd.SigTip"));
    jPanel1.setLayout(flowLayout1);
    jButton1.setText(GlobalState.getResString("GeneralOK"));
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