package oracle.jdevimpl.propedit;

import java.net.URL;
import java.util.ArrayList;

/**
 * PropertiesModel provides a view on to one or more related properties files
 * in the same directory with different locales.
 *
 * @author Brian.Duff@oracle.com
 */
public final class PropertiesModel 
{
  private final URL m_url;
  private final List m_keys = new ArrayList();
  private final List m_values = new ArrayList();

  /**
   * Construct a properties model
   *
   * @param defaultURL the URL of the default properties file
   */
  public PropertiesModel( URL url )
  {
    m_url = url;
  }

  /**
   * Load the properties model from the specified input stream.
   *
   * @param instream a stream to load into this model
   * @throws java.io.IOException if an I/O error occurs while reading.
   */
  public void load( InputStream instream ) throws IOException
  {
    final BufferedReader in = new BufferedReader( 
      new InputStreamReader( instream, "8859_1" )
    );

    
  }

}