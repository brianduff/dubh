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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Styles for the console.
 *
 * @author Brian.Duff@oracle.com
 */
class ConsoleStyles
{
  private final StyleContext m_context;
  private ConsoleSettings m_settings;

  private Style m_outputStyle;
  private Style m_inputStyle;
  private Style m_errorStyle;
  private Style m_promptStyle;

  /**
   * Construct an instance of ConsoleStyles
   */
  ConsoleStyles( ConsoleSettings settings )
  {
    m_context = new StyleContext();
    createStyles( );  
    setSettings( settings );
  }

  /**
   * Get the styleContext
   */
  public StyleContext getStyleContext()
  {
    return m_context;
  }

  /**
   * Set the console settings. The styles will be updated.
   * 
   */
  public void setSettings( ConsoleSettings settings )
  {
    m_settings = settings;
    setStyle( m_outputStyle, 
      m_settings.getOutputFont(), m_settings.getOutputColor() );
    setStyle( m_errorStyle,
      m_settings.getErrorFont(), m_settings.getErrorColor() );
    setStyle( m_inputStyle,
      m_settings.getInputFont(), m_settings.getInputColor() );  
    setStyle( m_promptStyle,
      m_settings.getPromptFont(), m_settings.getPromptColor() );
  }

  public Style getPromptStyle()
  {
    return m_promptStyle;
  }

  public Style getErrorStyle()
  {
    return m_errorStyle;
  }

  public Style getOutputStyle()
  {
    return m_outputStyle;
  }

  public Style getInputStyle()
  {
    return m_inputStyle;
  }

  private void setStyle( Style style, Font font, Color c )
  {
    StyleConstants.setForeground( style, c );
    StyleConstants.setFontFamily( style, font.getFamily() );
    StyleConstants.setFontSize( style, font.getSize() );
    StyleConstants.setBold( style, font.isBold() );
    StyleConstants.setItalic( style, font.isItalic() );
  }

  private void createStyles()
  {
    m_outputStyle = m_context.addStyle( null, null );
    m_inputStyle = m_context.addStyle( null, null );   
    m_errorStyle = m_context.addStyle( null, null );    
    m_promptStyle = m_context.addStyle( null, null );    
  }  

}