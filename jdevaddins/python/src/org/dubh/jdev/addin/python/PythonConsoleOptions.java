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

import java.awt.Color;
import java.awt.Font;

import oracle.ide.util.Copyable;

import org.dubh.ui.console.ConsoleSettings;

/**
 * The options object for the python console.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonConsoleOptions implements Copyable
{
  // Very simple wrapper round the ConsoleSettings object.

  private ConsoleSettings m_settings;

  private String[] m_fontNames = new String[4];
  private int[] m_fontSizes = new int[4];
  private boolean[] m_fontBolds = new boolean[4];
  private boolean[] m_fontItalics = new boolean[4];
  private int[] m_fontReds = new int[4];
  private int[] m_fontGreens = new int[4];
  private int[] m_fontBlues = new int[4];

  private int m_bgRed;
  private int m_bgGreen;
  private int m_bgBlue;
  
  private static final int INPUT = 0, OUTPUT = 1, PROMPT = 2, ERROR = 3;

  public PythonConsoleOptions()
  {
    fromConsoleSettings( new ConsoleSettings() );
  }

  public String[] getFontNames()
  {
    return m_fontNames;
  }

  public void setFontNames( String[] n )
  {
    m_fontNames = n;
  }

  public int[] getFontSizes()
  {
    return m_fontSizes;
  }

  public void setFontSizes( int[] sizes )
  {
    m_fontSizes = sizes;
  }

  public boolean[] getFontBolds()
  {
    return m_fontBolds;
  }

  public void setFontBolds( boolean[] bolds )
  {
    m_fontBolds = bolds;
  }

  public boolean[] getFontItalics()
  {
    return m_fontItalics;
  }

  public void setFontItalics( boolean[] italics )
  {
    m_fontItalics = italics;
  }

  public int[] getFontReds()
  {
    return m_fontReds;
  }

  public void setFontReds( int[] reds)
  {
    m_fontReds = reds;
  }

  public int[] getFontGreens()
  {
    return m_fontGreens;
  }

  public void setFontGreens( int[] g)
  {
    m_fontGreens = g;
  }

  public int[] getFontBlues()
  {
    return m_fontBlues;
  }

  public void setFontBlues( int[] c)
  {
    m_fontBlues = c;
  }  

  public int getBgRed()
  {
    return m_bgRed;
  }

  public void setBgRed( int red )
  {
  
    m_bgRed = red;
  }

  public int getBgGreen()
  {
    return m_bgGreen;
  }

  public void setBgGreen( int green )
  {
    m_bgGreen = green;
  }

  public int getBgBlue()
  {
    return m_bgBlue;
  }

  public void setBgBlue( int blue )
  {
    m_bgBlue = blue;
  }

  public void fromConsoleSettings( ConsoleSettings cs )
  {
    _set( INPUT, cs.getInputFont(), cs.getInputColor() );
    _set( OUTPUT, cs.getOutputFont(), cs.getOutputColor() );
    _set( PROMPT, cs.getPromptFont(), cs.getPromptColor() );
    _set( ERROR, cs.getErrorFont(), cs.getErrorColor() );

    m_bgRed = cs.getBackgroundColor().getRed();
    m_bgGreen = cs.getBackgroundColor().getGreen();
    m_bgBlue = cs.getBackgroundColor().getBlue();
    
    m_settings = cs;
  }

  private void _set( int index, Font font, Color color )
  {
    m_fontNames[index] = font.getName();
    m_fontSizes[index] = font.getSize() ;
    m_fontBolds[index] = font.isBold();
    m_fontItalics[index] = font.isItalic();
    m_fontReds[index] = color.getRed();
    m_fontGreens[index] = color.getGreen();
    m_fontBlues[index] = color.getBlue();
  }

  private Font _getFont( int index )
  {
    return new Font( m_fontNames[index], Font.PLAIN + 
      (m_fontBolds[index] ? Font.BOLD : 0) +
      (m_fontItalics[index] ? Font.ITALIC : 0),
      m_fontSizes[index]
    );
  }

  private Color _getColor( int index )
  {
    return new Color( m_fontReds[index], m_fontGreens[index], m_fontBlues[index] );
  }

  public ConsoleSettings toConsoleSettings( )
  {
    ConsoleSettings settings = new ConsoleSettings();
    settings.setInputFont( _getFont( INPUT ) );
    settings.setOutputFont( _getFont( OUTPUT ) );
    settings.setErrorFont( _getFont( ERROR ) );
    settings.setPromptFont( _getFont( PROMPT ) );
    settings.setInputColor( _getColor( INPUT ) );
    settings.setOutputColor( _getColor( OUTPUT ) );
    settings.setErrorColor( _getColor( ERROR ) );
    settings.setPromptColor( _getColor( PROMPT ) );

    settings.setBackgroundColor( new Color(
      m_bgRed, m_bgGreen, m_bgBlue
    ));
    return settings;
  }

// ---------------------------------------------------------------------------
// Copyable implementation
// ---------------------------------------------------------------------------

  public Object copyTo( final Object object )
  {
    PythonConsoleOptions copy = ( object != null ? (PythonConsoleOptions)object : new PythonConsoleOptions() );
    copy.fromConsoleSettings( this.toConsoleSettings() );
    return copy;
  }

}