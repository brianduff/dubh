package org.freeciv.net;
public class PktSpaceshipAction extends AbstractPacket
{
  public int action;
  public int num;
  public PktSpaceshipAction() 
  {
    super();
  }
  public PktSpaceshipAction( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    action = in.readUnsignedByte();
    num = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
}
