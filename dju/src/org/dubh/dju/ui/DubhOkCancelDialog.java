// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhOkCancelDialog.java,v 1.5 1999-11-11 21:24:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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

package org.javalobby.dju.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import org.javalobby.dju.event.*;
import org.javalobby.dju.misc.ResourceManager;

import org.javalobby.dju.error.ErrorManager;

/**
 * A dialog that can be dismissed using the ok or cancel buttons. You can
 * subclass this class and add a panel to it (the layout manager is
 * BorderLayout, and the two buttons are in the SOUTH location), or just
 * use the setPanel() method with an instance of this class.
 * @author Brian Duff
 * @version $Id: DubhOkCancelDialog.java,v 1.5 1999-11-11 21:24:35 briand Exp $
 */
public class DubhOkCancelDialog extends DubhDialog 
{

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
     ResourceManager.getManagerFor("org.javalobby.dju.ui.res.DubhOKCancelDialog").initComponents(panButtons);
     getContentPane().add(panButtons, BorderLayout.SOUTH);
     cmdOk.addActionListener(b);
     cmdCancel.addActionListener(b);
     getRootPane().setDefaultButton(cmdOk);
  }

  public JPanel getPanel()
  {  
     if (m_myPanel == null) setPanel(new JPanel());
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
  

   /**
    * Override this method in a subclass to do extra work before
    * the dialog is dismissed. If you return false, the dialog
    * will not be dismissed. If you are using validator
    * panels, make sure you call super.okClicked() and only
    * do your additional work if it returns true.
    */
   public boolean okClicked()
   {      
      if (getPanel() instanceof ValidatorPanel)
      {
         Vector messageValidators = ((ValidatorPanel)getPanel()).getMessageValidators();
         for (int i=0; i < messageValidators.size(); i++)
         {
            ValidatorPanel.MessageValidator mv = (ValidatorPanel.MessageValidator)messageValidators.elementAt(i);
            
            if (!mv.isValid())
            {
               Object[] subst = mv.getSubstitutions();
               if (subst != null)
               {
                  ErrorManager.display(getPanel(), mv.getErrorMessage(), subst);
                  return false;
               }
               else
               {
                  ErrorManager.display(getPanel(), mv.getErrorMessage());
                  return false;               
               }
            }
         }
      }
      return true;
   }
   /**
    * Override this method in a subclass to do extra work before
    * the dialog is dismissed. If you return false, the dialog
    * will not be dismissed.
    */   
   public boolean cancelClicked()
   {
      return true;   
   }

  class ButtonListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmdOk) {
           if (okClicked())
           {
              setVisible(false);
              m_isCancelled = false;
           }
        } else {
           if (cancelClicked())
           {
              setVisible(false);
              m_isCancelled = true;
           }
        }
     }
  }
 

}



/*
 * Version History: <UL>
 * <LI>0.1 [18/06/98]: Initial Revision
 * <LI>0.2 [28/06/98]: Added setPanel()
 * <LI>0.3 [08/12/98]: Internationalised
 * <LI>0.4 [12/12/98]: Add support for validated panel.
 *</UL>
 */


// 
// $Log: not supported by cvs2svn $
// Revision 1.4  1999/11/02 19:53:14  briand
// Commit changes before move to JFA.
//
//