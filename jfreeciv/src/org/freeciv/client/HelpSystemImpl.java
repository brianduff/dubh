package org.freeciv.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import org.freeciv.common.Logger;
import org.freeciv.common.RegistryParseException;
import org.freeciv.common.Shared;

/**
 * Implementation of the help system for the client.
 *
 * @author Brian Duff
 */
final class HelpSystemImpl extends JFrame implements HelpSystem
{
  private Client m_client;
  private MainWindow m_mainWindow;
  private HelpDataParser m_parser;

  private JTextArea m_ta;
  private JTree m_topicTree;

  /**
   * The MainWindow constructs an instance of this class at startup
   */
  HelpSystemImpl( Client c, MainWindow w)
  {
    m_client = c;
    m_mainWindow = w;
    URL helpURL = Shared.getDataURL( "helpdata.txt" );
    
    try
    {
      m_parser = new HelpDataParser( helpURL.openStream() );
    }
    catch (IOException ioe)
    {
      Logger.log( Logger.LOG_ERROR, 
        "Unable to initialize help system from "+helpURL );
      Logger.log( Logger.LOG_ERROR,
        "Many help topics will be unavailable" );
      Logger.log ( Logger.LOG_ERROR, ioe );
    }
    catch (RegistryParseException rpe)
    {
      Logger.log( Logger.LOG_ERROR, 
        "Unable to initialize help system from "+helpURL );
      Logger.log( Logger.LOG_ERROR,
        "Many help topics will be unavailable" );
      Logger.log ( Logger.LOG_ERROR, rpe );
    }

    m_ta = new JTextArea();
    m_ta.setEditable( false );
    m_ta.setLineWrap( true );
    m_ta.setWrapStyleWord( true );

    m_topicTree = new JTree();

    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent( new JScrollPane( m_topicTree ) );
    splitPane.setRightComponent( new JScrollPane( m_ta ) );
    
    getContentPane().setLayout( new BorderLayout() );
    getContentPane().add( splitPane );
    
    
  }

  public void showStaticTopic( String helpTopic )
  {
    if ( m_parser == null )
    {
      showNoTopic();
    }

    String topicName = m_parser.getTopicName( helpTopic );
    String topicText = m_parser.getTopicText( helpTopic );

    showTopic( topicName, topicText );
  }

  private void showNoTopic()
  {
    JOptionPane.showMessageDialog( m_mainWindow, "No help topic available", 
      m_client.APP_NAME, JOptionPane.WARNING_MESSAGE );
  }

  private void showTopic( String topicName, String topicText )
  {
    this.setTitle( m_client.APP_NAME + " Help: "+ topicName );
    m_ta.setText( topicText );
    // Force a scroll back to the top.
    m_ta.setSelectionStart( 0 );
    m_ta.setSelectionEnd( 0 );

    // text area's preferred size is stupid. 600x400 is the maximum size
    // of a dialog in Oracle, so I'm going to use it here too :)
    this.setSize( new Dimension( 600, 400 ) );
    centerOn( m_mainWindow, this );
    this.setVisible( true );
    this.toFront();
  }

  private void centerOn( JFrame parent, JFrame child )
  {
    Rectangle pRect = parent.getBounds();
    Rectangle cRect = child.getBounds();

    cRect.x = pRect.x + (( pRect.width - cRect.width ) / 2);
    cRect.y = pRect.y + (( pRect.height - cRect.height ) / 2);

    child.setBounds( cRect );
  }
}