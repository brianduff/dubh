// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: UnreadHighlightAgentPanel.java,v 1.6 2001-02-11 02:50:59 briand Exp $
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

package org.dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import org.dubh.dju.misc.StringUtils;
import org.dubh.dju.misc.Debug;

/**
 * Panel for the UnreadHighlightAgent
 * @author Brian Duff
 * @version $Id: UnreadHighlightAgentPanel.java,v 1.6 2001-02-11 02:50:59 briand Exp $
 */
public class UnreadHighlightAgentPanel extends JPanel {

   protected static final int MIN_FONTSIZE = 8;

// UI Vars

   TitledBorder borderHighlight = new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("Agents.UnreadHighlight"));
   TitledBorder borderFormatting= new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("Agents.Formatting"));
   TitledBorder borderSample = new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("Agents.Sample"));
   GridBagLayout gridBagLayout1 = new GridBagLayout();
   JPanel panHighlight = new JPanel();
   JPanel panFormatting = new JPanel();
   JPanel panSample = new JPanel();
   GridBagLayout gridBagLayout2 = new GridBagLayout();
   JLabel labIn = new JLabel();
   JRadioButton optAllNewsgroups = new JRadioButton();
   JRadioButton optNewsgroupsCont = new JRadioButton();
   JTextField tfGroups = new JTextField();
   GridBagLayout gridBagLayout3 = new GridBagLayout();
   JLabel labFont = new JLabel();
   JComboBox cmbFont = new JComboBox();
   JCheckBox chkBold = new JCheckBox();
   JLabel labSize = new JLabel();
   JTextField tfSize = new JTextField();
   JLabel labPt = new JLabel();
   JCheckBox chkItalic = new JCheckBox();
   JLabel labColour = new JLabel();
   JPanel panColour = new JPanel();
   JButton cmdColour = new JButton();
   JPanel panSampleText = new SampleHeaderPanel();
   GridBagLayout gridBagLayout4 = new GridBagLayout();
   ButtonGroup radioGroup = new ButtonGroup();
   SampleUpdater updateListener = new SampleUpdater();

   public UnreadHighlightAgentPanel() {
      try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    panFormatting.setLayout(gridBagLayout3);
    panSample.setLayout(gridBagLayout4);
    panHighlight.setBorder(borderHighlight);
    panFormatting.setBorder(borderFormatting);
    panSample.setBorder(borderSample);
    labIn.setText("In");
    optAllNewsgroups.setText("All Newsgroups");
    optAllNewsgroups.setActionCommand("allgroups");
    optAllNewsgroups.setSelected(true);
    optNewsgroupsCont.setText("Newsgroups Containing");
    optNewsgroupsCont.setActionCommand("groupscont");
    radioGroup.add(optAllNewsgroups);
    radioGroup.add(optNewsgroupsCont);
    labFont.setText("Font:");
    chkBold.setText("Bold");
    chkBold.addActionListener(updateListener);
    labSize.setText("Size:");
    labPt.setText("pt.");
    chkItalic.setText("Italic");
    chkItalic.addActionListener(updateListener);
    tfSize.addActionListener(updateListener);
    cmbFont.addActionListener(updateListener);
    labColour.setText("Colour");
    panColour.setBackground(SystemColor.textText);
    cmdColour.setText("...");
    cmdColour.addActionListener(new UnreadHighlightAgentPanel_cmdColour_actionAdapter(this));
    panSampleText.setBackground(SystemColor.window);
    panHighlight.setLayout(gridBagLayout2);
    this.setLayout(gridBagLayout1);
    this.add(panHighlight, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 5), 0, 0));
    panHighlight.add(labIn, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 2, 0, 10), 0, 0));
    panHighlight.add(optAllNewsgroups, new GridBagConstraints2(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 5, 0, 0), 0, 0));
    panHighlight.add(optNewsgroupsCont, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 1, 0), 0, 0));
    panHighlight.add(tfGroups, new GridBagConstraints2(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 1, 2), 40, 0));
    this.add(panFormatting, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 2, 5), 0, 0));
    panFormatting.add(labFont, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 2), 0, 0));
    panFormatting.add(cmbFont, new GridBagConstraints2(1, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 75, 0));
    panFormatting.add(chkBold, new GridBagConstraints2(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 10, 1, 5), 0, 0));
    panFormatting.add(labSize, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 2), 0, 0));
    panFormatting.add(tfSize, new GridBagConstraints2(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 2), 40, 0));
    panFormatting.add(labPt, new GridBagConstraints2(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 3, 1, 2), 0, 0));
    panFormatting.add(chkItalic, new GridBagConstraints2(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 10, 1, 2), 0, 0));
    panFormatting.add(labColour, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 2), 0, 0));
    panFormatting.add(panColour, new GridBagConstraints2(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 0), 15, 15));
    panFormatting.add(cmdColour, new GridBagConstraints2(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 5, 1, 2), 0, 0));
    this.add(panSample, new GridBagConstraints2(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 5, 5, 5), 0, 0));
    panSample.add(panSampleText, new GridBagConstraints2(0, 0, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 250, 30));

    initialiseCombo();
    setDefaults();
  }


  /**
   * Sets the Font that this panel is currently displaying properties of.
   @param f a Font object. The attributes of this font will be displayed in
     the various widgets.
   */
  public void setFormatFont(Font f) {
     if (f==null) {
       if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "null font in UnreadHighlightAgentPanel.setFormatFont!");
       return;
     }
     chkBold.setSelected(f.isBold());
     chkItalic.setSelected(f.isItalic());
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Font size is "+f.getSize());
     tfSize.setText(StringUtils.intToString(f.getSize()));
     cmbFont.setSelectedItem(f.getName());
     panSampleText.repaint();
  }

  /**
   * Retrieves the Font that is configured in the Formatting panel of this
   * panel. If the size field is blank or less than a minimum size, it is
   * automagically set to the minimum font size.
   @return a Font object.
   */
  public Font getFormatFont() {
     int style = Font.PLAIN;
     int size  = Font.PLAIN;
     if (chkBold.isSelected()) style += Font.BOLD;
     if (chkItalic.isSelected()) style += Font.ITALIC;
     String name = (String) cmbFont.getSelectedItem();
     size = StringUtils.stringToInt(tfSize.getText());
     if (size < MIN_FONTSIZE) {
       size = MIN_FONTSIZE;
       tfSize.setText(StringUtils.intToString(size));
     }
     return new Font(name, style, size);
  }

  /**
   * Retrieves the Color that is configured in the Formatting panel of this
   * panel.
   @return a Color object
   */
  public Color getFormatColor() {
   return panColour.getBackground();
  }

  /**
   * Set the Color that is displayed in the Formatting panel of this panel.
   @param c a Color object
   */
  public void setFormatColor(Color c) {
   panColour.setBackground(c);
   panColour.repaint();
   panSampleText.repaint();
  }

  /**
   * Determine whether messages are to be highlighted in all groups or just
   * groups containing a particular substring.
   @return true if just a substring is to be used.
   */
  public boolean isSubstringFilter() {
     return (optNewsgroupsCont.isSelected());
  }

  /**
   * Set whether this panel is displaying a filter.
   @param b true if a filter is to be used for newsgroups that are highlighted
   */
  public void setSubstringFilter(boolean b) {
     optNewsgroupsCont.setSelected(b);
     optAllNewsgroups.setSelected(!b);
     tfGroups.setEnabled(b);
     tfGroups.repaint();
  }

  /**
   * Get the text that newsgroups must contain before they are affected by the
   * formatting.
   @return a string
   */
  public String getNewsgroupText() {
     if (isSubstringFilter())
       return tfGroups.getText();
     return "";
  }

  /**
   * Set the text that newsgroups must contain before they are affected by
   * the formatting. Doesn't affect the radio buttons: use setSubstringFilter
   * to switch newsgroups substring filtering on.
   @param text the text
   */
  public void setNewsgroupText(String text) {
   tfGroups.setText(text);
  }

// private parts

  private void initialiseCombo() {
   cmbFont.setEditable(false);
   cmbFont.addItem("Serif");
   cmbFont.addItem("SansSerif");
   cmbFont.addItem("Monospaced");
   cmbFont.addItem("Dialog");
   cmbFont.addItem("DialogInput");
  }

  private void setDefaults() {
     // set all items to their default values
     setFormatFont(new Font("Dialog", 12, Font.BOLD));
     setFormatColor(UIManager.getColor("textText"));
     setSubstringFilter(false);
  }


  void cmdColour_actionPerformed(ActionEvent e) {
   setFormatColor(JColorChooser.showDialog(this, "Choose a colour...",
     getFormatColor()));
  }

  // Inner class JPanel subclass for sample panel.
  class SampleHeaderPanel extends JPanel {

   protected String sampleText = "Sample Unread Header Text";

   public void paint(Graphics g) {
     int x,y;
     g.setColor(Color.white);
     g.fillRect(0, 0, getSize().width, getSize().height);
     g.setFont(getFormatFont());
     g.setColor(getFormatColor());
     FontMetrics fm = getFontMetrics(g.getFont());
     // Center the text
     x = getSize().width / 2 - fm.stringWidth(sampleText) / 2;
     y = getSize().height / 2 + fm.getHeight() / 2 + fm.getMaxDescent();
     g.drawString(sampleText, x, y);
   }

  }

  /**
   * Inner class for Item events
   */
  class SampleUpdater implements ActionListener {

   public void actionPerformed(ActionEvent e) {
     panSampleText.repaint();
   }

  }

}

class UnreadHighlightAgentPanel_cmdColour_actionAdapter implements java.awt.event.ActionListener {
  UnreadHighlightAgentPanel adaptee;

  UnreadHighlightAgentPanel_cmdColour_actionAdapter(UnreadHighlightAgentPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdColour_actionPerformed(e);
  }
}

//
// Old Version History: <UL>
// 0.1 [28/04/98]: Initial Revision
// 0.2 [29/04/98]: Fixed updating of sample text
// 0.3 [06/06/98]: Added dubh utils import for StringUtils
// 0.4 [31/01/99]: Now using "official" Swing 1.1 colorchooser.
//
// New version history:
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:25:16  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//