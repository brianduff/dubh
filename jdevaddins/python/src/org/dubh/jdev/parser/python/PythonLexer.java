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

package org.dubh.jdev.parser.python;

import oracle.javatools.parser.AbstractLexer;
import oracle.javatools.parser.LexerToken;
import oracle.javatools.parser.util.KeywordTable;
import org.dubh.jdev.parser.python.PythonTokens;


/**
 * Implementation of the JDeveloper Lexer interface for the Python language.
 * 
 * @author  Herbert Czymontek, Andy Yu, Jimmy Yu (Java), Brian.Duff@oracle.com (python)
 */
public class PythonLexer extends AbstractLexer
  implements PythonTokens
{


  /**
   * The last token found from <code>lex()</code< operation.
   */
  private int lastToken;

  /**
   * The starting offset for the last token found
   */
  private int startOffset;

  /**
   * The ending offset of the last token found.
   */
  private int endOffset;

  /**
   * Whether to use the last token that was found.
   */
  private boolean useLastToken;

  /**
   * Whether to skip comments or not.
   */
  private boolean skipComments;

  /**
   * The keyword table for our python keywords.
   */
  private static KeywordTable keywordTable;

  /**
   * The lookup array for characters under 256 to see if they
   * constitute a valid part of an identifier.
   */
  private static boolean[] identifierPart;




  /**
   * Constructs a default <code>PythonLexer</code> with a starting
   * position of 0.  Clients must call <code>setTextBuffer()</code> to
   * initialize the text buffer used for the Lexer.  To start lexing
   * from an offset other than 0, call <code>setPosition()</code>.
   */
  public PythonLexer()
  {
    lastToken = TK_NOT_FOUND;
    startOffset = -1;
    endOffset = -1;
    useLastToken = false;
    skipComments = false;
    setTextBuffer( null );
    setPosition( 0 );
  }

  /**
   * Sets whether the lexer should generate tokens for comments
   * 
   */
  public void setSkipComments( final boolean skipComments )
  {
    this.skipComments = skipComments;
  }

  /**
   * Scans the text buffer at the current position and returns the token that
   * was found. The token and offset information is also stored in the 
   * <code>lexedToken</code> instance passed into the call.
   * 
   * @param lexedToken the instance passed in where token info is stored
   * @return the token that was found, same as calling 
   *    <code>lexedToken.getToken()</code> (for convenience)
   */
  public int lex( final LexerToken lexedToken )
  {
    // Check whether the user called backup() after the last lex() operation
    if ( useLastToken )
    {
      useLastToken = false;
      return fillLexerToken( lexedToken );
    }
    
    // Initialize token value - set this to EOF so that if an exception
    // is thrown on the first character access, we know it's an EOF and
    // not a part of a token.
    lastToken = EOF;

    // We don't do range-checking here at all, so we must catch
    // the IndexOutOfBoundsException that the text buffer may throw
    // (see the ReadTextBuffer interface for more details.)
    try
    {
      // Use a while loop here so that we can skip over white-space
      // and possibly comments.
      while ( true )
      {
        // Store the starting offset of the current token
        char c = textBuffer.getChar( currentPos++ );

        switch ( c )
        {
          case ' ': // SPC
          case '\t': // TAB
          case '\f': // FF
          case '\n': // LF ?
          case '\r': // CR ?
            continue;
        }

        // Record the start offset for the token
        startOffset = currentPos - 1;

        // Big switch statement to map token type.  Note, in case of
        // tokens that consist of more than one character, make sure
        // that the lastToken is kept up to date.  For example, if the
        // first character is '+', store TK_PLUS in case it happens to
        // be the last character in the buffer (and checking for the
        // next character throws an exception.)
        switch ( c )
        {
          // Handle single-character operators
          case '{':  lastToken = LBRACE;    break;
          case '}':  lastToken = RBRACE;    break;
          case '[':  lastToken = LBRACKET;  break;
          case ']':  lastToken = RBRACKET;  break;
          case '(':  lastToken = LPAREN;    break;
          case ')':  lastToken = RPAREN;    break;
          case ',':  lastToken = COMMA;     break;
          case ';':  lastToken = SEMICOLON; break;
          case '~':  lastToken = NOT;       break;
          case ':':  lastToken = COLON;     break;
          // Some missing here...

          // Multi character operators
          case '*':
            lastToken = MULTIPLY;
            c = textBuffer.getChar( currentPos );
            switch ( c )
            {
              case '*':
                lastToken = POWER;
                currentPos++;              
                if ( textBuffer.getChar( currentPos ) == '=' )
                {
                  lastToken = POWEREQ;
                  currentPos++;
                }
                break;
              case '=':
                lastToken = MULTIPLYEQ;
                currentPos++;
                break;
            }
            break;

          case '/':
            lastToken = DIVIDE;
            // Nb. // is defined in section 2.5 as an operator, but I can't
            // find a definition anywhere.... likewise for //=
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = DIVIDEEQ;
              currentPos++;
            }
            break;

          case '+':
            lastToken = PLUS;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = PLUSEQ;
              currentPos++;
            }
            break;

          case '-':
            lastToken = MINUS;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = MINUSEQ;
              currentPos++;
            }
            break;

          case '%':
            lastToken = MODULO;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = MODULOEQ;
              currentPos++;
            }
            break;

          case '<':
            lastToken = LESS;
            c = textBuffer.getChar( currentPos );
            switch ( c )
            {
              case '=':
                lastToken = EQLESS;
                currentPos++;
                break;            
              case '<':
                lastToken = LSHIFT;
                currentPos++;

                if ( textBuffer.getChar( currentPos ) == '=' )
                {
                  lastToken = LSHIFTEQ;
                  currentPos++;
                }
                break;
              case '>':
                lastToken = LESSGREATER;
                currentPos++;
                break;
            }
            break;

          case '>':
            lastToken = GREATER;
            c = textBuffer.getChar( currentPos );
            switch ( c )
            {
              case '=':
                lastToken = EQGREATER;
                currentPos++;
                break;
              case '>':
                lastToken = RSHIFT;
                currentPos++;
                if ( textBuffer.getChar( currentPos ) == '=' )
                {
                  lastToken = RSHIFTEQ;
                  currentPos++;
                }
                break;
            }
            break;
            
          case '&':
            lastToken = AND;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = ANDEQ;
              currentPos++;
            }
            break;

          case '|':
            lastToken = OR;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = OREQ;
              currentPos++;
            }
            break;

          case '^':
            lastToken = XOR;
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = XOREQ;
              currentPos++;
            }
            break;

          case '!':
            if ( textBuffer.getChar( currentPos ) == '=' )
            {
              lastToken = NOTEQUAL;
              currentPos++;
            }
            else
            {
              lastToken = EOF;     // not sure about this            
              continue;
            }
            break;

          case '#':
            lastToken = COMMENT;
            skipLineComment();
            if ( skipComments )
            {
              lastToken = EOF;
              continue;
            }
            break;

          // String literals?
          case '\'':
          case '\"':
            // Triple quoted string?
            if ( textBuffer.getChar( currentPos ) == c && textBuffer.getChar( currentPos + 1 ) == c &&
              !Character.isWhitespace( textBuffer.getChar( currentPos + 2 ) ) )   // This last requirement is strictly speaking wrong, but it helps the lexer.
            {
              lastToken = STRING_LITERAL;
              currentPos++;
              skipTripleQuotedStringLiteral( c );
            }
            else
            {
              lastToken = STRING_LITERAL;
              skipStringLiteral( c );
            }
            break;
            
          case '.':
            lastToken = DOT;
            c = textBuffer.getChar( currentPos );
            if ( isDecimalDigit( c ) )
            {
              currentPos++;
              lastToken = FLOAT_LITERAL;
              skipFloatLiteral();
            }
            break;


          // python string literal prefixes.

          // r|R[u|U]['|"]
          case 'r':
          case 'R':
            char save = c;
            c = textBuffer.getChar( currentPos );
            switch ( c ) 
            {
              case '\"': case '\'':
                lastToken = STRING_LITERAL;
                currentPos++;
                skipStringLiteral( c );
                break;
              case 'u':
              case 'U':
                char x = textBuffer.getChar( currentPos+1 );
                if ( x == '\"' || x == '\'' )
                {
                  lastToken = STRING_LITERAL;
                  currentPos+=2;
                  skipStringLiteral( x );
                }
                else
                {
                  lastToken = otherstuff( save );
                }
                break;
              default:
                lastToken = otherstuff( save );
            }
            break;   

          // The flip of the above, i.e. u|U[r|R]['|"]
          case 'u':
          case 'U':
            save = c;
            c = textBuffer.getChar( currentPos );
            switch ( c ) 
            {
              case '\"': case '\'':
                lastToken = STRING_LITERAL;
                currentPos++;
                skipStringLiteral( c );
                break;
              case 'r':
              case 'R':
                char x = textBuffer.getChar( currentPos+1 );
                if ( x  == '\"' || x == '\'' )
                {
                  lastToken = STRING_LITERAL;
                  currentPos +=2;
                  skipStringLiteral( x );
                }
                else
                {
                  lastToken = otherstuff( save );
                }
                break;
              default:
                lastToken = otherstuff( save );
            }
            break;    // I think?? dunno actually.

          // Int literals
          case '0':
            lastToken = INT_LITERAL;
            c = textBuffer.getChar( currentPos );
            switch ( c )
            {
              case 'x':
              case 'X':
                currentPos++;
                skipHexDigits();
                break;

              case '0': case '1': case '2': case '3': case '4': case '5':
              case '6': case '7':
                currentPos++;
                skipOctalDigits();
                break;

              case '.':
                currentPos++;
                lastToken = FLOAT_LITERAL;
                skipFloatLiteral();

              case 'e':
              case 'E':
                // Don't eat the 'e' character
                lastToken = FLOAT_LITERAL;
                skipFloatLiteral();
                break;

              case 'l':
              case 'L':
                currentPos++;
                break;

              case 'j':
              case 'J':
                currentPos++;
                lastToken = IMAGINARY_LITERAL;
                break;
              
            }
            break;

          case '1': case '2': case '3': case '4': case '5': case '6': case '7':
          case '8': case '9':
            lastToken = INT_LITERAL;
            skipDecimalDigits();

            c = textBuffer.getChar( currentPos );
            switch ( c )
            {
              case 'l':
              case 'L':
                currentPos++;
                break;

              case 'j':
              case 'J':
                currentPos++;
                lastToken = IMAGINARY_LITERAL;
                break;

              case '.':
                currentPos++;
                lastToken = FLOAT_LITERAL;
                skipFloatLiteral();
                break;

              case 'e':
              case 'E':
                // Don't eat 'e'
                lastToken = FLOAT_LITERAL;
                skipFloatLiteral();
                break;
              
            }
            break;
            
          default:
            lastToken = otherstuff( c ); 
        } // switch

        // break out of while loop unless we explicitly continue;ed
        break;
      } // while
    } // try
    catch ( IndexOutOfBoundsException e )
    {
      currentPos = textBuffer.getLength();

      if ( lastToken == EOF )
      {
        startOffset = currentPos;
      }
    }

    endOffset = currentPos;
    useLastToken = false;

    return fillLexerToken( lexedToken );
  } // lex()

  /**
   * For identifiers and keywords and other stuff not directly handled in the
   * lex() method...
   */
  private int otherstuff( char c )
    throws IndexOutOfBoundsException
  {
    if ( !isPythonIdentifierStart( c ) )
    {
      lastToken = EOF;
      return lastToken;
    }

    lastToken = IDENTIFIER;
    int hash = KeywordTable.computePartialHash( c, 0 );

    // scan until we find a character that is not part of an identifier
    // In python, an identifier is:
    // ( ( a..z | A..Z ) | _ ) ( a..z | A..Z | 0..9 | _ )*
    while ( true )
    {
      c = textBuffer.getChar( currentPos );
      if ( !isPythonIdentifierChar( c ) )
      {
        // Not a valid python identifier
        break;
      }
      hash = KeywordTable.computePartialHash( c, hash );
      currentPos++;
    }

    // Now check the keyword table to see if this is a keyword
    int keyword = keywordTable.lookupKeyword( textBuffer, startOffset, 
      currentPos, hash
    );

    if ( keyword != TK_NOT_FOUND )
    {
      lastToken = keyword;
    }

    return lastToken;
  }

  private static final boolean isPythonIdentifierChar( char c )
  {
    return ( c == '_' || ( c <= 127 && ( Character.isLetter( c ) || Character.isDigit( c ) ) ) );
  }

  private static final boolean isPythonIdentifierStart( char c ) 
  {
    return (c == '_' || ( c <= 127 && Character.isLetter( c ) ));
  }

  /**
   * Utility routine to fill in the <code>LexerToken</code> structure
   * with the current token information we have.  Returns the
   * current token.
   * @param lexedToken the client-specified <code>LexerToken</code>
   * @return the last token found
   */
  private int fillLexerToken( LexerToken lexedToken )
  {
    AbstractLexer.DefaultLexerToken outToken =
      (AbstractLexer.DefaultLexerToken)lexedToken;

    outToken.setToken( lastToken );
    outToken.setStartOffset( startOffset );
    outToken.setEndOffset( endOffset );

    return lastToken;
  }

  /**
   * Utility routine which scans through the text buffer to find the
   * end of the single-line comment.  Sets the current position on
   * the end of line character.
   */
  private void skipLineComment()
  {
    char c;
    while ( true )
    {
      c = textBuffer.getChar( currentPos );
      switch ( c )
      {
        case '\r':
        case '\n':
          return;
      }
      currentPos++;
    }
  }

  private void skipTripleQuotedStringLiteral( final char delimiter )
  {
    // Any character is valid in a triple quoted string literal ( including
    // newlines ) until an (unescaped) triple is encountered again.

    // Bit of ugliness here. Because the python symbols for start and end
    // for this construct are the same, the lexer has trouble telling whether
    // a given triple quote is the start or end of  a string literal. This is
    // why, when you scroll the editor so that the start of a triple quoted
    // literal goes off the top of the display, everything from the end
    // delimiter onwards appears to become part of the string. not sure
    // how to solve this...

    char c;
    int runCount = 0;
    while ( true )
    {
      c = textBuffer.getChar( currentPos++ );
      if ( c == delimiter )
      {
        runCount++;

        if ( runCount == 3 )
        {
          return;
        }
      }
      else
      {
        runCount = 0;
      }
    }
  }

  /**
   * Utility routine which scans through the text buffer to find the
   * end of the string literal.  Sets the current position on the
   * character after the trailing ("), or on the end of line
   * character.
   */
  private void skipStringLiteral( final char delimiter )
  {
    char c;
    while ( true )
    {
      c = textBuffer.getChar( currentPos++ );

      if ( c == delimiter )
      {
        return;
      }
      
      switch ( c )
      {
        case '\\':
          // Skip whitespace and newline characters
          while ( true )
          {
            c = textBuffer.getChar( currentPos++ );
            if ( !Character.isWhitespace( c ) )
            {
              break;    // while
            }
          }
          break;
        case '\r':
        case '\n':
          currentPos--;
          return;
      }
    }
  }

  /**
   * Utility routine to check whether the given digit is a decimal
   * digit.
   * @param digit the digit to check
   */
  public static boolean isDecimalDigit( char digit )
  {
    switch ( digit )
    {
      case '0':  case '1':  case '2':  case '3':  case '4':
      case '5':  case '6':  case '7':  case '8':  case '9':
        return true;
    }
    return false;
  }

  /**
   * Utility routine which scans through the text buffer to find
   * the end of an float literal.  Sets the current position
   * following the last digit.  This routine expects to start
   * at the (.) or at the (e) exponent.  These are the rules from the
   * Python language spec:
   * <ul>
   *   <li>  Digits . opt-Digits opt-ExponentPart  </li>
   *   <li>  . Digits opt-ExponentPart </li>
   *   <li>  Digits ExponentPart </li>
   *   <li>  Digits opt-ExponentPart  </li>
   * </ul>
   */
  private void skipFloatLiteral()
  {
    // For case 1, the main lex() method eats the initial digits and
    // the dot.  For case 2, lex() eats the dot and one digit.  For
    // case 3, lex() eats the digits, but leaves the 'e'.
    char c = textBuffer.getChar( currentPos );

    // For case 1 and 2, we are expecting some possible digits.  If
    // we hit them, then eat them.
    while ( isDecimalDigit( c ) )
    {
      currentPos++;
      c = textBuffer.getChar( currentPos );
    }

    // Next, we are expecting an optional exponent
    switch ( c )
    {
      case 'e':
      case 'E':
        currentPos++;
        // Eat the 'e', and check if the next character is a
        // sign prefix.
        c = textBuffer.getChar( currentPos );
        if ( ( c == '+' ) || ( c == '-' ) )
        {
          currentPos++;
          c = textBuffer.getChar( currentPos );
        }

        // Eat the rest of the digits if any
        while ( isDecimalDigit( c ) )
        {
          currentPos++;
          c = textBuffer.getChar( currentPos );
        }

        // Fall through
    }
  }

  /**
   * Private utility routine to skip a sequence of hex digits.
   * Sets the current position following the last hex digit.
   */
  private void skipHexDigits()
  {
    char c;
    while ( true )
    {
      c = textBuffer.getChar( currentPos );
      if ( !isHexDigit( c ) )
      {
        if ( ( c == 'l' ) ||
             ( c == 'L' ) )
        {
          // If it is the (long) integer suffix, eat the character as
          // part of this token
          /*
          System.out.println( "Skipping hex long suffix" );
          */
          currentPos++;
        }
        return;
      }
      currentPos++;
    }
  }

  /**
   * Private utility routine to skip a sequence of octal digits.
   * Sets the current position following the last octal digit.
   */
  private void skipOctalDigits()
  {
    char c;
    while ( true )
    {
      c = textBuffer.getChar( currentPos );
      if ( !isOctalDigit( c ) )
      {
        if ( ( c == 'l' ) ||
             ( c == 'L' ) )
        {
          // If it is the (long) integer suffix, eat the character as
          // part of this token
          /*
          System.out.println( "Skipping octal long suffix" );
          */
          currentPos++;
        }
        return;
      }
      currentPos++;
    }
  }

  /**
   * Private utility routine to skip a sequence of decimal digits.
   * Sets the current position following the last decimal digit.
   */
  private void skipDecimalDigits()
  {
    char c;
    while ( true )
    {
      c = textBuffer.getChar( currentPos );
      if ( !isDecimalDigit( c ) )
      {
        return;
      }
      currentPos++;
    }
  }


  /**
   * Utility routine to check whether the given digit is a hexadecimal
   * digit.
   * @param digit the digit to check
   */
  public static boolean isHexDigit( char digit )
  {
    switch ( digit )
    {
      case '0':  case '1':  case '2':  case '3':  case '4':
      case '5':  case '6':  case '7':  case '8':  case '9':
      case 'a':  case 'b':  case 'c':  case 'd':  case 'e':
      case 'f':  case 'A':  case 'B':  case 'C':  case 'D':
      case 'E':  case 'F':
        return true;
    }
    return false;
  }

  /**
   * Utility routine to check whether the given digit is an octal
   * digit.
   * @param digit the digit to check
   */
  public static boolean isOctalDigit( char digit )
  {
    switch ( digit )
    {
      case '0':  case '1':  case '2':  case '3':
      case '4':  case '5':  case '6':  case '7':
        return true;
    }
    return false;
  }

  private static void initialize()
  {
    keywordTable = new KeywordTable( 29 );
    keywordTable.addKeyword( "and", KW_AND );
    keywordTable.addKeyword( "del", KW_DEL );
    keywordTable.addKeyword( "for", KW_FOR );
    keywordTable.addKeyword( "is", KW_IS );
    keywordTable.addKeyword( "raise", KW_RAISE );
    keywordTable.addKeyword( "assert", KW_ASSERT );
    keywordTable.addKeyword( "elif", KW_ELIF );
    keywordTable.addKeyword( "from", KW_FROM );
    keywordTable.addKeyword( "lambda", KW_LAMBDA );
    keywordTable.addKeyword( "return", KW_RETURN );
    keywordTable.addKeyword( "break", KW_BREAK );
    keywordTable.addKeyword( "else", KW_ELSE );
    keywordTable.addKeyword( "global", KW_GLOBAL );
    keywordTable.addKeyword( "not", KW_NOT );
    keywordTable.addKeyword( "try", KW_TRY );
    keywordTable.addKeyword( "class", KW_CLASS );
    keywordTable.addKeyword( "except", KW_EXCEPT );
    keywordTable.addKeyword( "if", KW_IF );
    keywordTable.addKeyword( "or", KW_OR );
    keywordTable.addKeyword( "while", KW_WHILE );
    keywordTable.addKeyword( "continue", KW_CONTINUE );
    keywordTable.addKeyword( "exec", KW_EXEC );
    keywordTable.addKeyword( "import", KW_IMPORT );
    keywordTable.addKeyword( "pass", KW_PASS );
    keywordTable.addKeyword( "yield", KW_YIELD );
    keywordTable.addKeyword( "def", KW_DEF );
    keywordTable.addKeyword( "finally", KW_FINALLY );
    keywordTable.addKeyword( "in", KW_IN );
    keywordTable.addKeyword( "print", KW_PRINT );
  }

  static 
  {
    initialize();
  }
  
// ----------------------------------------------------------------------------
// AbstractLexer implementation
// ----------------------------------------------------------------------------

  /**
   * Unlexes the last found token.  The next call to <code>lex()</code>
   * will return the last token and offset information found.
   */
  public void backup()
  {
    useLastToken = true;
  }

}