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

import javax.swing.*;
import java.util.*;
import dubh.utils.misc.*;
/**
 * A JMenuBar that can be constructed from a localized resource file. See
 * JMenuResource for more information.
 * @see JMenuResource
 *<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [28/06/98]: Initial Revision
 * <LI>0.2 [30/06/98]: Added getPopupMenu(name);
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.2 [30/06/98]
 */
public class JMenuBarResource extends JMenuBar {
  protected Hashtable m_listeners;
  protected ResourceBundle m_bundle;
  protected Object m_invoker=null;

  /**
   * Construct a menu bar using the specified resource
   */
  public JMenuBarResource(String bundleName, String menuBarResource,
        Hashtable listeners) {
     construct(bundleName, Locale.getDefault(), menuBarResource, listeners);
  }

  /**
   * Construct a menu bar using the specified resource. This version of the
   * constructor automagically creates event mappings between your main
   * class and a hashtable of DubhActions. You specify an Object to
   * construct such a menu bar. This Object must contain parameterless methods
   * that have the same names as the action commands in your properties file.
   * Any methods that don't exist will have disabled menu items.
   */
  public JMenuBarResource(String bundleName, String menuBarResource,
        Object invoker) {
     this(bundleName, Locale.getDefault(), menuBarResource, invoker);

  }

  public JMenuBarResource(String bundleName, Locale locale, String menuBarResource,
     Object invoker) {
     m_bundle = ResourceBundle.getBundle(bundleName, locale);
     m_listeners = new Hashtable();
     m_invoker = invoker;
     // Go through the menubar resource adding all menu items to the
     // hashtable.
     String[] menus = StringUtils.getWords(m_bundle.getString(menuBarResource));
     for (int i=0; i<menus.length; i++) {
        checkMenuActions(menus[i]);
     }
     construct(bundleName, locale, menuBarResource, m_listeners);     
  }

  private void construct(String bundleName, Locale locale, String menuBarResource,
        Hashtable listeners) {
     JMenuItem mi;
     JMenuBar mb = new JMenuBar();
     m_bundle = ResourceBundle.getBundle(bundleName, locale);

      String[] menuKeys = StringUtils.getWords(m_bundle.getString(menuBarResource));
      for (int i = 0; i < menuKeys.length; i++) {
         JMenu m = new JMenuResource(m_bundle, menuKeys[i], listeners);
         if (m != null)
            add(m);
      }
     m_listeners = listeners;
  }

  /**
   * Gets a popup menu from the resource bundle associated with this menu
   * bar. Use this method if you want the items in the popup menu to have the
   * same action objects as your main menu items. This will ensure that
   * actions become enabled / disabled at the right time.
   @param resource the resource name of the popupmenu to retrieve. It should
     be located in the same resource file as your menu bar.
   */
  public JPopupMenu getPopupMenu(String resource) {
     checkMenuActions(resource);
     return ((new JMenuResource(m_bundle, resource, m_listeners)).getPopupMenu());
  }

  /**
   * Gets a toolbar from the resource bundle associated with this menu bar.
   * Use this method if you want the items in the toolbar to have the same
   * action objects as your main menu items. This ensures that items become
   * enabled / disabled at the right time.
   @param resource the resource name of the toolbar to retrieve. It should be
     located in the same resource file as your menu bar.
   */
  public JToolBar getToolBar(String resource) {
     checkMenuActions(resource);
     return (JToolBar) new JToolBarResource(m_bundle, resource, m_listeners);
  }

  /**
   * Return the Action with the specified name
   @return an AbstractAction or null if the specified action doesn't exist.
   */
  public AbstractAction getAction(String actionCommand) {
     return (AbstractAction) m_listeners.get(actionCommand);
  }

  /**
   * Set whether an action is enabled. This has the result of disabling all
   * menu items / toolbar items associated with the action.
   */
  public void setActionEnabled(String actionCommand, boolean isEnabled) {
     AbstractAction tmp = (AbstractAction) m_listeners.get(actionCommand);
     if (tmp != null) tmp.setEnabled(isEnabled);
  }

  /**
   * Get an enumeration of the actions
   */
  public Enumeration getActions() {
     return m_listeners.elements();
  }

  /**
   * Given a resourceName, reads all the individual items and checks that
   * entries exist in the listener table for them. If not, create new
   * DubhActions for them.
   */
  private void checkMenuActions(String resourceName) {
     if (m_invoker != null) {
        String[] items = StringUtils.getWords(m_bundle.getString(resourceName));
        for (int j=0; j<items.length;j++) {
           if (!m_listeners.containsKey(items[j]))
              m_listeners.put(items[j], new DubhAction(m_invoker, items[j]));
        }
     }
  }
}