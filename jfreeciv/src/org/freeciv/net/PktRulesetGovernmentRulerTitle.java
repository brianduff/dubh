package org.freeciv.net;

public class PktRulesetGovernmentRulerTitle  extends AbstractPacket  // should also be a help item
{
  public int gov;
  public int id;
  public int nation;
  public String male_title;
  public String female_title;

	public PktRulesetGovernmentRulerTitle()
	{
	  super();
	}
	
	public PktRulesetGovernmentRulerTitle(InStream in)
	{
	  super(in);
	}
	
	public void receive(InStream in)
	{
    gov = in.readUnsignedByte();
    id = in.readUnsignedByte();
    nation = in.readUnsignedByte();

    male_title = in.readZeroString();
    female_title = in.readZeroString();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	
	}
}