package org.freeciv.client.dialog;

import org.freeciv.common.City;

/**
 * The main interface to the city view dialog.  In the C Client this dialog
 * is non-modal, and furthermore, multiple instances (with multiple cities)
 * can be open at a time.  This interface will have to change for that to be
 * supported.
 * 
 * @author Ben Mazur
 */
public interface DlgCityView
{
  /**
   * This returns the city currently being displayed.  Theoretically, 
   * it should be null if the dialog is hidden.  Don't count on that,
   * tho.
   * 
   * @return the city currently being displayed.
   */
  public abstract City getCity();
  
  /**
   * Tells the city view that new city info has been sent and to update
   * accordingly.
   */
  public abstract void updateAll();
  
  /**
   * Display the dialog. The dialog is modal on the AWT event thread, i.e.
   * it will block all UI, but the packet handling thread will proceed.
   */
  public void display( City city );
  /**
   * Programmatically hide the dialog. This will unblock the AWT event thread.
   * N.b. The dialog will hide itself when buttons are pressed, this method
   * should be used to hide the dialog in response to an external event (usually
   * a received packet).
   */
  public void undisplay();
}
