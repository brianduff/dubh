package org.freeciv.net;

import org.freeciv.common.CommonConstants;

public class PktSpaceshipInfo extends AbstractPacket implements CommonConstants
{
  public int player_num;
  public int sship_state;
  public int structurals;
  public int components;
  public int modules;
  public boolean[] structure = new boolean[ NUM_SS_STRUCTURALS + 1 ];
  public int fuel;
  public int propulsion;
  public int habitation;
  public int life_support;
  public int solar_panels;
  public int launch_year;
  public int population;
  public int mass;
  public float support_rate;
  public float energy_rate;
  public float success_rate;
  public float travel_time;
  
  public PktSpaceshipInfo() 
  {
    super();
  }
  public PktSpaceshipInfo( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
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
    if( launch_year > 32767 )
    {
      launch_year -= 65536;
    }
    population = in.readUnsignedByte() * 1000;
    mass = in.readInt();
    support_rate = in.readInt() * 0.0001F;
    energy_rate = in.readInt() * 0.0001F;
    success_rate = in.readInt() * 0.0001F;
    travel_time = in.readInt() * 0.0001F;
    structure = in.readBitString();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
