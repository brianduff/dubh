package org.freeciv.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public final class RealSndSystem extends SndSystem
{

	HashMap nameMap = new HashMap(100);
	HashMap sndMap = new HashMap(100);
	String dir;

	public RealSndSystem(String aDir) throws IOException
	{
		dir = aDir;
		BufferedReader br = new BufferedReader(new FileReader(
			new File(dir,"sndconf")));
		String line;
		while ( (line = br.readLine()) != null )
		{
			if ( line.length() == 0 || line.charAt(0) == '#' )
				continue;
			line = line.trim();
			int colon = line.indexOf(':');
			String before = line.substring(0,colon).trim();
			String after = line.substring(colon+1);
			int hash = after.indexOf('#');
			if ( hash >=0 )
				after = after.substring(0,hash).trim();
			else
				after = after.trim();
			nameMap.put(before,after);
		}
	}

	public AudioClip getAudioClip(String id)
	{
		AudioClip ac = (AudioClip)sndMap.get(id);
		if ( ac == null )
		{
			try {
				String file = (String)nameMap.get(id);
				if ( file != null )
					ac = Applet.newAudioClip( new File(dir,file).toURL() );
				else
					System.out.println(_("Illegal sound name used: ") + id);
				sndMap.put(id,ac);
			} catch (MalformedURLException e)
				{
					System.out.println(e);
					e.printStackTrace();
				}
		}
		return ac;
	}

	public void play(String id)
	{
		AudioClip ac = getAudioClip(id);
		if ( ac!= null )
			ac.play();
	}

   

	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}


}