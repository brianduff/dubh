package org.freeciv.client.ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.freeciv.util.Localize;

/**
 * Dialog implementation - a subclass of JDialog making it easy to create
 * consistent dialogs.
 * 
 * @author Brian Duff (dubh@dubh.org)
 */
public class BaseDialog extends JDialog
{
  /**
   * The content panel
   */
  private Component m_contentPanel;
  private JButton m_okButton;
  private JButton m_cancelButton;
  private final JPanel m_buttonBar = new JPanel();
  
  // instance initializer - this is just to avoid duplicate code in the
  // constructors. 
  {
    m_buttonBar.setLayout( new ButtonBarLayout() );
    
    getContentPane().setLayout( new BorderLayout( 5, 5 ) );
    getContentPane().add( m_buttonBar, BorderLayout.SOUTH );
    ((JComponent)getContentPane()).setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
  }

  private BaseDialog( Dialog parent, String title )
  {
    super( parent, title, true );
  }
  
  private BaseDialog( Frame parent, String title )
  {
    super( parent, title, true );
  }

  public static BaseDialog createDialog( Component parentComponent, 
    String title )
  {
    return createDialog( parentComponent, title, true );
  }

  /**
   * Create a base dialog with the specified component as its parent, and 
   * OK / Cancel buttons by default.
   * 
   * @param parentComponent the parent component of the dialog. The ancestors
   *    of this component will be traversed until a Dialog or Frame is found
   *    to use as the parent of the new dialog.
   * @param title the titlebar for the dialog
   * 
   * @return a new BaseDialog instance.
   */
  public static BaseDialog createDialog( Component parentComponent, 
    String title, boolean okCancelButtons )
  {
    BaseDialog bd = null;
    Component c = parentComponent;
    while ( true )
    {
      if ( c instanceof Dialog )
      {
        bd = new BaseDialog( (Dialog)c, title );
        break;
      }
      c = c.getParent();
      if ( c == null )
      {
        break;
      }      
    }
    
    if ( bd == null )
    {
      c = parentComponent;
      while ( true )
      {
        if ( c instanceof Frame )
        {
          bd = new BaseDialog( (Frame)c, title );
          break;
        }
        c = c.getParent();
        if ( c == null )
        {
          break;
        }
      }
    }
    
    if ( bd == null )
    {
    
      // Must provide a parent, otherwise modality is messed up.
      assert( false );
      
      return null;
    }
    
    if ( okCancelButtons )
    {
      bd.m_okButton = new JButton( _("OK") );
      bd.m_cancelButton = new JButton( _("Cancel") );
      
      bd.addButton( bd.m_cancelButton );
      bd.addButton( bd.m_okButton );
      
//      bd.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
//      
//  
//      bd.addWindowListener( new WindowAdapter() 
//      {
//        public void windowClosing( WindowEvent we )
//        {
//          try
//          {
//            bd.m_cancelButton.doClick();
//          }
//          catch (RuntimeException re)
//          {
//            ErrorHandler.getHandler().internalError( re );
//          }
//        }
//      });      
    }
    
    return bd;

  }
  
  /**
   * Add a custom button to the button bar in this dialog. The button will 
   * be added to the left of the default buttons (OK and Cancel)
   */
  public void addButton( JButton button )
  {
    m_buttonBar.add( button );
    m_buttonBar.validate();
  }
  
  /**
   * Set the content of the dialog.
   * 
   * @param content a UI control that is the main "content" area of the dialog.
   */
  public void setContent( Component content )
  {
    if ( m_contentPanel != null )
    {
      getContentPane().remove( m_contentPanel );
    }
    m_contentPanel = content;
    getContentPane().add( m_contentPanel, BorderLayout.CENTER );
    getContentPane().validate();
  }
  
  
  
  public JButton getOKButton()
  {
    return m_okButton;
  }
  
  public JButton getCancelButton()
  {
    return m_cancelButton;
  }
  
  /**
   * Run the dialog, returning true or false.
   */
  public boolean runDialog()
  {
    return false;
  }
  
  // localization
  private static String _( String txt )
  {
    return Localize.translate( txt );
  }  
}