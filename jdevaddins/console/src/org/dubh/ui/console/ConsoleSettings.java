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

/**
 * This object controls settings for a console instance. The console component
 * does not provide a facility to persist these settings: this must be
 * provided by the user of the console component. 
 *
 * @author Brian.Duff@oracle.com
 */
public final class ConsoleSettings
{
  private Font m_inputFont, m_outputFont, m_errorFont, m_promptFont;
  private Color m_inputColor, m_outputColor, m_errorColor, m_promptColor, m_backgroundColor;

  // Hmm. 
  private static final Font DEFAULT_FONT = new Font( "Courier New", Font.PLAIN, 11 );
  private static final Color DEFAULT_BG = Color.white;
  private static final Color DEFAULT_INPUT = Color.black;
  private static final Color DEFAULT_OUTPUT = Color.blue;
  private static final Color DEFAULT_ERROR = Color.red;
  private static final Color DEFAULT_PROMPT = Color.black;

  private ConsoleHistory m_history;
    

  public ConsoleSettings()
  {
    m_inputFont = DEFAULT_FONT;
    m_outputFont = DEFAULT_FONT;
    m_errorFont = DEFAULT_FONT;
    m_promptFont = new Font( "Courier New", Font.BOLD, 11 );
    m_inputColor = DEFAULT_INPUT;
    m_outputColor = DEFAULT_OUTPUT;
    m_errorColor = DEFAULT_ERROR;
    m_promptColor = DEFAULT_PROMPT;
    m_backgroundColor = DEFAULT_BG;
    m_history = new ConsoleHistory();
  }

  public ConsoleSettings( ConsoleSettings copy )
  {
    set( copy );
  }

  /**
   * Copy the specified set of settings into this object
   */
  void set( ConsoleSettings settings )
  {
    m_inputFont = settings.m_inputFont;
    m_outputFont = settings.m_outputFont;
    m_errorFont = settings.m_errorFont;
    m_inputColor = settings.m_inputColor;
    m_outputColor = settings.m_outputColor;
    m_errorColor = settings.m_errorColor;
    m_backgroundColor = settings.m_backgroundColor;
    m_promptColor = settings.m_promptColor;
    m_promptFont = settings.m_promptFont;
    m_history = new ConsoleHistory( settings.m_history );
  }

  public ConsoleHistory getHistory()
  {
    return m_history;
  }

  public void setHistory( ConsoleHistory h )
  {
    m_history = h;
  }

  public Color getBackgroundColor()
  {
    return m_backgroundColor;
  }

  public void setBackgroundColor( Color c )
  {
    m_backgroundColor = c;
  }

  public Color getPromptColor()
  {
    return m_promptColor;
  }

  public void setPromptColor( Color c )
  {
    m_promptColor = c;
  }
  
  /**
   * Get the color used for input
   */
  public Color getInputColor()
  {
    return m_inputColor;
  }

  public void setInputColor( final Color c )
  {
    m_inputColor = c;
  }

  public Color getOutputColor()
  {
    return m_outputColor;
  }

  public void setOutputColor( final Color c )
  {
    m_outputColor = c;
  }

  public Color getErrorColor()
  {
    return m_errorColor;
  }

  public void setErrorColor( final Color c )
  {
    m_errorColor = c;
  }

  public Font getInputFont()
  {
    return m_inputFont;
  }

  public void setInputFont( final Font inputFont )
  {
    m_inputFont = inputFont;
  }

  public Font getOutputFont()
  {
    return m_outputFont;
  }

  public void setOutputFont( final Font outputFont )
  {
    m_outputFont = outputFont;
  }

  public Font getErrorFont()
  {
    return m_errorFont;
  }

  public void setErrorFont( final Font errorFont )
  {
    m_errorFont = errorFont;
  }

  public Font getPromptFont()
  {
    return m_promptFont;
  }

  public void setPromptFont( Font f )
  {
    m_promptFont = f;
  }
}