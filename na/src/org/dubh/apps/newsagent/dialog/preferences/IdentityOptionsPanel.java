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
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import dubh.apps.newsagent.GlobalState;


/**
 * Panel for displaying Identity Options in preferences.
 * <TABLE><TR><TD><B>Property</B></TD><TD><B>Description</B></TD><TD><B>Default</B></TD></TR>
 * <TR>
 * <TD>newsagent.identity.realname</TD>
 * <TD>The user's real name</TD>
 * <TD>""</TD>
 * </TR><TR>
 * <TD>newsagent.identity.email</TD>
 * <TD>The user's email address</TD>
 * <TD>""</TD>
 * </TR><TR>
 * <TD>newsagent.identity.organisation</TD>
 * <TD>The user's organisation</TD>
 * <TD>""</TD>
 * </TR></TABLE>
 * Version History: <UL>
 * <LI>0.1 [04/04/98]: Initial Revision
 * <LI>0.2 [20/04/98]: Changed to JPanel
 *</UL>
 @author Brian Duff
 @version 0.2 [20/04/98]
 */
public class IdentityOptionsPanel extends JPanel {
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labRealName = new JLabel();
  JTextField tfRealName = new JTextField();
  JLabel labEmail = new JLabel();
  JTextField tfEmail = new JTextField();
  JLabel labOrganisation = new JLabel();
  JTextField tfOrganisation = new JTextField();
  JPanel panIdentity = new JPanel();
  TitledBorder borderIdentity = new TitledBorder(new EtchedBorder(),
  	GlobalState.getResString("IdentityOptionsPanel.IdentityOptions"));
  BorderLayout borderLayout1 = new BorderLayout();

  public IdentityOptionsPanel() {
    try {
      jbInit();
      revertPreferences();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    labRealName.setText(GlobalState.getResString("IdentityOptionsPanel.RealName"));
    labEmail.setText(GlobalState.getResString("IdentityOptionsPanel.Email"));
    labOrganisation.setText(GlobalState.getResString("IdentityOptionsPanel.Organisation"));
    panIdentity.setLayout(gridBagLayout1);
    panIdentity.add(labRealName, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 1, 1), 0, 0));
    panIdentity.add(tfRealName, new GridBagConstraints2(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 1, 1, 5), 0, 0));
    panIdentity.add(labEmail, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 5), 0, 0));
    panIdentity.add(tfEmail, new GridBagConstraints2(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 5), 0, 0));
    panIdentity.add(labOrganisation, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 1), 0, 0));
    panIdentity.add(tfOrganisation, new GridBagConstraints2(1, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 5), 0, 0));
    panIdentity.setBorder(borderIdentity);
    this.setLayout(borderLayout1);
    this.add(panIdentity, BorderLayout.CENTER);
  }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revertPreferences() {
   tfRealName.setText(GlobalState.getPreference("newsagent.identity.realname", ""));
   tfEmail.setText(GlobalState.getPreference("newsagent.identity.email", ""));
   tfOrganisation.setText(GlobalState.getPreference("newsagent.identity.organisation", ""));
  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void applyPreferences() {
   GlobalState.setPreference("newsagent.identity.realname", tfRealName.getText());
   GlobalState.setPreference("newsagent.identity.email", tfEmail.getText());
   GlobalState.setPreference("newsagent.identity.organisation", tfOrganisation.getText());

  }

}