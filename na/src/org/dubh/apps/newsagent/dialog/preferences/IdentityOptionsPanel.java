// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: IdentityOptionsPanel.java,v 1.5 1999-06-01 00:37:04 briand Exp $
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
package dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;


import dubh.apps.newsagent.GlobalState;
import dubh.utils.ui.preferences.*;
import dubh.utils.misc.*;
import dubh.apps.newsagent.PreferenceKeys;
import dubh.utils.ui.IconicPreferencePanel;
import dubh.utils.ui.JTextFieldFixedHeight;
import dubh.utils.ui.VerticalFlowPanel;
import dubh.utils.ui.FixedTextArea;

import dubh.utils.misc.ResourceManager;
/**
 * Panel for displaying Identity Options in preferences.
 * @author Brian Duff
 * @version $Id: IdentityOptionsPanel.java,v 1.5 1999-06-01 00:37:04 briand Exp $
 */
public class IdentityOptionsPanel extends PreferencePage 
{
   private final static String RES = "dubh.apps.newsagent.dialog.preferences.res.IdentityOptions";

   private JPanel panMain = new JPanel();
   
   private JLabel labRealName = new JLabel();
   private JTextField tfRealName = new JTextFieldFixedHeight();
   private JLabel labEmail = new JLabel();
   private JTextField tfEmail = new JTextFieldFixedHeight();
   private JLabel labReplyTo = new JLabel();
   private JTextField tfReplyTo = new JTextFieldFixedHeight();
   private JLabel labOrganisation = new JLabel();
   private JTextField tfOrganisation = new JTextFieldFixedHeight();
   private IconicPreferencePanel ippAboutYou = new IconicPreferencePanel();
   
   private IconicPreferencePanel ippSignatures = new IconicPreferencePanel();
   private JLabel labSigText = new JLabel();
   private FixedTextArea taSigText = new FixedTextArea();
   private JScrollPane staSigText = new JScrollPane(taSigText);
   private JTextField tfSigFile = new JTextFieldFixedHeight();
   private JButton butSigFile = new JButton();
   
   private JFileChooser m_fileDialog = new JFileChooser();
   

   public static final String ID = ResourceManager.getManagerFor(RES).getString("IdentityOptions.title");

   /**
    * Create an identity options panel.
    */
   public IdentityOptionsPanel() 
   {
      super(ResourceManager.getManagerFor(RES), "IdentityOptions");
      init();
      setContent(panMain);
      ResourceManager.getManagerFor(RES).initComponents(panMain);
   }


   /**
    * Add all components
    *
    */
   private void init()
   {
      initAboutYou();
      initSignatures();
     
      panMain.setLayout(new BoxLayout(panMain, BoxLayout.Y_AXIS));
      panMain.setName("MainPanel");
      panMain.add(ippAboutYou);
      panMain.add(ippSignatures);
      panMain.add(Box.createGlue());

   }
   

   /**
    * Construct the "signatures" panel.
    *
    */
   private void initSignatures()
   {
      // Signatures panel
      ippSignatures.setName("Signatures");
      VerticalFlowPanel group = ippSignatures.getContainer();
      
      labSigText.setName("SignatureText");
      taSigText.setName("SignatureTextField");
      taSigText.setRows(3);
      group.addRow(labSigText);
      group.addSpacerRow(staSigText);
      
      JPanel grpSigFile = new JPanel();
      grpSigFile.setMaximumSize(new Dimension(grpSigFile.getMaximumSize().width, tfSigFile.getPreferredSize().height));
      grpSigFile.setLayout(new BorderLayout());
      grpSigFile.setName("SigFile");
      tfSigFile.setName("SignatureFileName");
      butSigFile.setName("SignatureFileButton");
      butSigFile.setMaximumSize(new Dimension(butSigFile.getMaximumSize().width, tfSigFile.getPreferredSize().height));
      grpSigFile.add(tfSigFile, BorderLayout.CENTER);
      grpSigFile.add(butSigFile, BorderLayout.EAST);
      group.addRow(grpSigFile);
      
      butSigFile.addActionListener(new BrowseButtonListener());
      tfSigFile.getDocument().addDocumentListener(new FilenameListener());
   }


   /**
    * Construct the "user information" panel
    *
    */
   private void initAboutYou()
   {
      //
      // User Information panel
      //
      ippAboutYou.setName("AboutYou");
      VerticalFlowPanel group = ippAboutYou.getContainer();
                  
      labRealName.setName("RealName");
      tfRealName.setName("RealNameField");
      group.addRow(labRealName);
      group.addRow(tfRealName);
      
      labEmail.setName("Email");
      tfEmail.setName("EmailField");
      group.addRow(labEmail);
      group.addRow(tfEmail);   
      
      labReplyTo.setName("ReplyTo");
      tfReplyTo.setName("ReplyToField");
      group.addRow(labReplyTo);
      group.addRow(tfReplyTo);

      labOrganisation.setName("Organization");
      tfOrganisation.setName("OrganizationField");
      group.addRow(labOrganisation);
      group.addRow(tfOrganisation);  
      group.addSpacerRow(new JPanel());

   }

   /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
   public void revert(UserPreferences p) 
   {
      tfRealName.setText(p.getPreference(PreferenceKeys.IDENTITY_REALNAME, ""));
      tfEmail.setText(p.getPreference(PreferenceKeys.IDENTITY_EMAIL, ""));
      tfReplyTo.setText(p.getPreference(PreferenceKeys.IDENTITY_REPLYTO, ""));
      tfOrganisation.setText(p.getPreference(PreferenceKeys.IDENTITY_ORGANISATION, ""));
      tfSigFile.setText(p.getPreference(PreferenceKeys.SIGNATURE_FILENAME, ""));
      if (tfSigFile.getText().length() != 0)
      {
         taSigText.setEnabled(false);
      }
      else
      {
         taSigText.setEnabled(true);
      }
      taSigText.setText(p.getPreference(PreferenceKeys.SIGNATURE_TEXT, ""));
      
   }

   /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
   public void save(UserPreferences p) 
   {
      p.setPreference(PreferenceKeys.IDENTITY_REALNAME, tfRealName.getText());
      p.setPreference(PreferenceKeys.IDENTITY_EMAIL, tfEmail.getText());
      p.setPreference(PreferenceKeys.IDENTITY_REPLYTO, tfReplyTo.getText());
      p.setPreference(PreferenceKeys.IDENTITY_ORGANISATION, tfOrganisation.getText());
      p.setPreference(PreferenceKeys.SIGNATURE_TEXT, taSigText.getText());
      p.setPreference(PreferenceKeys.SIGNATURE_FILENAME, tfSigFile.getText());
   }


   /** 
    * Listen out for clicks on the Filename button and bring up the file dialog.
    */
   class BrowseButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         int result = m_fileDialog.showOpenDialog(IdentityOptionsPanel.this);
         if (result == JFileChooser.APPROVE_OPTION)
         {
            tfSigFile.setText(m_fileDialog.getSelectedFile().getPath());
         }
      }
   }   
   
   /**
    * Listen to the filename text field. If it is blanked out, reenable the text area.
    */
   class FilenameListener implements DocumentListener
   {
      private void checkForBlank(Document doc)
      {
         if (doc.getLength() == 0)
         {
            taSigText.setEnabled(true);
         }
         else
         {
            taSigText.setEnabled(false);
         }
      }
   
      public void changedUpdate(DocumentEvent e)
      {
         checkForBlank(e.getDocument());
      }
      
      public void insertUpdate(DocumentEvent e)
      {
         checkForBlank(e.getDocument());
      }
      
      public void removeUpdate(DocumentEvent e)
      {
         checkForBlank(e.getDocument());
      }
   }

}

//
// Old Log
//
// <LI>0.1 [04/04/98]: Initial Revision
// <LI>0.2 [20/04/98]: Changed to JPanel


// New Log
//
// $Log: not supported by cvs2svn $