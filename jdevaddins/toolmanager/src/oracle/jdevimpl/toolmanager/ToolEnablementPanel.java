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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

public class ToolEnablementPanel extends JPanel 
{
  GridBagLayout gblEnablementPanel = new GridBagLayout();
  JLabel labHint = new JLabel();
  JRadioButton rbAlways = new JRadioButton();
  JRadioButton rbActiveNode = new JRadioButton();
  JRadioButton rbRegularExpression = new JRadioButton();
  JTextField tfRegularExpression = new JTextField();

  public ToolEnablementPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    ButtonGroup bg = new ButtonGroup();
    bg.add( rbAlways );
    bg.add( rbActiveNode );
    bg.add( rbRegularExpression );

    ActionListener al = 
      new ActionListener() {
        public void actionPerformed( ActionEvent e )
        {
          tfRegularExpression.setEnabled( rbRegularExpression.isSelected() );
        }
      };
    rbAlways.addActionListener( al );
    rbActiveNode.addActionListener( al );
    rbRegularExpression.addActionListener( al );
  }

  private void jbInit() throws Exception
  {
    this.setLayout(gblEnablementPanel);
    labHint.setText(
      ToolManagerArb.getString(
        ToolManagerArb.CHOOSE_WHEN_ENABLED_LABEL
      )
    );
    rbAlways.setText(
      ToolManagerArb.getString(
        ToolManagerArb.ALWAYS_RADIO_LABEL
      )      
    );
    rbAlways.setMnemonic(
      ToolManagerArb.getMnemonic(
        ToolManagerArb.ALWAYS_RADIO_MNEMONIC
      )   
    );
    rbActiveNode.setText(
      ToolManagerArb.getString(
        ToolManagerArb.ACTIVE_NODE_RADIO_LABEL
      )    
    );
    rbActiveNode.setMnemonic(
      ToolManagerArb.getMnemonic(
        ToolManagerArb.ACTIVE_NODE_RADIO_MNEMONIC
      )   
    );
    rbRegularExpression.setText(
      ToolManagerArb.getString(
        ToolManagerArb.REGEXP_RADIO_LABEL
      )  
    );
    rbRegularExpression.setMnemonic(
      ToolManagerArb.getMnemonic(
        ToolManagerArb.REGEXP_RADIO_MNEMONIC
      )   
    );
    this.add(labHint, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 10, 2), 0, 0));
    this.add(rbAlways, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 2), 0, 0));
    this.add(rbActiveNode, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 15, 0, 2), 0, 0));
    this.add(rbRegularExpression, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 15, 0, 2), 0, 0));
    this.add(tfRegularExpression, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 30, 2, 2), 0, 0));
  }

  void saveToTool( final Tool t )
  {
    if ( rbAlways.isSelected() )
    {
      t.setEnableType( t.ET_ALWAYS_ENABLED );
    }
    else if ( rbActiveNode.isSelected() )
    {
      t.setEnableType( t.ET_ENABLED_ON_NODE );
    }
    else
    {
      t.setEnableType( t.ET_ENABLED_ON_REGEXP );

    }
    t.setEnableRegularExpression( tfRegularExpression.getText() );
  }

  void populateFromTool( final Tool t )
  {
    if ( t == null )
    {
      rbAlways.setSelected( false );
      rbActiveNode.setSelected( false );
      rbRegularExpression.setSelected( false );
    }
    else
    {
      int type = t.getEnableType();
      if ( type == t.ET_ALWAYS_ENABLED )
      {
        rbAlways.setSelected( true );
      }
      else if ( type == t.ET_ENABLED_ON_NODE )
      {
        rbActiveNode.setSelected( true );
      }
      else
      {
        rbRegularExpression.setSelected( true );
        tfRegularExpression.setEnabled( true );
      }

      if ( t.getEnableRegularExpression() != null )
      {
        tfRegularExpression.setText( t.getEnableRegularExpression() );
      }
      else
      {
        tfRegularExpression.setText( "" );
      }
    }

  }

  boolean validateTool( final Tool t )
  {

    // If regular expression is selected...
    if ( t.getEnableType() == t.ET_ENABLED_ON_REGEXP )
    {
      if ( t.getEnableRegularExpression() == null || 
        t.getEnableRegularExpression().trim().length() == 0 )
      {
        tfRegularExpression.requestFocus();
        ToolPanel.alert( this, 
          ToolManagerArb.format(
            ToolManagerArb.MUST_ENTER_A_REGEXP,
            t.getTitle()
          )
        );
        return false;
      }

      // Check its actually a valid regular expression
      try
      {
        RE regexp = new RE( t.getEnableRegularExpression() );
      }
      catch ( RESyntaxException resyntax )
      {
        tfRegularExpression.requestFocus();
        tfRegularExpression.selectAll();

        ToolPanel.alert( this,
          ToolManagerArb.format(
            ToolManagerArb.BAD_REGEXP,
            new Object[] { t.getTitle(), resyntax.getMessage() }
          )
        );
        return false;
      }
    }

    

    return true;
  }

  void setControlsEnabled( final boolean enabled )
  {
    labHint.setEnabled( enabled );
    rbActiveNode.setEnabled( enabled );
    rbAlways.setEnabled( enabled );
    rbRegularExpression.setEnabled( enabled );
    tfRegularExpression.setEnabled( enabled && rbRegularExpression.isSelected() );
  }
  
}