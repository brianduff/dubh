package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.client.action.ACTEndTurn;
import org.freeciv.common.City;
import org.freeciv.common.Game;
import org.freeciv.common.Map;
import org.freeciv.common.MapPosition;
import org.freeciv.common.MapPositionIterator;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktNewYear;

/**
 * Handle the "game state" packet
 * 
 * @author Ben Mazur, based off of PHGameState
 */
public class PHNewYear extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktNewYear.class;
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    PktNewYear packet = (PktNewYear)pkt;
    
    c.getAction( ACTEndTurn.class ).setEnabled( true );
    
    c.getGame().setYear( packet.year );
    c.updateInfoLabel();

    c.getGame().getCurrentPlayer().initUnitFocusStatus();
    c.updateUnitFocus();
    c.autoCenterOnFocusUnit();

    c.updateUnitInfoLabel( c.getUnitInFocus() );
    
    //turn_gold_difference=game.player_ptr->economic.gold-last_turn_gold_amount;
    //last_turn_gold_amount=game.player_ptr->economic.gold;
    
    //plrdlg_update_delay_off();
    c.getDialogManager().getPlayersDialog().refresh();
    
    //report_update_delay_off();
    //update_report_dialogs();

    //meswin_update_delay_off();
    //update_meswin_dialog();

    //update_city_descriptions();    c.getMainWindow().getMapViewManager().updateMapBuffersVisible();
  }
}
