package org.freeciv.client;
import java.awt.Component;
import java.awt.Graphics;
public class UnknownTerrain extends Terrain
{
  public UnknownTerrain( int anId ) 
  {
    id = anId;
    variation = 0; // Hmm.
  }
  public void paintIcon( Component c, Graphics g, int x, int y )
  {
    
  }
  public boolean isKnown()
  {
    return false;
  }
}
