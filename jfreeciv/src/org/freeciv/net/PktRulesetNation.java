package org.freeciv.net;
public class PktRulesetNation extends AbstractPacket /* should also be a help item*/
{
  public int id;
  public String name;
  public String name_plural;
  public String graphic_str;
  public String graphic_alt;
  public int leader_count;
  public String[] leader_name;
  public boolean[] leader_sex;
  public int city_style;
  public PktRulesetNation() 
  {
    super();
  }
  public PktRulesetNation( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    id = in.readUnsignedByte();
    name = in.readZeroString();
    name_plural = in.readZeroString();
    graphic_str = in.readZeroString();
    graphic_alt = in.readZeroString();
    leader_count = in.readUnsignedByte();
    if( leader_name == null )
    {
      leader_name = new String[ leader_count ];
    }
    if( leader_sex == null )
    {
      leader_sex = new boolean[ leader_count ];
    }
    for( int i = 0;i < leader_count;i++ )
    {
      leader_name[ i ] = in.readZeroString();
      leader_sex[ i ] = in.readUnsignedByte() != 0;
    }
    city_style = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
