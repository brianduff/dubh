package org.freeciv.net;
public class PktRulesetCity extends AbstractPacket /* should also be a help item*/
{
  public int style_id;
  public int techreq;
  public int replaced_by;
  public String name;
  public String graphic;
  public String graphic_alt;
  public PktRulesetCity() 
  {
    super();
  }
  public PktRulesetCity( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    style_id = in.readUnsignedByte();
    techreq = in.readUnsignedByte();
    replaced_by = in.readShort();
    name = in.readZeroString();
    graphic = in.readZeroString();
    graphic_alt = in.readZeroString();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
