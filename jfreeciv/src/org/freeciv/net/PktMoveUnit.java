package org.freeciv.net;
public class PktMoveUnit extends AbstractPacket
{
  public int unitId;
  public int nx;
  public int ny;

  public PktMoveUnit()
  {
    super( );
  }
  public PktMoveUnit( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( PACKET_MOVE_UNIT );
    out.writeUnsignedByte( nx );
    out.writeUnsignedByte( ny );
    out.writeShort( (short)unitId );
    out.sendPacket();
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    nx = in.readUnsignedByte();
    ny = in.readUnsignedByte();
    unitId = in.readShort();
  }
}
