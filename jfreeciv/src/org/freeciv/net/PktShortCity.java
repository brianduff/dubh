package org.freeciv.net;

import java.io.IOException;

/**
 * ??
 * New in 1.12.cvs
 * @author Brian.Duff@dubh.org
 */
public class PktShortCity extends AbstractPacket
{
  public int id;
  public int owner;
  public int x;
  public int y;
  public String name;

  public int size;

  public boolean happy;
  public boolean capital;
  public boolean walls;
  
  public PktShortCity(InStream in)
	{
	  super(in);
	}

	public void receive(InStream in)
	{
    id = in.readShort();
    owner = in.readUnsignedByte();
    x = in.readUnsignedByte();
    y = in.readUnsignedByte();
    name = in.readZeroString();

    size = in.readUnsignedByte();

    int i = in.readUnsignedByte();

    happy = ((i & 1) != 0);
    capital = ((i & 2) != 0);
    walls = ((i & 4 ) != 0);
    
  }

	public void send(OutStream out) throws IOException
	{
    out.setType( PACKET_SHORT_CITY );

    out.writeShort(id);
    out.writeUnsignedByte(owner);
    out.writeUnsignedByte(x);
    out.writeUnsignedByte(y);
    out.writeZeroString(name);

    out.writeUnsignedByte(size);

    int i = (happy ? 1:0) | (capital ? 2:0) | (walls ? 4:0);
    out.writeUnsignedByte(i);
    
    out.sendPacket();
  }    
}