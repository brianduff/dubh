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

import java.awt.Color;
import java.awt.Font;

import oracle.javatools.buffer.TextBuffer;
import oracle.javatools.editor.language.BaseStyle;
import oracle.javatools.editor.language.BuiltInStyles;
import oracle.javatools.editor.language.LexerBlockRenderer;
import oracle.javatools.editor.language.StyledFragmentsList;
import oracle.javatools.editor.language.StyleRegistry;

import oracle.javatools.parser.Lexer;

import org.dubh.jdev.parser.python.PythonLexer;
import org.dubh.jdev.parser.python.PythonTokens;
import org.dubh.jdev.language.python.PythonStyles;

/**
 * The PythonBlockRenderer class is the BlockRenderer implementation for 
 * python code.
 * 
 * @author Brian.Duff@oracle.com
 */
public class PythonBlockRenderer extends LexerBlockRenderer
  implements PythonTokens
{
  public PythonBlockRenderer( TextBuffer textBuffer )
  {
    super( textBuffer );
  }


// ----------------------------------------------------------------------------
// LexerBlockRenderer implementation
// ----------------------------------------------------------------------------

  /**
   * Fetches the style to use for the token fetched from the lexer
   */
  protected String mapTokenToStyleName( int token )
  {
    switch ( token )
    {
      case COMMENT:
        return PythonStyles.PYTHON_COMMENT_STYLE;
      case IDENTIFIER:
        return PythonStyles.PYTHON_IDENTIFIER_STYLE;
      case INT_LITERAL:
      case FLOAT_LITERAL:
      case IMAGINARY_LITERAL:
        return PythonStyles.PYTHON_NUMBER_STYLE;
      case STRING_LITERAL:
        return PythonStyles.PYTHON_STRING_STYLE;

      case KW_AND: case KW_DEL: case KW_FOR: case KW_IS: case KW_RAISE:
      case KW_ASSERT: case KW_ELIF: case KW_FROM: case KW_LAMBDA:
      case KW_RETURN:  case KW_BREAK: case KW_ELSE: case KW_GLOBAL:
      case KW_NOT: case KW_TRY: case KW_CLASS: case KW_EXCEPT: case KW_IF:
      case KW_OR: case KW_WHILE: case KW_CONTINUE: case KW_EXEC:
      case KW_IMPORT: case KW_PASS: case KW_YIELD:  case KW_DEF:
      case KW_FINALLY: case KW_IN: case KW_PRINT:
        return PythonStyles.PYTHON_KEYWORD_STYLE;
    }
    return getDefaultStyleName();
  }

  protected Lexer createLexer()
  {
    return new PythonLexer();
  }
}