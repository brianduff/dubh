package org.freeciv.tile;
import java.awt.*;
import javax.swing.*;
public class FlashingImageIcon extends ImageIcon implements FlashingIcon
{
  private boolean visible = true;
  public FlashingImageIcon( Image img ) 
  {
    super( img );
  }
  public void paintIcon( Component c, Graphics g, int x, int y )
  {
    if( visible )
    {
      super.paintIcon( c, g, x, y );
    }
  }
  public void setVisible( boolean aVisible )
  {
    visible = aVisible;
  }
}
