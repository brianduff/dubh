package org.freeciv.client;

import java.io.*;
import javax.swing.*;
import org.freeciv.tile.*;
import java.awt.*;
import org.freeciv.client.dialog.*;
import org.freeciv.client.ui.*;
import org.freeciv.client.action.*;
import org.freeciv.client.ui.util.*;

// Tidied up imports from here onwards

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
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.border.TitledBorder;

import org.gjt.abies.SystemInfoPanel; // remove me soon bduff

import org.freeciv.common.Assert;
import org.freeciv.common.Factories;
import org.freeciv.common.Game;
import org.freeciv.common.Logger;
import org.freeciv.common.Player;
import org.freeciv.common.Tile;
import org.freeciv.common.ErrorHandler;
import org.freeciv.client.handler.ClientPacketDispacher;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.net.InStream;
import org.freeciv.net.OutStream;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericMessage;
import org.freeciv.net.PktReqJoinGame;

/**
 * This is the main class of Freeciv4J.
 */
public class Client implements Constants
{

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
  //private PktPlayerInfo[] players;
  // -- check this
  // Every turn, a game info packet is sent by the server with
  // global information about the current state of the game. The
  // packet is stored here.
  //private PktGameInfo gameInfo;
  // Information about the current player is stored here.
  // private PktPlayerInfo currentPlayer;
  //////////////////////// UI Components
  // The main map component

  // The desktop and MDI stuff


  int scaleDiv = 1;
  int scaleMul = 1;
  // BD: Same as clientGameState???
  int gameState;

  // Factories
  private Factories m_factories = new Factories();



  // recyc_init in climisc.c
  private boolean m_recycInit = false;
  private int m_lastContinentNumber = 0;


  // prob shouldn't instantiate this yet.
  private Game m_game = new Game(m_factories);
  public final static String APP_NAME = "Freeciv4J";
  public final static String APP_VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION + VERSION_LABEL;
  private final static String PROP_FREECIV_TILESET = "freeciv.tileset";
  private final static String DEFAULT_TILESET = "trident";
  public static final int majorVer = Constants.MAJOR_VERSION;
  public static final int minorVer = Constants.MINOR_VERSION;
  public static final int patchVer = Constants.PATCH_VERSION;
  public static final String CAPABILITIES = "+1.11.6 conn_info";
  public static final Integer MAP_PANEL_LAYER = new Integer( 0 );
  public static final Integer CITY_DIALOG_LAYER = new Integer( 2 );
  public static final Integer ADVISOR_DIALOG_LAYER = new Integer( 3 );
  public static final Integer HELP_DIALOG_LAYER = new Integer( 4 );
  public static final Integer SYSTEM_INFO_DIALOG_LAYER = new Integer( 5 );


  private MainWindow m_mainWindow;

  /**
   * Instantiate the client
   */
  public Client()
  {
    super( ); // !NLS



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



    m_mainWindow = new MainWindow( this );
    m_mainWindow.pack();

    // Try to start of in a semi-sensible location
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) (.85 * screenSize.width);
    int height = (int) (.85 * screenSize.height);

    m_mainWindow.setSize( new Dimension( width, height ) );
    m_mainWindow.setLocation(
      (screenSize.width - width) /2, (screenSize.height - height) /2
    );

    m_mainWindow.setVisible( true );

  }


  /**
   * Get the main window
   */
  public MainWindow getMainWindow()
  {
    return m_mainWindow;
  }



  public String getCapabilities()
  {
    return CAPABILITIES;
  }



  public void initContinents()
  {
    // climisc.c: climap_init_continents()

    // No idea what this is doing - mem mgmt crap from c.

  }

  public TileSpec getTileSpec()
  {
    return m_tileSpec;
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
      getMainWindow().setVisible( false ); // ?
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
      JOptionPane.showMessageDialog(
        getMainWindow(),
        _( "Error connection to server lost??" ), _( "Fatal Error" ),
        JOptionPane.ERROR_MESSAGE
      );
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
    prjg.capabilities = CAPABILITIES;
    prjg.version_label = Constants.VERSION_LABEL;
    return sendToServer( prjg );
  }


  public void addNotifyWindow( PktGenericMessage pgm )
  {
    // BD: TODO
    // dunno what this is
    getMainWindow().getConsole().println( pgm.message );
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
   * OR DISCONNECT, AND SHOULD PROBABLY NOT BE PUBLIC.
   */
  public synchronized void setConnected( boolean b )
  {
    m_bConnected = b;
    if( !b )
    {
      disconnect();
    }
  }
  public synchronized void disconnect()
  {
    try
    {
      sendMessage( "remove "+ name );   // Should it remove the player? Make it
                                        // ai?  JR
      out.close();
      in.close();
    }
    catch( Exception e ) { e.printStackTrace(); }
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
          if ( m_client.isConnected() )
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
  }

  /**
   * Creates the map
   */
  public void createMap( final int xsize, final int ysize, final boolean isEarth )
  {
    // map.isEarth ??
    // set map display
    /*
    SwingUtilities.invokeLater( new Runnable()
    {
      public void run()
      {
        getMainWindow().getMapOverview().setMapDimensions( xsize, ysize );

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
      });
      }
    } )
    */
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
    JOptionPane.showMessageDialog(
      getMainWindow(), e.toString(), _( "Fatal Server Connection Error" ),
      JOptionPane.ERROR_MESSAGE );
    System.exit( 1 ); // ??
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
    // ?
    try
    {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    }
    catch (Exception e )
    {

    }

    //parse args..
    Client c = new Client();
    Thread t = new Thread( new InputPacketListener( c ) );
    t.start();
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

  /**
   * Update information about continents stored by the client
   */
  public void updateContinents( int x, int y )
  {
    Tile tile = getGame().getMap().getTile( x, y );
    int  con, thisCon;

    if ( tile.getTerrain() == T_OCEAN) return;

    thisCon = -1;

    for ( int i = x-1; i < x+1; i++ )
    {
      for ( int j = y-1; j < y+1; j++ )
      {
        Tile thisTile = getGame().getMap().getTile( i, j );
        if (!(i==x && j==y) && j>0 && j < getGame().getMap().getWidth()
            && thisTile.isKnown()
            && thisTile.getTerrain() != T_OCEAN )
        {
          con = thisTile.getContinent( );

          if ( con > 0 )
          {
            if ( thisCon == -1 )
            {
              thisCon = con;
              tile.setContinent( thisCon );
            }
            else if ( con != thisCon )
            {
              renumberContinent( i, j, thisCon );
              recycleContinentNum( con );
            }
          }
        }
      }
    }

    if ( thisCon == -1 )
    {
      tile.setContinent( getNewContinentNumber() );
    }
  }

  /**
   * Renumber the continent at x, y. This natty bit of code is called
   * recursively and will renumber all tiles in a continental land mass which
   * contains the tile at map co-ordinate (x, y)
   */
  private void renumberContinent( int x, int y, int newnumber )
  {
    int old;

    if ( y < 0 || y >= getGame().getMap().getHeight() ) return;

    x = getGame().getMap().adjustX( x );

    Tile tile = getGame().getMap().getTile( x, y );

    old = tile.getContinent();

    Assert.that( tile.isKnown() );
    Assert.that( tile.getTerrain() != T_OCEAN );
    // Assert.that( old > 0 && old <= max_cont_used ); ??

    tile.setContinent( newnumber );

    for ( int i = x-1; i < x + 1; i++ )
    {
      for ( int j = y-1; j < y + 1; j++ )
      {
        if ( !( i == x && j == y ) && j >= 0 && j < getGame().getMap().getHeight()
            &&  getGame().getMap().getTile( i, j ).isKnown()
            &&  getGame().getMap().getTile( i, j ).getTerrain() != T_OCEAN
            &&  getGame().getMap().getTile( i, j ).getContinent() == old)
        {
          renumberContinent( i, j, newnumber );
        }
      }
    }
  }

  private int getNewContinentNumber()
  {
    return m_lastContinentNumber++;
  }

  private void recycleContinentNum( int num )
  {
    // NOOP
  }

  public void refreshTileMapCanvas( final int x, final int y, final boolean updateView )
  {
    // Not sure this method belongs in Client.java...
    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {
        // Actually, we should refresh all views here, but currently there is
        // only one view.
        getMainWindow().getMapViewManager().refreshTileMapCanvas( x, y );
        getMainWindow().getMapOverview().refresh( x, y );
      }
    });

  }


  /**
   * Update status information on the main window. It is safe to call this
   * from any thread.
   */
  public void updateInfoLabel()
  {

      SwingUtilities.invokeLater(new Runnable() {
        public void run()
        {

          Player p = getGame().getCurrentPlayer();
          // GTK updates the window title here to the nation name...
          getMainWindow().getCivInfo().setNationName(
            p.getNation().getName()
          );

          getMainWindow().getCivInfo().setPop(
            getGame().getCivilizationPopulation( p )
          );

          getMainWindow().getCivInfo().setYear( getGame().getYear() );
          getMainWindow().getCivInfo().setGold( p.getEconomy().getGold() );
          getMainWindow().getCivInfo().setTax( p.getEconomy().getTax(),
            p.getEconomy().getLuxury(), p.getEconomy().getScience() );
        }
      });

    // update indicator icons (research, warming, cooling, government)

    // Citizens

    // Timeout

  }


  public org.freeciv.common.Unit getUnitInFocus()
  {
     // TODO
     return null;
  }

  public org.freeciv.common.Unit getAttackingUnit()
  {
     // TODO
     return null;
  }

  public org.freeciv.common.Unit getDefendingUnit()
  {
     // TODO
     return null;
  }

  public boolean isUnitInFocus( org.freeciv.common.Unit unit )
  {
    return false;
  }

  /**
   * Call this when an internal error occurs (usually an unexpected
   * RuntimeException
   *
   * @param t
   */
  private void internalError( Throwable t )
  {
    ErrorHandler.getHandler().internalError( t );
  }
}
