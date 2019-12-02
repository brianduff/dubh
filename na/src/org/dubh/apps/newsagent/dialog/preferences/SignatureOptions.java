// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: SignatureOptions.java,v 1.6 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.dju.misc.StringUtils;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import java.util.*;
import org.dubh.dju.misc.Debug;

/**
 * Edits the user's signatures
 @author Brian Duff
 @version $Id: SignatureOptions.java,v 1.6 2001-02-11 02:51:00 briand Exp $
 */
public class SignatureOptions extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  FlowLayout bottomButtonsLayout = new FlowLayout(FlowLayout.RIGHT);
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  DefaultListModel sigEntries = new DefaultListModel();
  JDefaultableList  sigList = new JDefaultableList(sigEntries);
  JScrollPane scrollList = new JScrollPane(sigList);
  FlowLayout sigLayout = new FlowLayout(FlowLayout.LEFT);
  JButton cmdAdd = new JButton();
  JButton cmdRemove = new JButton();
  JButton cmdDefault = new JButton();
  JPanel panButtons = new JPanel();
  //VerticalFlowLayout panButtonsLayout = new VerticalFlowLayout();
  public TitledBorder borderSig = new TitledBorder(new EtchedBorder(),
    GlobalState.getRes().getString("SignatureOptions.Caption"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();


  public SignatureOptions() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    this.setSize(new Dimension(425, 300));
    this.setTitle(GlobalState.getRes().getString("SignatureOptions.Caption"));
    jButton1.setText(GlobalState.getRes().getString("GeneralOK"));
    jButton1.addActionListener(new SignatureOptions_jButton1_actionAdapter(this));
    jButton2.setText(GlobalState.getRes().getString("GeneralCancel"));
    jButton2.addActionListener(new SignatureOptions_jButton2_actionAdapter(this));
    scrollList.setPreferredSize(new Dimension(200, 100));
    sigList.addListSelectionListener(new SignatureOptions_sigList_listSelectionAdapter(this));
    sigList.addMouseListener(new SignatureOptions_sigList_mouseAdapter(this));
    sigList.setCellRenderer(new SigOptCellRenderer());
    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setLayout(bottomButtonsLayout);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jButton2, null);
    jPanel1.add(jButton1, null);
    jPanel2.setLayout(gridBagLayout1);
    this.getContentPane().add(jPanel2, BorderLayout.CENTER);
    jPanel3.setBorder(borderSig);
    jPanel3.setLayout(gridBagLayout2);
    panButtons.setLayout(gridBagLayout3);
    panButtons.add(cmdAdd, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
    cmdAdd.setText(GlobalState.getRes().getString("SignatureOptions.Add"));
    cmdAdd.setToolTipText(GlobalState.getRes().getString("SignatureOptions.AddTip"));
    cmdAdd.addActionListener(new SignatureOptions_cmdAdd_actionAdapter(this));
    cmdAdd.setMnemonic(GlobalState.getRes().getString("SignatureOptions.AddTip").charAt(0));
    panButtons.add(cmdRemove, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));
    cmdRemove.setText(GlobalState.getRes().getString("SignatureOptions.Remove"));
    cmdRemove.setToolTipText(GlobalState.getRes().getString("SignatureOptions.RemoveTip"));
    cmdRemove.addActionListener(new SignatureOptions_cmdRemove_actionAdapter(this));
    cmdRemove.setMnemonic(GlobalState.getRes().getString("SignatureOptions.RemoveAccelerator").charAt(0));
    panButtons.add(cmdDefault, new GridBagConstraints2(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
    cmdDefault.setText(GlobalState.getRes().getString("SignatureOptions.Default"));
    cmdDefault.setToolTipText(GlobalState.getRes().getString("SignatureOptions.DefaultTip"));
    cmdDefault.addActionListener(new SignatureOptions_cmdDefault_actionAdapter(this));
    cmdDefault.setMnemonic(GlobalState.getRes().getString("SignatureOptions.DefaultAccelerator").charAt(0));
    jPanel3.add(scrollList, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel3.add(panButtons, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(jPanel3, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    sigList.setToolTipText(GlobalState.getRes().getString("SignatureOptions.ListTip"));
    //jPanel2.add(jPanel4, null);
    cmdRemove.setEnabled(false);
    cmdDefault.setEnabled(false);
    populateList();
  }

  void sigList_valueChanged(ListSelectionEvent e) {
    if (sigList.getSelectedIndex() > -1) {
        cmdDefault.setEnabled(true);
      cmdRemove.setEnabled(true);
    } else {
        cmdDefault.setEnabled(false);
      cmdRemove.setEnabled(false);
    }
  }

  void cmdAdd_actionPerformed(ActionEvent e) {
    boolean success = false;
        SignatureAdd a = new SignatureAdd(this, "Add New Signature", true);
    a.pack();
    // Keep displaying the dialog until the title has been checked for sanity
    while (!success) {
        a.show();
        Enumeration enu = sigEntries.elements();
        boolean already = false;
        String newTitle = a.getTitle();
      // Check if the title already exists
        while(enu.hasMoreElements() && !already) {
            if (((SignatureEntry)enu.nextElement()).title.equals(newTitle))
            already = true;
        }

      // Sanity checking
      if (newTitle.trim().length() == 0) {
        // The title is zero length
        ErrorReporter.error("SignatureOptions.ZeroLength");
      } else if (StringUtils.getWordCount(newTitle.trim()) > 1) {
        // The title has more than one word.
        ErrorReporter.error("SignatureOptions.TooManyWords");
      } else {
            if (!already) {
            // We finally have a sane title.
            success = true;
                sigEntries.addElement(new SignatureEntry(a.getTitle(), a.getSig()));
            } else {
            // The title is already in use
            ErrorReporter.error("SignatureOptions.Exists",
                new String[] { a.getTitle() } );
        } // if
      } // if
    } // while
  } // method

  void cmdRemove_actionPerformed(ActionEvent e) {
    sigEntries.remove(sigList.getSelectedIndex());
  }

  void cmdDefault_actionPerformed(ActionEvent e) {
    int selElement = sigList.getSelectedIndex();
    int oldDefault = sigList.getDefault();
    if (selElement >= 0) {
    if (oldDefault != -1)
        ((SignatureEntry)sigEntries.getElementAt(oldDefault)).isDefault = false;
    sigList.setDefault(selElement);
    ((SignatureEntry)sigEntries.getElementAt(selElement)).isDefault = true;
    sigList.repaint();
    }
  }

  /**
   * Populate the list from the users preferences. Uses the preference
   * newsagent.signature.default to determine the default sig.
   */
  private void populateList() {
 /*   Properties p = GlobalState.getSignatures();
    String defSig = GlobalState.getPreference("newsagent.signature.default", "NONE");
    sigEntries.clear();
    // Enter all the signatures
    Enumeration en = p.propertyNames();
    while (en.hasMoreElements()) {
        String me = (String)(en.nextElement());
      SignatureEntry newentry = new SignatureEntry(me, p.getProperty(me));
      if (me.equals(defSig))    // This is the default signature
        newentry.isDefault = true;
      sigEntries.addElement(newentry);
      if (me.equals(defSig))  // And set the list default too.
        sigList.setDefault(sigEntries.indexOf(newentry));
    } */

  }

  /**
   * Update the users preferences with the contents of the list, and save them
   * to disk.
   */
  private void updatePrefs() {
    Properties p = new Properties();
    // enter all items into the Properties object
    Enumeration en = sigEntries.elements();
    while(en.hasMoreElements()) {
        SignatureEntry entry = (SignatureEntry)(en.nextElement());
      p.put(entry.title, entry.sig);
    }
    // Set the default item property
    int def = sigList.getDefault();
    if (def >= 0) {
        String itemName = ((SignatureEntry)(sigEntries.getElementAt(def))).title;
        GlobalState.getPreferences().setPreference("newsagent.signature.default", itemName);
        try
        {
           GlobalState.getPreferences().save();
        }
        catch (java.io.IOException ioe)
        {
           if (Debug.TRACE_LEVEL_1)
           {
              Debug.println(1, this, "Failed to save preferences:"+ioe);
           }
        }
    }
//    GlobalState.setSignatures(p);
//    GlobalState.saveSignatures();
  }

  void jButton1_actionPerformed(ActionEvent e) {
    // OK Clicked
    updatePrefs();
    setVisible(false);
  }

  void jButton2_actionPerformed(ActionEvent e) {
    setVisible(false);

  }

  void sigList_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
            int index = sigList.locationToIndex(e.getPoint());
            // Double clicked on item index. This allows the user to edit the entry.
      SignatureAdd a = new SignatureAdd(this, "Edit", true);
      a.setTitleEnabled(false);
      String title = ((SignatureEntry)sigEntries.getElementAt(index)).title;
      a.setTitle(title);
      a.setSig(((SignatureEntry)sigEntries.getElementAt(index)).sig);
      a.pack();
      a.show();
      SignatureEntry newversion = new SignatureEntry(title, a.getSig());
      newversion.isDefault = ((SignatureEntry)sigEntries.getElementAt(index)).isDefault;
      sigEntries.setElementAt(newversion, index);
    }
  }

}


class SignatureOptions_sigList_listSelectionAdapter implements javax.swing.event.ListSelectionListener{
  SignatureOptions adaptee;

  SignatureOptions_sigList_listSelectionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.sigList_valueChanged(e);
  }
}

class JDefaultableList extends JList {

    int m_default;

  public JDefaultableList() {
    super();
    m_default = -1;
  }

  public JDefaultableList(ListModel m) {
    super(m);
    m_default = -1;
  }

    public int getDefault() {
    return m_default;
  }

  public void setDefault(int def) {
    m_default = def;
  }

}

class SignatureOptions_cmdAdd_actionAdapter implements java.awt.event.ActionListener{
  SignatureOptions adaptee;

  SignatureOptions_cmdAdd_actionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdAdd_actionPerformed(e);
  }
}

class SignatureOptions_cmdRemove_actionAdapter implements java.awt.event.ActionListener {
  SignatureOptions adaptee;

  SignatureOptions_cmdRemove_actionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdRemove_actionPerformed(e);
  }
}

class SignatureOptions_cmdDefault_actionAdapter implements java.awt.event.ActionListener {
  SignatureOptions adaptee;

  SignatureOptions_cmdDefault_actionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdDefault_actionPerformed(e);
  }
}

class SignatureOptions_jButton1_actionAdapter implements java.awt.event.ActionListener {
  SignatureOptions adaptee;

  SignatureOptions_jButton1_actionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

class SignatureOptions_jButton2_actionAdapter implements java.awt.event.ActionListener {
  SignatureOptions adaptee;

  SignatureOptions_jButton2_actionAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}

class SignatureOptions_sigList_mouseAdapter implements java.awt.event.MouseListener {
    SignatureOptions adaptee;

  SignatureOptions_sigList_mouseAdapter(SignatureOptions adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.sigList_mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }
}


class SignatureEntry {

    public String title;
  public String sig;
  public boolean isDefault;

    public SignatureEntry(String t, String s) {
    title = t;
    sig = s;
    isDefault = false;
  }

}

// Display an icon and a string for each object in the list.
class SigOptCellRenderer extends JLabel implements ListCellRenderer {
    final static ImageIcon defIcon =
            GlobalState.getRes().getImage("SignatureOptions.defaultItem");
    final static ImageIcon nondefIcon =
            GlobalState.getRes().getImage("SignatureOptions.nonDefaultItem");

    public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus)
    {
        String s = ((SignatureEntry)value).title;
        setText(s);
    setOpaque(true);
    if (isSelected) {
       this.setBackground(UIManager.getColor("textHighlight"));
       this.setForeground(UIManager.getColor("textHighlightText"));
    } else {
       this.setBackground(UIManager.getColor("white"));
       this.setForeground(UIManager.getColor("textText"));
    }
        if (((SignatureEntry)value).isDefault) {
        setIcon(defIcon);
    } else {
        setIcon(nondefIcon);
    }
        return this;
    }
}

// Old Version History:
// <LI>0.1 [06/03/98]: Initial Revision
// <LI>0.2 [07/03/98]: Changed to use GlobalState.getImage() for default item
// <LI>0.3 [06/06/98]: Added dubh utils import for StringUtils

// New History:
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:33:51  briand
// (this file was automatically updated, but is now obsolete)
//