// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: ServerSubscriptions.java,v 1.4 1999-06-01 00:23:40 briand Exp $
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
import java.awt.event.*;
import java.util.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.event.*;

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.nntp.NNTPServer;
import dubh.apps.newsagent.nntp.NNTPServerException;
import dubh.apps.newsagent.nntp.Newsgroup;
import dubh.apps.newsagent.dialog.LongOperationDialog;

import dubh.utils.ui.DubhOkCancelDialog;
import dubh.utils.misc.Debug;

/**
 * Displays a dialog allowing the user to select newsgroups to subscribe to.
 * @author Brian Duff
 * @version $Id: ServerSubscriptions.java,v 1.4 1999-06-01 00:23:40 briand Exp $
 */
public class ServerSubscriptions extends DubhOkCancelDialog {
  JPanel panMain = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel panSubscriptions = new JPanel();
  JPanel panButtons = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labServers = new JLabel();
  JLabel labDisplayContaining = new JLabel();
  JTextField txtDisplayContaining = new JTextField();
  JList listGroups = new JList();
  JScrollPane scrollGroups = new JScrollPane(listGroups);
  JPanel panGroupButtons = new JPanel();
  JList listServers = new JList();
  JScrollPane scrollServers = new JScrollPane(listServers);
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JButton cmdSubscribe = new JButton();
  JButton cmdUnsubscribe = new JButton();
  JButton cmdReset = new JButton();
  JRadioButton optShowAll = new JRadioButton();
  JRadioButton optSubscribed = new JRadioButton();
  JRadioButton optNew = new JRadioButton();
  DefaultListModel lmAllGroups = new DefaultListModel();
  DefaultListModel lmSubscribedGroups = new DefaultListModel();
  DefaultListModel lmNewGroups = new DefaultListModel();
  DefaultListModel lmServers  = new DefaultListModel();
  ButtonGroup radioGroup = new ButtonGroup();

  public ServerSubscriptions(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
      init();
      setPanel(panMain);
  }

  public ServerSubscriptions(Frame frame) {
    this(frame, "", false);
  }

  public ServerSubscriptions(Frame frame, boolean modal) {
    this(frame, "", modal);
  }

  public ServerSubscriptions(Frame frame, String title) {
    this(frame, title, false);
  }

  private void init()
  {

   // Initially, while no server is selected, the newsgroup buttons must be
   // disabled.
    dubh.utils.misc.ResourceManager r = GlobalState.getRes();
    cmdSubscribe.setEnabled(false);
    cmdUnsubscribe.setEnabled(false);
    cmdReset.setEnabled(false);
    optShowAll.setEnabled(false);
    optSubscribed.setEnabled(false);
    optNew.setEnabled(false);

    txtDisplayContaining.setEnabled(false);
    listServers.setModel(lmServers);
    listServers.addListSelectionListener(new ServerSubscriptions_listServers_listSelectionAdapter(this));
    panSubscriptions.setLayout(gridBagLayout1);
    panMain.setSize(new Dimension(490, 300));
    flowLayout1.setAlignment(2);
    labServers.setText(GlobalState.getRes().getString("ServerSubscriptions.NewsServers"));
    labDisplayContaining.setText(GlobalState.getRes().getString("ServerSubscriptions.GroupsContaining"));
    listGroups.addListSelectionListener(new ServerSubscriptions_listGroups_listSelectionAdapter(this));
    scrollServers.addMouseListener(new ServerSubscriptions_scrollServers_mouseAdapter(this));
//    r.initButton(cmdSubscribe, "ServerSubscriptions.Subscribe");
    cmdSubscribe.setText("+Subscribe+"); // NLS
    cmdSubscribe.addActionListener(new ServerSubscriptions_cmdSubscribe_actionAdapter(this));
// NLS    r.initButton(cmdUnsubscribe, "ServerSubscriptions.Unsubscribe");
    cmdUnsubscribe.addActionListener(new ServerSubscriptions_cmdUnsubscribe_actionAdapter(this));
// NLS    r.initButton(cmdReset, "ServerSubscriptions.Reset");
    cmdReset.addActionListener(new ServerSubscriptions_cmdReset_actionAdapter(this));
// NLS    r.initButton(optShowAll, "ServerSubscriptions.All");
    optShowAll.setActionCommand("showall");
// NLS    r.initButton(optSubscribed, "ServerSubscriptions.Subscribed");
    optSubscribed.setActionCommand("showsubscribed");
// NLS    r.initButton(optNew, "ServerSubscriptions.New");
    optNew.setActionCommand("shownew");
    optNew.setSelected(true);
    radioGroup.add(optShowAll);
    radioGroup.add(optSubscribed);
    radioGroup.add(optNew);
    ActionListener grouplistener = new ServerSubscriptions_radioGroup_radioAdapter(this);
    optShowAll.addActionListener(grouplistener);
    optSubscribed.addActionListener(grouplistener);
    optNew.addActionListener(grouplistener);
    panGroupButtons.setLayout(gridBagLayout2);
    panButtons.setLayout(flowLayout1);
    panMain.setLayout(borderLayout1);
    panMain.add(panSubscriptions, BorderLayout.CENTER);
    panSubscriptions.add(labServers, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panSubscriptions.add(labDisplayContaining, new GridBagConstraints2(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panSubscriptions.add(txtDisplayContaining, new GridBagConstraints2(1, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panSubscriptions.add(scrollGroups, new GridBagConstraints2(1, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panSubscriptions.add(panGroupButtons, new GridBagConstraints2(2, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panGroupButtons.add(cmdSubscribe, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));
    panGroupButtons.add(cmdUnsubscribe, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5), 0, 0));
    panGroupButtons.add(cmdReset, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 10, 5), 0, 0));
    panGroupButtons.add(optShowAll, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    panGroupButtons.add(optSubscribed, new GridBagConstraints2(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    panGroupButtons.add(optNew, new GridBagConstraints2(0, 5, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    panSubscriptions.add(scrollServers, new GridBagConstraints2(0, 1, 1, 2, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panMain.add(panButtons, BorderLayout.SOUTH);
    populateServersList();
  }

  public boolean okClicked()
  {
     GlobalState.getStorageManager().serializeServers();
     // Update the folder tree on the main frame.
     GlobalState.getMainFrame().getFolderTreePanel().treeUpdate(); 
     return true;    
  }

  /**
   * User chose to subscribe to one or more Newsgroups.
   */
  void cmdSubscribe_actionPerformed(ActionEvent e) {
     // No point in subscribing to an already subscribed group.
     if (!optSubscribed.isSelected()) {
        // Get the selections in the current list.
        Object[] selections = getSelectedNewsgroups();
        NNTPServer server = getSelectedServer();
        if (server == null) {
           if (Debug.TRACE_LEVEL_2)
           {
              Debug.println(2, this, "Can't subscribe: no server selected.");
           }
           return;
        }
        for (int i=0;i<selections.length;i++) {
           server.addSubscription((Newsgroup)selections[i]);
           lmSubscribedGroups.addElement(selections[i]);
        }
     }

  }

  /**
   * User asked to unsubscribe from 1+ news groups.
   */
  void cmdUnsubscribe_actionPerformed(ActionEvent e) {
     Object[] selections = getSelectedNewsgroups();
     NNTPServer server = getSelectedServer();
     if (server == null) {
        if (Debug.TRACE_LEVEL_2)
        {
           Debug.println(2, this, "Can't subscribe: no server selected.");
        }
        return;
     }
     for (int i=0;i<selections.length;i++) {
        server.removeSubscription((Newsgroup)selections[i]);
        lmSubscribedGroups.removeElement(selections[i]);
     }
  }

  void cmdReset_actionPerformed(ActionEvent e) {
     populateNewsgroupsLists();
  }
  
  void scrollServers_mouseClicked(MouseEvent e) {

  }

  void radioGroup_actionPerformed(ActionEvent e) {
     // showall, shownew, showsubscribed
     setCorrectListModel();
  }

// Private methods

  /**
   * Get the current selection in the newsgroups list.
   */
  private Object[] getSelectedNewsgroups() {
     return listGroups.getSelectedValues();
  }

  /**
   * Get the current selection in the servers list.
   */
  private NNTPServer getSelectedServer() {
     int selection = listServers.getSelectedIndex();
     if (selection >= 0)
        return (NNTPServer) lmServers.getElementAt(selection);
     else
        return null;
  }

  /**
   * Get the list of NNTP Servers from the Storage Manager, and fill in the
   * servers list.
   */
  private void populateServersList() {
     lmServers.clear();
     Enumeration e_servers = GlobalState.getStorageManager().getServers();
     while (e_servers.hasMoreElements()) {
        lmServers.addElement(e_servers.nextElement());
     }
  }

  /**
   * Set the correct list model for listGroups, depending on which checkbox
   * is currently selected.
   */
  private void setCorrectListModel() {
     if (optNew.isSelected()) {
        listGroups.setModel(lmNewGroups);
       // listGroups.setModel(lmNewGroups);
     } else if (optShowAll.isSelected()) {
        listGroups.setModel(lmAllGroups);
        //listGroups.setModel(lmAllGroups);
     } else  { // Subscribed must be selected.
        listGroups.setModel(lmSubscribedGroups);
      //  listGroups.setModel(lmSubscribedGroups);
     }
     listGroups.revalidate();
    // listGroups.repaint();
  }

  /**
   * Populate the lists of new, all, and subscribed newsgroups on the server.
   * This is probably a pretty hefty function, performance-wise.
   */
  private void populateNewsgroupsLists() {
     // is a newsserver selected?

     int selection = listServers.getSelectedIndex();
     if (selection >=0 ) {

       final LongOperationDialog longop = new LongOperationDialog(GlobalState.getMainFrame(), true);
       longop.setLeftIcon(GlobalState.getRes().getImage("ServerSubscriptions.longOperation"));
       // Retrieve the news server
       final NNTPServer theServer = (NNTPServer) lmServers.getElementAt(selection);
       longop.setText(GlobalState.getRes().getString("ServerSubscriptions.Connecting",
           new String[] {theServer.getNiceName()}));
       // Check we are connected.
      // theServer.attachStream(new java.io.PrintWriter(System.out));

       if (GlobalState.getStorageManager().connectIfNeeded(theServer)) {

              // Get all groups from the server. This should take no time, after the last comd.
              Thread dosubs = new Thread() {
              public void run() {

               try {
                 longop.setText(GlobalState.getRes().getString("ServerSubscriptions.GettingList",
                       new String[] {theServer.getNiceName()}));
                 Vector newGroups, allGroups, subGroups;
                 newGroups = theServer.getNewNewsgroups();
                 allGroups = theServer.getAllNewsgroups();
                 subGroups = theServer.getSubscriptions();
                 // Populate the three list models.
                 Enumeration enum;
                 lmAllGroups.clear();
                 lmNewGroups.clear();
                 lmSubscribedGroups.clear();
                 enum = newGroups.elements();

                 longop.setText(GlobalState.getRes().getString("ServerSubscriptions.UpdatingList"));
                 while (enum.hasMoreElements()) {
                    Object obj = enum.nextElement();
                    lmNewGroups.addElement(obj);
                 }
                 if (newGroups.size() == allGroups.size()) {
                    lmAllGroups = lmNewGroups;
                 } else {
                    enum = allGroups.elements();
                    while (enum.hasMoreElements()) {
                       lmAllGroups.addElement(enum.nextElement());
                    }
                 }
                 enum = subGroups.elements();
                 if (Debug.ASSERT)
                 {
                    Debug.assert((enum != null), this, "null group list");
                 }
                 while (enum.hasMoreElements()) {
                    lmSubscribedGroups.addElement(enum.nextElement());
                 }
                 setCorrectListModel();
                 longop.setVisible(false);
              } catch (java.io.IOException ioe) {
                 ErrorReporter.error("ServerSubscriptions.IOError", new String[] {theServer.toString()});
                 if (Debug.TRACE_LEVEL_1)
                 {
                    Debug.println(1, this, "populateNewsgroupsList:"+ioe);
                 }
                 longop.setVisible(false);
              } catch (NNTPServerException nntp) {
                 ErrorReporter.error("ServerSubscriptions.NNTPError", new String[] {theServer.toString()});
                 if (Debug.TRACE_LEVEL_1)
                 {
                    Debug.println(1, this, "populateNewsgroupsList:"+nntp);
                 }
                 longop.setVisible(false);
              }
           } // run
        }; // thread
        dosubs.start();
        longop.showAtCentreOfParent();   // will block until dosubs thread stops it.

       } // connection check

     } // selection check

  }

  void listServers_valueChanged(ListSelectionEvent e) {

      if (!e.getValueIsAdjusting()) {
         if (e.getFirstIndex() >= 0) {
           populateNewsgroupsLists();
           // a news server is selected, so enable all the relevant buttons
           cmdSubscribe.setEnabled(true);
           cmdUnsubscribe.setEnabled(true);
           cmdReset.setEnabled(true);
           optShowAll.setEnabled(true);
           optSubscribed.setEnabled(true);
           optNew.setEnabled(true);
         } else {
           // no server is selected, disable buttons.
           cmdSubscribe.setEnabled(false);
           cmdUnsubscribe.setEnabled(false);
           cmdReset.setEnabled(false);
           optShowAll.setEnabled(false);
           optSubscribed.setEnabled(false);
           optNew.setEnabled(false);
         }
     }
  }

  void listGroups_valueChanged(ListSelectionEvent e) {

  }

  

}

class ServerSubscriptions_cmdSubscribe_actionAdapter implements java.awt.event.ActionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_cmdSubscribe_actionAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdSubscribe_actionPerformed(e);
  }
}

class ServerSubscriptions_cmdUnsubscribe_actionAdapter implements java.awt.event.ActionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_cmdUnsubscribe_actionAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdUnsubscribe_actionPerformed(e);
  }
}

class ServerSubscriptions_cmdReset_actionAdapter implements java.awt.event.ActionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_cmdReset_actionAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdReset_actionPerformed(e);
  }
}

class ServerSubscriptions_scrollServers_mouseAdapter extends java.awt.event.MouseAdapter {
  ServerSubscriptions adaptee;

  ServerSubscriptions_scrollServers_mouseAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.scrollServers_mouseClicked(e);
  }
}

class ServerSubscriptions_radioGroup_radioAdapter implements java.awt.event.ActionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_radioGroup_radioAdapter(ServerSubscriptions adaptee) {
     this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
     adaptee.radioGroup_actionPerformed(e);
  }



}

class ServerSubscriptions_listServers_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_listServers_listSelectionAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.listServers_valueChanged(e);
  }
}

class ServerSubscriptions_listGroups_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  ServerSubscriptions adaptee;

  ServerSubscriptions_listGroups_listSelectionAdapter(ServerSubscriptions adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.listGroups_valueChanged(e);
  }
} 

//
// Old History:
// <LI>0.1 [24/03/98]: Initial Revision
// <LI>0.2 [25/03/98]: Added populateServersList()
// <LI>0.3 [29/03/98]: Implemented OK and Cancel buttons.
// <LI>0.4 [30/03/98]: Forced OK to update the FolderTree. Disabled buttons
//   which can't be used until a server is selected.
// <LI>0.5 [08/05/98]: Changed button order. Made OK Default. Changed to sub
//   class NDialog. Internationalised. Added bug fix so scroll bars update
//   properly.
// <LI>0.6 [09/05/98]: Minor bug fix in longop dialogue for UNIX
// 
// New History:
//
// $Log: not supported by cvs2svn $