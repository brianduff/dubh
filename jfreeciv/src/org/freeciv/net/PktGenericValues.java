package org.freeciv.net;

public class PktGenericValues extends AbstractPacket
{

	public int value1;
	public int value2;
	public int id;

	public PktGenericValues()
	{
	  super();
	}

	public PktGenericValues(InStream in) {
	  super(in);
  }

  public void receive(InStream in)
  {
	  id = in.readShort();
		value1 = in.readInt();
		value2 = in.readInt();
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
		out.writeShort(id);
		out.writeInt(value1);
		out.writeInt(value2);
		out.sendPacket();
	}


}
