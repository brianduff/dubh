package org.freeciv.client;

import org.freeciv.tile.FlashingIcon;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class RoadOverlay implements FlashingIcon
{

	boolean visible = true;
	Icon icon;
	int m_index;

	public RoadOverlay(Icon i, int idx)
	{
		icon = i;
		m_index = idx;
	}

	public boolean isRail()
	{
		return ((m_index & 32) > 0);
	}

	public void setVisible(boolean aVisible) {
		visible = aVisible;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		if ( visible )
		{
			icon.paintIcon(c,g,x,y);
		}
	}

	public int getIconWidth() {
		return icon.getIconWidth();
	}

	public int getIconHeight() {
		return icon.getIconHeight();
	}

	private String m_description;
	
	private String getDescription()
	{
	   if (m_description == null)
	   {
	      boolean diagonal = ((m_index & 64) > 0);
	      int variation = m_index & 15;	
	
	      StringBuffer b = new StringBuffer();
	      b.append(diagonal ? "Diagonal " : "Cardinal ");
	      b.append(isRail() ? "rail " : "road ");
	
	      b.append(" Var: ");
	      if ((variation & 8) > 0)
	      {
	         b.append("N ");
	      }
	      if ((variation & 4) > 0)
	      {
	         b.append("S ");
	      }
	      if ((variation & 2) > 0)
	      {
	         b.append("E ");
	      }
	      if ((variation & 1) > 0)
	      {
	         b.append("W");
	      }
	
	      m_description = b.toString();	
	   }
	
	   return m_description;
	}
	
	public String toString()
	{
      return getDescription();
	}

}


