// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: FolderSelectorDialog.java,v 1.4 1999-06-01 00:23:40 briand Exp $
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
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import dubh.apps.newsagent.Folder;
import dubh.apps.newsagent.GlobalState;
import java.util.*;

import dubh.utils.ui.DubhDialog;

/**
 * A Dialog which allows the user to select a folder.<P>
 * @author Brian Duff
 * @version $Id: FolderSelectorDialog.java,v 1.4 1999-06-01 00:23:40 briand Exp $
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
    Enumeration enum = GlobalState.getStorageManager().getFolders();
    while (enum.hasMoreElements())
     lmFolders.addElement(enum.nextElement());
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
//