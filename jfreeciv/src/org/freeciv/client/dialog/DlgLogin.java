package org.freeciv.client.dialog;
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
