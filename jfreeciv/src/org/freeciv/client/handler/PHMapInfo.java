package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.common.Map;
import org.freeciv.net.Packet;
import org.freeciv.net.PktMapInfo;

/**
 * Map info handler
 */
public class PHMapInfo implements ClientPacketHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktMapInfo";
  }
  
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktMapInfo pmi = (PktMapInfo)pkt;

    Map map = c.getGame().getMap();
    
    map.setWidth( pmi.xsize );
    map.setHeight( pmi.ysize );
    map.setEarth( pmi.isEarth );

    map.allocate();
    c.initContinents();

    c.createMap( pmi.xsize, pmi.ysize, pmi.isEarth );
    

    
  }
}
