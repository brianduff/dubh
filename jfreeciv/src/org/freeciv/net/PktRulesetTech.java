package org.freeciv.net;


public class PktRulesetTech extends AbstractPacket
{
  public int id;
  public int[] req = new int[ 2 ]; 
  public String name;
  public int flags;
  public String helpText;
  
  public PktRulesetTech() 
  {
    super();
  }
  
  public PktRulesetTech( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  
  public void receive( InStream in ) throws NetworkProtocolException
  {
    id = in.readUnsignedByte();
    req[ 0 ] = in.readUnsignedByte();
    req[ 1 ] = in.readUnsignedByte();
    flags = in.readInt();
    name = in.readZeroString();
    if( in.hasMoreData() )
    {
      helpText = in.readZeroString();
    }
  }
  
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
  
  public String toString()
  {
    return name;
  }

}
