package org.freeciv.net;

public class PktJoinGameReply extends AbstractPacket
{
	public boolean youCanJoin;
	public String message;
	public String capabilities;

	public PktJoinGameReply()
	{
	  super();
	}
	public PktJoinGameReply(InStream in)
	{
	  super(in);
	}
	
	public void receive(InStream in)
	{
		youCanJoin = in.readInt() >0;
		message = in.readZeroString();
		capabilities = in.readZeroString();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	  out.writeInt(youCanJoin?1:0);
	  out.writeZeroString(message);
	  out.writeZeroString(capabilities);
	  out.sendPacket();
	}

}
