package org.freeciv.net;
import java.io.IOException;
// Eeek
public class PktGotoRoute extends AbstractPacket
{
  public PktGotoRoute() 
  {
    super();
  }
  public PktGotoRoute( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    
  }
  public void send( OutStream out )
               throws IOException
  {
    
  }
}
