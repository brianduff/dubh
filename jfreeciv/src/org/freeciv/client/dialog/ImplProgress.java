package org.freeciv.client.dialog;
import org.freeciv.client.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
class ImplProgress extends JPanel implements DlgProgress
{
  private Client m_client;
  private DialogManager m_dlgManager;
  private JLabel m_labMainMessage;
  private JProgressBar m_pbProgress;
  private JButton m_butCancel;
  private JDialog m_dialog;
  private Class m_clsCurrent;
  public ImplProgress( Client c ) 
  {
    m_client = c;
    m_dlgManager = c.getDialogManager();
    m_pbProgress = new JProgressBar();
    m_pbProgress.setStringPainted( true );
    m_labMainMessage = new JLabel();
    m_butCancel = new JButton( _( "Cancel" ) );
    setLayout( new BorderLayout() );
    add( m_labMainMessage, BorderLayout.NORTH );
    add( m_pbProgress, BorderLayout.CENTER );
    add( m_butCancel, BorderLayout.SOUTH );
    m_butCancel.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        m_client.setConnected( false );
        m_client.getDialogManager().hideAllDialogs();
      }
    } );
  }
  // DlgProgressDialog interface
  public void display( String baseMessage, int numSteps )
  {
    JDialog dlg = new JDialog( m_client, _( "Progress" ), true );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( this, BorderLayout.CENTER );
    m_dialog = dlg;
    m_labMainMessage.setText( baseMessage );
    m_pbProgress.setMaximum( numSteps );
    m_pbProgress.setMinimum( 0 );
    m_pbProgress.setValue( 0 );
    m_dlgManager.showDialog( m_dialog );
  }
  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }
  class AdvancerRunnable implements Runnable
  {
    private String m_message;
    public AdvancerRunnable( String message ) 
    {
      m_message = message;
    }
    public void run()
    {
      m_pbProgress.setString( m_message );
      m_pbProgress.setValue( m_pbProgress.getValue() + 1 );
    }
  }
  /**
   * If pi is a different class from the current class, update the progress
   * by one.
   */
  public void updateProgress( ProgressItem pi )
  {
    if( !pi.getClass().equals( m_clsCurrent ) )
    {
      m_clsCurrent = pi.getClass();
      SwingUtilities.invokeLater( new AdvancerRunnable( pi.getProgressString() ) );
    }
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
