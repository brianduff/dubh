package org.freeciv.client.dialog;
import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
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
  public ImplLogin( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;
    setupServerPanel();
    setupMetaServerPanel();
    cancel = new ActionButton( m_client.getAction( "ACTQuit" ) );
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
        int iport = 0;
        try
        {
          iport = Integer.parseInt( port.getText() );
        }
        catch( NumberFormatException e )
        {
          JOptionPane.showMessageDialog( dialog, _( "Port is not a number" ), _( "Error" ), JOptionPane.ERROR_MESSAGE );
          return ;
        }
        try
        {
          Socket civserver = new Socket( server.getText(), iport );
          input = civserver.getInputStream();
          output = civserver.getOutputStream();
          returnValue = 1;
          m_client.setConnected( true );
          dialog.setVisible( false );
          return ;
        }
        catch( UnknownHostException e )
        {
          JOptionPane.showMessageDialog( dialog, "Unknown host " + server.getText(), "Error connecting", JOptionPane.ERROR_MESSAGE );
          return ;
        }
        catch( IOException e )
        {
          JOptionPane.showMessageDialog( dialog, e.toString(), _( "Error connecting" ), JOptionPane.ERROR_MESSAGE );
          return ;
        }
      }
    } );
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
    //SwingUtilities.invokeLater(new DisplayerRunnable(f));
    showDialog( m_client );
    dialog.dispose();
  }
  public boolean isOK()
  {
    return ( returnValue == 1 );
  }
  public InputStream getInputStream()
  {
    return input;
  }
  public OutputStream getOutputStream()
  {
    return output;
  }
  public String getServerName()
  {
    return server.getText();
  }
  public int getPortNumber()
  {
    return Integer.parseInt( port.getText() );
  }
  public String getUserName()
  {
    return name.getText();
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
