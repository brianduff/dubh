package org.freeciv.net;

public class PktUnitInfo extends AbstractPacket
{
// Updated for 1.9.0
	public int id;
	public int owner;
	public int x;
	public int y;
	public boolean veteran;
	public int homecity;
	public int type;
	public int movesleft;
	public int hp;
	public int upkeep;
	public int upkeep_food;
	public int upkeep_gold;
	public int activity;
	public int activity_count;
	public int activity_target;
	public int unhappiness;
	public boolean ai;
	public boolean paradropped;
	public boolean connecting;
	public int fuel;
	public int goto_dest_x;
	public int goto_dest_y;

	public PktUnitInfo()
	{
	  super();
	}

	public PktUnitInfo(InStream in)
	{
	  super(in);
	}

	public void receive(InStream in)
	{
		id = in.readShort();
		int pack = in.readUnsignedByte();
		owner = pack&0x0f;
		veteran = ((pack&0x10) != 0);
		ai= ((pack&0x20) != 0);
		paradropped = ((pack&0x40) != 0);
		connecting = ((pack&0x80) != 0);
		x = in.readUnsignedByte();
		y = in.readUnsignedByte();
		homecity = in.readShort();
		type = in.readByte();
		movesleft = in.readByte();
		hp = in.readByte();
		upkeep = in.readByte();
		upkeep_food = in.readByte();
		upkeep_gold = in.readByte();
		unhappiness = in.readByte();
		activity = in.readByte();
		activity_count = in.readByte();
		goto_dest_x = in.readUnsignedByte();
		goto_dest_y = in.readUnsignedByte();
		activity_target = in.readShort();
		
		if (in.hasMoreData())
			fuel = in.readByte();
		else
			fuel = 0;
	}

	private int pktType;

	public void setPktType(int aType )
	{
		pktType = aType;
	}

	public void send( OutStream out ) throws java.io.IOException
	{
		int pack;
		out.setType( pktType );
		out.writeShort(id);
		out.writeByte((owner)|(veteran?0x10:0)|(ai?0x20:0)|(paradropped?0x40:0)|(connecting?0x80:0));
		out.writeByte(x);
		out.writeByte(y);
		out.writeShort(homecity);
		out.writeByte(type);
		out.writeByte(movesleft);
		out.writeByte(hp);
    out.writeByte(upkeep);
    out.writeByte(upkeep_food);
    out.writeByte(upkeep_gold);
    out.writeByte(unhappiness);
    out.writeByte(activity);
		out.writeByte(activity_count);
		out.writeByte(goto_dest_x);
		out.writeByte(goto_dest_y);
		out.writeShort(activity_target);
		out.writeByte(fuel);
		out.sendPacket();
	}

	public boolean equals(Object obj)
	{
		return ( obj != null &&
			obj instanceof PktUnitInfo &&
			((PktUnitInfo)obj).id == id );
	}



}
