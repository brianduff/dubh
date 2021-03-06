package org.freeciv.client.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;

/**
 * Implementation of the notify dialog.  Notify sure is a generic name
 * for this dialog.  In any case, it displays a few different reports.
 *
 * @author Ben Mazur
 */
class ImplNotify extends VerticalFlowPanel implements DlgNotify
{
  private JLabel m_labHeadline = new JLabel( "", JLabel.LEFT );
  private JScrollPane m_scpLines = new JScrollPane();
  private JTextArea m_tarLines = new JTextArea();
  private JButton m_butOK = new JButton( translate( "OK" ) );

  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;

  public ImplNotify( DialogManager mgr, Client c )
  {
    m_client = c;
    m_dlgManager = mgr;
    m_labHeadline.setFont( m_tarLines.getFont() );
    m_tarLines.setEditable( false );
    m_scpLines.setViewportView( m_tarLines );
    m_butOK.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );

    addRow( m_labHeadline );
    addSpacerRow( m_scpLines );
    addRow( m_butOK );
  }

  public void display( String caption, String headline, String lines )
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), translate( caption ), true
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplNotify.this, BorderLayout.CENTER );
    m_dialog = dlg;

    m_labHeadline.setText( translate( headline ) );
    m_tarLines.setText( translate( lines ) );
    m_tarLines.setRows( 12 );

    m_dlgManager.showDialog( m_dialog );
  }

  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }

  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
