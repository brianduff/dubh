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
import java.util.Hashtable;
import javax.swing.border.*;
import javax.swing.*;

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.dialog.ErrorReporter;

import dubh.utils.ui.*;
import dubh.utils.misc.StringUtils;
import dubh.utils.misc.ResourceManager;



/**
 * Send Options Panel for the Send Tab in the Options dialog box <P>
 * Version History: <UL>
 * <LI>0.1 [26/02/98]: Initial Revision
 * <LI>0.2 [27/02/98]: Changed layout managers. Changed to a JPanel to work in
 *				TabbedPane.
 * <LI>0.3 [03/03/98]: Implemented preferences methods.
 * <LI>0.4 [07/03/98]: Added event binding for Signatures, added
 *				Internationalisation (using getResString())
 * <LI>1.0 [10/06/98]: Big change for NewsAgent 1.02
 * <LI>
 *</UL>
 @author Brian Duff
 @version 1.0 [10/06/98]
 */
public class SendOptionsPanel extends JPanel {
  private TitledBorder borderQuoting = new TitledBorder(new EtchedBorder(),
  	GlobalState.getResString("SendOptionsPanel.Quoting"));
  private JPanel panQuoting = new JPanel();
  private GridBagLayout layoutMain = new GridBagLayout();
  private GridBagLayout layoutQuoting = new GridBagLayout();
  private JLabel labWhenReply = new JLabel();
  private ButtonGroup groupWhenReply = new ButtonGroup();
  private JRadioButton optNothing = new JRadioButton();
  private JRadioButton optSelection = new JRadioButton();
  private JRadioButton optAll = new JRadioButton();
  private JLabel labPrefix = new JLabel();
  private JTextArea taPrefix = new JTextArea();
  private JScrollPane scrollPrefix = new JScrollPane(taPrefix);
  private JButton cmdInsert = new JButton();
  private JLabel labPrefixLine = new JLabel();
  private JTextField tfPrefixLine = new JTextField();
  private JPopupMenu popupInsert;


  public SendOptionsPanel() {
    try {
      jbInit();
      revertPreferences();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    ResourceManager r = GlobalState.getRes();
    labWhenReply.setText(GlobalState.getResString("SendOptionsPanel.Quoting.WhenReply"));

//NLS    r.initButton(optNothing, "SendOptionsPanel.Quoting.OptNothing");
//NLS    r.initButton(optSelection, "SendOptionsPanel.Quoting.OptSelected");
//NLS    r.initButton(optAll, "SendOptionsPanel.Quoting.OptAll");
    groupWhenReply.add(optNothing);
    groupWhenReply.add(optSelection);
    groupWhenReply.add(optAll);
    labPrefix.setText(GlobalState.getResString("SendOptionsPanel.Quoting.Prefix"));

    /*
     * Add a popup glyph to the insert button
     */
//NLS    r.initButton(cmdInsert, "SendOptionsPanel.Quoting.Insert");
    cmdInsert.setIcon(new ImageIcon(GlobalState.getImage("glyphPopup.gif")));
    cmdInsert.setHorizontalTextPosition(AbstractButton.LEFT);
    cmdInsert.addActionListener(new InsertListener());

    labPrefixLine.setText(GlobalState.getResString("SendOptionsPanel.Quoting.PrefixLine"));
    panQuoting.setLayout(layoutQuoting);
    taPrefix.setFont(new Font("Monospaced", Font.PLAIN, 12));
    taPrefix.setLineWrap(true);
    taPrefix.setWrapStyleWord(true);
    taPrefix.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    this.setLayout(layoutMain);
    this.add(panQuoting, new GridBagConstraints2(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panQuoting.add(labWhenReply, new GridBagConstraints2(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
    panQuoting.add(optNothing, new GridBagConstraints2(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 5), 0, 0));
    panQuoting.add(optSelection, new GridBagConstraints2(0, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 5), 0, 0));
    panQuoting.add(optAll, new GridBagConstraints2(0, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 5), 0, 0));
    panQuoting.add(labPrefix, new GridBagConstraints2(0, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 5), 0, 0));
    panQuoting.add(scrollPrefix, new GridBagConstraints2(0, 5, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 20, 2, 5), 0, 0));
    panQuoting.add(cmdInsert, new GridBagConstraints2(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    panQuoting.add(labPrefixLine, new GridBagConstraints2(0, 6, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
    panQuoting.add(tfPrefixLine, new GridBagConstraints2(0, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 5, 5), 0, 0));
    panQuoting.setBorder(borderQuoting);
    JMenuBarResource rsc = new JMenuBarResource("Menus", "mbSendOptions", this);
    popupInsert = rsc.getPopupMenu("popupSendIns");

  }

  /**
   * Applies the preferences to the user preferences in the GlobalState. You
   * should call this on all panels, then save the preference file.
   */
  public void applyPreferences() {
     GlobalState.setPreference("newsagent.send.IncludeHeading", taPrefix.getText());
     GlobalState.setPreference("newsagent.send.IncludePrefix", tfPrefixLine.getText());
     if (optNothing.isSelected())
        GlobalState.setPreference("newsagent.send.IncludeBehaviour", "none");
     else if (optSelection.isSelected())
        GlobalState.setPreference("newsagent.send.IncludeBehaviour", "selected");
     else if (optAll.isSelected())
        GlobalState.setPreference("newsagent.send.IncludeBehaviour", "all");
  }

  /**
   * Set all controls to the values from the user preferences. If the preferences
   * don't exist, use sensible defaults. You should call this if the cancel
   * button was clicked or the window was closed without OK being clicked.
   */
  public void revertPreferences() {
     optAll.setSelected(false);
     optSelection.setSelected(false);
     optNothing.setSelected(false);

     String includeBehaviour = GlobalState.getPreference("newsagent.send.IncludeBehaviour", "all");
     if (includeBehaviour.trim().equalsIgnoreCase("all"))
        optAll.setSelected(true);
     else if (includeBehaviour.trim().equalsIgnoreCase("selected"))
        optSelection.setSelected(true);
     else if (includeBehaviour.trim().equalsIgnoreCase("none"))
        optNothing.setSelected(true);

     taPrefix.setText(GlobalState.getPreference("newsagent.send.IncludeHeading",
        "In message {message-id}, {x-na-realname} wrote:"));
     tfPrefixLine.setText(GlobalState.getPreference("newsagent.send.IncludePrefix",
        "> "));
  }

  /*
   * Listener for the insert button. Pops up a menu containing common
   * header fields from the original message.
   */
  class InsertListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        popupInsert.show(cmdInsert, 0, cmdInsert.getSize().height);
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