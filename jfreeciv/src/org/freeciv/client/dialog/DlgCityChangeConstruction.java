package org.freeciv.client.dialog;

import org.freeciv.common.City;

/**
 * The main interface to city change construction dialog.  This would be
 * normally opened from the City View dialog, but there's no real reason
 * that is can't be opened anytime.
 * 
 * @author Ben Mazur
 */
public interface DlgCityChangeConstruction
{
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
