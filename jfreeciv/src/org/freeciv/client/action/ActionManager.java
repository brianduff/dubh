package org.freeciv.client.action;

import org.freeciv.client.Client;
import org.freeciv.client.panel.QuickCommand;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JDesktopPane;
import javax.swing.JMenuItem;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.Component;

/**
 * This class manages all AbstractClientActions and their 
 * appropriate accelerators. It is especially important
 * for those ACActions which dont have representing MenuItems, 
 * like UACTMove*, as they hace no accelerators provided by
 * the MenuItems.
 *
 * @author Julian Rüth
 */
public final class ActionManager
{

  /**
   * The only instance of an ActionManager.
   *
   */
  public final static ActionManager actionManager=new ActionManager();

  /**
   * A map which relates KeyStrokes to ACActions.
   *
   */
  protected final AccelMap accelMap = new AccelMap();

  /**
   * A KeyListener which allows accelerations without using MenuItems.
   *
   */
  protected final class AccelListener implements KeyListener
  {
    /**
     * After some keyEvent this method is called. It looks up the 
     * appropriate ACAction for the keyCode and tests if this keyCode
     * is already handled by some regular acceleration (JMenuItem).
     * Otherwise it calls the actionPerformed() of the ACAction.
     *
     */
    public void keyPressed(KeyEvent e)
    {
      int mod = e.getModifiers();
      int code= e.getKeyCode();
      KeyStroke key = KeyStroke.getKeyStroke( code , mod );
      AbstractClientAction act = accelMap.getAction( key );
      //No ACAction for this key.
      if( act == null )
	return;
      //Get all components of this ACAction
      Set set = act.getComponents();
      Iterator it = set.iterator();
      while(it.hasNext())
      {
	Object o = it.next();
	//Is this key already handled by some other acceleration?
	if( o instanceof JMenuItem && ((JMenuItem)o).getAccelerator() == key )
	  return;
      }
      //Call the ACAction
      ActionEvent ae = new ActionEvent(this, e.getID() , "");
      act.actionPerformed(ae);
    }
    public void keyTyped(KeyEvent e)
    {
    }
    public void keyReleased(KeyEvent e)
    {
    }
  }

  public final AccelListener createAccelListener(){
    return new AccelListener();
  }

  /**
   * Adds an ACAction to the ActionManager.
   *
   */
  public void add(AbstractClientAction act)
  {
    accelMap.add(act);
  }

  /**
   * A map that relates KeyStrokes to ACActions.
   *
   */
  protected final class AccelMap
  {
    protected final Map map = new HashMap();
    public final void add(AbstractClientAction act)
    {
      Iterator it=act.accelerators.iterator();
      while( it.hasNext() )
	map.put(it.next(),act);
    }
    public final AbstractClientAction getAction(KeyStroke key)
    {
      return (AbstractClientAction)map.get(key);
    }
  }
  private ActionManager(){}
}
