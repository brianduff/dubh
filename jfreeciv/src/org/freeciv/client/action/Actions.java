package org.freeciv.client.action;

import org.freeciv.client.Client;
import java.util.HashMap;

public class Actions
{
  private Client m_client;
  private HashMap m_mapActions;
  
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
  public AbstractClientAction getAction( Class clazz )
  {
    AbstractClientAction a = (AbstractClientAction)m_mapActions.get( clazz );
    if( a == null )
    {
      try
      {
        a = (AbstractClientAction)clazz.newInstance();
        a.setClient( m_client );
        m_mapActions.put( clazz, a );
      }
      catch( Exception e )
      {
        throw new RuntimeException( "Couldn't find action " + clazz + " (exception was " + e + ")" );
      }
    }
    return a;
  }
  public java.util.Collection getAllActions()
  {
   return m_mapActions.values(); // TODO: clone !!!
  }
}
