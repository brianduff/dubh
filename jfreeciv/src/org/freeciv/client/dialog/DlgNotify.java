package org.freeciv.client.dialog;
/**
 * The main interface to the notify dialog.  Notify dialog is a pretty ambigous
 * name, but it's what the C client uses.
 * 
 * @author Ben Mazur
 */
public interface DlgNotify
{
  /**
   * Display the dialog. The dialog is modal on the AWT event thread, i.e.
   * it will block all UI, but the packet handling thread will proceed.
   * 
   * @param caption the window title
   * @param headline displayed above the text box, non-scrolling
   * @param lines text to display in a scrolling box
   */
  public void display( String caption, String headline, String lines );
  /**
   * Programmatically hide the dialog. This will unblock the AWT event thread.
   * N.b. The dialog will hide itself when buttons are pressed, this method
   * should be used to hide the dialog in response to an external event (usually
   * a received packet).
   */
  public void undisplay();
}
