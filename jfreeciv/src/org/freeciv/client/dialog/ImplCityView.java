package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.Collator;
import org.freeciv.common.Building;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;

/**
 * Implementation of the main city view.
 * 
 * @author Ben Mazur
 */
class ImplCityView extends VerticalFlowPanel implements DlgCityView, CommonConstants
{
  public static final int     VISIBLE_LIST_ROWS = 12;
  
  private VerticalFlowPanel m_panMain = new VerticalFlowPanel();
  // main panel stuff
  private JPanel m_panPeople = new JPanel();
  private JPanel m_panMiddle = new JPanel();
  private JPanel m_panUnits = new JPanel();
  private JPanel m_panButtons = new JPanel();
  // middle panel stuff
  private VerticalFlowPanel m_panCityStats = new VerticalFlowPanel();
  private VerticalFlowPanel m_panCityMap = new VerticalFlowPanel();
  private JPanel m_panMap = new JPanel();
  private VerticalFlowPanel m_panBuildings = new VerticalFlowPanel();
  // city stats panels
  private VerticalFlowPanel m_panProduction = new VerticalFlowPanel();
  private VerticalFlowPanel m_panTrade = new VerticalFlowPanel();
  private VerticalFlowPanel m_panMisc = new VerticalFlowPanel();
  // city stats labels
  private JLabel m_labFood = new JLabel( _( "Food : x (+-x)" ) );
  private JLabel m_labShields = new JLabel( _( "Shields : x (+-x)" ) );
  private JLabel m_labTrade = new JLabel( _( "Trade : x (+-x)" ) );
  private JLabel m_labGold = new JLabel( _( "Gold (xx%) : x (+-x)" ) );
  private JLabel m_labScience = new JLabel( _( "Science (xx%) : x" ) );
  private JLabel m_labLuxury = new JLabel( _( "Luxury (xx%) : x" ) );
  private JLabel m_labGranary = new JLabel( _( "Granary : x / y" ) );
  private JLabel m_labPollution = new JLabel( _( "Pollution : x" ) );
  // buildings (and constructing) panel stuff
  private VerticalFlowPanel m_panConstructing = new VerticalFlowPanel();
  private JList m_lisBuildings = new JList();
  private JButton m_butSellBuilding = new JButton( _( "Sell" ) );
  // producing panel stuff
  private JProgressBar m_pbarConstructing = new JProgressBar();
  private JPanel m_panConstructingButtons = new JPanel();
  private JButton m_butBuyConstructing = new JButton( _( "Buy" ) );
  private JButton m_butChangeConstructing = new JButton( _( "Change" ) );
  // units panel
  private JPanel m_panUnitsPresent = new JPanel();
  private JPanel m_panUnitsSupported = new JPanel();
  private VerticalFlowPanel m_panUnitButtons = new VerticalFlowPanel();
  private JButton m_butActivateUnits = new JButton( _( "Activate All" ) );
  private JButton m_butUnitList = new JButton( _( "Unit List" ) );
  // button panel buttons
  private JButton m_butClose = new JButton( _( "Close" ) );
  private JButton m_butRename = new JButton( _( "Rename" ) );
  private JButton m_butTrade = new JButton( _( "Trade" ) );
  private JButton m_butConfigure = new JButton( _( "Configure" ) );
  
  private ArrayList cityList;
  
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
  private City m_city;
    
  public ImplCityView( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;

    setupMainPanel();
    setupButtonPanel();
  }

  private Client getClient()
  {
    return m_client;
  }
  
  public City getCity()
  {
    return m_city;
  }
  
  /**
   * Sets up the main panel that contains all the other panels except 
   * the buttons
   */
  private void setupMainPanel()
  {
    m_panMain.setBorder( BorderFactory.createTitledBorder( _( "name - pop" ) ) );
    m_panPeople.add( new JLabel( "people people people" ) );  // just a dummy
    
    m_panMain.addRow( m_panPeople );
    setupMiddlePanel();
    setupUnitsPanel();
    
    
    this.addSpacerRow( m_panMain );
  }
  
  /**
   * Sets up the middle panel that displays city stats, map and buildings
   */
  private void setupMiddlePanel()
  {
    // fake map
    JComponent m_comMap = new JPanel();
    m_comMap.setPreferredSize( new Dimension( 160, 160 ) );
    m_comMap.setBackground( Color.black );
    
    m_panMap.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
    m_panMap.add( m_comMap );

    m_panCityMap.addRow( m_panMap );
    
    m_panMiddle.setLayout( new BorderLayout() );
    setupCityStatsPanel();
    m_panMiddle.add( m_panCityMap, BorderLayout.CENTER );
    setupBuildingsPanel();
    
    m_panMain.addSpacerRow( m_panMiddle );
  }
  
  /**
   * Sets up the city stats panel that displays the city's tile production,
   * trade, granary and pollution.
   */
  private void setupCityStatsPanel()
  {
    m_panProduction.setBorder( BorderFactory.createTitledBorder( _( "Production" ) ) );
    m_panProduction.addRow( m_labFood );
    m_panProduction.addRow( m_labShields );
    m_panProduction.addRow( m_labTrade );
    
    m_panTrade.setBorder( BorderFactory.createTitledBorder( _( "Trade Output" ) ) );
    m_panTrade.addRow( m_labGold );
    m_panTrade.addRow( m_labScience );
    m_panTrade.addRow( m_labLuxury );

    m_panMisc.setBorder( BorderFactory.createTitledBorder( _( "Misc" ) ) );
    m_panMisc.addRow( m_labGranary );
    m_panMisc.addRow( m_labPollution );
    
    m_panCityStats.addSpacerRow( m_panProduction );
    m_panCityStats.addSpacerRow( m_panTrade );
    m_panCityStats.addSpacerRow( m_panMisc );
    
    m_panMiddle.add( m_panCityStats, BorderLayout.WEST );
  }
  
  /**
   * sets up the buildings and producing panel
   */
  private void setupBuildingsPanel()
  {
    // progress bar
    m_pbarConstructing.setString( _( "xx / xx X Turns" ) );
    m_pbarConstructing.setStringPainted( true );
    // button panel
    m_butBuyConstructing.setEnabled( false );
    m_butChangeConstructing.setEnabled( false );
    m_panConstructingButtons.add( m_butBuyConstructing );
    m_panConstructingButtons.add( m_butChangeConstructing );
    // construction panel
    m_panConstructing.setBorder( BorderFactory.createTitledBorder( _( "Constructing : ?" ) ) );
    m_panConstructing.addSpacerRow( m_pbarConstructing );
    m_panConstructing.addSpacerRow( m_panConstructingButtons );
    // building list
    m_lisBuildings.setListData( new String[] { "building", "building", "etc." } );
    m_lisBuildings.setVisibleRowCount( 6 );
    m_butSellBuilding.setEnabled( false );
    
    m_panBuildings.addRow( m_panConstructing );
    m_panBuildings.addSpacerRow( new JScrollPane( m_lisBuildings ) );
    m_panBuildings.addRow( m_butSellBuilding );
  
    m_panMiddle.add( m_panBuildings, BorderLayout.EAST );
  }
  
  /**
   * sets up the units panel
   */
  private void setupUnitsPanel()
  {
    m_panUnitsPresent.setBorder( BorderFactory.createTitledBorder( _( "Units Present" ) ) );
    m_panUnitsPresent.add( new JLabel( "units go here" ) ); // temp
    m_panUnitsSupported.setBorder( BorderFactory.createTitledBorder( _( "Units Supported" ) ) );
    m_panUnitsSupported.add( new JLabel( "units go here" ) ); // temp
    
    m_butActivateUnits.setEnabled( false );
    m_butUnitList.setEnabled( false );
    m_panUnitButtons.addRow( m_butActivateUnits );
    m_panUnitButtons.addRow( m_butUnitList );
      
    m_panUnits.setLayout( new BorderLayout() );
    m_panUnits.add( m_panUnitsPresent, BorderLayout.WEST );
    m_panUnits.add( m_panUnitsSupported, BorderLayout.CENTER );
    m_panUnits.add( m_panUnitButtons, BorderLayout.EAST );
    
    m_panMain.addRow( m_panUnits );
  }
  
  /**
   * Initialization function; sets up the button panel
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
    
    //TODO: other buttons
    m_butRename.setEnabled( false );
    m_butTrade.setEnabled( false );
    m_butConfigure.setEnabled( false );

    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butClose );
    m_panButtons.add( m_butRename );
    m_panButtons.add( m_butTrade );
    m_panButtons.add( m_butConfigure );
    this.addRow( m_panButtons );
  }
  
  /**
   * Updates the city stats panel
   */
  public void updateCityStats()
  {
    // production
    m_labFood.setText( _( 
      "Food : " + m_city.getFoodProduction() 
      + " (" + ( m_city.getFoodSurplus() > 0 ? "+" : "" )
      + m_city.getFoodSurplus() + ")" ) );
    m_labShields.setText( _( 
      "Shields : " + m_city.getShieldProduction() 
      + " (" + ( m_city.getShieldSurplus() > 0 ? "+" : "" )
      + m_city.getShieldSurplus() + ")" ) );
    m_labTrade.setText( _( 
      "Trade : " + ( m_city.getTradeProduction() + m_city.getCorruption() )
      + " (" + ( m_city.getTradeProduction() > 0 ? "+" : "" ) 
      + m_city.getTradeProduction() + ")" ) );
    // trade
    m_labGold.setText( _( "Gold ("
            + getClient().getGame().getCurrentPlayer().getEconomy().getTax()
            + "%) : " + m_city.getTaxTotal() 
            + " (" + ( m_city.getGoldSurplus() > 0 ? "+" : "" ) 
            + m_city.getGoldSurplus() + ")" ) );
    m_labScience.setText( _( "Science ("
            + getClient().getGame().getCurrentPlayer().getEconomy().getScience()
            + "%) : " + m_city.getScienceTotal() ) );
    m_labLuxury.setText( _( "Luxury ("
            + getClient().getGame().getCurrentPlayer().getEconomy().getLuxury()
            + "%) : " + m_city.getLuxuryTotal() ) );
    // misc
    m_labGranary.setText( _( "Granary : " 
                             + ( m_city.hasEffect( B_GRANARY) ? "*" : "" )
                             + m_city.getFoodStock() + " / "
                             + m_city.getGranarySize() ) );
    m_labPollution.setText( _( "Pollution : " + m_city.getPollution() ) );

  }
  
  /**
   * Updates the constructing panel
   */
  public void updateConstructing()
  {
    m_panConstructing.setBorder( BorderFactory.createTitledBorder( 
                                _( "Constructing : " 
                                   + m_city.getCurrentlyBuildingDescription() ) ) );
    final int stock = m_city.getShieldStock();
    final int cost = m_city.getCurrentProductionCost();
    final int turns = m_city.getTurnsToBuild();
    m_pbarConstructing.setMinimum( 0 );
    m_pbarConstructing.setValue( stock );
    m_pbarConstructing.setMaximum( cost );
    m_pbarConstructing.setString( stock + " / " + cost
                                  + "    " + turns + _( " turns" ) );
  }
  
  /**
   * Updates the buildings list
   * 
   * TODO: keep track of these somehow so when they click "sell" we know what to do.
   */
  public void updateBuildingsList()
  {
    ArrayList names = new ArrayList();
    for( int i = 0; i < getClient().getGame().getNumberOfImprovementTypes(); i++ ) {
      if( m_city.hasBuilding( i ) ) {
        final Building bld = (Building)getClient().getGame().getFactories().getBuildingFactory().findById( i );
        names.add( _( bld.getName() ) );
      }
    }
    m_lisBuildings.setListData( names.toArray() );
  }
  
  
  public void display( City city )
  {
    m_city = city;
    JDialog dlg = new JDialog( 
      m_client.getMainWindow(), 
      _( m_city.getName() + " - " + m_city.getPopulation() ), 
      true 
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplCityView.this, BorderLayout.CENTER );
    m_dialog = dlg;
    
    m_panMain.setBorder( BorderFactory.createTitledBorder( 
                        _( m_city.getName() + " - " + m_city.getPopulation() ) ) );
    updateCityStats();
    updateConstructing();
    updateBuildingsList();
    
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
