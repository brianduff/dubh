package org.freeciv.client.dialog;
public interface DlgProgress
{
  public void display( String baseMessage, int numSteps );
  public void undisplay();
  public void updateProgress( ProgressItem pi );
}
