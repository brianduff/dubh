package org.gjt.abies;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;


public class TriangleSlider extends JComponent implements MouseListener, MouseMotionListener, Serializable
{
	// different units !!!!!
	// to get centerX units, need to multiply centerY * sq3_2
	float centerX = 0.5f;
	float centerY = 2f/3f;

	static final float sq3_2 = (float)Math.sqrt(3f)/2;

	Color colorA,colorB,colorC;
	float aVal, bVal,cVal;
	String aLabel ="A";
	String bLabel ="B";
	String cLabel ="C";
	transient int top,bottom,left,right;

	public TriangleSlider()
	{
		addMouseListener(this);
		addMouseMotionListener(this);
		setFont( new Font("sansserif",Font.PLAIN,12));
	}

	public void setColors( Color baseColor )
	{
		colorA = baseColor;
		colorB = baseColor.brighter();
		colorC = baseColor.darker();
	}

	public void setColors( Color aColorA, Color aColorB, Color aColorC )
	{
		colorA = aColorA;
		colorB = aColorB;
		colorC = aColorC;
		repaint();
	}

	public void setValues( float a, float b, float c)
	{
		float sum = a + b + c;
		aVal = a/sum;
		bVal = b/sum;
		cVal = c/sum;
		updateCenter();
		fireStateChanged();
	}

	public float getValueA()
	{
		return aVal;
	}

	public float getValueB()
	{
		return bVal;
	}

	public float getValueC()
	{
		return cVal;
	}

	public void setLabels(String aLab, String bLab, String cLab )
	{
		aLabel = aLab;
		bLabel = bLab;
		cLabel = cLab;
		repaint();
	}

	public void setLabelA( String aLab )
	{
		aLabel = aLab;
		repaint();
	}

	public void setLabelB(String bLab )
	{
		bLabel = bLab;
		repaint();
	}

	public void setLabelC(String cLab )
	{
		cLabel = cLab;
		repaint();
	}


	final static Polygon boundingPolygon = new Polygon(
		new int[] {500,1000,0},
		new int[] {0,1000,1000},
		3);

	private void moveCenter( int x, int y)
	{
		Insets ins = getInsets();
		Dimension size = getSize();
		float ncenterX = (float)(x - left) / (float)(right-left);
		float ncenterY = (float)(y - top) / (float)(bottom - top);
		if ( boundingPolygon.contains(ncenterX*1000,ncenterY*1000) )
		{
			centerX = ncenterX;
			centerY = ncenterY;
		}
		else
		{
			// find point on edge
		}
		updateValues();
		repaint();
		fireStateChanged();
	}

	private void updateCenter()
	{
	}

	private void updateValues()
	{
	}


	int[] polydatax = new int[3];
	int[] polydatay = new int[3];


	protected void paintComponent( Graphics g)
	{
		Insets ins = getInsets();
		Dimension size = getSize();
		FontMetrics fm = getFontMetrics(getFont());
		int strHeight = fm.getHeight();
		int strDescent = fm.getDescent();

		left = ins.left;
		right = size.width - left - ins.right;
		int mediumx = left + (right-left)/2;
		top = ins.top;
		g.drawString(aLabel,mediumx - fm.stringWidth(aLabel)/2,top+strHeight);
		top += strHeight + strDescent;

		bottom = size.height - top - ins.bottom;
		g.drawString(bLabel,0,bottom - strDescent );
		g.drawString(cLabel,right - fm.stringWidth(cLabel),bottom - strDescent);
		bottom -=  strHeight + strDescent;

		int icenterX = left + (int)((right-left)*centerX);
		int icenterY = top + (int)((bottom-top)*centerY);
		polydatax[0] = left; polydatay[0] = bottom;
		polydatax[1] = mediumx; polydatay[1] = top;
		polydatax[2] = icenterX; polydatay[2] = icenterY;
		g.setColor(colorB);
		g.fillPolygon(polydatax, polydatay, 3);
		polydatax[1] = right; polydatay[1] = bottom;
		g.setColor(colorA);
		g.fillPolygon(polydatax, polydatay, 3);
		polydatax[0] = mediumx; polydatay[0] = top;
		g.setColor(colorC);
		g.fillPolygon(polydatax, polydatay, 3);
	}



	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		moveCenter(e.getX(), e.getY() );	
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}


	public void mouseDragged(MouseEvent e)
	{
		moveCenter(e.getX(), e.getY() );
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	protected transient ChangeEvent changeEvent;

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}


	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	protected void fireStateChanged()
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==ChangeListener.class)
			{
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
			}
		}
	}


	public static void main(String argv[] )
	{
		JFrame jf = new JFrame();
		TriangleSlider ts = new TriangleSlider();
		ts.setMinimumSize(new Dimension(200,200));
		ts.setPreferredSize(new Dimension(200,200));
//		ts.setColors(Color.red, Color.blue, Color.green);
		ts.setColors(new Color(212,100,150));
		jf.getContentPane().add(ts);
		jf.pack();
		jf.show();

	}


}