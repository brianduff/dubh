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
package dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dubh.apps.newsagent.dialog.NDialog;
import dubh.apps.newsagent.GlobalState;


/**
 * Configuration Dialogue for Agents. Consists of a Panel (which can be set to
 * anything you like) and two buttons, OK and Cancel. <P>
 * To use the dialog, construct it with an agent configuration panel and
 * call the showAtCentre method. It will block modally until the user clicks on OK
 * or cancel. Upon exit, call getPanel to retrive the panel and isCancelled
 * to determine whether the user cancelled. Finally, remember to dispose the
 * dialogue.<P>
 * Version History: <UL>
 * <LI>0.1 [28/04/98]: Initial Revision
 * <LI>0.2 [04/05/98]: Fixed labels on Ok and Cancel buttons.
 *</UL>
 @author Brian Duff
 @version 0.2 [04/04/98]
 */
public class AgentConfigDialogue extends NDialog {

// Private instance variables

  private JPanel   panUser;
  private JPanel   panButtons;
  private JButton  cmdOK;
  private JButton  cmdCancel;
  private boolean  m_didCancel;

  /**
   * Construct a new AgentConfigDialogue.
   @param parent the Parent frame
   @param panel the configuration panel to use
   @param title the title for the dialog
   */
  public AgentConfigDialogue(Frame parent, JPanel panel, String title) {
    super(parent, title, true);
    // Construct the UI
    getContentPane().setLayout(new BorderLayout());
    panButtons = new JPanel();
    panButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
    cmdOK = new JButton(GlobalState.getResString("GeneralOK"));
    getRootPane().setDefaultButton(cmdOK);
    OKCancelListener listener = new OKCancelListener();
    cmdOK.setActionCommand("ok");
    cmdOK.addActionListener(listener);
    cmdCancel = new JButton(GlobalState.getResString("GeneralCancel"));
    cmdCancel.setActionCommand("cancel");
    cmdCancel.addActionListener(listener);
    panButtons.add(cmdOK);
    panButtons.add(cmdCancel);
    getContentPane().add(panButtons, BorderLayout.SOUTH);
    panUser = panel;
    getContentPane().add(panUser,    BorderLayout.CENTER);
  }

  /**
   * Determine if the user cancelled the dialog.
   @return true if the cancel button was used to close the dialog, false
     otherwise.
   */
  public boolean isCancelled() {
   return m_didCancel;
  }

  /**
   * Inner class for button events
   */
  class OKCancelListener implements ActionListener {

     public void actionPerformed(ActionEvent e) {
       if (e.getActionCommand().equals("ok"))
         m_didCancel = false;
       else
         m_didCancel = true;
       setVisible(false);
     }
     
  }
  
}