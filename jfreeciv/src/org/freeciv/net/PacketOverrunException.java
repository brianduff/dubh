package org.freeciv.net;

/**
 * Exception thrown if the client attempts to read past the end of a packet
 * when using the network protocol
 *
 * @author Brian Duff
 */
public class PacketOverrunException extends NetworkProtocolException
{
  public PacketOverrunException( )
  {
    super( "Attempted to read past the end of the packet" );
  }
}