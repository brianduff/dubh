// ---------------------------------------------------------------------------
//   Dubh Servlets and Tools
//   $Id: FindNewFiles.java,v 1.1.1.1 2001-06-03 05:07:03 briand Exp $
//   Copyright (C) 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
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

package org.dubh.tool.findnew;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.net.URLEncoder;

/**
 * The main class for the "Find New Files" utility.
 */
public class FindNewFiles
{
   private static final String[] BANNED_EXTENSIONS =
   {
      "gif", "wav", "au", "jpg", "jpeg", "bmp"
   };

   private static final FileRecognizer[] RECOGNIZERS =
   {
      new HTMLFileRecognizer(),
      new SimpleRecognizer()
   };

   private static boolean isIgnored(File f)
   {
      String fName = f.getName();
      for (int i=0; i < BANNED_EXTENSIONS.length; i++)
      {
         if (fName.endsWith(BANNED_EXTENSIONS[i]))
         {
            return true;
         }
      }
      return false;
   }

   private static FileInformation recognize(File f)
   {
      for (int i=0; i < RECOGNIZERS.length; i++)
      {
         if (RECOGNIZERS[i].isRecognizedFile(f))
         {
            return RECOGNIZERS[i].getInformation(f);
         }
      }
      return null;
   }

   private static void displayFileXML(String startPath, File f)
   {
      FileInformation info = recognize(f);

      if (info != null)
      {
         System.out.println("<file>");
         System.out.println("  <title>"+info.getTitle()+"</title>");
         System.out.println("  <author>"+info.getAuthor()+"</title>");
         System.out.println("  <description>"+info.getDescription()+"</title>");
         System.out.println("  <revision>"+info.getRevision()+"</revision>");
         System.out.println("  <modified>"+getDateDescription(f.lastModified(), Calendar.getInstance())+"</modified>");
         System.out.println("  <url>"+getRelativeURL(startPath, f)+"</url>");
         System.out.println("</file>");
      }
   }

   public static final void main(String[] args)
   {
      newFilesXML("C:\\My Music", 5);
   }

   public static void newFilesXML(String startPath, long daysOld)
   {
      File start = new File(startPath);
      if (!start.isDirectory())
      {
         throw new IllegalArgumentException("start path must be a directory");
      }
      // Convert days to milliseconds
      long msOld = 1000 * 60 * 60 * 24 * daysOld;

      List l = findFiles(startPath, msOld);
      Collections.sort(l, new FileSorter());

      Calendar now = Calendar.getInstance();
      System.out.println("<newfiles>");

      Iterator i = l.iterator();
      while (i.hasNext())
      {
         File f = (File)i.next();
         displayFileXML(startPath, f);
      }

      System.out.println("</newfiles>");
   }



   private static String getDateDescription(long msSinceEpoch,
      Calendar now)
   {
      Date d = new Date(msSinceEpoch);

      Calendar then = Calendar.getInstance();
      then.setTime(d);

      int nowYear = now.get(Calendar.YEAR);
      int thenYear = then.get(Calendar.YEAR);
      if (nowYear > thenYear)
      {
         return plural(nowYear - thenYear, "year") + " ago";
      }
      else if (nowYear == thenYear)
      {
         int nowMonth = now.get(Calendar.MONTH);
         int thenMonth = then.get(Calendar.MONTH);

         if (nowMonth > thenMonth)
         {
            return plural(nowMonth - thenMonth, "month") + " ago";
         }
         else if (nowMonth == thenMonth)
         {
            int nowDate = now.get(Calendar.DATE);
            int thenDate = then.get(Calendar.DATE);

            if (nowDate > thenDate)
            {
               return plural(nowDate - thenDate, "day") + " ago";
            }
            else if (nowDate == thenDate)
            {
               int nowHour = now.get(Calendar.HOUR);
               int thenHour = then.get(Calendar.HOUR);

               if (nowHour > thenHour)
               {
                  return plural(nowHour - thenHour, "hour") + " ago";
               }
               else if (nowHour == thenHour)
               {
                  return "Less than 1 hour ago";
               }
            }
         }
      }

      return "In the future ("+d+"). Spooky.";
   }


   /**
    * hacktastic
    */
   private static String plural(int value, String singular)
   {
      if (value == 1)
      {
         return value + " " + singular;
      }
      return value + " " + singular + "s";
   }

   private static String getRelativeURL(String startPath, File f)
   {

      if (f.isDirectory())
      {
         throw new IllegalArgumentException("Directories not allowed here");
      }
      File startFile = new File(startPath);

      try
      {
         String canonStart = startFile.getCanonicalPath();
         String canonFile = f.getCanonicalPath();

         if (canonFile.startsWith(canonStart))
         {
            String restOfPath = canonFile.substring(canonStart.length()+1);
            if (restOfPath.charAt(0) == File.separatorChar)
            {
               restOfPath = canonFile.substring(1);
            }

            return URLEncoder.encode(
               restOfPath.replace(File.separatorChar, '/')
            );
         }
      }
      catch (Exception e) {}

      return "";
   }

   /**
    * Main Method.
    *
    */
   public static final List findFiles(String startPath, long oldestAge)
   {
      long now = System.currentTimeMillis();
      long since = now - oldestAge;

      ArrayList l = new ArrayList();
      find(new File(startPath), since, l);

      return l;
   }

   /**
    * If the specified file is a file and was modified on or after the
    * since date, add it to the list. If the file is a directory, recursively
    * call find for all the children
    */
   private static void find(File f, long since, List l)
   {
      if (f.isDirectory())
      {
         File[] children = f.listFiles();
         int i=0;
         // BD: Test this to see if it's really all that much faster.
         try
         {
            while (true)
            {
               find(children[i++], since, l);
            }
         }
         catch (ArrayIndexOutOfBoundsException e)
         {}
      }
      else
      {
         if (!f.isHidden())
         {
            if (since <= f.lastModified() && !isIgnored(f))
            {
               l.add(f);
            }
         }
      }
   }


   static class FileSorter implements Comparator
   {
      public int compare(Object o1, Object o2)
      {
         long f1 = ((File)o1).lastModified();
         long f2 = ((File)o2).lastModified();

         return (int) (f2 - f1);
      }

      public boolean equals(Object obj)
      {
         return (obj == FileSorter.this);
      }
   }

   static private class SimpleRecognizer implements FileRecognizer
   {
      /**
       * If this recognizer recognizes the specified file, it should return
       * true.
       *
       * @param f a file
       * @return true if this recognizer recognizes f
       */
      public boolean isRecognizedFile(File f)
      {
         return (f.exists() && f.isFile());
      }

      /**
       * The recognizer should return information on the specified
       * file, which is guaranteed to be a file that the recognizer has
       * previously returned true for from isRecognizedFile(File).
       *
       * @param f a file
       * @return a FileInformation implementation with information about
       *    the specified file.
       */
      public FileInformation getInformation(final File f)
      {
         return new FileInformation() {
            public String getAuthor() { return "Unknown"; }
            public String getDescription() { return "Unknwown"; }
            public String getRevision() { return "Unknown"; }
            public String getTitle() { return f.getName(); } 
         };
      }
   }
}
