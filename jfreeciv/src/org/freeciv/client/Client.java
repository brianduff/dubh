package org.freeciv.client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.freeciv.net.*;
import org.freeciv.tile.*;
import org.gjt.abies.SystemInfoPanel;
import java.awt.*;
import org.freeciv.client.dialog.*;
import org.freeciv.client.handler.ClientPacketDispacher;
import org.freeciv.client.ui.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.client.action.*;
import org.freeciv.client.ui.util.*;
import org.freeciv.common.Factories;
import org.freeciv.common.Game;
import org.freeciv.common.Logger;
/**
 * This is the main class of Freeciv4J.
 */
public class Client extends JFrame implements ComponentListener,UndockablePanel.DockTarget,Constants
{
  
  

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
         /*---------------*/                 null,
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

  

  // The tile spec holds all the images that the client uses
  private TileSpec m_tileSpec;
  // The actions are anything the user can do through the interface
  private Actions m_actions;
  // The dialog manager is responsible for providing and displaying dialogs
  private DialogManager m_dlgManager;
  // The sound system deals with playing sounds.
  private SndSystem sound;
  // The dispacher deals with incoming packets from the server.
  private ClientPacketDispacher m_dispacher;
  // The options object contains user options
  private Options m_options;
  // Boolean flags to remember bits of our state
  private boolean m_bAlive = true, m_bConnected, m_bSetup;
  // Input / Output stream for the server
  private InStream in;
  private OutStream out;
  // The name of the server.
  private String server;
  // Server port number
  private int port;
  // ??
  private String name;
  // The capabilities string from the server.
  private String serverCapabilities;
  // BD: redundant?
  private boolean alive = true;
  // The current state of the client.
  private int clientGameState = Constants.CLIENT_BOOT_STATE;
  // All the units in the game are stored in this list
  private ArrayList units = new ArrayList( 1000 );
  // All the cities in the game are stored in this list
  private ArrayList cities = new ArrayList( 1000 );
  // Information about all current players is stored in this
  // array
  private PktPlayerInfo[] players;
  // -- check this
  // Every turn, a game info packet is sent by the server with
  // global information about the current state of the game. The
  // packet is stored here.
  private PktGameInfo gameInfo;
  // Information about the current player is stored here.
  private PktPlayerInfo currentPlayer;
  //////////////////////// UI Components
  // The main map component
  private CivMap map;
  private JComponent mapPanel;
  // The desktop and MDI stuff
  private JDesktopPane desktop = new JDesktopPane();
  private JInternalFrame mapFrame;
  private JSplitPane splitPane;
  // ?????
  private JButton buttons[] = new JButton[ 8 ];
  // The chat area at the bottom of the main window
  private JPanel chatArea;
  private JTextArea chatText = new JTextArea( "", 2, 80 );
  private JTextField chatInput;
  private JScrollPane chatTextScroll = new JScrollPane( chatText );
  // The panel that is at the left of the screen with various
  // status displays and the minimap  (I think)
  private VerticalFlowPanel controls;
  // not used?
  private JPanel leftControls;
  // Redundant I think
  // Control that displays a description of the current unit
  // private UnitDescription unitDescription;
  // Control that displays info on a unit stack.
  // private UnitStackDisplay unitStack;
  // The help UI (BD: Move to dialog mgr)
  private HelpPanel helpPanel;
  private JInternalFrame helpFrame;
  // BD: Shouldn't be used any more
  // String dataDir;
  // File cacheDir;
  // BD: Dunno
  int scaleDiv = 1;
  int scaleMul = 1;
  // BD: Same as clientGameState???
  int gameState;
  // Undockable panels that contain the main status displays
  private UndockablePanel m_upMessages;
  private UndockablePanel m_upMiniMap;
  private UndockablePanel m_upStatus;
  // Various status panels
  private CivInfoPanel m_panCivInfo;
  private CivStatusPanel m_panCivStatus;
  private UnitInfoPanel m_panUnitInfo;
  private UnitStackDisplay m_panUnitStack;
  private JPanel panWest;
  // Factories
  private Factories m_factories = new Factories();
  // prob shouldn't instantiate this yet. 
  private Game m_game = new Game();
  private final static String APP_NAME = "Freeciv4J";
  private final static String APP_VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION + VERSION_LABEL;
  private final static String PROP_FREECIV_TILESET = "freeciv.tileset";
  private final static String DEFAULT_TILESET = "trident";
  public static final int majorVer = Constants.MAJOR_VERSION;
  public static final int minorVer = Constants.MINOR_VERSION;
  public static final int patchVer = Constants.PATCH_VERSION;
  public static final String capabilities = "+1.11.6 conn_info";
  public static final Integer MAP_PANEL_LAYER = new Integer( 0 );
  public static final Integer CITY_DIALOG_LAYER = new Integer( 2 );
  public static final Integer ADVISOR_DIALOG_LAYER = new Integer( 3 );
  public static final Integer HELP_DIALOG_LAYER = new Integer( 4 );
  public static final Integer SYSTEM_INFO_DIALOG_LAYER = new Integer( 5 );
  /**
   * Instantiate the client
   */
  public Client() 
  {
    super( APP_NAME + " ver " + APP_VERSION ); // !NLS
    // Take a hit right now and load the images etc.
    // BD: Need to do most of this in a different thread so connection
    // dialog appears sharpish.
    Logger.log( Logger.LOG_NORMAL, "Loading images..." );
    String ts = System.getProperty( PROP_FREECIV_TILESET );
    if( ts == null )
    {
      Logger.log( Logger.LOG_NORMAL, "No tileset was specified. You can specify a tileset using the " + PROP_FREECIV_TILESET + " Java system property." );
      Logger.log( Logger.LOG_NORMAL, "Trying the default, \"" + DEFAULT_TILESET + "\"." );
      ts = DEFAULT_TILESET;
    }
    m_tileSpec = new TileSpec( this );
    m_tileSpec.loadTileset( ts );
    Logger.log( Logger.LOG_NORMAL, "Finished loading images" );
    m_options = new Options();
    m_options.initMessagesWhere();
    m_actions = new Actions( this );
    //      dataDir = System.getProperty("freeciv.datadir","data");
    //      cacheDir = new File(dataDir,"cache");
    //      String currentTileset = System.getProperty("freeciv.tileset","trident");
    //      cacheDir = new File(cacheDir, currentTileset);
    setSize( java.awt.Toolkit.getDefaultToolkit().getScreenSize() );
    getContentPane().add( desktop );
    // Set up the sound system
    String soundDir = System.getProperty( "freeciv.sound" );
    if( soundDir == null )
    {
      sound = new SndSystem();
    }
    else
    {
      try
      {
        sound = new RealSndSystem( soundDir );
      }
      catch( IOException e )
      {
        System.out.println( _( "Sound init failed\n" ) + e );
      }
    }
    /*   fillSoft(tileIcons);
    fillSoft(unitIcons);
    fillSoft(unitIconsDim);
    fillSoft(roadIcons);
    fillSoft(flagIcons);
    fillSoft(darkIcons);
    fillSoft(smallIcons);
    fillSoft(dblsizeSmallIcons);
    initTileMetrics();
    for ( int i =0; i < Constants.T_LAST; i++ )
    unknownTerrains[i] = new UnknownTerrain(i);
    emptyRoad = new RoadOverlay(null,false);
    emptyRoad.setVisible(false);
    emptyRail = new RoadOverlay(null,true);
    emptyRail.setVisible(false);
    */
    // BD: ?????
    addComponentListener( this );
  }
  public CivMap getMap()
  {
    return map;
  }
  public TileSpec getTileSpec()
  {
    return m_tileSpec;
  }
  /**
   * Get an icon from the tileset using the specified key, which is
   * an image tag
   */
  public Icon getImage( String key )
  {
    return m_tileSpec.getImage( key );
  }
  /**
   * Get the status panel that is displaying civilization information
   */
  public CivInfoPanel getCivInfoPanel()
  {
    return m_panCivInfo;
  }
  /**
   * Get the action associated with the specified name
   */
  public AbstractClientAction getAction( String actionName )
  {
    return m_actions.getAction( actionName );
  }
  /**
   * Enables or disables an action. Guaranteed to take place on
   * the UI thread.
   */
  public void setActionEnabled( final String actionName, final boolean enabled )
  {
    SwingUtilities.invokeLater( new Runnable() 
    {
      public void run()
      {
        getAction( actionName ).setEnabled( enabled );
      }
    } );
  }
  /**
   * Get the packet dispacher that reads incoming packets and dishes
   * them out to hander instances.
   */
  private ClientPacketDispacher getClientDispacher()
  {
    if( m_dispacher == null )
    {
      m_dispacher = new ClientPacketDispacher( this );
    }
    return m_dispacher;
  }
  /**
   * Get the user options object
   */
  public Options getOptions()
  {
    return m_options;
  }

  /**
   * Get the dialog manager.
   */
  public DialogManager getDialogManager()
  {
    if( m_dlgManager == null )
    {
      m_dlgManager = new DialogManager( this );
    }
    return m_dlgManager;
  }
  /**
   * Get the main window
   */
  public JFrame getMainFrame()
  {
    // BD: this is in case we stop subclassing JFrame directly.
    return this;
  }
  /**
   * Get the server capabilities string
   */
  public String getServerCapabilities()
  {
    return serverCapabilities;
  }
  /**
   * Set the server capabilities string. Normally used by the handler
   * that deals with connection to store the server capability string
   * in the client for later use
   */
  public void setServerCapabilities( String cap )
  {
    serverCapabilities = cap;
  }
  /**
   * Get the current game state. Returns one of the CLIENT_*_STATE
   * constants defined in the Constants interface.
   */
  public int getGameState()
  {
    return gameState;
  }
  /**
   * Sets the current game state. The parameter should be one of the
   * CLIENT_*_STATE constants in the Constants interface
   */
  public void setGameState( int state )
  {
    gameState = state;
  }
  /**
   * Set the game info packet
   */
  public void setGameInfo( PktGameInfo gi )
  {
    gameInfo = gi;
    if( players == null )
    {
      players = new PktPlayerInfo[ gi.max_players ];
    }
  }
  /**
   * Get the game info packet
   */
  public PktGameInfo getGameInfo()
  {
    return gameInfo;
  }
  /**
   * Get the current player
   */
  public PktPlayerInfo getCurrentPlayer()
  {
    return currentPlayer;
  }
  /**
   * Set the current player
   */
  public void setCurrentPlayer( PktPlayerInfo pi )
  {
    currentPlayer = pi;
  }
  /**
   * Get the specified player
   */
  public PktPlayerInfo getPlayer( int i )
  {
    return players[ i ];
  }
  /**
   * Set the specified player
   */
  public void setPlayer( int i, PktPlayerInfo pi )
  {
    players[ i ] = pi;
  }
  /**
   * Sets up the UI for the chat area
   */
  private void setupMessagesPanel()
  {
    m_upMessages = new UndockablePanel();
    chatInput = new JTextField( "", 80 );
    chatInput.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent e )
      {
        sendMessage( chatInput.getText() );
        chatInput.setText( "" );
      }
    } );
    chatText.setMinimumSize( new Dimension( 100, 30 ) );
    chatText.setEditable( false );
    chatArea = new JPanel();
    chatArea.setLayout( new BoxLayout( chatArea, BoxLayout.Y_AXIS ) );
    chatArea.add( chatTextScroll );
    chatArea.add( chatInput );
    chatArea.setMinimumSize( new Dimension( 100, 40 ) );
    m_upMessages.setContent( _( "Messages" ), chatArea, this );
  }
  /**
   * Sets up the UI for the mini map panel
   */
  private void setupMiniMapPanel()
  {
    m_upMiniMap = new UndockablePanel();
    JPanel pan = new JPanel();
    pan.setLayout( new BorderLayout() );
    JLabel lab = new JLabel();
    lab.setIcon( getImage( "minimap_intro_file" ) );
    pan.add( lab, BorderLayout.CENTER );
    m_upMiniMap.setContent( _( "Mini Map" ), pan, this );
  // TODO
  }
  /**
   * Sets up the UI for the status panel
   */
  private void setupStatusPanel()
  {
    m_upStatus = new UndockablePanel();
    controls = new VerticalFlowPanel();
    m_panCivInfo = new CivInfoPanel( this );
    m_panCivStatus = new CivStatusPanel( this );
    m_panUnitInfo = new UnitInfoPanel( this );
    //      m_panUnitStack = new UnitStackDisplay(getIcon(roadIcons, "roads", 0), 2, this);
    // TODO: Sort out stack.
    controls.addRow( m_panCivInfo );
    controls.addRow( m_panCivStatus );
    controls.addRow( m_panUnitInfo );
    //      controls.addSpacerRow(m_panUnitStack);
    m_upStatus.setContent( _( "Status" ), controls, this );
  }
  /**
   * Sets up the main UI components of the main window. Only
   * does something the first time it is called.
   */
  public void setupComponents()
  {
    if( !m_bSetup )
    {
      mapFrame = new JInternalFrame( _( "Map" ), true, false, true, true );
      setupMenus();
      desktop.add( mapFrame, MAP_PANEL_LAYER );
      setupMessagesPanel();
      setupMiniMapPanel();
      setupStatusPanel();
      // OK, now add all the panels to the main window.
      panWest = new JPanel();
      panWest.setLayout( new BorderLayout() );
      panWest.add( m_upMiniMap.getMainPanel(), BorderLayout.NORTH );
      panWest.add( m_upStatus.getMainPanel(), BorderLayout.CENTER );
      mapPanel = new JPanel();
      mapPanel.setLayout( new BorderLayout() );
      mapPanel.add( panWest, BorderLayout.WEST );
      splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, mapPanel, m_upMessages.getMainPanel() );
      mapFrame.getContentPane().setLayout( new BorderLayout() );
      mapFrame.getContentPane().add( splitPane, BorderLayout.CENTER );
      mapFrame.pack();
      mapFrame.setVisible( true );
      m_bSetup = true;
    }
  }
  /**
   * Sets up all of our menus
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
          Action a = m_actions.getAction( MENUS[ i ][ j ] );
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
    mapFrame.setJMenuBar( jmb );
  // TODO: Register keyboard actions
  }
  // BD: Did I do this?????
  ArrayList unitActions = new ArrayList();
  private void registerUnitAction( Action act )
  {
    unitActions.add( act );
  }
  public void updateOrdersMenu( Unit u )
  {
    
  

  // BD: Does this logic go here?
  }
  /**
   * Display the login dialog, wait for it to return, connect
   * to the server and attempt to join the game.
   */
  public int performLogin()
  {
    DlgLogin dlg = getDialogManager().getLoginDialog();
    dlg.display();
    if( dlg.isOK() )
    {
      in = new InStream( dlg.getInputStream() );
      out = new OutStream( dlg.getOutputStream() );
      server = dlg.getServerName();
      port = dlg.getPortNumber();
      name = dlg.getUserName();
    }
    else
    {
      setVisible( false );
      return 0;
    }
    joinGame();
    // Redundant?
    return 1;
  }
  /**
   * Send the specified packet to the server.
   * Returns false if the send failed. This normally means
   * that the connection was lost, and an error dialog will
   * be displayed to the user before this method returns.
   */
  public boolean sendToServer( Packet pack )
  {
    try
    {
      pack.send( out );
      return true;
    }
    catch( IOException ioe )
    {
      JOptionPane.showMessageDialog( this, _( "Error connection to server lost??" ), _( "Fatal Error" ), JOptionPane.ERROR_MESSAGE );
    }
    return false;
  }
  /**
   * Sends a request to join the game to the server.
   * BD: Need to pull out outgoing packets?
   */
  private boolean joinGame()
  {
    PktReqJoinGame prjg = new PktReqJoinGame();
    prjg.name = name;
    prjg.majorVer = majorVer;
    prjg.minorVer = minorVer;
    prjg.patchVer = patchVer;
    prjg.capabilities = capabilities;
    prjg.version_label = Constants.VERSION_LABEL;
    return sendToServer( prjg );
  }
  /**
   * Append the specified message to the output window (chat
   * area)
   */
  public void appendOutputWindow( String str )
  {
    // BD: On event thread?
    chatText.setRows( chatText.getRows() + 1 );
    chatText.append( str + "\n" );
    int y = chatText.getHeight() - chatTextScroll.getHeight();
    chatTextScroll.getViewport().setViewPosition( new Point( 0, y ) );
  }
  public void addNotifyWindow( PktGenericMessage pgm )
  {
    // BD: TODO
    // dunno what this is
    appendOutputWindow( pgm.message );
  }
  /**
   * Hides all windows and disposes of the underlying OS resources
   * for this frame. Don't call this unless you intend to terminate
   * the application or construct a new Client.
   */
  public void hideAllWindows()
  {
    setVisible( false );
    dispose();
  }
  /**
   * BD: Sort out unit stacks!
   */
  public void setUnitStack( Unit u )
  {
    
  
  
  /*    Dimension d =unitStack.getWantedSize();
  unitStackScrollPane.setSize(d);
  unitStackScrollPane.setPreferredSize(d);
  */
  /*      unitStack.removeAllUnits();
  if ( u != null )
  {
  unitStack.addUnitStack(u);
  }
  unitStack.repaint();
  */
  }
  /**
   * BD: Sort out unit stacks
   */
  public void changedActiveUnit( Unit u )
  {
    
  
  
  /*      unitDescription.setUnit(u);
  setUnitStack(u);
  updateOrdersMenu(u);
  */
  }
  /**
   * Plays the specified sound
   */
  public void playSound( String id )
  {
    sound.play( id );
  }
  /**
   * Displays the help UI for the specified help item
   */
  public void showHelp( String category, String item )
  {
    
  

  // BD: Need to sort out datadir stuff.
  /*
  if ( helpPanel == null )
  {
  try {
  helpPanel = new HelpPanel(this,
  new File(new File(dataDir,"help"),"helpdata.txt"));
  } catch ( IOException e )
  {
  // add verbose error panel here
  JOptionPane.showInternalMessageDialog(desktop,
  _("No help available on this subject"),
  _("No Help"), JOptionPane.WARNING_MESSAGE);
  System.out.println(e);
  return;
  }
  }
  
  if ( helpFrame != null )
  {
  helpFrame.getContentPane().removeAll();
  helpFrame.setVisible(false);
  helpFrame.dispose();
  desktop.remove(helpFrame);
  }
  
  helpFrame = new JInternalFrame(_("Help"),true,true,true/*,true);
  desktop.add(helpFrame,HELP_DIALOG_LAYER);
  helpFrame.getContentPane().add(helpPanel);
  helpFrame.pack();
  helpFrame.show();
  helpPanel.showHelp(category, item);       */
  }
  /**
   * Terminates the client. You really out to disconnect & stuff
   * before calling this, as it just basically system.exits
   */
  public synchronized void quit()
  {
    System.exit( 0 );
  }
  /**
   * Hmm. Is alive?
   */
  public synchronized boolean isAlive()
  {
    return m_bAlive;
  }
  /**
   * Are we currently connected to the server?
   */
  public synchronized boolean isConnected()
  {
    return m_bConnected;
  }
  /**
   * Sets the connected flag. THIS DOESN'T ACTUALLY CONNECT
   * OR DISCONNECT.
   */
  public synchronized void setConnected( boolean b )
  {
    m_bConnected = b;
  }
  /**
   * The runnable object that receives incoming packets from the server.
   * Because this is multithreaded, it is IMPERATIVE that all UI code in
   * packet handlers, and any methods that packet handlers might call
   * is invoked using SwingUtilities.invokeLater(), this will ensure that
   * it happens on the AWT event thread and not our listener thread. Packet
   * handlers should never block; e.g even if they bring up a modal dialog. This
   * can be accomplished by using invokeLater, as this will cause the dialog
   * to be invoked on the AWT event thread and will immediately return on the
   * listener thread without blocking.
   */
  static class InputPacketListener implements Runnable
  {
    private Client m_client;
    public InputPacketListener( Client c ) 
    {
      m_client = c;
    }
    public void run()
    {
      while( m_client.isAlive() )
      {
        // OK. mind bending thread stuff. This method will invoke
        // the login dialog on the AWT event thread, but will lock
        // the current thread (because it is modal). After returning,
        // the user will have dismissed the dialog. Connection
        // may or may not have taken place.
        m_client.performLogin();
        // user oked the login dialog, and successfully connnected.
        // Enter the loop and keep listening until the server has
        // been shutdown or the client is terminated.
        listen();
      }
    }
    // main packet input handle loop
    public void listen()
    {
      while( m_client.isConnected() && m_client.isAlive() )
      {
        try
        {
          m_client.in.recvPacket();
          m_client.getClientDispacher().dispach( m_client.in );
        }
        catch( IOException e )
        {
          System.err.println( "Server io exception" + e );
          e.printStackTrace();
          // Need to do these in an invokeLater
          //JOptionPane.showMessageDialog(c,
          //   e.toString(),_("Server connection error"),JOptionPane.ERROR_MESSAGE);
          //c.hideAllWindows();
          return ;
        }
      }
    }
  }
  /**
   * Creates the map
   */
  public void createMap( final int xsize, final int ysize, final boolean isEarth )
  {
    // map.isEarth ??
    // set map display
    // BD: Looks ok at the mo' need to change the tile sizes, tho'.
    SwingUtilities.invokeLater( new Runnable() 
    {
      public void run()
      {
        map = new CivMap( Client.this, xsize, ysize, getTileSpec().getNormalTileWidth(), getTileSpec().getNormalTileHeight(), true, false );
        map.setSize( mapFrame.getContentPane().getSize() );
        JScrollPane scroller = new JScrollPane( map );
        scroller.getHorizontalScrollBar().setUnitIncrement( getTileSpec().getNormalTileWidth() );
        scroller.getHorizontalScrollBar().setBlockIncrement( getTileSpec().getNormalTileWidth() * 3 );
        scroller.getVerticalScrollBar().setUnitIncrement( getTileSpec().getNormalTileHeight() );
        scroller.getVerticalScrollBar().setBlockIncrement( getTileSpec().getNormalTileHeight() * 3 );
        mapPanel.add( scroller, BorderLayout.CENTER );
        mapPanel.invalidate();
      /*mapPanel.addComponentListener(new ComponentAdapter() {
      
      public void componentResized(ComponentEvent e)
      {
      map.setSize(mapPanel.getSize());
      }
      }); */
      }
    } );
  }
  // Yeech. Move this to the top or eliminate it BD
  boolean mapAdded = false;
  /**
   * Sends a chat message. Prob want to kill this
   */
  public void sendMessage( String str )
  {
    PktGenericMessage pkt = new PktGenericMessage();
    pkt.message = str;
    pkt.setType( Constants.PACKET_CHAT_MSG );
    sendToServer( pkt );
  }
  /**
   * BD: Check
   */
  private void serverError( IOException e )
  {
    JOptionPane.showMessageDialog( this, e.toString(), _( "Fatal Server Connection Error" ), JOptionPane.ERROR_MESSAGE );
    System.exit( 1 ); // ??
  }
  /**
   * BD?
   */
  public void componentResized( ComponentEvent evt )
  {
    if( mapFrame != null )
    {
      getContentPane().invalidate();
      Dimension d = getSize();
      try
      {
        mapFrame.setMaximum( false );
        mapFrame.setMaximum( true );
      }
      catch( Exception e )
      {
        
      }
    }
  }
  // BD?????
  public void componentMoved( ComponentEvent e )
  {
    
  

  //TODO: implement this java.awt.event.ComponentListener method;
  }
  public void componentShown( ComponentEvent e )
  {
    
  

  //TODO: implement this java.awt.event.ComponentListener method;
  }
  public void componentHidden( ComponentEvent e )
  {
    
  

  //TODO: implement this java.awt.event.ComponentListener method;
  }
  // BD: ????????????
  // BD: Image stuff outa here
  //
  AlphaComposite calpha = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.50F );
  Color ccolor = new Color( 0, 0, 200 );
  public AlphaComposite getComponentAlpha()
  {
    return calpha;
  }
  public Color getComponentColor()
  {
    return ccolor;
  }
  // Don't really think this is necessary: I'm pretty sure
  // array list does its own scaling
  public static void growArray( ArrayList l, int len )
  {
    for( int i = len - l.size() + 1;i > 0;i-- )
    {
      l.add( null );
    }
  }
  // Fair enough.
  private static void sleep( int ms )
  {
    try
    {
      Thread.currentThread().sleep( ms );
    }
    catch( InterruptedException e )
    {
      
    }
  }
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
  // Move into Shared
  private static int WIPEBIT( int val, int no )
  {
    return ( ( ~( -1 << no ) ) & val ) | ( ( ( -1 << ( no + 1 ) ) & val ) >> 1 );
  }
  /**
   * The main program for Client
   */
  public static void main( String[] argv )
  {
    //parse args..
    Client c = new Client();
    c.show();
    c.setupComponents();
    Thread t = new Thread( new InputPacketListener( c ) );
    t.start();
  }
  // Docking TODO
  public boolean dock( UndockablePanel up )
  {
    return false;
  }
  public boolean undock( UndockablePanel up )
  {
    return false;
  }
  public boolean close( UndockablePanel up )
  {
    return false;
  }
  public Game getGame()
  {
    return m_game;
  }
  // FACTORIES

  public Factories getFactories()
  {
    return m_factories; 
  }
}
