package org.freeciv.client.handler;

import org.freeciv.common.ErrorHandler;
import org.freeciv.net.Packet;
import org.freeciv.client.Client;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 * This handler class is useful for handlers that need to do things
 * on the UI thread.
 */
abstract class AbstractHandler implements ClientPacketHandler
{
  public final void handle( final Client c, final Packet pkt )
  {
    // Want to wait for this to avoid processing more packets until finished.
    try
    {
      SwingUtilities.invokeAndWait( new Runnable() 
      {
        public void run()
        {
          handleOnEventThread( c, pkt );
        }
      } );
    }
    catch (InvocationTargetException ite)
    {
      ErrorHandler.getHandler().internalError( ite );
    }
    catch (InterruptedException interrupt )
    {
      ErrorHandler.getHandler().internalError( interrupt );
    }
    handleOnCurrentThread( c, pkt );
  }
  /**
   * The implementation of this method will always be executed
   * on the current thread. This is usually the thread that listens
   * for incoming packets from the server.
   */
  void handleOnCurrentThread( Client c, Packet pkt )
  {
    
  }
  /**
   * The implementation of this method will always be executed on
   * the AWT event thread; code in this method can safely manipulate
   * Swing components. It's a good idea to do all UI work in this
   * method.
   */
  void handleOnEventThread( Client c, Packet pkt )
  {
    
  }
}
