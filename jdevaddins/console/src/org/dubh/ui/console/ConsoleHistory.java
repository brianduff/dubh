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
 * The Original Code is The Dubh.Org Console Control.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.ui.console;

import java.util.ArrayList;
import java.util.List;

/**
 * The console command history.
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class ConsoleHistory
{
  private List m_commandList;
  private static final int LIST_SIZE_MAX = 200;
  private int m_position;

  public ConsoleHistory()
  {
    setList( new ArrayList() );
  }

  public ConsoleHistory( ConsoleHistory copy )
  {
    m_commandList = new ArrayList();
    m_commandList.addAll( copy.m_commandList );
    m_position = copy.m_position;
  }

  /**
   * Get the command history list
   */
  public List getList()
  {
    return m_commandList;
  }

  /**
   * Set the command history list
   */
  public void setList( List l )
  {
    m_commandList = l;
    m_position = l.size()-1;
  }

  /**
   * Add a command to the command history. The command is added at the end
   * of the history list.
   */
  public void addCommand( final String command )
  {
    // This is grossly inefficient.
    m_commandList.add( command );
    if ( m_commandList.size() > LIST_SIZE_MAX )
    {
      int diff = m_commandList.size() - LIST_SIZE_MAX;
      for ( int i=0; i < diff; i++ )
      {
        m_commandList.remove( i );
      }
    }
    m_position = m_commandList.size() - 1;
  }

  /**
   * Get the next command in the history. Returns the next command, or null
   * if there is no next command.
   */
  public String getNextCommand()
  {
    if ( m_position + 1 > m_commandList.size() - 1 )
    {
      return null;
    }
    return (String) m_commandList.get( ++m_position );
  }

  /**
   * Get the previous command in the history. Returns the previous command, or
   * null if there is no previous command.
   */
  public String getPreviousCommand()
  {
    if ( m_position < 0 )
    {
      return null;
    }
    return (String) m_commandList.get( m_position-- );
  }

  /**
   * Reset the history position so that the next call to getNextCommand()
   * will retrieve the last entered command
   * 
   */
  public void reset()
  {
    m_position = m_commandList.size() - 1;
  }
}