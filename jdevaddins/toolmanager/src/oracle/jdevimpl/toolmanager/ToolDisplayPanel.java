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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.net.URL;
import java.awt.MediaTracker;

import oracle.bali.share.nls.StringUtils;

import oracle.ide.Ide;
import oracle.ide.net.DefaultURLFilter;
import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFilter;
import oracle.ide.net.URLFileSystem;
import oracle.ide.resource.IdeIcons;

import oracle.jdeveloper.resource.ModelArb;
import oracle.jdeveloper.model.GifImageNode;
import oracle.jdeveloper.model.JpegImageNode;
import oracle.jdeveloper.model.PngImageNode;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;


public class ToolDisplayPanel extends JPanel 
{
  GridBagLayout gblDisplayPanel = new GridBagLayout();
  JLabel labCaption = new JLabel();
  JTextField tfCaption = new JTextField();
  JLabel lblIconLocation = new JLabel();
  JTextField tfIconLocation = new JTextField();
  JButton btnBrowse = new JButton();
  JLabel labToolTip = new JLabel();
  JTextField tfToolTip = new JTextField();

  private ToolManager m_manager;
  JLabel labPreviewLabel = new JLabel();
  JLabel labPreview = new JLabel();

  public ToolDisplayPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    btnBrowse.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        doBrowseIcon();
      }
    });

    DocumentListener dl = new DocumentListener() {
      public void changedUpdate( DocumentEvent de )
      {
        updatePreviewText();
      }
      public void insertUpdate( DocumentEvent de )
      {
        updatePreviewText();
      }

      public void removeUpdate( DocumentEvent de )
      {
        updatePreviewText();
      }
    };
    tfCaption.getDocument().addDocumentListener( dl );

    tfIconLocation.getDocument().addDocumentListener(
      new DocumentListener() {
        public void changedUpdate( DocumentEvent de )
        {
          updatePreviewIcon();
        }
        public void insertUpdate( DocumentEvent de )
        {
          updatePreviewIcon();
        }

        public void removeUpdate( DocumentEvent de )
        {
          updatePreviewIcon();
        }
      }
    );

  }

  private void updatePreviewIcon()
  {
    labPreview.setIcon( IdeIcons.getIcon( IdeIcons.BLANK_ICON )  );
    String iconURL = tfIconLocation.getText();
    if ( iconURL.trim().length() != 0 )
    {
      URL u = URLFactory.newURL( iconURL );
      if ( u != null )
      {
        ImageIcon image = new ImageIcon( u );
        if ( image != null && 
          image.getImageLoadStatus() == MediaTracker.COMPLETE )
        {
          labPreview.setIcon( image );
        }
      }
    }
  }

  private void updatePreviewText()
  {
    labPreview.setText( StringUtils.stripMnemonic( tfCaption.getText() ) );

    final int mnemonic = StringUtils.getMnemonicKeyCode( tfCaption.getText() );
    labPreview.setDisplayedMnemonic( mnemonic );
  }


  void setToolManager( ToolManager mgr )
  {
    m_manager = mgr;
  }

  void saveToTool( final Tool t )
  {

    t.setToolTip( 
      tfToolTip.getText().trim().length() == 0 ? null : tfToolTip.getText()
    );
    String iconURL = tfIconLocation.getText();
    if ( iconURL.trim().length() == 0 )
    {
      t.setIconURL( null );
    }
    else
    {
      t.setIconURL(
        URLFactory.newURL( iconURL )
      );
    }


    t.setTitle( tfCaption.getText()  );
  }

  void populateFromTool( final Tool t )
  {
    if ( t == null )
    {

      tfCaption.setText("");    // NOTRANS
      tfToolTip.setText("");  // NOTRANS
      tfIconLocation.setText(""); // NOTRANS
      labPreview.setText(" "); // NOTRANS
      labPreview.setIcon( null );
      labPreview.setDisplayedMnemonic( (char) 0 );
    }
    else
    {



      tfCaption.setText( t.getTitle() );
      
      tfToolTip.setText( t.getToolTip() == null ? "" : t.getToolTip() );  // NOTRANS
      URL iconURL = t.getIconURL();
      if ( iconURL != null )
      {
        if ( "file".equals( iconURL.getProtocol() ) )
        {
          tfIconLocation.setText(
            URLFileSystem.getPlatformPathName( iconURL )
          );
        }
        else
        {
          tfIconLocation.setText(
            iconURL.toString()
          );
        }
      }
      else
      {
        tfIconLocation.setText( "" );
      }

      updatePreviewIcon();
      updatePreviewText();
    }
  }

  boolean validateTool( final Tool t )
  {

    // The tool must have a title.
    if ( t.getTitle() == null || t.getTitle().trim().length() == 0 )
    {
      tfCaption.requestFocus();
      tfCaption.selectAll();
      ToolPanel.alert( this,
        ToolManagerArb.getString( ToolManagerArb.MUST_PROVIDE_A_CAPTION )
      );
      return false;    
    }

    // The icon URL must exist if provided
    if ( t.getIconURL() != null )
    {
      if ( !URLFileSystem.canRead( t.getIconURL() ) )
      {
        tfIconLocation.requestFocus();
        tfIconLocation.selectAll();
        ToolPanel.alert( this,
          ToolManagerArb.format( 
            ToolManagerArb.BAD_ICON_URL, t.getTitle()
          )
        );
        return false;
      }
    }
    

    return true;
  }

  void setControlsEnabled( final boolean enabled )
  {
    labCaption.setEnabled( enabled );
    labToolTip.setEnabled( enabled );
    lblIconLocation.setEnabled( enabled );
    tfCaption.setEnabled( enabled );
    tfToolTip.setEnabled( enabled );
    tfIconLocation.setEnabled( enabled );
    btnBrowse.setEnabled( enabled );
    labPreviewLabel.setEnabled( enabled );
  }



  private void doBrowseIcon()
  {
    URLChooser urc = new URLChooser();
    urc.setSelectionMode( urc.SINGLE_SELECTION );
    final URLFilter imageFilter =
      new DefaultURLFilter( ModelArb.getString( ModelArb.IMAGE_FILTER ),
                            new String[] { GifImageNode.EXT, JpegImageNode.EXT,
                                           JpegImageNode.ALT_EXT, PngImageNode.EXT } );

    urc.setURLFilter( imageFilter );
    
  
    if ( tfIconLocation.getText().trim().length() == 0 )
    {

      urc.setSelectedURL(
        URLFactory.newFileURL( Ide.getHomeDirectory() )
      );

    }
    else
    {
      urc.setSelectedURL(
        URLFactory.newFileURL( tfIconLocation.getText() )
      );
    }

    if ( urc.showOpenDialog( this,
      ToolManagerArb.getString( ToolManagerArb.CHOOSE_ICON_LOCATION )
         ) == urc.APPROVE_OPTION )
    {
      URL result = urc.getSelectedURL();

      if ( "file".equals( result.getProtocol() ) )
      {
        tfIconLocation.setText(
          URLFileSystem.getPlatformPathName( result )
        );
      }
      else
      {
        tfIconLocation.setText(
          result.toString()
        );
      }      
    }

  
  }

  private void jbInit() throws Exception
  {
    this.setLayout(gblDisplayPanel);
    labCaption.setText(
      ToolManagerArb.getString( ToolManagerArb.MENU_CAPTION_LABEL )
    );
    labCaption.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.MENU_CAPTION_MNEMONIC )
    );
    labCaption.setLabelFor(tfCaption);
    lblIconLocation.setText(
      ToolManagerArb.getString( ToolManagerArb.ICON_LOCATION_LABEL )
    );
    lblIconLocation.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.ICON_LOCATION_MNEMONIC )
    );
    lblIconLocation.setLabelFor(tfIconLocation);
    btnBrowse.setText( 
      ToolManagerArb.getString( ToolManagerArb.BROWSE_ICON_BUTTON_LABEL )
    );
    btnBrowse.setMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.BROWSE_ICON_MNEMONIC )
    );
    labToolTip.setText(
      ToolManagerArb.getString( ToolManagerArb.TOOLTIP_LABEL )
    );
    labToolTip.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.TOOLTIP_MNEMONIC )
    );
    labToolTip.setLabelFor(tfToolTip);
    labPreviewLabel.setText(
      ToolManagerArb.getString( ToolManagerArb.PREVIEW_LABEL )  
    );
    labPreviewLabel.setDisplayedMnemonic(
      ToolManagerArb.getMnemonic( ToolManagerArb.PREVIEW_MNEMONIC )  
    );
    labPreviewLabel.setLabelFor( labPreview );
    labPreview.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    this.add(labCaption, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(tfCaption, new GridBagConstraints(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    this.add(lblIconLocation, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(tfIconLocation, new GridBagConstraints(0, 6, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    this.add(btnBrowse, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
    this.add(labToolTip, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(tfToolTip, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
    this.add(labPreviewLabel, new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(labPreview, new GridBagConstraints(0, 8, 3, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
  }
}