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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import oracle.jdeveloper.layout.VerticalFlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.Icon;
import javax.swing.JTabbedPane;



import oracle.ide.Ide;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.resource.IdeIcons;


/**
 * A panel which can be used to edit the external tools available in the IDE
 * tools menu.
 *
 * @author Brian.Duff@oracle.com
 */
final class ToolPanel extends JPanel
{



  private GridBagLayout gbl = new GridBagLayout();
  private JLabel cmdLabel = new JLabel();
  private JScrollPane cmdScrollPane = new JScrollPane();
  private JList cmdList = new JList();
  private BarSeparator topBar = new BarSeparator();
//  private JLabel lblDescription = new JLabel();
//  private JTextField txtDescription = new JTextField();
  private BarSeparator bottomBar = new BarSeparator();
  private JPanel resizeButtonPanel = new JPanel();
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  private JButton btnMoveUp = new JButton();
  private JButton btnMoveDown = new JButton();
  private JButton btnRemoveCommand = new JButton();
  private JPanel paddingPanel = new JPanel();
  private JButton btnAddCommand = new JButton();

  private ToolManager m_manager;

  private Tool m_selectedTool;


  private JTabbedPane m_tabbedPane = new JTabbedPane();
  private ToolDetailsPanel m_detailsPanel;
  private ToolDisplayPanel m_displayPanel;
  private ToolInvocationPanel m_invocationPanel;
  private ToolEnablementPanel m_enablementPanel;



  ToolPanel()
  {

    m_displayPanel = new ToolDisplayPanel();
    m_tabbedPane.add(
      ToolManagerArb.getString( ToolManagerArb.DISPLAY_PANEL_TITLE ),
      m_displayPanel
    );  
    m_detailsPanel = new ToolDetailsPanel();
    m_tabbedPane.add( 
      ToolManagerArb.getString( ToolManagerArb.DETAILS_PANEL_TITLE ),
      m_detailsPanel
    );
    m_invocationPanel = new ToolInvocationPanel();
    m_tabbedPane.add(
      ToolManagerArb.getString( ToolManagerArb.INVOCATION_PANEL_TITLE ),      
      m_invocationPanel
    );

    m_enablementPanel = new ToolEnablementPanel();
    m_tabbedPane.add(
      ToolManagerArb.getString( ToolManagerArb.ENABLEMENT_PANEL_TITLE ),
      m_enablementPanel
    );

    
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }




  }


  /**
   * Validate the panel. If validation fails, display an alert to the user and
   * focus a sensible control
   *
   * @return true if the contents of the panel are valid, false otherwise
   */
  boolean doValidationCheck()
  {
    // Simulate a selection change to force the current UI control contents to
    // be saved down into the selected Tool object.
    commandListSelectionChanged();


    for ( int i=0; i < cmdList.getModel().getSize(); i++ )
    {
      final Tool t = (Tool) cmdList.getModel().getElementAt( i );


      if ( !m_displayPanel.validateTool( t ) )
      {
        cmdList.setSelectedIndex( i );
        cmdList.ensureIndexIsVisible( i );
        m_tabbedPane.setSelectedComponent( m_displayPanel );
        return false;
      }

      if ( !m_detailsPanel.validateTool( t ) )
      {
        cmdList.setSelectedIndex( i );
        cmdList.ensureIndexIsVisible( i );
        m_tabbedPane.setSelectedComponent( m_detailsPanel );
        return false;
      }

      if ( !m_invocationPanel.validateTool( t  ) )
      {
        cmdList.setSelectedIndex( i );
        cmdList.ensureIndexIsVisible( i );
        m_tabbedPane.setSelectedComponent( m_invocationPanel );
        return false;        
      }

      if ( !m_enablementPanel.validateTool( t  ) )
      {
        cmdList.setSelectedIndex( i );
        cmdList.ensureIndexIsVisible( i );
        m_tabbedPane.setSelectedComponent( m_enablementPanel );
        return false;        
      }


    }

    if ( duplicateToolTitles() )
    {
      return false;
    }


    return true;
  }

  /**
   * Are there any duplicate tool titles? If so, focus and alert and
   * return true
   */
  private boolean duplicateToolTitles()
  {
    String toolName;
    String compareName;
    for ( int i=0; i < cmdList.getModel().getSize(); i++ )
    {
      toolName = ((Tool) cmdList.getModel().getElementAt( i )).getTitle();
      for ( int j = i + 1; j < cmdList.getModel().getSize(); j++ )
      {
        compareName = ((Tool) cmdList.getModel().getElementAt( j )).getTitle();
        if ( compareName.equals( toolName ) )
        {
          cmdList.setSelectedIndex( j );
          cmdList.ensureIndexIsVisible( j );
          m_tabbedPane.setSelectedComponent( m_displayPanel );
          m_displayPanel.tfCaption.requestFocus();
          m_displayPanel.tfCaption.selectAll();
          alert( this,
            ToolManagerArb.format(
              ToolManagerArb.DUPLICATE_CAPTIONS, compareName
            )
          );
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Get the list of tools in the panel
   */
  List getTools()
  {
    ArrayList al = new ArrayList( cmdList.getModel().getSize() );
    for ( int i=0; i < cmdList.getModel().getSize(); i++ )
    {
      al.add( cmdList.getModel().getElementAt( i ) );
    }

    return al;
  }

  static void alert( Component parent, String message )
  {
    JEWTMessageDialog msg = JEWTMessageDialog.createMessageDialog(
      parent, Ide.getProgramName(), JEWTMessageDialog.TYPE_ALERT
    );
    msg.setMessageText( message );
    msg.setButtonMask( msg.getButtonMask() ^ JEWTMessageDialog.BUTTON_HELP );

    msg.runDialog();
  }



  void setToolManager( final ToolManager mgr )
  {
    m_manager = mgr;
    m_detailsPanel.setToolManager( mgr );
    // Populate the tool list with the set of existing tools. We make a copy of
    // each one to avoid altering the original tools (in case the user Cancels)
    if ( m_manager.getTools() != null )
    {
      final Iterator i = m_manager.getTools().iterator();
      while ( i.hasNext() )
      {
        ((SimpleListModel)cmdList.getModel()).addElement(
          new Tool( (Tool)i.next() )
        );
      }
      if ( cmdList.getModel().getSize() > 0 )
      {
        cmdList.setSelectedIndex( 0 );
      }
    }


  }

  private void jbInit() throws Exception
  {

    Icon upIcon = IdeIcons.getIcon( IdeIcons.UP_ICON );
    Icon downIcon = IdeIcons.getIcon( IdeIcons.DOWN_ICON );



    ButtonListener bl = new ButtonListener();
    this.setLayout(gbl);
    cmdLabel.setText(ToolManagerArb.getString( ToolManagerArb.TOOLS_LABEL ) );
    cmdLabel.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.TOOLS_MNEMONIC )
    );
    cmdLabel.setLabelFor( cmdList );
    cmdScrollPane.setPreferredSize(new Dimension(0, 0));

    resizeButtonPanel.setLayout(verticalFlowLayout1);
    verticalFlowLayout1.setHgap(3);
    verticalFlowLayout1.setVgap(4);
    btnMoveUp.setIcon(upIcon);
    btnMoveUp.setMargin(new Insets(1, 1, 1, 1));
    btnMoveUp.addActionListener( bl );
    btnMoveUp.setToolTipText(
      ToolManagerArb.getString( ToolManagerArb.MOVE_TOOL_UP_TOOLTIP )
    );
    btnMoveDown.setIcon(downIcon);
    btnMoveDown.setMargin(new Insets(1, 1, 1, 1));
    btnMoveDown.addActionListener( bl );
    btnMoveDown.setToolTipText(
      ToolManagerArb.getString( ToolManagerArb.MOVE_TOOL_DOWN_TOOLTIP )
    );
    btnRemoveCommand.setText(
      ToolManagerArb.getString( ToolManagerArb.REMOVE_TOOL_LABEL )
    );
    btnRemoveCommand.addActionListener( bl );
    btnAddCommand.setText(
      ToolManagerArb.getString( ToolManagerArb.ADD_TOOL_LABEL )
    );

    btnAddCommand.addActionListener( bl );





    cmdList.setModel( new SimpleListModel() );
    cmdList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    this.add(cmdLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
    cmdScrollPane.getViewport().add(cmdList, null);
    this.add(cmdScrollPane, new GridBagConstraints(0, 1, 3, 2, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));
    this.add(topBar, new GridBagConstraints(4, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
//    this.add(lblDescription, new GridBagConstraints(4, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
//    this.add(txtDescription, new GridBagConstraints(4, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 4, 2), 0, 0));
    this.add(m_tabbedPane, new GridBagConstraints(4, 2, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.add(bottomBar, new GridBagConstraints(4, 4, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    resizeButtonPanel.add(btnMoveUp, null);
    resizeButtonPanel.add(btnMoveDown, null);
    this.add(resizeButtonPanel, new GridBagConstraints(3, 1, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 4), 0, 0));
    this.add(btnRemoveCommand, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(paddingPanel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(btnAddCommand, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


    cmdList.addListSelectionListener( new ListSelectionListener() {
      public void valueChanged( ListSelectionEvent lse )
      {
        commandListSelectionChanged();
      }
    });

    m_displayPanel.tfCaption.getDocument().addDocumentListener( new DocumentListener() {
      public void changedUpdate( DocumentEvent de )
      {
        descriptionFieldChanged();
      }

      public void insertUpdate( DocumentEvent de )
      {
        descriptionFieldChanged();
      }

      public void removeUpdate( DocumentEvent de )
      {
        descriptionFieldChanged();
      }
    });





    commandListSelectionChanged();
  }

  private void descriptionFieldChanged()
  {
    final Tool t = (Tool) cmdList.getSelectedValue();
    if ( t != null )
    {
      t.setTitle( m_displayPanel.tfCaption.getText() );
      // Fire a change event so that the list knows the field has changed
      ((SimpleListModel)cmdList.getModel()).changed(
        cmdList.getSelectedIndex()
      );
    }
  }




  private void commandListSelectionChanged()
  {
    if ( m_selectedTool != null )
    {
      m_detailsPanel.saveToTool( m_selectedTool );
      m_displayPanel.saveToTool( m_selectedTool );
      m_invocationPanel.saveToTool( m_selectedTool );
      m_enablementPanel.saveToTool( m_selectedTool );
      
      m_selectedTool = null;
    }

    // Enable / disable controls
    boolean enabled = (cmdList.getSelectedIndex() != -1);

    m_detailsPanel.setControlsEnabled( enabled );
    m_displayPanel.setControlsEnabled( enabled );
    m_invocationPanel.setControlsEnabled( enabled );
    m_enablementPanel.setControlsEnabled( enabled );

    btnMoveUp.setEnabled( cmdList.getSelectedIndex() > 0 );
    btnMoveDown.setEnabled(
      cmdList.getSelectedIndex() < cmdList.getModel().getSize() -1
    );

    // Populate controls
    final Tool t = (Tool) cmdList.getSelectedValue();
    m_detailsPanel.populateFromTool( t );
    m_displayPanel.populateFromTool( t );
    m_invocationPanel.populateFromTool( t );
    m_enablementPanel.populateFromTool( t );
    if ( t == null )
    {
      btnRemoveCommand.setEnabled( false );
    }
    else
    {
      btnRemoveCommand.setEnabled( true );
      m_selectedTool = t;
    }

    //if ( lstArguments.getModel().getSize() > 0 )
    //{
    //  lstArguments.setSelectedIndex( 0 );
    //}

  }







  private void doAddCommand()
  {
    final Tool t = new Tool();
    t.setTitle( getUniqueToolName( 1 ) );
    t.setCommand( "" ); // NOTRANS
    ((SimpleListModel)cmdList.getModel()).addElement( t );
    cmdList.setSelectedIndex( cmdList.getModel().getSize() - 1 );
    cmdList.ensureIndexIsVisible( cmdList.getModel().getSize() - 1 );

    m_tabbedPane.setSelectedComponent( m_displayPanel );
    m_displayPanel.tfCaption.requestFocus();
    m_displayPanel.tfCaption.selectAll();
  }

  private void doRemoveCommand()
  {
    removeSelectedItem( cmdList );
    if ( cmdList.getModel().getSize() == 0 )
    {
      commandListSelectionChanged();
    }
  }



  static void removeSelectedItem( final JList list )
  {
    final int selectIndex = list.getSelectedIndex();
    final Object t =  list.getSelectedValue();
    if ( t != null )
    {
      ((SimpleListModel)list.getModel()).removeElement( t );
    }

    // Reset the selection.
    if ( selectIndex < list.getModel().getSize() )
    {
      list.setSelectedIndex( selectIndex );
    }
    else if ( list.getModel().getSize() > 0 )
    {
      list.setSelectedIndex( list.getModel().getSize() - 1 );
    }
  }



  /**
   * Generate a unique name for a new tool
   */
  private String getUniqueToolName( int start )
  {
    final String template =
      ToolManagerArb.getString( ToolManagerArb.NEW_TOOL_TITLE_TEMPLATE );
    final String tryName = MessageFormat.format( template, new Object[] {
      new Integer( start )
    });

    for ( int i=0; i < cmdList.getModel().getSize(); i++ )
    {
      final Tool t = (Tool)cmdList.getModel().getElementAt( i );
      if ( tryName.equals( t.getTitle() ) )
      {
        return getUniqueToolName( start+1 );
      }
    }

    return tryName;
  }

  static void moveSelectionUp( final JList list )
  {
    final int sel = list.getSelectedIndex();
    ((SimpleListModel)list.getModel()).moveUp( sel );
    list.setSelectedIndex( sel - 1 );
  }

  static void moveSelectionDown( final JList list )
  {
    final int sel = list.getSelectedIndex();
    ((SimpleListModel)list.getModel()).moveDown( sel );
    list.setSelectedIndex( sel + 1 );
  }

  private void doMoveToolUp()
  {
    moveSelectionUp( cmdList );
  }

  private void doMoveToolDown()
  {
    moveSelectionDown( cmdList );
  }



  /**
   * A single action listener which dispaches all button clicks in this panel
   */
  private class ButtonListener implements ActionListener
  {
    public void actionPerformed( ActionEvent e )
    {
      JButton button = (JButton) e.getSource();


      if ( btnAddCommand == button )
      {
        doAddCommand();
      }
      else if ( btnRemoveCommand == button )
      {
        doRemoveCommand();
      }
      else if ( btnMoveUp == button )
      {
        doMoveToolUp();
      }
      else if ( btnMoveDown == button )
      {
        doMoveToolDown();
      }
    }
  }



}