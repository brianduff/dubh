package org.freeciv.client;

import org.freeciv.client.action.*;

/**
 * Contains an object which describes all the menus for JFreeciv
 *
 * @author Brian Duff
 */
public interface MenuDefinitions 
{
 /**
  * The menus for the application. Each of these is the class name of
  * an action handler that deals with that menu item
  */
  public static final Object[][] MENUS = new Object[][] {
    { "Game",
        ACTLocalOptions.class,
        ACTMessageOptions.class,
        ACTSaveSettings.class,
       /*---------------*/                 null,
        ACTPlayers.class,
        ACTMessages.class,
       /*---------------*/                 null,
        ACTServerOptInitial.class,
        ACTServerOptOngoing.class,
       /*---------------*/                 null,
        ACTExportLog.class,
        ACTClearLog.class,
       /*---------------*/                 null,
        ACTDisconnect.class,
        ACTQuit.class
    },
    { "Kingdom",
        ACTTaxRates.class,
       /*---------------*/                 null,
        ACTFindCity.class,
        ACTWorklists.class,
       /*---------------*/                 null,
        ACTRevolution.class
    },
    { "View",
        ACTMapGrid.class,
        ACTCenterView.class
    },
    { "Orders",
        UACTBuildCity.class,
        UACTBuildRoad.class,
        UACTBuildIrrigation.class,
        UACTMine.class,
        UACTTransformTerrain.class,
        UACTBuildFortress.class,
        UACTBuildAirbase.class,
        UACTCleanPollution.class,
       /*---------------*/                 null,
        UACTFortify.class,
        UACTSentry.class,
        UACTPillage.class,
       /*---------------*/                 null,
        UACTMakeHomeCity.class,
        UACTUnload.class,
        UACTWakeUpOthers.class,
       /*---------------*/                 null,
        UACTAutoSettler.class,
        UACTAutoAttack.class,
        UACTAutoExplore.class,
        UACTConnect.class,
        UACTGoTo.class,
        UACTGoToCity.class,
       /*---------------*/                     // Remove me
                                          null,
        UACTDisbandUnit.class,
        UACTHelpBuildWonder.class,
        UACTMakeTradeRoute.class,
        UACTExplodeNuclear.class,
       /*---------------*/                 null,
        UACTWait.class,
        UACTDone.class
    },
    { "Reports",
        ACTCityReport.class,
        ACTMilitaryReport.class,
        ACTTradeReport.class,
        ACTScienceReport.class,
       /*---------------*/                 null,
        ACTWondersOfTheWorld.class,
        ACTTopFiveCities.class,
        ACTDemographics.class,
        ACTSpaceship.class
    },
    { "Help",
        ACTHelpCopying.class
        //ACTHelpAbout.class
    }
 };
}