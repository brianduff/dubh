package org.freeciv.client.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.freeciv.client.Client;
import org.freeciv.client.ui.util.ToolButton;
import org.freeciv.common.Unit;

/**
 * Displays a stack of units on the current grid square
 *
 * @author Brian Duff
 */
public final class UnitStackDisplay extends JPanel
{
  private Client m_client;
  private Dimension m_tileDimension;


  /**
   * Construct the unit stack display
   */
  public UnitStackDisplay( Client c )
  {
    m_client = c;
    m_tileDimension = new Dimension( 
      m_client.getTileSpec().getNormalTileWidth(),
      m_client.getTileSpec().getNormalTileHeight()
    );
    setLayout( new FlowLayout() );
  }

  /**
   * Call this whenever the active unit or active tile changes
   */
  public void update(int gridx, int gridy, Unit focusUnit)
  {
    this.removeAll();

    this.add( new UnitComponent(focusUnit) );
    Iterator tileUnits = 
      m_client.getGame().getMap().getTile( gridx, gridy ).getUnits();

    while (tileUnits.hasNext() )
    {
      Unit u = (Unit)tileUnits.next();
      if ( u != focusUnit )
      {
        this.add( new UnitComponent(u) );
      }
    }

    invalidate();
    validate();
  }


  /**
   * This component is used to actually represent a unit
   */
  private class UnitComponent extends ToolButton
  {
    private Unit m_unit;
    private boolean m_toggled;

    private UnitComponent( Unit u )
    {
      m_unit = u;
      initIcon();
      initToolTip();
      boolean toggle = (m_client.getUnitInFocus() == m_unit);
      if (toggle)
      {
        // hack: simulate a click inside the button, yech
        m_isInside = true;
        mouseReleased( null );
        m_isInside = false;
      }

      Dimension prefSize = getPreferredSize();
      setPreferredSize( new Dimension( prefSize.width+3, prefSize.height+3 ));
      
    }

    private Unit getUnit()
    {
      return m_unit;
    }

    private void initIcon()
    {
      setIcon( m_unit.getSprite() );
    }

    private void initToolTip()
    {
      StringBuffer toolTipBuffer = new StringBuffer();
      String nationName = m_unit.getOwner().getNation().getName();
      toolTipBuffer.append( nationName );
      toolTipBuffer.append( " " );
      String unitName = m_unit.getUnitType().getName();
      toolTipBuffer.append( unitName );
      toolTipBuffer.append( " (" );
      toolTipBuffer.append( m_unit.getUnitType().getAttackStrength() );
      toolTipBuffer.append("/");
      toolTipBuffer.append( m_unit.getUnitType().getDefenseStrength() );
      toolTipBuffer.append("/");
      toolTipBuffer.append( m_unit.getUnitType().getMoveRate() );
      toolTipBuffer.append(")");

      setToolTipText( toolTipBuffer.toString() );
    }

    public void mouseEntered( MouseEvent e )
    {
      if ( !m_toggled )
      {
        super.mouseEntered( e );
      }
      else
      {
        m_isInside = true;
      }
    }

    public void mouseExited( MouseEvent e )
    {
      if ( !m_toggled )
      {
        super.mouseExited( e );
      }
      else
      {
        m_isInside = false;
      }
    }

    public void mousePressed( MouseEvent e )
    {
      if ( m_toggled )
      {
        setBackground( UIManager.getDefaults().getColor( "control" ) );
        m_isDepressed = true;
      }
      else
      {
        super.mousePressed( e );
      }
    }

    public void mouseReleased( MouseEvent e )
    {
      if ( !m_toggled && m_isInside )
      {
        m_toggled = true;
        setBorder( LOWERED_BORDER );
        setBackground( UIManager.getDefaults().getColor( "control" ).brighter() );
        m_isDepressed = false;
      }
      else if ( m_toggled && m_isInside )
      {
        m_toggled = false;
        setBorder( RAISED_BORDER );
        setBackground( UIManager.getDefaults().getColor( "control" ) );
        m_isDepressed = false;        
      }
      else if ( m_toggled && !m_isInside )
      {
        setBackground( UIManager.getDefaults().getColor( "control" ) );
        m_isDepressed = false;        
      }
      else
      {
        super.mouseReleased( e );
      }
    }

  }


}
