package org.freeciv.client;
import javax.swing.JComponent;
public interface HelpItem
{
  public String getHelpCategory();
  public String getHelpName();
  void setHelpText( String txt );
  JComponent getRenderer( HelpPanel help );
}
