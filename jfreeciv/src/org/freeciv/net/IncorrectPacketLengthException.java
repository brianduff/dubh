package org.freeciv.net;

/**
 * Thrown if the packet length is wrong while receiving or sending a packet
 * to a freeciv server
 *
 * @author Brian Duff
 */
public class IncorrectPacketLengthException extends NetworkProtocolException
{
  public IncorrectPacketLengthException( String message )
  {
    super( message );
  }
}