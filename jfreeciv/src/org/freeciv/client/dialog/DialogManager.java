package org.freeciv.client.dialog;

import org.freeciv.client.Client;
import org.freeciv.common.City;

import javax.swing.*;
import java.util.ArrayList;

/**
 * The dialog manager does the job of handing out dialogs
 * to whoever needs them
 */
public class DialogManager
{
  private Client m_client;
  // We can hold on to any dialogs that are capable of resetting
  // themselves on each call.
  ImplLogin m_login;
  ImplNation m_nation;
  ImplProgress m_progress;
  ImplNotify m_notify;
  ImplCityView m_cityView;
  ImplCityChangeConstruction m_cityChangeConstruction;
  ImplTaxRates m_taxRates;
  ImplFindCity m_findCity;
  ImplCityReport m_cityReport;
  ImplMilitaryReport m_militaryReport;
  ImplPlayers m_players;
  ImplTradeReport m_tradeReport;
  ImplScienceReport m_scienceReport;
  ArrayList m_alVisibleDialogs;
  public DialogManager( Client c )
  {
    m_client = c;
    m_alVisibleDialogs = new ArrayList();
  }
  protected DialogManager()
  {
    this( null );
  }
  private class DialogInvoker implements Runnable
  {
    private JDialog m_dialog;
    public DialogInvoker( JDialog d )
    {
      m_dialog = d;
    }
    public void run()
    {
      m_dialog.pack();
      m_dialog.setLocationRelativeTo( m_client.getMainWindow() );
      m_dialog.setVisible( true );
    }
  }
  private class DialogCloser implements Runnable
  {
    private JDialog m_dialog;
    public DialogCloser( JDialog d )
    {
      m_dialog = d;
    }
    public void run()
    {
      m_dialog.setVisible( false );
    }
  }
  /**
   * Packs and opens the specified dialog in the AWT event thread.
   * Remembers that the dialog is open so that a call to
   * {@link #closeAllDialogs()} will hide the dialog. You should
   * use {@link #closeDialog(JDialog)} to pop the dialog back down
   * programmatically (in response to an event e.g. a button click)
   */
  void showDialog( JDialog d )
  {
    // Remember the dialog.
    m_alVisibleDialogs.add( d );
    // Pack & display
    SwingUtilities.invokeLater( new DialogInvoker( d ) );
  }
  /**
   * Hides the specified dialog and forgets about it for the purposes
   * of {@link #closeAllDialogs()}.
   */
  void hideDialog( JDialog d )
  {
    m_alVisibleDialogs.remove( d );
    SwingUtilities.invokeLater( new DialogCloser( d ) );
  }
  /**
   * Closes all dialogs that have been opened using {@link #openDialog(JDialog)}
   * but not yet closed with {@link #closeDialog(JDialog)}.
   */
  public void hideAllDialogs()
  {
    for( int i = 0;i < m_alVisibleDialogs.size();i++ )
    {
      JDialog d = (JDialog)m_alVisibleDialogs.get( i );
      SwingUtilities.invokeLater( new DialogCloser( d ) );
    }
  }
  
  /**
   * Refreshes the city view dialog if it is showing the specified city.
   */
  public void refreshCityDialog( City city )
  {
    if( getCityViewDialog().isShowing() 
        && city.equals( getCityViewDialog().getCity() ) )
    {
      getCityViewDialog().refresh();
    }
  }
  
  public DlgNotifyGoto getNotifyGotoDialog()
  {
    return null;
  }
  public DlgNation getNationDialog()
  {
    if( m_nation == null )
    {
      m_nation = new ImplNation( this, m_client );
    }
    return m_nation;
  }
  public DlgLogin getLoginDialog()
  {
    if( m_login == null )
    {
      m_login = new ImplLogin( this, m_client );
    }
    return m_login;
  }
  public DlgProgress getProgressDialog()
  {
    if( m_progress == null )
    {
      m_progress = new ImplProgress( m_client );
    }
    return m_progress;
  }
  public DlgNotify getNotifyDialog()
  {
    if( m_notify == null )
    {
      m_notify = new ImplNotify( this, m_client );
    }
    return m_notify;
  }
  public DlgCityView getCityViewDialog()
  {
    if( m_cityView == null )
    {
      m_cityView = new ImplCityView( this, m_client );
    }
    return m_cityView;
  }
  public DlgCityChangeConstruction getCityChangeConstructionDialog()
  {
    if( m_cityChangeConstruction == null )
    {
      m_cityChangeConstruction = new ImplCityChangeConstruction( this, m_client );
    }
    return m_cityChangeConstruction;
  }
  public DlgTaxRates getTaxRatesDialog()
  {
    if( m_taxRates == null )
    {
      m_taxRates = new ImplTaxRates( this, m_client );
    }
    return m_taxRates;
  }
  public DlgFindCity getFindCityDialog()
  {
    if( m_findCity == null )
    {
      m_findCity = new ImplFindCity( this, m_client );
    }
    return m_findCity;
  }
  public DlgCityReport getCityReport()
  {
    if( m_cityReport == null )
    {
      m_cityReport = new ImplCityReport( m_client );
    }
    return m_cityReport;
  }
  public DlgMilitaryReport getMilitaryReport()
  {
    if( m_militaryReport == null )
    {
      m_militaryReport = new ImplMilitaryReport( this, m_client );
    }
    return m_militaryReport;
  }
  public DlgPlayers getPlayersDialog()
  {
    if( m_players == null )
    {
      m_players = new ImplPlayers( this, m_client );
    }
    return m_players;
  }
  public DlgTradeReport getTradeReportDialog()
  {
    if( m_tradeReport == null )
    {
      m_tradeReport = new ImplTradeReport( this, m_client );
    }
    return m_tradeReport;
  }
  public DlgScienceReport getScienceReportDialog()
  {
    if( m_scienceReport == null )
    {
      m_scienceReport = new ImplScienceReport( this, m_client );
    }
    return m_scienceReport;
  }
  class MessageDialogRunnable implements Runnable
  {
    private String m_title, m_message;
    public MessageDialogRunnable( String title, String message )
    {
      m_title = title;
      m_message = message;
    }
    public void run()
    {
      JOptionPane.showMessageDialog(
        m_client.getMainWindow(), m_title, m_message, JOptionPane.ERROR_MESSAGE
      );
    }
  }
  /**
   * Display a message dialog, and return immediately
   *
   * @param message the message to display in the dialog
   */
  public void showMessageDialogNonBlocking( String message )
  {
    showMessageDialogNonBlocking( m_client.APP_NAME, message );
  }

  /**
   * Display a message dialog. Block until the user dismisses the dialog
   *
   * @param message the message to display
   */
  public void showMessageDialog( String message )
  {
    showMessageDialogBlocking( m_client.APP_NAME, message );
  }

  /**
   * @deprecated use showMessageDialogNonBlocking( String )
   */
  public void showMessageDialogNonBlocking( String title, String message )
  {
    SwingUtilities.invokeLater( new MessageDialogRunnable( title, message ) );
  }
  /**
   * @deprecated use showMessageDialog( String )
   */
  public void showMessageDialogBlocking( String title, String message )
  {
    JOptionPane.showMessageDialog(
      m_client.getMainWindow(), message, title, JOptionPane.WARNING_MESSAGE );
  }
  
  /**
   * Display a yes/no confirmation dialog.  Blocking, of course.
   */
  public int showConfirmationDialog( String message )
  {
    return JOptionPane.showConfirmDialog( m_client.getMainWindow(),
                                          message,
                                          m_client.APP_NAME,
                                          JOptionPane.YES_NO_OPTION );
  }
  
  /**
   * Display dialog asking for the user to type something in.
   */
  public String showInputDialog( String message, String initialSelectionValue )
  {
    JOptionPane pane = new JOptionPane( message,
                                   JOptionPane.QUESTION_MESSAGE,
                                   JOptionPane.OK_CANCEL_OPTION );
    pane.setWantsInput( true );
    pane.setInitialSelectionValue( initialSelectionValue );
    
    JDialog dialog = pane.createDialog( m_client.getMainWindow(),
                                        m_client.APP_NAME );
    dialog.show();
    
    return (String)pane.getInputValue();
  }
}
