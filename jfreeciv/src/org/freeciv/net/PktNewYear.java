package org.freeciv.net;

public class PktNewYear extends AbstractPacket
{

	public int year;

	public PktNewYear()
	{
	  super();
	}
	public PktNewYear(InStream in) {
	  super(in);
	}
	
	public void receive(InStream in)
	{
		year = in.readInt();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	  out.writeInt(year);
	  out.sendPacket();
	
	}
}


