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


package oracle.jdevimpl.toolmanager;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;

public class ToolInvocationPanel extends JPanel 
{
  GridBagLayout gblInvocationPanel = new GridBagLayout();
  JLabel labHint = new JLabel();
  JCheckBox cbToolsMenu = new JCheckBox();
  JCheckBox cbNavContextMenu = new JCheckBox();
  JCheckBox cbCodeEditorContext = new JCheckBox();
  JCheckBox cbToolbar = new JCheckBox();

  private ToolManager m_manager;

  public ToolInvocationPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }



  void setToolManager( ToolManager mgr )
  {
    m_manager = mgr;
  }

  void saveToTool( final Tool t )
  {
    t.setToolsMenuItem( cbToolsMenu.isSelected() );
    t.setToolbarButton( cbToolbar.isSelected() );
    t.setContextMenuItem( cbNavContextMenu.isSelected() );
    t.setCodeEditorContextMenuItem( cbCodeEditorContext.isSelected() );
  }

  void populateFromTool( final Tool t )
  {
    if ( t == null )
    {
      cbToolsMenu.setSelected( false );
      cbToolbar.setSelected( false );
      cbNavContextMenu.setSelected( false );
      cbCodeEditorContext.setSelected( false );
    }
    else
    {
      cbToolsMenu.setSelected( t.isToolsMenuItem() );
      cbToolbar.setSelected( t.isToolbarButton() );
      cbNavContextMenu.setSelected( t.isContextMenuItem() );
      cbCodeEditorContext.setSelected( t.isCodeEditorContextMenuItem() );
    }

  }

  boolean validateTool( final Tool t )
  {

    // At least one item must be selected.
    if ( !t.isCodeEditorContextMenuItem() && !t.isToolsMenuItem() &&
         !t.isContextMenuItem() && !t.isToolbarButton() )
    {
      ToolPanel.alert( this, 
        ToolManagerArb.format(
          ToolManagerArb.MUST_SELECT_A_DISPLAY,
          t.getTitle()
        )
      );
      return false;
    }
    

    return true;
  }

  void setControlsEnabled( final boolean enabled )
  {
    cbCodeEditorContext.setEnabled( enabled );
    cbNavContextMenu.setEnabled( enabled );
    cbToolbar.setEnabled( enabled );
    cbToolsMenu.setEnabled( enabled );
    labHint.setEnabled( enabled );
  }



  private void jbInit() throws Exception
  {
    this.setLayout(gblInvocationPanel);
    labHint.setText(
      ToolManagerArb.getString( ToolManagerArb.WHERE_INVOKE_FROM_LABEL )
    );
    cbToolsMenu.setText(
      ToolManagerArb.getString( ToolManagerArb.TOOLS_MENU_CHECKBOX_LABEL )      
    );
    cbToolsMenu.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.TOOLS_MENU_CHECKBOX_MNEMONIC )
    );
    cbNavContextMenu.setText(
      ToolManagerArb.getString( ToolManagerArb.NAV_CTX_CHECKBOX_LABEL )         
    );
    cbNavContextMenu.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.NAV_CTX_CHECKBOX_MNEMONIC )
    );
    cbCodeEditorContext.setText(
      ToolManagerArb.getString( ToolManagerArb.CODE_CTX_CHECKBOX_LABEL )         
    );
    cbCodeEditorContext.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.CODE_CTX_CHECKBOX_MNEMONIC )
    );      
    cbToolbar.setText(
      ToolManagerArb.getString( ToolManagerArb.TOOLBAR_CHECKBOX_LABEL )         
    );
    cbToolbar.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.TOOLBAR_CHECKBOX_MNEMONIC )
    );      
    this.add(labHint, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 10, 2), 0, 0));
    this.add(cbToolsMenu, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 2), 0, 0));
    this.add(cbNavContextMenu, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 15, 0, 2), 0, 0));
    this.add(cbCodeEditorContext, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 15, 0, 2), 0, 0));
    this.add(cbToolbar, new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 15, 2, 2), 0, 0));
  }
}