package org.freeciv.net;
import java.io.IOException;

/**
 * All packets implement this interface.
 */
public interface Packet extends PacketConstants
{
  /**
   * Implement this method in your subclass to send the contents of
   * the packet to the peer. You should implement this for all packets
   * so that this code can be used for either a client or a server.
   */
  public abstract void send( OutStream os )
                        throws IOException;
  
  /**
   * Implement this method in your subclass to recieve the contents
   * of the packet from the peer and initialize internal data. You should
   * implement this for all packets so that this code can be used for either
   * a client or a server.
   */
  public abstract void receive( InStream is );
  
  /**
   * Get the type of this packet.
   */
  public int getType();
  public void setType( int type );
}
