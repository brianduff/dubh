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
   * This returns the city currently being displayed.
   * 
   * @return the city currently being displayed.
   */
  public City getCity();
  
  /**
   * Refresh this dialog with new information from the client
   */
  public void refresh();
  
  /**
   * Is this dialog showing?
   */
  public boolean isShowing();
  
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
