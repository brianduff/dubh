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
 * The Original Code is Python Addin for Oracle9i JDeveloper.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.jdev.addin.python;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.ide.Ide;
import oracle.ide.addin.Context;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.addin.VetoableMessage;
import oracle.ide.addin.Wizard;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.dialogs.DialogUtil;
import oracle.ide.exception.ChangeVetoException;
import oracle.ide.model.Node;
import oracle.ide.model.NodeFactory;
import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.util.MenuSpec;

import oracle.jdeveloper.model.JProject;

import oracle.bali.ewt.dialog.JEWTDialog;
import java.net.URL;


/**
 * NewPythonFileWizard
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.1 $
 */
class NewPythonFileWizard extends JPanel
  implements Wizard
{

  GridBagLayout m_layout = new GridBagLayout();
  JLabel m_labLocation = new JLabel();
  JTextField m_tfLocation = new JTextField();
  JButton m_butBrowse = new JButton();
  JLabel m_labName = new JLabel();
  JTextField m_tfName = new JTextField();

  public NewPythonFileWizard()
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

  private void jbInit() throws Exception
  {
    this.setLayout(m_layout);
    m_labLocation.setText("Directory Name:");
    m_labLocation.setDisplayedMnemonic( 'D' );
    m_labLocation.setLabelFor(m_tfLocation);
    m_butBrowse.setText("Browse...");
    m_butBrowse.setMnemonic('B');
    m_labName.setText("File Name:");
    m_labName.setDisplayedMnemonic( 'F' );
    m_labName.setLabelFor(m_tfName);
    this.add(m_labLocation, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(m_tfLocation, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(m_butBrowse, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(m_labName, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(m_tfName, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 350, 0));
  }

  private String getDefaultLocation( Context ctx )
  {
    // return the sourcepath for now. we could do so much more here. :)
    if ( ctx.getProject() instanceof JProject )
    {
      return URLFileSystem.getPlatformPathName(
        ((JProject)ctx.getProject()).getSourcePath().getEntries()[0]
      );
    }
    return "";
  }

  /**
   * Create a new python script 
   */
  private boolean createNewPythonScript( Context context, String dir, String name )
  {
    URL u = URLFactory.newFileURL( new File( dir, name ) );
    if ( URLFileSystem.exists( u ) )
    {
      alert( "A file called "+name+" already exists in the specified location" );
      return false;
    }

    if ( !URLFileSystem.hasSuffix( u, ".py" ) )
    {
      // Hmm
      name = name + ".py";
      m_tfName.setText( name );
      return createNewPythonScript( context, dir, name );
      
    }

    
    Node node;
    // It's a new node
    try
    {
      node = NodeFactory.findOrCreate( u );;
    }
    catch (IllegalAccessException ille )
    {
      alert( "Failed to open "+u );
      return false;
    }
    catch (InstantiationException ie )
    {
      alert( "Failed to open "+u );
      return false;
    }

    node.markDirty( true );

    if ( context.getProject() != null &&
      context.getProject().canAdd( node ) )
    {
      List addedItems = new ArrayList( 1 );
      addedItems.add( node );
      try
      {
        VetoableMessage.fireCanAddChildren( context.getProject(), addedItems );
        context.getProject().addToProject( addedItems );
        if ( addedItems.size() > 0 )
        {
          UpdateMessage.fireChildrenAdded( context.getProject(), addedItems );
        }
      }
      catch (ChangeVetoException cve)
      {
        // Someone vetoed the add. So don't do it.
      }

      // Update the File->Reopen menu
      Ide.getFileOpenHistory().updateFileHistory( node.getURL() );

      // Open the file in an editor window.
      Ide.getEditorManager().openDefaultEditorInFrame( node );

    }   

    return true;
    
  }

  /**
   * Utility to display an alert in the neat OLAF style
   */
  private void alert( final String message )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_ALERT
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( message );
    md.runDialog();
  }  

// ---------------------------------------------------------------------------
// Wizard implementation
// ---------------------------------------------------------------------------



  public String getName()
  {
    return "Python Script";
  }

  public String getMenuLabel()
  {
    return null;
  }

  public MenuSpec getMenuSpecification()
  {
    return null;
  }

  public boolean isAvailable( final Context context )
  {
    return (context != null && context.getProject() != null);
  }

  public String toString()
  {
    return getName();
  }

  public Icon getIcon()
  {
    return null;
  }

  public boolean invoke( final Context ctx, final String[] args )
  {
    String defaultLocation = getDefaultLocation( ctx );

    final JEWTDialog dialog = JEWTDialog.createDialog(
      Ide.getMainWindow(), "New Python Script", 
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );
    dialog.setContent( this );

    m_tfLocation.setText( defaultLocation );
    m_tfName.setText( "" );
    // Everybody's favorite GBL workaround...
    m_tfLocation.setPreferredSize( 
      new Dimension( 0, m_tfLocation.getPreferredSize().height ) 
    );
    dialog.setOKButtonEnabled( false );

    // Listen to the browse button and bring up the URLChooser
    ActionListener browseListener = new ActionListener()
    {
      public void actionPerformed( ActionEvent ae )
      {
        URLChooser u = DialogUtil.newURLChooser( ctx );
        u.setSelectionScope( u.DIRECTORIES_ONLY );
        u.setSelectionMode( u.SINGLE_SELECTION );
        u.setSelectedURL( URLFactory.newDirURL( m_tfLocation.getText() ) );

        int res = u.showOpenDialog( Ide.getMainWindow(), "Choose Directory" );

        if ( res == u.APPROVE_OPTION )
        {
          m_tfLocation.setText(
            URLFileSystem.getPlatformPathName(
              u.getSelectedURL()
            )
          );
        }
      }
    };
    m_butBrowse.addActionListener( browseListener );

    // Listen to the location and name fields to keep the OK button enabled
    // or disabled
    DocumentListener dl = new DocumentListener() 
    {
      private boolean isValid()
      {
        return m_tfLocation.getText().trim().length() > 0 && 
          m_tfName.getText().trim().length() > 0;
      }
    
      public void insertUpdate( DocumentEvent de )
      {
        dialog.setOKButtonEnabled( isValid() );
      }

      public void removeUpdate( DocumentEvent de )
      {
        dialog.setOKButtonEnabled( isValid() );
      }

      public void changedUpdate( DocumentEvent de )
      {
        dialog.setOKButtonEnabled( isValid() );
      }
    };
    m_tfName.getDocument().addDocumentListener( dl );
    m_tfLocation.getDocument().addDocumentListener( dl );

    // Validate while the dialog is still onscreen.
    dialog.addVetoableChangeListener( new VetoableChangeListener() 
    {
      public void vetoableChange( PropertyChangeEvent pce )
        throws PropertyVetoException
      {
        if ( dialog.isDialogClosingEvent( pce ) )
        {
          if ( !createNewPythonScript( ctx, m_tfLocation.getText(), m_tfName.getText() ) )
          {
            throw new PropertyVetoException( "Didn't do it", pce );
          }
        }
      }
    });

    dialog.setInitialFocus( m_tfName );

    dialog.runDialog();
    dialog.dispose();
    m_tfName.getDocument().removeDocumentListener( dl );
    m_tfLocation.getDocument().removeDocumentListener( dl );
    m_butBrowse.removeActionListener( browseListener );

    return true;
  }


}