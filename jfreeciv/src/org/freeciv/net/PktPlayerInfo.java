package org.freeciv.net;

import java.io.IOException;

/**
 * Packet representing information about a player.
 *
 * Verified 1.12.cvs
 * @author Brian.Duff@dubh.org
 */
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
  public int reputation;
  public PlayerDiplomacyState[] diplstates = 
      new PlayerDiplomacyState[ MAX_NUM_PLAYERS + MAX_NUM_BARBARIANS ];
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
	public int revolution;
	public boolean ai;
	public boolean is_barbarian;
	public String capability;
	public WorkList[] worklists = new WorkList[MAX_NUM_WORKLISTS];
  public int gives_shared_vision;

	public PktPlayerInfo()
	{
	  super();
	}
	
	public PktPlayerInfo(InStream in) {
    super(in);
  }

  public void send(OutStream out) throws IOException
  {
    out.writeUnsignedByte(playerno);
    out.writeZeroString(name);

    out.writeUnsignedByte(is_male?1:0);
    out.writeUnsignedByte(government);
    out.writeInt(embassy);
    out.writeUnsignedByte(city_style);
    out.writeUnsignedByte(nation);
    out.writeUnsignedByte(turn_done?1:0);
    out.writeShort(nturns_idle);
    out.writeUnsignedByte(is_alive?1:0);

    out.writeInt(reputation);

    for (int i=0; i < MAX_NUM_PLAYERS + MAX_NUM_BARBARIANS; i++)
    {
      out.writeInt(diplstates[i].getType());
      out.writeInt(diplstates[i].getTurnsLeft());
      out.writeInt(diplstates[i].getReasonToCancel());
    }

    out.writeInt(gold);
    out.writeUnsignedByte(tax);
    out.writeUnsignedByte(science);
    out.writeUnsignedByte(luxury);

    out.writeInt(researched);
    out.writeInt(researchpoints);
    out.writeUnsignedByte(researching);

    out.writeBitString(inventions);
    out.writeShort(future_tech);

    out.writeUnsignedByte(is_connected?1:0);

    out.writeUnsignedByte(revolution);
    out.writeUnsignedByte(tech_goal);
    out.writeUnsignedByte(ai?1:0);
    out.writeUnsignedByte(is_barbarian?1:0);

    for (int i=0; i < MAX_NUM_WORKLISTS; i++)
    {
      putWorkList(out, worklists[i]);
    }

    out.writeInt(gives_shared_vision);

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
		turn_done = in.readUnsignedByte() != 0;
		nturns_idle = in.readShort();
		is_alive = in.readUnsignedByte() !=0;

    reputation = in.readInt();

    for (int i=0; i < MAX_NUM_PLAYERS + MAX_NUM_BARBARIANS; i++)
    {
      if (diplstates[i] == null)
      {
        diplstates[i] = new PlayerDiplomacyState();
      }
      diplstates[i].setType(in.readInt());
      diplstates[i].setTurnsLeft(in.readInt());
      diplstates[i].setReasonToCancel(in.readInt());
    }
		
		gold = in.readInt();
		tax = in.readUnsignedByte();
		science = in.readUnsignedByte();
		luxury = in.readUnsignedByte();
		
		researched = in.readInt();
		researchpoints = in.readInt();
		researching = in.readUnsignedByte();
    
		inventions = in.readBitString();
		future_tech = in.readShort();
		
		is_connected = in.readUnsignedByte() != 0;
    
		revolution = in.readUnsignedByte();
		tech_goal = in.readUnsignedByte();
		ai = in.readUnsignedByte() != 0;
		is_barbarian = in.readUnsignedByte() > 0;
		capability = in.readZeroString();
		
		for (int i=0; i< MAX_NUM_WORKLISTS; i++)
		{
		  worklists[i] = getWorkList(in, worklists[i]);
		}

    gives_shared_vision = in.readInt();
	}
}















