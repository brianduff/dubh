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
import java.util.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.event.*;
import dubh.apps.newsagent.dialog.NFrame;
import dubh.apps.newsagent.GlobalState;


/**
 * The Options Dialog Box.<BR>
 * Version History: <UL>
 * <LI>0.1 [27/02/98]: Initial Revision
 * <LI>0.2 [03/03/98]: Added preference saving to file using GlobalState.
 * <LI>0.3 [07/03/98]: Changed to use GlobalState.getResString(). Internationalised.
 * <LI>0.4 [08/03/98]: Added Servers tab. Added private ok and cancel methods.
 * <LI>0.5 [24/03/98]: Added support for displaying dialog opened at a particular
 *      tab.
 * <LI>0.6 [04/04/98]: Added IdentityOptionsPanel.
 * <LI>0.7 [14/04/98]: Added SendAgentsOptionsPanel.
 * <LI>0.8 [28/04/98]: Added ListAgentsOptionsPanel.
 * <LI>0.9 [08/05/98]: Made OK button the default
 *</UL>
 @author Brian Duff
 @version 0.9 [08/05/98]
 */
public class OptionsFrame extends NFrame {

  /** The General Tab */
  public static final int TAB_GENERAL=0;
  /** The Send Tab */
  public static final int TAB_SEND=1;
  /** The Servers Tab */
  public static final int TAB_SERVERS=2;
  /** The Identity tab */
  public static final int TAB_IDENTITY=3;
  /** The Send Agents tab */
  public static final int TAB_SEND_AGENTS=4;
  /** The List Agents tab */
  public static final int TAB_LIST_AGENTS=5;

//  XYLayout xYLayout1 = new XYLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JTabbedPane tabPane = new JTabbedPane();
  GeneralOptionsPanel general = new GeneralOptionsPanel();
  SendOptionsPanel send = new SendOptionsPanel();
  ServerOptionsPane servers = new ServerOptionsPane(this);
  IdentityOptionsPanel identity = new IdentityOptionsPanel();
  SendAgentsOptionsPanel sendagents = new SendAgentsOptionsPanel();
  ListAgentsOptionsPanel listagents = new ListAgentsOptionsPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cmdOK = new JButton();
  JButton cmdCancel = new JButton();
  public OptionsFrame() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Constructs an Options frame which is open at the specified tab (see the
   * TAB constants).
   */
  public OptionsFrame(int tab) {
     this();
     tabPane.setSelectedIndex(tab);
  }

  private void jbInit() throws Exception{
  	Container content = getContentPane();
    //Container content = this;
    this.setTitle(GlobalState.appName+" "+GlobalState.getResString("OptionsFrame.Preferences"));
    this.addWindowListener(new OptionsFrame_this_windowAdapter(this));
    jPanel1.setPreferredSize(new Dimension(0, 30));
    jPanel1.setLayout(flowLayout1);
    tabPane.setPreferredSize(new Dimension(450, 300));
    flowLayout1.setAlignment(2);
    cmdOK.setText(GlobalState.getResString("GeneralOK"));
    cmdOK.addActionListener(new OptionsFrame_cmdOK_actionAdapter(this));
    cmdCancel.setText(GlobalState.getResString("GeneralCancel"));
    cmdCancel.addActionListener(new OptionsFrame_cmdCancel_actionAdapter(this));

    //this.setLayout(borderLayout1);
    content.setLayout(borderLayout1);

    tabPane.addTab(GlobalState.getResString("OptionsFrame.GeneralTab"), general);
    tabPane.addTab(GlobalState.getResString("OptionsFrame.SendTab"), send);
    tabPane.addTab(GlobalState.getResString("OptionsFrame.ServersTab"), servers);
    tabPane.addTab(GlobalState.getResString("OptionsFrame.IdentityTab"), identity);
    tabPane.addTab(GlobalState.getResString("OptionsFrame.SendAgentsTab"), sendagents);
    tabPane.addTab(GlobalState.getResString("OptionsFrame.ListAgentsTab"), listagents);
    //this.add(jPanel1, BorderLayout.SOUTH);
    content.add(jPanel1, BorderLayout.SOUTH);

    this.getRootPane().setDefaultButton(cmdOK);
    jPanel1.add(cmdOK, null);
    jPanel1.add(cmdCancel, null);

   //this.add(tabPane, BorderLayout.CENTER);
 		content.add(tabPane, BorderLayout.CENTER);

  }

  private void ok() {
    // Apply preference changes to all panels, then save the prefs file.
    general.applyPreferences();
    send.applyPreferences();
    servers.applyPreferences();
    identity.applyPreferences();
    sendagents.applyPreferences();
    listagents.applyPreferences();
    if (GlobalState.savePreferences()) hideAndStore("preferences");
  }

  private void cancel() {
    // Revert preferences in all panels
    general.revertPreferences();
    send.revertPreferences();
    servers.revertPreferences();
    identity.revertPreferences();
    sendagents.revertPreferences();
    listagents.revertPreferences();
    hideAndStore("preferences");
  }

  void cmdOK_actionPerformed(ActionEvent e) {
  	ok();
  }

  void cmdCancel_actionPerformed(ActionEvent e) {
  	cancel();

  }

  void this_windowClosing(WindowEvent e) {
    // Behaves like a click on cancel.
    cancel();
  }

}

class OptionsFrame_cmdOK_actionAdapter implements java.awt.event.ActionListener {
  OptionsFrame adaptee;

  OptionsFrame_cmdOK_actionAdapter(OptionsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdOK_actionPerformed(e);
  }
}

class OptionsFrame_cmdCancel_actionAdapter implements java.awt.event.ActionListener {
  OptionsFrame adaptee;

  OptionsFrame_cmdCancel_actionAdapter(OptionsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdCancel_actionPerformed(e);
  }
}

class OptionsFrame_this_windowAdapter extends java.awt.event.WindowAdapter {
  OptionsFrame adaptee;

  OptionsFrame_this_windowAdapter(OptionsFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void windowClosing(WindowEvent e) {
    adaptee.this_windowClosing(e);
  }
}

