package org.freeciv.client.dialog;


/**
 * Implementation of Change key bindings panel.
 *
 * @author Justin
 */
class ImplChangeKeyBindings extends org.freeciv.client.dialog.util.VerticalFlowPanel
   implements DlgChangeKeyBindings
{
 private org.freeciv.client.Client m_client;
 private DialogManager m_dlgManager;
 private ActionsTableModel m_actionsTableModel = new ActionsTableModel();
 private org.freeciv.client.action.AbstractClientAction m_selectedAction;
 private javax.swing.KeyStroke m_KeyStrokeSelected;
 javax.swing.JDialog m_dialog;
 javax.swing.JPanel m_panButtons = new javax.swing.JPanel();
 javax.swing.JTextField m_TextFieldKeyBinding = new javax.swing.JTextField(20);
 javax.swing.JButton m_ButtonChangeKeyBinding = new javax.swing.JButton(_("Change"));
 javax.swing.JButton m_butOK = new javax.swing.JButton(_("OK"));
 javax.swing.JButton m_butCancel = new javax.swing.JButton(_("Cancel"));
 javax.swing.JTable m_TableActions = new javax.swing.JTable(m_actionsTableModel);
 
 public ImplChangeKeyBindings(DialogManager mgr, org.freeciv.client.Client c)
 {
  m_client = c;
  m_dlgManager = mgr;
  
  setupActionsTable();
  setupKeyBindingPanel();
  setupButtonPanel();
 }
 
 public void display()
 {
  javax.swing.JDialog dlg = new javax.swing.JDialog( m_client.getMainWindow(), _( "Change key bindings" ), true );
  dlg.getContentPane().setLayout( new java.awt.BorderLayout() );
  dlg.getContentPane().add( this, java.awt.BorderLayout.CENTER );
  m_dialog = dlg;
  
  resetActionsList();
  
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
 void setSelectedKeyStroke(javax.swing.KeyStroke keystroke)
 {
  m_KeyStrokeSelected = keystroke;
  
  StringBuffer element = new StringBuffer();
  if( m_KeyStrokeSelected != null )
  {
   int modifiers = m_KeyStrokeSelected.getModifiers();
   int keycode = m_KeyStrokeSelected.getKeyCode();
   if( modifiers != 0 )
   {
    element.append(java.awt.event.KeyEvent.getKeyModifiersText(modifiers));
    element.append("+");
   }
   element.append(java.awt.event.KeyEvent.getKeyText(keycode));
  }
  m_TextFieldKeyBinding.setText(element.toString());
 }
 
 /**
  * Initialization function; sets up the city list panel and adds it to the
  * main dialog panel.
  */
 private void setupActionsTable()
 {
  m_TableActions.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
  m_TableActions.getSelectionModel().addListSelectionListener(
   new javax.swing.event.ListSelectionListener()
   {
    public void valueChanged(javax.swing.event.ListSelectionEvent e)
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
  * Initialization function; sets up the button panel and adds it to the
  * main dialog panel.
  */
 private void setupButtonPanel()
 {
  m_butOK.addActionListener( new java.awt.event.ActionListener() {
     public void actionPerformed( java.awt.event.ActionEvent e )
     {
      //actionChange();
      undisplay();
     }
    } );
  
  m_butCancel.addActionListener( new java.awt.event.ActionListener() {
     public void actionPerformed( java.awt.event.ActionEvent e )
     {
      undisplay();
     }
    } );
  
  m_panButtons.setLayout( new java.awt.FlowLayout() );
  m_panButtons.add( m_butOK );
  m_panButtons.add( m_butCancel );
  this.addRow( m_panButtons );
 }
 
 /**
  */
 private void setupKeyBindingPanel()
 {
  javax.swing.JPanel panel = new javax.swing.JPanel();
  
  panel.setBorder(javax.swing.BorderFactory.createTitledBorder(_("Key Binding")));
  
  m_TextFieldKeyBinding.addKeyListener(new java.awt.event.KeyListener(){
     public void keyPressed(java.awt.event.KeyEvent e)
     {
      setSelectedKeyStroke(javax.swing.KeyStroke.getKeyStrokeForEvent(e));
     }
     public void keyReleased(java.awt.event.KeyEvent e)
     {
     }
     public void keyTyped(java.awt.event.KeyEvent e)
     {
      e.consume();
     }
    });
  panel.add(m_TextFieldKeyBinding);
  
  m_ButtonChangeKeyBinding.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(java.awt.event.ActionEvent e)
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
 
 private class ActionsTableModel extends javax.swing.table.AbstractTableModel
 {
  java.util.List m_actions;
  private String[] m_columns = {_("Action"), _("Key Binding")};
  
  public void refresh()
  {
   if( m_actions != null )
   {
    int size = m_actions.size();
    m_actions = null;
    fireTableRowsDeleted(0, size);
   }
   m_actions = new java.util.ArrayList(m_client.getAllActions());
   java.util.Collections.sort(m_actions, new java.util.Comparator() {
      public int compare(Object o1, Object o2)
      {
       String name1 = ((org.freeciv.client.action.AbstractClientAction)o1).getName();
       String name2 = ((org.freeciv.client.action.AbstractClientAction)o2).getName();
       return name1.compareTo(name2);
      }
     });
   fireTableRowsInserted(0, m_actions.size());
  }
  
  public org.freeciv.client.action.AbstractClientAction getAction(int row)
  {
   org.freeciv.client.action.AbstractClientAction action = null;
   if(m_actions != null )
   {
    action = (org.freeciv.client.action.AbstractClientAction)m_actions.get(row);
   }
   return action;
  }
  
  public int getRowCount()
  {
   return m_actions!=null?m_actions.size():0;
  }
  
  public Object getValueAt(int row, int col)
  {
   org.freeciv.client.action.AbstractClientAction action = getAction(row);
   if(action != null)
   {
    switch(col)
    {
     case 0:
      return action.getName();
     case 1:
      StringBuffer element = new StringBuffer();
      javax.swing.KeyStroke keystroke = action.getFirstAccelerator();
      if( keystroke != null )
      {
       int modifiers = keystroke.getModifiers();
       if( modifiers != 0 )
       {
        element.append(java.awt.event.KeyEvent.getKeyModifiersText(modifiers));
        element.append("+");
       }
       element.append(java.awt.event.KeyEvent.getKeyText(keystroke.getKeyCode()));
      }
      return element.toString();
     default:
      return "not implemented";
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
 private static String _( String txt )
 {
  return org.freeciv.util.Localize.translate( txt );
 }
}
