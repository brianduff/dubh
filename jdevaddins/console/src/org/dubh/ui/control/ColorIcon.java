/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is The Dubh.Org Java Utilities
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.ui.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 * An icon that is a colored swatch with a drop down indicator.
 *
 * @author Brian.Duff@oracle.com
 */
public class ColorIcon implements Icon
{
  private static final int DEFAULT_SIZE = 16;
  private final Dimension m_size;

  private Color m_color;

  public ColorIcon( Color color )
  {
    this( color, new Dimension( DEFAULT_SIZE, DEFAULT_SIZE ) );
  }

  public ColorIcon( Color color, Dimension size )
  {
    m_color = color;
    m_size = size;
  }

  public Color getColor()
  {
    return m_color;
  }

  public void setColor( Color c )
  {
    m_color = c;
  }

// ---------------------------------------------------------------------------
// Icon implementation
// ---------------------------------------------------------------------------

  public int getIconWidth()
  {
    return m_size.width;
  }

  public int getIconHeight()
  {
    return m_size.height;
  }

  public void paintIcon( Component component, Graphics g, int x, int y )
  {
    g.setColor( getColor() );
    g.fillRect( x, y, getIconWidth()-1, getIconHeight()-1 );

    g.setColor( Color.black );
    g.drawRect( x, y, getIconWidth()-1, getIconHeight()-1 );
    
  }


}