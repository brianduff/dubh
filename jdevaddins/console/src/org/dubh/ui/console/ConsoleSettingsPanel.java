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
 * The Original Code is The Dubh.Org Console Control.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.ui.console;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.JColorChooser;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dubh.ui.control.ColorIcon;
import javax.swing.JCheckBox;

/**
 * A UI Panel that can be used to edit a ConsoleSettings object.
 * 
 * @author Brian.Duff@oracle.com
 */
public class ConsoleSettingsPanel extends JPanel
{
  GridBagLayout gbl = new GridBagLayout();
  JLabel lblFontName = new JLabel();
  JComboBox cbFontName = new JComboBox();
  JLabel lblFontSize = new JLabel();
  JComboBox cbFontSize = new JComboBox();
  JLabel lblFgColor = new JLabel();
  JButton butFgColor = new JButton();
  JLabel lblStyle = new JLabel();
  JComboBox cbStyle = new JComboBox();
  JLabel lblBgColor = new JLabel();
  JButton butBgColor = new JButton();
  JLabel lblSample = new JLabel();
  JScrollPane spSample = new JScrollPane();
  JTextPane tpSample = new JTextPane();

  ColorIcon icoFgColor = new ColorIcon( Color.black );
  ColorIcon icoBgColor = new ColorIcon( Color.white );

  private static final Insets SQUARE_INSETS = new Insets( 1, 1, 1, 1 );

  private ConsoleSettings m_settings = null;
  private ConsoleStyles m_styles = null;

  private static final int INPUT = 0, OUTPUT = 1, ERROR = 2, PROMPT = 3;
  JLabel lblFontStyle = new JLabel();
  JCheckBox chkBold = new JCheckBox();
  JCheckBox chkItalic = new JCheckBox();

  private boolean m_protected = false;

  public ConsoleSettingsPanel()
  {
    
    try
    {
      jbInit();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }

    populateFont();
    populateSizes();
    populateStyles();

    ConsoleSettings settings = new ConsoleSettings();
    m_styles = new ConsoleStyles( settings );
    tpSample.setDocument( new DefaultStyledDocument( m_styles.getStyleContext() ) );

    setConsoleSettings( settings );

    Listener l = new Listener();
    butFgColor.addActionListener( l );
    butBgColor.addActionListener( l );
    cbStyle.addActionListener( l );
    cbFontName.addActionListener( l );
    cbFontSize.addActionListener( l );
    chkBold.addActionListener( l );
    chkItalic.addActionListener( l );

    updateSample();
    
  }
  
  private void jbInit() throws Exception
  {

    this.setLayout(gbl);
    lblFontName.setText("Font Name:");
    lblFontName.setLabelFor(cbFontName);
    lblFontSize.setText("Font Size:");
    lblFontSize.setLabelFor(cbFontSize);
    lblFgColor.setText("Foreground Color:");
    lblFgColor.setLabelFor(butFgColor);
    butFgColor.setMargin( SQUARE_INSETS );
    butFgColor.setDefaultCapable( false );
    butFgColor.setIcon( icoFgColor );
    lblStyle.setText("Text Type:");
    lblStyle.setLabelFor(cbStyle);
    lblBgColor.setText("Background Color:");
    lblBgColor.setLabelFor(butBgColor);
    butBgColor.setMargin( SQUARE_INSETS );
    butBgColor.setIcon( icoBgColor );
    butBgColor.setDefaultCapable( false );
    lblSample.setText("Sample:");
    lblSample.setLabelFor(tpSample);
    tpSample.setText("Empty");
    tpSample.setEditable(false);
    lblFontStyle.setText("Font Style:");
    chkBold.setText("Bold");
    chkItalic.setText("Italic");

    this.add(lblFontName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 20, 2, 2), 0, 0));
    this.add(cbFontName, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(lblFontSize, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 20, 2, 2), 0, 0));
    this.add(cbFontSize, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(lblFgColor, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 20, 2, 2), 0, 0));
    this.add(butFgColor, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(lblStyle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    this.add(cbStyle, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    this.add(lblBgColor, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(8, 2, 2, 2), 0, 0));
    this.add(butBgColor, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(8, 2, 2, 2), 0, 0));
    this.add(lblSample, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    spSample.getViewport().add(tpSample, null);
    this.add(spSample, new GridBagConstraints(0, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 200, 100));
    this.add(lblFontStyle, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 20, 2, 2), 0, 0));
    this.add(chkBold, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
    this.add(chkItalic, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 20, 2, 2), 0, 0));
  }

  /**
   * Populate the font combo with all fonts defined by the JDK
   */
  private void populateFont()
  {
    cbFontName.removeAllItems();
    String[] names = 
      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    for ( int i=0; i < names.length; i++ )
    {
      cbFontName.addItem( names[i] );
    }
  }

  /**
   * Populate the style names
   *
   */
  private void populateStyles()
  {
    cbStyle.removeAllItems();
    cbStyle.addItem( "Input" );
    cbStyle.addItem( "Output" );
    cbStyle.addItem( "Error" );
    cbStyle.addItem( "Prompt" );
  }

  /**
   * Populate the font sizes
   *
   */
  private void populateSizes()
  {
    cbFontSize.removeAllItems();
    for ( int i=7; i < 25; i++ )
    {
      cbFontSize.addItem( new Integer( i ) );
    }
  }

  private void updateSample()
  {
    try
    {
      if ( m_styles != null && m_settings != null )
      {
        m_styles.setSettings( m_settings );
      }
      
      tpSample.setBackground( m_settings.getBackgroundColor() );
      tpSample.setText("");
      tpSample.getDocument().insertString( tpSample.getDocument().getLength(),
        "prompt% ", m_styles.getPromptStyle()
      );
      tpSample.getDocument().insertString( tpSample.getDocument().getLength(),
        "System.out.println(\"Hello World\")\n", m_styles.getInputStyle()
      );
      tpSample.getDocument().insertString( tpSample.getDocument().getLength(),
        "Hello World\n", m_styles.getOutputStyle()
      );
      tpSample.getDocument().insertString( tpSample.getDocument().getLength(),
        "Stack Overflow Error\n", m_styles.getErrorStyle()
      );
    }
    catch (BadLocationException ble )
    {
      
    }
  }

  /**
   * Set the console settings that are being displayed. The settings object
   * will not be modified by this method.
   * 
   * @param settings the console settings 
   */
  public void setConsoleSettings( final ConsoleSettings settings )
  {
    m_settings = new ConsoleSettings( settings );
    
    icoBgColor.setColor( m_settings.getBackgroundColor() );
    butBgColor.repaint();

    changeStyle();
    
    updateSample();
  }

  private void setDisplayedStyle( Font f, Color c )
  {
    try
    {

      // Stop this method from firing events
      m_protected =true;

      
      boolean found = false;
      for ( int i=0; i < cbFontName.getModel().getSize(); i++ )
      {
        String item = (String) cbFontName.getModel().getElementAt( i );
        if ( item.equals( f.getName() ) )
        {
          cbFontName.setSelectedIndex( i );
          found = true;
          break;
        }
      }
      if ( !found )
      {
        cbFontName.addItem( f.getName() );

        cbFontName.setSelectedIndex( cbFontName.getModel().getSize() -1 );
      }

      found = false;
      for ( int i=0; i < cbFontSize.getModel().getSize(); i++ )
      {
        Integer item = (Integer) cbFontSize.getModel().getElementAt( i );
        if ( item.intValue() == f.getSize() )
        {
          cbFontSize.setSelectedIndex( i );
          found = true;
          break;
        }
      }

      if ( !found )
      {
        cbFontSize.addItem( new Integer( f.getSize() ) );
        cbFontSize.setSelectedIndex( cbFontSize.getModel().getSize() - 1 );

      }

      chkBold.setSelected( f.isBold() );
      chkItalic.setSelected( f.isItalic() );
    

      icoFgColor.setColor( c );
      butFgColor.repaint();
    }
    finally
    {
      m_protected = false;
    }
    
  }

  /**
   * Get the current console settings. This will return a new instance of
   * ConsoleSettings. 
   * 
   * @return a new instance of ConsoleSettings set to the current choices in 
   *    this panel.
   */
  public ConsoleSettings getConsoleSettings()
  {
    return m_settings;
  }

  private void changeForegroundColor()
  {
    
    Color c = JColorChooser.showDialog( this, "Choose Foreground Color", 
      icoFgColor.getColor()
    );

    if ( c == null ) return;
    
    icoFgColor.setColor( c );
    butFgColor.repaint();

    switch (cbStyle.getSelectedIndex() )
    {
      case ERROR:
        m_settings.setErrorColor( c );
        break;
      case OUTPUT:
        m_settings.setOutputColor( c );
        break;
      case INPUT: 
        m_settings.setInputColor( c );
        break;
      case PROMPT:
        m_settings.setPromptColor( c );
        break;
    }

    // Refresh the styles
    m_styles.setSettings( m_settings );
    
    updateSample();
  }

  private void changeBackgroundColor()
  {
    Color c = JColorChooser.showDialog( this, "Choose Background Color", 
      icoFgColor.getColor()
    );

    if ( c != null )
    {
      icoBgColor.setColor( c );
      butBgColor.repaint();    
      m_settings.setBackgroundColor( c );
    }
    
    updateSample();
  }

  private void changeStyle()
  {
    switch ( cbStyle.getSelectedIndex() )
    {
      case INPUT:
        setDisplayedStyle( m_settings.getInputFont(), m_settings.getInputColor() );
        break;
      case OUTPUT:
        setDisplayedStyle( m_settings.getOutputFont(), m_settings.getOutputColor() );
        break;
      case PROMPT:
        setDisplayedStyle( m_settings.getPromptFont(), m_settings.getPromptColor() );
        break;
      case ERROR:
        setDisplayedStyle( m_settings.getErrorFont(), m_settings.getErrorColor() );
        break;
    }
  }

  private void changeFont()
  {
    Font f = new Font( 
      (String)cbFontName.getSelectedItem(),
      Font.PLAIN + (chkBold.isSelected() ? Font.BOLD : 0 ) + ( chkItalic.isSelected() ? Font.ITALIC : 0 ),
      ((Integer)cbFontSize.getSelectedItem()).intValue()
    );
    switch ( cbStyle.getSelectedIndex() )
    {
      case INPUT:
        m_settings.setInputFont( f );
        setDisplayedStyle( f, m_settings.getInputColor() );
        break;
      case OUTPUT:
        m_settings.setOutputFont( f );
        setDisplayedStyle( f, m_settings.getOutputColor() );
        break;
      case ERROR:
        m_settings.setErrorFont( f );
        setDisplayedStyle( f, m_settings.getErrorColor() );
        break;
      case PROMPT:
        m_settings.setPromptFont( f );
        setDisplayedStyle( f, m_settings.getPromptColor() );
        break;
    }

    m_styles.setSettings( m_settings );
    updateSample();
  }



  private class Listener implements ActionListener
  {
    public void actionPerformed( ActionEvent ae )
    {
      if ( m_protected ) return;
      
      if ( ae.getSource() == butFgColor )
      {
        changeForegroundColor();
      }
      else if ( ae.getSource() == butBgColor )
      {
        changeBackgroundColor();
      }
      else if ( ae.getSource() == cbStyle )
      {
        changeStyle();
      }
      else if ( ae.getSource() == cbFontName )
      {
        changeFont();
      }
      else if ( ae.getSource() == cbFontSize )
      {
        changeFont();
      }
      else if ( ae.getSource() == chkBold )
      {
        changeFont();
      }
      else if ( ae.getSource() == chkItalic )
      {
        changeFont();
      }
    }
  }


  public static void main( String[] args )
  {
    javax.swing.JFrame f = new javax.swing.JFrame();
    f.setContentPane( new ConsoleSettingsPanel() );
    f.pack();
    f.setVisible( true );
  }


}
