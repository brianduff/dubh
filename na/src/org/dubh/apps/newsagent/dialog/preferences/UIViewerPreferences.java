// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: UIViewerPreferences.java,v 1.2 1999-06-01 18:00:10 briand Exp $
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
import dubh.apps.newsagent.MessageHeaderFields;

/**
 * Panel for displaying Identity Options in preferences.
 * @author Brian Duff
 * @version $Id: UIViewerPreferences.java,v 1.2 1999-06-01 18:00:10 briand Exp $
 */
public class UIViewerPreferences extends PreferencePage 
{
   private final static String RES = "dubh.apps.newsagent.dialog.preferences.res.UIViewer";
   public static final String ID = ResourceManager.getManagerFor(RES).getString("UIViewer.title");

   private final static Font DEFAULT_BODYFONT = new Font("Monospaced", Font.PLAIN, 12);
   private final static Font DEFAULT_SIGFONT  = new Font("Monospaced", Font.PLAIN, 12);
   private final static Font DEFAULT_QUOTEFONT = new Font("Monospaced", Font.ITALIC, 12);
   
   private final static Color DEFAULT_BODYCOLOR = Color.black;
   private final static Color DEFAULT_SIGCOLOR = Color.black;
   private final static Color DEFAULT_QUOTECOLOR = Color.green;
      

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
   private FormatBar fbQuoteFormat = new FormatBar();
   
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

      ResourceManager r = ResourceManager.getManagerFor(RES);
      shlHeaders.setLeftListDescription(r.getString("MainPanel.Headers.Controls.Shuttle.LeftList.text"));
      shlHeaders.setRightListDescription(r.getString("MainPanel.Headers.Controls.Shuttle.RightList.text"));
      
      //
      // Set the left list in the shuttle
      //
      shlHeaders.setLeftListItems(MessageHeaderFields.ALL_HEADERS);
      
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
      group.addRow(fbQuoteFormat);
      ResourceManager res = ResourceManager.getManagerFor(RES);
      fbBodyFormat.setSampleLabel(res.getString("MainPanel.Body.Controls.BodyFormat.sampleText"));
      fbQuoteFormat.setSampleLabel(res.getString("MainPanel.Body.Controls.QuoteFormat.sampleText"));
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
      fbSigFormat.setSampleLabel(ResourceManager.getManagerFor(RES).getString(
         "MainPanel.Signature.Controls.SigFormat.sampleText"
      ));
   }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revert(UserPreferences p) 
  {
     cbShowHeaders.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_SHOWHEADERS, true));
     shlHeaders.setRightListItems(p.getStringArrayPreference(PreferenceKeys.VIEWER_DISPLAYEDHEADERS, MessageHeaderFields.DEFAULT_VIEWER_HEADERS));
     
     cbCreateHyperlinks.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_HYPERLINKS, true));
     cbAllowHTML.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_ALLOWHTML, true));
     cbWrap.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_WRAP, true));
     fbBodyFormat.setFormatFont(p.getFontPreference(PreferenceKeys.VIEWER_NORMALFONT, DEFAULT_BODYFONT));
     fbBodyFormat.setFormatColor(p.getColorPreference(PreferenceKeys.VIEWER_NORMALCOLOR, DEFAULT_BODYCOLOR));
     fbQuoteFormat.setFormatFont(p.getFontPreference(PreferenceKeys.VIEWER_QUOTEDFONT, DEFAULT_QUOTEFONT));
     fbQuoteFormat.setFormatColor(p.getColorPreference(PreferenceKeys.VIEWER_QUOTEDCOLOR, DEFAULT_QUOTECOLOR));
     
     cbHideSig.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_HIDESIG, false));
     cbShowVCard.setSelected(p.getBoolPreference(PreferenceKeys.VIEWER_SHOWVCARD, false));
     fbSigFormat.setFormatFont(p.getFontPreference(PreferenceKeys.VIEWER_SIGFONT, DEFAULT_SIGFONT));
     fbSigFormat.setFormatColor(p.getColorPreference(PreferenceKeys.VIEWER_SIGCOLOR, DEFAULT_SIGCOLOR));
  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void save(UserPreferences p) 
  {
     p.setBoolPreference(PreferenceKeys.VIEWER_SHOWHEADERS, cbShowHeaders.isSelected());
     p.setPreference(PreferenceKeys.VIEWER_DISPLAYEDHEADERS, StringUtils.createSentence(shlHeaders.getRightListItems()));
     
     p.setBoolPreference(PreferenceKeys.VIEWER_HYPERLINKS, cbCreateHyperlinks.isSelected());
     p.setBoolPreference(PreferenceKeys.VIEWER_ALLOWHTML, cbAllowHTML.isSelected());
     p.setBoolPreference(PreferenceKeys.VIEWER_WRAP, cbWrap.isSelected());
     p.setFontPreference(PreferenceKeys.VIEWER_NORMALFONT, fbBodyFormat.getFormatFont());
     p.setColorPreference(PreferenceKeys.VIEWER_NORMALCOLOR, fbBodyFormat.getFormatColor());
     p.setFontPreference(PreferenceKeys.VIEWER_QUOTEDFONT, fbQuoteFormat.getFormatFont());
     p.setColorPreference(PreferenceKeys.VIEWER_QUOTEDCOLOR, fbQuoteFormat.getFormatColor());
     
     p.setBoolPreference(PreferenceKeys.VIEWER_HIDESIG, cbHideSig.isSelected());
     p.setBoolPreference(PreferenceKeys.VIEWER_SHOWVCARD, cbShowVCard.isSelected());
     p.setFontPreference(PreferenceKeys.VIEWER_SIGFONT, fbSigFormat.getFormatFont());
     p.setColorPreference(PreferenceKeys.VIEWER_SIGCOLOR, fbSigFormat.getFormatColor());
     
  }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/06/01 00:33:14  briand
// New preference page for configuring the message viewer.
//
//