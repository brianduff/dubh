package org.freeciv.client;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

import org.freeciv.client.Constants;
import org.freeciv.client.Terrain;
import org.freeciv.net.InStream;
import org.freeciv.net.PktUnitInfo;
import org.freeciv.net.PktRulesetUnit;
import org.freeciv.tile.FlashingIcon;

public class Unit extends PktUnitInfo implements FlashingIcon {

	Client client;
	Icon icon;
	Icon dimIcon;
	Icon flag;
	Icon hpIcon;
	int oldHp;
	PktRulesetUnit prototype;
	Unit nextInStack;
	boolean visible = true;

	public Unit(Client c) {
		super();
		client = c;
	}

	public Unit(Client c, InStream in)
	{
		super(); // not super(in) because client has to be initialized in fillData
		client = c;
		receive(in);
	}

	public void receive(InStream in)
	{
		super.receive(in);
		prototype = client.getRulesetManager().getRulesetUnit(type);
		// BD: Need to change the way images work in 1.9.0; uses a string identifier.
		//icon = client.getUnitIcon(0);
		//dimIcon = client.getUnitIconDim(0);
		//flag = client.getFlagForPlayer(owner);
	}

	public void setVisible(boolean aVisible)
	{
		visible = aVisible;
	}

	public void reallyPaintMainIcon(Component c, Graphics g, int x, int y)
	{
		if ( wantToBeSelected() )
			icon.paintIcon(c,g,x,y);
		else
			dimIcon.paintIcon(c,g,x,y);
	}

	public void reallyPaintIcon(Component c, Graphics g, int x, int y,
		boolean topOfStack)
	{
		flag.paintIcon(c,g,x,y);
		if ( topOfStack )
		{
			reallyPaintMainIcon(c,g,x+2,y+2);
		}
		else
		{
			reallyPaintMainIcon(c,g,x-4,y-4);
		}
		if ( oldHp != hp )
		{
		//	hpIcon = client.getHpIcon((11*(prototype.hp-hp))/prototype.hp);
			oldHp = hp;
		}
		hpIcon.paintIcon(c,g,x,y);
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		if ( nextInStack != null )
			nextInStack.reallyPaintMainIcon(c,g,x-4,y-4);

		if ( visible )
			reallyPaintIcon(c,g,x,y, nextInStack != null);
	}

	public int getIconWidth() {
		return icon.getIconWidth();
	}

	public int getIconHeight() {
		return icon.getIconHeight();
	}

	public String toString()
	{
		return prototype.name + id  + " at " + x + "," +y;
	}

	public boolean canMoveTo( Terrain t )
	{
		if ( t.getId() == Constants.T_OCEAN &&
			prototype.move_type == Constants.LAND_MOVING )
		{
			return false;
		}
		
		return true;
	}

	public boolean isSettler()
	{
		return prototype.isSettler();
	}

	public boolean isMilitary()
	{
		return prototype.isMilitary();
	}

	public boolean wantToBeSelected()
	{
		return
			(movesleft > 0 &&
			activity == Constants.ACTIVITY_IDLE &&
			!ai );
	}

	public boolean isGroundMoving()
	{
		return prototype.isGroundMoving();
	}


}
