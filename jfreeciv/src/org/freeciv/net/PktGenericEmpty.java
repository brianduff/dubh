package org.freeciv.net;

import java.io.IOException;

/**
 * A generic "empty" packet.
 * These types of packet must have a type: correspondingly, the only 
 * constructor takes an integer type, which must be one of the PACKET_
 * constants.
 */
public class PktGenericEmpty extends AbstractPacket
{

  public PktGenericEmpty()
  {
    super();
  }

	public PktGenericEmpty(int type)
	{
	  super(type);
	}

	public void receive(InStream in)
	{
    //
	}

	public void send( OutStream out ) throws IOException
	{
		out.setType( getType() );
		out.sendPacket();
	}


}
