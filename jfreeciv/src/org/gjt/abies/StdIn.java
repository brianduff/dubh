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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class StdIn
{

   public final static BufferedReader in =
      new BufferedReader( new InputStreamReader(System.in) );

   public static final int YES = 1;
   public static final int NO = -1;

   public static String readLine()
   {
      try {
         return in.readLine();
      } catch ( IOException e )
         {
            return null;
         }
   }

   public static int readYesNo()
   {
      while ( true )
      {
         String a = readLine();
         if ( a.equals(_("yes")) || a.equals(_("_yes.short")) )
            return YES;
         if ( a.equals(_("no")) || a.equals(_("_no.short")) )
            return NO;
         System.out.println(_("You have to answer yes or no"));
      }
   }

   private static String _(String str)
   {
      return Localize.translation.translate(str);
   }

}