/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please note that this software is not in any way endorsed by
 * Oracle Corporation
 * Version History:
 *  FV   DUV    Date          Who    What
 *  ======================================================================
 *  0.0  0.0.01 [22/Nov/1998] BD     Initial Revision
 *
 */

package dubh.utils.misc;

import java.io.*;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;
import java.util.Hashtable;

/**
 * <p>
 * Creates, updates and manages serialised version information at both
 * buildtime and runtime.
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 0.0.01) [22/Nov/1998]
 */
public class VersionManager
{
   private static final String DEFAULT_VERFILE = "version.dat";
   
   private static final String CMD_BASECREATE = "basecreate";
   private static final String CMD_BASEEDIT   = "baseedit";
   private static final String CMD_INCBUILD   = "incbuild";
   private static final String CMD_SHOW       = "show";

   private static VersionManager m_instance = null;


   private Hashtable m_versions;


   private VersionManager()
   {
      m_versions = new Hashtable();
   }


/************************
 * RUNTIME SUPPORT
 ************************/

   public static VersionManager getInstance()
   {
      if (m_instance == null) m_instance = new VersionManager();
      return m_instance;
   }


   /**
    * Get version information
    * @param package The package that contains version information
    * @return a version object containing version information for the given
    *   package. If this is the first time you have called getVersion for
    *   a specific package, the version information will first be
    *   deserialised and stored in a cache. On subsequent calls, the 
    *   cached copy will be returned. Version information is immutable
    *   at runtime.
    * @throws IllegalArgumentException if the version information for the
    *   specified package couldn't be read.
    */
   public ReadOnlyVersion getVersion(String pkg)
      throws IllegalArgumentException
   {
      if (m_versions.contains(pkg))
      {
         return (ReadOnlyVersion) m_versions.get(pkg);
      }
      else
      {
         try {
            return deserializeVersion(pkg);
         }
         catch (Throwable t)
         {
            throw new IllegalArgumentException("Unable to read version from package");
         }  
      }
   }
   
   private ReadOnlyVersion deserializeVersion(String pkg) throws
      IOException, StreamCorruptedException, OptionalDataException, 
      ClassNotFoundException, ClassCastException
   {

      ObjectInputStream ois = new ObjectInputStream(
         ClassLoader.getSystemResource(
            packageToPath(pkg)+'/'+DEFAULT_VERFILE
         ).openStream()
      );
      
      Version v = (Version) ois.readObject();
      
      ois.close();
      
      m_versions.put(pkg, v);
      
      return (ReadOnlyVersion) v;
   }
   
   
   private String packageToPath(String pkg)
   {
      return pkg.replace('.', '/');
   }
   
   private String pathToPackage(String path)
   {
      return path.replace('/', '.');
   }
   
   
   
   /**
    * Args are:
    *  [0] - command
    *          basecreate: create a baseline version file. Pops up
    *          a UI asking for parameters for the version file.
    *          baseedit:   edit a baseline version file. Pops up
    *          a UI asking for parameters for the version file.
    *          incbuild:   increment the build number of the
    *          base version and copy it to the destination location
    *          show:       show the long version description of
    *          the base version.
    *  [1] - base version file
    *  [2] (optional) - destination version file directory
    */
   public static void main(String[] args)
   {
      if (args.length < 2)
      {
         System.err.println("Not enough arguments.");
         System.exit(1);
      }
      
      if (args[0].equalsIgnoreCase(CMD_BASECREATE))
         bt_baseCreate(args[1]);
      else if (args[0].equalsIgnoreCase(CMD_BASEEDIT))
         bt_baseEdit(args[1]);
      else if (args[0].equalsIgnoreCase(CMD_INCBUILD))
      {
         if (args.length < 3)
         {
            System.err.println(CMD_INCBUILD+" command requires a destination version directory.");
            System.exit(1);
         }
         bt_incBuild(args[1], args[2]);
      }
      else if (args[0].equalsIgnoreCase(CMD_SHOW))
         bt_show(args[1]);
   }
   
   
   private static final void bt_baseCreate(String baseVer)
   {
      Version v = new Version();
      v = VersionEditorPanel.edit(v);
      if (v != null)
      {
         bt_serializeVersion(v, baseVer);
      }
      System.exit(0);
   }
   
   private static final void bt_baseEdit(String baseVer)
   {
      Version v = bt_deserializeVersion(baseVer);
      v = VersionEditorPanel.edit(v);
      if (v != null)
      {
         bt_serializeVersion(v, baseVer);
      }
      System.exit(0);
   }
   
   private static final void bt_incBuild(String baseVer, String destVer)
   {
      Version v = bt_deserializeVersion(baseVer);
      
      v.setBuildNumber(v.getBuildNumber()+1);
      v.setReleaseDate(new Date());
      bt_serializeVersion(v, baseVer);
      bt_serializeVersion(v, destVer + '/' + DEFAULT_VERFILE);
   }
   
   private static final void bt_show(String baseVer)
   {
      Version v = bt_deserializeVersion(baseVer);
      System.out.println(v.getLongDescription());
   }

   private static final Version bt_deserializeVersion(String verfile)
   {
      try {
         File f = new File(verfile);
         ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(f)
         );
         
         Version v = (Version) ois.readObject();
         
         ois.close();
         
         return v;
      
      }
      catch (Throwable t)
      {
         System.err.println("Unable to deserialize "+verfile);
         System.exit(1);
      }
      return null;
   }
   
   private static final void bt_serializeVersion(Version v, String verfile)
   {
      try {
         File f = new File(verfile);
         
         ObjectOutputStream os = new ObjectOutputStream(
            new FileOutputStream(f)
         );
         
         os.writeObject(v);
         os.close();
      }
      catch (Throwable t)
      {
         System.err.println("Unable to serialize "+verfile);
         System.exit(1);
      }
   
   }

   
}
