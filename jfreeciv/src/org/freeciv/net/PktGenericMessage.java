package org.freeciv.net;
import org.freeciv.client.Constants;
public class PktGenericMessage extends AbstractPacket
{
  public int x;
  public int y;
  public int event;
  public String message;
  int type;
  public PktGenericMessage() 
  {
    super();
  }
  public PktGenericMessage( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    event = in.readInt();
    message = in.readZeroString();
  }
  public void setType( int aType )
  {
    type = aType;
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    if( type == 0 )
    {
      throw new RuntimeException( "Type not set" );
    }
    out.setType( type );
    out.writeUnsignedByte( x );
    out.writeUnsignedByte( y );
    out.writeInt( event );
    out.writeZeroString( message );
    out.sendPacket();
  }
}
