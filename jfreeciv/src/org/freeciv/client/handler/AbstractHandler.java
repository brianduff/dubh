package org.freeciv.client.handler;

import org.freeciv.net.AbstractPacket;
import org.freeciv.client.Client;

import javax.swing.SwingUtilities;

/**
 * This handler class is useful for handlers that need to do things
 * on the UI thread.
 */
abstract class AbstractHandler implements ClientPacketHandler
{
   public final void handle(final Client c, final AbstractPacket pkt)
   {
      SwingUtilities.invokeLater(new Runnable() {
         public void run()
         {
            handleOnEventThread(c, pkt);
         }
      });

      handleOnCurrentThread(c, pkt);
   }

   /**
    * The implementation of this method will always be executed
    * on the current thread. This is usually the thread that listens
    * for incoming packets from the server.
    */
   void handleOnCurrentThread(Client c, AbstractPacket pkt)
   {}

   /**
    * The implementation of this method will always be executed on
    * the AWT event thread; code in this method can safely manipulate
    * Swing components. It's a good idea to do all UI work in this
    * method.
    */
   void handleOnEventThread(Client c, AbstractPacket pkt)
   {}

}