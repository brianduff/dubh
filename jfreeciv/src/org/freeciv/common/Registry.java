package org.freeciv.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**************************************************************************
 
 Java conversion: Brian Duff <bri@dubh.co.uk> from common/registry.c
 
 Description of the file format:
 (This is based on a format by the original authors, with
 various incremental extensions. --dwp)
 
 - Whitespace lines are ignored, as are lines where the first
 non-whitespace character is ';' (comment lines).
 Optionally '#' can also be used for comments.
 
 - A line with "[name]" labels the start of a section with
 that name; one of these must be the first non-comment line in
 the file.  Any spaces within the brackets are included in the
 name, but this feature (?) should probably not be used...
 
 - Within a section, lines have one of the following forms:
 subname = "stringvalue"
 subname = -digits
 subname = digits
 for a value with given name and string, negative integer, and
 positive integer values, respectively.  These entries are
 referenced in the following functions as "sectionname.subname".
 The section name should not contain any dots ('.'); the subname
 can, but they have no particular significance.  There can be
 optional whitespace before and/or after the equals sign.
 You can put a newline after (but not before) the equals sign.
 
 Backslash is an escape character in strings (double-quoted strings
 only, not names); recognised escapes are \n, \\, and \".
 (Any other \<char> is just treated as <char>.)
 
 - Gettext markings:  You can surround strings like so:
 foo = _("stringvalue")
 The registry just ignores these extra markings, but this is
 useful for marking strings for translations via gettext tools.
 
 - Multiline strings:  Strings can have embeded newlines, eg:
 foo = _("
 This is a string
 over multiple lines
 ")
 This is equivalent to:
 foo = _("\nThis is a string\nover multiple lines\n")
 Note that if you missplace the trailing doublequote you can
 easily end up with strange errors reading the file...
 
 - Vector format: An entry can have multiple values separated
 by commas, eg:
 foo = 10, 11, "x"
 These are accessed by names "foo", "foo,1" and "foo,2"
 (with section prefix as above).  So the above is equivalent to:
 foo   = 10
 foo,1 = 11
 foo,2 = "x"
 As in the example, in principle you can mix integers and strings,
 but the calling program will probably require elements to be the
 same type.   Note that the first element of a vector is not "foo,0",
 in order that the name of the first element is the same whether or
 not there are subsequent elements.  However as a convenience, if
 you try to lookup "foo,0" then you get back "foo".  (So you should
 never have "foo,0" as a real name in the datafile.)
 
 - Tabular format:  The lines:
 foo = { "bar",  "baz",   "bax"
 "wow",   10,     -5
 "cool",  "str"
 "hmm",    314,   99, 33, 11
 }
 are equivalent to the following:
 foo0.bar = "wow"
 foo0.baz = 10
 foo0.bax = -5
 foo1.bar = "cool"
 foo1.baz = "str"
 foo2.bar = "hmm"
 foo2.baz = 314
 foo2.bax = 99
 foo2.bax,1 = 33
 foo2.bax,2 = 11
 The first line specifies the base name and the column names, and the
 subsequent lines have data.  Again it is possible to mix string and
 integer values in a column, and have either more or less values
 in a row than there are column headings, but the code which uses
 this information (via the registry) may set more stringent conditions.
 If a row has more entries than column headings, the last column is
 treated as a vector (as above).  You can optionally put a newline
 after '=' and/or after '{'.
 
 The equivalence above between the new and old formats is fairly
 direct: internally, data is converted to the old format.
 In principle it could be a good idea to represent the data
 as a table (2-d array) internally, but the current method
 seems sufficient and relatively simple...
 
 There is a limited ability to save data in tabular:
 So long as the section_file is constructed in an expected way,
 tabular data (with no missing or extra values) can be saved
 in tabular form.  (See section_file_save().)
 
 - Multiline vectors: if the last non-comment non-whitespace
 character in a line is a comma, the line is considered to
 continue on to the next line.  Eg:
 foo = 10,
 11,
 "x"
 This is equivalent to the original "vector format" example above.
 Such multi-lines can occur for column headings, vectors, or
 table rows, again with some potential for strange errors...
 
 ***************************************************************************/
public class Registry
{
  private static final boolean DEBUG = false;
  private String filename;
  private int num_entries;
  private ArrayList sections;
  private HashMap hashd;
  private Object sbuffer; //?
  
  private class Entry
  {
    public String name;
    public int ivalue;
    public String svalue;
    public int used;
    public Entry() 
    {
      
    }
    public Entry( String name, String tok ) 
    {
      this.name = name;
      if( tok.charAt( 0 ) == '\"' )
      {
        svalue = tok.substring( 1, tok.length() - 1 );
        ivalue = 0;
      }
      else
      {
        svalue = null;
        ivalue = Integer.parseInt( tok );
      }
      used = 0;
    }
  }

  private class Section
  {
    public String name;
    public ArrayList entries;
  }

  private class HashEntry
  {
    Entry data;
    String key_val; /* including section prefix */
  }

  /**
   * Construct a Registry. To actually parse a file, use the loadFile()
   * method.
   */
  public Registry() 
  {
    init();
  }
  
  private void init()
  {
    filename = null;
    sections = new ArrayList();
    num_entries = 0;
    hashd = null;
  // sb = sbuf_new();
  }
  
  private String getFilename()
  {
    if( filename == null )
    {
      return "(anonymous)";
    }
    else
    {
      return filename;
    }
  }
  
  /**
   *   Print log messages for any entries in the file which have
   * not been looked up -- ie, unused or unrecognised entries.
   * To mark an entry as used without actually doing anything with it,
   * you could do something like:
   * section_file_lookup(&file, "foo.bar");  / * unused * /
   */
  private void fileCheckUnused()
  {
    
  }

  /**
   * Load and parse the specified file.
   *
   * @param filename the file to load
   * @throws java.io.IOException if the file could not be loaded, or an
   *    error occurred while reading the file.
   * @throws org.freeciv.common.RegistryParseException if the file is
   *    badly formed and could not be parsed
   */
//  public void loadFile( String filename ) throws IOException, 
//    RegistryParseException
//  {
//    this.filename = filename;
//    loadFile( new InputFile( filename ) );
//  }

  /**
   * Load and parse the specified InputStream. 
   *
   * @param is the input stream to read from
   * @throws java.io.IOException if an error occurs reading from the stream
   * @throws org.freeciv.common.RegistryParseException if the input is badly
   *    formed and could not be parsed
   */
  public void load( InputStream is ) throws IOException, RegistryParseException
  {
    this.filename = null;
    loadFile( new InputFile( is ) );
  }

  private void loadFile( InputFile input ) throws IOException,
      RegistryParseException
  {
  
    InputFile inf;
    Section psection = null;
    Entry pentry = null;
    boolean table_state = false; /* 1 when within tabular format */
    int table_lineno = 0; /* row number in tabular, 0=top data row */
    // struct sbuffer sb
    String tok;
    int i;
    String base_name = "";
    String entry_name;
    ArrayList columns_tab = new ArrayList();
    String[] columns; // needed?
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "Loading " + filename );
    }

    inf = input;

    init();
    
    // ath_init(...)
    // sb = sf->sb;  // ln 380
    while( !inf.atEof() )
    {
      if( inf.getToken( inf.INF_TOK_EOL ) != null )
      {
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Swallowed a line" );
        }
        continue;
      }
      if( inf.atEof() )
      {
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Reached EOF" );
        }
        break;
      }
      tok = inf.getToken( inf.INF_TOK_SECTION_NAME );
      if( tok != null )
      {
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Got section name " + tok );
        }
        if( table_state )
        {
          throw new RegistryParseException( "New section during table" );
        }
        psection = new Section();
        psection.name = tok;
        psection.entries = new ArrayList();
        sections.add( psection );
        inf.getTokenRequired( inf.INF_TOK_EOL );
        continue;
      }
      if( psection == null )
      {
        throw new RegistryParseException( "data before first section" );
      }
      if( Logger.DEBUG )
      {
        Logger.log( Logger.LOG_DEBUG, "Trying to get EOT token" );
      }
      if( inf.getToken( inf.INF_TOK_TABLE_END ) != null )
      {
        if( !table_state )
        {
          throw new RegistryParseException( "misplaced \"}\"" );
        }
        inf.getTokenRequired( inf.INF_TOK_EOL );
        table_state = false;
        continue;
      }
      if( table_state )
      {
        i = -1;
        do
        {
          i++;
          inf.discardTokens( inf.INF_TOK_EOL );
          tok = inf.getTokenRequired( inf.INF_TOK_VALUE );
          if( i < columns_tab.size() )
          {
            entry_name = base_name + table_lineno + "." + columns_tab.get( i );
          }
          else
          {
            entry_name = base_name + table_lineno + "." + columns_tab.get( columns_tab.size() - 1 ) + "," + ( i - columns_tab.size() + 1 );
          }
          pentry = new Entry( entry_name, tok );
          psection.entries.add( pentry );
          if( Logger.DEBUG )
          {
            Logger.log( Logger.LOG_DEBUG, "Added tabular entry " + pentry.name + " = " + tok + " to section " + psection.name );
          }
          num_entries++;
        } while( inf.getToken( inf.INF_TOK_COMMA ) != null );
        inf.getTokenRequired( inf.INF_TOK_EOL );
        table_lineno++;
        continue;
      }
      tok = inf.getTokenRequired( inf.INF_TOK_ENTRY_NAME );
      base_name = tok;
      if( Logger.DEBUG )
      {
        Logger.log( Logger.LOG_DEBUG, "Got entry name " + tok );
      }
      inf.discardTokens( inf.INF_TOK_EOL );
      if( inf.getToken( inf.INF_TOK_TABLE_START ) != null )
      {
        i = -1;
        do
        {
          i++;
          inf.discardTokens( inf.INF_TOK_EOL );
          tok = inf.getTokenRequired( inf.INF_TOK_VALUE );
          if( tok.charAt( 0 ) != '\"' )
          {
            throw new RegistryParseException( "table column header non-string" );
          }
          columns_tab.add( tok.substring( 1, tok.length() - 1 ) ); // Ln 466
        }while( inf.getToken( inf.INF_TOK_COMMA ) != null );
        inf.getTokenRequired( inf.INF_TOK_EOL );
        table_state = true;
        table_lineno = 0;
        continue;
      }
      /* ordinary value */
      i = -1;
      do
      {
        i++;
        inf.discardTokens( inf.INF_TOK_EOL );
        tok = inf.getTokenRequired( inf.INF_TOK_VALUE );
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Got value " + tok );
        }
        if( i == 0 )
        {
          pentry = new Entry( /*sb,*/ base_name, tok );
        }
        else
        {
          entry_name = base_name + "," + i;
          pentry = new Entry( /*sb, */ entry_name, tok );
        }
        psection.entries.add( pentry );
        num_entries++;
      } while( inf.getToken( inf.INF_TOK_COMMA ) != null );
      inf.getTokenRequired( inf.INF_TOK_EOL );
    }
    if( table_state )
    {
      throw new RegistryParseException( "Finished file " + filename + " before end of table" );
    }
    inf.close();
    hashAll();
  }

  /**
   * Create a hashmap of all entries
   */
  private void hashAll()
  {
    hashd = new HashMap();
    for( int i = 0;i < sections.size();i++ )
    {
      Section sec = (Section)sections.get( i );
      for( int j = 0;j < sec.entries.size();j++ )
      {
        Entry ent = (Entry)sec.entries.get( j );
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Hashed " + sec.name + "." + ent.name );
        }
        hashd.put( sec.name + "." + ent.name, ent );
      }
    }
  }

  /**
   * Save the registry file back out. CURRENTLY UNIMPLEMENTED
   *
   * @param filename the file name to save
   * @throws java.lang.UnsupportedOperationException
   */
  public void saveFile( String filename )
  {
    throw new UnsupportedOperationException(
      "saveFile() is not yet implemented"
    );
  }

  /**
   * Look up a string in the registry
   *
   * @param path the path name to the registry string. E.g. sectionname.key
   * @return the value of the specified string, or null if it does not exist
   *    in the registry
   */
  public String lookupString( String path )
  {
    return lookupString( path, (Object[])null );
  }
  
  /**
   * Look up a string in the file, substituting the specified array of
   * parameters into the path before the lookup
   *
   * @param path the path name to the registry string, e.g. sectionname.key.
   *    this will contain one or more % delimited substitution parameters
   * @param subst an ordered array of items to substitute into the path
   *    of the specified registry string
   *
   * @return the value of the string at the specified path, with the
   *    items in subst substituted in.
   *
   * @throws java.lang.IllegalArgumentException if the substituted path
   *    does not exist.
   */
  public String lookupString( String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    if( ( pentry = lookupInternal( buf ) ) == null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " doesn't contain a " + buf + " entry." );
    // System exit!
    }
    if( pentry.svalue == null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " entry " + buf + " doesn't contain a string." );
    // System exit!
    }
    return pentry.svalue;
  }
  
  private class StringOrInt
  {
    public String string;
    public int integer;
  }
  
  public StringOrInt lookupStringOrInt( String path )
  {
    return lookupStringOrInt( path, null );
  }
  
  /**
   * Convenient in C. messy in java.
   */
  public StringOrInt lookupStringOrInt( String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    if( ( pentry = lookupInternal( buf ) ) == null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " doesn't contain a " + buf + " entry" );
    }
    StringOrInt si = new StringOrInt();
    if( pentry.svalue != null )
    {
      si.string = pentry.svalue;
    }
    else
    {
      si.integer = pentry.ivalue;
    }
    return si;
  }
  public void insertInt( int val, String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    pentry = insertInternal( buf );
    pentry.ivalue = val;
    pentry.svalue = null;
  }
  public void insertString( String sval, String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    pentry = insertInternal( buf );
    pentry.ivalue = 0;
    pentry.svalue = sval;
  }
  public int lookupInt( String path )
  {
    return lookupInt( path, null );
  }
  public int lookupInt( String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    if( ( pentry = lookupInternal( buf ) ) == null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " doesn't contain a " + buf + " entry." );
    // System exit!
    }
    if( pentry.svalue != null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " entry " + buf + " doesn't contain an integer." );
    // System exit!
    }
    return pentry.ivalue;
  }
  public int lookupInt( int def, String path )
  {
    return lookupInt( def, path, null );
  }
  public int lookupInt( int def, String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    if( ( pentry = lookupInternal( buf ) ) == null )
    {
      return def;
    // System exit!
    }
    if( pentry.svalue != null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " entry " + buf + " doesn't contain an integer." );
    // System exit!
    }
    return pentry.ivalue;
  }
  public String lookupString( String def, String path )
  {
    return lookupString( def, path, null );
  }
  public String lookupString( String def, String path, Object[] subst )
  {
    Entry pentry;
    String buf = subst == null ? path : substitute( path, subst );
    if( ( pentry = lookupInternal( buf ) ) == null )
    {
      return def;
    // System exit!
    }
    if( pentry.svalue == null )
    {
      throw new IllegalArgumentException( "Sectionfile " + getFilename() + " entry " + buf + " doesn't contain a string." );
    // System exit!
    }
    return pentry.svalue;
  }
  public boolean lookup( String path )
  {
    return lookup( path, null );
  }
  public boolean lookup( String path, Object[] subst )
  {
    String buf = subst == null ? path : substitute( path, subst );
    return ( lookupInternal( buf ) != null );
  }
  public int lookupListDimensions( String path )
  {
    return lookupListDimensions( path, null );
  }
  
  /**
   * Returns the number of elements in a "vector".
   * That is, returns the number of consecutive entries in the sequence:
   * "path,0" "path,1", "path,2", ...
   * If none, returns 0.
   */
  public int lookupListDimensions( String path, Object[] subst )
  {
    String buf = subst == null ? path : substitute( path, subst );
    int j = 0;
    while( lookup( "%s,%d", new Object[]
    {
      buf, new Integer( j )
    } ) )
    {
      j++;
    }
    return j;
  }
  public String[] lookupStringList( String path )
  {
    return lookupStringList( path, null );
  }
  
  /**
   *  Return a pointer for a list of integers for a "vector".
   * The return value is malloced here, and should be freed by the user.
   * The size of the returned list is returned in (*dimen).
   * If the vector does not exist, returns NULL ands sets (*dimen) to 0.
   */
  public String[] lookupStringList( String path, Object[] subst )
  {
    String buf = subst == null ? path : substitute( path, subst );
    int size = lookupListDimensions( buf, null );
    String[] lst = new String[ size ];
    for( int i = 0;i < size;i++ )
    {
      lst[ i ] = lookupString( "%s,%d", new Object[]
      {
        buf, new Integer( i )
      } );
    }
    return lst;
  }
  public int[] lookupIntList( String path )
  {
    return lookupIntList( path, null );
  }
  public int[] lookupIntList( String path, Object[] subst )
  {
    String buf = subst == null ? path : substitute( path, subst );
    int size = lookupListDimensions( buf, null );
    int[] lst = new int[ size ];
    for( int i = 0;i < size;i++ )
    {
      lst[ i ] = lookupInt( "%s,%d", new Object[]
      {
        buf, new Integer( i )
      } );
    }
    return lst;
  }
  private Entry lookupInternal( String fullPath )
  {
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "Looking up key " + fullPath );
    }
    Entry e = (Entry)hashd.get( fullPath );
    if( e == null && fullPath.endsWith( ",0" ) )
    {
      e = (Entry)hashd.get( fullPath.substring( 0, fullPath.length() - 2 ) );
    }
    else
    {
      if( e == null )
      {
        // Try ,0 notation for a laff
        e = (Entry)hashd.get( fullPath + ",0" );
      }
    }
    return e;
  }
  private Entry insertInternal( String fullPath )
  {
    Entry pentry;
    Section psection;
    int pdelim;
    String sec_name, ent_name;
    if( ( pdelim = fullPath.indexOf( '.' ) ) <= 0 )
    {
      throw new IllegalArgumentException( "Insertion fullpath " + fullPath + " missing '.' for sectionfile " + getFilename() );
    // Exit!!
    }
    sec_name = fullPath.substring( 0, pdelim - 1 );
    ent_name = fullPath.substring( pdelim + 1 );
    num_entries++;
    if( sec_name.length() == 0 || ent_name.length() == 0 )
    {
      throw new IllegalArgumentException( "Insertion fullpath " + fullPath + " missing " + ( sec_name.length() == 0 ? "section" : "entry" ) + " for sectionfile " + getFilename() );
    }
    for( int i = sections.size() - 1;i >= 0;i-- )
    {
      psection = (Section)sections.get( i );
      if( sec_name.equals( psection.name ) )
      {
        pentry = new Entry();
        pentry.name = ent_name;
        psection.entries.add( pentry );
        
        // Add to hash
        hashd.put( psection.name + "." + pentry.name, pentry );
        return pentry;
      }
    }
    psection = new Section();
    psection.name = sec_name;
    psection.entries = new ArrayList();
    sections.add( psection );
    pentry = new Entry();
    pentry.name = ent_name;
    psection.entries.add( pentry );
    hashd.put( psection.name + "." + pentry.name, pentry );
    return pentry;
  }
  
  /**
   * Get all section names with the specified prefix. Returns null
   * if none match. Case insensitive.
   */
  public ArrayList getSecNamesPrefix( String prefix )
  {
    ArrayList l = null;
    for( int i = 0;i < sections.size();i++ )
    {
      Section s = (Section)sections.get( i );
      if( s.name.toLowerCase().startsWith( prefix.toLowerCase() ) )
      {
        if( l == null )
        {
          l = new ArrayList();
        }
        l.add( s.name );
      }
    }
    return l;
  }

  /**
   * Get an iterator over all section names in the registry file.
   *
   * @return an iterator of Strings, each of which is a section name
   */
  public Iterator getSectionNames()
  {
    if ( sections == null )
    {
      return null;
    }
    ArrayList listCopy = new ArrayList( sections.size() );
    Iterator i = sections.iterator();
    while ( i.hasNext() )
    {
      listCopy.add( ((Section)i.next()).name );
    }

    return listCopy.iterator();
  }
  
  /**
   * Substitutes % parameters in the original string with object values.
   * Unlike the C version, this doesn't care what the letter after the
   * % is, it just substitutes parameters in the correct order using
   * the toString() method on the objects in the subst array.
   */
  private static String substitute( String original, Object[] subst )
  {
    int lastPos = -2;
    int nextPos;
    int curIndex = 0;
    nextPos = original.indexOf( '%' );
    if( nextPos < 0 )
    {
      return original;
    }
    StringBuffer bufStr = new StringBuffer( original.length() );
    while( nextPos >= 0 )
    {
      bufStr.append( original.substring( lastPos + 2, nextPos ) );
      if( original.charAt( nextPos + 1 ) == '%' )
      {
        bufStr.append( "%" );
      }
      else
      {
        bufStr.append( subst[ curIndex++ ].toString() );
      }
      lastPos = nextPos;
      nextPos = original.indexOf( '%', lastPos + 2 );
    }
    

    // append any remaining bits of the string.
    int lastSubst = original.lastIndexOf( '%' );
    if( lastSubst + 2 <= original.length() )
    {
      bufStr.append( original.substring( lastSubst + 2 ) );
    }
    return bufStr.toString();
  }
  public static void main( String[] args )
  {
    System.out.println( substitute( "%s.testing %s", new Object[]
    {
      "just", "something"
    } ) );
  
  /*
  Registry r = new Registry();
  
  if (args.length < 2)
  {
  System.err.println("Please specify a filename and a key to look up");
  System.exit(1);
  }
  
  String fname = args[0];
  String key = args[1];
  
  r.loadFile(fname);
  
  System.out.println(r.lookupString(key));
  
  */
  }
}
