// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsServerPropsDlg.java,v 1.4 1999-06-01 00:23:40 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
import dubh.utils.misc.Debug;

import dubh.utils.ui.DubhOkCancelDialog;

/**
 * Edits the preferences of a particular news server.<P>
 *
 * @author Brian Duff
 * @version $Id: NewsServerPropsDlg.java,v 1.4 1999-06-01 00:23:40 briand Exp $
 */
public class NewsServerPropsDlg extends DubhOkCancelDialog {
  JPanel panMain = new JPanel();
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

  public NewsServerPropsDlg(JFrame frame, String title, boolean modal) 
  {
     super(frame, title, modal);
     init();
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

  private void init()
  {
      // this.setTitle("Edit News Server Options");
      ResourceManager r = GlobalState.getRes();
      labServerName.setText(GlobalState.getRes().getString("NewsServerPropsDlg.NiceName"));
      tfServerName.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.NiceNameTip"));
      //chkRequireLogon.setText(GlobalState.getRes().getString("NewsServerPropsDlg.RequireLogin"));
      //chkRequireLogon.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.RequireLoginTip"));
      //chkRequireLogon.setMnemonic(GlobalState.getRes().getString("NewsServerPropsDlg.RequireLoginAccelerator").charAt(0));
//NLS      r.initButton(chkRequireLogon, "NewsServerPropsDlg.RequireLogin");
      labHostName.setText(GlobalState.getRes().getString("NewsServerPropsDlg.ServerHostname"));
      tfHostName.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.ServerHostnameTip"));
      chkRequireLogon.addActionListener(new NewsServerPropsDlg_chkRequireLogon_actionAdapter(this));
      labLogin.setText(GlobalState.getRes().getString("NewsServerPropsDlg.LoginName"));
      txtLogin.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.LoginNameTip"));
      lblPassword.setText(GlobalState.getRes().getString("NewsServerPropsDlg.Password"));
      tfPassword.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.PasswordTip"));
      labPort.setText(GlobalState.getRes().getString("NewsServerPropsDlg.ServerPort"));
      tfPort.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.ServerPortTip"));
      tfPort.setText("119");
      //cmdDefault.setText(GlobalState.getRes().getString("NewsServerPropsDlg.Default"));
      //cmdDefault.setToolTipText(GlobalState.getRes().getString("NewsServerPropsDlg.DefaultTip"));
      //cmdDefault.setMnemonic(GlobalState.getRes().getString("NewsServerPropsDlg.DefaultAccelerator").charAt(0));
      cmdDefault.addActionListener(new NewsServerPropsDlg_cmdDefault_actionAdapter(this));
 //NLS     r.initButton(cmdDefault, "NewsServerPropsDlg.Default");
      txtLogin.setEnabled(false);
      tfPassword.setEnabled(false);
      panMain.add(labServerName, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panMain.add(tfServerName, new GridBagConstraints2(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 2, 5), 100, 0));
      panMain.add(labHostName, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 75, 0));
      panMain.add(tfHostName, new GridBagConstraints2(1, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panMain.add(chkRequireLogon, new GridBagConstraints2(0, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));
      panMain.add(labLogin, new GridBagConstraints2(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panMain.add(txtLogin, new GridBagConstraints2(1, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panMain.add(lblPassword, new GridBagConstraints2(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panMain.add(tfPassword, new GridBagConstraints2(1, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
      panMain.add(jPanel1, new GridBagConstraints2(0, 6, 3, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
      panMain.add(labPort, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
      panMain.add(tfPort, new GridBagConstraints2(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 5, 5), 15, 0));
      panMain.add(cmdDefault, new GridBagConstraints2(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 5, 5), 0, 0));
     
     setPanel(panMain);
  
  }

  public boolean okClicked()
  {
     applyProps();
     return true;
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
        if (Debug.TRACE_LEVEL_1)
        {
           Debug.println(1, this, "IOException in NewsServerPropsDlg.applyProps:"+e);
        }
        ErrorReporter.error("UnableToConnect", new String[] {host});
    } catch (NNTPServerException ex) {
       if (Debug.TRACE_LEVEL_1)
       {
          Debug.println(1, this, "NNTPException in NewsServerPropsDlg.applyProps:"+ex);   
       }
       GlobalState.getStorageManager().nntpException(ex,
           GlobalState.getRes().getString("Action.Connecting"), host);
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

//
// Old Version History:
// <LI>0.1 [08/03/98]: Initial Revision
// <LI>0.2 [23/03/98]: Major change to the way NNTP Server preferences are
//      saved. Now using serialisation.
// <LI>0.3 [20/04/98]: Changed to a subclass of NDialog, Internationalised
// <LI>0.4 [21/04/98]: Changed to use nntpException for error handling.
// <LI>0.5 [28/04/98]: Added fix for bug #10: Checking for no port number.
// <LI>0.6 [08/05/98]: Changed button order and aligned right.
// <LI>0.7 [06/06/98]: Added dubh utils import for StringUtils
// <LI>0.8 [05/10/98]: Fixed button initialisation to use new resource strings.
//
// New version history:
// $Log: not supported by cvs2svn $
//