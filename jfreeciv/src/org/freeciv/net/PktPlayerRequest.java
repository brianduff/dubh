package org.freeciv.net;

public class PktPlayerRequest extends AbstractPacket
{

	public int tax, luxury, science;              /* rates */
	public int government;                        /* government */
	public int tech;                              /* research */
  public WorkList worklist;
  public int wl_idx;


	public PktPlayerRequest()
	{
	  super();
	}
	
	public PktPlayerRequest(InStream in)
	{
	  super(in);
	}


	private int pktType;

	public void setType( int aType )
	{
		pktType = aType;
	}

	public void send( OutStream out ) throws java.io.IOException
	{
		out.setType( pktType );
		out.writeUnsignedByte(tax);
		out.writeUnsignedByte(luxury);
		out.writeUnsignedByte(science);
		out.writeUnsignedByte(government);
		out.writeUnsignedByte(tech);
		putWorkList(out, worklist);
		out.writeShort(wl_idx);
		out.sendPacket();
	}
	
	public void receive(InStream in)
	{
	  pktType = in.getInputPacketType();
	
	  tax = in.readUnsignedByte();
	  luxury = in.readUnsignedByte();
	  science = in.readUnsignedByte();
	  government = in.readUnsignedByte();
	  tech = in.readUnsignedByte();
	  worklist = getWorkList(in, worklist);
	  wl_idx = in.readShort();
	}



} 