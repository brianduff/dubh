package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Game;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGameInfo;


/**
 * Game info handler
 */
final class PHGameInfo implements ClientPacketHandler, Constants
{
  public Class getPacketClass()
  {
    return PktGameInfo.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktGameInfo pinfo = (PktGameInfo)pkt;
    Game g = c.getGame();

    g.setGold(pinfo.gold);
    g.setTech(pinfo.tech);
    g.setResearchCost(pinfo.researchcost);
    g.setSkillLevel(pinfo.skill_level);
    g.setTimeout((pinfo.timeout != 0));

    g.setEndYear(pinfo.end_year);
    g.setYear(pinfo.year);
    g.setMinPlayers(pinfo.min_players);
    g.setMaxPlayers(pinfo.max_players);
    g.setNumberOfPlayers(pinfo.nplayers);
    g.setGlobalWarming(pinfo.globalWarming);
    g.setHeating(pinfo.heating);
    g.setNuclearWinter(pinfo.nuclearwinter);
    g.setCooling(pinfo.cooling);

    if ( c.getGameState() != CLIENT_GAME_RUNNING_STATE )
    {
      g.setCurrentPlayer( pinfo.player_idx );
    }

    for ( int i=0; i < A_LAST; i++ )
    {
      g.setGlobalAdvance( i, pinfo.global_advances[i] );
    }

    for ( int i=0; i < B_LAST; i++ )
    {
      g.setGlobalWonder( i, pinfo.global_wonders[i] );
    }

    if ( c.getGameState() != CLIENT_GAME_RUNNING_STATE )
    {
      if ( c.getGameState() == CLIENT_SELECT_RACE_STATE )
      {
        c.getDialogManager().getNationDialog().undisplay();
      }
    }

    g.setTechPenalty( pinfo.techpenalty );
    g.setFoodBox( pinfo.foodbox );
    g.setCivStyle( pinfo.civstyle );
    g.setUnhappySize( pinfo.unhappysize );
    g.setCityFactor( pinfo.cityfactor );

    boolean bootHelp = (c.getGameState() == CLIENT_GAME_RUNNING_STATE &&
      g.isSpaceRace() != pinfo.spacerace);

    g.setSpaceRace( pinfo.spacerace );

    if ( g.isTimeout() )
    {
      if ( pinfo.seconds_to_turndone != 0 )
      {
        //c.setSecondsToTurnDone( pinfo.seconds_to_turndone );
      }
    }
    else
    {
      //c.setSecondsToTurnDone( 0 );
    }

    if ( bootHelp )
    {
      // boot_help_texts()
    }

    // update_unit_focus()
    
  }
}
