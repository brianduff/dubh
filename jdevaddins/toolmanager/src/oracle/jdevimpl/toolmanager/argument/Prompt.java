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
 * The Original Code is Oracle9i JDeveloper release 9.0.3
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */


package oracle.jdevimpl.toolmanager.argument;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.Ide;
import oracle.ide.addin.Context;

import oracle.jdevimpl.toolmanager.ToolArgument;
import oracle.jdevimpl.toolmanager.ToolManagerArb;

/**
 * A tool argument which prompts the user for a string value to pass in to
 * the tool
 *
 * @author Brian.Duff@oracle.com
 */
public class Prompt implements ToolArgument
{

  public String toString()
  {
    return getName();
  }

  public String getName()
  {
    return
      ToolManagerArb.getString( ToolManagerArb.PROMPT_NAME );
  }

  public String getDescription()
  {
    return
      ToolManagerArb.getString( ToolManagerArb.PROMPT_DESCRIPTION );
  }

  public String getValue( Context context )
  {
    final JPanel panel = new JPanel();
    final JLabel label = new JLabel();
    final JTextField field = new JTextField();
    panel.setLayout( new BorderLayout() );
    label.setText(
      ToolManagerArb.getString( ToolManagerArb.PROMPT_DIALOG_LABEL_TEXT )
    );
    label.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.PROMPT_DIALOG_LABEL_MNEMONIC )
    );
    label.setLabelFor( field );
    field.setPreferredSize( new Dimension( 300, field.getPreferredSize().height ) );

    panel.add( label, BorderLayout.NORTH );
    panel.add( field, BorderLayout.CENTER );

    final JEWTDialog jd = JEWTDialog.createDialog(
      Ide.getMainWindow(),
      ToolManagerArb.getString( ToolManagerArb.PROMPT_DIALOG_TITLE ),
      JEWTDialog.BUTTON_OK
    );
    jd.setContent( panel );

    jd.runDialog();
    return field.getText();
  }

  public String getMoniker()
  {
    return "prompt";   // NOTRANS
  }

  public boolean isDirectoryArgument()
  {
    return false;
  }  
}