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
 * The Original Code is TemplateMaker addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */


package oracle.jdevimpl.templatemaker.velocity;

import java.io.PrintWriter;

import oracle.ide.layout.ViewId;
import oracle.ide.log.LogPage;
import oracle.ide.log.LogPipe;
import oracle.ide.log.DefaultLogPage;

import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * Velocity log system that logs output in the IDE message window
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class IDELogSystem extends DefaultLogPage 
  implements LogSystem
{
  private static final String LOG_PAGE_ID = "VELOCITY_LOG";
  private PrintWriter m_writer;

  public IDELogSystem()
  {
    super( 
      new ViewId( LOG_PAGE_ID, "Velocity Template Engine" ), null, false
    );
  }

// ----------------------------------------------------------------------------
// LogSystem implementation
// ----------------------------------------------------------------------------


  public void init( final RuntimeServices runTime) throws Exception
  {
    m_writer = LogPipe.getPrintWriter( this );
  }

  public void logVelocityMessage( final int level, final String message)
  {
    if ( level == WARN_ID || level == ERROR_ID )
    {
      m_writer.println( message );
    }
  }

}