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
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;


/**
 * This dialogue displays a preview of a message that has been altered by
 * one or more Send Agents. It also contains a checkbox that the user can
 * use to disable further previews. This affects the
 * <b>newsagent.agents.send.alwayspreview</b> user preference, and saves
 * the preference file on dismissal of the dialogue.<P>
 * Version History: <UL>
 * <LI>0.1 [07/06/98]: Initial Revision
 * <LI>0.2 [08/06/98]: Made preview box non-editable, and enabled the buttons.
 @author Brian Duff
 @version 0.1 [07/06/98]
 */
public class SendAgentPreviewDialog extends JDialog {
  private JPanel panMain = new JPanel();
  private GridBagLayout layoutMain = new GridBagLayout();
  private JLabel labExplanation = new JLabel();
  private JTextArea taPreview = new JTextArea();
  private JScrollPane scrollMsgPreview = new JScrollPane(taPreview);
  private JCheckBox cbAlwaysPreview = new JCheckBox();
  private JButton cmdPost = new JButton();
  private JButton cmdCancel = new JButton();

  private boolean m_wasCancelled = true;

  /**
   * Construct a Send Agent Preview Dialogue with the speicifed parent frame
   * and the given message header and body.
   @param parent the parent frame
   @param hd the message header to display in the preview
   @param bd the message body to display in the preview
   */
  public SendAgentPreviewDialog(Frame parent, MessageHeader hd, MessageBody bd) {
    super(parent, "Post Preview", true);
    try {
      jbInit();
      setPreviewMessage(hd, bd);
      getContentPane().add(panMain);
      pack();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    labExplanation.setText("One or more Send Agents has modified your message. This is how it will be sent:");
    cbAlwaysPreview.setText("Always show a preview when my messages have been altered");
    cbAlwaysPreview.setSelected(true);
    cmdPost.setText("Post");
    cmdPost.addActionListener(new PostListener());
    cmdCancel.setText("Cancel");
    cmdCancel.addActionListener(new CancelListener());
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
    panMain.add(cmdPost, new GridBagConstraints2(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 2), 0, 0));
    panMain.add(cmdCancel, new GridBagConstraints2(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
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
     return !m_wasCancelled;
  }

// PRIVATE IMPLEMENTATION

  private void checkAlwaysPreview() {
     if (!cbAlwaysPreview.isSelected()) {
        GlobalState.setPreference("newsagent.agents.send.alwayspreview", false);
        GlobalState.savePreferences();
     }
  }

// EVENT HANDLING

  class PostListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        m_wasCancelled = false;
        checkAlwaysPreview();
        setVisible(false);
     }
  }

  class CancelListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        m_wasCancelled = true;
        checkAlwaysPreview();
        setVisible(false);
     }
  }
}