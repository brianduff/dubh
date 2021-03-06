// JFreeCiv: Java Freeciv client
// Copyright (C) 2001 A. Biesiadowski, B. Duff
//
//  This program is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package org.freeciv.client;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.InvocationTargetException;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.freeciv.client.action.ACTDisconnect;
import org.freeciv.client.action.AbstractClientAction;
import org.freeciv.client.action.AbstractUnitAction;
import org.freeciv.client.action.Actions;
import org.freeciv.client.dialog.DialogManager;
import org.freeciv.client.dialog.DlgLogin;
import org.freeciv.client.handler.ClientPacketDispacher;
import org.freeciv.common.City;
import org.freeciv.common.ErrorHandler;
import org.freeciv.common.Factories;
import org.freeciv.common.Game;
import org.freeciv.common.Logger;
import org.freeciv.common.Player;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;
import org.freeciv.net.InStream;
import org.freeciv.net.NetworkProtocolException;
import org.freeciv.net.OutStream;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericMessage;
import org.freeciv.net.PktReqJoinGame;
import org.freeciv.net.PktUnitInfo;

/**
 * This is the main class for the JFreeciv client. Only one instance of this
 * class exists.
 *
 * @author Artur Biesiadowski
 * @author Brian Duff
 */
public final class Client implements Constants
{
  // The only client object
  private static Client m_client;
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
  private String m_serverHost;
  // Server port number
  private int m_serverPort;
  // The username the user is connected using
  private String m_userName;
  // The current state of the client
  private int gameState = Constants.CLIENT_BOOT_STATE;

  // recyc_init in climisc.c
  private boolean m_recycInit = false;
  private int m_lastContinentNumber = 0;

  // prob shouldn't instantiate this yet.
  private Game m_game = new Game();
  public final static String APP_NAME = "JFreeCiv";
  public final static String APP_VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION + VERSION_LABEL;
  private final static String PROP_FREECIV_TILESET = "freeciv.tileset";
  private final static String DEFAULT_TILESET = "trident";
  public static final int majorVer = Constants.MAJOR_VERSION;
  public static final int minorVer = Constants.MINOR_VERSION;
  public static final int patchVer = Constants.PATCH_VERSION;
  public static final String CAPABILITIES = "+1.11.6 conn_info";


  private MainWindow m_mainWindow;

  // The currently focused unit
  private Unit m_focusUnit;

  /**
   * Instantiate the client
   */
  public Client()
  {
    super( );
    m_client = this;

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
        System.out.println( translate( "Sound init failed\n" ) + e );
      }
    }



    m_mainWindow = new MainWindow( this );
    m_mainWindow.pack();

    // Try to start of in a semi-sensible location
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) (.85 * screenSize.width);
    int height = (int) (.85 * screenSize.height);
    Dimension dim = new Dimension( width, height );

    int winx = (screenSize.width - width) / 2;
    int winy = (screenSize.height - height ) / 2;

    // Temporary code so that I can watch TV and code at the same time :)
    //winx = 0;
    //winy = 0;
    //dim = new Dimension( screenSize.width / 2 + 85 , screenSize.height - 75 );


    m_mainWindow.setSize( dim );
    m_mainWindow.setLocation( winx, winy );

    m_mainWindow.setVisible( true );

  }


  /**
   * Get the main window
   */
  public MainWindow getMainWindow()
  {
    return m_mainWindow;
  }


  /**
   * Static function to get the only client object
   *
   * @return the only client object
   */
  public static Client getClient()
  {
    return m_client;
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
   * Get the action instance for the specified action class.
   *
   * @param actionClass an action class
   * @return the stored instance of the specified action.
   */
  public AbstractClientAction getAction( Class actionClass )
  {
    return m_actions.getAction( actionClass );
  }

  /**
   * Get all currently registered actions.
   *
   * @return set of AbstractClientAction objects
   */
  public java.util.Collection getAllActions()
  {
    return m_actions.getAllActions();
  }

  /**
   * Enables or disables an action. Guaranteed to take place on
   * the UI thread.
   */
  public void setActionEnabled( final Class actionName, final boolean enabled )
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

  /**
   * Go thru the orders menu and de/activate the items based on whether
   * the currently focused unit can perform them.
   *
   * It is sort of painful associating the UnitAction with the MenuItem this
   * way.  Is there a better way? --Ben
   */
  public void updateOrdersMenu( Unit u )
  {
    final int ORDERS_MENU = 3;
    // go thru menu definitions...
    for( int i = 1 ; i < MenuDefinitions.MENUS[ ORDERS_MENU ].length; i++ )
    {
      if( MenuDefinitions.MENUS[ ORDERS_MENU ][ i ] != null )
      {
        // retrieve appropriate class...
        AbstractUnitAction aua = (AbstractUnitAction)getAction( (Class)MenuDefinitions.MENUS[ 3 ][ i ] );

        // set menu item dis/enabled
        JMenuItem item = m_mainWindow.getJMenuBar().getMenu( ORDERS_MENU ).getItem( i - 1 );
        if( u != null )
        {
          item.setEnabled( aua.isEnabledFor( u ) );
        }
        else
        {
          item.setEnabled( false );
        }
      }
    }
  }

  /**
   * Display the login dialog, wait for it to return, connect
   * to the server and attempt to join the game.
   */
  public int performLogin()
  {
    DlgLogin dlg = getDialogManager().getLoginDialog();
    dlg.display();

    if ( !isConnected() )
    {
      getMainWindow().setVisible( false );
      return 0;
    }

    joinGame();

    return 1;
  }

  /**
   * Create a new packet of the specified class.
   */
  public Packet createPacket( Class packetClass )
  {

    try
    {
      Packet p = (Packet) packetClass.getDeclaredConstructors()[0].newInstance(
        new Object[] { in }
      );
      return p;
    }
    catch (InvocationTargetException ite)
    {
      ErrorHandler.getHandler().internalError( ite );
    }
    catch (IllegalAccessException ille)
    {
      ErrorHandler.getHandler().internalError( ille );
    }
    catch (InstantiationException ine)
    {
      ErrorHandler.getHandler().internalError( ine );
    }

    return null;
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
        translate( "Error connection to server lost??" ), translate( "Fatal Error" ),
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
    prjg.name = m_userName;
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
  //public void setUnitStack( Unit u )
  //{



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
  //}
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
   * Terminates the client. You really ought to disconnect & stuff
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
   * Sets the connected flag.
   */
  private synchronized void setConnected( boolean b )
  {
    m_bConnected = b;
    getAction( ACTDisconnect.class ).setEnabled( b );
  }

  /**
   * Connect to the specified host and port
   *
   * @param host the host to connect to
   * @param port the port to connect to
   */
  public synchronized void connect( String username, String host, int port )
    throws IOException
  {
    Socket civserver = new Socket( host, port );

    InputStream input = civserver.getInputStream();
    OutputStream output = civserver.getOutputStream();

    in = new InStream( input );
    out = new OutStream( output );

    setConnected(true);
    m_userName = username;
  }

  /**
   * Actually disconnect
   */
  public synchronized void disconnect() throws IOException
  {

    sendMessage( "remove "+ m_userName ); // ? is this right?

    out.close();
    in.close();
    out = null;
    in = null;

    setConnected( false );



    getDialogManager().hideAllDialogs();

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
            Logger.log( Logger.LOG_ERROR, "Server IO Exception" );
            Logger.log( Logger.LOG_ERROR, e );
            return ;
          }
        }
        catch ( NetworkProtocolException nep )
        {
          Logger.log( Logger.LOG_ERROR, "Network protocol exception" );
          Logger.log( Logger.LOG_ERROR, nep );
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
      getMainWindow(), e.toString(), translate( "Fatal Server Connection Error" ),
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
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
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
    return getGame().getFactories();
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

    assert( tile.isKnown() );
    assert( tile.getTerrain() != T_OCEAN );
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

  /**
   * @deprecated use getMainWindow().getMapViewManager().refreshTileMapCanvas()
   */
  public void refreshTileMapCanvas( final int x, final int y,
    final boolean updateView )
  {
    getMainWindow().getMapViewManager().refreshTileMapCanvas( x, y );
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
            p.getEconomy().getScience(), p.getEconomy().getLuxury() );
        }
      });

    // update indicator icons (research, warming, cooling, government)

    // Citizens

    // Timeout

  }


  public org.freeciv.common.Unit getUnitInFocus()
  {
     return m_focusUnit;
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
    return ( m_focusUnit == unit );
  }

  public void updateUnitInfoLabel( org.freeciv.common.Unit u )
  {
    // TODO

    // this is called with null when the player controls no units that are
    // active on the map -- BenM
    if(u != null)
    {
      // Hmm not sure about this
      getMainWindow().getUnitStack().update(
        u.getX(), u.getY(), u
      );
      getMainWindow().getUnitInfo().setUnit( u );
    }
  }

  /**
   * Focus and select the specified unit
   */
  public void setUnitFocusAndSelect( org.freeciv.common.Unit u )
  {
    setUnitFocus( u );
    // Todo: put cross overlay tile.
  }

  /**
   * Set the focused unit.
   *
   * @param u the new focused unit
   */
  public void setUnitFocus( Unit u )
  {
    org.freeciv.common.Unit oldFocus = m_focusUnit;

    m_focusUnit = u;

    if ( u != null )
    {
      autoCenterOnFocusUnit();
      u.setFocusStatus( u.FOCUS_AVAIL );
      refreshTileMapCanvas( u.getX(), u.getY(), true );
    }

    if ( oldFocus != null
         && ( u == null
              || !getGame().getMap().isSamePosition(
                  oldFocus.getX(), oldFocus.getY(), u.getX(), u.getY() )))
    {
      refreshTileMapCanvas( oldFocus.getX(), oldFocus.getY(), true );
    }

    updateUnitInfoLabel( u );
    updateOrdersMenu( u );
  }


  public void autoCenterOnFocusUnit()
  {
    // control.c: auto_center_on_focus_unit()

    if ( m_focusUnit != null
         && getOptions().autoCenterOnUnit
         // TODO: Extra condition here
         )
    {
      getMainWindow().getMapViewManager().centerOnTile(
        m_focusUnit.getX(), m_focusUnit.getY()
      );
    }
  }

  /**
   * If there is no unit currently in focus, or if the current unit in focus
   * should not be in focus, then get a new focus unit.
   * We let GOTO-int units stay in focus, so that if they have moves left at
   * the end of the goto, then they are still in focus
   */
  public void updateUnitFocus()
  {
    // control.c: update_unit_focus()

    org.freeciv.common.Unit focus = getUnitInFocus();
    if ( focus == null
         || (focus.getActivity() != ACTIVITY_IDLE
             && focus.getActivity() != ACTIVITY_GOTO)
         || focus.getMovesLeft() == 0
         || focus.getAI().isControlled() )
    {
      advanceUnitFocus();
    }
  }

  /**
   * Actually figure out the next unit to focus
   */
  public void advanceUnitFocus()
  {
    // control.c: advance_unit_focus()
    org.freeciv.common.Unit oldFocus = m_focusUnit;

    m_focusUnit = findBestFocusCandidate();
    // setHoverState()...

    if ( m_focusUnit == null )
    {
      Iterator i = getGame().getCurrentPlayer().getUnits();
      while (i.hasNext())
      {
        org.freeciv.common.Unit punit = (org.freeciv.common.Unit)i.next();

        if ( punit.getFocusStatus() == punit.FOCUS_WAIT )
        {
          punit.setFocusStatus( punit.FOCUS_AVAIL );
        }
      }
      m_focusUnit = findBestFocusCandidate();
      if ( m_focusUnit == oldFocus )
      {
        // Ideally, we don't want the same unit as before
        m_focusUnit = findBestFocusCandidate();
        if ( m_focusUnit == null )
        {
          // But if it's the only one, we'll take it.
          m_focusUnit = findBestFocusCandidate();
        }
      }
    }

    if ( oldFocus != null && oldFocus != m_focusUnit )
    {
      refreshTileMapCanvas( oldFocus.getX(), oldFocus.getY(), true );
    }

    setUnitFocus( m_focusUnit );

    if ( getOptions().autoTurnDone && oldFocus != null && m_focusUnit == null )
    {
      // TODO: Programmatic end turn
    }
  }

  /**
   * Find the nearest available unit for focus, excluding the current unit
   * in focus ( if any ). If the current focus unit is the only
   * possible unit, or if there is no possible unit, returns null
   */
  private org.freeciv.common.Unit findBestFocusCandidate()
  {
    org.freeciv.common.Unit bestCandidate;
    int bestDist = 99999;
    int x, y;

    if ( m_focusUnit != null )
    {
      x = m_focusUnit.getX();
      y = m_focusUnit.getY();
    }
    else
    {
      // Hmm. the c client asks the map for this.
      x = getGame().getMap().getWidth() / 2;
      y = getGame().getMap().getHeight() / 2;
    }

    bestCandidate = null;
    Iterator i = getGame().getCurrentPlayer().getUnits();
    while ( i.hasNext() )
    {
      org.freeciv.common.Unit unit = (org.freeciv.common.Unit) i.next();
      if ( unit != m_focusUnit )
      {
        if ( unit.getFocusStatus() == unit.FOCUS_AVAIL
             && unit.getActivity() == unit.ACTIVITY_IDLE
             && unit.getMovesLeft() > 0
             && !unit.getAI().isControlled() )
        {
          int d = getGame().getMap().getSquareMapDistance(
            unit.getX(), unit.getY(), x, y
          );
          if ( d < bestDist )
          {
            bestCandidate = unit;
            bestDist = d;
          }
        }
      }
    }

    return bestCandidate;

  }

  /**
   * Handles moving the unit from its current position to the new
   * position in the unit info packet.
   */
  public void doMoveUnit( Unit unit, PktUnitInfo packet )
  {
    boolean wasTeleported = !getGame().getMap().isTilesAdjacent(
            unit.getX(), unit.getY(), packet.x, packet.y );
    int x = unit.getX();
    int y = unit.getY();
    //unit.setX( -1 ); // focus hack?

    getGame().getMap().getTile( x, y ).removeUnit( unit );

    if( !packet.carried )
    {
      refreshTileMapCanvas( x, y, wasTeleported );
    }

    if( getGame().isCurrentPlayer( unit.getOwner() )
       && unit.getActivity() != ACTIVITY_GOTO
       // && !tile_visible_and_not_on_border_mapcanvas( packet.x, packet.y )
       )
    {
      getMainWindow().getMapViewManager().centerOnTile( packet.x, packet.y );
    }

    if( !packet.carried && !wasTeleported )
    {
      int dx = packet.x - x;
      if ( dx > 1 )
      {
        dx = -1;
      }
      else if ( dx < -1 )
      {
        dx = 1;
      }
      if ( getOptions().smoothMoveUnits )
      {
        //move_unit_map_canvas(punit, x, y, dx, pinfo->y - punit->y);
      }
      refreshTileMapCanvas( x, y, true );
    }

    unit.setX( packet.x );
    unit.setY( packet.y );
    unit.setFuel( packet.fuel );
    unit.setHitPoints( packet.hp );
    getGame().getMap().getTile( unit.getX(), unit.getY() ).addUnit( unit );

    for ( y = unit.getY() - 2; y < unit.getY() + 3; ++y )
    {
      if (y < 0 || y > getGame().getMap().getHeight() )
      {
        continue;
      }
      for ( x = unit.getX() - 2; x < unit.getX() + 3; ++x )
      {
        for( Iterator i = getGame().getMap().getTile( x, y ).getUnits();
             i.hasNext(); )
        {
          refreshTileMapCanvas(
                getGame().getMap().adjustX( ( (Unit)i.next() ).getX() ),
                y, true );
        }
      }
    }

    if( !packet.carried
        && getGame().getMap().getTile( unit.getX(), unit.getY() ).getKnown() == TILE_KNOWN )
    {
      refreshTileMapCanvas( unit.getX(), unit.getY(), true );
    }

    // if ( isFocusUnit( unit ) )
    // {
    //   updateMenus();
    // }
  }

  /**
   * Remove the specified city from the map and the game, popping down the
   * city dialog if showing and updating the city report dialog
   *
   * @param c the city to remove
   */
  public void removeCity( City city )
  {
    int x = city.getX();
    int y = city.getY();

    Logger.log( Logger.LOG_DEBUG,
      "Removing city "+ city.getName() + ", "+
      city.getOwner().getNation().getName() + "(" +
      x + " " + y + ")");

    if( getDialogManager().getCityViewDialog().isShowing()
        && city.equals( getDialogManager().getCityViewDialog().getCity() ) )
    {
      getDialogManager().getCityViewDialog().undisplay();
    }
    city.removeFromGame();
    // TODO: update city report dialog
    refreshTileMapCanvas( x, y, true );
  }

  /**
   * Remove the specified unit from the map and game
   */
  public void removeUnit( Unit unit )
  {
    int x = unit.getX();
    int y = unit.getY();
    City city;

    Logger.log( Logger.LOG_DEBUG,
      "Removing unit " + unit.getId() + ", "
      + unit.getOwner().getNation().getName() + " "
      + unit.getUnitType().getName()
      + "(" + x + " " + y + ")"
      + " hcity " + unit.getHomeCity() );

    if ( isUnitInFocus( unit ) )
    {
      // setUnitFocusNoCenter( 0 );
      unit.removeFromGame();
      advanceUnitFocus();
    }
    else
    {
      boolean update = ( m_focusUnit != null
                        && m_focusUnit.getX() == unit.getX()
                        && m_focusUnit.getY() == unit.getY() );
      unit.removeFromGame();
      if( update ) {
        updateUnitInfoLabel( m_focusUnit );
      }
    }

    city = getGame().getMap().getCity( x, y );
    if( city != null )
    {
      getDialogManager().refreshCityDialog( city );
    }

    city = unit.getHomeCity();
    if( city != null )
    {
      getDialogManager().refreshCityDialog( city );
    }

    refreshTileMapCanvas( x, y, true );
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
