package org.freeciv.net;

public class PktSpaceshipInfo  extends AbstractPacket
  implements org.freeciv.client.Constants
{

	int player_num;
	int sship_state;
	int structurals;
	int components;
	int modules;
	boolean[] structure = new boolean[NUM_SS_STRUCTURALS+1];
	int fuel;
	int propulsion;
	int habitation;
	int life_support;
	int solar_panels;
	int launch_year;
	int population;
	int mass;
	float support_rate;
	float energy_rate;
	float success_rate;
	float travel_time;

	public PktSpaceshipInfo()
	{
	  super();
	}
	public PktSpaceshipInfo(InStream in) {
	  super(in);
	}
	
	public void receive(InStream in)
	{
	  player_num = in.readUnsignedByte();
	  sship_state = in.readUnsignedByte();
	  structurals = in.readUnsignedByte();
	  components = in.readUnsignedByte();
	  modules = in.readUnsignedByte();
	  fuel = in.readUnsignedByte();
	  propulsion = in.readUnsignedByte();
	  habitation = in.readUnsignedByte();
	  life_support = in.readUnsignedByte();
	  solar_panels = in.readUnsignedByte();
	  launch_year = in.readShort();
	  if (launch_year > 32767) launch_year -=65536;
	
	  population = in.readUnsignedByte() * 1000;
	
	  mass = in.readInt();
	  support_rate = in.readInt() * 0.0001F;
	  energy_rate = in.readInt() * 0.0001F;
	  success_rate = in.readInt() * 0.0001F;
	  travel_time = in.readInt() * 0.0001F;
	
	  structure = in.readBitString();
	}
	
	public void send(OutStream out) throws java.io.IOException
	{
	}
}
