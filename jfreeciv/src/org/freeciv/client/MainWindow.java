package org.freeciv.client;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.freeciv.client.action.BasicMoveFactory;
import org.freeciv.client.action.MenuFactory;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.client.map.MapMouseListener;
import org.freeciv.client.map.MapViewManager;
import org.freeciv.client.panel.CivInfoPanel;
import org.freeciv.client.panel.Console;
import org.freeciv.client.panel.MapOverview;
import org.freeciv.client.panel.QuickCommand;
import org.freeciv.client.panel.UnitInfoPanel;
import org.freeciv.client.panel.UnitStackDisplay;
import org.freeciv.client.ui.util.DockPanel;


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

  private static final String CONSOLE_SPLASHMSG =
    "Freeciv is free software and you are welcome to distribute copies of it\n"+
    "under certain conditions; See the \"Copying\" item in the Help menu.\n"+
    "Now.. Go give'em hell!";

  private HelpSystemImpl m_helpSystem;

  MainWindow( Client c )
  {
    m_desktop = new JDesktopPane();
    m_mapOverview = new MapOverview(
      c.getGame(),  c.getTileSpec().getImage( "minimap_intro_file" )
    );
    m_mapOverview.setVersion( c.APP_VERSION );
    m_civInfo = new CivInfoPanel( c );
    m_unitInfo = new UnitInfoPanel( c );
    m_console = new Console();
    m_quickCommand = new QuickCommand( c );
    m_unitStack = new UnitStackDisplay( c );

    m_client = c;

    m_viewManager = new MapViewManager( m_client );
    m_internalFrames = new HashMap();

    setTitle( m_client.APP_NAME );

    setupComponents();
    getContentPane().add( m_viewManager.getMainMapView().getComponent(),
      BorderLayout.CENTER );
    m_mapOverview.addJumpListener( m_viewManager.getMainMapView() );

    // Set the listener for mouse event
    m_viewManager.getMainMapView().getComponent().addMouseListener(
					  new MapMouseListener());

    setDefaultCloseOperation( this.DO_NOTHING_ON_CLOSE );
    addWindowListener( new WindowCloseListener() );

    m_console.println( CONSOLE_SPLASHMSG );

    m_helpSystem = new HelpSystemImpl( c, this );
  }

  /**
   * Get the help system
   *
   * @return an object implementing HelpSystem
   */
  public HelpSystem getHelpSystem()
  {
    return m_helpSystem;
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
  public MapViewManager getMapViewManager()
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
   * Sets up the main UI components of the main window. Only
   * does something the first time it is called.
   */
  private void setupComponents()
  {
    setupMenus();

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
  }


  /**
   * Set up the main window menus.
   */
  private void setupMenus()
  {
    MenuFactory.createMenus( m_client , this );
    BasicMoveFactory.createBasicMoves( m_client );
  }
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }

  private class WindowCloseListener extends WindowAdapter
  {
    public void windowClosing(WindowEvent e)
    {
      m_client.quit();
    }
  }
}
