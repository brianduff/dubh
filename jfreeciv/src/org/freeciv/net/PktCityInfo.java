package org.freeciv.net;

import org.freeciv.client.Constants;
import java.io.IOException;


public class PktCityInfo extends AbstractPacket {
// BD Checked against 1.9.0.

	public int id;
	public int owner;
	public int x, y;
	public String name;

	public int size;
	public int ppl_happy, ppl_content, ppl_unhappy;
	public int ppl_elvis, ppl_scientist, ppl_taxman;

	public int food_prod, food_surplus;
	public int shield_prod, shield_surplus;
	public int trade_prod, corruption;
	public int[] trade = new int[4];
	public int[] trade_value = new int[4];
	public int luxury_total, tax_total, science_total;

	public WorkList worklist;
	/* the physics */
	public int food_stock;
	public int shield_stock;
	public int pollution;

	public boolean is_building_unit;
	public int currently_building;

	public boolean[] improvements;
	public char[] city_map = new char[26];

	public boolean did_buy;
	public boolean did_sell;
	public boolean was_happy;
	public boolean airlift;
	public boolean diplomat_investigate;
	public int city_options;

	public PktCityInfo() {
	  super();
	}

	public PktCityInfo(InStream in)
	{
		super(in);
	}

	public void send(OutStream os) throws IOException
	{
	  // TODO
	}
	
	public void receive(InStream in)
	{
		id = in.readShort();
		owner = in.readByte();
		x = in.readUnsignedByte();
		y = in.readUnsignedByte();
		name = in.readZeroString();

		size = in.readByte();
		ppl_happy = in.readByte();
		ppl_content = in.readByte();
		ppl_unhappy = in.readByte();
		ppl_elvis = in.readByte();
		ppl_scientist = in.readByte();
		ppl_taxman= in.readByte();

		food_prod = in.readByte();
		food_surplus = in.readByte();
		if ( food_surplus > 127 ) //??
			food_surplus -=256;
		shield_prod = in.readShort();
		shield_surplus = in.readShort();
		if ( shield_surplus > 32767 ) //??
			shield_surplus-=65536;
		trade_prod = in.readShort();
		corruption = in.readShort();

		luxury_total = in.readShort();
		tax_total = in.readShort();
		science_total = in.readShort();
		
		food_stock = in.readShort();
		shield_stock = in.readShort();
		pollution = in.readShort();
		currently_building = in.readByte();

		getWorkList(in, worklist);
		
		int data = in.readByte();

		is_building_unit = ((data&1) !=0);
		did_buy = (((data>>=1)&1) !=0);
		did_sell = (((data>>=1)&1) !=0);
		was_happy = (((data>>=1)&1) !=0);
		airlift = (((data>>=1)&1) !=0);
		diplomat_investigate = (((data>>=1)&1) !=0);

		
		city_map = in.readCityMap();

		improvements = in.readBitString();
		city_options = in.readByte();

		for(data=0;data<4;data++)
		{
			if ( !in.hasMoreData () )
				break;
			trade[data]= in.readShort();
			trade_value[data] = in.readByte();
		}

		// it is not needed first time, but same object can be refilled
		for(;data<4;data++)
		{
			trade_value[data]=trade[data]=0;
		}
	}

}
