// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: SendOptionsPanel.java,v 1.7 1999-11-09 22:34:41 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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
package org.javalobby.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.border.*;
import javax.swing.*;

import org.javalobby.apps.newsagent.PreferenceKeys;
import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.apps.newsagent.dialog.ErrorReporter;

import org.javalobby.dju.ui.*;
import org.javalobby.dju.misc.StringUtils;
import org.javalobby.dju.misc.ResourceManager;
import org.javalobby.dju.misc.UserPreferences;
import org.javalobby.dju.ui.preferences.PreferencePage;


/**
 * Send Options Panel for the Send Tab in the Options dialog box <P>
 * @author Brian Duff
 * @version $Id: SendOptionsPanel.java,v 1.7 1999-11-09 22:34:41 briand Exp $
 */
public class SendOptionsPanel extends PreferencePage 
{

   private final static String RES = "org.javalobby.apps.newsagent.dialog.preferences.res.SendOptions";
   public static final String ID = ResourceManager.getManagerFor(RES).getString("SendOptions.title");

   private JPanel panMain = new JPanel();
   
   private IconicPreferencePanel ippQuoting = new IconicPreferencePanel();
   private JLabel labWhenReply = new JLabel();
   private ButtonGroup groupWhenReply = new ButtonGroup();
   private JRadioButton optNothing = new JRadioButton();
   private JRadioButton optSelection = new JRadioButton();
   private JRadioButton optAll = new JRadioButton();
   private JLabel labPrefix = new JLabel();
   private FixedTextArea taPrefix = new FixedTextArea();
   private JPanel panPrefixPanel = new JPanel();
   private VerticalFlowPanel panRight = new VerticalFlowPanel();
   private JScrollPane scrollPrefix = new JScrollPane(taPrefix);
   private JButton cmdInsert = new JButton();
   private JLabel labPrefixLine = new JLabel();
   private JTextField tfPrefixLine = new JTextField();
   private JPopupMenu popupInsert;
   
   private IconicPreferencePanel ippFormatting = new IconicPreferencePanel();
   private JCheckBox cbUseHardBreaks = new JCheckBox();
   private JCheckBox cbAddAdverts = new JCheckBox();

   public SendOptionsPanel() 
   {
      super(ResourceManager.getManagerFor(RES), "SendOptions");
      init();
      setContent(panMain);
      ResourceManager.getManagerFor(RES).initComponents(panMain);
   }

   private void init()
   {
      initQuoting();
      initFormatting();
   
      panMain.setLayout(new BoxLayout(panMain, BoxLayout.Y_AXIS));
      panMain.setName("MainPanel");
      panMain.add(ippQuoting);
      panMain.add(ippFormatting);
      panMain.add(Box.createGlue());   
   }
   
   private void initQuoting()
   {
      ippQuoting.setName("Quoting");
      VerticalFlowPanel group = ippQuoting.getContainer();
      
      labWhenReply.setName("WhenReply");
      group.addRow(labWhenReply);

      groupWhenReply.add(optNothing);
      optNothing.setName("OptNothing");
      group.addIndentRow(optNothing);
      groupWhenReply.add(optSelection);
      optSelection.setName("OptSelection");
      group.addIndentRow(optSelection);
      groupWhenReply.add(optAll);
      optAll.setName("OptAll");
      group.addIndentRow(optAll);
      
      labPrefix.setName("Prefix");
      group.addRow(labPrefix);

      cmdInsert.setName("Insert");
      cmdInsert.addActionListener(new InsertListener());
      panPrefixPanel.setName("PrefixPanel");
      panRight.setName("Right");
      panPrefixPanel.setLayout(new BorderLayout());
      panPrefixPanel.add(scrollPrefix, BorderLayout.CENTER);
      panPrefixPanel.add(panRight, BorderLayout.EAST);
      panRight.addRow(cmdInsert);
      panRight.addSpacerRow(new JPanel());
      group.addSpacerRow(panPrefixPanel);
      
      //cmdInsert.setIcon(GlobalState.getRes().getImage("glyphPopup.gif"));
      //cmdInsert.setHorizontalTextPosition(AbstractButton.LEFT);
     
      labPrefixLine.setName("PrefixLine");
      group.addRow(labPrefixLine);
      
      tfPrefixLine.setName("PrefixLineField");
      group.addRow(tfPrefixLine);

      JMenuBarResource rsc = new JMenuBarResource("Menus", "mbSendOptions", this);
      popupInsert = rsc.getPopupMenu("popupSendIns");

   }

   private void initFormatting()
   {
      ippFormatting.setName("Formatting");
      VerticalFlowPanel group = ippFormatting.getContainer();
      
      cbUseHardBreaks.setName("UseHardBreaks");
      group.addRow(cbUseHardBreaks);
      
      cbAddAdverts.setName("AddAdvert");
      group.addRow(cbAddAdverts);
      
   }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void save(UserPreferences p) {
     p.setPreference(PreferenceKeys.SEND_INCLUDEHEADING, taPrefix.getText());
     p.setPreference(PreferenceKeys.SEND_INCLUDEPREFIX, tfPrefixLine.getText());
     if (optNothing.isSelected())
        p.setPreference(PreferenceKeys.SEND_INCLUDEBEHAVIOUR, "none");
     else if (optSelection.isSelected())
        p.setPreference(PreferenceKeys.SEND_INCLUDEBEHAVIOUR, "selected");
     else if (optAll.isSelected())
        p.setPreference(PreferenceKeys.SEND_INCLUDEBEHAVIOUR, "all");
        
     p.setBoolPreference(PreferenceKeys.SEND_HARDBREAKS, cbUseHardBreaks.isSelected());
     p.setBoolPreference(PreferenceKeys.SEND_ADDNEWSAGENTHEADERS, cbAddAdverts.isSelected());
  }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revert(UserPreferences s) {
     optAll.setSelected(false);
     optSelection.setSelected(false);
     optNothing.setSelected(false);

     String includeBehaviour = s.getPreference(PreferenceKeys.SEND_INCLUDEBEHAVIOUR, "all");
     if (includeBehaviour.trim().equalsIgnoreCase("all"))
        optAll.setSelected(true);
     else if (includeBehaviour.trim().equalsIgnoreCase("selected"))
        optSelection.setSelected(true);
     else if (includeBehaviour.trim().equalsIgnoreCase("none"))
        optNothing.setSelected(true);

     taPrefix.setText(s.getPreference(PreferenceKeys.SEND_INCLUDEHEADING,
        "In message {message-id}, {x-na-realname} wrote:"));
     tfPrefixLine.setText(s.getPreference(PreferenceKeys.SEND_INCLUDEPREFIX,
        "> "));
        
     cbUseHardBreaks.setSelected(s.getBoolPreference(PreferenceKeys.SEND_HARDBREAKS, true));
     cbAddAdverts.setSelected(s.getBoolPreference(PreferenceKeys.SEND_ADDNEWSAGENTHEADERS, true));   
  }

  /*
   * Listener for the insert button. Pops up a menu containing common
   * header fields from the original message.
   */
  class InsertListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        popupInsert.show(cmdInsert, cmdInsert.getSize().width-popupInsert.getSize().width, cmdInsert.getSize().height);
     }
  }

  public void insertFrom() {
     taPrefix.append("{from}");
  }

  public void insertDate() {
     taPrefix.append("{date}");
  }


  public void insertSubject() {
     taPrefix.append("{subject}");
  }

  public void insertRealname() {
     taPrefix.append("{x-na-realname}");
  }

  public void insertOther() {
     String newField;
     boolean reallyInsert = true;

     newField = ErrorReporter.getInput("SendOptionsPanel.Quoting.InsertPrompt");
     if (StringUtils.getWordCount(newField) > 1) {
        reallyInsert = ErrorReporter.yesNo(
           "SendOptionsPanel.Quoting.BadHeader",
           new String[] { newField }
        );
     }
     if (reallyInsert) taPrefix.append("{"+newField+"}");
  }



}

//
// Old History:
//
// Version History: <UL>
// <LI>0.1 [26/02/98]: Initial Revision
// <LI>0.2 [27/02/98]: Changed layout managers. Changed to a JPanel to work in
//          TabbedPane.
// <LI>0.3 [03/03/98]: Implemented preferences methods.
// <LI>0.4 [07/03/98]: Added event binding for Signatures, added
//          Internationalisation (using getRes().getString())
// <LI>1.0 [10/06/98]: Big change for NewsAgent 1.02
// <LI>
//</UL>
//
// New History:
//
// $Log: not supported by cvs2svn $
// Revision 1.6  1999/06/01 00:34:55  briand
// Total rewrite. Should now look a lot nicer, and follow a standard format for preference pages.
//