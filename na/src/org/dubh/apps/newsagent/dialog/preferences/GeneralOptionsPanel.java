// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: GeneralOptionsPanel.java,v 1.5 1999-06-01 00:37:14 briand Exp $
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
import java.util.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import dubh.utils.misc.ResourceManager;
import javax.swing.*;
import javax.swing.border.*; 
import java.beans.*;
import javax.swing.event.*;

import dubh.utils.ui.preferences.*;
import dubh.utils.misc.UserPreferences;
import dubh.utils.misc.Debug;
import dubh.apps.newsagent.PreferenceKeys;
import dubh.apps.newsagent.GlobalState;

/**
 * General Options Panel for the General Tab in the Options dialog box <P>
 * The following options are available from this panel:<P>
 * <TABLE><TH><TD><B>Property</B></TD><TD><B>Description</B></TD><TD><B>Default</B></TD></TH>
 * <TR>
 * <TD>newsagent.general.AutoUpdate</TD>
 * <TD>Whether to automatically update messages</TD>
 * <TD>yes</TD>
 * </TR>
 * <TR>
 * <TD>newsagent.general.NewsgroupNotify</TD>
 * <TD>Whether to notify the user when there are new newsgroups</TD>
 * <TD>yes</TD>
 * </TR>
 * <TR>
 * <TD>newsagent.general.UpdateInterval</TD>
 * <TD>How many minutes between updating the message list if AutoUpdate is yes</TD>
 * <TD>5</TD>
 * </TR></TABLE><P>
 @author Brian Duff
 @version $Id: GeneralOptionsPanel.java,v 1.5 1999-06-01 00:37:14 briand Exp $
 */

public class GeneralOptionsPanel extends PreferencePage {
  JPanel jPanel1 = new JPanel();
 
  public TitledBorder borderGeneral = new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("GeneralOptionsPanel.GeneralOptions"));
  BorderLayout borderLayout1 = new BorderLayout();
  JCheckBox jCheckBox1 = new JCheckBox();
  JSlider updateSlider = new JSlider();
  JLabel jLabel1 = new JLabel();
  JLabel valLabel = new JLabel();
  JCheckBox jCheckBox2 = new JCheckBox();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();

  public GeneralOptionsPanel() {
     super("General", "General options", null);
    try {
      jbInit();
      setContent(jPanel1);
      //revert(null);
    }
    catch (Exception e) {
      Debug.printException(1, this, e);
    }
  }
  
  public Container getContent()
  {
     return jPanel1;
  }
  

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revert(UserPreferences p) {
     UserPreferences prefs = GlobalState.getPreferences();
    jCheckBox1.setSelected(prefs.getBoolPreference(PreferenceKeys.GENERAL_AUTOUPDATE, true));
    jCheckBox2.setSelected(prefs.getBoolPreference(PreferenceKeys.GENERAL_NEWSGROUPNOTIFY, true));
    String update = prefs.getPreference(PreferenceKeys.GENERAL_UPDATEINTERVAL,"5");
    updateSlider.setValue(Integer.parseInt(update.trim()));
    valLabel.setText(update+" minutes");
    // Only enable slider if the AutoUpdate checkbox is selected.
    updateSlider.setEnabled(jCheckBox1.isSelected());
    updateSlider.repaint();
  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void save(UserPreferences p) {
     UserPreferences prefs = GlobalState.getPreferences();
     prefs.setBoolPreference(PreferenceKeys.GENERAL_AUTOUPDATE, jCheckBox1.isSelected());
     prefs.setBoolPreference(PreferenceKeys.GENERAL_NEWSGROUPNOTIFY, jCheckBox2.isSelected());
     prefs.setPreference(PreferenceKeys.GENERAL_UPDATEINTERVAL, valLabel.getText().substring(0,2));
  }


  public void jbInit() throws Exception{
    ResourceManager r = GlobalState.getRes();
    jPanel1.setMaximumSize(new Dimension(457, 264));
    jPanel1.setBorder(borderGeneral);
    // jCheckBox1.setText(GlobalState.getResString("GeneralOptionsPanel.UpdateEvery"));
    jCheckBox1.setHorizontalAlignment(SwingConstants.LEFT);
    // jCheckBox1.setMnemonic(GlobalState.getResString("GeneralOptionsPanel.UpdateEveryAccelerator").charAt(0));
    // jCheckBox1.setToolTipText(GlobalState.getResString("GeneralOptionsPanel.UpdateEveryTip"));
    jCheckBox1.addActionListener(new GeneralOptionsPanel_jCheckBox1_actionAdapter(this));
//NLS    r.initButton(jCheckBox1, "GeneralOptionsPanel.UpdateEvery");
     updateSlider.setMinorTickSpacing(1);
    updateSlider.setValue(60);
    updateSlider.setMaximum(30);
    updateSlider.setPaintTicks(true);
    updateSlider.setMajorTickSpacing(5);
    updateSlider.setPreferredSize(new Dimension(125, 40));
    updateSlider.setToolTipText(GlobalState.getRes().getString("GeneralOptionsPanel.TickerTip"));
    updateSlider.addChangeListener(new GeneralOptionsPanel_updateSlider_changeAdapter(this));
    updateSlider.addPropertyChangeListener(new GeneralOptionsPanel_updateSlider_propertyChangeAdapter(this));
    updateSlider.addChangeListener(new GeneralOptionsPanel_updateSlider_changeAdapter(this));
    updateSlider.addPropertyChangeListener(new GeneralOptionsPanel_updateSlider_propertyChangeAdapter(this));
    valLabel.setText("value");
    //jCheckBox2.setText(GlobalState.getResString("GeneralOptionsPanel.Notify"));
    //jCheckBox2.setMnemonic(GlobalState.getResString("GeneralOptionsPanel.NotifyAccelerator").charAt(0));
    //jCheckBox2.setToolTipText(GlobalState.getResString("GeneralOptionsPanel.NotifyTip"));
//NLS    r.initButton(jCheckBox2, "GeneralOptionsPanel.Notify");
     jPanel3.setPreferredSize(new Dimension(426, 45));
    jPanel4.setPreferredSize(new Dimension(424, 35));
    jPanel4.setLayout(gridBagLayout2);
    jPanel3.setLayout(gridBagLayout3);
    jCheckBox2.setHorizontalAlignment(SwingConstants.LEFT);
    jPanel1.setLayout(gridBagLayout1);
    jPanel1.add(jPanel3, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel3.add(jCheckBox1, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0));
    jPanel3.add(updateSlider, new GridBagConstraints2(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
    jPanel3.add(valLabel, new GridBagConstraints2(2, 0, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jPanel4, new GridBagConstraints2(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jCheckBox2, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
  }

  void updateSlider_stateChanged(ChangeEvent e) {
   valLabel.setText(Integer.toString(updateSlider.getValue())+" "+GlobalState.getRes().getString("GeneralOptionsPanel.Minutes"));
  }

  void updateSlider_propertyChange(PropertyChangeEvent e) {

  }

  void jCheckBox1_actionPerformed(ActionEvent e) {
    updateSlider.setEnabled(jCheckBox1.isSelected());
    updateSlider.repaint();

  }


}

class GeneralOptionsPanel_updateSlider_propertyChangeAdapter implements java.beans.PropertyChangeListener{
  GeneralOptionsPanel adaptee;

  GeneralOptionsPanel_updateSlider_propertyChangeAdapter(GeneralOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void propertyChange(PropertyChangeEvent e) {
    adaptee.updateSlider_propertyChange(e);
  }
}

class GeneralOptionsPanel_updateSlider_changeAdapter implements javax.swing.event.ChangeListener{
  GeneralOptionsPanel adaptee;

  GeneralOptionsPanel_updateSlider_changeAdapter(GeneralOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void stateChanged(ChangeEvent e) {
    adaptee.updateSlider_stateChanged(e);
  }
}

class GeneralOptionsPanel_jCheckBox1_actionAdapter implements java.awt.event.ActionListener{
  GeneralOptionsPanel adaptee;

  GeneralOptionsPanel_jCheckBox1_actionAdapter(GeneralOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jCheckBox1_actionPerformed(e);
  }
}

//
// Old Log
//
// <LI>0.1 [24/02/98]: Initial Revision
// <LI>0.2 [26/02/98]: Added property interface.
// <LI>0.3 [27/02/98]: Changed to a JPanel so it works in the TabbedPane.
//       Changed layout managers, things are looking a lot better now. Fixed
//       repaint bug for slider.
// <LI>0.4 [03/03/98]: Changed property interface to use GlobalState.
// <LI>0.5 [07/03/98]: Updated to use GlobalState.getResString() - Internationalised.
// <LI>0.6 [20/04/98]: Changed to use GridBagLayout rather than VerticalFlow.
// <LI>0.7 [05/10/98]: Changed to use new resource strings.
//
// New Log:
//
// $Log: not supported by cvs2svn $