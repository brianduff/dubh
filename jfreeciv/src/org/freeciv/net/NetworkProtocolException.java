package org.freeciv.net;


/**
 * Exception class thrown when a protocol exception happens when receiving or
 * sending data from or to a freeciv server.
 *
 * @author Brian Duff
 */
public class NetworkProtocolException extends Exception
{
  public NetworkProtocolException( String message )
  {
    super( message );
  }
}