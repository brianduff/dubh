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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import oracle.jdeveloper.layout.VerticalFlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import oracle.bali.ewt.dialog.JEWTDialog;
import oracle.bali.ewt.button.PopupButton;


import oracle.ide.Ide;
import oracle.ide.controls.JMultiLineLabel;
import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.resource.IdeIcons;
import java.awt.FlowLayout;

import oracle.jdevimpl.toolmanager.argument.TextArgument;
import oracle.ideimpl.resource.IdeImplArb;


public class ToolDetailsPanel extends JPanel 
{

  /**
   * Used to remember the last selected executable path in the URL chooser
   */
  private static URL s_lastExecutablePath;


  /**
   * Used to remember the last selected run directory in the URL chooser
   */
  private static URL s_lastRunDirectory;  

  private ToolManager m_manager;

  GridBagLayout gblDetailsPanel = new GridBagLayout();
  JLabel labExecutable = new JLabel();
  JTextField tfExecutable = new JTextField();
  JButton btnBrowseExecutable = new JButton();
  JLabel labRunInDirectory = new JLabel();
  JTextField tfRunInDirectory = new JTextField();
  JButton btnBrowseDirectory = new JButton();
  JLabel labArguments = new JLabel();
  JScrollPane spArgumentsList = new JScrollPane();
  JPanel panArgumentsResizer = new JPanel();
  VerticalFlowLayout vflArgumentResizer = new VerticalFlowLayout();
  JButton btnMoveArgumentUp = new JButton();
  JButton btnMoveArgumentDown = new JButton();
  JPanel panArgumentsButtons = new JPanel();
  FlowLayout flwArgumentsButtons = new FlowLayout();
  JButton btnAddText = new JButton();
  JButton btnAdd = new JButton();
  JButton btnRemove = new JButton();
  JList lstArguments = new JList();
  PopupButton btnDirectoryArgument = new PopupButton();

  public ToolDetailsPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    final ActionDispatcher al = new ActionDispatcher();

    lstArguments.setModel( new SimpleListModel() );
    lstArguments.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );    

    btnBrowseDirectory.addActionListener( al );
    btnBrowseExecutable.addActionListener( al );
    btnMoveArgumentUp.addActionListener( al );
    btnMoveArgumentDown.addActionListener( al );
    btnAdd.addActionListener( al );
    btnAddText.addActionListener( al );
    btnRemove.addActionListener( al );

    lstArguments.addListSelectionListener( new ListSelectionListener() {
      public void valueChanged( ListSelectionEvent lse )
      {
        argumentListSelectionChanged();
      }
    });    

    lstArguments.addMouseListener( new MouseAdapter() {
      public void mouseClicked( MouseEvent me )
      {
        if ( me.getClickCount() == 2 && me.getModifiers() == me.BUTTON1_MASK )
        {
          if ( lstArguments.getSelectedIndex() != -1 )
          {
            doEditArgument();
          }
        }
      }
    });    

  }

  void setToolManager( ToolManager mgr )
  {
    m_manager = mgr;

    btnDirectoryArgument.setPopupMenu(
      createArgumentTypePopup( tfRunInDirectory, true )
    );
  }

  void saveToTool( final Tool t )
  {
    t.setCommand( tfExecutable.getText() );
    if ( tfRunInDirectory.getText().trim().length() == 0 )
    {
      t.setWorkingDirectory( null );
    }
    else
    {
      t.setWorkingDirectory( tfRunInDirectory.getText() );
    }
    final ToolArgument[] args =
      new ToolArgument[ lstArguments.getModel().getSize() ];
    for ( int i=0; i < lstArguments.getModel().getSize(); i++ )
    {
      args[ i ] = (ToolArgument) lstArguments.getModel().getElementAt( i );
    }
    t.setArguments( args );
  }

  void populateFromTool( final Tool t )
  {
    if ( t == null )
    {

      tfExecutable.setText("");    // NOTRANS
      tfRunInDirectory.setText("");  // NOTRANS
      ((SimpleListModel)lstArguments.getModel()).removeAll();
    }
    else
    {
      tfExecutable.setText( t.getCommand() );
      if ( t.getWorkingDirectory() == null )
      {
        tfRunInDirectory.setText("");  // NOTRANS
      }
      else
      {
        tfRunInDirectory.setText( t.getWorkingDirectory() );
      }
      ((SimpleListModel)lstArguments.getModel()).removeAll();
      ToolArgument[] args = t.getArguments();
      if ( args != null )
      {
        for ( int i=0; i < args.length; i++ )
        {
          ((SimpleListModel)lstArguments.getModel()).addElement( args[i] );
        }
      }
    }
  }

  boolean validateTool( Tool t )
  {
    // The tool must have an executable
    if ( t.getCommand() == null || t.getCommand().trim().length() == 0 )
    {

      tfExecutable.requestFocus();
      ToolPanel.alert( this,
        ToolManagerArb.format( ToolManagerArb.MUST_SPECIFY_AN_EXEC,
          t.getTitle()
        )
      );
      return false;
    }     

    return true;
  }

  void setControlsEnabled( final boolean enabled )
  {
    labArguments.setEnabled( enabled );

    labExecutable.setEnabled( enabled );
    labRunInDirectory.setEnabled( enabled );
    lstArguments.setEnabled( enabled );
    btnAdd.setEnabled( enabled );
    btnAddText.setEnabled( enabled );
    btnRemove.setEnabled(
      enabled && lstArguments.getSelectedIndex() != -1
    );
    btnBrowseDirectory.setEnabled( enabled );
    btnBrowseExecutable.setEnabled( enabled );
    btnMoveArgumentDown.setEnabled(
      enabled && lstArguments.getSelectedIndex() < lstArguments.getModel().getSize() -1
    );
    btnMoveArgumentUp.setEnabled(
      enabled && lstArguments.getSelectedIndex() > 0
    );

    tfExecutable.setEnabled( enabled );
    tfRunInDirectory.setEnabled( enabled ); 
    btnDirectoryArgument.setEnabled( enabled );   
  }

  private void jbInit() throws Exception
  {
    Icon upIcon = IdeIcons.getIcon( IdeIcons.UP_ICON );
    Icon downIcon = IdeIcons.getIcon( IdeIcons.DOWN_ICON );
  
    this.setLayout(gblDetailsPanel);
    labExecutable.setText(
      ToolManagerArb.getString( ToolManagerArb.EXECUTABLE_LABEL )
    );
    labExecutable.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.EXECUTABLE_MNEMONIC )
    );
    labExecutable.setLabelFor(tfExecutable);
    btnBrowseExecutable.setText(
      ToolManagerArb.getString( ToolManagerArb.BROWSE_EXECUTABLE_LABEL )
    );
    btnBrowseExecutable.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.BROWSE_EXECUTABLE_MNEMONIC )
    );
    labRunInDirectory.setText(
      ToolManagerArb.getString( ToolManagerArb.RUN_IN_DIRECTORY_LABEL )
    );
    labRunInDirectory.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.RUN_IN_DIRECTORY_MNEMONIC )
    );
    labRunInDirectory.setLabelFor(tfRunInDirectory);
    btnBrowseDirectory.setText(
      ToolManagerArb.getString( ToolManagerArb.BROWSE_DIRECTORY_LABEL )
    );
    btnBrowseDirectory.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.BROWSE_DIRECTORY_MNEMONIC )
    );
    labArguments.setText(
      ToolManagerArb.getString( ToolManagerArb.ARGUMENTS_LABEL )
    );
    labArguments.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.ARGUMENTS_MNEMONIC )
    );
    labArguments.setLabelFor(lstArguments);
    panArgumentsResizer.setLayout(vflArgumentResizer);
    vflArgumentResizer.setHgap(2);
    vflArgumentResizer.setVgap(2);
    btnMoveArgumentUp.setIcon( upIcon );
    btnMoveArgumentUp.setMargin(new Insets(1, 1, 1, 1));
    btnMoveArgumentUp.setDefaultCapable(false);
    btnMoveArgumentUp.setToolTipText(
      ToolManagerArb.getString( ToolManagerArb.MOVE_ARG_UP_TOOLTIP )
    );
    btnMoveArgumentDown.setIcon( downIcon );
    btnMoveArgumentDown.setMargin(new Insets(1, 1, 1, 1));
    btnMoveArgumentDown.setDefaultCapable(false);
    btnMoveArgumentDown.setToolTipText(
      ToolManagerArb.getString( ToolManagerArb.MOVE_ARG_DOWN_TOOLTIP )
    );
    panArgumentsButtons.setLayout(flwArgumentsButtons);
    flwArgumentsButtons.setHgap(2);
    flwArgumentsButtons.setVgap(2);
    flwArgumentsButtons.setAlignment(2);
    btnAddText.setText(
      ToolManagerArb.getString( ToolManagerArb.ADD_TEXT_ARG_LABEL )
    );
    btnAddText.setDefaultCapable( false );
    btnAdd.setText(
      ToolManagerArb.getString( ToolManagerArb.ADD_ARG_LABEL )
    );
    btnAdd.setDefaultCapable( false );
    btnRemove.setText(
      ToolManagerArb.getString( ToolManagerArb.REMOVE_ARG_LABEL )
    );
    btnRemove.setDefaultCapable( false );
    btnDirectoryArgument.setMargin(new Insets(1, 1, 1, 1));
    btnDirectoryArgument.setDefaultCapable(false);
    btnDirectoryArgument.setDelay( 0 );
    btnDirectoryArgument.setIcon( IdeImplArb.getIcon( IdeImplArb.OIEL_DROP_DOWN_IMAGE ) ); 
    this.add(labExecutable, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    this.add(tfExecutable, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    this.add(btnBrowseExecutable, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    this.add(labRunInDirectory, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(tfRunInDirectory, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
    this.add(btnBrowseDirectory, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
    this.add(labArguments, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    spArgumentsList.getViewport().add(lstArguments, null);
    this.add(spArgumentsList, new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
    panArgumentsResizer.add(btnMoveArgumentUp, null);
    panArgumentsResizer.add(btnMoveArgumentDown, null);
    this.add(panArgumentsResizer, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    panArgumentsButtons.add(btnAddText, null);
    panArgumentsButtons.add(btnAdd, null);
    panArgumentsButtons.add(btnRemove, null);
    this.add(panArgumentsButtons, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(btnDirectoryArgument, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
  }


  private void doBrowseExecutable()
  {
    // Future ENH: Should probably have a *.exe file filter for Windows?

    URLChooser urc = new URLChooser();
    urc.setSelectionMode( urc.SINGLE_SELECTION );

    if ( tfExecutable.getText().trim().length() == 0 )
    {
      if ( s_lastExecutablePath == null )
      {
        urc.setSelectedURL(
          URLFactory.newFileURL( Ide.getHomeDirectory() )
        );
      }
      else
      {
        urc.setSelectedURL( s_lastExecutablePath );
      }
    }
    else
    {
      urc.setSelectedURL(
        URLFactory.newFileURL( tfExecutable.getText() )
      );
    }

    if ( urc.showOpenDialog( this,
      ToolManagerArb.getString( ToolManagerArb.CHOOSE_EXECUTABLE_TITLE )
      ) == urc.APPROVE_OPTION )
    {
      URL result = urc.getSelectedURL();
      tfExecutable.setText(
        URLFileSystem.getPlatformPathName( result )
      );
    }

    s_lastExecutablePath = urc.getSelectedURL();
  }  


  private void doBrowseDirectory()
  {
    URLChooser urc = new URLChooser();
    urc.setSelectionScope( urc.DIRECTORIES_ONLY );
    urc.setSelectionMode( urc.SINGLE_SELECTION );

    if ( tfRunInDirectory.getText().trim().length() == 0 )
    {
      if ( s_lastRunDirectory == null )
      {
        urc.setSelectedURL(
          URLFactory.newFileURL( Ide.getHomeDirectory() )
        );
      }
      else
      {
        urc.setSelectedURL( s_lastRunDirectory );
      }
    }
    else
    {
      urc.setSelectedURL(
        URLFactory.newFileURL( tfRunInDirectory.getText() )
      );
    }

    if ( urc.showOpenDialog( this,
      ToolManagerArb.getString( ToolManagerArb.CHOOSE_DIRECTORY_TITLE )
         ) == urc.APPROVE_OPTION )
    {
      URL result = urc.getSelectedURL();
      tfRunInDirectory.setText(
        URLFileSystem.getPlatformPathName( result )
      );
    }

    // Regardless, store the last path in the static
    s_lastRunDirectory = urc.getSelectedURL();
  }


  private void doMoveArgumentUp()
  {
    ToolPanel.moveSelectionUp( lstArguments );
  }

  private void doMoveArgumentDown()
  {
    ToolPanel.moveSelectionDown( lstArguments );
  }

  private void doAddArgument()
  {
    final JPanel argPanel = new JPanel();
    final JList argList = new JList();
    argList.setModel( new SimpleListModel() );
    final JMultiLineLabel descLabel = new JMultiLineLabel();

    descLabel.setText( " \n " );  // NOTRANS
    // Constrain the label so it's always at least two lines tall.
    descLabel.setMinimumSize(
      new java.awt.Dimension( descLabel.getMinimumSize().width,
        descLabel.getPreferredSize().height
      )
    );

    argPanel.setLayout( new BorderLayout() );
    argPanel.add( new JScrollPane( argList ), BorderLayout.CENTER );
    argPanel.add( descLabel, BorderLayout.SOUTH );

    final Iterator i = m_manager.getArgumentRegistry().getArguments();
    while ( i.hasNext() )
    {
      ((SimpleListModel)argList.getModel()).addElement(
        i.next()
      );
    }

    final JEWTDialog dialog = JEWTDialog.createDialog(
      this,
      ToolManagerArb.getString( ToolManagerArb.TOOL_ARGUMENT_DIALOG_TITLE ),
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );


    dialog.setContent( argPanel );
    dialog.setPreferredSize( 300, 400 );
    dialog.setResizable( true );

    argList.setSelectedIndex( 0 );
    argList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

    argList.addListSelectionListener( new ListSelectionListener() {
      public void valueChanged( ListSelectionEvent lse )
      {
        ToolArgument theArg = (ToolArgument) argList.getSelectedValue();
        if ( theArg != null )
        {
          descLabel.setText( theArg.getDescription() );
          dialog.setOKButtonEnabled( true );
        }
        else
        {
          descLabel.setText( " \n " );  // NOTRANS
          dialog.setOKButtonEnabled( false );
        }
      }
    });

    descLabel.setText( 
      ((ToolArgument)argList.getSelectedValue()).getDescription() 
    );

    if ( dialog.runDialog() )
    {
      ((SimpleListModel)lstArguments.getModel()).addElement(
        argList.getSelectedValue()
      );

      lstArguments.setSelectedIndex( lstArguments.getModel().getSize() -1 );
      lstArguments.ensureIndexIsVisible( lstArguments.getModel().getSize() -1 );
    }
  }



  private void doEditArgument()
  {
    if ( lstArguments.getSelectedValue() instanceof TextArgument )
    {
      String str = runTextArgumentDialog(
        ((TextArgument)lstArguments.getSelectedValue()).getText()
      );
      if ( str != null )
      {
        // Switch with a new instance to avoid altering the original instance.
        // yuck.
        int idx = lstArguments.getSelectedIndex();
        ((SimpleListModel)lstArguments.getModel()).removeElement(
          lstArguments.getModel().getElementAt( idx )
        );
        TextArgument newInstance = new TextArgument();
        newInstance.setText( str );
        ((SimpleListModel)lstArguments.getModel()).insertAt( idx, newInstance );
        lstArguments.setSelectedIndex( idx );
        lstArguments.ensureIndexIsVisible( idx );
      }
    }
  }

  private JPopupMenu createArgumentTypePopup( 
    JTextField field, boolean onlyDirectories )
  {
    final JPopupMenu menu = new JPopupMenu();
    // Create an item for each registered argument type
    Iterator i = m_manager.getArgumentRegistry().getArguments();
    while ( i.hasNext() )
    {
      ToolArgument arg = (ToolArgument) i.next();
      if ( onlyDirectories && !arg.isDirectoryArgument() )
      {
        continue;
      }
      JMenuItem mi = new JMenuItem( arg.getName() );
      mi.addActionListener( new InsertActionListener( field, arg ) );
      menu.add( mi );
    }

    return menu;
  }


  private String runTextArgumentDialog( String prepopulate )
  {
    final JPanel panTextArgument = new JPanel();
    final JLabel lblTextArgument = new JLabel();
    lblTextArgument.setText(
      ToolManagerArb.getString( ToolManagerArb.TEXT_ARGUMENT_LABEL )
    );
    lblTextArgument.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.TEXT_ARGUMENT_LABEL_MNEMONIC )
    );
    final JTextField taTextArgument = new JTextField();
    lblTextArgument.setLabelFor( taTextArgument );
    taTextArgument.setText( prepopulate );
    taTextArgument.setPreferredSize( new java.awt.Dimension( 0,
      taTextArgument.getPreferredSize().height
    ));
    final PopupButton btnInsert = new PopupButton();
    btnInsert.setText(
      ToolManagerArb.getString( ToolManagerArb.INSERT_BUTTON_LABEL )
    );
    btnInsert.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.INSERT_BUTTON_MNEMONIC )
    );
    panTextArgument.setLayout( new GridBagLayout() );

    panTextArgument.add(
      lblTextArgument, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.BOTH,
        new Insets( 2, 2, 2, 2 ), 0, 0
      )
    );
    panTextArgument.add(
      taTextArgument , new GridBagConstraints(
        0, 1, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
        new Insets( 2, 2, 2, 2 ), 200, 0
      )
    );
    panTextArgument.add(
      btnInsert, new GridBagConstraints(
      1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        new Insets( 2, 2, 2, 2 ), 0, 0
      )
    );

    final JPopupMenu menu = createArgumentTypePopup( taTextArgument, false );

    btnInsert.setDelay( 0 );
    btnInsert.setPopupMenu( menu );
    btnInsert.setIcon( IdeImplArb.getIcon( IdeImplArb.OIEL_DROP_DOWN_IMAGE ) );
    btnInsert.setDefaultCapable( false );
    btnInsert.setHorizontalTextPosition( btnInsert.LEFT );


    final JEWTDialog dialog = JEWTDialog.createDialog(
      this,
      ToolManagerArb.getString( ToolManagerArb.TEXT_ARGUMENT_DIALOG_TITLE ),
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );

    dialog.setContent( panTextArgument );
    // Deliberately constrain horizontal size to stop the dialog stretching
    //dialog.setPreferredSize( 400, dialog.getPreferredSize().height );

    if ( dialog.runDialog() )
    {
      return taTextArgument.getText();
    }
    return null;
  }

  private void doAddTextArgument()
  {
    String str = runTextArgumentDialog(""); // NOTRANS
    if ( str != null )
    {
      TextArgument textArg = new TextArgument();
      textArg.setText( str );
      ((SimpleListModel)lstArguments.getModel()).addElement(
        textArg
      );
      lstArguments.setSelectedIndex( lstArguments.getModel().getSize() -1 );
      lstArguments.ensureIndexIsVisible( lstArguments.getModel().getSize() -1 );
    }
  }

  private void doRemoveArgument()
  {
    ToolPanel.removeSelectedItem( lstArguments );
    if ( lstArguments.getModel().getSize() == 0 )
    {
      argumentListSelectionChanged();
    }
  }  

  void argumentListSelectionChanged()
  {
    btnMoveArgumentUp.setEnabled(
      lstArguments.getSelectedIndex() > 0
    );
    btnMoveArgumentDown.setEnabled(
      lstArguments.getSelectedIndex() < lstArguments.getModel().getSize() - 1 &&
      lstArguments.getSelectedIndex() != -1
    );
    btnRemove.setEnabled(
      lstArguments.getSelectedIndex() != -1
    );
  }


  private class ActionDispatcher implements ActionListener
  {
    public void actionPerformed( ActionEvent ae )
    {
      JButton button = (JButton) ae.getSource();

      if ( btnBrowseExecutable == button )
      {
        doBrowseExecutable();
      }
      else if ( btnBrowseDirectory == button )
      {
        doBrowseDirectory();
      }
      else if ( btnMoveArgumentUp == button )
      {
        doMoveArgumentUp();
      }
      else if ( btnMoveArgumentDown == button )
      {
        doMoveArgumentDown();
      }
      else if ( btnAdd == button )
      {
        doAddArgument();
      }
      else if ( btnRemove == button )
      {
        doRemoveArgument();
      }
      else if ( btnAddText == button )
      {
        doAddTextArgument();
      }
    }
  }


  private static class InsertActionListener implements ActionListener
  {
    private String m_text;
    private JTextField m_area;

    InsertActionListener( JTextField area, ToolArgument arg )
    {
      m_area = area;
      m_text = "{" + arg.getMoniker() + "}";    // NOTRANS
    }

    public void actionPerformed(ActionEvent e)
    {
      try
      {
        m_area.getDocument().insertString( m_area.getCaretPosition(), m_text, null );
      }
      catch (Exception ex) {}
    }
  }  
}