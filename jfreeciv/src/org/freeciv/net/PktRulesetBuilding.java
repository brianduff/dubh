package org.freeciv.net;

import javax.swing.JComponent;
import org.freeciv.client.HelpItem;
import org.freeciv.client.HelpPanel;

public class PktRulesetBuilding extends AbstractPacket implements HelpItem {

	public int id;			/* index for improvement_types[] */
	public String name;
	public boolean is_wonder;
	public int tech_requirement; //ptr?
	public int build_cost;
	public int upkeep;
	public int obsolete_by; //ptr?
	public int variant;

	public PktRulesetBuilding()
	{
	  super();
	}
	public PktRulesetBuilding(InStream in)
	{
	  super(in);
	}
	
	public void receive(InStream in)
	{
		id = in.readByte();
		is_wonder = in.readByte() != 0;
		tech_requirement = in.readByte();
		build_cost = in.readShort();
		upkeep = in.readByte();
		obsolete_by = in.readByte();
		variant = in.readByte();
		name = in.readZeroString();
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
	}

	String helpText;

	public void setHelpText(String txt)
	{
		helpText = txt;
	}

	public JComponent getRenderer(HelpPanel help)
	{
		return help.getBuildingHelpPanel(this);
	}

	public String getHelpCategory()
	{
		if ( is_wonder )
			return "WONDERS";
		else
			return "IMPROVEMENTS";
	}

	public String getHelpName()
	{
		return name;
	}


}
