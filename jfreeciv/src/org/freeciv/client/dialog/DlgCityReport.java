package org.freeciv.client.dialog;
public interface DlgCityReport
{
  /**
   * Refresh this dialog with new information from the client
   */
  public void refresh();
  
  /**
   * Is this dialog showing?
   */
  public boolean isShowing();
  
  public void display();
  public void undisplay();
}