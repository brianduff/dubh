// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: UIViewerPreferences.java,v 1.1 1999-06-01 00:33:14 briand Exp $
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
import dubh.apps.newsagent.GlobalState;
import dubh.utils.ui.preferences.*;
import dubh.utils.ui.*;
import dubh.utils.misc.*;
import dubh.apps.newsagent.PreferenceKeys;

/**
 * Panel for displaying Identity Options in preferences.
 * @author Brian Duff
 * @version $Id: UIViewerPreferences.java,v 1.1 1999-06-01 00:33:14 briand Exp $
 */
public class UIViewerPreferences extends PreferencePage 
{
   private final static String RES = "dubh.apps.newsagent.dialog.preferences.res.UIViewer";
   public static final String ID = ResourceManager.getManagerFor(RES).getString("UIViewer.title");

   private JPanel panMain = new JPanel();
   
   private IconicPreferencePanel ippHeaders = new IconicPreferencePanel();
   private JCheckBox cbShowHeaders = new JCheckBox();
   private Shuttle shlHeaders = new Shuttle();
   private JButton cmdOtherHeader = new JButton();

   private IconicPreferencePanel ippBody = new IconicPreferencePanel();
   private JCheckBox cbCreateHyperlinks = new JCheckBox();
   private JCheckBox cbAllowHTML = new JCheckBox();
   private JCheckBox cbWrap = new JCheckBox();
   private FormatBar fbBodyFormat = new FormatBar();
   
   private IconicPreferencePanel ippSignature = new IconicPreferencePanel();
   private JCheckBox cbHideSig = new JCheckBox();
   private JCheckBox cbShowVCard = new JCheckBox();
   private FormatBar fbSigFormat = new FormatBar();
   

   public UIViewerPreferences() 
   {
      super(ResourceManager.getManagerFor(RES), "UIViewer");
      init();
      setContent(panMain);
      ResourceManager.getManagerFor(RES).initComponents(panMain);
   }

   private void init()
   {
      initHeaders();
      initBody();
      initSignature();

      panMain.setLayout(new BoxLayout(panMain, BoxLayout.Y_AXIS));
      panMain.setName("MainPanel");
      panMain.add(ippHeaders);
      panMain.add(ippBody);
      panMain.add(ippSignature);
      panMain.add(Box.createGlue());   


   }
   
   private void initHeaders()
   {
      ippHeaders.setName("Headers");
      VerticalFlowPanel group = ippHeaders.getContainer();
      
      cbShowHeaders.setName("ShowHeaders");
      group.addRow(cbShowHeaders);
      
      group.addSpacerRow(shlHeaders);
      //
      // TODO : Set shuttle labels
      //
      
      cmdOtherHeader.setName("OtherHeader");
      group.addRow(cmdOtherHeader);
      
   }
   
   private void initBody()
   {
      ippBody.setName("Body");
      VerticalFlowPanel group = ippBody.getContainer();
      
      cbCreateHyperlinks.setName("CreateHyperlinks");
      group.addRow(cbCreateHyperlinks);
      
      cbAllowHTML.setName("AllowHTML");
      group.addRow(cbAllowHTML);
      
      cbWrap.setName("Wrap");
      group.addRow(cbWrap);
      
      group.addRow(fbBodyFormat);
      // TODO set sample text of formatbar
   }
   
   private void initSignature()
   {
      ippSignature.setName("Signature");
      VerticalFlowPanel group = ippSignature.getContainer();
   
      cbHideSig.setName("HideSig");
      group.addRow(cbHideSig);
      
      cbShowVCard.setName("ShowVCard");
      group.addRow(cbShowVCard);
      
      group.addRow(fbSigFormat);
      // Todo format sample text
   }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revert(UserPreferences p) {
  /* tfRealName.setText(GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_REALNAME, ""));
   tfEmail.setText(GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_EMAIL, ""));
   tfOrganisation.setText(GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_ORGANISATION, ""));
  */
  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void save(UserPreferences p) {
   /*GlobalState.getPreferences().setPreference(PreferenceKeys.IDENTITY_REALNAME, tfRealName.getText());
   GlobalState.getPreferences().setPreference(PreferenceKeys.IDENTITY_EMAIL, tfEmail.getText());
   GlobalState.getPreferences().setPreference(PreferenceKeys.IDENTITY_ORGANISATION, tfOrganisation.getText());
*/
  }

}

//
// $Log: not supported by cvs2svn $
//