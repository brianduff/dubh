// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: WizardDialog.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.dubh.dju.ui.GridBagConstraints2;
import java.util.*;


/**
 * A WizardDialog displays an MS Windows-like Wizard, consisting of a number
 * of ordered configuration panels, back, forward cancel and (optionally)
 * help buttons at the bottom and an (optional) image to the left.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [09/05/98]: Initial Revision
 * </UL>
 @author Brian Duff
 */
public class WizardDialog extends DubhDialog {
  Panel panMain = new Panel();
  EtchedLine etchedLine = new EtchedLine();
  GridBagLayout layout = new GridBagLayout();
  JLabel labPicture = new JLabel();
  JPanel panWizard = new JPanel();
  JPanel panButtons = new JPanel();
  GridBagLayout layoutButtons = new GridBagLayout();
  JButton cmdBack = new JButton();
  JButton cmdNext = new JButton();
  JButton cmdCancel = new JButton();
  JButton cmdHelp = new JButton();
  CardLayout layoutWiz = new CardLayout();
  JPanel[] m_panels;
  int m_panelshowing = 0;
  boolean m_isCancelled = false;;
  boolean m_locked = false;
  Vector m_listeners = new Vector();

  public static final String ACTION_HELP = "help";
  public static final String ACTION_NEXT = "next";

  public WizardDialog(Frame frame, String title, JPanel[] panels) {
    super(frame, title, true);
    try {
      m_panels = panels;
      jbInit();
      getContentPane().add(panMain);
      pack();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
     labPicture.setVisible(false);
     labPicture.setBorder(BorderFactory.createLoweredBevelBorder());
     panWizard.setLayout(layoutWiz);
     for (int i=0; i < m_panels.length; i++) {
        panWizard.add(m_panels[i], Integer.toString(i));
     }
     layoutWiz.first(panWizard);
    cmdBack.setText("< Back");
    cmdBack.setEnabled(false);
    cmdBack.addActionListener(new BackAdapter());
    if (m_panels.length > 1)
     cmdNext.setText("Next >");
    else
     cmdNext.setText("Finish");
    getRootPane().setDefaultButton(cmdNext);
    cmdNext.addActionListener(new NextAdapter());
    cmdCancel.setText("Cancel");
    cmdCancel.addActionListener(new CancelAdapter());
    cmdHelp.setText("Help");
    cmdHelp.addActionListener(new HelpAdapter());
    panButtons.setLayout(layoutButtons);
    panMain.setLayout(layout);
    panMain.add(labPicture, new GridBagConstraints2(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 0), 0, 0));
    panMain.add(panWizard, new GridBagConstraints2(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panMain.add(etchedLine, new GridBagConstraints2(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panMain.add(panButtons, new GridBagConstraints2(0, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panButtons.add(cmdBack, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panButtons.add(cmdNext, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panButtons.add(cmdCancel, new GridBagConstraints2(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    panButtons.add(cmdHelp, new GridBagConstraints2(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

  }

  /**
   * Set the picture to use on the left of the frame. At the moment, the
   * picture remains the same throughout the wizard's progress. Maybe in the
   * future, you will be able to change the image as the wizard progresses.
   * Automatically sets the picture to be visible.
   @param picture a Swing Icon object to display
   */
  public void setPicture(Icon picture) {
     labPicture.setMinimumSize(new Dimension(picture.getIconWidth(), picture.getIconHeight()));
     labPicture.setPreferredSize(new Dimension(picture.getIconWidth(), picture.getIconHeight()));
     labPicture.setIcon(picture);
     setPictureVisible(true);
     pack();
  }

  /**
   * Set the picture to be visible or invisible
   */
  public void setPictureVisible(boolean visible) {
     labPicture.setVisible(visible);
     panMain.validate();
     repaint();
  }

  /**
   * Determine whether the picture is visible
   @return true if the picture is visible
   */
  public boolean isPictureVisible() {
     return labPicture.isVisible();
  }

  /**
   * Determine whether this dialogue was cancelled. It only really makes sense
   * to call this after the dialogue has been displayed.
   @return true if the user cancelled the dialogue.
   */
  public boolean isCancelled() {
     return m_isCancelled;
  }

  /**
   * Set whether this dialog displays a "Help" button. The help button <b>is</b>
   * displayed by default.
   @param display whether to display
   */
  public void setHelpButtonVisible(boolean display) {
     cmdHelp.setVisible(display);
     panButtons.validate();
     panButtons.repaint();
  }

  /**
   * Determine whether the help button is visible.
   @return true if the help button is visible.
   */
  public boolean isHelpButtonVisible() {
     return cmdHelp.isVisible();
  }

  /**
   * Programmatically force the dialog to move forward. It won't go any
   * further forward than the last panel. This method fires ACTION_NEXT
   * action events, and is ignored if the wizard has been locked using
   * setLocked().
   */
  public void moveForward() {
     fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
        ACTION_NEXT));
     if (!m_locked) {
        if (m_panelshowing == 0 && m_panels.length > 1)
           cmdBack.setEnabled(true);
        if (m_panelshowing >= m_panels.length - 1) {
           // on the last page: dismiss the dialogue.
           m_isCancelled = false;
           setVisible(false);
        } else {
           // for all panels but the last.
           layoutWiz.next(panWizard);
           m_panelshowing++;
           // if we've moved to the last panel, change the text
           if (m_panelshowing == m_panels.length -1) cmdNext.setText("Finish");
        }
     }
  }

  /**
   * Programmatically force the dialog to move backward. It won't go any
   * further back than the first panel.
   */
  public void moveBack() {
     if (m_panelshowing>0) {
        // If we've moved back from the final page, reset the next button
        if (m_panelshowing == m_panels.length - 1)
           cmdNext.setText("Next >");
        layoutWiz.previous(panWizard);
        m_panelshowing--;
        if (m_panelshowing==0) cmdBack.setEnabled(false);
     }
  }

  /**
   * Tells the dialog that it can't move on to the next step for some reason.
   * You might want to use this if you are validating panels as the user
   * clicks next, and the current panel is invalid. If you set this inside
   * a next action handler, the 'next' operation will be aborted. <b>Remember
   * to unlock the dialog again</b> or the user will never be able to progress.
   * You should always explain to the user why they can't move on before
   * locking the wizard. Nb. The cancel option is never locked.
   @param locked if true, the next button will still fire events, but will
     not move on to the next panel.
   */
  public synchronized void setLocked(boolean locked) {
     m_locked = locked;
  }

  /**
   * Get whether this wizard is locked
   @return true if the wizard is ignoring "Next" requests.
   */
  public boolean isLocked() {
     return m_locked;
  }

  /**
   * Gets the current panel index.
   @return an integer value, corresponding to the index in your original
     array of panels.
   */
  public int getCurrentPanelIndex() {
     return m_panelshowing;
  }

  /**
   * Add an action listener to this wizard. The wizard passes on two types of
   * events: clicks on the Next button and clicks on the Help button. You can
   * use getCurrentPanelIndex() to determine which panel is displayed (this
   * will be the <i>current</i> panel, not the panel the wizard is about to
   * move on to). If you are unhappy with its contents in some way (i.e. the
   * user has entered invalid data), display an alert to the user, then call
   * the setLocked() method to stop the wizard from progressing onto the next
   * step. <B>Take care to unlock the wizard</b> when the user has filled
   * in the form properly.<P>
   * The action command is ACTION_HELP or ACTION_NEXT.
   */
  public void addActionListener(ActionListener l) {
     m_listeners.addElement(l);
  }

  /**
   * Remove an action listener
   @see addActionListener
   */
  public void removeActionListener(ActionListener l) {
     m_listeners.removeElement(l);
  }

  /**
   * Fire an action event. The action command should be ACTION_HELP or
   * ACTION_NEXT.
   @param e an ActionEvent
   */
  public void fireActionEvent(ActionEvent e) {
     Vector list = (Vector) m_listeners.clone();
     for (int i = 0; i < list.size(); i++) {
        ActionListener listener = (ActionListener)list.elementAt(i);
        listener.actionPerformed(e);
     }
  }

/******************
 * EVENT HANDLING *
 ******************/

  class BackAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        moveBack();
     }
  }

  class NextAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        moveForward();
     }
  }

  class CancelAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        m_isCancelled = true;
        setVisible(false);
     }
  }

  class HelpAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        fireActionEvent(new ActionEvent(WizardDialog.this,
           ActionEvent.ACTION_PERFORMED, ACTION_HELP));
     }
  }


  class EtchedLine extends JPanel {

     public EtchedLine() {
     }

     public Dimension getPreferredSize() {
        return getMinimumSize();
     }

     public Dimension getMinimumSize() {
        return new Dimension(0, 2);
     }

     public void paint(Graphics g) {
        g.setColor(SystemColor.controlLtHighlight);
        g.drawLine(0,1,getSize().width, 1);
        g.setColor(SystemColor.controlDkShadow);
        g.drawLine(0,0,getSize().width, 0);
     }
  }

  public static void main(String[] args) {
  /*   Vector warble = new Vector();
     warble.addElement("Hello");
     warble.addElement("Now is the time for all wah wah");
     warble.addElement("Test test test");
     final VectorList[] panels = { new VectorList(warble), new VectorList(warble),
        new VectorList(warble)};

     final WizardDialog test = new WizardDialog(new Frame(), "Test Wizard", panels);
     test.setPicture(new ImageIcon(GlobalState.getImage("wizard.gif")));
     test.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
           if (e.getActionCommand() == WizardDialog.ACTION_NEXT) {
              if (test.getCurrentPanelIndex() == 1) {
                 if (panels[1].getListContents().length > 0) {
                    test.setLocked(true);
                    System.err.println("Locking panel 2 until it is empty.");
                 } else {
                    test.setLocked(false);
                    System.err.println("Unlocked panel 2");
                 }
              }
           }
        }
     });
     test.setVisible(true);       */
     JPanel testPanel = new JPanel();
     testPanel.setLayout(new BorderLayout());
     MultiLineJLabel testLabel = new MultiLineJLabel();
     testLabel.setText("Now is the time for\nAll good men to\ncome to the aid");
     testPanel.add(testLabel, BorderLayout.CENTER);
     JPanel[] panels = { testPanel };
     WizardDialog dlg = new WizardDialog(new Frame(), "Test Wizard", panels);
     dlg.setVisible(true);
  }

}