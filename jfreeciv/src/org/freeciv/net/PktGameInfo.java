package org.freeciv.net;

public class PktGameInfo extends AbstractPacket
{

	public int gold;
	public int civstyle;
	public int tech;
	public int techlevel;
	public int skill_level;
	public int timeout;
	public int end_year;
	public int year;
	public int min_players, max_players, nplayers;
	public int player_idx;
	public int globalWarming;
	public int heating;
	public int cityfactor;
	public int unhappysize;
	public int diplcost,freecost,conquercost;
	public int[] global_advances = new int[A_LAST];
	public int[] global_wonders = new int[B_LAST];
	public int foodbox;
	public int techpenalty;
	public int spacerace;


	public PktGameInfo()
	{
	  super();
	}

	public PktGameInfo(InStream in) {
	  super(in);
	}
	
	public void send(OutStream os) throws java.io.IOException
	{}
	
	public void receive(InStream in)
	{
		gold = in.readShort();
		tech = in.readInt();
		techlevel = in.readUnsignedByte();
		skill_level = in.readInt();
		timeout = in.readShort();
		end_year = in.readInt();
		year = in.readInt();
		min_players = in.readUnsignedByte();
		max_players = in.readUnsignedByte();
		nplayers = in.readUnsignedByte();
		player_idx = in.readUnsignedByte();
		globalWarming = in.readInt();
		heating = in.readInt();
		cityfactor = in.readUnsignedByte();
		diplcost = in.readUnsignedByte();
		freecost = in.readUnsignedByte();
		conquercost = in.readUnsignedByte();
		unhappysize  = in.readUnsignedByte();

		for ( int i =0; i < A_LAST; i++ )
			global_advances[i] = in.readUnsignedByte();

		for ( int i =0; i < B_LAST; i++ )
			global_wonders[i] = in.readShort();

		techpenalty = in.readUnsignedByte();
		foodbox = in.readUnsignedByte();
		civstyle = in.readUnsignedByte();
		spacerace = in.readUnsignedByte();


	}


}
