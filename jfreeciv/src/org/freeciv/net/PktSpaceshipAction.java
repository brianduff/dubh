package org.freeciv.net;

public class PktSpaceshipAction  extends AbstractPacket
  implements org.freeciv.client.Constants
{

  public int action;
  public int num;

	public PktSpaceshipAction()
	{
	  super();
	}
	public PktSpaceshipAction(InStream in) {
	  super(in);
	}
	
	public void receive(InStream in)
	{
    action = in.readUnsignedByte();
    num = in.readUnsignedByte();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	}
}
