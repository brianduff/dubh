package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Game;
import org.freeciv.common.Map;
import org.freeciv.common.MapPosition;
import org.freeciv.common.MapPositionIterator;
import org.freeciv.common.Tile;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericInteger;

/**
 * Handle the "game state" packet
 */
public class PHGameState extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public String getPacketClass()
  {
    return "org.freeciv.net.PktGenericInteger";
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    PktGenericInteger packet = (PktGenericInteger) pkt;
    
    if (c.getGameState() == CLIENT_SELECT_RACE_STATE &&
        packet.value == CLIENT_GAME_RUNNING_STATE &&
        c.getGame().getCurrentPlayer().getNation() == null) // Check this maybe replace with isNationSet()
    {
      c.getDialogManager().getNationDialog().undisplay();
    }

    c.setGameState( packet.value );

    if (c.getGameState() == CLIENT_GAME_RUNNING_STATE)
    {
      c.getMainWindow().getMapOverview().refresh();
      c.updateInfoLabel();


      // TODO: Clever tile centering algorithm, as per handle_game_state..
      // Until normal service is resumed, however, find any old known tile...
      int hx = c.getGame().getMap().getWidth() / 2;
      int hy = c.getGame().getMap().getHeight() / 2;
      c.getGame().getMap().iterateOutwards( hx, hy, Math.max( hx, hy ), 
        new MapPositionIterator() 
        {
          public void iteratePosition( MapPosition pos )
          {
            Game g = c.getGame();
            Map m = g.getMap();
            Tile t = m.getTile( pos.x, pos.y );
            if ( t.isKnown() )
            {
              c.getMainWindow().getMapViewManager().centerOnTile(
                pos.x, pos.y
              );
              setFinished( true );
            }
          }
        }
      );

      
      //c.getMainWindow().getMapViewManager().centerOnTile( 40, 25 );  //TODO
      // enable_turn_done_button()
      // player_set_unit_focus_status()



      // update_unit_focus()
      // update_unit_info_label();

      /* Find something interesting to display - see packhand.c */
    }
  }
}