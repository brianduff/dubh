package org.freeciv.net;
public class PktMapInfo extends AbstractPacket
{
  // Checked against 1.12.cvs
  public int xsize;
  public int ysize;
  public boolean isEarth;
  public PktMapInfo() 
  {
    super();
  }
  public PktMapInfo( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    xsize = in.readUnsignedByte();
    ysize = in.readUnsignedByte();
    isEarth = in.readUnsignedByte() != 0;
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( xsize );
    out.writeUnsignedByte( ysize );
    out.writeUnsignedByte( isEarth ? 1 : 0 );
    out.sendPacket();
  }
}
