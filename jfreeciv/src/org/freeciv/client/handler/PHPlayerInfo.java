package org.freeciv.client.handler;


import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.DiplomacyState;
import org.freeciv.common.Government;
import org.freeciv.common.Nation;
import org.freeciv.common.Player;
import org.freeciv.net.Packet;
import org.freeciv.net.PktPlayerInfo;

/**
 * Game info handler
 */
public class PHPlayerInfo implements ClientPacketHandler, Constants
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktPlayerInfo";
  }
  
  /**
   */
  public void handle( final Client c, Packet pkt )
  {
    PktPlayerInfo pinfo = (PktPlayerInfo) pkt;
    
    Player p = c.getGame().getPlayer( pinfo.playerno );

    p.setName( pinfo.name );
    p.setNation( (Nation)
      c.getFactories().getNationFactory().findById( pinfo.nation )
    );
    p.setMale( pinfo.is_male );

    p.getEconomy().setGold( pinfo.gold );
    p.getEconomy().setTax( pinfo.tax );
    p.getEconomy().setScience( pinfo.science );
    p.getEconomy().setLuxury( pinfo.luxury );

    p.setGovernment( (Government)
      c.getFactories().getGovernmentFactory().findById( pinfo.government )
    );
    p.setEmbassy( pinfo.embassy );
    p.setGivesSharedVision( pinfo.gives_shared_vision );
    p.setCityStyle( pinfo.city_style );


    for ( int i=0; i < MAX_NUM_PLAYERS + MAX_NUM_BARBARIANS; i++)
    {
      DiplomacyState ds = p.getDiplomacyState(i);
      ds.setType( pinfo.diplstates[i].getType() );
      ds.setTurnsLeft( pinfo.diplstates[i].getTurnsLeft() );
      ds.setReasonToCancel( pinfo.diplstates[i].hasReasonToCancel() );
    }

    p.setReputation( pinfo.reputation );

    for (int i=0; i < MAX_NUM_WORKLISTS; i++)
    {
      p.setWorkList( i, pinfo.worklists[i] );
    }

    for(int i=0; i < c.getGame().getNumberOfTechnologyTypes(); i++)
    {
      p.getResearch().setHasInvention( i, pinfo.inventions[ i ] ); // ?
    }

    // c.updateResearch( p );

    //?
    boolean popTechUp = p.getResearch().getCurrentlyResearching().getId() != 
      pinfo.researching;

    p.getResearch().setResearched( pinfo.researched );
    p.getResearch().setResearchPoints( pinfo.researchpoints );
    p.getResearch().setCurrentlyResearching( pinfo.researching ); // lookup advance?

    p.setFutureTech( pinfo.future_tech );
    p.getAI().setTechGoal( pinfo.tech_goal );

    if ( c.getGameState() == CLIENT_GAME_RUNNING_STATE && 
      c.getGame().isCurrentPlayer( p ) )
    {
      if (popTechUp)
      {
        if ( !p.getAI().isControlled() ) // || ai_popup_windows
        {
          // c.popupScienceDialog( 1 );
        }
        // did_advance_tech_this_turn = c.getGame().getYear();  ??
        // c.updateScienceDialog();

        // if we just learned bridge building and focus is on a settler on
        // a river, the road menu item will remain disabled unless we do
        // this. 

        // if (c.isUnitFocused())
        // {
        //   c.updateMenus();
        // }
      }
    }

    p.setTurnDone( pinfo.turn_done );
    p.setNumberOfIdleTurns( pinfo.nturns_idle );
    p.setAlive( pinfo.is_alive );

    p.setConnected( pinfo.is_connected );

    p.getAI().setBarbarian( pinfo.is_barbarian );
    p.setRevolution( pinfo.revolution );

    if ( p.getAI().isControlled() != pinfo.ai )
    {
      p.getAI().setControlled( pinfo.ai );
      
      if ( c.getGame().isCurrentPlayer( p ) )
      {
        c.getMainWindow().getConsole().println( 
          "AI Mode is now "+
            (p.getAI().isControlled() ? "ON" : "OFF")
        );
      }
    }

    if ( c.getGame().isCurrentPlayer( p ) &&
         (p.getRevolution() < 1 || p.getRevolution() > 5) &&
         p.getGovernment() == c.getGame().getGovernmentWhenAnarchy() &&
         (!p.getAI().isControlled() || true ) && // ai_popup_windows
         c.getGameState() == CLIENT_GAME_RUNNING_STATE )
    {
      // c.popupGovernmentDialog();
    }

    // c.updatePlayersDialog();
    // c.updateWorklistReportDialog();

    if ( c.getGame().isCurrentPlayer( p ) )
    {
      if ( c.getGameState() == CLIENT_GAME_RUNNING_STATE )
      {
        if ( !p.isTurnDone() )
        {
          // c.setTurnDoneButtonEnabled( true );
        }
        else
        {
          // c.updateTurnDoneButton();
        }
        c.updateInfoLabel();
      }
    }
  }
}
