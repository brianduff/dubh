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

import java.io.File;
import java.io.IOException;

public class Dirs
{

	public static File lastDir;
	private static final String nl = System.getProperty("line.separator");

	private static void out( String str )
	{
		System.out.print(str);
	}

	private static void outln( String str )
	{
		System.out.println(str);
	}

	public static File chooseDir()
	{
		return chooseDir( _("Choose directory:") );
	}

	public static File chooseDir( String comment )
	{
		return chooseDir( comment, (lastDir != null ?
			lastDir : new File(System.getProperty("user.dir")) ) );

	}

	public static File chooseDir( File proposed )
	{
		return chooseDir( _("Choose directory:"), proposed );
	}

	public static File chooseDir( String comment, File proposed )
	{
		outln(comment + "=--> [" + proposed + _("] (?-help) "));
		while ( true )
		{
			String cmd = StdIn.readLine();
			if ( cmd.length() == 0 )
			{
				if ( checkDir(proposed) )
					return proposed;
				continue;
			}
			boolean isCommand =
				 (cmd.length() == 1) || (cmd.charAt(1) == ' ');
			String arg;
			File path;

			if ( !isCommand )
				arg = cmd;
			else
				arg = (cmd.length() >= 2) ?
					cmd.substring(2,cmd.length() ) : "";

			if ( "".equals(arg) )
				path = proposed;
			else
			{
				path = new File(arg);
				if ( !path.isAbsolute() )
					path = new File(proposed, arg);
			}

			if ( !isCommand )
			{
				if ( checkDir(path) )
					return path;
				outln("=--> [" + proposed + _("] (? - help)"));

				continue;
			}

			switch ( cmd.charAt(0) )
			{
				case '?':
				default:
					writeHelp(comment);
					break;
				case 'l':
					out(listDir(path));
					break;
				case 'c':
					if ( "".equals(arg) )
						proposed = new File(System.getProperty("user.dir"));
					else
						try {
							proposed = new File(path.getCanonicalPath());
						} catch (IOException e )
							{
								outln(e.toString());
							}
					break;
				case 'm':
					makeDirs(path);
					break;
			}
			outln("=--> [" + proposed + _("] (? - help)"));
		}
	}

	public static String listDir( File dir )
	{
		if ( !dir.exists() )
		{
			return _("No such directory");
		}

		if ( !dir.isDirectory() )
		{
			return dir.getPath() + _(" is not a directory");
		}

		String[] list = dir.list();
		StringBuffer sb = new StringBuffer(20*list.length);
		for ( int i =0; i < list.length; i++ )
		{
			File f = new File(dir,list[i]);
			if ( f.isDirectory() )
				sb.append('[').append(list[i]).append(']');
			else
				sb.append(list[i]);
			sb.append(nl);
		}
		return sb.toString();
	}

	public static void makeDirs( File path )
	{
		path.mkdirs();
	}



	public static boolean checkDir( File dir )
	{
		if ( !dir.exists() )
		{
			outln(dir + _(" - no such directory - do you want me to create it ?"));
			if ( StdIn.readYesNo() == StdIn.YES )
				dir.mkdirs();
			else
				return false;
		}

		if ( !dir.exists() )
		{
			outln(_("Cannot create dir ") + dir );
			return false;
		}

		if ( !dir.isDirectory() )
		{
			outln(_("It is not a directory") );
			return false;
		}
		return true;

	}

	private final static String[] help =
	{
		"You may browse and choose directories with following commands:",
		"?             displays this help",
		"/some/dir     selects /some/dir",
		"some/dir      selects [current_dir]/some/dir",
		"<ENTER>       selects current dir (one shown in [] )",
		"l             lists current dir",
		"l /some/dir   lists /some/dir",
		"l some/dir    lists [current_dir]/some/dir",
		"c             change current directory to user.dir",
		"c /some/dir   change current directory to /some/dir",
		"c some/dir    change current directory to [current_dir]/some/dir",
		"m /some/dir   create /some/dir",
		"m some/dir    create [current_dir]/some/dir"
	};

	private static void writeHelp( String comment )
	{
		outln("");
		outln("");
		outln(comment);
		outln("----------------------------------------");
		for ( int i =0; i < help.length; i++ )
			outln(_(help[i]));
	}

	private static String _(String str)
	{
		return Localize.translation.translate(str);
	}



	public static void main(String[] args)
	{
		System.out.println(_("CHOOSEN = ") + chooseDir() );
		StdIn.readLine();
	}




}