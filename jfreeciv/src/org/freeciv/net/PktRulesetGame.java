package org.freeciv.net;
public class PktRulesetGame extends AbstractPacket
{
  public int min_city_center_food;
  public int min_city_center_shield;
  public int min_city_center_trade;
  public int min_dist_bw_cities;
  public int init_vis_radius_sq;
  public int hut_overflight;
  public int pillage_select;
  public int nuke_contamination;
  public int granary_food_ini;
  public int granary_food_inc;
  public PktRulesetGame() 
  {
    super();
  }
  public PktRulesetGame( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    min_city_center_food = in.readUnsignedByte();
    min_city_center_shield = in.readUnsignedByte();
    min_city_center_trade = in.readUnsignedByte();
    min_dist_bw_cities = in.readUnsignedByte();
    init_vis_radius_sq = in.readUnsignedByte();
    hut_overflight = in.readUnsignedByte();
    pillage_select = in.readUnsignedByte();
    nuke_contamination = in.readUnsignedByte();
    granary_food_ini = in.readUnsignedByte();
    granary_food_inc = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
