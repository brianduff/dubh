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

package org.dubh.jdev.language.python;

import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.language.BlockRenderer;
import oracle.javatools.editor.language.LanguageSupport;
import oracle.javatools.editor.language.LexerDocumentRenderer;
import oracle.javatools.editor.language.NumberRange;
import oracle.javatools.parser.Lexer;
import org.dubh.jdev.parser.python.PythonLexer;
import org.dubh.jdev.parser.python.PythonTokens;
import org.dubh.jdev.language.python.PythonBlockRenderer;

/**
 * The PythonDocumentRenderer is the DocumentRenderer implementation for
 * rendering an entire python document. This is used for providing syntax
 * highlighting for python code. <p>
 * 
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class PythonDocumentRenderer extends LexerDocumentRenderer
  implements PythonTokens
{
  /**
   * Constructs a new document renderer
   * 
   * @param support the python language support
   */
  public PythonDocumentRenderer( LanguageSupport support )
  {
    super( support );
  }

// ----------------------------------------------------------------------------
// LexerDocumentRenderer implementation
// ----------------------------------------------------------------------------

  public boolean isMultiLineToken( int token )
  {
    if ( token == PythonTokens.STRING_LITERAL )
    {
      return true;
    }
    return false;
  }

  /**
   * Creates a lexer used for breaking apart tokens
   */
  protected Lexer createLexer()
  {
    return new PythonLexer();
  }

  /**
   * Creates a BlockRenderer used for rendering the tokens
   */
  protected BlockRenderer createBlockRenderer()
  {
    TextBuffer textBuffer = getTextBuffer();
    return new PythonBlockRenderer( textBuffer );
  }
}