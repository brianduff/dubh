package org.freeciv.net;


import org.freeciv.client.HelpItem;
import org.freeciv.client.HelpPanel;


public class PktRulesetTech extends AbstractPacket implements  HelpItem {
// Updated for 1.9.0
	public int id;
	public int[] req = new int[2];		/* indices for advances[] */ //ptr?
	public String name;
	public int flags;

	public PktRulesetTech()
	{
	  super();
	}
	public PktRulesetTech(InStream in) {
	  super(in);
	}
	
	public void receive(InStream in)
	{
		id = in.readByte();
		req[0] = in.readByte();
		req[1] = in.readByte();
		flags = in.readInt();
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

	public javax.swing.JComponent getRenderer(HelpPanel help)
	{
		return help.getTechHelpPanel(this);
	}

	public String getHelpCategory()
	{
		return "TECHS";
	}

	public String getHelpName()
	{
		return name;
	}


}
