

package org.freeciv.net;

/**
 * Nation allocation packet
 *
 * @author Brian Duff
 */
public class PktAllocNation extends AbstractPacket implements org.freeciv.client.Constants {

	public int nation_no;
	public String name;
	public boolean is_male;
	public int city_style;


	public PktAllocNation()
	{
	  super();
	}
	
	public PktAllocNation(InStream in)
	{
	  super(in);
	}

	public void send(OutStream out) throws java.io.IOException
	{
		out.setType( PACKET_ALLOC_NATION );
		out.writeInt(nation_no);
		out.writeZeroString(name);
		out.writeUnsignedByte(is_male?1:0);
		out.writeUnsignedByte(city_style);
		out.sendPacket();
	}
	
	public void receive(InStream in)
	{
	  nation_no = in.readInt();
	  name = in.readZeroString();
	  is_male = (in.readUnsignedByte()>0)?true:false;
	  city_style = in.readUnsignedByte();
	}
}
