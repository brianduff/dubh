package org.freeciv.client;

import org.freeciv.tile.FlashingIcon;
import java.awt.Component;
import java.awt.Graphics;

public class CornerOverlay implements FlashingIcon  {

	boolean visible = true;
	FlashingIcon cornerIcon;
	FlashingIcon n,e,s,w;

	public CornerOverlay(Client c, int corners, boolean nriver,
								boolean eriver, boolean sriver, boolean wriver) {

		//if ( corners > 0 )
		//	cornerIcon = c.getTerrain(c.BORDER_TILES, corners-1);
		// else
		//	cornerIcon = c.getEmptyIcon();
		//if ( nriver )
		//	n = c.getRiverDelta(0);
		//if ( eriver )
		//	e = c.getRiverDelta(3);
		//if (sriver )
		//	s = c.getRiverDelta(2);
		//if (wriver )
		//	w = c.getRiverDelta(1);
	}

	public void setVisible(boolean aVisible) {
		visible = aVisible;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if ( visible )
		{
			cornerIcon.paintIcon(c,g,x,y);
			if ( n != null)
				n.paintIcon(c,g,x,y);
			if ( e != null)
				e.paintIcon(c,g,x,y);
			if ( w != null)
				w.paintIcon(c,g,x,y);
			if ( s != null)
				s.paintIcon(c,g,x,y);
		}
	}

	public int getIconWidth() {
		return cornerIcon.getIconWidth();
	}

	public int getIconHeight() {
		return cornerIcon.getIconHeight();
	}

	public String toString()
	{
		return "corn=" + cornerIcon +" " + n + e + s + w;
	}
}
