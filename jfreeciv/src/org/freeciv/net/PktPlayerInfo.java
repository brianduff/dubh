package org.freeciv.net;

public class PktPlayerInfo extends AbstractPacket
{
	public int playerno;
	public String name;
	public boolean is_male;
	public int government;
	public int embassy;
	public int city_style;
	public int nation;
	public boolean turn_done;
	public int nturns_idle;
	public boolean is_alive;
	public int gold;
	public int tax;
	public int science;
	public int luxury;
	public int researched; //??
	public int researchpoints;
	public int researching; //ptr?
	public int future_tech;
	public int tech_goal; //ptr??
	public boolean[] inventions; // = new boolean[A_LAST+1];
	public boolean is_connected;
	public String addr;
	public int revolution;
	public boolean ai;
	public boolean is_barbarian;
	public String capability;
	public WorkList[] worklists = new WorkList[MAX_NUM_WORKLISTS];

	public PktPlayerInfo()
	{
	  super();
	}
	
	public PktPlayerInfo(InStream in) {
	  super(in);
  }

  public void send(OutStream out) throws java.io.IOException
  {
    // NYI
  }

  public void receive(InStream in)
  {
		playerno = in.readUnsignedByte();
		name = in.readZeroString();
		is_male = in.readUnsignedByte()!=0;
		government = in.readUnsignedByte();
		embassy = in.readInt();
		city_style = in.readUnsignedByte();
		nation = in.readUnsignedByte();
		turn_done = in.readByte() != 0;
		nturns_idle = in.readShort();
		is_alive = in.readByte() !=0;
		
		gold = in.readInt();
		tax = in.readByte();
		science = in.readByte();
		luxury = in.readByte();
		
		researched = in.readInt();
		researchpoints = in.readInt();
		researching = in.readByte();
		inventions = in.readBitString();
		future_tech = in.readShort();
		
		is_connected = in.readByte() != 0;
		addr = in.readZeroString();
		revolution = in.readByte();
		tech_goal = in.readByte();
		ai = in.readByte() != 0;
		is_barbarian = in.readByte() > 0;
		capability = in.readZeroString();
		
		for (int i=0; i< MAX_NUM_WORKLISTS; i++)
		{
		  worklists[i] = getWorkList(in, worklists[i]);
		}
	}
}















