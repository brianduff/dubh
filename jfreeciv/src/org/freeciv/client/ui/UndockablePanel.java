package org.freeciv.client.ui;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * An undockable panel is a panel that can be docked to a main window
 * or undocked to an external frame.
 *
 * The "Main window" is anything that implements the (inner) interface
 * DockTarget.
 *
 * You should use the {@link #setContent(String, JPanel, DockTarget)}
 * method to set the content panel of the undockable panel. You
 * can then use the {@link #getMainPanel()} method to retrieve a panel
 * that contains the content panel as well as a control that the user
 * can use to undock the panel. The panel will always be in the docked
 * state originally, until the undocking control is used.
 *
 * @author Brian Duff
 */
public class UndockablePanel
{
  private Component m_panContent;
  private JPanel m_panMain;
  private JButton m_butUndockMe;
  private DockTarget m_dtTarget;
  private String m_strName;
  private JFrame m_fraUndockedWindow;
  private boolean m_bDocked = true;
  /**
   * Construct an undockable panel. The
   * {@link #setContent(String, JPanel, DockTarget)} method should
   * be used to set the content of the panel.
   */
  public UndockablePanel() 
  {
    m_butUndockMe = new JButton();
    m_butUndockMe.setMargin( new Insets( 0, 0, 0, 0 ) );
    m_butUndockMe.setFocusPainted( false );
    m_butUndockMe.addActionListener( new DockButtonListener() );
    m_butUndockMe.setBackground( Color.darkGray );
    m_panMain = new JPanel();
    m_panMain.setLayout( new BorderLayout() );
    m_panMain.add( m_butUndockMe, BorderLayout.WEST );
  }
  /**
   * Sets the content of the panel.
   *
   * @param name The name that will be used as the title of the
   *   window when the panel is undocked.
   * @param pan The panel containing controls that you want to be
   *   undockable.
   * @param dt Anything that implements the DockTarget interface
   *   and can physically remove the main panel of this class
   *   from itself or add it when methods on the interface are called.
   */
  public void setContent( String name, Component pan, DockTarget targ )
  {
    if( m_panContent != null )
    {
      m_panMain.remove( m_panContent );
    }
    m_panContent = pan;
    m_panMain.add( m_panContent, BorderLayout.CENTER );
    m_panMain.invalidate();
    m_dtTarget = targ;
    m_strName = name;
  }
  /**
   * Get the main panel. This panel contains the content panel as
   * well as a control for undocking. You should add this panel (NOT
   * the content panel) to your main control, and add/remove it
   * whenever the undock() or dock() methods are called on your
   * DockTarget.
   */
  public JPanel getMainPanel()
  {
    return m_panMain;
  }
  /**
   * Programmatically docks the panel. If the panel is already docked,
   * this has no effect. If the panel is currently undocked, it asks
   * the dock target to dock the panel. If the dock target docks
   * sucessfully, the undock window is closed. If the dock target fails
   * to dock, the window remains on the screen.
   */
  public void dock()
  {
    if( m_bDocked )
    {
      return ;
    }
    if( m_fraUndockedWindow == null )
    {
      throw new RuntimeException( "The undock frame was never created" );
    }
    // First, unparent the main panel from the frame content pane.
    m_fraUndockedWindow.getContentPane().remove( getMainPanel() );
    if( m_dtTarget.dock( this ) )
    {
      m_fraUndockedWindow.setVisible( false );
      m_bDocked = true;
    }
    else
    {
      m_fraUndockedWindow.getContentPane().add( getMainPanel(), BorderLayout.CENTER );
    }
  }
  /**
   * Programmatically undocks the panel. If the panel is already undocked,
   * this has no effect. If the panel is currently docked, it asks the
   * dock target to undock the panel. If the dock target succeeds, the
   * panel is added to the undock window and the undock window is displayed.
   * If the dock target fails, nothing happens.
   */
  public void undock()
  {
    if( !m_bDocked )
    {
      return ;
    }
    if( m_dtTarget == null )
    {
      throw new RuntimeException( "Target is null" );
    }
    if( m_dtTarget.undock( this ) )
    {
      if( m_fraUndockedWindow == null )
      {
        m_fraUndockedWindow = new JFrame( m_strName );
        m_fraUndockedWindow.getContentPane().setLayout( new BorderLayout() );
        m_fraUndockedWindow.addWindowListener( new UndockWindowListener() );
      }
      m_fraUndockedWindow.getContentPane().add( getMainPanel(), BorderLayout.CENTER );
      m_fraUndockedWindow.pack();
      m_fraUndockedWindow.setVisible( true );
      m_bDocked = false;
    }
  }
  /**
   * I don't think this should be exposed.
   */
  protected void close()
  {
    if( m_bDocked )
    {
      return ;
    }
    if( m_dtTarget.close( this ) )
    {
      m_fraUndockedWindow.setVisible( false );
    }
  }
  /**
   * Listen for the undock window being closed
   */
  private class UndockWindowListener extends WindowAdapter
  {
    public void WindowClosed( WindowEvent e )
    {
      System.out.println( "Window closing" );
      close();
    }
  }
  /**
   * Listen for the dock button being clicked.
   */
  private class DockButtonListener implements ActionListener
  {
    public void actionPerformed( ActionEvent e )
    {
      if( m_bDocked )
      {
        undock();
      }
      else
      {
        dock();
      }
    }
  }
  /**
   * The object that the undockable panel can be docked and undocked
   * from should implement this interface.
   */
  public interface DockTarget
  {
    /**
     * Your target should do whatever it takes to remove the panel. If
     * the panel couldn't be undocked, you should return false.
     */
    public boolean undock( UndockablePanel pan );
    /**
     * Your target should do whatever it takes to add the panel. If
     * the panel couldn't be docked, you should return false.
     */
    public boolean dock( UndockablePanel pan );
    /**
     * This method is called if an undocked panel's window is closed.
     * You might want to redock the panel, or alternatively just
     * remember that it is missing so the user can reinstate it
     * at some point. You can return false to stop the window being
     * closed.
     */
    public boolean close( UndockablePanel pan );
  }
  // TEST CODE
  public static void main( String[] args )
  {
    MainFrame mf = new MainFrame( "Funky" );
    mf.pack();
    mf.setLocation( 100, 100 );
    mf.setVisible( true );
  }
  static class MainFrame extends JFrame implements UndockablePanel.DockTarget
  {
    private JPanel m_panStatic = new JPanel();
    private UndockablePanel m_panDockStuffWest = new UndockablePanel();
    private UndockablePanel m_panDockStuffEast = new UndockablePanel();
    public MainFrame( String title ) 
    {
      super( title );
      m_panStatic.setLayout( new BorderLayout() );
      m_panStatic.add( new JButton( "NORTH" ), BorderLayout.NORTH );
      m_panStatic.add( new JButton( "SOUTH" ), BorderLayout.SOUTH );
      m_panStatic.add( new JButton( "CENTER" ), BorderLayout.CENTER );
      JPanel undockOne = new JPanel();
      undockOne.setLayout( new BorderLayout() );
      undockOne.add( new JButton( "EAST" ), BorderLayout.EAST );
      undockOne.add( new JButton( "WEST" ), BorderLayout.WEST );
      m_panDockStuffWest.setContent( "West Panel", undockOne, this );
      JPanel undockTwo = new JPanel();
      undockTwo.setLayout( new BorderLayout() );
      undockTwo.add( new JButton( "EAST" ), BorderLayout.EAST );
      undockTwo.add( new JButton( "WEST" ), BorderLayout.WEST );
      m_panDockStuffEast.setContent( "East Panel", undockTwo, this );
      getContentPane().setLayout( new BorderLayout() );
      getContentPane().add( m_panStatic, BorderLayout.CENTER );
      getContentPane().add( m_panDockStuffWest.getMainPanel(), BorderLayout.WEST );
      getContentPane().add( m_panDockStuffEast.getMainPanel(), BorderLayout.EAST );
    }
    public boolean dock( UndockablePanel pan )
    {
      if( pan == m_panDockStuffWest )
      {
        getContentPane().add( pan.getMainPanel(), BorderLayout.WEST );
        getContentPane().validate();
      }
      else
      {
        if( pan == m_panDockStuffEast )
        {
          getContentPane().add( pan.getMainPanel(), BorderLayout.EAST );
          getContentPane().validate();
        }
      }
      return true;
    }
    public boolean undock( UndockablePanel pan )
    {
      getContentPane().remove( pan.getMainPanel() );
      getContentPane().validate();
      return true;
    }
    public boolean close( UndockablePanel pan )
    {
      dock( pan );
      return true;
    }
  }
}
