package org.freeciv.client;

import java.io.InputStream;
import java.io.IOException;

import org.freeciv.common.Registry;
import org.freeciv.common.RegistryParseException;

/**
 * Utility class that parses the helpdata.txt format used by freeciv
 *
 * @author Brian Duff
 */
class HelpDataParser 
{
  private Registry m_registry;

  /**
   * Construct a help data parser.
   *
   * @param helpStream input stream for the help data file
   * @throws java.io.IOException if an error occurred reading the help file
   * @throws org.freeciv.common.RegistryParseException if the help file
   *    failed to parse
   */
  HelpDataParser( InputStream helpStream )
    throws IOException, RegistryParseException
  {
    m_registry = new Registry();
    m_registry.load( helpStream );
  }

  /**
   * Get the name of the topic with the specified key
   *
   * @param topicKey the topic key
   * @return a human readable title for the topic
   */
  String getTopicName( String topicKey )
  {
    return m_registry.lookupString( topicKey + ".name" );
  }

  /**
   * Get the text of the topic with the specified key
   *
   * @param topicKey the topic key
   * @return the entire text of the specified topic
   */
  String getTopicText( String topicKey )
  {
    String[] topicText = m_registry.lookupStringList( topicKey + ".text" );

    int totalLength = 0;
    for ( int i=0; i < topicText.length; i++ )
    {
      totalLength += topicText[i].length() + 2;
    }
    StringBuffer retString = new StringBuffer( totalLength );
    for ( int i = 0; i < topicText.length; i++ )
    {
      retString.append( topicText[i] );
      retString.append( "\n\n" );
    }
    return retString.toString();
  }
}