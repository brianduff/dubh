package org.freeciv.client.ui.util;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.LayoutManager2;
import javax.swing.*;

/**
 * ButtonBarLayout lays out its components left to right, or right to left 
 * (depending on the alignment), ensuring that all components are the same 
 * height and are all the same width as the component with the widest preferred
 * width.
 * 
 * @author Brian Duff (dubh@dubh.org)
 */
public class ButtonBarLayout implements LayoutManager2 
{
  private static final int GAP = 5;
  
  private final int m_alignment;


  /**
   * Construct a button bar layout with the specified alignment. 
   * 
   * @param alignment the alignment of the buttonbar - either 
   *    SwingConstants.LEFT or SwingConstants.RIGHT.
   */
  public ButtonBarLayout( int alignment )
  {
    m_alignment = alignment;
  }
  
  /**
   * Construct a button bar layout with the default alignment (east)
   */
  public ButtonBarLayout( )
  {
    this( SwingConstants.RIGHT );
  }

  public void addLayoutComponent(Component comp, Object constraints)
  {
    // NOOP
  }

  public Dimension maximumLayoutSize( Container target )
  {
    return new Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
  }

  public float getLayoutAlignmentX( Container target )
  {
    return 0;
  }

  public float getLayoutAlignmentY( Container target )
  {
    return 0;
  }

  public void invalidateLayout( Container target )
  {
  }

  public void addLayoutComponent( String name, Component comp )
  {
    // NOOP
  }

  public void removeLayoutComponent(Component comp)
  {
    // NOOP
  }

  public Dimension preferredLayoutSize(Container parent)
  {
    int prefWidth = 0;
    int prefHeight = 0;
    
    int maxItemWidth = 0;
    
    int componentCount = parent.getComponentCount();
    for ( int i=0; i < componentCount; i++ )
    {
      Component component = parent.getComponent( i );
      Dimension d = component.getPreferredSize();
      
      maxItemWidth = Math.max( maxItemWidth, d.width );
      prefHeight = Math.max( prefHeight, d.height );
    }
    
    prefWidth = componentCount * maxItemWidth;
    
    // Add space for gaps between buttons.
    if ( componentCount > 1 )
    {
      prefWidth += ( ( componentCount - 1 ) * GAP );
    }
    
    return new Dimension( prefWidth, prefHeight );
  }

  public Dimension minimumLayoutSize(Container parent)
  {
    return preferredLayoutSize( parent );
  }

  public void layoutContainer( Container parent )
  {
    int componentCount = parent.getComponentCount();
    
    int prefWidth = 0;
    int prefHeight = 0;
    
    int maxItemWidth = 0;
    
    for ( int i=0; i < componentCount; i++ )
    {
      Component component = parent.getComponent( i );
      Dimension d = component.getPreferredSize();
      
      maxItemWidth = Math.max( maxItemWidth, d.width );
      prefHeight = Math.max( prefHeight, d.height );  
    }
    
    if ( m_alignment == SwingConstants.LEFT )
    {
      int x = 0;
      
      for ( int i = 0; i < componentCount; i++ )
      {
        Component component = parent.getComponent( i );
        component.setBounds( x, 0, maxItemWidth, prefHeight );
  
        x += maxItemWidth + GAP;
      }
    }
    else
    {
      int x = parent.getWidth() - maxItemWidth;
      
      for ( int i = 0; i < componentCount; i++ )
      {
        Component component = parent.getComponent( i );
        component.setBounds( x, 0, maxItemWidth, prefHeight );
  
        x -= maxItemWidth + GAP;
      }      
    }
  }

}