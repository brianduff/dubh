package org.freeciv.client.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.common.Building;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;
import org.freeciv.common.UnitType;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktCityRequest;
import org.freeciv.net.WorkList;

/**
 * Implementation of the tax rate panel.  This updates the economy values
 * itself when "Okay" is pressed, or simply hides when "Cancel" is pressed.
 * 
 * Maybe the economy values should be updated by another class, but where?
 * 
 * @author Ben Mazur
 */
class ImplCityChangeConstruction extends VerticalFlowPanel 
  implements DlgCityChangeConstruction, CommonConstants
{
  private ConstructionTableModel m_targetModel;
  private JTable m_tabTargets;
  private JPanel m_panButtons = new JPanel();
  private JButton m_butChange = new JButton( _( "Change" ) );
  private JButton m_butCancel = new JButton( _( "Cancel" ) );
  
  private City m_city;
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
    
  public ImplCityChangeConstruction( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;

    setupTargetsTable();
    setupButtonPanel();
  }

  private Client getClient()
  {
    return m_client;
  }
  
  private City getCity()
  {
    return m_city;
  }
  
  /**
   * Initialization function; sets up the city list panel and adds it to the
   * main dialog panel.
   */
  private void setupTargetsTable()
  {
    m_targetModel = new ConstructionTableModel();
    m_tabTargets = new JTable( m_targetModel );
    
    m_tabTargets.setRowSelectionAllowed( true );
    m_tabTargets.setColumnSelectionAllowed( false );
    m_tabTargets.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_tabTargets.setShowHorizontalLines( false );
    m_tabTargets.setShowVerticalLines( false );
    
    this.addSpacerRow( new JScrollPane( m_tabTargets ) );
  }
  
  /**
   * Initialization function; sets up the button panel and adds it to the
   * main dialog panel.
   */
  private void setupButtonPanel()
  {
    m_butChange.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionChange();
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
    m_panButtons.add( m_butChange );
    m_panButtons.add( m_butCancel );
    this.addRow( m_panButtons );
  }
  
  /**
   * Resets the city list from the client in case it's out of date.
   */
  private void resetTargetList()
  {
    ArrayList tList = new ArrayList();
    for( int i = 0; i < getClient().getGame().getNumberOfImprovementTypes(); i++ )
    {
      if( getCity().canBuildImprovement( i ) )
      {
        final Building bld = getClient().getGame().getBuilding( i );
        Object[] datum = new Object[6];
        
        datum[0] = bld.getName();
        if( bld.isWonder() ) 
        {
          datum[1] = _( "Wonder" );
          if( bld.isWonderObsolete() ) 
          {
            // um but getCity().canBuildImprovement( i ) shouldn't allow this??
            datum[1] = _( "Obsolete" );
          }
          if( getClient().getGame().getGlobalWonder( i ) != 0 ) 
          {
            // um but getCity().canBuildImprovement( i ) shouldn't allow this??
            datum[1] = _( "Built" );
          }
        }
        else
        {
          datum[1] = "";
        }
        if( i != B_CAPITAL )
        {
          datum[2] = new Integer( bld.getBuildCost() );
          datum[3] = new Integer( 
                  getCity().getTurnsToBuild( i, false ) );
        }
        else 
        {
          datum[2] = "--";
          datum[3] = "--";
        }
        datum[4] = new Integer( i );
        datum[5] = new Boolean( false );
        
        tList.add( datum );
      }
    }
    for( int i = 0; i < getClient().getGame().getNumberOfUnitTypes(); i++ )
    {
      if( getCity().canBuildUnitType( i ) )
      {
        final UnitType unt = getClient().getGame().getUnitType( i );
        Object[] datum = new Object[6];
        
        datum[0] = unt.getName();
        if( unt.getFuel() > 0 ) 
        {
          datum[1] = unt.getAttackStrength() + "/" + unt.getDefenseStrength()
            + "/" + ( unt.getMoveRate() / 3 ) + "/"
            + ( ( unt.getMoveRate() / 3 ) * unt.getFuel() );
        }
        else
        {
          datum[1] = unt.getAttackStrength() + "/" + unt.getDefenseStrength()
            + "/" + ( unt.getMoveRate() / 3 );
        }
        datum[2] = new Integer( unt.getBuildCost() );
        datum[3] = new Integer( getCity().getTurnsToBuild( i, true ) );
        datum[4] = new Integer( i );
        datum[5] = new Boolean( true );
        
        tList.add( datum );
      }
    }
    
    Object[][] newData = new Object[tList.size()][6];
    tList.toArray( newData );
    m_targetModel.data = newData;
    
    resizeTable( m_tabTargets );
  }
  
  /**
   * Called when "Change" is pressed
   */
  private void actionChange()
  {
    if( m_tabTargets.getSelectedRow() != -1 )
    {
      requestChange( 
              ( (Integer)m_targetModel.data[m_tabTargets.getSelectedRow()][4]
              ).intValue(),
              ( (Boolean)m_targetModel.data[m_tabTargets.getSelectedRow()][5]
              ).booleanValue() );
      undisplay();
    }
  }
  
  /**
   * Sends a packet indicating that the city should change contruction
   * 
   * @param id the id to change to
   * @param isUnit is the new target a unit?
   */
  private void requestChange( int id, boolean isUnit ) 
  {
    PktCityRequest packet = new PktCityRequest();
    packet.setType( PacketConstants.PACKET_CITY_CHANGE );
    
    packet.city_id = getCity().getId();
    packet.name = "";
    packet.build_id = id;
    packet.is_build_id_unit_id = isUnit;
    packet.worklist = new WorkList();
    packet.worklist.setName( "" );
    
    getClient().sendToServer( packet );
  }
  
  /**
   * Attempt to resize a table's PreferredScrollableViewportSize based on the
   * size of the headers and data within it.  Isn't there some way to do this
   * automatically?  There should be.
   * 
   * TableColumn.sizeWidthToFit() seems inadequate.
   * 
   * This method might be better off in some utility class.
   */
  private static void resizeTable( JTable table )
  {
    int totalWidth = 0;
    for( int i = 0; i < table.getColumnCount(); i++ )
    {
      final TableColumn column = table.getColumnModel().getColumn( i );
      int width = 0;
      width = Math.max( width, table.getFontMetrics( table.getFont() ).stringWidth( table.getColumnName( i ) ) );
      for( int j = 0; j < table.getRowCount(); j++ )
      {
        width = Math.max( width, table.getFontMetrics( table.getFont() ).stringWidth( table.getValueAt( j, i ).toString() ) );
      }
      // and it seems like we need some padding.
      width += 10;
      column.setPreferredWidth( width );
      totalWidth += width;
    }

    int height = Math.min( 20, table.getRowCount() ) * table.getRowHeight();
    table.setPreferredScrollableViewportSize( 
      new Dimension( totalWidth, height )
    );
  }

  public void display( City city )
  {
    JDialog dlg = new JDialog( 
      m_client.getMainWindow(), _( "Change Construction" ), true 
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplCityChangeConstruction.this, BorderLayout.CENTER );
    m_dialog = dlg;
    m_city = city;
    
    resetTargetList();

    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }
  
  /**
   * Stores all the things you can make.
   * 
   * Note the super-secret index and isUnit "columns".
   */
  private class ConstructionTableModel extends AbstractTableModel
  {
    String[] columnNames = { _( "Type" ), _( "Info" ), 
                             _( "Cost" ), _( "Turns" ) };
    Object[][] data = { { _( "Barracks" ), _( "" ), 
                          new Integer( 40 ), new Integer( 14 ), 
                          new Integer( 2 ), new Boolean( false ) },
                        { _( "Super-warriors" ), _( "50/1/1" ), 
                          new Integer( 10 ), new Integer( 4 ), 
                          new Integer( 2 ), new Boolean( true ) } };
    
    public int getColumnCount()
    {
      return columnNames.length;
    }
    public int getRowCount()
    {
      return data.length;
    }
    public String getColumnName( int col )
    {
      return columnNames[col];
    }
    public Object getValueAt( int row, int col )
    {
      return data[row][col];
    }
    public Class getColumnClass( int col )
    {
      return getValueAt( 0, col ).getClass();
    }
  }
 
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
