// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: SendAgentPreviewDialog.java,v 1.4 1999-06-01 00:25:16 briand Exp $
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
package dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.PreferenceKeys;
import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;

import dubh.utils.ui.DubhOkCancelDialog;
import dubh.utils.misc.UserPreferences;
import dubh.utils.misc.Debug;

/**
 * This dialog displays a preview of a message that has been altered by
 * one or more Send Agents. It also contains a checkbox that the user can
 * use to disable further previews. This affects the
 * <b>newsagent.agents.send.alwayspreview</b> user preference, and saves
 * the preference file on dismissal of the dialogue.
 *
 * @author Brian Duff
 * @version $Id: SendAgentPreviewDialog.java,v 1.4 1999-06-01 00:25:16 briand Exp $
 */
public class SendAgentPreviewDialog extends DubhOkCancelDialog {
   private JPanel panMain = new JPanel();
   private GridBagLayout layoutMain = new GridBagLayout();
   private JLabel labExplanation = new JLabel();
   private JTextArea taPreview = new JTextArea();
   private JScrollPane scrollMsgPreview = new JScrollPane(taPreview);
   private JCheckBox cbAlwaysPreview = new JCheckBox();
   
   private boolean m_wasCancelled = true;
   
   /**
   * Construct a Send Agent Preview Dialogue with the speicifed parent frame
   * and the given message header and body.
   @param parent the parent frame
   @param hd the message header to display in the preview
   @param bd the message body to display in the preview
   */
   public SendAgentPreviewDialog(Frame parent, MessageHeader hd, MessageBody bd) {
      super(parent, GlobalState.getRes().getString("SendAgentPreviewDialog.SendAgentPreviewDialog"), true);
      init();
      setPreviewMessage(hd, bd);
   }

   private void init()
   {
      panMain.setName("MainPanel");
      labExplanation.setName("ExplanationLabel");
      cbAlwaysPreview.setName("AlwaysPreview");
      cbAlwaysPreview.setSelected(true);
      setOKText(GlobalState.getRes().getString("SendAgentPreviewDialog.PostButton.text"));
      panMain.setLayout(layoutMain);
      taPreview.setColumns(80);
      taPreview.setLineWrap(true);
      taPreview.setWrapStyleWord(true);
      taPreview.setEditable(false);
      panMain.add(labExplanation, new GridBagConstraints2(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      panMain.add(scrollMsgPreview, new GridBagConstraints2(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      panMain.add(cbAlwaysPreview, new GridBagConstraints2(0, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      
      setPanel(panMain);
      
      GlobalState.getRes().initComponents(panMain);
   }

  /**
   * Set the message that will be displayed in the preview dialogue.
   @param hd The header of the message
   @param bd The body of the message
   */
  public void setPreviewMessage(MessageHeader hd, MessageBody bd) {
     taPreview.setText("");
     taPreview.append(hd.toString());
     taPreview.append("---\n");
     taPreview.append(bd.toString());
     // Go to the start of the text
     taPreview.setSelectionStart(0);
     taPreview.setSelectionEnd(0);
  }

  /**
   * Determine whether the dialogue was cancelled.
   @return true if it is still ok to post the message
   */
  public boolean isPostingOk() {
     return !isCancelled();
  }

// PRIVATE IMPLEMENTATION

  private void checkAlwaysPreview() {
     UserPreferences prefs = GlobalState.getPreferences();
     if (!cbAlwaysPreview.isSelected()) {
        prefs.setBoolPreference(PreferenceKeys.AGENTS_SEND_ALWAYSPREVIEW, false);
        try
        {
           prefs.save();
        }
        catch (java.io.IOException ioe)
        {
           if (Debug.TRACE_LEVEL_1)
           {
              Debug.println(1, this, "Unable to save preferences: "+ioe);
           }
        }
     }
  }
  
  public boolean okClicked()
  {
     checkAlwaysPreview();
     return true;
  }
  
  public boolean cancelClicked()
  {
     checkAlwaysPreview();
     return true;
  }
}

//
// Old history:
// <LI>0.1 [07/06/98]: Initial Revision
// <LI>0.2 [08/06/98]: Made preview box non-editable, and enabled the buttons.
//
// New history:
// $Log: not supported by cvs2svn $