package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.common.Map;
import org.freeciv.net.Packet;
import org.freeciv.net.PktMapInfo;

/**
 * Map info handler
 */
public class PHMapInfo extends AbstractHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktMapInfo";
  }

  void handleOnEventThread( Client c, Packet pkt )
  {
    PktMapInfo pmi = (PktMapInfo)pkt;

    Map map = c.getGame().getMap();
    
    map.setWidth( pmi.xsize );
    map.setHeight( pmi.ysize );
    map.setEarth( pmi.isEarth );

    map.allocate();
    c.initContinents();


    c.getMainWindow().getMapViewManager().initialize();
    c.getMainWindow().getMapOverview().setMapDimensions( 
      pmi.xsize, pmi.ysize
    ); 
  }

}
