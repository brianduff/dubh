package org.freeciv.net;

import org.freeciv.client.Constants;


public class PktReqJoinGame extends AbstractPacket
{
// BD: Verified with 1.12.cvs
	public String short_name;
	public int majorVer;
	public int minorVer;
	public int patchVer;
	public String capabilities;
	public String name;
	public String version_label;

	public PktReqJoinGame() {
	  super();
	}
	
	public PktReqJoinGame(InStream in)
	{
	  super(in);
	}

	public void send( OutStream out ) throws java.io.IOException
	{
		out.setType(PACKET_REQUEST_JOIN_GAME);
		out.writeZeroString(short_name);
		out.writeInt(majorVer);
		out.writeInt(minorVer);
		out.writeInt(patchVer);
		out.writeZeroString(capabilities);
		out.writeZeroString(name);
		out.writeZeroString(version_label);
		out.sendPacket();
	}
	
	public void receive(InStream in)
	{
	
	}
}
