package org.freeciv.client.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.freeciv.common.ErrorHandler;

/**
 * The console window is an output area and an input field
 *
 * @author Brian Duff
 */
public class Console extends JPanel
{
  // TODO: Implement history etc.

  private JTextArea m_outputArea;
  private JTextField m_inputField;

  private List m_listeners;

  public Console( )
  {
    m_outputArea = new JTextArea();
    m_inputField = new JTextField();

    m_listeners = new ArrayList( 1 );

    m_outputArea.setRows( 5 );
    m_outputArea.setEditable( false );

    setLayout( new BorderLayout() );
    add( new JScrollPane( m_outputArea ), BorderLayout.CENTER );
    add( m_inputField, BorderLayout.SOUTH );

    m_inputField.addActionListener( new InputFieldListener() );
  }


  /**
   * Write a message to the output area followed by a carriage return
   *
   * @param msg the message to write
   */
  public void println( String msg )
  {
    m_outputArea.append( msg );
    m_outputArea.append( "\n" );
    int last = m_outputArea.getText().length() - 1;
    m_outputArea.select( last, last );
  }

  /**
   * Get the current contents of the input field
   *
   * @return a string containing the text currently in the input field
   */
  public String getInput()
  {
    return m_inputField.getText();
  }

  /**
   * Add a listener which is notified whenever the user wants to execute the
   * command in the input field. On receiving this event, you can determine
   * the command to execute by alling getInput().
   *
   * @param al a listener which will be notified when to act on the input
   *    in the input field.
   *
   */
  public void addInputListener( ActionListener al )
  {
    if ( al == null ) return;
    m_listeners.add( al );
  }

  /**
   * Remove a listener previously added with addInputListener()
   *
   * @param al the listener to remove
   */
  public void removeInputListener( ActionListener al )
  {
    if ( al == null ) return;
    m_listeners.remove( al );
  }


  /**
   * Inner class that listens for <enter> on the input field, fires events
   * to all our listeners, then clears the contents of the field.
   */
  private class InputFieldListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      try
      {
        // First, notify all listeners added to the console.
        Iterator i = m_listeners.iterator();
        while ( i.hasNext() )
        {
          ((ActionListener)i.next()).actionPerformed( e );
        }

        // Now clear the field.
        m_inputField.setText("");
      }
      catch ( RuntimeException re )
      {
        ErrorHandler.getHandler().internalError( re );
      }
    }
  }
}