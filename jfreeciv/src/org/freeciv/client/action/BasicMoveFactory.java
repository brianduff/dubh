package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import org.freeciv.client.Client;

/**
 * Creates all UACTMove actions and registers them at the ActionManager
 *
 * @author Julian Rueth
 */
public final class BasicMoveFactory
{
  public static Class[] moves=
  { 
    UACTMoveNorth.class,
    UACTMoveWest.class,
    UACTMoveSouth.class,
    UACTMoveEast.class,

    UACTMoveNorthWest.class,
    UACTMoveSouthWest.class,
    UACTMoveSouthEast.class,
    UACTMoveNorthEast.class
  };
  public static void createBasicMoves(Client client)
  {
    for(int i=0;i<moves.length;i++)
      ActionManager.actionManager.add(client.getAction(moves[i]));
  }
  private BasicMoveFactory()
  {
  }
}
