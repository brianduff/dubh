package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

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
   public void handle(Client c, Packet pkt)
   {
     PktMapInfo pmi = (PktMapInfo)pkt;
     c.createMap(pmi.xsize, pmi.ysize, pmi.isEarth);

   }

}