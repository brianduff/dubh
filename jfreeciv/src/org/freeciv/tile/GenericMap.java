package org.freeciv.tile;

import java.awt.Color;
import java.awt.Point;
import javax.swing.*;


public abstract class GenericMap extends JComponent
{

	protected int upperLeftX;
	protected int upperLeftY;
	protected int totalWidth;
	protected int totalHeight;
	protected boolean hWrap;
	protected boolean vWrap;
	protected Color gridlineColor = Color.white;
	protected Color voidColor = Color.black;


	public GenericMap()
	{
	}
	
	public boolean isWrapHorizontal()
	{
	   return hWrap;
	}
	
	public boolean isWrapVertical()
	{
	   return vWrap;
	}

   
	public void setGridlineColor ( Color c )
	{
		if ( c == null )
		{
			if ( gridlineColor != null )
			{
				gridlineColor = null;
				repaint();
			}
		}
		else if ( !c.equals(gridlineColor) )
		{
			gridlineColor = c;
			repaint();
		}
	}

	public void setVoidColor(Color c)
	{
		voidColor = c;
		repaint();
	}

	public Color getGridlineColor()
	{
		return gridlineColor;
	}

	public Color getVoidColor()
	{
		return voidColor;
	}



	public void setUpperLeftX(int aX)
	{
		setUpperLeftXY(aX,upperLeftY);
	}

	public void setUpperLeftY(int aY)
	{
		setUpperLeftXY(upperLeftX,aY);
	}


	public void setUpperLeftXY( int aX, int aY )
	{
		if ( hWrap )
		{
			while ( aX < 0 )
				aX += totalWidth;
			while ( aX >= totalWidth )
				aX -= totalWidth;
		}
		else
		{
			if ( aX < -(getWidth()/2) )
				aX = -getWidth()/2;
			else if ( aX > totalWidth  - (getWidth()/2) )
				aX = totalWidth - (getWidth()/2);
		}
		upperLeftX = aX;

		if ( vWrap )
		{
			while (aY < 0)
				aY += totalHeight;
			while ( aY >= totalHeight )
				aY -= totalHeight;
		}
		else
		{
			if ( aY < -(getHeight()/2) )
				aY = -getHeight()/2;
			else if ( aY > totalHeight  - (getHeight()/2) )
				aY = totalHeight - (getHeight()/2);
		}
		upperLeftY = aY;
		repaint();
	}

	public int getUpperLeftX()
	{
		return upperLeftX;
	}

	public int getUpperLeftY()
	{
		return upperLeftY;
	}

	public boolean isHorizontallyWrapped()
	{
		return hWrap;
	}

	public boolean isVerticallyWrapped()
	{
		return vWrap;
	}


}