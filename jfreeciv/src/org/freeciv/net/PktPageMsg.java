package org.freeciv.net;
import org.freeciv.client.Constants;

/**
 * A packet sent in response to certain report requests
 */
public class PktPageMsg extends AbstractPacket
{
  public int x;
  public int y;
  public int event;
  public String caption;
  public String headline;
  public String lines;
  int type;
  
  public PktPageMsg() throws NetworkProtocolException
  {
    super();
  }
  public PktPageMsg( InStream in ) throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    event = in.readInt();
    String message = in.readZeroString();
    // parse message, skip the '\n's
    caption = message.substring( 0, message.indexOf( '\n' ) );
    headline = message.substring(
      caption.length() + 1,
      message.indexOf( '\n', caption.length() + 1 )
    );
    lines = message.substring( caption.length() + 1 + headline.length() + 1 );
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
    out.writeZeroString( caption + "\n" + headline + "\n" + lines );
    out.sendPacket();
  }
}
