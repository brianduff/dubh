package org.freeciv.net;

public class PktCityNameSuggestion  extends AbstractPacket
  implements org.freeciv.client.Constants
{

  public int id;
  public String name;

	public PktCityNameSuggestion()
	{
	  super();
	}
	public PktCityNameSuggestion(InStream in) {
	  super(in);
	}
	
	public void receive(InStream in)
	{
    id = in.readShort();
    name = in.readZeroString();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	}
}
