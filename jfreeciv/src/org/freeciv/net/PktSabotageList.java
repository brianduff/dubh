package org.freeciv.net;
import java.io.IOException;
public class PktSabotageList extends AbstractPacket
{
  public int diplomat_id;
  public int city_id;
  public boolean[] improvements;
  public PktSabotageList() 
  {
    super();
  }
  public PktSabotageList( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    diplomat_id = in.readShort();
    city_id = in.readShort();
    improvements = in.readBitString();
  }
  public void send( OutStream out )
               throws IOException
  {
    
  }
}
