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
package dubh.apps.newsagent.dialog;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.apps.newsagent.dialog.NDialog;


/**
 * Displays and animation and a message to the user during long operations.<BR>
 * Version History: <UL>
 * <LI>0.1 [25/03/98]: Initial Revision
 * <LI>0.2 [05/04/98]: Now extends NDialog
 * <LI>0.3 [20/04/98]: Changed to JPanel
 *</UL>
 @author Brian Duff
 @version 0.3 [20/04/98]
 */
public class LongOperationDialog extends NDialog implements Runnable {
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