package org.freeciv.client.dialog.util;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Displays a stack of icons as one single icon
 */
public class IconStack
  implements Icon
{
  protected ArrayList m_list; 
    
  public IconStack()
  {
    m_list = new ArrayList();
  }
    
  public void paintIcon( Component c, Graphics g, int x, int y )
  {
    for( Iterator i = m_list.listIterator(); i.hasNext(); )
    {
      Icon icon = (Icon)i.next();
      icon.paintIcon( c, g, x, y );
    }
  }
    
  public int getIconWidth()
  {
    int width = 0;
    for( Iterator i = m_list.listIterator(); i.hasNext(); )
    {
      Icon icon = (Icon)i.next();
      width = Math.max( width, icon.getIconWidth() );
    }
    return width;
  }
    
  public int getIconHeight()
  {
    int height = 0;
    for( Iterator i = m_list.listIterator(); i.hasNext(); )
    {
      Icon icon = (Icon)i.next();
      height = Math.max( height, icon.getIconHeight() );
    }
    return height;
  }
  
  /**
   * Add an icon onto the end of the stack
   */
  public void addIcon( Icon icon )
  {
    m_list.add( icon );
  }
  
  /**
   * Access the list to change it.  Please only add icons.
   */
  public java.util.List getList()
  {
    return m_list;
  }

}
      
  
