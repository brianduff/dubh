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
 * The Original Code is Filesystem Browser addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.fsbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.ide.Ide;

import oracle.bali.ewt.dialog.JEWTDialog;

/**
 * Simple UI panel that contains a prompt for a name for a new folder
 *
 * @author Brian.Duff@oracle.com
 */
public final class NewFolderPrompt extends JPanel 
  implements DocumentListener
{
  private JTextField m_field;
  private JLabel m_label;
  private JEWTDialog m_dialog;

  private static Dimension m_fieldDimension;

  public NewFolderPrompt()
  {
    m_field = new JTextField();
    m_label = new JLabel( "Package or Directory Name:" );
    m_label.setDisplayedMnemonic( 'P' );
    m_label.setLabelFor( m_field );
    
    setLayout( new BorderLayout( 4, 4 ) );
    add( m_field, BorderLayout.SOUTH );
    add( m_label, BorderLayout.NORTH );
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

  /**
   * Show the panel in a dialog, parented on the IDE. You can call
   * getText() to get the entered text after calling. 
   *
   * @returns true if the user clicked OK, false if Cancel.
   */
  public boolean showDialog()
  {
    m_dialog = JEWTDialog.createDialog(
      Ide.getMainWindow(), "Create Package or Directory", 
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );
    m_dialog.setContent( this );
    m_field.setText( "" );
    m_dialog.setOKButtonEnabled( false );

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

// DocumentListener interface

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