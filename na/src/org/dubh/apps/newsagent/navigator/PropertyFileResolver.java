// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: PropertyFileResolver.java,v 1.3 1999-12-12 03:31:51 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE. 
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package org.javalobby.apps.newsagent.navigator;

import java.net.URL;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Properties;

import org.javalobby.dju.misc.Debug;
import org.javalobby.dju.misc.UserPreferences;

import org.javalobby.apps.newsagent.GlobalState;

import javax.swing.Icon;
import javax.swing.ImageIcon;
/**
 * Utility class to find a property file. 
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: PropertyFileResolver.java,v 1.3 1999-12-12 03:31:51 briand Exp $
 */
public class PropertyFileResolver 
{  

   /**
    * Get the speified icon. The filename value is looked up using 
    * the following:
    *
    * 1) As absolute filename on filesystem
    * 2) As a relative filename from the current directory
    * 3) As relative filename from the NewsAgent configuration directory
    * 4) As relative path to image resource in the CLASSPATH    
    */
   public static Icon getIcon(UserPreferences prefs, String key)
   {
      String rootdir = GlobalState.getDataDirectory();
   
      String fileName = prefs.getPreference(key);
      
      if (fileName == null) return null;
      
      File f = new File(fileName);
      
      if (f.exists())
      {
         return new ImageIcon(fileName);
      }
      
      // Ok. Try a relative path from the NewsAgent dir.
      String fullName = rootdir+File.separator+fileName;
      
      File fi = new File(fullName);
      
      if (fi.exists())
      {
         return new ImageIcon(fullName);
      }
      
      // Last try; let's hunt around for a resource inside the CLASSPATH.
      java.net.URL imgResource = ClassLoader.getSystemResource(fileName);
      
      if (imgResource != null) 
      {
         return new ImageIcon(imgResource);
      }
      
      
      // TODO: Should have a default image.
      
      if (Debug.TRACE_LEVEL_1)
      {
         Debug.println(1, PropertyFileResolver.class, "Can't load image: "+fileName);
      }
      
      return null;
      
   }
    

   /**
    * Get a UserPreferences object, creating the file if necessary.
    */
   public static UserPreferences getPreferences(String pathFromData)
   {
      String base = GlobalState.getDataDirectory();
      String fullName = base+File.separator+pathFromData;
      File f = new File(fullName);
      
      try
      {
         if (!f.exists())
         {
            if (!f.createNewFile() || !f.isFile())
            {
               if (Debug.TRACE_LEVEL_1)
               {
                  Debug.println(1, PropertyFileResolver.class,
                      "Unable to create a new file: "+fullName
                  );
               }
               return null;
            }
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, PropertyFileResolver.class,
                  "Erm. Managed to create a new directory instead of a file! "+fullName
               );
            }
         }
         
         // Should have a valid file at this point
         return new UserPreferences(f);
      }
      catch (IOException ioe)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, PropertyFileResolver.class,
               "IOException trying to create UserPreferences file "
            );
            Debug.printException(1, PropertyFileResolver.class, ioe);
         }
      }
      return null;
   }

   /**
    * Get a properties object from a file. 
    *
    * The method will first look in newsagent-dir/storagePath/fileName
    * if the file isn't found there, it will load it from
    * defaultPath/fileName within the CLASSPATH and then
    * copy the file to newsagent-dir/storagePath/fileName.
    * May return null if the file doesn't exist in the CLASSPATH
    */
   public static UserPreferences getDefaultedProperties(String storagePath, String fileName, String defaultPath, String defaultFile)
   {
      // First look in local storage
      String dir = GlobalState.getDataDirectory();
      
      String localFileName = dir+File.separator+storagePath+File.separator+fileName;
      
      File localfile = new File(localFileName);
      
      if (Debug.TRACE_LEVEL_3)
      {
         Debug.println(3, PropertyFileResolver.class, "Trying localfile "+localFileName);
      }
      
      if (localfile.exists() && localfile.isFile())
      {
         try
         {
            UserPreferences p = new UserPreferences(localfile);
            if (Debug.TRACE_LEVEL_3)
            {
               Debug.println(3, PropertyFileResolver.class, "Success getting properties from "+localFileName);
            }
            return p;
         }
         catch (IOException ioe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, PropertyFileResolver.class, "Unable to load file "+localFileName);
               Debug.printException(1, PropertyFileResolver.class, ioe);
            }
         }
      }
      
      // Ok that didn't work. Maybe it's in the CLASSPATH.
      
      
      String resourceName = defaultPath+"/"+defaultFile;

      if (Debug.TRACE_LEVEL_3)
      {
         Debug.println(3, PropertyFileResolver.class, "Trying resource "+resourceName);
      }
      
      URL resource = ClassLoader.getSystemResource(resourceName);

      if (Debug.TRACE_LEVEL_3)
      {
         Debug.println(3, PropertyFileResolver.class, "Resolved URL is "+resource);
      }


      if (Debug.TRACE_LEVEL_3)
      {
         Debug.println(3, PropertyFileResolver.class, "Trying resource "+resource);
      }

      
      if (resource != null)
      {
         try
         {
            UserPreferences p = new UserPreferences(resource.openStream());


            if (Debug.TRACE_LEVEL_3)
            {
               Debug.println(3, PropertyFileResolver.class, "Successfuly retrieved default properties from CLASSPATH");
            }
            
            // Ok, got some properties. Now write them out to the localfile.
            FileOutputStream fos = new FileOutputStream(localFileName);
            
            //p.save(fos, "Automatically generated from default "+resourceName);

            if (Debug.TRACE_LEVEL_3)
            {
               Debug.println(3, PropertyFileResolver.class, "Attempting to save to "+localFileName);
            }
            
            File f = new File(localFileName);
            f.createNewFile();
            
            p.setFile(new File(localFileName));
            p.save();
            
            return p;
         }
         catch (IOException ioe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, PropertyFileResolver.class, "Unable to load system resource "+resourceName+" or output to "+localFileName+" failed.");
               Debug.printException(1, PropertyFileResolver.class, ioe);
            }
         
         }
      }

      if (Debug.TRACE_LEVEL_1)
      {
         Debug.println(1, PropertyFileResolver.class, "Failed to find properties "+resource);
      }
      
      // All else has failed, return null
      return null;
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//