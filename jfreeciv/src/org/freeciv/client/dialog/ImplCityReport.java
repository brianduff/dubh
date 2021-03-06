package org.freeciv.client.dialog;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.MultiLineHeaderJTable;

public class ImplCityReport extends JDialog implements DlgCityReport
{
  private static String[] buttonLabels = { translate("Close"), translate("Center"), translate("Popup"),
                                      translate("Buy"), translate("Change"), translate("Change All"),
                                      translate("Refresh"), translate("Select"), translate("Configure") };

  private Client m_client;
  private FlowLayout layout = new FlowLayout();
  private MultiLineHeaderJTable table;
  private JButton[] buttons = new JButton[9];

  public ImplCityReport( Client c )
  {
    m_client = c;
    setupHeader();
    setupTable();
    setupButtonPanel();
    getContentPane().setLayout( layout );
    setSize( 700, 450 );
  }

  /**
   * Sets the "header" up for the report
   */
  private void setupHeader()
  {
    Box box = new Box( BoxLayout.Y_AXIS );
    JTextField text1 = new JTextField( translate("Cities") );
    JTextField text2 = new JTextField( translate("Democracy of the Americans") );
    JTextField text3 = new JTextField( translate("George W Bush: 2001 AD") );
    text1.setEditable( false );
    text1.setOpaque( false );
    text1.setHorizontalAlignment( JTextField.CENTER );
    text1.setBorder( null );
    box.add( translate("Cities"), text1 );

    text2.setEditable( false );
    text2.setOpaque( false );
    text2.setHorizontalAlignment( JTextField.CENTER );
    text2.setBorder( null );
    box.add( translate("<Goverment> of the <Country>"), text2 );

    text3.setEditable( false );
    text3.setOpaque( false );
    text3.setHorizontalAlignment( JTextField.CENTER );
    text3.setBorder( null );
    box.add( translate("<Leader>: <Date>"), text3 );

    layout.addLayoutComponent( translate("Header"), box );
    getContentPane().add( box );
  }

  /**
   * Sets the table with the list of cities up
   */
  private void setupTable()
  {
    TableSorter sorter = new TableSorter( new MyTableModel() );
    table = new MultiLineHeaderJTable( sorter );
    sorter.addMouseListenerToHeaderInTable( table );
    table.setColumnSelectionAllowed( false );
    table.setRowSelectionAllowed( false );
    table.setShowGrid( false );
    JScrollPane scroll = new JScrollPane( table );
    scroll.setPreferredSize( new Dimension( 696, 300 ) );
    layout.addLayoutComponent( translate("Table"), scroll );
    getContentPane().add( scroll );
  }

  /**
   * Sets the button panel up
   */
  private void setupButtonPanel()
  {
    JPanel panel = new JPanel();

    for( int i = 0; i < buttons.length; i++ )
    {
      buttons[i] = new JButton( buttonLabels[i] );
      buttons[i].addActionListener( getActionListener( i ) );
      panel.add( buttons[i] );
    }
    getContentPane().add( panel );
  }

  private ActionListener getActionListener( int i )
  {
    ActionListener[] actions = {  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {
                                      setVisible( false );
                                    }
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {
                                    }
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  },
                                  new ActionListener()
                                  {
                                    public void actionPerformed( ActionEvent e )
                                    {}
                                  } };
    return actions[i];
  }


  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }

  public void refresh()
  {
    ;
  }

  public void display()
  {
    setVisible( true );
  }

  public void undisplay()
  {
    setVisible( false );
  }

  private class MyTableModel extends AbstractTableModel
  {
    String[] columnNames = { translate("Name"), translate("State"), translate("Workers\nH/C/U"), translate("Surplus\nF/P/T"),
                            translate("Economy\nG/L/S"), translate("Food\nStock"),
                            translate("Currently Building\n(Stock, Target, Turns, Buy)") };
    Object[][] data = { { translate("New York"), translate("War"), translate("0/5/1"), translate("23/15/31"), translate("3/6/1"),
                            translate("97/120"), translate("(La,la,la,la)") },
                        { translate("San Diego"), translate("Peace"), translate("9/2/5"), translate("3/5/2"),
                            translate("2/6/8"), translate("30/40"), translate("(A,a,fl,al)") } };

    public int getColumnCount()
    {
      return columnNames.length;
    }
    public int getRowCount()
    {
      return data.length;
    }
    public String getColumnName( int col )
    {
      return columnNames[ col ];
    }
    public Object getValueAt( int row, int col )
    {
      return data[row][col];
    }
  }

  private class TableSorter extends TableMap  //from java.sun.com tutorials
  {
    int             indexes[];
    Vector          sortingColumns = new Vector();
    boolean         ascending = true;
    int compares;

    public TableSorter() {
        indexes = new int[0]; // for consistency
    }

    public TableSorter(TableModel model) {
        setModel(model);
    }

    public void setModel(TableModel model) {
        super.setModel(model);
        reallocateIndexes();
    }

    public int compareRowsByColumn(int row1, int row2, int column) {
        Class type = model.getColumnClass(column);
        TableModel data = model;

        // Check for nulls.

        Object o1 = data.getValueAt(row1, column);
        Object o2 = data.getValueAt(row2, column);

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        /*
         * We copy all returned values from the getValue call in case
         * an optimised model is reusing one object to return many
         * values.  The Number subclasses in the JDK are immutable and
         * so will not be used in this way but other subclasses of
         * Number might want to do this to save space and avoid
         * unnecessary heap allocation.
         */

        if (type.getSuperclass() == java.lang.Number.class) {
            Number n1 = (Number)data.getValueAt(row1, column);
            double d1 = n1.doubleValue();
            Number n2 = (Number)data.getValueAt(row2, column);
            double d2 = n2.doubleValue();

            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == java.util.Date.class) {
            Date d1 = (Date)data.getValueAt(row1, column);
            long n1 = d1.getTime();
            Date d2 = (Date)data.getValueAt(row2, column);
            long n2 = d2.getTime();

            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == String.class) {
            String s1 = (String)data.getValueAt(row1, column);
            String s2    = (String)data.getValueAt(row2, column);
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Boolean.class) {
            Boolean bool1 = (Boolean)data.getValueAt(row1, column);
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean)data.getValueAt(row2, column);
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) { // Define false < true
                return 1;
            } else {
                return -1;
            }
        } else {
            Object v1 = data.getValueAt(row1, column);
            String s1 = v1.toString();
            Object v2 = data.getValueAt(row2, column);
            String s2 = v2.toString();
            int result = s1.compareTo(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int compare(int row1, int row2) {
        compares++;
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = (Integer)sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        return 0;
    }

    public void reallocateIndexes() {
        int rowCount = model.getRowCount();

        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];

        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }

    public void tableChanged(TableModelEvent e) {
        //System.out.println("Sorter: tableChanged");
        reallocateIndexes();

        super.tableChanged(e);
    }

    public void checkModel() {
        if (indexes.length != model.getRowCount()) {
            System.err.println("Sorter not informed of a change in model.");
        }
    }

    public void sort(Object sender) {
        checkModel();

        compares = 0;
        // n2sort();
        // qsort(0, indexes.length-1);
        shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
        //System.out.println("Compares: "+compares);
    }

    public void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i+1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }

    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high)/2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        /* This is an optional short-cut; at each recursive call,
        check to see if the elements in this subset are already
        ordered.  If so, no further comparisons are needed; the
        sub-array can just be copied.  The array must be copied rather
        than assigned otherwise sister calls in the recursion might
        get out of sinc.  When the number of elements is three they
        are partitioned so that the first set, [low, mid), has one
        element and and the second, [mid, high), has two. We skip the
        optimisation when the number of elements is three or less as
        the first compare in the normal merge will produce the same
        sequence of steps. This optimisation seems to be worthwhile
        for partially ordered lists but some analysis is needed to
        find out how the performance drops to Nlog(N) as the initial
        order diminishes - it may drop very quickly.  */

        if (high - low >= 4 && compare(from[middle-1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.

        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            }
            else {
                to[i] = from[q++];
            }
        }
    }

    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".

    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return model.getValueAt(indexes[aRow], aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        model.setValueAt(aValue, indexes[aRow], aColumn);
    }

    /**
     * Sort by column, toggling the ascending attribute
     */
    public void sortByColumn(int column) {
        sortByColumn(column, !this.ascending);
    }

    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        sort(this);
        super.tableChanged(new TableModelEvent(this));
    }

    protected boolean isAscending()
    {
       return this.ascending;
    }

    // There is no-where else to put this.
    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(JTable table) {
        final TableSorter sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(true);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    sorter.sortByColumn(column);
                }
            }
        };
      JTableHeader th = tableView.getTableHeader();
      th.addMouseListener(listMouseListener);
    }
  }

  public class TableMap extends AbstractTableModel
                      implements TableModelListener    //from java.sun.com tutorials
  {
    protected TableModel model;

    public TableModel getModel() {
        return model;
    }

    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    // By default, implement TableModel by forwarding all messages
    // to the model.

    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }

    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }

    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }

    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn);
    }

    public boolean isCellEditable(int row, int column) {
         return false;
    }
//
// Implementation of the TableModelListener interface,
//
    // By default forward all events to all the listeners.
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
  }
}

