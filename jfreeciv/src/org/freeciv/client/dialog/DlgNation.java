package org.freeciv.client.dialog;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFrame;
import org.freeciv.client.Client;
/**
 * The main interface to the nation dialog
 */
public interface DlgNation
{
  public void toggleAvailableRaces( int bits1, int bits2 );
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
  public int getNation();
  public String getLeaderName();
  public boolean isLeaderMale();
  public int getCityStyle();
}
