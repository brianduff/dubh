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
        actionCenter();
      }
    } );
    m_butZoomTo.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionZoom();
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
    ArrayList cList = new ArrayList();
    for(int i = 0; i < getClient().getGame().getNumberOfPlayers(); i++)
    {
      Iterator ci = getClient().getGame().getPlayer( i ).getCities();
      while( ci.hasNext() ) 
      {
        final City city = (City)ci.next();
        final boolean showNation =
                getClient().getGame().getCurrentPlayer().getNation().getId()
                != getClient().getGame().getPlayer( i ).getNation().getId();
        cList.add( new CityListItem( city, showNation ) );
      }
    }
    m_lisCityList.setListData( cList.toArray() );
    m_lisCityList.setVisibleRowCount( VISIBLE_LIST_ROWS );
  }
  
  /**
   * Called when "Center" is pressed
   */
  private void actionCenter()
  {
      if( !m_lisCityList.isSelectionEmpty() )
      {
        final City city = ( (CityListItem)m_lisCityList.getSelectedValue() ).m_city;
        getClient().getMainWindow().getMapViewManager().centerOnTile( city.getX(), city.getY() );
        undisplay();
      }
  }
  
  /**
   * Called when "Zoom to City" is pressed
   */
  private void actionZoom()
  {
      if( !m_lisCityList.isSelectionEmpty() )
      {
        final City city = ( (CityListItem)m_lisCityList.getSelectedValue() ).m_city;
        getClient().getMainWindow().getMapViewManager().centerOnTile( city.getX(), city.getY() );
        undisplay();
        if( city.getOwner().getNation().getId()
            == getClient().getGame().getCurrentPlayer().getNation().getId() )
        {
          getClient().getDialogManager().getCityViewDialog().display( city );
        }

      }
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
    m_dlgManager.hideDialog( m_dialog );
  }
  
  /**
   * A small class to store the city in the city list
   */
  private class CityListItem
  {
    public City m_city;
    public boolean m_showNation;
    
    public CityListItem( City city, boolean showNation )
    {
      m_city = city;
      m_showNation = showNation;
    }
    
    public String toString()
    {
      return m_city.getName() + 
      ( m_showNation 
        ? " (" + m_city.getOwner().getNation().getName() + ")" 
          : "" );
    }
  }
 
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
