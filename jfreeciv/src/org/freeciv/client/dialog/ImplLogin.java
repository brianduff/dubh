package org.freeciv.client.dialog;

import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import org.freeciv.client.action.ACTQuit;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import javax.swing.table.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;

/**
 * An implementation of the login dialog
 */
class ImplLogin extends JPanel implements DlgLogin
{
  private static final int LABEL_WIDTH = 50;
  private static final int FIELD_WIDTH = 400;
  private static final int TABLE_WIDTH = 450;
  private static final int TABLE_HEIGHT = 125;
  private JDialog dialog;
  private VerticalFlowPanel m_panServer = new VerticalFlowPanel();
  private VerticalFlowPanel m_panMetaServer = new VerticalFlowPanel();
  private JTable m_metaTable = new JTable();
  private JTabbedPane m_tbTabs = new JTabbedPane();
  private JTextField name = new JTextField( "", 10 );
  private JTextField server = new JTextField();
  private JTextField port = new JTextField();
  private JButton ok = new JButton( _( "Connect" ) );
  private ActionButton cancel;
  private InputStream input;
  private OutputStream output;
  private int returnValue;
  private DialogManager m_dlgManager;
  private Client m_client;
  private BusyCursor m_busy;
  

  
  public ImplLogin( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;
    setupServerPanel();
    setupMetaServerPanel();
    cancel = new ActionButton( m_client.getAction( ACTQuit.class ) );
    JPanel p = new JPanel();
    p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
    p.add( ok );
    p.add( cancel );
    m_tbTabs.add( m_panServer, _( "Freeciv Server Selection" ) );
    m_tbTabs.add( m_panMetaServer, _( "Metaserver" ) );
    this.setLayout( new BorderLayout() );
    this.add( m_tbTabs, BorderLayout.CENTER );
    this.add( p, BorderLayout.SOUTH );
    ok.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent evt )
      {
        connectClicked();
      }
    } );

    
  }


  protected void connectClicked()
  {
    int iport = 0;
    try
    {
      iport = Integer.parseInt( port.getText() );
    }
    catch( NumberFormatException e )
    {
      JOptionPane.showMessageDialog( dialog, _( "Port is not a number" ), _( "Error" ), JOptionPane.WARNING_MESSAGE );
      return;
    }

    if ( iport < 0 || iport > 65535 )
    {
      JOptionPane.showMessageDialog( dialog, 
        _( "The specified port number is invalid." ), 
        m_client.APP_NAME,
        JOptionPane.WARNING_MESSAGE
      );

      return;
    }

    ok.setEnabled( false );
    cancel.setEnabled( false );
    if ( m_busy == null )
    {
      m_busy = new BusyCursor( ok );
    }
    m_busy.show( 0 );
    ConnectRunnable cr = new ConnectRunnable( server.getText(), iport );
    Thread t = new Thread( cr );
    t.start();
  }

  /**
   * Allows us to attempt the connection on another thread so that the UI
   * still repaints while attempting to connect...
   */
  private class ConnectRunnable implements Runnable
  {
    private String m_host;
    private int m_port;
    
    private ConnectRunnable( String host, int port )
    {
      m_host = host;
      m_port = port;
    }

    public void run()
    {
      try
      {
        m_client.connect( name.getText(), m_host, m_port );
        dialog.setVisible( false );
      }
      catch (UnknownHostException host)
      {
        JOptionPane.showMessageDialog( dialog, 
          "Unknown host "+ m_host,
          m_client.APP_NAME,
          JOptionPane.WARNING_MESSAGE
        );
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog( dialog, 
          "Unable to connect to a Freeciv server on "+ m_host,
          m_client.APP_NAME,
          JOptionPane.WARNING_MESSAGE
        );
      }
      finally
      {
        m_busy.hide();
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            ok.setEnabled( true );
            cancel.setEnabled( true );
          }
        });        
      }
    }

    
  }

  private void reset()
  {
    name.setText( _( "YourName" ) );
    server.setText( _( "localhost" ) );
    port.setText( _( "5555" ) );
  }
  
  private JPanel createLabelField( String label, JTextField tf )
  {
    JPanel pan = new JPanel();
    pan.setLayout( new BorderLayout() );
    JLabel lab = new JLabel( label );
    tf.setPreferredSize( new Dimension( FIELD_WIDTH, tf.getPreferredSize().height ) );
    tf.setMinimumSize( tf.getPreferredSize() );
    lab.setPreferredSize( new Dimension( LABEL_WIDTH, lab.getPreferredSize().height ) );
    pan.add( lab, BorderLayout.WEST );
    pan.add( tf, BorderLayout.CENTER );
    return pan;
  }
  
  private void setupServerPanel()
  {
    m_panServer.addRow( createLabelField( _( "Name:" ), name ) );
    m_panServer.addRow( createLabelField( _( "Host:" ), server ) );
    m_panServer.addRow( createLabelField( _( "Port:" ), port ) );
    m_panServer.addSpacerRow( new JPanel() );
  }
  
  private void setupMetaServerPanel()
  {
    m_metaTable.setModel( new MetaServerTableModel() );
    JScrollPane scr = new JScrollPane( m_metaTable );
    scr.setPreferredSize( new Dimension( TABLE_WIDTH, TABLE_HEIGHT ) );
    m_panMetaServer.addSpacerRow( scr );
    m_panMetaServer.addRow( new JButton( _( "Update" ) ) );
  }
  
  private int showDialog( JFrame jf )
  {
    dialog = new JDialog( jf, _( "Choose server" ), true );
    Container contentPane = dialog.getContentPane();
    contentPane.setLayout( new BorderLayout() );
    contentPane.add( this, BorderLayout.CENTER );
    reset();


    dialog.setDefaultCloseOperation( dialog.DO_NOTHING_ON_CLOSE );
    dialog.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent we )
      {
        // Quit. Maybe should use ACTQuit here, but I'm not sure about the
        // consequences of this.
        m_client.quit();
      }
    });

    dialog.pack();
    dialog.setLocationRelativeTo( jf );
    dialog.show();    
    
    return returnValue;
  }
  class MetaServerTableModel extends AbstractTableModel
  {
    private final String[] COLUMN_NAMES = 
    {
      _( "Server Name" ), _( "Port" ), _( "Version" ), _( "Status" ), 
      _( "Players" ), _( "Comment" )
    };
    public String getColumnName( int column )
    {
      return COLUMN_NAMES[ column ];
    }
    public int getColumnCount()
    {
      return COLUMN_NAMES.length;
    }
    public int getRowCount()
    {
      return 0; // TODO
    }
    public Object getValueAt( int row, int col )
    {
      return null;
    }
  }
  /**
   * Displays the dialog. This is guaranteed to return immediately if invoked
   * on a thread other than the AWT event thread.
   */
  public void display()
  {
    showDialog( m_client.getMainWindow() );
    dialog.dispose();
  }
  public boolean isOK()
  {
    return ( returnValue == 1 );
  }

  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
