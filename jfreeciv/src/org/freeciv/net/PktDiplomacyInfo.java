package org.freeciv.net;

/**
 * Diplomacy information verified 1.12.cvs
 *
 * @author Brian Duff
 */
public class PktDiplomacyInfo extends AbstractPacket implements org.freeciv.client.Constants
{
  public int plrno0;
  public int plrno1;
  public int plrno_from;
  public int clause_type;
  public int value;
  public PktDiplomacyInfo() 
  {
    super();
  }
  public PktDiplomacyInfo( InStream in ) 
  {
    super( in );
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    out.writeInt( plrno0 );
    out.writeInt( plrno1 );
    out.writeInt( plrno_from );
    out.writeInt( clause_type );
    out.writeInt( value );
    out.sendPacket();
  }
  public void receive( InStream in )
  {
    plrno0 = in.readInt();
    plrno1 = in.readInt();
    plrno_from = in.readInt();
    clause_type = in.readInt();
    value = in.readInt();
  }
}
