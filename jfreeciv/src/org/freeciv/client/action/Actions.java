package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.util.HashMap;
public class Actions
{
  private Client m_client;
  private HashMap m_mapActions;
  private static final String PACKAGE = "org.freeciv.client.action.";
  public Actions( Client c ) 
  {
    m_client = c;
    m_mapActions = new HashMap();
    // Just testing
    ACTLocalOptions o = new ACTLocalOptions();
    o.setClient( c );
    m_mapActions.put( "ACTLocalOptions", o );
  }
  /**
   * Gets the named action
   */
  public AbstractClientAction getAction( String name )
  {
    AbstractClientAction a = (AbstractClientAction)m_mapActions.get( name );
    if( a == null )
    {
      String fullName = PACKAGE + name;
      try
      {
        Class c = Class.forName( fullName );
        a = (AbstractClientAction)c.newInstance();
        a.setClient( m_client );
        m_mapActions.put( name, a );
      }
      catch( Exception e )
      {
        throw new RuntimeException( "Couldn't find action " + name + " in " + PACKAGE + " (exception was " + e + ")" );
      }
    }
    return a;
  }
}
