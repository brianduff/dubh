package org.freeciv.client;

import java.awt.BorderLayout;

import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.freeciv.client.action.AbstractClientAction;
import org.freeciv.client.action.AbstractToggleAction;
import org.freeciv.client.ui.util.ActionMenuItem;
import org.freeciv.client.ui.util.ToggleActionMenuItem;

/**
 * The main Window for FreeCiv4J.
 *
 * @author Brian.Duff@dubh.org
 */
public final class MainWindow extends JFrame
{
  private JDesktopPane m_desktop;
  private MapOverview m_mapOverview;
  private Client m_client;

  MainWindow( Client c )
  {
    m_desktop = new JDesktopPane();
    m_mapOverview = new MapOverview( c );
    m_client = c;

    setupComponents();
  }

  /**
   * Get the map overview component
   */
  public MapOverview getMapOverview()
  {
    return m_mapOverview;
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
    JPanel pan = new JPanel();
    pan.setLayout( new BorderLayout() );
    pan.add( getMapOverview(), BorderLayout.NORTH );
    getContentPane().add( pan, BorderLayout.WEST );
    getContentPane().add( m_desktop, BorderLayout.CENTER );
    
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