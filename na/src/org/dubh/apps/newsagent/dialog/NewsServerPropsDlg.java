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
import java.io.IOException;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.nntp.NNTPServer;
import dubh.apps.newsagent.nntp.NNTPServerException;
import dubh.utils.misc.StringUtils;
import dubh.utils.misc.ResourceManager;
/**
 * Edits the preferences of a particular news server.<P>
 *
 * Version History: <UL>
 * <LI>0.1 [08/03/98]: Initial Revision
 * <LI>0.2 [23/03/98]: Major change to the way NNTP Server preferences are
 *      saved. Now using serialisation.
 * <LI>0.3 [20/04/98]: Changed to a subclass of NDialog, Internationalised
 * <LI>0.4 [21/04/98]: Changed to use nntpException for error handling.
 * <LI>0.5 [28/04/98]: Added fix for bug #10: Checking for no port number.
 * <LI>0.6 [08/05/98]: Changed button order and aligned right.
 * <LI>0.7 [06/06/98]: Added dubh utils import for StringUtils
 * <LI>0.8 [05/10/98]: Fixed button initialisation to use new resource strings.
 *</UL>
 @author Brian Duff
 @version 0.8 [05/10/98]
 */
public class NewsServerPropsDlg extends NDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel panBottom = new JPanel();
  JButton cmdOK = new JButton();
  JPanel panel2 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labServerName = new JLabel();
  JTextField tfServerName = new JTextField();
  JLabel labHostName = new JLabel();
  JTextField tfHostName = new JTextField();
  JCheckBox chkRequireLogon = new JCheckBox();
  JLabel labLogin = new JLabel();
  JTextField txtLogin = new JTextField();
  JLabel lblPassword = new JLabel();
  JPasswordField tfPassword = new JPasswordField();
  JPanel jPanel1 = new JPanel();
  JLabel labPort = new JLabel();
  JTextField tfPort = new JTextField();
  JButton cmdDefault = new JButton();
  JButton cmdCancel  = new JButton();

  public NewsServerPropsDlg(JFrame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      getContentPane().add(panel1);
      pack();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public NewsServerPropsDlg(JFrame frame) {
    this(frame, "", false);
  }

  public NewsServerPropsDlg(JFrame frame, boolean modal) {
    this(frame, "", modal);
  }

  public NewsServerPropsDlg(JFrame frame, String title) {
    this(frame, title, false);
  }

  private void jbInit() throws Exception{
      // this.setTitle("Edit News Server Options");
      ResourceManager r = GlobalState.getRes();
      panel1.setSize(new Dimension(389, 294));
      cmdOK.setText(GlobalState.getResString("GeneralOK"));
      cmdOK.addActionListener(new NewsServerPropsDlg_cmdOK_actionAdapter(this));
      cmdCancel.setText(GlobalState.getResString("GeneralCancel"));
      cmdCancel.addActionListener(new NewsServerPropsDlg_cmdCancel_actionAdapter(this));
      labServerName.setText(GlobalState.getResString("NewsServerPropsDlg.NiceName"));
      tfServerName.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.NiceNameTip"));
      //chkRequireLogon.setText(GlobalState.getResString("NewsServerPropsDlg.RequireLogin"));
      //chkRequireLogon.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.RequireLoginTip"));
      //chkRequireLogon.setMnemonic(GlobalState.getResString("NewsServerPropsDlg.RequireLoginAccelerator").charAt(0));
//NLS      r.initButton(chkRequireLogon, "NewsServerPropsDlg.RequireLogin");
      labHostName.setText(GlobalState.getResString("NewsServerPropsDlg.ServerHostname"));
      tfHostName.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.ServerHostnameTip"));
      chkRequireLogon.addActionListener(new NewsServerPropsDlg_chkRequireLogon_actionAdapter(this));
      labLogin.setText(GlobalState.getResString("NewsServerPropsDlg.LoginName"));
      txtLogin.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.LoginNameTip"));
      lblPassword.setText(GlobalState.getResString("NewsServerPropsDlg.Password"));
      tfPassword.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.PasswordTip"));
      labPort.setText(GlobalState.getResString("NewsServerPropsDlg.ServerPort"));
      tfPort.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.ServerPortTip"));
      tfPort.setText("119");
      //cmdDefault.setText(GlobalState.getResString("NewsServerPropsDlg.Default"));
      //cmdDefault.setToolTipText(GlobalState.getResString("NewsServerPropsDlg.DefaultTip"));
      //cmdDefault.setMnemonic(GlobalState.getResString("NewsServerPropsDlg.DefaultAccelerator").charAt(0));
      cmdDefault.addActionListener(new NewsServerPropsDlg_cmdDefault_actionAdapter(this));
 //NLS     r.initButton(cmdDefault, "NewsServerPropsDlg.Default");
      txtLogin.setEnabled(false);
      tfPassword.setEnabled(false);
      panel2.setLayout(gridBagLayout1);
      panel1.setLayout(borderLayout1);
      panel1.add(panBottom, BorderLayout.SOUTH);
      panel1.add(panel2, BorderLayout.CENTER);
      panel2.add(labServerName, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panel2.add(tfServerName, new GridBagConstraints2(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 2, 5), 100, 0));
      panel2.add(labHostName, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 75, 0));
      panel2.add(tfHostName, new GridBagConstraints2(1, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panel2.add(chkRequireLogon, new GridBagConstraints2(0, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));
      panel2.add(labLogin, new GridBagConstraints2(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panel2.add(txtLogin, new GridBagConstraints2(1, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panel2.add(lblPassword, new GridBagConstraints2(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panel2.add(tfPassword, new GridBagConstraints2(1, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panel2.add(jPanel1, new GridBagConstraints2(0, 6, 3, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      panel2.add(labPort, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panel2.add(tfPort, new GridBagConstraints2(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 5, 5), 15, 0));
         panel2.add(cmdDefault, new GridBagConstraints2(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 5, 5), 0, 0));
      this.getRootPane().setDefaultButton(cmdOK);      // RootPane
      panBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
      panBottom.add(cmdOK, null);
      panBottom.add(cmdCancel, null);

  }

  void cmdOK_actionPerformed(ActionEvent e) {
   applyProps();
    setVisible(false);
  }

  void cmdCancel_actionPerformed(ActionEvent e) {
   setVisible(false);
  }

  void chkRequireLogon_actionPerformed(ActionEvent e) {
      txtLogin.setEnabled(getRequiresLogin());
      tfPassword.setEnabled(getRequiresLogin());
     txtLogin.repaint();
     tfPassword.repaint();
  }

  /**
   * Reads from props
   */
  public void revertToProps() {
   // Read in the server's details.
     NNTPServer thisserver = GlobalState.getStorageManager().getServer(getServerHost());
    //String host = getServerHost();
    setServerName(thisserver.getNiceName());
      setPort(StringUtils.intToString(thisserver.getPort()));
    setRequiresLogin(thisserver.isSecureServer());
    setUserName(thisserver.getLogin());
    setPassword(thisserver.getPassword());
    txtLogin.setEnabled(getRequiresLogin());
    tfPassword.setEnabled(getRequiresLogin());
  }

  /**
   * Applies props
   */
  public void applyProps() {
   String host = getServerHost();
     try {
        NNTPServer thisserver = GlobalState.getStorageManager().getServer(host);
        if (thisserver == null) {
           GlobalState.getStorageManager().addServer(host);
           thisserver = GlobalState.getStorageManager().getServer(host);
        }
         thisserver.setNiceName(getServerName());
        int thePort = StringUtils.stringToInt(getPort());
        // Use default port if none entered
        if (thePort <= 0)
         thisserver.setPort(119);
        else
         thisserver.setPort(thePort);
        thisserver.setSecureServer(getRequiresLogin());
        if (getRequiresLogin())
            thisserver.setLoginInfo(getUserName(), getPassword());
    } catch (IOException e) {
        ErrorReporter.debug("IOException in NewsServerPropsDlg.applyProps:"+e);
        ErrorReporter.error("UnableToConnect", new String[] {host});
    } catch (NNTPServerException ex) {
        ErrorReporter.debug("NNTPException in NewsServerPropsDlg.applyProps:"+ex);
        GlobalState.getStorageManager().nntpException(ex,
           GlobalState.getResString("Action.Connecting"), host);
    }
  }

  /**
   * Sets if this host is uneditable.
   */
  public void setHostEnabled(boolean b) {
   tfHostName.setEnabled(b);
  }

  /**
   * Set this Dialog's server name field.
   */
  public void setServerName(String s) {
   tfServerName.setText(s);
  }

  /**
   * Set this Dialog's setver host field.
   */
  public void setServerHost(String s) {
   tfHostName.setText(s);
  }

  /**
   * Get this dialog's server name field.
   */
  public String getServerName() {
      return tfServerName.getText();
  }

  /**
   * Get this dialog's server host field.
   */
  public String getServerHost() {
   return tfHostName.getText();
  }

  /**
   * Determine whether the "my server requires login" checkbox is ticked
   */
  public boolean getRequiresLogin() {
   return chkRequireLogon.isSelected();
  }

  public void setRequiresLogin(boolean b) {
   chkRequireLogon.setSelected(b);
  }

  /**
   * Gets this dialog's username
   */
  public String getUserName() {
   return txtLogin.getText();
  }

  public void setUserName(String s) {
   txtLogin.setText(s);
  }

  /**
   * Gets this dialog's password
   */
  public String getPassword() {
   return tfPassword.getText();
  }

  public void setPassword(String s) {
   tfPassword.setText(s);
  }

  public String getPort() {
   return tfPort.getText();
  }

  public void setPort(String s) {
   tfPort.setText(s);
  }

  void cmdDefault_actionPerformed(ActionEvent e) {
   setPort("119");
  }

}

class NewsServerPropsDlg_cmdOK_actionAdapter implements java.awt.event.ActionListener{
  NewsServerPropsDlg adaptee;

  NewsServerPropsDlg_cmdOK_actionAdapter(NewsServerPropsDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdOK_actionPerformed(e);
  }
}

class NewsServerPropsDlg_chkRequireLogon_actionAdapter implements java.awt.event.ActionListener{
  NewsServerPropsDlg adaptee;

  NewsServerPropsDlg_chkRequireLogon_actionAdapter(NewsServerPropsDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.chkRequireLogon_actionPerformed(e);
  }
}

class NewsServerPropsDlg_cmdDefault_actionAdapter implements java.awt.event.ActionListener {
  NewsServerPropsDlg adaptee;

  NewsServerPropsDlg_cmdDefault_actionAdapter(NewsServerPropsDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdDefault_actionPerformed(e);
  }
}

//cmdCancel_actionPerformed
class NewsServerPropsDlg_cmdCancel_actionAdapter implements java.awt.event.ActionListener {
  NewsServerPropsDlg adaptee;

  NewsServerPropsDlg_cmdCancel_actionAdapter(NewsServerPropsDlg adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdCancel_actionPerformed(e);
  }
}