// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhKawaPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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
import java.util.Enumeration;

/**
 * The base class providing common methods between all 
 * dubh kawa plugins.
 * @author Brian Duff
 * @version $Id: DubhKawaPlugin.java,v 1.1 1999-04-10 20:38:53 briand Exp $
 */
public class DubhKawaPlugin
{
   /**
    * Necessary to look up the correct instance of a KawaFile for
    * the current file in the project. Would be nice if there
    * was a method in KawaApp or KawaProject to do this.
    */
   protected static KawaFile getFile(String filename)
   {
      try
      {
         Enumeration files = KawaApp.getCurrentProject().enumerateAllFiles();
         
         while (files.hasMoreElements())
         {
            KawaFile f = (KawaFile) files.nextElement();
            
            if (f.getPath().equals(filename))
            {
               return f;
            }
         }
         
         KawaApp.out.println("Can't find "+filename+" in the project.");
         return new KawaFile(filename);
      }
      catch (Throwable t)
      {
         KawaApp.out.println("Unable to getFile("+filename+"): "+t);
         return new KawaFile(filename);
      }
   }

}

//
// $Log: not supported by cvs2svn $
//