package org.freeciv.common;

import java.io.*;


/**
 A low-level object for reading a registry-format file.
 original author: David Pfitzner <dwp@mso.anu.edu.au>
 Converted to Java: Brian Duff <bri@dubh.co.uk>
 From freeciv common/inputfile.c
 
 This module implements an object which is useful for reading/parsing
 a file in the registry format of registry.c.  It takes care of the
 low-level file-reading details, and provides functions to return
 specific "tokens" from the file.
 
 When the user tries to read a token, we return a (const char*)
 pointing to some data if the token was found, or NULL otherwise.
 The data pointed to should not be modified.  The retuned pointer
 is valid _only_ until another inputfile is performed.  (So should
 be used immediately, or mystrdup-ed etc.)
 
 The tokens recognised are as follows:
 (Single quotes are delimiters used here, but are not part of the
 actual tokens/strings.)
 Most tokens can be preceeded by optional whitespace; exceptions
 are section_name and entry_name.
 
 section_name:  '[foo]'
 returned token: 'foo'
 
 entry_name:  'foo =' (optional whitespace allowed before '=')
 returned token: 'foo'
 
 end_of_line: newline, or optional '#' or ';' (comment characters)
 followed by any other chars, then newline.
 returned token: should not be used except to check non-NULL.
 
 table_start: '{'
 returned token: should not be used except to check non-NULL.
 
 table_end: '}'
 returned token: should not be used except to check non-NULL.
 
 comma:  literal ','
 returned token: should not be used except to check non-NULL.
 
 value:  a signed integer, or a double-quoted string, or a
 gettext-marked double quoted string.  Strings _may_ contain
 raw embedded newlines, and escaped doublequotes, or \.
 eg:  '123', '-999', '"foo"', '_("foo")'
 returned token: string containing number, for numeric, or string
 starting at first doublequote for strings, but ommiting
 trailing double-quote.  Note this does _not_ translate
 escaped doublequotes etc back to normal.
 
 ***********************************************************************/
public class InputFile
{
  public static final boolean ASSERT_ON = true;
  private static final boolean DEBUG = false;
  private interface TokenGetter
  {
    public String getToken( InputFile file );
  }
  public static final TokenGetter INF_TOK_SECTION_NAME = new SectionNameGetter(), INF_TOK_ENTRY_NAME = new EntryNameGetter(), INF_TOK_EOL = new EOLGetter(), INF_TOK_TABLE_START = new TableStartGetter(), INF_TOK_TABLE_END = new TableEndGetter(), INF_TOK_COMMA = new CommaGetter(), INF_TOK_VALUE = new ValueGetter();
  String m_filename;
  BufferedReader m_is;
  boolean m_eof;
  String m_curLine = "";
  String m_copyLine = "";
  int m_curLinePos;
  int m_lineNum;
  String m_token = "";
  String m_partial = "";
  public InputFile( String filename )
          throws IOException 
  {
    open( filename );
  }
  private void assert( boolean b )
  {
    if( ASSERT_ON )
    {
      if( !b )
      {
        throw new IllegalStateException( "Assertion Failed" );
      }
    }
  }
  private void assertSanity()
  {
    if( ASSERT_ON )
    {
      assert( ( m_filename != null ) );
      assert( ( m_is != null ) );
      assert( ( m_lineNum >= 0 ) );
      assert( ( m_curLinePos >= 0 ) );
      assert( ( m_curLine.length() >= 0 ) );
      assert( ( m_copyLine.length() >= 0 ) );
      assert( ( m_token.length() >= 0 ) );
      assert( ( m_partial.length() >= 0 ) );
    }
  }
  private boolean isComment( char c )
  {
    return ( c == '#' || c == ';' );
  }
  private void initZeros()
  {
    m_filename = null;
    m_is = null;
    m_eof = false;
    m_curLine = "";
    m_copyLine = "";
    m_curLinePos = m_lineNum = 0;
    m_token = "";
    m_partial = "";
  }
  public void open( String filename )
               throws IOException
  {
    BufferedReader is = new BufferedReader( new InputStreamReader( new FileInputStream( filename ) ) );
    initZeros();
    m_filename = filename;
    m_is = is;
  }
  public void close()
  {
    assertSanity();
    try
    {
      m_is.close();
    }
    catch( IOException ioe )
    {
      ioe.printStackTrace();
    }
    initZeros();
  }
  
  /**
   * Return true if have data for current line
   */
  private boolean haveLine()
  {
    assertSanity();
    return ( m_curLine.length() > 0 );
  }
  
  /**
   * Return true if current pos is at end of line
   */
  private boolean atEol()
  {
    assertSanity();
    assert( ( m_curLinePos <= m_curLine.length() ) );
    return ( m_curLinePos >= m_curLine.length() );
  }
  public boolean atEof()
  {
    assertSanity();
    return m_eof;
  }
  
  /**
   * Read a new line into cur_line; also copy to copy_line.
   * Increments line_num and cur_line_pos.
   * Returns 0 if didn't read or other problem: treat as EOF.
   * Strips newline from input.
   */
  private boolean readALine()
  {
    assertSanity();
    if( atEof() )
    {
      debug( "Hit EOF" );
      return false;
    }
    String line;
    try
    {
      line = m_is.readLine();
      debug( "Got line " + line );
      if( line == null )
      {
        debug( "Got null line from m_is.readLine() - EOF!!" );
        m_eof = true;
        line = "";
      }
      m_lineNum++;
      m_curLinePos = 0;
      m_curLine = line;
      m_copyLine = line;
    }
    catch( IOException io )
    {
      System.err.println( "IO Exception reading line" );
      io.printStackTrace();
    }
    return !atEof();
  }
  private void assignFlagToken( String str, char val )
  {
    
  
  // Don't know what this does.
  }
  
  /**
   * For compatibility, currently does nothing.,
   */
  private void infLog( int loglevel, String message )
  {
    
  }
  private void infDie( String message )
  {
    throw new IllegalStateException( "InputFile wants to die: " + message );
  }
  private void infWarn( String message )
  {
    
  }
  private String getToken( TokenGetter type, boolean required )
  {
    assertSanity();
    String c = "";
    if( type == null )
    {
      throw new IllegalArgumentException( "Must specify a token type, not null" );
    }
    if( !haveLine() )
    {
      readALine();
    }
    if( haveLine() )
    {
      c = type.getToken( this );
    }
    if( c == null && required )
    {
      infDie( "Didn't find mandatory token " + type );
    }
    return c;
  }
  public String getToken( TokenGetter type )
  {
    return getToken( type, false );
  }
  public String getTokenRequired( TokenGetter type )
  {
    return getToken( type, true );
  }
  public int discardTokens( TokenGetter type )
  {
    int count = 0;
    while( getToken( type ) != null )
    {
      count++;
    }
    return count;
  }
  private static class SectionNameGetter implements TokenGetter
  {
    public String getToken( InputFile inf )
    {
      inf.assert( inf.haveLine() );
      int c = inf.m_curLinePos;
      if( inf.m_curLine.charAt( c++ ) != '[' )
      {
        return null;
      }
      int start = c;
      while( c < inf.m_curLine.length() && inf.m_curLine.charAt( c ) != ']' )
      {
        c++;
      }
      if( inf.m_curLine.charAt( c ) != ']' )
      {
        return null;
      }
      inf.m_curLinePos = c + 1;
      inf.m_token = inf.m_curLine.substring( start, c );
      return inf.m_token;
    }
  }
  private static class EntryNameGetter implements TokenGetter
  {
    public String getToken( InputFile inf )
    {
      inf.assert( inf.haveLine() );
      int c = inf.m_curLinePos;
      while( c < inf.m_curLine.length() && Character.isWhitespace( inf.m_curLine.charAt( c ) ) )
      {
        c++;
      }
      if( c >= inf.m_curLine.length() )
      {
        return null;
      }
      int start = c;
      while( c < inf.m_curLine.length() && !Character.isWhitespace( inf.m_curLine.charAt( c ) ) && inf.m_curLine.charAt( c ) != '=' && !inf.isComment( inf.m_curLine.charAt( c ) ) )
      {
        c++;
      }
      if( !( ( c < inf.m_curLine.length() ) && ( Character.isWhitespace( inf.m_curLine.charAt( c ) ) || ( inf.m_curLine.charAt( c ) == '=' ) ) ) )
      {
        return null;
      }
      int end = c;
      while( c < inf.m_curLine.length() && inf.m_curLine.charAt( c ) != '=' && !inf.isComment( inf.m_curLine.charAt( c ) ) )
      {
        c++;
      }
      if( inf.m_curLine.charAt( c ) != '=' )
      {
        return null;
      }
      inf.m_curLinePos = c + 1;
      inf.m_token = inf.m_curLine.substring( start, end );
      return inf.m_token;
    }
  }
  private static class EOLGetter implements TokenGetter
  {
    public String getToken( InputFile inf )
    {
      int c;
      inf.assert( inf.haveLine() );
      if( !inf.atEol() )
      {
        inf.debug( "Trying to get EOL. Not currently at EOL: " + inf.m_curLine + " at pos " + inf.m_curLinePos );
        c = inf.m_curLinePos;
        while( c < inf.m_curLine.length() && Character.isWhitespace( inf.m_curLine.charAt( c ) ) )
        {
          c++;
        }
        if( c >= inf.m_curLine.length() )
        {
          inf.debug( "Reached EOL" );
        }
        else
        {
          if( !inf.isComment( inf.m_curLine.charAt( c ) ) )
          {
            return null;
          }
        }
      }
      else
      {
        inf.debug( "Call to EOLGetter when already at EOL." );
      }
      inf.m_curLine = "";
      inf.m_curLinePos = 0;
      
      // assign flag token???
      inf.m_token = " ";
      return inf.m_token;
    }
  }
  private abstract static class WhiteCharGetter implements TokenGetter
  {
    public String getToken( InputFile inf )
    {
      inf.assert( inf.haveLine() );
      int c = inf.m_curLinePos;
      inf.debug( "Curline is " + inf.m_curLine );
      while( c < inf.m_curLine.length() && Character.isWhitespace( inf.m_curLine.charAt( c ) ) )
      {
        c++;
      }
      
      // Reached end of line, with no sign of the required target
      if( c >= inf.m_curLine.length() )
      {
        return null;
      }
      if( inf.m_curLine.charAt( c ) != getTarget() )
      {
        return null;
      }
      inf.m_curLinePos = c + 1;
      
      // assign flag token???
      inf.m_token = "" + getTarget();
      return inf.m_token;
    }
    abstract char getTarget();
  }
  private static class TableStartGetter extends WhiteCharGetter
  {
    public char getTarget()
    {
      return '{';
    }
    ;
  }
  private static class TableEndGetter extends WhiteCharGetter
  {
    public char getTarget()
    {
      return '}';
    }
    ;
  }
  private static class CommaGetter extends WhiteCharGetter
  {
    public char getTarget()
    {
      return ',';
    }
    ;
  }
  
  // Fun fun fun
  private static class ValueGetter implements TokenGetter
  {
    public String getToken( InputFile inf )
    {
      String ln = inf.m_curLine;
      char trailing;
      boolean has_i18n_marking = false;
      int start_line;
      StringBuffer partial;
      inf.assert( inf.haveLine() );
      int c = inf.m_curLinePos;
      int start;
      inf.debug( "In VALUEGETTER getToken. line is " + ln );
      
      // Skip any initial whitespace
      while( c < ln.length() && Character.isWhitespace( ln.charAt( c ) ) )
      {
        c++;
      }
      
      // If reached end of line, no value was found, return null.
      if( c >= ln.length() )
      {
        return null;
      }
      inf.debug( "Char at " + c + " is " + ln.charAt( c ) );
      
      // Check for numerical value
      if( ln.charAt( c ) == '-' || Character.isDigit( ln.charAt( c ) ) )
      {
        /* a number */
        start = c++;
        while( c < ln.length() && Character.isDigit( ln.charAt( c ) ) )
        {
          c++;
        }
        
        /* check that the trailing stuff is ok: */
        if( !( !( c < ln.length() ) || ln.charAt( c ) == ',' || Character.isWhitespace( ln.charAt( c ) ) || inf.isComment( ln.charAt( c ) ) ) )
        {
          inf.debug( "Returning null bigcond" );
          return null;
        }
        
        /* If its a comma, we don't want to obliterate it permanently,
        * so rememeber it: */
        if( c < ln.length() )
        {
          trailing = ln.charAt( c );
        }
        // *c = '\0';
        inf.m_curLinePos = c;
        inf.m_token = ln.substring( start, c );
        inf.debug( "Returning numerical value " + inf.m_token );
        return inf.m_token;
      }
      
      /* allow gettext marker: */
      if( ln.charAt( c ) == '_' && ln.charAt( c + 1 ) == '(' )
      {
        has_i18n_marking = true;
        c += 2;
        while( c < ln.length() && Character.isWhitespace( ln.charAt( c ) ) )
        {
          c++;
        }
        if( c >= ln.length() )
        {
          inf.debug( "Returning null c > ln.length()" );
          return null;
        }
      }
      if( !( ln.charAt( c ) == '\"' ) )
      {
        inf.debug( "Returning null c != \"" );
        return null;
      }
      
      /* From here, we know we have a string, we just have to find the
      trailing (un-escaped) double-quote.  We read in extra lines if
      necessary to find it.  If we _don't_ find the end-of-string
      (that is, we come to end-of-file), we return NULL, but we
      leave the file in at_eof, and don't try to back-up to the
      current point.  (That would be more difficult, and probably
      not necessary: at that point we probably have a malformed
      string/file.)
      
      As we read extra lines, the string value from previous
      lines is placed in partial.
      */
      
      /* prepare for possibly multi-line string: */
      start_line = inf.m_lineNum;
      partial = new StringBuffer();
      start = c++;
      for( ;; )
      {
        while( c < ln.length() && ( ln.charAt( c ) != '\"' || ( c > 0 && ln.charAt( c - 1 ) == '\\' ) ) )
        {
          c++;
        }
        if( c < ln.length() && ln.charAt( c ) == '\"' )
        {
          break; /* found end of string*/
        }
        
        
        /* Accumulate to partial string and try more lines;
        * note partial->n must be _exactly_ the right size, so we
        * can strcpy instead of strcat-ing all the way to the end
        * each time. */
        partial.append( ln.substring( start ) );
        partial.append( "\n" );
        if( !inf.readALine() )
        {
          // do error
          inf.debug( "Failed to read a line" );
          return null;
        }
        c = start = 0;
        ln = inf.m_curLine;
      }
      
      /* found end of string */
      inf.m_curLinePos = c + 1;
      inf.m_token = partial.toString();
      inf.m_token = inf.m_token + ln.substring( start, c + 1 );
      if( has_i18n_marking )
      {
        if( ln.charAt( ++c ) == ')' )
        {
          inf.m_curLinePos++;
        }
        else
        {
          System.err.println( "Warning: Missing end of i18n string marking" );
        }
      }
      inf.debug( "Returning value " + inf.m_token );
      return inf.m_token;
    }
  }
  void debug( String str )
  {
    if( DEBUG )
    {
      System.out.println( "InputFile: " + str );
    }
  }
  public void finalize()
  {
    close();
  }
}
