package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Game info handler
 */
public class PHGameInfo implements ClientPacketHandler
{

   public String getPacketClass()
   {
     return "org.freeciv.net.PktGameInfo";
   }

   /**
    */
   public void handle(Client c, AbstractPacket pkt)
   {
     PktGameInfo pgi = (PktGameInfo)pkt;
     c.setGameInfo(pgi);

   }

}