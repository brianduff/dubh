package org.freeciv.net;

public class PktJoinGameReply extends AbstractPacket
{
  public boolean youCanJoin;
  public String message;
  public String capabilities;
  public int conn_id;
  public PktJoinGameReply() 
  {
    super();
  }
  public PktJoinGameReply( InStream in ) 
  {
    super( in );
  }
  public void receive( InStream in )
  {
    youCanJoin = in.readInt() > 0;
    message = in.readZeroString();
    capabilities = in.readZeroString();

    // BUGBUG: There is a bug in the C code here ( watch the console messages)
    // To reproduce, simply connect with a blank username. 
    // 
    // Basically, the conn_id isn't sent if the connection is refused, even
    // if you have the conn_info capability.
    //
    // So we check youCanJoin before reading the conn_id.
    if( youCanJoin && 
      capabilities != null && capabilities.indexOf( "conn_info" ) != -1 )
    {
      conn_id = in.readInt();
    }
    else
    {
      conn_id = 0;
    }
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.setType( PACKET_JOIN_GAME_REPLY );
    out.writeInt( youCanJoin ? 1 : 0 );
    out.writeZeroString( message );
    out.writeZeroString( capabilities );
    if( capabilities.indexOf( "conn_info" ) != -1 )
    {
      out.writeInt( conn_id );
    }
    out.sendPacket();
  }
}
