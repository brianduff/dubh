package org.freeciv.net;

import java.io.IOException;


public class PktCityInfo extends AbstractPacket 
{
// BD Checked against 1.12.cvs

	public int id;
	public int owner;
	public int x, y;
	public String name;

	public int size;
	public int[] ppl_happy = new int[5], ppl_content = new int[5], ppl_unhappy = new int[5];
	public int ppl_elvis, ppl_scientist, ppl_taxman;

	public int food_prod, food_surplus;
	public int shield_prod, shield_surplus;
	public int trade_prod, corruption;
	public int[] trade = new int[4];
	public int[] trade_value = new int[4];
	public int luxury_total, tax_total, science_total;

	public WorkList worklist;
	public int food_stock;
	public int shield_stock;
	public int pollution;

	public boolean is_building_unit;
	public int currently_building;

  public int turn_last_built;
  public int turn_changed_target;
  public int changed_from_id;
  public int before_change_shields;

  public int disbanded_shields;
  public int caravan_shields;

	public boolean[] improvements;
	public char[] city_map = new char[26];

	public boolean did_buy;
	public boolean did_sell;
	public boolean was_happy;
	public boolean airlift;
	public boolean diplomat_investigate;
  public boolean changed_from_is_unit;
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
		owner = in.readUnsignedByte();
		x = in.readUnsignedByte();
		y = in.readUnsignedByte();
		name = in.readZeroString();

		size = in.readUnsignedByte();
    for (int i=0; i < 5; i++)
    {
      ppl_happy[i] = in.readUnsignedByte();
      ppl_content[i] = in.readUnsignedByte();
      ppl_unhappy[i] = in.readUnsignedByte();
    }
		ppl_elvis = in.readUnsignedByte();
		ppl_scientist = in.readUnsignedByte();
		ppl_taxman= in.readUnsignedByte();

		food_prod = in.readUnsignedByte();
		food_surplus = in.readUnsignedByte();
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
		currently_building = in.readUnsignedByte();


    turn_last_built = in.readShort(); //!! SIGNED
    turn_changed_target = in.readShort(); // !! SIGNED
    changed_from_id = in.readUnsignedByte();
    before_change_shields = in.readShort();

    disbanded_shields = in.readShort();
    caravan_shields = in.readShort();

		getWorkList(in, worklist);
		
		int data = in.readUnsignedByte();

		is_building_unit = ((data&1) !=0);
		did_buy = (((data>>=1)&1) !=0);
		did_sell = (((data>>=1)&1) !=0);
		was_happy = (((data>>=1)&1) !=0);
		airlift = (((data>>=1)&1) !=0);
		diplomat_investigate = (((data>>=1)&1) !=0);
    changed_from_is_unit = (((data>>=1)&1) !=0);

		
		city_map = in.readCityMap();

		improvements = in.readBitString();
		city_options = in.readUnsignedByte();

		for(data=0;data<4;data++)
		{
			if ( !in.hasMoreData () )
				break;
			trade[data]= in.readShort();
			trade_value[data] = in.readUnsignedByte();
		}

		for(;data<4;data++)
		{
			trade_value[data]=trade[data]=0;
		}
	}

}
