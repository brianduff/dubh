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
 * The Original Code is Python Addin for Oracle9i JDeveloper.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.jdev.addin.python;

import java.io.PrintWriter;

import oracle.ide.layout.ViewId;
import oracle.ide.log.LogPage;
import oracle.ide.log.LogPipe;
import oracle.ide.log.DefaultLogPage;


/**
 * IDE log page for Python.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonLog extends DefaultLogPage
{
  // Lots of improvements could be made to this..
  
  private static final String LOG_PAGE_ID = "PYTHON_LOG";
  private PrintWriter m_writer;

  public PythonLog()
  {
    super( 
      new ViewId( LOG_PAGE_ID, "Python" ), null, true
    );
    m_writer = LogPipe.getPrintWriter( this );    
  }

  PrintWriter getWriter()
  {
    return m_writer;
  }
}