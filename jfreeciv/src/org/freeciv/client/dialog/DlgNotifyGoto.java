package org.freeciv.client.dialog;
import javax.swing.JFrame;
import org.freeciv.client.Client;
/**
 * A notification dialog that allows you to go to a particular square.
 */
public interface DlgNotifyGoto
{
  public void display( Client c, JFrame f, String message, int x, int y );
}
