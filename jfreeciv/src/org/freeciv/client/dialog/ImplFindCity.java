package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.Collator;
import org.freeciv.common.City;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;

/**
 * Implementation of the tax rate panel.  This updates the economy values
 * itself when "Okay" is pressed, or simply hides when "Cancel" is pressed.
 * 
 * Maybe the economy values should be updated by another class, but where?
 * 
 * @author Ben Mazur
 */
class ImplFindCity extends VerticalFlowPanel implements DlgFindCity
{
  public static final int     VISIBLE_LIST_ROWS = 12;
  
  private VerticalFlowPanel m_panCityList = new VerticalFlowPanel();
  private JList m_lisCityList = new JList();
  private JPanel m_panButtons = new JPanel();
  private JButton m_butCenter = new JButton( _( "Center" ) );
  private JButton m_butZoomTo = new JButton( _( "Zoom To City" ) );
  private JButton m_butCancel = new JButton( _( "Cancel" ) );
  
  private ArrayList cityList;
  
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
    
  public ImplFindCity( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;

    setupCityListPanel();
    setupButtonPanel();
  }

  private Client getClient()
  {
    return m_client;
  }
  
  /**
   * Initialization function; sets up the city list panel and adds it to the
   * main dialog panel.
   */
  private void setupCityListPanel()
  {
    m_panCityList.setBorder( BorderFactory.createTitledBorder( _( "Select a city:" ) ) );
    
    m_lisCityList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_lisCityList.setVisibleRowCount( VISIBLE_LIST_ROWS );
    
    m_panCityList.addSpacerRow( new JScrollPane( m_lisCityList ) );
    this.addSpacerRow( m_panCityList );
  }
  
  /**
   * Initialization function; sets up the button panel and adds it to the
   * main dialog panel.
   */
  private void setupButtonPanel()
  {
    m_butCenter.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        if( !m_lisCityList.isSelectionEmpty() )
        {
          final City city = (City)cityList.get( m_lisCityList.getSelectedIndex() );
          getClient().getMainWindow().getMapViewManager().centerOnTile( city.getX(), city.getY() );
        }
        undisplay();
      }
    } );
    m_butZoomTo.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        City city = null;
        if( !m_lisCityList.isSelectionEmpty() )
        {
          city = (City)cityList.get( m_lisCityList.getSelectedIndex() );
          getClient().getMainWindow().getMapViewManager().centerOnTile( city.getX(), city.getY() );
        }
        undisplay();
        if( city != null && city.getOwnerId() == getClient().getGame().getCurrentPlayer().getId() )
        {
          getClient().getDialogManager().getCityViewDialog().display( city );
        }
      }
    } );
    m_butCancel.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );

    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butCenter );
    m_panButtons.add( m_butZoomTo );
    m_panButtons.add( m_butCancel );
    this.addRow( m_panButtons );
  }
  
  /**
   * Resets the city list from the client in case it's out of date.
   */
  private void resetCityListFromClient()
  {
    ArrayList cityNameList = new ArrayList(); // temporary
    cityList = new ArrayList(); // less temporary
    for(int i = 0; i < getClient().getGame().getNumberOfPlayers(); i++)
    {
      Iterator ci = getClient().getGame().getPlayer( i ).getCities();
      while( ci.hasNext() ) 
      {
        City city = (City)ci.next();
        cityList.add( city ); 
        boolean sameOwner = getClient().getGame().isCurrentPlayer( i );
        cityNameList.add(
          city.getName() + 
          ( sameOwner ? "" : " (" + city.getOwner().getNation().getName() + ")" ) 
        ); 
      }
    }
    m_lisCityList.setListData( cityNameList.toArray() );
    m_lisCityList.setVisibleRowCount( VISIBLE_LIST_ROWS );
  }
  
  
  public void display()
  {
    JDialog dlg = new JDialog( 
      m_client.getMainWindow(), _( "Find City" ), true 
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplFindCity.this, BorderLayout.CENTER );
    m_dialog = dlg;
    resetCityListFromClient();
    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    // dispose of the city list to save memory.
    cityList = null;
    m_dlgManager.hideDialog( m_dialog );
  }
 
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
