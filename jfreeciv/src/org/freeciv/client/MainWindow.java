package org.freeciv.client;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.freeciv.client.action.AbstractClientAction;
import org.freeciv.client.action.AbstractToggleAction;
import org.freeciv.client.ui.CivInfoPanel;
import org.freeciv.client.ui.UnitInfoPanel;
import org.freeciv.client.ui.UnitStackDisplay;
import org.freeciv.client.ui.Console;
import org.freeciv.client.ui.QuickCommand;
import org.freeciv.client.ui.util.ActionMenuItem;
import org.freeciv.client.ui.util.DockPanel;
import org.freeciv.client.ui.util.ToggleActionMenuItem;
import org.freeciv.client.dialog.util.VerticalFlowPanel;


/**
 * The main Window for FreeCiv4J.
 * <p>
 * The main window consists of a number of "dockable" panels containing
 * information or controls, and a central area.
 * <p>
 * The contents of the central area depend on the setting of the
 * SingleMapViewMode property, and can be changed by calling the
 * setSingleMapViewMode() method.
 * <p>
 * The default mode is to display the main map view in the central area.
 * However, if single map view mode is switched off, the central area changes
 * to a desktop pane, which can contain one or more MDI child windows, each
 * with a separate view on the map.
 *
 * @author Brian.Duff@dubh.org
 */
public final class MainWindow extends JFrame
{
  private JDesktopPane m_desktop;

  private MapOverview m_mapOverview;
  private CivInfoPanel m_civInfo;
  private UnitInfoPanel m_unitInfo;
  private UnitStackDisplay m_unitStack;
  private Console m_console;
  private QuickCommand m_quickCommand;

  private Client m_client;

  private MapViewManager m_viewManager;
  private Map m_internalFrames;
  private JInternalFrame m_mainInternalFrame;

  private boolean m_singleMapViewMode = true;

  MainWindow( Client c )
  {
    m_desktop = new JDesktopPane();
    m_mapOverview = new MapOverview( c );
    m_civInfo = new CivInfoPanel( c );
    m_unitInfo = new UnitInfoPanel( c );
    m_console = new Console();
    m_quickCommand = new QuickCommand( c );

    m_client = c;

    m_viewManager = new MapViewManager( m_client );
    m_internalFrames = new HashMap();

    setTitle( m_client.APP_NAME );

    setupComponents();
    getContentPane().add( m_viewManager.getMainMapView().getComponent(),
      BorderLayout.CENTER );
  }

  /**
   * The main window can be configured to be fully MDI, and therefore support
   * multiple map view MDI windows, or SDI, and only have a single map view
   */
  void setSingleMapViewMode( boolean singleMapViewMode )
  {
    if ( singleMapViewMode != m_singleMapViewMode )
    {
      if ( singleMapViewMode )
      {
        getContentPane().remove( m_desktop );
        // Find the internal frame which is hosting the main view
        JInternalFrame mainIF = (JInternalFrame) m_internalFrames.get(
          getMapViewManager().getMainMapView()
        );

        if ( mainIF != null )
        {
          m_mainInternalFrame = mainIF;
          // And rip the main view component out of its content pane
          m_mainInternalFrame.getContentPane().remove(
            getMapViewManager().getMainMapView().getComponent()
          );
        }

        // .. add the main view to the centre of the main window
        getContentPane().add(
          getMapViewManager().getMainMapView().getComponent(),
          BorderLayout.CENTER
        );
      }
      else
      {
        getContentPane().remove(
          getMapViewManager().getMainMapView().getComponent()
        );
        getContentPane().add( m_desktop, BorderLayout.CENTER );

        // If there is a saved internal frame for the main view...
        if ( m_mainInternalFrame != null )
        {
          // Put the main view back in it..
          m_mainInternalFrame.getContentPane().add(
            getMapViewManager().getMainMapView().getComponent()
          );
          // If not, we should probably create one...
          m_mainInternalFrame = null;
        }
      }
    }
  }

  /**
   * Get the map view manager, this provides access to the map views available
   * in the main window
   */
  MapViewManager getMapViewManager()
  {
    return m_viewManager;
  }

  /**
   * Get the map overview component
   */
  public MapOverview getMapOverview()
  {
    return m_mapOverview;
  }

  /**
   * Get the civilization info panel
   */
  public CivInfoPanel getCivInfo()
  {
    return m_civInfo;
  }

  /**
   * Get the unit info panel
   */
  public UnitInfoPanel getUnitInfo()
  {
    return m_unitInfo;
  }

  /**
   * Get the unit stack panel
   */
  public UnitStackDisplay getUnitStack()
  {
    return m_unitStack;
  }

  public Console getConsole()
  {
    return m_console;
  }

  public QuickCommand getQuickCommand()
  {
    return m_quickCommand;
  }

   /**
    * The menus for the application. Each of these is the class name of
    * an action handler that deals with that menu item
    */
   private static final String[][] MENUS = new String[][] {
      { "Game",
          "ACTLocalOptions",
          "ACTMessageOptions",
          "ACTSaveSettings",
         /*---------------*/                 null,
          "ACTPlayers",
          "ACTMessages",
         /*---------------*/                 null,
          "ACTServerOptInitial",
          "ACTServerOptOngoing",
         /*---------------*/                 null,
          "ACTExportLog",
          "ACTClearLog",
         /*---------------*/                 null,
          "ACTDisconnect",
          "ACTQuit"
      },
      { "Kingdom",
          "ACTTaxRates",
         /*---------------*/                 null,
          "ACTFindCity",
          "ACTWorklists",
         /*---------------*/                 null,
          "ACTRevolution"
      },
      { "View",
          "ACTMapGrid",
          "ACTCenterView"
      },
      { "Orders",
          "UACTBuildCity",
          "UACTBuildRoad",
          "UACTBuildIrrigation",
          "UACTMine",
          "UACTTransformTerrain",
          "UACTBuildFortress",
          "UACTBuildAirbase",
          "UACTCleanPollution",
         /*---------------*/                 null,
          "UACTFortify",
          "UACTSentry",
          "UACTPillage",
         /*---------------*/                 null,
          "UACTMakeHomeCity",
          "UACTUnload",
          "UACTWakeUpOthers",
         /*---------------*/                 null,
          "UACTAutoSettler",
          "UACTAutoAttack",
          "UACTAutoExplore",
          "UACTConnect",
          "UACTGoTo",
          "UACTGoToCity",
         /*---------------*/                     // Remove me
                                            null,
          "UACTDisbandUnit",
          "UACTHelpBuildWonder",
          "UACTMakeTradeRoute",
          "UACTExplodeNuclear",
         /*---------------*/                 null,
          "UACTWait",
          "UACTDone"
      },
      { "Reports",
          "ACTCityReport",
          "ACTMilitaryReport",
          "ACTTradeReport",
          "ACTScienceReport",
         /*---------------*/                 null,
          "ACTWondersOfTheWorld",
          "ACTTopFiveCities",
          "ACTDemographics",
          "ACTSpaceship"
      }
   };

  /**
   * Sets up the main UI components of the main window. Only
   * does something the first time it is called.
   */
  public void setupComponents()
  {
    //mapFrame = new JInternalFrame( _( "Map" ), true, false, true, true );
    setupMenus();
    //desktop.add( mapFrame, MAP_PANEL_LAYER );

    getContentPane().setLayout( new BorderLayout() );
    VerticalFlowPanel pan = new VerticalFlowPanel();
    pan.setSpacing( 0 );

    DockPanel dp = new DockPanel( "Map Overview", getMapOverview() );
    pan.addRow( dp );

    dp = new DockPanel( "Civilization Info", getCivInfo() );
    pan.addRow( dp );

    dp = new DockPanel( "Unit Info", getUnitInfo() );
    pan.addRow( dp );

    dp = new DockPanel( "Unit Stack", getUnitStack() );
    pan.addRow( dp );

    pan.addSpacerRow( new JPanel() );

    getContentPane().add( pan, BorderLayout.WEST );

    dp = new DockPanel( "Console", getConsole() );
    getContentPane().add( dp, BorderLayout.SOUTH );

    pan = new VerticalFlowPanel();
    dp = new DockPanel( "Quick Commands", getQuickCommand() );
    pan.addRow( dp );

    pan.addSpacerRow( new JPanel() );

    getContentPane().add( pan, BorderLayout.EAST );

    //setupMessagesPanel();
    //setupMiniMapPanel();
    //setupStatusPanel();
    // OK, now add all the panels to the main window.
    //panWest = new JPanel();
    //panWest.setLayout( new BorderLayout() );
    //panWest.add( m_upMiniMap.getMainPanel(), BorderLayout.NORTH );
    //panWest.add( m_upStatus.getMainPanel(), BorderLayout.CENTER );
    //mapPanel = new JPanel();
    //mapPanel.setLayout( new BorderLayout() );
    //mapPanel.add( panWest, BorderLayout.WEST );
    //splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, mapPanel, m_upMessages.getMainPanel() );
    //mapFrame.getContentPane().setLayout( new BorderLayout() );
    //mapFrame.getContentPane().add( splitPane, BorderLayout.CENTER );
    //mapFrame.pack();
    //mapFrame.setVisible( true );
    //m_bSetup = true;
  }


  /**
   * Set up the main window menus.
   */
  private void setupMenus()
  {
    JMenuBar jmb = new JMenuBar();
    for( int i = 0;i < MENUS.length;i++ )
    {
      JMenu menu = new JMenu( _( MENUS[ i ][ 0 ] ) ); // _ should go with the literal.
      for( int j = 1;j < MENUS[ i ].length;j++ )
      {
        if( MENUS[ i ][ j ] == null )
        {
          menu.addSeparator();
        }
        else
        {
          Action a = m_client.getAction( MENUS[ i ][ j ] );
          if( a instanceof AbstractToggleAction )
          {
            menu.add( new ToggleActionMenuItem( (AbstractToggleAction)a ) );
          }
          else
          {
            menu.add( new ActionMenuItem( (AbstractClientAction)a ) );
          }
        }
      }
      jmb.add( menu );
    }
    setJMenuBar( jmb );
  }


  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}