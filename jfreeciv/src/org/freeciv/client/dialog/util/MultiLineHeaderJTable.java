package org.freeciv.client.dialog.util;

import org.freeciv.client.dialog.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class MultiLineHeaderJTable extends JTable
{
  private static int[] columnWidths = { 30, 30, 70, 70, 70, 30, 140 };

  public MultiLineHeaderJTable( TableModel model )
  {
    super( model );
    TableColumnModel columnModel = getColumnModel();
    int columnCount = columnModel.getColumnCount();
    for(int i = 0; i < columnCount; i++)
    {
        columnModel.getColumn(i).setHeaderRenderer( new MultiLineTableHeaderRenderer() );
        columnModel.getColumn(i).setPreferredWidth( columnWidths[i] );
    }
  }

}