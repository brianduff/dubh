package org.freeciv.net;
public class PktNewYear extends AbstractPacket
{
  public int year;
  public PktNewYear() 
  {
    super();
  }
  public PktNewYear( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    year = in.readInt();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeInt( year );
    out.sendPacket();
  }
}
