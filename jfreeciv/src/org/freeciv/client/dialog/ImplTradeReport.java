package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.freeciv.common.City;
import org.freeciv.common.Building;
import org.freeciv.common.Player;
import org.freeciv.common.CommonConstants;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;
import org.freeciv.net.PktPlayerRequest;
import org.freeciv.net.PacketConstants;

/**
 * Implementation of the Trade dialog.  Shows owned buildings and their
 * upkeep costs.  "Trade" is a strange name for this dialog.
 * 
 * @author Ben Mazur
 */
class ImplTradeReport extends VerticalFlowPanel 
  implements DlgTradeReport, CommonConstants
{
  private JLabel m_labTrade = new JLabel( _( "Trade" ), JLabel.CENTER );
  private JLabel m_labGovt = new JLabel( _( "<Gov't> of the <Nation>" ), JLabel.CENTER );
  private JLabel m_labYear = new JLabel( _( "<Title> <Name> : XXXX BC/AD" ), JLabel.CENTER );
  private BuildingsTableModel m_buildingsModel;
  private JTable m_tabBuildings;
  private JPanel m_panEcon = new JPanel();
  private JPanel m_panButtons = new JPanel();
  // econ panel
  private JLabel m_labIncome = new JLabel( _( "Income:" ), JLabel.RIGHT );
  private JLabel m_labIncomeValue = new JLabel( "X", JLabel.LEFT );
  private JLabel m_labCosts = new JLabel( _( "Total Costs:" ), JLabel.RIGHT );
  private JLabel m_labCostsValue = new JLabel( "X", JLabel.LEFT );
  // buttons panel
  private JButton m_butClose = new JButton( _( "Close" ) );
  private JButton m_butSellObsolete = new JButton( _( "Sell Obsolete" ) );
  private JButton m_butSellAll = new JButton( _( "Sell All" ) );
  
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
    
  public ImplTradeReport( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;
    
    addRow( m_labTrade );
    addRow( m_labGovt );
    addRow( m_labYear );
    
    setupBuildingsTable();
    setupEconPanel();
    setupButtonPanel();
  }
  
  /**
   * for consistancy
   */
  private Client getClient()
  {
    return m_client;
  }
  
  /**
   * Shortcut to getClient().getGame().getCurrentPlayer()
   */
  private Player getPlayer()
  {
    return getClient().getGame().getCurrentPlayer();
  }

  /**
   * Sets up the units table
   */
  private void setupBuildingsTable()
  {
    m_buildingsModel = new BuildingsTableModel();
    m_tabBuildings = new JTable( m_buildingsModel );
    
    m_tabBuildings.setRowSelectionAllowed( true );
    m_tabBuildings.setColumnSelectionAllowed( false );
    m_tabBuildings.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
    m_tabBuildings.setShowHorizontalLines( false );
    m_tabBuildings.setShowVerticalLines( false );
    
    this.addSpacerRow( new JScrollPane( m_tabBuildings ) );
  }
  
  /**
   * Sets up the economics panel
   */
  private void setupEconPanel()
  {
    m_panEcon.setLayout( new FlowLayout() );
    
    m_panEcon.add( m_labIncome );
    m_panEcon.add( m_labIncomeValue );
    m_panEcon.add( m_labCosts );
    m_panEcon.add( m_labCostsValue );
    
    this.addRow( m_panEcon );
  }

  /**
   * Initialization function; sets up the button panel and adds it to the
   * main dialog panel.
   */
  private void setupButtonPanel()
  {
    m_butClose.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );
    m_butSellObsolete.setEnabled( false ); //TODO
    m_butSellAll.setEnabled( false ); //TODO

    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butClose );
    m_panButtons.add( m_butSellObsolete );
    m_panButtons.add( m_butSellAll );
    this.addRow( m_panButtons );
  }
  
  
  /**
   * Re-populates the combo box and lists, as well as updates the labels
   * with new info from the client
   */
  public void refresh()
  {
    m_labGovt.setText( _( 
            getPlayer().getGovernment().getName()
            + " of the "
            + getPlayer().getNation().getPluralName()
            ) );
    m_labYear.setText( _( 
            getPlayer().getRulerTitle() + " "
            + getPlayer().getName()
            + " : " + getClient().getGame().getYearString()
            ) );
    
    updateBuildingsAndEcon();
    resizeTable( m_tabBuildings );
  }
  
  /**
   * Repopulate the buildings table with data from the client.  As a bonus,
   * updates the cost and income data too.
   */
  public void updateBuildingsAndEcon()
  {
    ArrayList bList = new ArrayList();
    int totalCost = 0;
    for( int i = 0; i < getClient().getGame().getNumberOfImprovementTypes(); i++ )
    {
      final Building building = (Building)getClient().getFactories().getBuildingFactory().findById( i );
      
      if( building.isWonder() )
      {
        continue;
      }
      
      int count = 0, cost = 0, uTotal = 0;
      for( Iterator j = getPlayer().getCities(); j.hasNext(); )
      {
        City city = (City)j.next();
        if( city.hasBuilding( i ) )
        {
           count++;
           cost = Math.max( cost, city.getBuildingUpkeep( i ) );
           uTotal += city.getBuildingUpkeep( i );
           totalCost += uTotal;
        }
      }
      
      if( count == 0 )
      {
        continue;
      }

      Object[] datum = new Object[5];
      datum[0] = building.getName();
      datum[1] = new Integer( count );
      datum[2] = new Integer( cost );
      datum[3] = new Integer( uTotal );
      datum[4] = new Integer( i );
      bList.add( datum );
    }
    
    Object[][] newData = new Object[bList.size()][5];
    bList.toArray( newData );
    m_buildingsModel.data = newData;
    
    int totalTax = 0;
    for( Iterator i = getPlayer().getCities(); i.hasNext(); )
    {
      City city = (City)i.next();
      totalTax += city.getTaxTotal();
      if( !city.isBuildingUnit() 
          && city.getCurrentlyBuildingId() == CommonConstants.B_CAPITAL )
      {
         totalTax += city.getShieldSurplus();
      }
    }
    
    m_labIncomeValue.setText( new Integer( totalTax ).toString() );
    m_labCostsValue.setText( new Integer( totalCost ).toString() );
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

    int height = Math.min( 12, table.getRowCount() ) * table.getRowHeight();
    table.setPreferredScrollableViewportSize( 
      new Dimension( totalWidth, height )
    );
  }

  public void display()
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), _( "Trade" ), true 
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplTradeReport.this, BorderLayout.CENTER );
    m_dialog = dlg;
    
    refresh();
    
    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }
  
  /**
   * A nice, if unoriginal, class representing the columns and data in the 
   * units table
   */
  class BuildingsTableModel extends AbstractTableModel
  {
    String[] columnNames = { _( "Building Name" ), _( "Count" ), 
                             _( "Cost" ), _( "U Total" ) };
    Object[][] data = { { _( "Barracks" ), new Integer( 2 ), 
                          new Integer( 1 ), new Integer( 2 ) }, 
                        { _( "Palace" ), new Integer( 30 ), 
                          new Integer( 0 ), new Integer( 0 ) } };
    
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
      return columnNames[ col ];
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
