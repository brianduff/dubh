package org.freeciv.net;
public class PktBeforeEndYear extends AbstractPacket
{
  public PktBeforeEndYear() 
  {
    super();
  }
  public PktBeforeEndYear( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    
  
  // Nothing in this packet at the moment.
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.sendPacket();
  }
}
