// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FileMacroPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.dev.kawa.plugin;

import com.tektools.kawa.plugin.*;
import java.io.*;
import java.util.*;

import dubh.utils.dev.kawa.plugin.DubhKawaPlugin;
/**
 * Kawa plugin that reads from a specified file and pastes
 * its contents into the Kawa editor.
 * @author Brian Duff, bduff@uk.oracle.com
 * @version $Id: FileMacroPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
 */
public class FileMacroPlugin extends DubhKawaPlugin
{
   private static Hashtable m_storedText = new Hashtable();

   /**
    * Args as follows:
    *  0: the full path to the file to insert
    *  1: current path to file
    */
   public static void main(String[] args)
   {
      if (args.length < 1)
      {
         KawaApp.out.println("Not enough arguments to the FileMacroPlugin plugin.");
         System.exit(1);  
      }
   
      String text = (String) m_storedText.get(args[0]);
      
      if (text == null)
      {
         text = "";
         try
         {
            String line=" ";
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            while (line != null)
            {
               line = br.readLine();
               if (line != null) text = text + line + "\n";
            }
            m_storedText.put(args[0], text);
            br.close();
         }
         catch (IOException ioe)
         {
            KawaApp.out.println("Unable to read macro file: "+args[0]);
            return;
         }
      }   
      
      //
      // This is unpleasant, kawa plugin still has no way of getting the
      // current editor (only getting the editor for a particular file).
      //
      KawaEditor ed = getFile(args[1]).getEditor(true);
      
      ed.paste(text);
      
   }

}

//
// $Log: not supported by cvs2svn $
//