// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: FolderSelectorDialog.java,v 1.6 2001-02-11 02:50:59 briand Exp $
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

package org.dubh.apps.newsagent.dialog;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import org.dubh.apps.newsagent.Folder;
import org.dubh.apps.newsagent.GlobalState;
import java.util.*;

import org.dubh.dju.ui.DubhDialog;

/**
 * A Dialog which allows the user to select a folder.<P>
 * @author Brian Duff
 * @version $Id: FolderSelectorDialog.java,v 1.6 2001-02-11 02:50:59 briand Exp $
 */
public class FolderSelectorDialog extends DubhDialog {
  JPanel panMain = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel labQuestion = new JLabel();
  JPanel panButtons = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JButton cmdOK = new JButton();
  JButton cmdCancel = new JButton();
  JButton cmdNew = new JButton();
  JList listFolders = new JList();
  JScrollPane scrollFolderList = new JScrollPane(listFolders);
  DefaultListModel lmFolders = new DefaultListModel();
  private boolean cancelClicked = false;


  public FolderSelectorDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      getContentPane().add(panMain);
      pack();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public FolderSelectorDialog(Frame frame) {
    this(frame, "", false);
  }

  public FolderSelectorDialog(Frame frame, boolean modal) {
    this(frame, "", modal);
  }

  public FolderSelectorDialog(Frame frame, String title) {
    this(frame, title, false);
  }

  private void jbInit() throws Exception{
    labQuestion.setText(GlobalState.getRes().getString("FolderSelectorDialog.SelectAFolder"));
    cmdOK.setText(GlobalState.getRes().getString("GeneralOK"));
    cmdOK.addActionListener(new FolderSelectorDialog_cmdOK_actionAdapter(this));
    cmdCancel.setText(GlobalState.getRes().getString("GeneralCancel"));
    cmdCancel.addActionListener(new FolderSelectorDialog_cmdCancel_actionAdapter(this));
    cmdNew.setText(GlobalState.getRes().getString("FolderSelectorDialog.New"));
    cmdNew.setActionCommand("newfolder");
    cmdNew.addActionListener(new FolderSelectorDialog_cmdNew_actionAdapter(this));    // Handled by the NewFolder Action.
    cmdNew.addActionListener(GlobalState.getMainFrame().getAction("newfolder"));
    panButtons.setLayout(gridBagLayout1);
    panMain.setLayout(borderLayout1);
    panMain.add(labQuestion, BorderLayout.NORTH);
    panMain.add(panButtons, BorderLayout.EAST);
    panButtons.add(cmdOK, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    panButtons.add(cmdCancel, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    panButtons.add(cmdNew, new GridBagConstraints2(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    panMain.add(scrollFolderList, BorderLayout.CENTER);

    fillList();
    listFolders.setModel(lmFolders);
    listFolders.setCellRenderer(new FolderListCellRenderer());
  }

  void cmdOK_actionPerformed(ActionEvent e) {
     cancelClicked = false;
     setVisible(false);
     //dispose();
  }

  void cmdCancel_actionPerformed(ActionEvent e) {
     cancelClicked = true;
     setVisible(false);
     //dispose();
  }


  /**
   * Should be called after the dialog has closed, will return the selected
   * folder or null if the user cancelled or clicked ok when there was
   * no selection.
   */
  public Folder getChosenFolder() {
   if (cancelClicked) return null;
   int selection = listFolders.getSelectedIndex();
   if (selection >= 0)
        return (Folder) lmFolders.getElementAt(selection);
   else
        return null;

  }

  void cmdNew_actionPerformed(ActionEvent e) {
   // ErrorReporter.debug("Got new action in dialog.");
   fillList();
  }

  private void fillList() {
    lmFolders.removeAllElements();
    Enumeration en = GlobalState.getStorageManager().getFolders();
    while (en.hasMoreElements())
     lmFolders.addElement(en.nextElement());
  }


}

class FolderSelectorDialog_cmdOK_actionAdapter implements java.awt.event.ActionListener {
  FolderSelectorDialog adaptee;

  FolderSelectorDialog_cmdOK_actionAdapter(FolderSelectorDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdOK_actionPerformed(e);
  }
}

class FolderSelectorDialog_cmdCancel_actionAdapter implements java.awt.event.ActionListener {
  FolderSelectorDialog adaptee;

  FolderSelectorDialog_cmdCancel_actionAdapter(FolderSelectorDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdCancel_actionPerformed(e);
  }
}

class FolderSelectorDialog_cmdNew_actionAdapter implements java.awt.event.ActionListener {
  FolderSelectorDialog adaptee;

  FolderSelectorDialog_cmdNew_actionAdapter(FolderSelectorDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdNew_actionPerformed(e);
  }
}


// Display an icon and a string for each object in the list.
class FolderListCellRenderer extends JLabel implements ListCellRenderer {
   final static ImageIcon folderIcon = GlobalState.getRes().getImage("FolderSelector.folder");

   public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
   {
      setText(value.toString());
    setOpaque(true);
    if (isSelected) {
       this.setBackground(UIManager.getColor("textHighlight"));
       this.setForeground(UIManager.getColor("textHighlightText"));
    } else {
       this.setBackground(UIManager.getColor("white"));
       this.setForeground(UIManager.getColor("textText"));
    }
      setIcon(folderIcon);
      return this;
   }
}

//
// Old History
// <LI>0.1 [02/04/98]: Initial Revision
// <LI>0.2 [04/04/98]: Converted to use res strings.
//
// New History
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:23:40  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog.
//
//