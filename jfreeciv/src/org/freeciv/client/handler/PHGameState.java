package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericInteger;

/**
 * Handle the "game state" packet
 */
public class PHGameState implements ClientPacketHandler,  Constants
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
  public void handle( Client c, Packet pkt )
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
      // refresh_overview_canvas()
      // refresh_overview_viewrect()
      // enable_turn_done_button()
      // player_set_unit_focus_status()

      c.updateInfoLabel();

      // update_unit_focus()
      // update_unit_info_label();

      /* Find something interesting to display - see packhand.c */
    }
  }
}