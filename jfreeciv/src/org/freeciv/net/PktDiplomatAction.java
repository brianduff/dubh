package org.freeciv.net;


/**
 * Diplomat action
 *
 * @author Brian Duff
 */
public class PktDiplomatAction extends AbstractPacket
{
  public int action_type;
  public int diplomat_id;
  public int target_id;
  public int value;
  public PktDiplomatAction() 
  {
    super();
  }
  public PktDiplomatAction( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeUnsignedByte( action_type );
    out.writeShort( diplomat_id );
    out.writeShort( target_id );
    out.writeShort( value );
    out.sendPacket();
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    action_type = in.readUnsignedByte();
    diplomat_id = in.readShort();
    target_id = in.readShort();
    value = in.readShort();
  }
}
