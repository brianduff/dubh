package org.freeciv.net;

public class PktUnittypeInfo extends AbstractPacket
{
  public int type, action;
  public PktUnittypeInfo() 
  {
    super();
  }
  public PktUnittypeInfo( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    type = in.readUnsignedByte();
    action = in.readUnsignedByte();
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( type );
    out.writeUnsignedByte( action );
    out.sendPacket();
  }
}
