package org.freeciv.client.dialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import org.freeciv.client.Client;
import org.freeciv.client.action.AbstractClientAction;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.client.ui.util.BaseDialog;


/**
 * Implementation of Change key bindings panel.
 *
 * @author Justin
 * @author Brian Duff (dubh@dubh.org)
 */
class ImplChangeKeyBindings extends VerticalFlowPanel
   implements DlgChangeKeyBindings
{
 private Client m_client;
 private DialogManager m_dlgManager;
 private final ActionsTableModel m_actionsTableModel = new ActionsTableModel();
 private AbstractClientAction m_selectedAction;
 private javax.swing.KeyStroke m_KeyStrokeSelected;
 private BaseDialog m_dialog;
 private final JPanel m_panButtons = new JPanel();
 private final JLabel m_labelKeyBinding = new JLabel( translate("Key binding:") );
 private final JTextField m_TextFieldKeyBinding = new JTextField(20);
 private final JButton m_ButtonChangeKeyBinding = new JButton(translate("Change"));
 private final JTable m_TableActions = new JTable(m_actionsTableModel);

 public ImplChangeKeyBindings(DialogManager mgr, Client c)
 {
  m_client = c;
  m_dlgManager = mgr;

  setupActionsTable();
  setupKeyBindingPanel();
 }

  public void display()
  {
    BaseDialog dlg = BaseDialog.createDialog(
      m_client.getMainWindow(), translate( "Key Bindings" ) );
    dlg.setContent( this );

    m_dialog = dlg;

    resetActionsList();

    // Fixme. we should either support OK/ cancel properly, or only have
    // one button.

    m_dialog.getOKButton().addActionListener( new ActionListener() {
       public void actionPerformed( ActionEvent e )
       {
        //actionChange();
        undisplay();
       }
      } );

    m_dialog.getCancelButton().addActionListener( new ActionListener() {
       public void actionPerformed( ActionEvent e )
       {
        undisplay();
       }
      } );


    m_dlgManager.showDialog( m_dialog );
  }

 public void undisplay()
 {
  m_dlgManager.hideDialog( m_dialog );
 }

 /**
  */
 void resetActionsList()
 {
  m_actionsTableModel.refresh();
 }

 /**
  */
 void setSelectedKeyStroke(KeyStroke keystroke)
 {
  m_KeyStrokeSelected = keystroke;

  StringBuffer element = new StringBuffer();
  if( m_KeyStrokeSelected != null )
  {
   int modifiers = m_KeyStrokeSelected.getModifiers();
   int keycode = m_KeyStrokeSelected.getKeyCode();
   if( modifiers != 0 )
   {
    element.append(KeyEvent.getKeyModifiersText(modifiers));
    element.append("+");
   }
   element.append(KeyEvent.getKeyText(keycode));
  }
  m_TextFieldKeyBinding.setText(element.toString());
 }

 /**
  * Initialization function; sets up the city list panel and adds it to the
  * main dialog panel.
  */
 private void setupActionsTable()
 {
  m_TableActions.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  m_TableActions.getSelectionModel().addListSelectionListener(
   new javax.swing.event.ListSelectionListener()
   {
    public void valueChanged(ListSelectionEvent e)
    {
     if(!e.getValueIsAdjusting())
     {
      m_selectedAction = null;
      setSelectedKeyStroke(null);
      int row = m_TableActions.getSelectedRow();
      if(row >= 0)
      {
       m_selectedAction = m_actionsTableModel.getAction(row);
       if(m_selectedAction != null)
       {
        javax.swing.KeyStroke keystroke = m_selectedAction.getFirstAccelerator();
        setSelectedKeyStroke(keystroke);
       } // if(action!=null)
      } // if(row>=0)
     } // if
    }
   });
  this.addSpacerRow(new javax.swing.JScrollPane(m_TableActions));
 }



 /**
  */
 private void setupKeyBindingPanel()
 {
  JPanel panel = new JPanel();
  FlowLayout fl = new FlowLayout( SwingConstants.WEST, 5, 5 );
  panel.setLayout( fl );

  panel.add( m_labelKeyBinding  );
  m_labelKeyBinding.setLabelFor( m_TextFieldKeyBinding );


  m_TextFieldKeyBinding.addKeyListener(new KeyListener(){
     public void keyPressed(KeyEvent e)
     {
      setSelectedKeyStroke(KeyStroke.getKeyStrokeForEvent(e));
     }
     public void keyReleased(KeyEvent e)
     {
     }
     public void keyTyped(KeyEvent e)
     {
      e.consume();
     }
    });
  panel.add(m_TextFieldKeyBinding);

  m_ButtonChangeKeyBinding.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e)
     {
      if( m_selectedAction != null)
      {
       m_selectedAction.setFirstAccelerator(m_KeyStrokeSelected.getKeyCode(), m_KeyStrokeSelected.getModifiers());
       m_actionsTableModel.fireTableDataChanged();
      }
     }
    });
  panel.add(m_ButtonChangeKeyBinding);

  this.addRow( panel );
 }

 private class ActionsTableModel extends AbstractTableModel
 {
  List m_actions;
  private String[] m_columns = {translate("Action"), translate("Key Binding")};

  public void refresh()
  {
   if( m_actions != null )
   {
    int size = m_actions.size();
    m_actions = null;
    fireTableRowsDeleted(0, size);
   }
   m_actions = new java.util.ArrayList(m_client.getAllActions());
   Collections.sort(m_actions, new Comparator() {
      public int compare(Object o1, Object o2)
      {
       String name1 = ((AbstractClientAction)o1).getName();
       String name2 = ((AbstractClientAction)o2).getName();
       return name1.compareTo(name2);
      }
     });
   fireTableRowsInserted(0, m_actions.size());
  }

  public AbstractClientAction getAction(int row)
  {
   AbstractClientAction action = null;
   if(m_actions != null )
   {
    action = (AbstractClientAction)m_actions.get(row);
   }
   return action;
  }

  public int getRowCount()
  {
   return m_actions!=null?m_actions.size():0;
  }

  public Object getValueAt(int row, int col)
  {
   AbstractClientAction action = getAction(row);
   if(action != null)
   {
    switch(col)
    {
     case 0:
      return action.getName();
     case 1:
      StringBuffer element = new StringBuffer();
      KeyStroke keystroke = action.getFirstAccelerator();
      if( keystroke != null )
      {
       int modifiers = keystroke.getModifiers();
       if( modifiers != 0 )
       {
        element.append(KeyEvent.getKeyModifiersText(modifiers));
        element.append("+");
       }
       element.append(KeyEvent.getKeyText(keystroke.getKeyCode()));
      }
      return element.toString();
     default:
      throw new IllegalStateException( "Invalid column" );
    }
   }
   else return null;
  }

  public int getColumnCount()
  {
   return m_columns.length;
  }

  public String getColumnName(int column)
  {
   return m_columns[column];
  }
 }

 // localization
 private static String translate( String txt )
 {
  return org.freeciv.util.Localize.translate( txt );
 }
}
