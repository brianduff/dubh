package org.freeciv.client.handler;
import org.freeciv.client.dialog.DlgNation;
import org.freeciv.client.Constants;
import org.freeciv.client.Client;
import org.freeciv.net.*;
/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
public class PHSelectNation extends PHGenericValues implements Constants
{
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handle( Client c, Packet pkt )
  {
    PktGenericValues pgv = (PktGenericValues)pkt;
    if( c.getGameState() == CLIENT_SELECT_RACE_STATE )
    {
      // This is a special notifier that the game start was aborted
      // and that we should return to the login state. Of course
      // the select nation dialog is still there on the AWT event thread,
      // so we have to bring it down.
      if( pgv.value2 == 0xffff )
      {
        c.setGameState( CLIENT_WAITING_FOR_GAME_START_STATE );
        c.getDialogManager().getNationDialog().undisplay();
      }
      else
      {
        c.getDialogManager().getNationDialog().toggleAvailableRaces( pgv.value1, pgv.value2 );
      }
    }
    else
    {
      if( c.getGameState() == CLIENT_PRE_GAME_STATE )
      {
        c.setGameState( CLIENT_SELECT_RACE_STATE );
        c.getDialogManager().getProgressDialog().undisplay();
        c.getDialogManager().getNationDialog().display();
        c.getDialogManager().getNationDialog().toggleAvailableRaces( pgv.value1, pgv.value2 );
      }
      else
      {
        
      

      //   freelog(LOG_VERBOSE, "got a select nation packet in an incompatible state");
      }
    }
  }
}
