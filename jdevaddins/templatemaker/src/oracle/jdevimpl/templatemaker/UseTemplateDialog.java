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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

/**
 * UI panel that prompts the user to instantiate a template.
 *
 * @author Brian.Duff@oracle.com
 */
final class UseTemplateDialog extends JPanel
{
  private final JLabel m_labTemplate = new JLabel();
  private final JTextField m_tfTemplate = new JTextField();
  private final JLabel m_labPath = new JLabel();
  private final JTextField m_tfPath = new JTextField();
  private final JButton m_butPathBrowse = new JButton();
  private final JTextField m_tfPackage = new JTextField();
  private final JLabel m_labPackage = new JLabel();
  private final JLabel m_labFileName = new JLabel();
  private final JTextField m_tfFileName = new JTextField();
  private final JCheckBox m_addToProject = new JCheckBox();
  private String m_projectName = null;
  private boolean m_packageMode = false;


  public UseTemplateDialog()
  {
    setLayout( new GridBagLayout() );
  
    m_labTemplate.setText( "Template:");
    m_labTemplate.setDisplayedMnemonic( 'T' );
    m_labTemplate.setLabelFor( m_tfTemplate );
    m_labPath.setText("Directory Name:");
    m_labPath.setDisplayedMnemonic( 'D' );
    m_labPath.setLabelFor( m_tfPath );
    m_butPathBrowse.setText( "Browse..." );
    m_butPathBrowse.setMnemonic( 'B' );
    m_labPackage.setText("Package:");
    m_labPackage.setDisplayedMnemonic( 'a' );
    m_labPackage.setLabelFor( m_tfPackage );
    m_labFileName.setText("File Name:");
    m_labFileName.setDisplayedMnemonic( 'F' );
    m_labFileName.setLabelFor( m_tfFileName );
    m_addToProject.setText("Add to Project");
    m_addToProject.setMnemonic( 'P' );
    m_addToProject.setVisible( false );

    showOrHidePackageComponents();

    m_tfTemplate.setEditable( false );

    final Insets twoInsets = new Insets( 2, 2, 2, 2 );

    add( m_labTemplate, 
      new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        twoInsets, 0, 0
      )
    );
    add( m_tfTemplate, 
      new GridBagConstraints( 0, 1, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        twoInsets, 0, 0
      )
    );

    add( m_labPath, 
      new GridBagConstraints( 0, 2, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        twoInsets, 0, 0
      )
    );
    add( m_tfPath, 
      new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        twoInsets, 0, 0
      )
    );
    add( m_butPathBrowse, 
      new GridBagConstraints( 1, 3, 1, 1, 0.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        twoInsets, 0, 0
      )
    );

    add( m_labPackage, 
      new GridBagConstraints( 0, 4, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
        twoInsets, 0, 0
      )
    );
    add( m_tfPackage, 
      new GridBagConstraints( 0, 5, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        twoInsets, 0, 0
      )
    );

    add( m_labFileName, 
      new GridBagConstraints( 0, 6, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        twoInsets, 0, 0
      )
    );
    add( m_tfFileName, 
      new GridBagConstraints( 0, 7, 2, 1, 1.0, 0.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        twoInsets, 0, 0
      )
    );
    add( m_addToProject, 
      new GridBagConstraints( 0, 8, 2, 1, 1.0, 1.0, 
        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
        twoInsets, 0, 0
      )
    );    

    m_butPathBrowse.addActionListener( new ActionListener() {
      public void actionPerformed( final ActionEvent ae )
      {
        browseDirectory();
      }
    });
    
  }

  private void showOrHidePackageComponents()
  {
    m_labPackage.setVisible( m_packageMode );
    m_tfPackage.setVisible( m_packageMode );
    m_labPath.setVisible( !m_packageMode );
    m_tfPath.setVisible( !m_packageMode );
    m_butPathBrowse.setVisible( !m_packageMode );
  }

  private void browseDirectory()
  {
    final URLChooser chooser = new URLChooser();
    chooser.setSelectionScope( chooser.DIRECTORIES_ONLY );
    chooser.setSelectionMode( chooser.SINGLE_SELECTION );

    if ( getDirectoryName().trim().length() > 0 )
    {
      chooser.setSelectedURL( 
        URLFactory.newDirURL( getDirectoryName() )
      );
    }

    if (chooser.showOpenDialog( this, "Select a Directory" ) == chooser.APPROVE_OPTION)
    {
      setDirectoryName( 
        URLFileSystem.getPlatformPathName( chooser.getSelectedURL() )
      );
    }
  }

  public void setTemplateName( final String templateName )
  {
    m_tfTemplate.setText( templateName );
  }

  public String getTemplateName()
  {
    return m_tfTemplate.getText();
  }

  public void setFileName( final String fileName )
  {
    m_tfFileName.setText( fileName );
  }

  public String getFileName()
  {
    return m_tfFileName.getText();
  }

  public void setDirectoryName( final String path )
  {
    m_tfPath.setText( path );
  }

  public String getDirectoryName()
  {
    return m_tfPath.getText();
  }

  public String getProjectName()
  {
    return m_projectName;
  }

  public void setProjectName( final String projectName )
  {
    if ( projectName == null )
    {
      m_addToProject.setVisible( false );
    }
    else
    {
      m_addToProject.setText( "Add to Project "+projectName );
      m_addToProject.setVisible( true );
    }
  }

  public void setAddToProject( final boolean isAddToProject )
  {
    m_addToProject.setSelected( isAddToProject );
  }

  public boolean isAddToProject()
  {
    return m_addToProject.isVisible() && m_addToProject.isSelected();
  }

  public boolean isPackageMode()
  {
    return m_packageMode;
  }

  public void setPackageMode( final boolean packageMode )
  {
    m_packageMode = packageMode;
    showOrHidePackageComponents();
  }

  public void setPackageName( final String packageName )
  {
    if ( packageName.endsWith( "." ) )
    {
      m_tfPackage.setText( packageName.substring( 0, packageName.length() -1 ));
    }
    else
    {
      m_tfPackage.setText( packageName );
    }
  }

  public String getPackageName()
  {
    return m_tfPackage.getText();
  }
  
  private boolean isInValidState()
  {
    return 
      getFileName().trim().length() > 0 &&
      (0 <= ( m_packageMode ? getPackageName().trim().length() :
                              getDirectoryName().trim().length() ));
  }


  /**
   * Run the dialog 
   *
   * @param parent parent for modality
   *
   * @return true if the user clicked OK, false otherwise
   */
  public boolean runDialog( final Component parent )
  {
    final JEWTDialog dialog = JEWTDialog.createDialog( 
      parent, "Create New File From Template", 
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );
    dialog.setContent( this );
    dialog.setInitialFocus( m_tfFileName );
    dialog.setOKButtonEnabled( isInValidState() );

    final DocumentListener docListener = new DocumentListener()
      {
        public void changedUpdate( final DocumentEvent de )
        {
          dialog.setOKButtonEnabled( isInValidState() );
        }

        public void insertUpdate( final DocumentEvent e )
        {
          dialog.setOKButtonEnabled( isInValidState() );
        }

        public void removeUpdate( final DocumentEvent e )
        {
          dialog.setOKButtonEnabled( isInValidState() );
        }
      };

    m_tfPath.getDocument().addDocumentListener( docListener );
    m_tfFileName.getDocument().addDocumentListener( docListener );
    m_tfPackage.getDocument().addDocumentListener( docListener );

    // Workaround issues with gridbaglayout and text fields that expand
    // forever...
    m_tfPath.setPreferredSize( 
      new Dimension( 0, m_tfPath.getPreferredSize().height )
    );
    m_tfFileName.setPreferredSize(
      new Dimension( 350, m_tfFileName.getPreferredSize().height )
    );
    m_tfPackage.setPreferredSize(
      new Dimension( 0, m_tfPackage.getPreferredSize().height )
    );

    final boolean result = dialog.runDialog();

    m_tfPath.getDocument().removeDocumentListener( docListener );
    m_tfFileName.getDocument().removeDocumentListener( docListener );
    m_tfPackage.getDocument().removeDocumentListener( docListener );

    return result;
  }

  public static void main( String[] args )
  {
    UseTemplateDialog dlg = new UseTemplateDialog();
    dlg.runDialog( null );
  }
}