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

import org.gjt.abies.TranslationFile;

class Localize
{
	static TranslationFile translation;
	static {
		translation =
			TranslationFile.createTranslationFile(Localize.class.getClassLoader(),
				"org/gjt/abies/translation" );
		if ( translation == null )
			translation = new TranslationFile();
		translation.setVerbose(false);
        translation.setDevelopmentOutput("abies.translation");
	}
}