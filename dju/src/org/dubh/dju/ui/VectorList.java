/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.utils.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A VectorList is a generic panel which displays a vector of values in a list,
 * allowing the user to add / remove items from the list and optionally move
 * the items up and down in the list. <P>
 * To use this class properly, you probably want to add an action listener.
 * Clicks on the buttons will trigger an actionPerformed
 * event on all listeners. At this point, you can add an item to the list
 * (using the addElement() method), or change an item (use getSelectedItem()).
 * If you change an item, call updateList() to repaint the list and make sure
 * the scrollbars are updated properly. When the action command is ADD_ACTION,
 * the add button has been pressed. For EDIT_ACTION, the edit button has been
 * pressed, and using getSource() on the ActionEvent will return the currently
 * selected item (the item that is to be edited). EDIT_ACTIONs are also fired
 * for double clicks on items in the list.<P>
 * You can switch the Move Up / Move Down buttons on or off using the
 * setReorderable() method. ActionEvents with the action command MOVE_ACTION
 * are fired whenever these two buttons are clicked, but there is no need for
 * you to do anything unless you want to provide additional functionality.<P>
 * A click on the remove button fires a REMOVE_ACTION event. There is no need
 * for you to do anything special: the item will have already been removed from
 * the graphical display by the time the event is fired, but you may want to
 * perform some extra work on items that are being deleted. For such events,
 * using getSource() on the ActionEvent object returns the item in the list that
 * is being deleted.<P>
 * When you are finished (e.g. the dialogue containing this VectorList is
 * dismissed), you might want to use the getListContents() method
 * to retrieve an array of items in the list, in the order the user has
 * arranged them.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [09/05/98]: Initial Revision
 * <LI>0.2 [18/06/98]: Better event support: the action listener interface now
 *   reports MOVE_ACTION and REMOVE_ACTION events for when the user moves
 *   or  deletes an item. You can also add selection listeners for the list.
 *   Changed setListContents so that it clears the previous contents of the list
 *   before adding new items. Added double click support - double clicking an
 *   item is the same as selecting it and doing Edit...
 * <LI>0.3 [19/06/98]: Added support for getting the buttons so that they
 *   can be customized (text, icons changed for example).
 * <LI>0.4 [28/06/98]: Added code to disable buttons that require a selection
 *   when there is no current selection [BUGFIX]. Deprecated fireAddEditEvent,
 *   and fixed MOVE_ACTION events so that the source of the event is the
 *   list item that was moved.
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.4 [28/06/98]
 */
public class VectorList extends JPanel {
// Private parts

  private GridBagLayout layoutMain = new GridBagLayout();
  private JButton cmdAdd = new JButton();
  private JButton cmdEdit = new JButton();
  private JButton cmdRemove = new JButton();
  private JButton cmdMoveUp = new JButton();
  private JButton cmdMoveDown = new JButton();
  private DefaultListModel lmList = new DefaultListModel();
  private JList   listList = new JList(lmList);
  private JScrollPane scrollList = new JScrollPane(listList);
  private Vector listeners = new Vector();

  public static final String ADD_ACTION = "add";
  public static final String EDIT_ACTION = "edit";
  public static final String MOVE_ACTION  = "move";
  public static final String REMOVE_ACTION = "del";

  /**
   * Construct an empty VectorList.
   */
  public VectorList() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Construct a VectorList consisting of the specified vector.
   @param v a vector to display in the list
   */
  public VectorList(Vector v) {
     setListContents(v);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception{
    cmdAdd.setText("Add...");
    cmdAdd.addActionListener(new AddActionAdapter());
    cmdEdit.setText("Edit...");
    cmdEdit.addActionListener(new EditActionAdapter());
    cmdEdit.setEnabled(false);
    cmdRemove.setText("Remove");
    cmdRemove.addActionListener(new RemoveActionAdapter());
    cmdRemove.setEnabled(false);
    cmdMoveUp.setText("Move Up");
    cmdMoveUp.addActionListener(new MoveUpActionAdapter());
    cmdMoveUp.setEnabled(false);
    cmdMoveUp.setVisible(false);
    cmdMoveDown.setText("Move Down");
    cmdMoveDown.addActionListener(new MoveDownActionAdapter());
    cmdMoveDown.setEnabled(false);
    cmdMoveDown.setVisible(false);
    listList.addListSelectionListener(new ListListener());
    listList.addMouseListener(new DoubleClickListener());
    this.setLayout(layoutMain);
    this.add(cmdAdd, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 1, 5), 0, 0));
    this.add(cmdEdit, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 5), 0, 0));
    this.add(cmdRemove, new GridBagConstraints2(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 5, 5), 0, 0));
    this.add(cmdMoveUp, new GridBagConstraints2(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 5), 0, 0));
    this.add(cmdMoveDown, new GridBagConstraints2(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 5, 5), 0, 0));
    this.add(scrollList, new GridBagConstraints2(0, 0, 1, 5, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
     listList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  /**
   * Set whether the "move up" and "move down" buttons are visible and the order
   * of elements in the list can be changed by the user.
   @param b true if the elements in the list should be reorderable
   */
  public void setReorderable(boolean b) {
     cmdMoveUp.setVisible(b);
     cmdMoveDown.setVisible(b);
     validate();
  }

  /**
   * Determine whether the move up and move down buttons are visible.
   @return true if the list can be reordered using the buttons
   */
  public boolean isReorderable() {
     return cmdMoveUp.isVisible();
  }

  /**
   * Set the vector the list is displaying.
   */
  public void setListContents(Vector v) {
     lmList.removeAllElements();
     for (int i=0; i< v.size(); i++) {
        lmList.addElement(v.elementAt(i));
     }
     if (isVisible()) updateList();
  }

  /**
   * Get the vector currently being displayed in the list. You should call this
   * when you are finished with the panel, in order to get the items in the
   * order the user specified them in.
   @return an array of objects
   */
  public Object[] getListContents() {
     return lmList.toArray();
  }


  /**
   * Adds an item to the list. This should normally be used after an add
   * action has been fired. The new item will be automatically selected.
   @param item an Object to add to the list
   */
  public void addElement(Object item) {
     lmList.addElement(item);
     listList.revalidate();  // force scrollbars to update
     listList.setSelectedIndex(lmList.size()-1);
     cmdMoveDown.setEnabled(false);
     if (lmList.size() == 1) cmdMoveUp.setEnabled(false);
     else cmdMoveUp.setEnabled(true);
     repaint();
  }

  /**
   * Gets the first selected item in the list.
   @returns the first selected item (several items may actually be selected),
     or null if there is no current selection)
   */
  public Object getSelectedItem() {
     return listList.getSelectedValue();
  }

  /**
   * Updates the list. You should call this if (for instance), you intercepted
   * an edit event, and changed an object in the list in some way.
   */
  public void updateList() {
     listList.revalidate();
     repaint();
  }

  /**
   * Set a cell renderer for the list.
   @param renderer a component that renders items in the list
   */
  public void setCellRenderer(ListCellRenderer cellRenderer) {
     listList.setCellRenderer(cellRenderer);
  }

  /**
   * Add a listener for clicks on the buttons.
   @see fireAddEditEvent
   */
  public void addActionListener(ActionListener l) {
     listeners.addElement(l);
  }

  /**
   * Remove a listener for clicks on the buttons
   @see fireAddEditEvent
   */
  public void removeActionListener(ActionListener l) {
     listeners.removeElement(l);
  }

  /**
   * Fires an action event from the vector list. This method is called when
   * one of the Add, Remove, Edit or Move buttons is clicked by the user.
   @see addActionListener
   @param e An ActionEvent with the Action Command ADD_ACTION, EDIT_ACTION,
     MOVE_ACTION or REMOVE_ACTION. In all cases (except ADD_ACTION), the source
     of the ActionEvent should be the list item that was selected when the
     event was triggered
   @deprecated use fireCommand() instead.
   */
  public void fireAddEditEvent(ActionEvent e) {
     fireCommand(e);
  }

  /**
   * Fires an action event from the vector list. This method is called when
   * one of the Add, Remove, Edit or Move buttons is clicked by the user.
   @see addActionListener
   @param e An ActionEvent with the Action Command ADD_ACTION, EDIT_ACTION,
     MOVE_ACTION or REMOVE_ACTION. In all cases (except ADD_ACTION), the source
     of the ActionEvent should be the list item that was selected when the
     event was triggered
   */
  public void fireCommand(ActionEvent e) {
     Vector list = (Vector) listeners.clone();
     for (int i = 0; i < list.size(); i++) {
        ActionListener listener = (ActionListener)list.elementAt(i);
        listener.actionPerformed(e);
     }
  }

  /**
   * You can add a ListSelectionListener to listen out for selection events
   * on the list.
   */
  public void addListSelectionListener(ListSelectionListener l) {
     listList.addListSelectionListener(l);
  }

  public void removeListSelectionListener(ListSelectionListener l) {
     listList.removeListSelectionListener(l);
  }

  /**
   * Get the add button.
   @return a JButton that you can customize (e.g. change text, icon etc.)
   */
  public JButton getAddButton() {
     return cmdAdd;
  }

  /**
   * Get the edit button.
   @return a JButton that you can customize (e.g. change text, icon etc.)
   */
  public JButton getEditButton() {
     return cmdEdit;
  }

  /**
   * Get the move up button.
   @return a JButton that you can customize (e.g. change text, icon etc.)
   */
  public JButton getMoveUpButton() {
     return cmdMoveUp;
  }


  /**
   * Get the move down button.
   @return a JButton that you can customize (e.g. change text, icon etc.)
   */
  public JButton getMoveDownButton() {
     return cmdMoveDown;
  }

  /**
   * Get the remove button.
   @return a JButton that you can customize (e.g. change text, icon etc.)
   */
  public JButton getRemoveButton() {
     return cmdRemove;
  }

  public ListCellRenderer getCellRenderer() {
     return listList.getCellRenderer();
  }
  
/******************
 * EVENT HANDLING *
 ******************/

  class AddActionAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        fireAddEditEvent(new ActionEvent(VectorList.this,
           ActionEvent.ACTION_PERFORMED, ADD_ACTION));
     }
  }

  class EditActionAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        fireAddEditEvent(new ActionEvent(getSelectedItem(),
           ActionEvent.ACTION_PERFORMED, EDIT_ACTION));
     }
  }

  class RemoveActionAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        Object deletedItem = getSelectedItem();
        ((DefaultListModel)listList.getModel()).removeElement(getSelectedItem());
        listList.revalidate();
        repaint();
        if (listList.getSelectedIndex() == -1 || lmList.size() == 0) {
           cmdEdit.setEnabled(false);
           cmdRemove.setEnabled(false);
           cmdMoveUp.setEnabled(false);
           cmdMoveDown.setEnabled(false);
        }
        fireAddEditEvent(new ActionEvent(deletedItem,
           ActionEvent.ACTION_PERFORMED, REMOVE_ACTION));
     }
  }

  class MoveUpActionAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        int oldIndex = listList.getSelectedIndex();
        if (oldIndex > 0) {
           Object oldItem = lmList.elementAt(oldIndex);
           lmList.removeElementAt(oldIndex);
           lmList.insertElementAt(oldItem, oldIndex-1);
           if (oldIndex == 1) // now at the top
              cmdMoveUp.setEnabled(false);
           listList.setSelectedIndex(oldIndex-1);
           repaint();
        }
        fireAddEditEvent(new ActionEvent(getSelectedItem(),
           ActionEvent.ACTION_PERFORMED, MOVE_ACTION));
     }
  }

  class MoveDownActionAdapter implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        int oldIndex = listList.getSelectedIndex();
        if (oldIndex < lmList.size()-1) {
           Object oldItem = lmList.elementAt(oldIndex);
           lmList.removeElementAt(oldIndex);
           lmList.insertElementAt(oldItem, oldIndex+1);
           if (oldIndex == lmList.size()-2) // now at the bottom
              cmdMoveDown.setEnabled(false);
           listList.setSelectedIndex(oldIndex+1);
           repaint();
        }
        fireAddEditEvent(new ActionEvent(getSelectedItem(),
           ActionEvent.ACTION_PERFORMED, MOVE_ACTION));
     }
  }

  class ListListener implements ListSelectionListener {
     public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
           boolean sel = (e.getFirstIndex() >= 0);
           cmdEdit.setEnabled(sel);
           cmdRemove.setEnabled(sel);
           cmdMoveUp.setEnabled(sel);
           cmdMoveDown.setEnabled(sel);
           if (e.getFirstIndex() == 0) cmdMoveUp.setEnabled(false);
           if (e.getFirstIndex() == lmList.size()-1) cmdMoveDown.setEnabled(false);
        } //if
        // Disable buttons that require a selection if there is no current
        // selection.
        if (e.getFirstIndex() == -1) {
           cmdMoveUp.setEnabled(false);
           cmdMoveDown.setEnabled(false);
           cmdEdit.setEnabled(false);
           cmdRemove.setEnabled(false);
        }
     } // valueChanged
  }  // inner class ListListener

  class DoubleClickListener extends MouseAdapter {
     public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
           if (getSelectedItem() != null)
              fireAddEditEvent(new ActionEvent(getSelectedItem(),
                 ActionEvent.ACTION_PERFORMED, EDIT_ACTION));
        }
     }
  }
}
