package org.freeciv.client.dialog.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class MultiLineTableHeaderRenderer extends JPanel implements TableCellRenderer
{
  JTextArea text = new JTextArea();


  public Component getTableCellRendererComponent( JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column )
  {
    if( value instanceof String )
    {
      text.setText( (String) value );
      add( text );
    }

    text.setOpaque( false );
    setBorder( new EtchedBorder( EtchedBorder.LOWERED ) );
    return this;
  }
}
