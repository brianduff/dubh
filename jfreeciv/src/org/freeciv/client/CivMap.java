package org.freeciv.client;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import java.util.ArrayList;
import java.util.List;

import org.freeciv.net.PktTileInfo;
import org.freeciv.net.PktUnitCombat;
import org.freeciv.tile.Coords;
import org.freeciv.tile.FlashingIcon;
import org.freeciv.tile.TileMap;

public class CivMap extends TileMap implements Constants, ComponentListener {

	Client client;
	Unit activeUnit;
	int lastActiveUnitId = -1;
	SmallMap smallMap = new SmallMap();

	boolean nuking;
	Object nukingWait = new Object();
	boolean showingCityRanges = true;
	boolean gotoMode = false;


	public CivMap(Client c, int aHorizontalTiles, int aVerticalTiles, int aSingleTileWidth, int aSingleTileHeight,
				boolean aHorizontalWrap, boolean aVerticalWrap) {

		super(aHorizontalTiles, aVerticalTiles, aSingleTileWidth, aSingleTileHeight,
				aHorizontalWrap, aVerticalWrap);
		//setDoubleBuffered(false);
		
		setFlashInterval(250);
		client = c;
		setGridlineColor(null);

		for ( int x=0; x < aHorizontalTiles; x++ )
		{
			for ( int y=0; y < aVerticalTiles; y++ )
			{
				ArrayList l = new ArrayList(MAX_LAYER);
				for ( int i =0; i < MAX_LAYER; i++ )
					l.add(null);
				tiles[x][y] = l;
			}
		}

		addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent e )
			{
				requestFocus();
				if ( (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0 )
				{
					if ( activeUnit != null )
					{
						deactivateUnit();
					}
					Coords p = toTileCoordinates(e.getX(), e.getY());
					int x = e.getX() + getUpperLeftX();
					int y = e.getY() + getUpperLeftY();
					setUpperLeftXY(x-(getWidth()/2),y-(getHeight()/2));
					if ( p != null )
					{
						List l = getTileList(p.x,p.y);
						if ( e.getClickCount() == 1 )
						{
							Unit u = (Unit)l.get(UNIT_LAYER);
							if ( u != null )
							{
								activateUnit(u);
							}
						}
					}
				}
				else if ( (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 )
				{
					Coords p = toTileCoordinates(e.getX(), e.getY());
					if ( p == null )
					{
						System.out.println("Outside map");
					}
					else
					{
						if ( activeUnit != null )
						{
							Unit u = activeUnit;
							boolean gm = gotoMode;
							deactivateUnit();
							if ( gm )
							{
								//client.commandGotoUnit(u,p.x,p.y);
							}
							else
							{
								//client.commandMoveUnit(u, p.x, p.y);
							}
						}
						else
						{
							City ct = (City)getTileList(p.x,p.y).get(CITY_LAYER);
							if ( ct != null )
							{
								ct.openCityDialog();
							}
						}

					}
				}

			}

		} );
/*
		addMouseMotionListener( new MouseMotionAdapter() {
			int lastX;
			int lastY;
			int cnt;
			Cursor defaultCursor = Cursor.getDefaultCursor();
			Cursor oddCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

			public void mouseMoved(MouseEvent evt)
			{
				Coords p = toTileCoordinates(evt.getX(), evt.getY());
				if ( p == null || ((lastX==p.x) && (lastY==p.y)) )
					return;
				lastX = p.x;
				lastY = p.y;
				if ( (p.x+p.y)%2  == 0)
				{
					System.out.println("Default" + cnt++);
					setCursor(defaultCursor);
				}
				else
				{
					System.out.println("Odd" + cnt++);
					setCursor(oddCursor);
				}
			}
		} );
*/
	}

	public int adjustX(int x)
	{
		while ( hWrap && x < 0 )
			x+=horizontalTiles;

		while ( hWrap && x >= horizontalTiles )
			x-= horizontalTiles;

		return x;
	}

	public int adjustY(int y)
	{
		while ( vWrap && y < 0 )
			y+= verticalTiles;
		while( vWrap && y >= verticalTiles )
			y-= verticalTiles;
		return y;
	}

	public void changeTileSize( int x, int y)
	{
		super.changeTileSize(x,y);
		smallMap.updateView();
	}



	public Terrain getTerrain( int x, int y )
	{
		return (Terrain) tiles[x][y].get(TERRAIN_LAYER);
	}

	public Unit getUnit(int x, int y)
	{
		return (Unit) tiles[x][y].get(UNIT_LAYER);
	}

	public City getCityAt(int x, int y)
	{
      return (City) tiles[x][y].get(CITY_LAYER);
	}


	public RoadOverlay getRoad(int x, int y)
	{
		return (RoadOverlay) tiles[x][y].get(ROAD_LAYER);
	}

	public Icon getIrrigation(int x, int y)
	{
		return (Icon)tiles[x][y].get(IRRIGATION_LAYER);
	}




	public FlashingIcon computeOverlayTerrain( int x, int y )
	{
	   return null;
      /*
		Terrain n,w,e,s,ne,nw,se,sw;
		n=w=e=s=ne=nw=se=sw=null;
		if ( y > 0 )
		{
			n = getTerrain(x,y-1);
			nw = (x > 0) ? getTerrain(x-1,y-1) :
     			(hWrap ? getTerrain(horizontalTiles-1,y-1):null);
			ne = (x< horizontalTiles-1 ) ? getTerrain(x+1,y-1) :
     			(hWrap ? getTerrain(0,y-1):null);
			if ( n == null || nw == null || ne == null )
				return null;
		}

		if ( y < verticalTiles-1 )
		{
			s = getTerrain(x,y+1);
			sw = (x > 0) ? getTerrain(x-1,y+1) :
				(hWrap ? getTerrain(horizontalTiles-1,y+1):null);
			se = (x< horizontalTiles-1 ) ? getTerrain(x+1,y+1) :
				(hWrap ? getTerrain(0,y+1):null);
			if ( s == null || sw == null || se == null )
				return null;
		}

		w = (x > 0) ? getTerrain(x-1,y) :
    		(hWrap ? getTerrain(horizontalTiles-1,y):null);
		e = (x< horizontalTiles-1 ) ? getTerrain(x+1,y) :
			(hWrap ? getTerrain(0,y):null);

		if ( w == null || e == null)
			return null;

		int cornerVariation = 0;

		if ( (ne != null) && (ne.getId() != T_OCEAN) && (n.getId() == T_OCEAN) && (e.getId() == T_OCEAN) )
			cornerVariation += 1;
		if ( (se != null) && (se.getId() != T_OCEAN)&& (s.getId() == T_OCEAN) && (e.getId() == T_OCEAN) )
			cornerVariation += 2;
		if ( (sw != null) && (sw.getId() != T_OCEAN) && (s.getId() == T_OCEAN) && (w.getId() == T_OCEAN))
			cornerVariation += 4;
		if ( (nw != null) && (nw.getId() != T_OCEAN)&& (n.getId() == T_OCEAN) && (w.getId() == T_OCEAN) )
			cornerVariation += 8;
		boolean nriver = ((n!= null) && (n.getId() == T_RIVER) );
		boolean eriver =  ((e!= null) && (e.getId() == T_RIVER) );
		boolean sriver = ((s!= null) && (s.getId() == T_RIVER) );
		boolean wriver = ((w!= null) && (w.getId() == T_RIVER) );
		if ( cornerVariation == 0 && !nriver && !eriver && !sriver && !wriver )
			return null;

		return  new CornerOverlay(client,
								cornerVariation,nriver, eriver,sriver,wriver
								);    */
	}






	public void recomputeVariation(int x, int y)
	{ /*
		if ( x < 0 || x > horizontalTiles-1 || y<0 || y > verticalTiles-1 )
			return;
		List l = tiles[x][y];
		Terrain current = (Terrain)l.get(TERRAIN_LAYER);
		if ( current == null || !current.isKnown() )
		{
			return;
		}
		int variation = whatVariation(x,y,current.getId());
		l.set(TERRAIN_LAYER,client.getTerrain(current.getId(), variation ));     */
	}

   /**
    * Set the terrain for one tile. This method is normally called
    * by the client packet handler that deals with incoming TileInfo
    * packets from the server.
    */
	public synchronized void setTerrain( PktTileInfo pkt, boolean update )
	{
      // TODO: Deprecate me.
      client.getTileSpec().setTerrain(pkt, update);
	}

	public void updateAllOverlays()
	{                            /*
		for ( int x = 0; x < horizontalTiles; x++ )
		{
			for ( int y = 0; y < verticalTiles; y++ )
			{
				recomputeVariation(x,y);
				computeAndSetOverlayTerrain(x,y);
			}
		}
		smallMap.updateAll();
		repaint();     */
	}


	public synchronized void addUnit(Unit unit)
	{                      /*
//		System.out.println("added " + unit);
		List l = tiles[unit.x][unit.y];
		unit.nextInStack = (Unit)l.get(UNIT_LAYER);
		l.set(UNIT_LAYER, unit);
		repaintOneTile(unit.x,unit.y);
		if ( lastActiveUnitId == unit.id && activeUnit == null &&
				unit.wantToBeSelected())
		{
			activateUnit(unit);
		}                        */
	}

	public synchronized void removeUnit(Unit unit)
	{      /*
//		System.out.println("removed " + unit);
		if ( unit == activeUnit )
			deactivateUnit();
		List l = tiles[unit.x][unit.y];
		if( l.get(UNIT_LAYER).equals(unit) )
		{
			l.set(UNIT_LAYER, unit.nextInStack);
			unit.nextInStack = null;
			repaintOneTile(unit.x,unit.y);
		}
		else
		{
			Unit stackU = (Unit)l.get(UNIT_LAYER);
			Unit stackPrev = null;
			while ( stackU != null )
			{
				if ( stackU.equals(unit) )
				{
					stackPrev.nextInStack = stackU.nextInStack;
					stackU.nextInStack = null;
					break;
				}
				stackPrev = stackU;
				stackU = stackU.nextInStack;
			}
		}

		if ( activeUnit != null && activeUnit.x == unit.x &&
				activeUnit.y == unit.y )
			client.changedActiveUnit(activeUnit);   */
	}

	public synchronized void deactivateUnit()
	{           /*
		if ( activeUnit != null )
		{
//			System.out.println("deactivated " + activeUnit);
			Unit u = activeUnit;
			activeUnit = null;
			stopFlashing(u);
			int x = u.x;
			int y = u.y;
			if ( showingCityRanges && u.isSettler() )
			{
				removeCityRangeGlass(x,y);
			}
			if ( gotoMode )
				setGotoMode(false);
			client.changedActiveUnit(null);
		}        */

	}

	public synchronized void activateUnit(Unit u)
	{
            /*
		weaklyCenterOnTile(u.x,u.y);

		if ( activeUnit == u )
		{
			return;
		}
		deactivateUnit();
//		System.out.println("activated " + u);
		if ( u == null )
			return;

		Unit top = getUnit(u.x,u.y);
		if ( top != u )
		{
			Unit curr = top.nextInStack;
			if ( curr == u )
			{
				top.nextInStack = u.nextInStack;
				u.nextInStack = top;
			}
			else
			{
				for ( ; curr != null; curr = curr.nextInStack)
				{
					if ( curr.nextInStack == u )
					{
						curr.nextInStack = top;
						break;
					}
				}
				Unit tmp = u.nextInStack;
				u.nextInStack = top.nextInStack;
				top.nextInStack = tmp;
			}
			tiles[u.x][u.y].set(UNIT_LAYER,u);
		}

		startFlashing(u.x, u.y, u);
		activeUnit = u;
		lastActiveUnitId = u.id;
		weaklyCenterOnTile(u.x,u.y);
		client.changedActiveUnit(u);
		if ( showingCityRanges && u.isSettler() )
		{
			addCityRangeGlassRedOverlap(u.x,u.y);
		} */
	}

	public void removeCityRangeGlass( int x, int y)
	{               /*
		for ( int ax=-2; ax < 3; ax++ )
		{
			int starty = -2;
			int endy = 3;
			if ( (ax == -2) || (ax==2) )
			{
				starty = -1;
				endy = 2;
			}

			for ( int ay=starty; ay < endy; ay++ )
			{
				List l = getTileList(x+ax,y+ay);
				assert(l.size() > MAX_LAYER);
				if ( l != null )
					l.remove(l.size()-1);
			}
		}
		repaintTwoTilesAround(x,y);
	}


	public void addCityRangeGlass(int x, int y)
	{
		for ( int ax=-2; ax < 3; ax++ )
		{
			int starty = -2;
			int endy = 3;
			if ( (ax == -2) || (ax==2) )
			{
				starty = -1;
				endy = 2;
			}

			for ( int ay=starty; ay < endy; ay++ )
			{
				List l = getTileList(x+ax,y+ay);
				if ( l != null )
					l.add(grayGlassTile);
			}
		}
		repaintTwoTilesAround(x,y);     */
	}

   public void addCityRangeGlassRedOverlap(int x, int y)
	{                         /*
		for ( int ax=-2; ax < 3; ax++ )
		{
			int starty = -2;
			int endy = 3;
			if ( (ax == -2) || (ax==2) )
			{
				starty = -1;
				endy = 2;
			}

			for ( int ay=starty; ay < endy; ay++ )
			{
				List l = getTileList(x+ax,y+ay);
				if ( l != null )
				{
					if ( l.size() > MAX_LAYER )
						l.add(redGlassTile);
					else
						l.add(grayGlassTile);
				}
			}
		}
		repaintTwoTilesAround(x,y);    */
	}




	public synchronized void addCity(City c)
	{            /*
		tiles[c.x][c.y].set(CITY_LAYER,c);
		if ( showingCityRanges )
		{
			addCityRangeGlass(c.x,c.y);
		}
		else
		{
		  repaintOneTile(c.x,c.y);
		}

		smallMap.updateMap(c.x,c.y);     */
	}

	public synchronized void removeCity(City c)
	{                    /*
		tiles[c.x][c.y].set(CITY_LAYER,null);
		// this will remove glass from neighbour cities
		// maybe I'll correct it later
		if ( showingCityRanges )
		{
			removeCityRangeGlass(c.x,c.y);
		}
		else
		{
		  repaintOneTile(c.x,c.y);
		}
		smallMap.updateMap(c.x,c.y);    */

	}


	public void setUpperLeftXY(int x, int y)
	{
		super.setUpperLeftXY(x,y);
		smallMap.updateView();
	}


	protected void paintBackground( Graphics g, Rectangle bounds )
	{
		// do nothing (leave black);
	}

	// later make many nukes possible
	protected void paintComponent( Graphics g )
	{
		super.paintComponent(g);
		if ( nuking )
			paintNukeStep(g);
	}


	public void startFlashing( int x, int y, FlashingIcon i)
	{
		synchronized(flashing)
		{
			flashing.add(i);
		}
		i.setVisible(flashVisible);
		if ( flashing.size() == 1 )
		{
			timer.start();
			if ( flashVisible )
				actionPerformed(null); // hack to start flashing at once
		}
	}

	public void stopFlashing(FlashingIcon i)
	{
		synchronized(flashing)
		{
			int index = flashing.indexOf(i);
			Unit u = (Unit)flashing.remove(index);
			u.setVisible(true);
			repaintOneTile(u.x,u.y);
		}
		if ( flashing.size() == 0 )
			timer.stop();
	}

	// flash timer
	public void actionPerformed( ActionEvent e )
	{                         /*
		synchronized(flashing)
		{
			for ( int i =0; i < flashing.size(); i++ )
			{
				Unit u =(Unit)flashing.get(i);
				u.setVisible(flashVisible);
				repaintOneTile(u.x,u.y);

			}
			flashVisible = !flashVisible;
		}            */
	}

	public void centerOnTile(int x, int y)
	{
		super.centerOnTile(x,y);
		smallMap.updateView();
	}

	public boolean isGotoMode()
	{
		return gotoMode;
	}

	public void setGotoMode(boolean aGoto )
	{                   /*
		if ( gotoMode == aGoto )
			return;
		gotoMode = aGoto;
		if ( gotoMode )
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		else
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); */
	}



	public boolean isShowingCityRanges()
	{
		return showingCityRanges;
	}

	public synchronized void setShowingCityRanges(boolean sh)
	{               /*
		Unit u = activeUnit;
		deactivateUnit();
		if ( sh == showingCityRanges )
			return;
		showingCityRanges = sh;
		if ( !sh )
		{
			for ( int x = 0; x < horizontalTiles; x++ )
			{
				for ( int y = 0; y < verticalTiles; y++ )
				{
					ArrayList l = (ArrayList)getTileList(x,y);
					while ( l.size() > MAX_LAYER )
						l.remove(l.size()-1);
				}
			}
			repaint();
		}
		else
		{
			java.util.Iterator i = client.cities.iterator();
			while ( i.hasNext() )
			{
				// for each city
				City c = (City)i.next();
				if ( c != null )
					addCityRangeGlass(c.x,c.y);
			}
		}
		if ( u != null )
			activateUnit(u);  */
	}




	public void showUnitCombat(final PktUnitCombat unitCombat )
	{           /*
		deactivateUnit();
		Unit attA = (Unit)client.units.get(unitCombat.attacker_unit_id);
		centerOnTile(attA.x,attA.y);
		client.playSound(attA.prototype.name.toLowerCase());
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e )
			{
			}
		SwingUtilities.invokeLater(new Runnable() {
			Unit att = (Unit)client.units.get(unitCombat.attacker_unit_id);
			Unit def = (Unit)client.units.get(unitCombat.defender_unit_id);
			List attL = getTileList(att.x,att.y);
			List defL = getTileList(def.x,def.y);
			public void run()
			{
				if(att.hp>unitCombat.attacker_hp)
					att.hp--;
				if(def.hp>unitCombat.defender_hp)
					def.hp--;
				repaintOneTile(att.x,att.y);
				repaintOneTile(def.x,def.y);
				Toolkit.getDefaultToolkit().sync();
				try{
					Thread.currentThread().sleep(100);
				} catch ( InterruptedException e )
					{
					}
				if (def.hp>unitCombat.defender_hp || att.hp>unitCombat.attacker_hp)
				{
					SwingUtilities.invokeLater(this);
				}
				else
				{
					// make winner veteran ?
					if ( unitCombat.attacker_hp == 0 )
					{
						removeUnit((Unit)client.units.get(unitCombat.attacker_unit_id));
						client.units.set(unitCombat.attacker_unit_id,null);
						if ( unitCombat.make_winner_veteran )
							def.veteran = true;
					}
					else if ( unitCombat.defender_hp == 0 )
					{
						removeUnit((Unit)client.units.get(unitCombat.defender_unit_id));
						client.units.set(unitCombat.defender_unit_id,null);
						if ( unitCombat.make_winner_veteran )
							att.veteran = true;
					}

					synchronized( unitCombat )
					{
						unitCombat.notifyAll();
					}
				}
			}
		});

		synchronized( unitCombat )
		{
			try {
				unitCombat.wait();
			} catch ( InterruptedException e)
				{}
		}    */


	}

	public static final int START_NUKE_DELAY =-60;
	public static final int NUKE_UPDATE_MS = 60 ;

	public void explodeNuke(int x, int y )
	{    /*
		if ( nuking )
		{
			synchronized ( nukingWait )
			{
				try {
					nukingWait.wait();
				} catch (InterruptedException e )
					{}
			}
		}
		centerOnTile(x,y);
		nukeX = x;
		nukeY = y;
		nuking = true;
		centerX = getWidth()/2 + upperLeftX;
		centerY = getHeight()/2 + upperLeftY;
		NUKE_TRESHOLD = (singleTileWidth*3)/2;
		nukeRadius = client.sound instanceof RealSndSystem ?
			START_NUKE_DELAY : 0;
		client.playSound("nuclear");
		repaint();           */
	}

	/*
	int centerX;
	int nukeX;
	int nukeY;
	int centerY;
	int nukeRadius;

	Runnable nukeRepainter = new Runnable() {
		public void run()
		{
			try {
				Thread.currentThread().sleep(NUKE_UPDATE_MS);
			} catch (InterruptedException e )
				{
				}
			repaintTilesAround(nukeX,nukeY);
		}
	};



	int NUKE_TRESHOLD = (singleTileWidth*3)/2;
	static final int NUKE_PEN_WIDTH = 15;
	final int NUKE_PEN_DROPOFF = (NUKE_TRESHOLD/2)/NUKE_PEN_WIDTH;
	static BasicStroke wideStroke = new BasicStroke((float)NUKE_PEN_WIDTH+1);
	static BasicStroke[] strokes;
	static {
		strokes = new BasicStroke[NUKE_PEN_WIDTH+1];
		for ( int i=0; i < NUKE_PEN_WIDTH+1; i++ )
			strokes[i] = new BasicStroke((float)i+1);

	}

   */

	private void paintNukeStep(Graphics g)
	{
	   /*
		if ( nukeRadius > 0 )
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.gray);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
							0.60f - (0.25f*(float)nukeRadius/(float)NUKE_TRESHOLD)
													));
			g2.fillOval(centerX-nukeRadius-upperLeftX,centerY-nukeRadius-upperLeftY,
				nukeRadius*2-(NUKE_PEN_WIDTH/2),nukeRadius*2-(NUKE_PEN_WIDTH/2));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
							0.70f - (0.30f*(float)nukeRadius/(float)NUKE_TRESHOLD)
													));

			int x = (nukeRadius-(NUKE_TRESHOLD/2))/NUKE_PEN_DROPOFF;
			if ( x<0 )
			{
				g2.setStroke(wideStroke);
			}
			else
			{
				x = strokes.length-x-1;
				x = Math.min(strokes.length-1,x);
				x = Math.max(0,x);
				g2.setStroke(strokes[x]);
			}

			g2.drawOval(centerX-nukeRadius-upperLeftX,centerY-nukeRadius-upperLeftY,
				nukeRadius*2,nukeRadius*2);
			Toolkit.getDefaultToolkit().sync();
			nukeRadius++;
			if ( nukeRadius > NUKE_TRESHOLD )
			{
				nuking = false;
				synchronized (nukingWait)
				{
					nukingWait.notify();
				}
			}
		}
		else
		{
			// sound wait
			nukeRadius++;
		}
		javax.swing.SwingUtilities.invokeLater(nukeRepainter);
	   */
	}

	public boolean isManagingFocus()
	{
		return true;
	}

	public void componentResized(ComponentEvent e)
	{
		smallMap.updateView();
	}

	public void componentMoved(ComponentEvent e)
	{

	}

	public void componentShown(ComponentEvent e)
	{

	}

	public void componentHidden(ComponentEvent e)
	{

	}


	public static final int TERRAIN_LAYER = 0;
	public static final int TERRAIN_OVERLAY_LAYER = 1;
	public static final int RESOURCE_LAYER = 2;
	public static final int IRRIGATION_LAYER = 3;
	public static final int ROAD_LAYER = 4;
	public static final int ROAD_DIAGONAL_LAYER = 5;
	public static final int HUT_LAYER = 6;
	public static final int CITY_LAYER = 7;
	public static final int UNIT_LAYER = 8;
	public static final int SHADOW_LAYER =9;
	public static final int MAX_LAYER = 10;


/*
	  class SmallMap extends JComponent
	  {
		 int ht = getHorizontalTiles();
		 int vt = getVerticalTiles();
		 int data[] = new int[vt*ht];
		 MemoryImageSource mis = new MemoryImageSource(ht,vt,data,0,ht);
		 Image img;

		private Insets insets;
		Color[] colors = new Color[]
		{
			Color.black,
			Color.white,
			Color.blue,
			Color.green
		};

		int oldX,oldY,oldW,oldH;
		int sizeH,sizeV;

		int pixelSize = 2;


		 public SmallMap()
		 {
			mis.setAnimated(true);
			img = createImage(mis);
		  Dimension d = new Dimension(ht,vt);
			setPreferredSize(d);
			setMinimumSize(d);
		  setMaximumSize(d);
			for ( int i =0; i < data.length; i++ )
			  data[i] = Color.green.getRGB();
			mis.newPixels();
			insets = getInsets();

		 }

		 public void updateMapInternal(int x, int y)
		 {
			List l = getTileList(x,y);
			int pixel = 0;
		  Object o = l.get(CITY_LAYER);
			if ( o != null )
			{
			  pixel = 0x00ffffff; // white
			}
			else
			{
			pixel = 0x00ff0000;
			  Terrain t = (Terrain)l.get(TERRAIN_LAYER);
			  if ( t != null )
			  {
			  if ( t.getId() == T_OCEAN )
					pixel = 0x000000ff; // blue
				 else
				 {
					pixel = 0x0000ff00; // green
			  }
			  }
			}
			data[x+y*ht] = pixel;

		 }

		public void updateMap(int x, int y)
		{
			updateMapInternal(x,y);
	//      mis.newPixels(x,y,1,1);
			mis.newPixels();
			repaint(x*pixelSize+insets.left,y*pixelSize+insets.top,
				pixelSize,pixelSize);
		}

		public void updateAll()
		{
			for ( int x =0; x < ht; x++ )
				for ( int y =0; y < vt; y++ )
					updateMapInternal(x,y);
			updateView();
			mis.newPixels();
			repaint();
		}




		 public void updateView()
		 {
		 }

		 public void paintComponent(Graphics g)
		 {
//			g.setColor(Color.black);
//			g.fillRect(0,0,ht,vt);
			g.drawImage(img,0,0,this);
		}
	  }
 */

	class SmallMap extends JComponent
	{
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.50f);
		int ht = getHorizontalTiles();
		int vt = getVerticalTiles();
		byte[] data = new byte[vt*ht];
		private Insets insets;
		Color[] colors = new Color[]
		{
			Color.black,
			Color.white,
			Color.blue,
			Color.green
		};

		int oldX,oldY,oldW,oldH;
		int sizeH,sizeV;

		int pixelSize = 2;



		public SmallMap()
		{
			setSizes();
			setOpaque(false);

			addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent e )
				{
					if ( (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 )
					{
						int x = e.getX()-insets.left;
						int y = e.getY()-insets.top;
						if ( x < 0 || x >= sizeH || y < 0 || y >= sizeV )
							return;
						centerOnTile(x/pixelSize,y/pixelSize);
					}
					else
					{
						//changeMode();
					}
				}
			} );

		}

		public void setSizes()
		{
			sizeH = ht*pixelSize;
			sizeV = vt*pixelSize;
			Dimension d = new Dimension(sizeH,sizeV);
			insets = SmallMap.this.getInsets();
			d.width += insets.left + insets.right;
			d.height += insets.top + insets.bottom;
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);

		}

		private void updateMapInternal(int x, int y)
		{
			List l = getTileList(x,y);
			byte pixel = 0;
			Object o = l.get(CITY_LAYER);
			if ( o != null )
			{
				pixel = 1; // white
			}
			else
			{
				Terrain t = (Terrain)l.get(TERRAIN_LAYER);
				if ( t != null )
				{
					if ( (t.getId() == T_OCEAN) || (t.getId() == T_RIVER) )
					{
						pixel = 2; // blue
					}
					else
					{

						pixel = 3; // green
					}
				}
			}
			data[x+y*ht] = pixel;
		}

		public void updateMap(int x, int y)
		{
			updateMapInternal(x,y);
			repaint(x*pixelSize+insets.left,y*pixelSize+insets.top,
				pixelSize,pixelSize);
		}

		public void updateAll()
		{
			for ( int x =0; x < ht; x++ )
				for ( int y =0; y < vt; y++ )
					updateMapInternal(x,y);
			updateView();
			repaint();
		}

		public void updateView()
		{
			int nx,ny,nw,nh;
			Coords p = getCentralTile();

			nx = p.x;
			ny = p.y;
			nw = CivMap.this.getWidth()/getSingleTileWidth();
			nh = CivMap.this.getHeight()/getSingleTileWidth();
			if ( (oldX != nx) || (oldY != ny) || (oldW!=nw) || (oldH!=nh) )
			{
				oldX = nx*pixelSize;
				oldY = ny*pixelSize;
				oldW = nw*pixelSize;
				oldH = nh*pixelSize;
				repaint(); // optimize later to repaint only sum of old/new ?
			}
		}

		Color c = new Color(30,30,30);

		public void paintComponent(Graphics g)
		{
			Rectangle r = g.getClipBounds();
			int minX = Math.max(r.x,insets.left);
			int minY = Math.max(r.y,insets.top);
			int maxX =  Math.min(sizeH+insets.left,r.x+r.width);
			int maxY =  Math.min(sizeV+insets.top,r.y+r.height);
			g.clipRect(insets.left,insets.top,maxX,maxY);

//			((Graphics2D)g).setComposite(alpha);
			g.setColor(c);
			g.fillRect(minX,minY,maxX-minX,maxY-minY);

			for ( int x = minX; x < maxX; x+= pixelSize )
			{
				for ( int y = minY; y < maxY; y+= pixelSize )
				{
					int color = data[
						(x-insets.left)/pixelSize +
						((y-insets.top)/pixelSize)*ht
						];
					if ( color != 0 )
					{
						g.setColor(colors[color]);
						g.fillRect(x,y,pixelSize,pixelSize);
					}
				}
			}
			g.setColor(Color.yellow);
			int x = oldX- oldW/2 + insets.left;
			int y = oldY - oldH/2 + insets.top;
			g.drawRect(x,y,oldW,oldH);
			if ( isHorizontallyWrapped() )
			{
				g.drawRect(x-sizeH,y,oldW,oldH);
				g.drawRect(x+sizeH,y,oldW,oldH);
				if ( isVerticallyWrapped() )
				{
					g.drawRect(x-sizeH,y-sizeV,oldW,oldH);
					g.drawRect(x+sizeH,y+sizeV,oldW,oldH);
				}
			}

			if ( isVerticallyWrapped() )
			{
				g.drawRect(x,y-sizeV,oldW,oldH);
				g.drawRect(x,y+sizeV,oldW,oldH);
			}
//			g.setPaintMode();
		}

	}
  

	class GlassTile implements Icon
	{
		AlphaComposite alpha;
		Color color;


		public GlassTile(Color c, float anAlpha)
		{
			color = c;
			alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,anAlpha);
		}

		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(color);
			g2.setComposite(alpha);
			g2.fill3DRect(x,y,getSingleTileWidth(),getSingleTileHeight(),true);
			g2.setPaintMode();
		}

		public int getIconWidth()
		{
			return getSingleTileWidth();
		}

		public int getIconHeight()
		{
			return getSingleTileHeight();
		}


	}

	public Icon grayGlassTile = new GlassTile(Color.gray,0.50f);
	public Icon redGlassTile = new GlassTile(Color.red,0.30f);


	public static void assert(boolean condition)
	{
		if ( !condition )
			throw new RuntimeException("Assertion failed");
	}


}
