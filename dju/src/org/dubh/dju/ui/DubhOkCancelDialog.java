/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
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

package dubh.utils.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import dubh.utils.event.*;
import dubh.utils.misc.ResourceManager;
/**
 * A dialog that can be dismissed using the ok or cancel buttons. You can
 * subclass this class and add a panel to it (the layout manager is
 * BorderLayout, and the two buttons are in the SOUTH location), or just
 * use the setPanel() method with an instance of this class.
 * Version History: <UL>
 * <LI>0.1 [18/06/98]: Initial Revision
 * <LI>0.2 [28/06/98]: Added setPanel()
 * <LI>0.3 [08/12/98]: Internationalised
 * <LI>0.4 [12/12/98]: Add support for validated panel.
 *</UL>
 @author Brian Duff
 @version 0.4 [12/12/98]
 */
public class DubhOkCancelDialog extends DubhDialog {

  protected JButton cmdOk = new JButton();
  protected JButton cmdCancel = new JButton();
  protected JPanel  panButtons = new JPanel();
  protected FlowLayout layoutButtons = new FlowLayout(FlowLayout.RIGHT);
  private   JPanel  m_myPanel;
  
  public static final int s_OK_BUTTON = 0, s_CANCEL_BUTTON = 1;
  
  private ValidationChangeListener m_validationListener = 
     new ValidationChangeListener() {
        public void validationStateChanged(ValidationChangeEvent e)
        {
           cmdOk.setEnabled(e.isValid());
        }
     };

  protected boolean m_isCancelled = true;

  public DubhOkCancelDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    initButtons();
  }

  public DubhOkCancelDialog(Frame frame) {
    this(frame, "", false);
  }

  public DubhOkCancelDialog(Frame frame, boolean modal) {
    this(frame, "", modal);
  }

  public DubhOkCancelDialog(Frame frame, String title) {
    this(frame, title, false);
  }

  private void initButtons() {
     ButtonListener b = new ButtonListener();
     getContentPane().setLayout(new BorderLayout());
     panButtons.setLayout(layoutButtons);
     panButtons.add(cmdOk);
     panButtons.add(cmdCancel);
     panButtons.setName("OKCancelPanel");
     cmdOk.setName("cmdOK");
     cmdCancel.setName("cmdCancel");
     ResourceManager.getManagerFor("dubh.utils.ui.res.DubhOKCancelDialog").initComponents(panButtons);
     getContentPane().add(panButtons, BorderLayout.SOUTH);
     cmdOk.addActionListener(b);
     cmdCancel.addActionListener(b);
     getRootPane().setDefaultButton(cmdOk);
  }

  public JPanel getPanel()
  {
     return m_myPanel;
  }
  
  /**
   * Set the panel that is being displayed in the dialogue.
   */
  public void setPanel(JPanel pan) {

  
     if (m_myPanel instanceof ValidatorPanel)
     {
        ((ValidatorPanel)m_myPanel).removeValidationChangeListener(m_validationListener);
     }
     try
     {
        getContentPane().remove(m_myPanel);
     }
     catch (Throwable t) {}
     m_myPanel = pan;
     getContentPane().add(m_myPanel, BorderLayout.CENTER);
     
     cmdOk.setEnabled(true);
     
     if (m_myPanel instanceof ValidatorPanel)
     {
        cmdOk.setEnabled(((ValidatorPanel)m_myPanel).isValid());
        ((ValidatorPanel)m_myPanel).addValidationChangeListener(m_validationListener);
     }
  }
  
  public void setButtonVisible(int button, boolean visible)
  {
     if (button == s_OK_BUTTON) cmdOk.setVisible(visible);
     if (button == s_CANCEL_BUTTON) cmdCancel.setVisible(visible);
  }
  
  public boolean isButtonVisible(int button)
  {
     if (button == s_OK_BUTTON) return cmdOk.isVisible();
     if (button == s_CANCEL_BUTTON) return cmdCancel.isVisible();
     return false;
  }

  /**
   * Get whether the dialogue was dismissed using the cancel button
   @return true if the user cancelled, false otherwise
   */
  public boolean isCancelled() { return m_isCancelled; }

  /** Set label for the Cancel button */
  public void setCancelText(String text) { cmdCancel.setText(text); }
  /** Get label for the Cancel button */
  public String getCancelText() { return cmdCancel.getText(); }
  /** Set label for the OK button */
  public void setOKText(String text) { cmdOk.setText(text); }
  /** Get label for the OK button */
  public String getOKText() { return cmdOk.getText(); }
  

  class ButtonListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmdOk) {
           setVisible(false);
           m_isCancelled = false;
        } else {
           setVisible(false);
           m_isCancelled = true;
        }
     }
  }
 

}