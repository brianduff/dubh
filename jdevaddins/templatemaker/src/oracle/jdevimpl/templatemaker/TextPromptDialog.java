/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is TemplateMaker addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.templatemaker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.ide.Ide;

import oracle.bali.ewt.dialog.JEWTDialog;
/**
 * Simple dialog that prompts for some text. Slightly neater than the
 * JOptionPane input dialog, and validates its OK button properly...
 *
 * @author Brian.Duff@oracle.com
 */
final class TextPromptDialog extends JPanel 
  implements DocumentListener
{
  private final JTextField m_field;
  private final JLabel m_label;
  private JEWTDialog m_dialog;
  private String m_dialogTitle;

  private static Dimension m_fieldDimension;

  TextPromptDialog()
  {
    m_field = new JTextField();
    m_label = new JLabel( "Enter a Value:" );
    m_label.setDisplayedMnemonic( 'E' );
    m_label.setLabelFor( m_field );
    m_dialogTitle = "Enter a Value";
    
    setLayout( new BorderLayout( 4, 4 ) );
    add( m_field, BorderLayout.SOUTH );
    add( m_label, BorderLayout.NORTH );    
  }

  /**
   * Set the prompt
   *
   * @param prompt the prompt to display
   */
  public void setPrompt( final String prompt )
  {
    m_label.setText( prompt );
  }

  /**
   * Get the prompt text
   */
  public String getPrompt( )
  {
    return m_label.getText();
  }

  /**
   * Set the prompt mnemonic
   */
  public void setPromptMnemonic( int mnmemonic )
  {
    m_label.setDisplayedMnemonic( mnmemonic );
  }

  /**
   * Get the prompt mnemonic
   */
  public int getPromptMnemonic()
  {
    return m_label.getDisplayedMnemonic();
  }

  /**
   * Set the text
   */
  public void setText( final String text )
  {
    m_field.setText( text );
  }

  /**
   * Get the text entered by the user
   *
   * @return the text entered by the user.
   */
  public String getText()
  {
    return m_field.getText();
  }

  public void setDialogTitle( final String dialogTitle )
  {
    m_dialogTitle = dialogTitle;
  }

  public String getDialogTitle()
  {
    return m_dialogTitle;
  }

  /**
   * Show the panel in a dialog, parented on the IDE. You can call
   * getText() to get the entered text after calling. 
   *
   * @param parent a parent component for modality
   * @returns true if the user clicked OK, false if Cancel.
   */
  public boolean showDialog( Component parent )
  {
    m_dialog = JEWTDialog.createDialog(
      parent, m_dialogTitle, 
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );
    m_dialog.setContent( this );
    m_dialog.setOKButtonEnabled( (m_field.getText().trim().length() > 0) );

    if ( m_fieldDimension == null )
    {
      m_fieldDimension = 
        new Dimension( 300, m_field.getPreferredSize().height );
    }
    m_field.setPreferredSize( m_fieldDimension );

    m_dialog.setInitialFocus( m_field );
    m_field.getDocument().addDocumentListener( this );
    boolean ok =  m_dialog.runDialog();    
    m_field.getDocument().removeDocumentListener( this );

    return ok;
  }

  private void fieldChanged()
  {
    m_dialog.setOKButtonEnabled( (m_field.getText().trim().length() > 0) );
  }

// ----------------------------------------------------------------------------
// DocumentListener interface
// ----------------------------------------------------------------------------


  public void changedUpdate( DocumentEvent de )
  {
    fieldChanged();
  }

  public void insertUpdate( DocumentEvent de )
  {
    fieldChanged();
  }

  public void removeUpdate( DocumentEvent de )
  {
    fieldChanged();
  }

}