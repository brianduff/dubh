/*
 * Written by Artur Biesiadowski <abies@pg.gda.pl>
 * This file is public domain - you can use/modify/distribute it as long
 * as some credit is given to me. You are not required to keep it open
 * sourced, nor to give back all changes to me, BUT if you do, everybody
 * will benefit.
 * For latest version contact me at <abies@pg.gda.pl> or check
 * http://www.gjt.org/servlets/JCVSlet/list/gjt/top.org.gjt.abies
 * This file comes with no guarantee of anything - you have been WARNED.
 */
 
package org.gjt.abies;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * This class is designed to be used instead of java.util.ResourceBundle.<BR>
 * java.util.ResourceBundle works almost well, but have some limitations, which
 * makes using it in more complicated applications hard, especially at
 * developement stage.<BR>
 * Two main differences are: <BR>
 * <B>Allows use of any characters in keys, including newlines, '=' sign etc.</B><BR>
 * <B>When it is fed with unknown key, it returns it unchanged and take a note
 * in missingTranslations vector</B><BR>
 * You do not have to create default translations file and update it every time
 * you will add some string to your program !! This is done automatically. Also
 * doing translations is easier - just run the program and look for missing
 * translations. When program changes old translation file can be used, and
 * new strings will be just written as english (or whatever lang you use in
 * program) - a lot better thing that breaking program with exception. You
 * can later look in missingTranslations to track offending strings.<BR>
 * This class also supports retriving its data, so you can edit translations
 * on line if you want.<BR>
 * Sample code for initialization of TranslationFile follows (such class can/should
 * be created for each package separately)
 * <PRE>
class Localize
{
	static TranslationFile translation;
	static {
		translation =
			TranslationFile.createTranslationFile(Localize.class.getClassLoader(),
				"com/foo/bar/translation" );
		if ( translation == null )
			translation = new TranslationFile();
		translation.setVerbose(false);
        translation.setDevelopmentOutput("foo.bar.translation");
	}
}
 * </PRE>
 * If you would like to catch missing translations to file, to allow easy
 * replacement of them, invoke VM with -Dtranslation.out=/some/dir option -
 * snapshot of translation will be saved under name of /some/dir/foo.bar.translation
 * @see #translate
 */

public class TranslationFile
{
	/**
	 * Pairs of &lt String,String &gt are kept here; key is original text,
	 * value is translated text
	 */
	protected Hashtable table = new Hashtable(251);
	/**
	 * This vector of strings keep all translations not found in table
	 */
	transient protected Vector missingTranslations = new Vector();
	/**
	 * This is true if any translations have been added/removed from this object
	 */
	transient protected boolean changed = false;

	transient protected boolean verbose = false;

	transient protected String developmentOutput;

	public static boolean output = (System.getProperty("translation.out")!= null);

	/**
	 * Default constructor
	 * @see #load
	 */
	public TranslationFile()
	{
	}

	/**
	 * This constructor helps you to convert std resource bundle in
	 * TranslationFile
	 * @param res ResourceBundle to be converted
	 */
	public TranslationFile( ResourceBundle res )
	{
		for ( Enumeration e = res.getKeys(); e.hasMoreElements(); )
		{
			String key = (String)e.nextElement();
			String val = res.getString(key);
			table.put(key,val);
		}
	}

	/**
	 * TranslationFile is read from file; for format of file see save method
	 * @param file file from which translations are read
	 * @exception IOException if any IO error occurs
	 */
	public TranslationFile( File file ) throws IOException
	{
		load( new BufferedInputStream(new FileInputStream(file)) );
	}

    /**
	 * TranslationFile is read from stream ; for format of file see save method
	 * @param is stream from which translations are read
	 * @exception IOException if any IO error occurs
	 */

	public TranslationFile( InputStream is ) throws IOException
	{
		load( is );
	}

    /**
	 * TranslationFile is read from reader ; for format of file see save method
	 * @param r reader from which translations are read
	 * @exception IOException if any IO error occurs
	 */
	public TranslationFile( Reader r ) throws IOException
	{
		load(r);
	}

	/**
	 * Basic method of this class - translates given string. If no translation
	 * is found, it adds give string to missingTranslations vector and
	 * returns string unchanged
	 * @param original string to translate
	 * @return localized version of string (always not-null)
	 * @see #getMissingTranslations
	 */
	public String translate( String original )
	{
		Object answer = table.get(original);
		if ( answer != null )
			return (String)answer;
		else
			return translationMiss(original);
	}
	
	/**
	 * Similar to translate method but if translation does not exist, it just
	 * returns null - main use for it is in development of translation file
	 * @param original string to translate
	 * @return localized string, or null if not found
	 */
	public String getTranslation( String original )
	{
		return (String) table.get(original);
	}

	/**
	 * Sets translation for given string. If translation already exists, this
	 * method replaces it. To be used in development mode.
	 * @param original string in original form
	 * @param translation localized version
	 */
	public void setTranslation( String original, String translation )
	{
		changed = true;
		table.put(original,translation);
	}
	
	/**
	 * To be used in development mode.
	 * @return all key strings in this translation bundle
	 */
	public Enumeration getAllKeys()
	{
		return table.keys();
	}

	/**
	 * This method is called when tarnslate method does not find translation in
	 * table. It can be overridden to perform some other actions. (normally it
	 * prints missing string to stdout and adds it to missingTranslationsvector)
	 * @param text key to missing translation
	 * @return what should be substituted for this text (normally just text itself is returned)
	 */	
	protected String translationMiss( String text )
	{
		changed = true;
		if ( verbose )
			System.out.println("Missing translation of \"" + text + '"');
		missingTranslations.addElement(text);
		table.put(text,text);
		if ( output )
			saveDevelopmentVersion();
		return text;
	}

	/**
	 * @return array with all translations missing to that point
	 * @see #clearMissingTranslations
	 */	
	public String[] getMissingTranslations()
	{
		String[] answer = new String[missingTranslations.size()];
		missingTranslations.copyInto(answer);
		return answer;
	}

	/**
	 * To be used in development mode. Removes one entry from missingTranslations
	 * vector;
	 * @param key enrty to be removed
	 */
	public void removeMissingTranslation( String key )
	{
		missingTranslations.removeElement(key);
	}
	
	/**
	 * Clears entire missingTranslations vector. Can be used after taking care of
	 * them, by for example dumping them to file.
	 * @see #getMissingTranslations
	 */
	public void clearMissingTranslations()
	{
		missingTranslations.removeAllElements();
	}

	/**
	 * @return true if any translations have been added/removed from this object
	 */
	public boolean isChanged()
	{
		return changed;
	}

	/**
	 * Writes out this TranslationFile to output stream. Format of output is:<BR>
	 * original text\\n<BR>
	 * localized text\\n<BR>
	 * and so on for every entry. Strings are escaped - \n \t \r are written as
	 * escape sequences and characters > 127 are written in \\ uXXXX form
	 * @param os stream to write to
	 * @exception IOException if error occurs during save
	 */
	public void save( OutputStream os ) throws IOException
	{
		save(new OutputStreamWriter(os) );
	}


	/**
	 * Writes out this TranslationFile to output writer. Format of output is:<BR>
	 * original text\\n<BR>
	 * localized text\\n<BR>
	 * and so on for every entry. Strings are escaped - \n \t \r are written as
	 * escape sequences and characters > 127 are written in \\ uXXXX form.<BR>
     * Note that Writer is treated just like 7-bit stream - no chars &gt 127 are written to it.
	 * @param w writerstream to write to
	 * @exception IOException if error occurs during save
	 */
	public void save( Writer w ) throws IOException
	{
		PrintWriter ps = new PrintWriter(w);
		String key;
		for ( Enumeration e = table.keys(); e.hasMoreElements();)
		{
			key = (String)e.nextElement();
			String val = (String)table.get(key);
			ps.println(escapeString(key));
			ps.println(escapeString(val));
		}
	}

	/**
	 * Loads data from given input stream. If table is already filled up, it is first
	 * cleared !!! - no concatenation occurs.
	 * @param is stream from which data is read
	 * @exception IOException if any error occurs during load
	 */
	public void load( InputStream is ) throws IOException
	{
		load(new InputStreamReader(is));
	}

	/**
	 * Loads data from given input stream. If table is already filled up,
     * it is first cleared !!! - no concatenation occurs.
	 * @param r reader from which data is read
	 * @exception IOException if any error occurs during load
	 */

	public void load( Reader r ) throws IOException
	{
		BufferedReader br = new BufferedReader(r);
		table.clear();
		clearMissingTranslations();
		String key;
		while ( (key = unescapeString(br.readLine()))!= null )
		{
			String val = unescapeString(br.readLine());
			table.put(key,val);
		}
	}

    /**
     * Controls verbosity of TranslationFile - it set to true every missing translation
     * is reported to stdout (useful for development).
     * @param aVerbose new verbosity setting
     */
	public void setVerbose( boolean aVerbose )
	{
		verbose = aVerbose;
	}

    /**
     * Saves all current translations, including all that were missing.
     * File is saved to System.getProperty("translation.out") +
     * this.developmentOutput.
     */
	public void saveDevelopmentVersion()
	{
		try {
            File f = new File(System.getProperty("translation.out"));
            if ( developmentOutput != null )
                f = new File(f,developmentOutput);
			FileWriter fw = new FileWriter(f);
			save(fw);
			fw.close();
		} catch ( IOException e )
			{
				System.out.println(e.toString());
			}
	}

    /**
     * Sets name of file to which snapshots of TranslationFile will be
     * saved during development stage.
     * @param anOutputName name of file
     */
     public void setDevelopmentOutput(String anOutputName )
     {
        developmentOutput = anOutputName;
     }

//---- STATIC UTIL METHODS
	
	/**
	 * <B>STATIC UTILITY METHOD:</B> escapes special characters in strings
	 */

	public static String escapeString( String text )
	{
		int len = text.length();
		StringBuffer answer = new StringBuffer(len * 2);
		for ( int i =0; i < len; i++ )
		{
			char ch = text.charAt(i);
			switch (ch)
			{
				case '\n':
					answer.append("\\n");
					break;
				case '\r':
					answer.append("\\r");
					break;
				case '\t':
					answer.append("\\t");
					break;
				default:
					if ( ch < (char)6 || ch > (char)127 )
						answer.append(unicodeEscape(ch));
					else
						answer.append(ch);
					break;
			}
		}
		return answer.toString();
	}

	/**
	 * <B>STATIC UTILITY METHOD:</B> unescapes special characters in strings
	 */
	public static String unescapeString( String text )
	{
		if ( text == null )
			return null;
		int len = text.length();
		StringBuffer answer = new StringBuffer(len);
		for ( int i =0; i < len; i++ )
		{
			char ch = text.charAt(i);
			if ( ch != '\\' )
			{
				answer.append(ch);
			}	
			else
			{
				i++;
				ch = text.charAt(i);
				switch ( ch )
				{
					case 'n':
						answer.append('\n');
						break;
					case 'r':
						answer.append('\r');
						break;
					case 't':
						answer.append('\t');
						break;
					case 'u':
						int shift = 12;
						int val = 0;
						while ( shift >= 0 )
						{
							i++;
							ch = text.charAt(i);
							if ( ch >= '0' && ch <= '9' )
							{
								val += (int)(ch - '0') << shift;
							}
							else if ( ch >= 'a' && ch <= 'f' )
							{
								val += (int)(ch - 'a' + 10) << shift;
							}
							else if (ch >= 'A' && ch <= 'F')
							{
								val += (int)(ch - 'A' + 10) << shift;
							}
							else
							{
								System.out.println("Error in escape sequence ");
								return answer.toString();
							}
							shift -= 4;
						}
						answer.append((char) val);
						break;
					default:
						System.out.println("Unrecognized escape sequence \\" +ch);
						return answer.toString();
				}
			}	
		}
		return answer.toString();
	}

	/**
	 * <B>STATIC UTILITY METHOD:</B> converts character to unicode escape form
	 */
	public static String unicodeEscape( char ch )
	{
		int val = (int) ch;
		char[] answer = new char[6];
		answer[0] = '\\';
		answer[1] = 'u';
		for ( int i =5; i >= 2; i-- )
		{
			int part = val & 0xf;
			if ( part < 10 )
				answer[i] = (char)('0' + part);
			else
				answer[i] = (char)(('a' + part) - 10);
			val >>= 4;
		}
		return new String(answer);
	}

	public static InputStream findResourceStream( ClassLoader cl,String basename, Locale loc )
	{
		int index;
		basename = basename + '_' + loc.toString();

		while (true)
		{
			InputStream is;
			is = cl.getResourceAsStream(basename);
			if ( is != null )
				return is;
			index = basename.lastIndexOf('_');
			if ( index > 0 )
				basename = basename.substring(0,index);
			else
				break;
		}

		return null;

	}

	public static InputStream findResourceStream(ClassLoader cl, String basename )
	{
		return findResourceStream( cl,basename, Locale.getDefault() );
	}

	public static File findResourceFile( File path, String basename, Locale loc)
	{
		int index;
		basename = basename + '_' + loc.toString();

		while (true)
		{
			File tryfile;
			tryfile = new File(path,basename);
			if ( tryfile.exists() )
				return tryfile;
			index = basename.lastIndexOf('_');
			if ( index > 0 )
				basename = basename.substring(0,index);
			else
				break;
		}

		return null;
	}
	
	public static File findResourceFile( File path, String basename )
	{
		return findResourceFile(path,basename,Locale.getDefault());
	}

	public static TranslationFile createTranslationFile( File path,
		String basename, Locale loc)
	{
		File f = findResourceFile(path, basename, loc);
		if ( f != null )
		{
			try {
				return new TranslationFile(f);
			} catch ( IOException e )
				{
					return null;
				}
		}
		return null;
	}

	public static TranslationFile createTranslationFile( File path,
		String basename )
	{
		return createTranslationFile( path, basename, Locale.getDefault());
	}

	public static TranslationFile createTranslationFile( ClassLoader cl,
		String basename, Locale loc )
	{
		InputStream is = findResourceStream(cl,basename,loc);
		if ( is != null )
		{
			try {
				return new TranslationFile( new BufferedInputStream(is) );
			} catch ( IOException e )
				{
					return null;
				}
		}
		return null;
	}

	public static TranslationFile createTranslationFile( ClassLoader cl,
		String basename )
	{
		return createTranslationFile(cl,basename,Locale.getDefault());
	}

	
//+---
}
