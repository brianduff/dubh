package org.freeciv.client.dialog;
/**
 * The main interface to the military (units) report dialog.
 * 
 * @author Ben Mazur
 */
public interface DlgMilitaryReport
{
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
  public void display();
  /**
   * Programmatically hide the dialog. This will unblock the AWT event thread.
   * N.b. The dialog will hide itself when buttons are pressed, this method
   * should be used to hide the dialog in response to an external event (usually
   * a received packet).
   */
  public void undisplay();
}
