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

import java.io.IOException;
import java.io.Writer;

import javax.swing.text.Style;

/**
 * A writer implementation that writes to the console in a particular
 * style.
 *
 * @author Brian.Duff@oracle.com
 */
class ConsoleWriter extends Writer
{

  private final Console m_console;
  private final Style m_style;

  /**
   * Construct an instance of ConsolePrintWriter
   */
  ConsoleWriter( Console console, Style style )
  {
    m_console = console;
    m_style = style;
  }

  public void write( char[] c, int off, int len )
    throws IOException
  {
    String s = new String( c, off, len );
    m_console.append( s, m_style );
  }

  public void close()
  {
    
  }

  public void flush()
  {
    
  }

}