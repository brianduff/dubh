package org.freeciv.net;
public class PktGameInfo extends AbstractPacket
{
  public int gold;
  public int civstyle;
  public int tech;
  public int researchcost;
  public int skill_level;
  public int timeout;
  public int end_year;
  public int year;
  public int min_players, max_players, nplayers;
  public int player_idx;
  public int globalWarming;
  public int heating;
  public int nuclearwinter;
  public int cooling;
  public int cityfactor;
  public int unhappysize;
  public int diplcost, freecost, conquercost;
  public int[] global_advances = new int[ A_LAST ];
  public int[] global_wonders = new int[ B_LAST ];
  public int foodbox;
  public int techpenalty;
  public boolean spacerace;
  public int seconds_to_turndone;
  public PktGameInfo() 
  {
    
  }
  public PktGameInfo( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeShort( gold );
    out.writeInt( tech );
    out.writeUnsignedByte( researchcost );
    out.writeInt( skill_level );
    out.writeShort( timeout );
    out.writeInt( end_year );
    out.writeInt( year );
    out.writeUnsignedByte( min_players );
    out.writeUnsignedByte( max_players );
    out.writeUnsignedByte( nplayers );
    out.writeUnsignedByte( player_idx );
    out.writeInt( globalWarming );
    out.writeInt( heating );
    out.writeInt( nuclearwinter );
    out.writeInt( cooling );
    out.writeUnsignedByte( cityfactor );
    out.writeUnsignedByte( diplcost );
    out.writeUnsignedByte( freecost );
    out.writeUnsignedByte( conquercost );
    out.writeUnsignedByte( unhappysize );
    for( int i = 0;i < A_LAST;i++ )
    {
      out.writeUnsignedByte( global_advances[ i ] );
    }
    for( int i = 0;i < B_LAST;i++ )
    {
      out.writeUnsignedByte( global_wonders[ i ] );
    }
    out.writeUnsignedByte( techpenalty );
    out.writeUnsignedByte( foodbox );
    out.writeUnsignedByte( civstyle );
    out.writeUnsignedByte( spacerace ? 1 : 0 );
    out.writeInt( seconds_to_turndone );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    gold = in.readShort();
    tech = in.readInt();
    researchcost = in.readUnsignedByte();
    skill_level = in.readInt();
    timeout = in.readShort();
    end_year = in.readInt();
    year = in.readInt();
    min_players = in.readUnsignedByte();
    max_players = in.readUnsignedByte();
    nplayers = in.readUnsignedByte();
    player_idx = in.readUnsignedByte();
    globalWarming = in.readInt();
    heating = in.readInt();
    nuclearwinter = in.readInt();
    cooling = in.readInt();
    cityfactor = in.readUnsignedByte();
    diplcost = in.readUnsignedByte();
    freecost = in.readUnsignedByte();
    conquercost = in.readUnsignedByte();
    unhappysize = in.readUnsignedByte();
    for( int i = 0;i < A_LAST;i++ )
    {
      global_advances[ i ] = in.readUnsignedByte();
    }
    for( int i = 0;i < B_LAST;i++ )
    {
      global_wonders[ i ] = in.readShort();
    }
    techpenalty = in.readUnsignedByte();
    foodbox = in.readUnsignedByte();
    civstyle = in.readUnsignedByte();
    spacerace = (in.readUnsignedByte() != 0);
    seconds_to_turndone = in.readInt();
  }
}
