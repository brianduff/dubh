package org.freeciv.net;
public class PktNukeTile extends AbstractPacket
{
  public int x;
  public int y;
  public PktNukeTile() 
  {
    super();
  }
  public PktNukeTile( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( x );
    out.writeUnsignedByte( y );
    out.sendPacket();
  }
}
