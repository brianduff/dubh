package org.freeciv.client;
import org.gjt.abies.TranslationFile;
public class Localize
{
  public static TranslationFile translation;
  static
  {
    translation = TranslationFile.createTranslationFile( Localize.class.getClassLoader(), "org/freeciv/client/translation" );
    if( translation == null )
    {
      translation = new TranslationFile();
    }
    translation.setVerbose( false );
    translation.setDevelopmentOutput( "freecivclient.translation" );
  }
}
