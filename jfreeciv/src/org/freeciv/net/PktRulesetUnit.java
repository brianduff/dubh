package org.freeciv.net;

import org.freeciv.client.Constants;
import org.freeciv.client.HelpItem;
import org.freeciv.client.HelpPanel;

public class PktRulesetUnit extends AbstractPacket implements  HelpItem
{
	public int id;			/* index for unit_types[] */
	public String name;
// gone in 1.9.0	public int graphics; //ptr?
	public int move_type;
	public int build_cost;
	public int attack_strength;
	public int defense_strength;
	public int move_rate;
	public int tech_requirement; //ptr?
	public int vision_range;
	public int transport_capacity;
	public int hp;
	public int firepower;
	public int obsoleted_by; //ptr?
	public int fuel;
	public int flags;
	public int roles;
	public int happy_cost;
	public int shield_cost;
	public int food_cost;
	public int gold_cost;
	public String graphic_str;
	public String graphic_alt;
	public int paratroopers_range;
	public int paratroopers_mr_req;
	public int paratroopers_mr_sub;

	public PktRulesetUnit(InStream in) {
	  super(in);
	}
	
	public PktRulesetUnit()
	{
	  super();
	}
	
	public void receive(InStream in)
	{
		id = in.readByte();
		move_type = in.readByte();
		build_cost = in.readShort();
		attack_strength = in.readByte();
		defense_strength =in.readByte();
		move_rate = in.readByte();
		tech_requirement = in.readByte();
		vision_range =in.readByte();
		transport_capacity = in.readByte();
		hp = in.readByte();
		firepower =in.readByte();
		obsoleted_by = in.readByte();

		if ( obsoleted_by>127 )
			obsoleted_by-=256;
		fuel = in.readByte();
		flags = in.readInt();
		roles = in.readInt();
		happy_cost = in.readByte();
		shield_cost = in.readByte();
		food_cost = in.readByte();
		gold_cost = in.readByte();
		name = in.readZeroString();		
		graphic_str = in.readZeroString();
		graphic_alt = in.readZeroString();
		
		if ((flags & (1L << Constants.F_PARATROOPERS)) != 0) // check this
		{
		  paratroopers_range = in.readShort();
		  paratroopers_mr_req = in.readByte();
		  paratroopers_mr_sub = in.readByte();
		}
		else
		{
		  paratroopers_range = paratroopers_mr_req = paratroopers_mr_sub = 0;	
		}
		
    if (in.hasMoreData())
    {
      helpText = in.readZeroString();
    }
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	}

    
	public String toString()
	{
		return name;

		/*"" + attack_strength + "/" + defense_strength + "/" +
			(move_rate/3) + " " +  name + "   " + build_cost;
		*/
	}

	public boolean isSettler()
	{
		return (flags & (1<<Constants.F_SETTLERS)) !=0;
	}

	public boolean isMilitary()
	{
		return (flags & (1<<Constants.F_NONMIL)) ==0;
	}

	public boolean isGroundMoving()
	{
		return move_type == Constants.LAND_MOVING;
	}


	String helpText;

	public void setHelpText(String txt)
	{
		helpText = txt;
	}

	public javax.swing.JComponent getRenderer(HelpPanel help)
	{
		return help.getUnitHelpPanel(this);
	}

	public String getHelpCategory()
	{
		return "UNITS";
	}

	public String getHelpName()
	{
		return name;
	}


}
