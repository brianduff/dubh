package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.Collator;
import org.freeciv.common.Building;
import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.common.Player;
import org.freeciv.common.CommonConstants;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;
import org.freeciv.client.action.*;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktCityRequest;

/**
 * Implementation of the main city view.
 * 
 * @author Ben Mazur
 */
public class ImplCityView extends VerticalFlowPanel 
  implements DlgCityView, CommonConstants, Constants, PacketConstants
{
  private static final int VISIBLE_LIST_ROWS = 8;
  private static final int NUM_TILES_HP_BAR = 11;
  
  private VerticalFlowPanel m_panMain = new VerticalFlowPanel();
  // main panel stuff
  private CityCitizensDisplay m_citizensDisplay = new CityCitizensDisplay();
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
  private JButton m_butBuyConstructing = new JButton( _( "Buy (X gold)" ) );
  private JButton m_butChangeConstructing = new JButton( _( "Change" ) );
  // units panel
  private JScrollPane m_panUnitsPresent;
  private JScrollPane m_panUnitsSupported;
  private CityUnitsDisplay m_cudPresent = new CityUnitsDisplay();
  private CityUnitsDisplay m_cudSupported = new CityUnitsDisplay();
  private VerticalFlowPanel m_panUnitButtons = new VerticalFlowPanel();
  private JButton m_butActivateUnits = new JButton( _( "Activate All" ) );
  private JButton m_butUnitList = new JButton( _( "Unit List" ) );
  // button panel buttons
  private JButton m_butClose = new JButton( _( "Close" ) );
  private JButton m_butRename = new JButton( _( "Rename" ) );
  private JButton m_butTrade = new JButton( _( "Trade" ) );
  private JButton m_butConfigure = new JButton( _( "Configure" ) );
  
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

  /**
   * Shortcut (sort of) to m_client
   */
  private Client getClient()
  {
    return m_client;
  }
  
  /**
   * Shortcut (sort of) to m_city
   */
  public City getCity()
  {
    return m_city;
  }
  
  /**
   * Shortcut to getClient().getGame().getCurrentPlayer()
   */
  public Player getPlayer()
  {
    return getClient().getGame().getCurrentPlayer();
  }
  
  /**
   * Sets up the main panel that contains all the other panels except 
   * the buttons
   */
  private void setupMainPanel()
  {
    m_panMain.setBorder( BorderFactory.createTitledBorder( _( "name - pop" ) ) );
    
    m_panMain.addRow( m_citizensDisplay );
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
    m_comMap.setPreferredSize( new Dimension( 
            m_client.getTileSpec().getNormalTileWidth() * 5,
            m_client.getTileSpec().getNormalTileHeight() * 5 ) );
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
    m_butBuyConstructing.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionBuy();
      }
    } );
    m_butChangeConstructing.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionChange();
      }
    } );
    m_panConstructingButtons.add( m_butBuyConstructing );
    m_panConstructingButtons.add( m_butChangeConstructing );
    // construction panel
    m_panConstructing.setBorder( BorderFactory.createTitledBorder( _( "Constructing : ?" ) ) );
    m_panConstructing.addSpacerRow( m_pbarConstructing );
    m_panConstructing.addSpacerRow( m_panConstructingButtons );
    // building list
    m_lisBuildings.setListData( new String[] { "building", "building", "etc." } );
    m_lisBuildings.setVisibleRowCount( 6 );
    m_lisBuildings.setPrototypeCellValue( "Really Long Building Name" );
    m_butSellBuilding.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionSell();
      }
    } );

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
    m_panUnitsPresent = new JScrollPane( m_cudPresent, 
            JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
    m_panUnitsPresent.setBorder( BorderFactory.createTitledBorder( _( "Units Present" ) ) );
    m_panUnitsSupported = new JScrollPane( m_cudSupported, 
            JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
    m_panUnitsSupported.setBorder( BorderFactory.createTitledBorder( _( "Units Supported" ) ) );
    
    m_butActivateUnits.setEnabled( false );
    m_butUnitList.setEnabled( false );
    m_panUnitButtons.addRow( m_butActivateUnits );
    m_panUnitButtons.addRow( m_butUnitList );
      
    m_panUnits.setLayout( new GridBagLayout() );
    m_panUnits.add( m_panUnitsPresent, new GridBagConstraints2( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets( 0, 1, 0, 1 ), 0, 0 ) );
    m_panUnits.add( m_panUnitsSupported, new GridBagConstraints2( 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets( 0, 1, 0, 1 ), 0, 0 ) );
    m_panUnits.add( m_panUnitButtons, new GridBagConstraints2( 2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets( 0, 1, 0, 1 ), 0, 0 ) );
    
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
    m_butRename.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        actionRename();
      }
    } );
    
    //TODO: other buttons
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
   * Updates the title (name and population) in various places
   */
  public void updateTitle()
  {
    m_dialog.setTitle( _( m_city.getName() + " - " + m_city.getPopulation() ) );
    m_panMain.setBorder( BorderFactory.createTitledBorder( 
            _( m_city.getName() + " - " + m_city.getPopulation() ) ) );
  }

  /**
   * Updates the display of citizen icons
   */
  public void updateCitizens()
  {
    m_citizensDisplay.updateFromCity();
    m_citizensDisplay.validate();
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
            + getPlayer().getEconomy().getTax()
            + "%) : " + m_city.getTaxTotal() 
            + " (" + ( m_city.getGoldSurplus() > 0 ? "+" : "" ) 
            + m_city.getGoldSurplus() + ")" ) );
    m_labScience.setText( _( "Science ("
            + getPlayer().getEconomy().getScience()
            + "%) : " + m_city.getScienceTotal() ) );
    m_labLuxury.setText( _( "Luxury ("
            + getPlayer().getEconomy().getLuxury()
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
    m_butBuyConstructing.setText( _( "Buy" ) );
  }
  
  /**
   * Updates the buildings list
   * 
   * TODO: keep track of these somehow so when they click "sell" we know what to do.
   */
  public void updateBuildingsList()
  {
    ArrayList bList = new ArrayList();
    for( int i = 0; i < getClient().getGame().getNumberOfImprovementTypes(); i++ ) {
      if( m_city.hasBuilding( i ) ) {
        final Building bld = getClient().getGame().getBuilding( i );
        bList.add( new BuildingListItem( bld.getId(), 
                                         bld.getBuildCost(), 
                                         bld.getName() ) );
      }
    }
    m_lisBuildings.setListData( bList.toArray() );
  }
  
  /**
   * Updates the unit displays
   */
  public void updateUnitDisplays()
  {
    m_cudPresent.updateFromIterator( 
      getClient().getGame().getMap().getTile( getCity().getX(), getCity().getY() ).getUnits(),
      true );  
    m_cudSupported.updateFromIterator( getCity().getSupportedUnits(),
                                       false );
    // do NOT exceed width of middle panel
    m_panUnits.setPreferredSize(new Dimension(
            m_panMiddle.getPreferredSize().width,
            m_panUnits.getPreferredSize().height ) );
    m_cudPresent.validate();
    m_cudSupported.validate();
  }
  
  /**
   * Updates the whole shebang
   */
  public void refresh()
  {
    updateTitle();
    updateCitizens();
    updateCityStats();
    updateConstructing();
    updateBuildingsList();
    updateUnitDisplays();
  }
  
  /**
   * The user has clicked the "Buy" button for Construction
   */
  private void actionBuy()
  {
    int cost = getCity().getBuyCost();
    int treasury = getPlayer().getEconomy().getGold();
    if( treasury >= cost )
    {
      int result = m_dlgManager.showConfirmationDialog( 
              _( "Buy " + getCity().getCurrentlyBuildingDescription() 
                 + " for " + cost + " gold?\n"
                 + "Treasury contains " + treasury + " gold." ) );
      if( result == JOptionPane.YES_OPTION )
      {
        requestBuy();
      }
    }
    else
    {
      m_dlgManager.showMessageDialog( 
              _( getCity().getCurrentlyBuildingDescription() 
                 + " costs " + cost + " gold.\n"
                 + "Treasury contains " + treasury + " gold." ) );
    }
  }
  
  /**
   * The user has clicked to change construction
   */
  private void actionChange()
  {
    m_dlgManager.getCityChangeConstructionDialog().display( getCity() );
  }
  
  /**
   * The user has clicked to "Sell" a building
   */
  private void actionSell()
  {
    if( m_lisBuildings.isSelectionEmpty() )
    {
      return;
    }
    
    BuildingListItem bli = (BuildingListItem)m_lisBuildings.getSelectedValue();
    int result = m_dlgManager.showConfirmationDialog( 
            _( "Sell " + bli.m_name + " for " + bli.m_cost
               + " gold?" ) );
    if( result == JOptionPane.YES_OPTION )
    {
      requestSell( bli.m_id );
    }
  }
  
  /**
   * The user has clicked to rename the city.
   */
  private void actionRename()
  {
    String newName = m_dlgManager.showInputDialog( 
            _( "What should we rename the city to?" ),
            getCity().getName() );
    
    if( newName != null && newName.length() > 0 )
    {
      requestRename( newName );
    }
    else 
    {
      System.err.println( "##### didn't get any name from rename city" );
    }
  }
  
  /**
   * Sends a packet indicating that the city should buy its current 
   * construction
   */
  private void requestBuy() 
  {
    PktCityRequest packet = new PktCityRequest();
    packet.setType( PACKET_CITY_BUY );
    packet.city_id = getCity().getId();
    packet.name = "";
    getClient().sendToServer( packet );
  }
  
  /**
   * Sends a packet indicating that the user wants to sell the 
   * specified building
   */
  private void requestSell( int buildingId )
  {
    PktCityRequest packet = new PktCityRequest();
    packet.setType( PACKET_CITY_SELL );
    packet.city_id = getCity().getId();
    packet.build_id = buildingId;
    packet.name = "";
    getClient().sendToServer( packet );
  }
  
  /**
   * Sends a packet indicating that the user wants to rename the
   * city to the specified new name
   */
  private void requestRename( String newName )
  {
    PktCityRequest packet = new PktCityRequest();
    packet.setType( PACKET_CITY_RENAME );
    packet.city_id = getCity().getId();
    packet.name = newName;
    getClient().sendToServer( packet );
  }
  
  /**
   * Sets the Dialog visible with teh specified city displayed in it.
   */
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
    
    refresh();
    
    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    m_city = null;
    m_dlgManager.hideDialog( m_dialog );
  }
  
  /**
   * A class to display icons of all the units, either present or supported.
   * 
   * I think it will be okay to let the units be just icons and to make
   * a right click menu to do all the actions on them.
   * 
   * TODO: show unit health, fortification & shield-use status.
   * TODO: pop-up menu
   */
  protected class CityUnitsDisplay extends JPanel
  {
    public CityUnitsDisplay()
    {
      super();
      this.setLayout( new FlowLayout() );
    }
    
    /**
     * Clears and adds unit icons 
     */
    public void updateFromIterator( Iterator iter, boolean isPresent )
    {
      this.removeAll();
      while( iter.hasNext() )
      {
        final Unit u = (Unit)iter.next();
        if( isPresent )
        {
          this.add( new PresentUnitComponent( u ) );
        }
        else
        {
          this.add( new SupportedUnitComponent( u ) );
        }
      }
    }
  }
  
  /**
   * Superclass of SupportedUnitComponent & PresentUnitComponent.
   */
  public abstract class UnitComponent
    extends JLabel
    implements MouseListener
  {
    protected Unit m_unit;
    protected IconStack m_icon;
    protected JPopupMenu m_popup;
    
    public UnitComponent( Unit unit )
    {
      super();
      m_unit = unit;
      initIcon();
      initToolTip();
      initPopup();
      
      this.addMouseListener( this );
    }
    
    public  Unit getUnit()
    {
      return m_unit;
    }

    protected void initIcon()
    {
      m_icon = new IconStack();
      
      m_icon.addIcon( m_unit.getSprite() );
      
      if ( m_unit.getActivity() != ACTIVITY_IDLE )
      {
        int a = m_unit.getActivity();
        if ( ACTIVITY_MINE == a )
          addIcon( "unit.mine" );
        else if ( ACTIVITY_POLLUTION == a )
          addIcon( "unit.pollution" );
        else if ( ACTIVITY_FALLOUT  ==  a )
          addIcon( "unit.fallout" );
        else if ( ACTIVITY_PILLAGE == a )
          addIcon( "unit.pillage" );
        else if ( ACTIVITY_ROAD == a || ACTIVITY_RAILROAD == a )
          addIcon( "unit.road" );
        else if ( ACTIVITY_IRRIGATE == a )
          addIcon( "unit.irrigate" );
        else if ( ACTIVITY_EXPLORE == a )
          addIcon( "unit.explore" );
        else if ( ACTIVITY_FORTIFIED == a )
          addIcon( "unit.fortified" );
        else if ( ACTIVITY_FORTRESS == a )
          addIcon( "unit.fortress" );
        else if ( ACTIVITY_FORTIFYING == a )
          addIcon( "unit.fortifying" );
        else if ( ACTIVITY_AIRBASE == a )
          addIcon( "unit.airbase" );
        else if ( ACTIVITY_SENTRY == a )
          addIcon( "unit.sentry" );
        else if ( ACTIVITY_GOTO == a )
          addIcon( "unit.goto" );
        else if ( ACTIVITY_TRANSFORM == a )
          addIcon( "unit.transform" );

      }

      if ( m_unit.getAI().isControlled() )
      {
        if ( m_unit.isMilitary() )
        {
          addIcon( "unit.auto_attack" );
        }
        else
        {
          addIcon( "unit.auto_settler" );
        }
      }

      if ( m_unit.isConnecting() )
      {
        addIcon( "unit.connect" );
      }

      if ( m_unit.getActivity() == ACTIVITY_PATROL )
      {
        addIcon( "unit.patrol" );
      }

      int ihp = ( ( NUM_TILES_HP_BAR - 1 ) * m_unit.getHitPoints() )  / m_unit.getUnitType().getHitPoints();
      ihp = clip( 0, ihp, NUM_TILES_HP_BAR - 1 );

      addIcon( "unit.hp_" + ( ihp * 10 ) ); 
      
      setIcon( m_icon );
    }
    
    protected int clip(int lower, int thisOne, int upper)
    {
      return ( thisOne < lower ? lower : ( thisOne > upper ? upper : thisOne ) );
    }
    
    /**
     * Adds another icon to the multiicon, specified by key
     */
    protected void addIcon( String key )
    {
      m_icon.addIcon( getClient().getTileSpec().getImage( key ) );
    }

    protected void initToolTip()
    {
      StringBuffer toolTipBuffer = new StringBuffer();
      String nationName = m_unit.getOwner().getNation().getName();
      toolTipBuffer.append( nationName );
      toolTipBuffer.append( " " );
      String unitName = m_unit.getUnitType().getName();
      toolTipBuffer.append( unitName );
      toolTipBuffer.append( " (" );
      toolTipBuffer.append( m_unit.getUnitType().getAttackStrength() );
      toolTipBuffer.append("/");
      toolTipBuffer.append( m_unit.getUnitType().getDefenseStrength() );
      toolTipBuffer.append("/");
      toolTipBuffer.append( m_unit.getUnitType().getMoveRate() );
      toolTipBuffer.append(")");

      setToolTipText( toolTipBuffer.toString() );
    }
    
    protected abstract void initPopup();
    
    protected void addToPopup( Class aClass )
    {
      Action a = getClient().getAction( aClass );
      ActionMenuItem ami = new ActionMenuItem( (AbstractClientAction)a );
      //m_popup.add( ami );
      m_popup.add( a );
    }
    
    //
    // MouseListener
    //
    public void mousePressed( MouseEvent e )
    {
      maybeShowPopup( e );
    }
    public void mouseReleased( MouseEvent e ) 
    {
      maybeShowPopup( e );
    }
    public void mouseClicked( MouseEvent e ) {}
    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e ) {}
    private void maybeShowPopup( MouseEvent e ) {
      if( e.isPopupTrigger() ) {
        m_popup.show( e.getComponent(), e.getX(), e.getY() );
      }
    }
  }
  
  private class PresentUnitComponent
    extends UnitComponent
  {
    public PresentUnitComponent( Unit unit )
    {
      super( unit );
    }
    
    protected void initPopup()
    {
      m_popup = new JPopupMenu();
      
      addToPopup( UACTActivateUnit.class );
      addToPopup( UACTActivateCloseDialog.class );
      addToPopup( UACTSentry.class );
      addToPopup( UACTFortify.class );
      addToPopup( UACTDisbandUnit.class );
      addToPopup( UACTMakeHomeCity.class );
      addToPopup( UACTUpgrade.class );
    }
  }
  
  /**
   * A component to display a supported unit.  Displays the unit icon, with 
   * the support cost (shield or food) superimposed.
   */
  private class SupportedUnitComponent
    extends UnitComponent
  {
    public SupportedUnitComponent( Unit unit )
    {
      super( unit );
      // add upkeep icon(s)      if( unit.getUpkeep() > 0 )      {
        addIcon( "upkeep.shield" );      }
      if( unit.getUpkeepFood() == 1 )      {
        addIcon( "upkeep.food" );      }
      if( unit.getUpkeepFood() > 1 )      {
        addIcon( "upkeep.food2" );      }
      if( unit.getUnhappiness() == 1 )      {
        addIcon( "upkeep.unhappy" );      }
      if( unit.getUnhappiness() > 1 )      {
        addIcon( "upkeep.unhappy2" );      }
    }
    
    protected void initPopup()
    {
      m_popup = new JPopupMenu();
      
      addToPopup( UACTActivateUnit.class );
      addToPopup( UACTActivateCloseDialog.class );
      addToPopup( UACTDisbandUnit.class );
    }
  }
  
  /**
   * Displays icons of all the citizens in the city
   * 
   * TODO: change citizen type w/ click
   * TODO: maybe even a pop-up menu
   */
  private class CityCitizensDisplay extends JPanel
  {
    public CityCitizensDisplay()
    {
      super();
      this.setLayout( new FlowLayout() );
    }
    
    /**
     * Clears & re-adds citizen icons from the info in City
     * 
     * Ugh, voodoo constants.  Watch out.
     */
    public void updateFromCity()
    {
      final TileSpec spec = getClient().getTileSpec();
      final String nation = getPlayer().getNation().getName();

      this.removeAll();
      // citizens
      for(int i = 0; i < getCity().getHappyCitizens( 4 ); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 5 + i % 2 ) );
        citizen.setToolTipText( _( "A Happy " + nation + " Citizen" ) );
        this.add( citizen );
      }
      for(int i = 0; i < getCity().getContentCitizens( 4 ); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 3 + i % 2 ) );
        citizen.setToolTipText( _( "A Content " + nation + " Citizen" ) );
        this.add( citizen );
      }
      for(int i = 0; i < getCity().getUnhappyCitizens( 4 ); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 7 + i % 2 ) );
        citizen.setToolTipText( _( "An Unhappy " + nation + " Citizen" ) );
        this.add( citizen );
      }
      // specialists
      for(int i = 0; i < getCity().getElvises(); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 0 ) );
        citizen.setToolTipText( _( nation + " Entertainer" ) );
        this.add( citizen );
      }
      for(int i = 0; i < getCity().getScientists(); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 1 ) );
        citizen.setToolTipText( _( nation + " Scientist" ) );
        this.add( citizen );
      }
      for(int i = 0; i < getCity().getTaxmen(); i++ )
      {
        JLabel citizen = new JLabel( spec.getCitizenSprite( 2 ) );
        citizen.setToolTipText( _( nation + " Taxman" ) );
        this.add( citizen );
      }
    }
  }
  
  /**
   * A quick class to hold buidings in the building list
   */
  protected class BuildingListItem
  {
    public int m_id;
    public int m_cost;
    public String m_name;
    
    public BuildingListItem( int id, int cost, String name )
    {
      m_id = id;
      m_cost = cost;
      m_name = name;
    }
    
    public String toString()
    {
      return m_name;
    }
  }
 
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
