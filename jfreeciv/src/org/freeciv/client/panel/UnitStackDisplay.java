package org.freeciv.client.panel;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.*;

import org.freeciv.client.Client;
import org.freeciv.client.Unit;

public class UnitStackDisplay extends JComponent
{
  int unitWidth;
  int unitHeight;
  Icon sampleIcon;
  int horizontalUnits;
  Client client;
  static final int GAP = 5;
  public UnitStackDisplay( Icon aSampleIcon, int hCount, Client c )
  {
    horizontalUnits = hCount;
    sampleIcon = aSampleIcon;
    setSizes();
    setOpaque( false );
    client = c;
  }
  public void setSizes()
  {
    if( unitWidth != sampleIcon.getIconWidth() || unitHeight != sampleIcon.getIconHeight() )
    {
      unitWidth = sampleIcon.getIconWidth();
      unitHeight = sampleIcon.getIconHeight();
      setMinimumSize( new Dimension( unitWidth * horizontalUnits + ( horizontalUnits - 1 ) * GAP, unitHeight * 3 + 2 * GAP ) );
      setPreferredSize( getMinimumSize() );
      setWantedSize();
    }
  }
  public void invalidate()
  {
    setSizes();
    super.invalidate();
  }
  ArrayList units = new ArrayList();
  public void removeAllUnits()
  {
    units.clear();
  }
  public void setWantedSize()
  {
    setSize( getWantedSize() );
  }
  public Dimension getWantedSize()
  {
    int count = Math.max( units.size(), 9 ) - 1;
    int rows = ( count / horizontalUnits ) + 1;
    return new Dimension( unitWidth * horizontalUnits + ( horizontalUnits - 1 ) * GAP, unitHeight * rows + ( rows - 1 ) * GAP );
  }
  public void addUnit( Unit u )
  {
    units.add( u );
    setWantedSize();
  }
  public void addUnitStack( Unit u )
  {
    /*
    for( ;u != null;u = u.nextInStack )
    {
      addUnit( u );
    }
    */
  }
  public Unit getUnitAt( int x, int y )
  {
    Insets ins = getInsets();
    x -= ins.left;
    y -= ins.top;
    int posX = 0;
    int posY = 0;
    while( x >= unitWidth )
    {
      x -= unitWidth;
      if( x < GAP )
      {
        return null;
      }
      posX++;
    }
    while( y >= unitHeight )
    {
      y -= unitHeight;
      if( y < GAP )
      {
        return null;
      }
      posY++;
    }
    int index = posY * horizontalUnits + posX;
    if( index >= units.size() )
    {
      return null;
    }
    return (Unit)units.get( index );
  }
  public Unit getUnitAt( Point p )
  {
    return getUnitAt( p.x, p.y );
  }
  public void paintComponent( Graphics g )
  {
    /*
    Insets ins = getInsets();
    ( (Graphics2D)g ).setComposite( client.getComponentAlpha() );
    g.setColor( client.getComponentColor() );
    Rectangle r = g.getClipBounds();
    g.fillRect( r.x, r.y, r.width, r.height );
    g.setPaintMode();
    int len = units.size();
    for( int i = 0;i < len;i++ )
    {
      Unit u = (Unit)units.get( i );
      u.reallyPaintIcon( this, g, ins.left + ( i % horizontalUnits ) * ( unitWidth + GAP ), ins.top + ( i / horizontalUnits ) * ( unitHeight + GAP ), false );
    }
    */
  }
}
