package org.freeciv.client;

/**
 * This is the exposed interface to the client help system.
 *
 * @author Brian Duff
 */
public interface HelpSystem 
{
  /**
   * Show the help system at one of the static topics. The static topics
   * are those contained in the helpdata.txt file that ships with 
   * freeciv. If the topicName is not found, an alert is displayed indicating
   * that there is no help for the selected item.
   *
   * @param topicName
   */
  public void showStaticTopic( String topicName );
}