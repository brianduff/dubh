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
 * A "console" UI component. A text pane with special highlighting for
 * console prompt, input, output and errors and the ability to constrain
 * input to a particular part of the document.
 * 
 * The console also has support for a command history and configurable
 * styles.
 *
 * @author Brian.Duff@dubh.org
 */
public class Console extends JTextPane
{

  private DefaultStyledDocument m_document;

  private Interpreter m_interpreter;

  private String m_prompt;
  private int m_promptPosition;

  private PrintWriter m_errorWriter, m_outputWriter;

  private boolean m_hasContextMenu = true;
  private JPopupMenu m_contextMenu;

  private ConsoleSettings m_settings;
  private ConsoleStyles m_styles;

  private Keymap m_keyMap;

  static ConsoleAction
    ACT_INVOKE_COMMAND = new InvokeCommandAction(),
    ACT_DELETE_PREV_CHAR = new DeletePrevCharAction(),
    ACT_DELETE_NEXT_CHAR = new DeleteNextCharAction(),
    ACT_PASTE = new PasteAction(),
    ACT_CUT = new CutAction(),
    ACT_PREV_COMMAND = new PreviousCommandAction(),
    ACT_NEXT_COMMAND = new NextCommandAction(),
    ACT_CLEAR_INPUT = new ClearInputAction();

  private final ConsoleAction CLEAR_ACTION;

  /**
   * Construct the console with the specified default prompt
   * 
   * @param prompt the console prompt.
   */
  public Console( final String prompt )
  {
    m_settings = new ConsoleSettings();
    m_styles = new ConsoleStyles( m_settings );
    m_document = new DefaultStyledDocument( m_styles.getStyleContext() );
    setDocument( m_document );

    m_keyMap = super.addKeymap( "ConsoleKeymap", getKeymap(DEFAULT_KEYMAP) );
    registerKeymapActions( m_keyMap );
    this.setKeymap( m_keyMap );

    m_prompt = prompt;
    prompt();

    CLEAR_ACTION = new ClearAction( this );
    addMouseListener( new ContextMouseListener() );
  }

  /**
   * Construct an instance of Console with the default prompt
   */
  public Console()
  {
    this( "console%" );
  }


  /**
   * You can use this method to hook into the keymap of this console and 
   * provide additional actions or override the default actions used
   * when the user presses certain keys in the console. 
   */
  public void registerAction( KeyStroke ks, ConsoleAction action )
  {
    m_keyMap.addActionForKeyStroke( ks, action );
  }

  /**
   * Set the settings to use for this console. 
   */
  public void setSettings( final ConsoleSettings settings )
  {
    m_settings = settings;
    m_styles.setSettings( settings );
    setBackground( settings.getBackgroundColor() );
    repaint();
    prompt();
  }

  /**
   * Get the settings being used for this console. If you want settings to
   * remain persistent, you should persist this object.
   * 
   * @return console settings
   */
  public ConsoleSettings getSettings()
  {
    return m_settings;
  }
  /**
   * Set whether this console has a context menu. By default, this is true
   */
  public final void setContextMenuAvailable( final boolean isAvailable )
  {
    m_hasContextMenu = isAvailable;
  }

  /**
   * Is the context menu available for this console?
   */
  public final boolean isContextMenuAvailable()
  {
    return m_hasContextMenu;
  }

  /**
   * Set the prompt being displayed. The prompt will be updated when the
   * current command completes.
   * 
   * @param prompt the new prompt to display
   */
  public final void setPrompt( String prompt )
  {
    m_prompt = prompt;
  }

  /**
   * Set the interpreter for the console. The console will send commands
   * to the interpreter when the user enters commands
   * 
   * @param interpreter the interpreter the console should use
   */
  public final void setInterpreter( final Interpreter interpreter )
  {
    m_interpreter = interpreter;
  }

  /**
   * Get the interpreter for this console.
   * 
   * @return the interpreter
   */
  public final Interpreter getInterpreter()
  {
    return m_interpreter;
  }

  /**
   * Get a printwriter you can send output to
   * 
   */
  public final PrintWriter getOutputWriter()
  {
    if ( m_outputWriter == null )
    {
      m_outputWriter = new PrintWriter( new ConsoleWriter( this, m_styles.getOutputStyle() ) );
    }
    return m_outputWriter;
  }

  /**
   * Get the PrintWriter you can send errors to
   */
  public final PrintWriter getErrorWriter()
  {
    if ( m_errorWriter == null )
    {
      m_errorWriter = new PrintWriter( new ConsoleWriter( this, m_styles.getErrorStyle() ) );
    }
    return m_errorWriter;
  }

  /**
   * Reset the console. The console is cleared and a prompt is displayed
   */
  public final void reset()
  {
    clear();
    prompt();
  }

  /**
   * Clear the console. You should probably call prompt after calling this to
   * ensure that the console has been reset properly.
   */
  final void clear()
  {
    setText( "" );
  }

  /**
   * Insert the console prompt
   */
  final void prompt()
  {
    try
    {
      // Make sure we're at the start of the line. If not, insert a newline.
      if ( getDocument().getLength() > 1 )
      {
        char lastChar = getDocument().getText( getDocument().getLength()-1, 1 ).charAt( 0 );
        if ( lastChar != '\n' )
        {
          newline();
        }
      }
      appendPrompt( m_prompt );
    }
    catch (BadLocationException ble )
    {
      
    }
    finally
    {
      appendInput(" "); // To change the style back to input style.
      m_promptPosition = getDocument().getLength();      
      setCaretPosition( getDocument().getLength() );      
    }
  }

  /**
   * Get the position of the character immediately after the prompt
   * 
   * @return the current prompt position
   */
  final int getPromptPosition()
  {
    return m_promptPosition;
  }

  /**
   * Insert a newline in the document. 
   */
  final void newline()
  {
    try
    {
      getDocument().insertString( getDocument().getLength(), "\n", null );
    }
    catch (BadLocationException ble )
    {
      
    }
  }

  /**
   * Get the current command buffer contents
   */
  final String getCurrentCommand()
  {
    try
    {
      return getDocument().getText( 
        m_promptPosition, getDocument().getLength() - m_promptPosition );
    }
    catch (BadLocationException ble )
    {
      
    }
    return "";
  }


  /**
   * Programmatically append output to the console
   * 
   * @param output the output to append
   */
  public void appendOutput( String output )
  {
    append( output, m_styles.getOutputStyle() );
  }

  /**
   * Programmatically append error messages to the console
   * 
   * @param error the error text to append
   */
  public void appendError( String error )
  {
    append( error, m_styles.getErrorStyle() );
  }

  /**
   * Programmatically append input to the console. 
   * 
   * @param input the input text to append
   */
  public void appendInput( String input )
  {
    append( input, m_styles.getInputStyle() );
  }

  private void appendPrompt( String output )
  {
    append( output, m_styles.getPromptStyle() );
  }

  /**
   * Replace the current input line with the specified text.
   */
  final void setInput( String input )
  {
    try
    {
      m_document.remove( m_promptPosition, m_document.getLength()-m_promptPosition );
      appendInput( input );
    }
    catch (BadLocationException ble )
    {
      ble.printStackTrace();
    }
  }


  private final void registerKeymapActions( Keymap map )
  {
    // Stop the user being destructive in parts of the console where they
    // oughtn't to be. 
    //
    // This code is dodgy, because the keystrokes used here depend on the 
    // look and feel. Not sure how to solve this short of checking the LAF
    // ugh
    map.addActionForKeyStroke( 
      KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0, false ),
      ACT_INVOKE_COMMAND
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "typed \010" ), // Matches MetalLAF, WinLAF...
      ACT_DELETE_PREV_CHAR
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "DELETE" ),
      ACT_DELETE_NEXT_CHAR
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "shift DELETE" ),
      ACT_DELETE_NEXT_CHAR
    );    
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "ctrl V" ),
      ACT_PASTE
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "ctrl X" ),
      ACT_CUT
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "UP" ),
      ACT_PREV_COMMAND
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "DOWN" ),
      ACT_NEXT_COMMAND
    );
    map.addActionForKeyStroke(
      KeyStroke.getKeyStroke( "ctrl C" ),
      ACT_CLEAR_INPUT
    );
    


    getActionMap().get( DefaultEditorKit.copyAction ).putValue(
      ConsoleAction.NAME, "Copy"
    );    

    map.setDefaultAction( new DefaultConsoleAction() );
  }
  
  /**
   * Append some output to the console.
   * 
   * @param output the output to append
   */
  final void append( String output, Style style )
  {
    try
    {
      m_document.insertString( m_document.getLength(), output, style );
    }
    catch (BadLocationException ble )
    {
    
    }
  }



  private final void showContextMenu( int x, int y)
  {
    if ( m_contextMenu == null )
    {
      m_contextMenu = createContextMenu();
    }
    m_contextMenu.show( this, x, y );
  }

  /**
   * Create the context menu for the console. Subclasses can override this
   * and provide a different context menu, or add additional items.
   * 
   * @return a popup menu.
   */
  protected JPopupMenu createContextMenu()
  {
    JPopupMenu menu = new JPopupMenu();

    menu.add( CLEAR_ACTION );
    menu.addSeparator();
    menu.add( ACT_CUT );
    menu.add( this.getActionMap().get( DefaultEditorKit.copyAction ) );
    menu.add( ACT_PASTE );

    return menu;
  }

// ---------------------------------------------------------------------------
// Inner classes
// ---------------------------------------------------------------------------

  private class ContextMouseListener extends MouseAdapter
  {
    public void mouseReleased( MouseEvent me )
    {
      if (me.isPopupTrigger() && m_hasContextMenu)
      {
        showContextMenu( me.getX(), me.getY() );
      }
    }
  }


// ---------------------------------------------------------------------------
// Test Code
// ---------------------------------------------------------------------------

/*
  public static void main( String[] args )
  {
    javax.swing.JFrame f = new javax.swing.JFrame();
    f.getContentPane().setLayout( new java.awt.BorderLayout() );
    Console c = new Console();
    f.getContentPane().add( new javax.swing.JScrollPane( c ) );
    
    c.setInterpreter( new TestInterpreter( c ) );
    
    f.pack();
    f.setLocation( 200, 200 );
    f.setVisible( true );
  }

  private static class TestInterpreter implements Interpreter
  {
    private Console m_console;
    private TestInterpreter( Console c )
    {
      m_console = c;
    }
    public void interpret( String s )
    {
      if ( s.indexOf( "Brian" ) != -1 )
      {
        m_console.getErrorWriter().println( "Bad word in input" );
      }
      m_console.getOutputWriter().println( "The command was "+s );
      m_console.setPrompt( "New Prompt>");
    }
  }
*/
}