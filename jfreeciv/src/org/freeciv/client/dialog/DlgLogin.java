package org.freeciv.client.dialog;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFrame;
import org.freeciv.client.Client;
/**
 * The main interface to the login dialog
 */
public interface DlgLogin
{
  /**
   * Actually display the dialog
   */
  public void display();
  /**
   * Returns true if the dialog was dismissed successfully
   */
  public boolean isOK();

}
