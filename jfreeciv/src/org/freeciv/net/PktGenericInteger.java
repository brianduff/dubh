package org.freeciv.net;

public class PktGenericInteger extends AbstractPacket
{

	public int value;

	public PktGenericInteger()
	{
	  super();
	}

	public PktGenericInteger(InStream in) {
	  super(in);
	}
	public void receive(InStream in)
	{
		value = in.readInt();
	}

	private int pktType;

	public void setType(int t)
	{
		pktType = t;
	}

	public void send( OutStream out ) throws java.io.IOException
	{
		if ( pktType == 0 )
			throw new RuntimeException("Type not set");
		out.setType( pktType );
		out.writeInt(value);
		out.sendPacket();
	}


}
