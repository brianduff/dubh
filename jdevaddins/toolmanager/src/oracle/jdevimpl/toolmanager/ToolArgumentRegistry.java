/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Oracle9i JDeveloper release 9.0.3
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */


package oracle.jdevimpl.toolmanager;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import oracle.ide.addin.Context;

/**
 * The ToolManager provides flexible support for argument types. A
 * ToolArgument is passed a context when a tool is invoked and is responsible
 * for translating this into a string to be passed into the tool.
 * <p>
 * All available ToolArguments are centrally managed by the
 * ToolArgumentRegistry. Use the getArguments() method to retrieve an
 * iterator over all registered ToolArguments.
 *
 * @author Brian.Duff@oracle.com
 */
public final class ToolArgumentRegistry
{
  private final Map m_registry;
  private final Map m_monikerMap;

  public ToolArgumentRegistry()
  {
    m_registry = new HashMap();
    m_monikerMap = new HashMap();
  }

  /**
   * Given a string containing zero or more monikers escaped by brackets
   * and a context, expand all monikers to their associated ToolArgument's
   * value for the context.
   *
   * Any unrecognized monikers or mismatched brackets are left unaltered
   * in the string.
   *
   * @param text the text to parse
   * @param context the context to expand
   * @return the expanded string
   */
  public String expandMonikers( final String text, final Context context )
  {
    StringBuffer outBuffer = new StringBuffer();

    for ( int i=0 ; i < text.length(); i++)
    {
      if ( text.charAt( i ) == '{' )
      {
        int stop = text.indexOf( '}', i+1 );
        if ( stop == -1 )
        {
          outBuffer.append( '{' );
          continue;
        }
        else
        {
          if ( stop == i + 1 )
          {
            outBuffer.append( '{' );
            continue;
          }
          String moniker = text.substring( i+1, stop );
          ToolArgument arg = getByMoniker( moniker );
          if ( arg != null )
          {
            String result = arg.getValue( context );
            if ( result != null )
            {
              outBuffer.append( arg.getValue( context ) );
            }
            i = stop;
            continue;
          }
        }
      }
      outBuffer.append( text.charAt( i ) );
    }

    return outBuffer.toString();
  }


  /**
   * Register an argument with the registry
   *
   * @param key the key to register with
   */
  public void register( final ToolArgument argument )
  {
    m_registry.put( argument.getClass(), argument );
    m_monikerMap.put( argument.getMoniker(), argument );
  }

  /**
   * Unregister an argument with the registry
   *
   * @param key the key to unregister
   */
  public void unregister( final ToolArgument argument )
  {
    m_registry.remove( argument.getClass() );
    m_monikerMap.remove( argument.getMoniker() );
  }

  /**
   * Retrieve an argument by class
   *
   * @param argumentClass the class of the ToolArgument instance to retrieve
   * @return the single instance of the specified class registered with the
   *    registry. Null if no such ToolArgument is registered.
   */
  public ToolArgument get( final Class argumentClass )
  {
    return (ToolArgument)m_registry.get( argumentClass );
  }

  /**
   * Retrieve an argument by its moniker
   *
   * @param a ToolArgument moniker
   * @return a registered ToolArgument with a matching moniker. Null if no
   *    such ToolArgument is registered
   */
  public ToolArgument getByMoniker( final String moniker )
  {
    return (ToolArgument)m_monikerMap.get( moniker );
  }

  /**
   * Get an iterator of all ToolArguments
   *
   * @return an Iterator, the contents of which are all ToolArgument
   *    instances.
   */
  public Iterator getArguments()
  {
    final Collection args = m_registry.values();
    final Object[] argArray = args.toArray();

    final Collator naturalCollator = Collator.getInstance();

    Arrays.sort( argArray, new Comparator() {

      public boolean equals( Object o )
      {
        return ( this == o );
      }

      public int compare( Object thisOne, Object thatOne )
      {
        if ( thisOne == null && thatOne == null ) return 0;
        if ( thisOne == null || thatOne == null ) return -1;
        return naturalCollator.compare(
          ((ToolArgument)thisOne).getName(),
          ((ToolArgument)thatOne).getName()
        );
      }

    });

    return Arrays.asList( argArray ).iterator();
  }

}