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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;
import dubh.utils.ui.GridBagConstraints2;
import dubh.utils.misc.StringUtils;
import dubh.apps.newsagent.PreferenceKeys;
import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.nntp.NNTPServer;
import dubh.apps.newsagent.dialog.NewsServerPropsDlg;
import dubh.apps.newsagent.dialog.ErrorReporter;

/**
 * Server Options Panel for the Servers Tab in the Options dialog box <P>
 * The following options are available from this panel:<P>
 * <TABLE><TR><TD><B>Property</B></TD><TD><B>Description</B></TD><TD><B>Default</B></TD></TR>
 * <TR>
 * <TD>newsagent.servers.SMTPHostName</TD>
 * <TD>Outgoing mail server</TD>
 * <TD></TD>
 * </TR>
  * <TR>
 * <TD>newsagent.servers.SMTPPort</TD>
 * <TD>Outgoing mail port</TD>
 * <TD>25</TD>
 * </TR>
 * </TABLE>
 * Version History: <UL>
 * <LI>0.1 [08/03/98]: Initial Revision
 * <LI>0.2 [23/03/98]: Major change to the way in which NNTP Server settings
 *   are stored: now we use serialised NNTPServer objects, and set the properties
 *   directly, using the NNTPServer collection stored by the default storagemanager.
 * <LI>0.3 [29/03/98]: Implemented server properties... command.
 * <LI>0.4 [06/06/98]: Added dubh utils import for StringUtils. Changed
 *   superclass to JPanel (was Panel).
 * <LI>
 *</UL>
 @author Brian Duff
 @version 0.4 [06/06/98]
 */
public class ServerOptionsPane extends JPanel {
  public TitledBorder borderNews = new TitledBorder(new EtchedBorder(),
   "News");
  public TitledBorder borderMail = new TitledBorder(new EtchedBorder(),
   "Mail");
  JPanel panNews = new JPanel();
  JPanel panMail = new JPanel();
  JList lstNewsServers = new JList();
  JScrollPane lstScroll = new JScrollPane(lstNewsServers);
  JPanel panNewsButtons = new JPanel();
  JButton cmdAdd = new JButton();
  JButton cmdRemove = new JButton();
  JButton cmdProperties = new JButton();
  JPanel panMailServer = new JPanel();
  JLabel labMailServer = new JLabel();
  JLabel labMailPort = new JLabel();
  JTextField tfMailServer = new JTextField();
  JTextField tfMailPort = new JTextField();
  JButton cmdPortDefault = new JButton();
  DefaultListModel listItems = new DefaultListModel();
  private static final String defaultPort = "25";
  JFrame parent;

  GridBagLayout snLayout = new GridBagLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  public ServerOptionsPane() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ServerOptionsPane(JFrame parent) {
   this();
    this.parent = parent;
  }

  public void jbInit() throws Exception{
   panNews.setBorder(borderNews);
    panNews.setLayout(gridBagLayout4);
    panMail.setBorder(borderMail);
    panMail.setLayout(gridBagLayout2);
    labMailServer.setText("SMTP Mail Server:");
    labMailPort.setText("SMTP Mail Port:");
    cmdPortDefault.setText("Default");
    cmdPortDefault.addActionListener(new ServerOptionsPane_cmdPortDefault_actionAdapter(this));
    panNewsButtons.setLayout(gridBagLayout3);
    panMailServer.setLayout(snLayout);
    panMailServer.add(labMailServer, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 7), 0, 0));
    panMailServer.add(labMailPort, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 7), 0, 0));
    panMailServer.add(tfMailPort, new GridBagConstraints2(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 50, 0));
    panMailServer.add(cmdPortDefault, new GridBagConstraints2(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

    lstNewsServers.setPreferredSize(new Dimension(200, 100));
    lstNewsServers.setModel(listItems);
    lstNewsServers.addListSelectionListener(new ServerOptionsPane_lstNewsServers_listSelectionAdapter(this));
    lstNewsServers.addMouseListener(new ServerOptionsPane_lstScroll_mouseAdapter(this));
    cmdAdd.setText("Add...");
    cmdAdd.setToolTipText("Adds a new news server");
    cmdAdd.addActionListener(new ServerOptionsPane_cmdAdd_actionAdapter(this));
    cmdRemove.setText("Remove");
    cmdRemove.setToolTipText("Deletes the selected item from the list of news servers");
    cmdRemove.addActionListener(new ServerOptionsPane_cmdRemove_actionAdapter(this));
    cmdProperties.setText("Properties...");
    cmdProperties.setToolTipText("Allows you to edit the properties of the selected server");
    cmdProperties.addActionListener(new ServerOptionsPane_cmdProperties_actionAdapter(this));
    this.setLayout(gridBagLayout1);
    this.add(panNews, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 22, 0));
    panNews.add(lstScroll, new GridBagConstraints2(0, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 100, 0));
    panNews.add(panNewsButtons, new GridBagConstraints2(1, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));
    panNewsButtons.add(cmdAdd, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 38, 0));
    panNewsButtons.add(cmdRemove, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 22, 0));
    panNewsButtons.add(cmdProperties, new GridBagConstraints2(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
    this.add(panMail, new GridBagConstraints2(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 130, 0));
    panMail.add(panMailServer, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 26, 5), 0, 0));
    panMailServer.add(tfMailServer, new GridBagConstraints2(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 155, 0));
    cmdRemove.setEnabled(false);
    cmdProperties.setEnabled(false);
   revertPreferences();
  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void applyPreferences() {
 /*   String allServers = "";
    Enumeration e = listItems.elements();
    while (e.hasMoreElements()) {
      ServerListItem item = (ServerListItem) e.nextElement();
      //allServers = allServers + item.serverHost + " ";

    }
    GlobalState.setPreference("newsagent.servers.InstalledServers", allServers);
*/
    // Save the SMTP Mail Host & Port  (Nb. should do port sanity checking elsewhere)
    GlobalState.setPreference(PreferenceKeys.SERVERS_SMTPHOSTNAME, tfMailServer.getText());
    GlobalState.setPreference(PreferenceKeys.SERVERS_SMTPPORT, tfMailPort.getText());
    // Serialise NNTPServers
    GlobalState.getStorageManager().serializeServers();
    // Update User Interface.
    GlobalState.getMainFrame().getFolderTreePanel().treeUpdate();
  }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revertPreferences() {
      // Get the list of NNTP Servers and populate the list.
      refreshServerList();

    // Get the SMTP Mail Host & Port
    tfMailServer.setText(GlobalState.getPreference(PreferenceKeys.SERVERS_SMTPHOSTNAME, ""));
    tfMailPort.setText(GlobalState.getPreference(PreferenceKeys.SERVERS_SMTPPORT, ""));

  }

  /**
   * Refreshes the list
   */
  private void refreshServerList() {
    listItems.clear();
    Enumeration enum = GlobalState.getStorageManager().getServers();
    while(enum.hasMoreElements()) {
        NNTPServer thisserver = (NNTPServer)enum.nextElement();
         listItems.addElement(new ServerListItem(thisserver.getHostName(),
            thisserver.getNiceName()));
    }

  }

  /**
   * Sets the properties of the selected list item.
   */
  private void setProperties() {
     ServerListItem thisitem =
      (ServerListItem)lstNewsServers.getSelectedValue();
     NewsServerPropsDlg props = new NewsServerPropsDlg(parent, true);
       props.pack();
       props.setServerHost(thisitem.serverHost);
       props.setHostEnabled(false);
       props.revertToProps();
       props.show();
       refreshServerList();        // The user might have changed a server name
       props.dispose();
  }

  void cmdAdd_actionPerformed(ActionEvent e) {
    NewsServerPropsDlg test = new NewsServerPropsDlg(parent, true);
    test.pack();
    test.show();
    //listItems.addElement(new ServerListItem(test.getServerHost(), test.getServerName()));
    refreshServerList();
    test.dispose();
  }

  
  void lstScroll_mouseClicked(MouseEvent e) {

  }
  void cmdRemove_actionPerformed(ActionEvent e) {
     ServerListItem thisitem =
      (ServerListItem)lstNewsServers.getSelectedValue();
     // Check the user really wants to delete the server.
     if (ErrorReporter.yesNo("ReallyDeleteServer", new String[] {thisitem.serverName})) {
        listItems.remove(lstNewsServers.getSelectedIndex());
        try {
           GlobalState.getStorageManager().removeServer(thisitem.serverHost);
        } catch (IOException ex) {
           ErrorReporter.debug("Remove command in ServerOptionsPane: Unable to disconnect.");
        }
     }
  }

  void cmdProperties_actionPerformed(ActionEvent e) {
   setProperties();
  }

  void cmdPortDefault_actionPerformed(ActionEvent e) {
   tfMailPort.setText(defaultPort);
  }

  void lstNewsServers_mouseClicked(MouseEvent e) {
   if (e.getClickCount() == 2) {    // Double click events
      setProperties();
    }
  }

  /**
   * Triggered when the selected list item changes
   */
  void lstNewsServers_valueChanged(ListSelectionEvent e) {
   if (lstNewsServers.getSelectedIndex() >= 0) {
      cmdRemove.setEnabled(true);
      cmdProperties.setEnabled(true);
    } else {
      cmdRemove.setEnabled(false);
      cmdProperties.setEnabled(false);
    }
  }
}

class ServerOptionsPane_cmdAdd_actionAdapter implements java.awt.event.ActionListener {
  ServerOptionsPane adaptee;

  ServerOptionsPane_cmdAdd_actionAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdAdd_actionPerformed(e);
  }
}

class ServerOptionsPane_cmdRemove_actionAdapter implements java.awt.event.ActionListener {
  ServerOptionsPane adaptee;

  ServerOptionsPane_cmdRemove_actionAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdRemove_actionPerformed(e);
  }
}

class ServerOptionsPane_cmdProperties_actionAdapter implements java.awt.event.ActionListener {
  ServerOptionsPane adaptee;

  ServerOptionsPane_cmdProperties_actionAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdProperties_actionPerformed(e);
  }
}

class ServerOptionsPane_cmdPortDefault_actionAdapter implements java.awt.event.ActionListener {
  ServerOptionsPane adaptee;

  ServerOptionsPane_cmdPortDefault_actionAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdPortDefault_actionPerformed(e);
  }
}

class ServerOptionsPane_lstNewsServers_mouseAdapter extends java.awt.event.MouseAdapter {
  ServerOptionsPane adaptee;

  ServerOptionsPane_lstNewsServers_mouseAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.lstNewsServers_mouseClicked(e);
  }
}

class ServerOptionsPane_lstScroll_mouseAdapter extends java.awt.event.MouseAdapter {
  ServerOptionsPane adaptee;

  ServerOptionsPane_lstScroll_mouseAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.lstScroll_mouseClicked(e);
  }
}

class ServerOptionsPane_lstNewsServers_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  ServerOptionsPane adaptee;

  ServerOptionsPane_lstNewsServers_listSelectionAdapter(ServerOptionsPane adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.lstNewsServers_valueChanged(e);
  }
}

/**
 * Simple implementation of a list item.
 */
class ServerListItem {
   String serverHost;
  String serverName;
  ServerListItem(String h,String  n) { serverHost = h; serverName = n; }
  public String toString() {
   return serverName;
  }
}