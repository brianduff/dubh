// $Id: BuildNumber.java,v 1.5 2000-08-20 21:03:38 briand Exp $
package org.dubh.tool.ant;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * This class extends the Ant Task class and is capable of generating a build
 * number which increments with each build on the build machine.
 *
 * It sets a property called buildnumber.
 *
 * @author Brian Duff
 */
public class BuildNumber extends Task
{
   private String m_bnoFile;

   private static final String BUILD_NUMBER_PROPERTY = "dubh.buildnumber";

   /**
    * The count file is used to determine the current build number and is
    * updated to reflect the new build number.
    */
   public void setPropertyfile(String bnoFile)
   {
      m_bnoFile = bnoFile;
   }

   /**
    * Reads the count file, increments its value by 1 and sets the buildnumber
    * property.
    */
   public void execute()
      throws BuildException
   {
      File f = new File(m_bnoFile);

      // Create the counter file if it doesn't exist.
      try
      {
         f.createNewFile();
      }
      catch (IOException ioe)
      {
         throw new BuildException(
            m_bnoFile+" doesn't exist and new file can't be created",
            ioe
         );
      }

      // Can we actually read and write from the file?
      if (!f.canRead())
      {
         throw new BuildException("Unable to read from "+m_bnoFile);
      }

      if (!f.canWrite())
      {
         throw new BuildException("Unable to write to "+m_bnoFile);
      }

      try
      {
         Properties p = new Properties();
         FileInputStream fis = new FileInputStream(m_bnoFile);
         p.load(fis);
         fis.close();

         String buildNumberString = (String) p.get(BUILD_NUMBER_PROPERTY);
         if (buildNumberString == null)
         {
            buildNumberString = "0";
         }

         buildNumberString = buildNumberString.trim();
         // Try parsing the line into an integer.
         try
         {
            int buildNumber = Integer.parseInt(buildNumberString);
            buildNumber++;
            p.put(BUILD_NUMBER_PROPERTY, ""+buildNumber);

            // Write the properties file back out
            try
            {
               FileOutputStream fos = new FileOutputStream(m_bnoFile);
               p.store(fos, "Whatever dude");
               fos.close();
            }
            catch (IOException ioe)
            {
               throw new BuildException("Error while writing "+m_bnoFile, ioe);
            }
         }
         catch (NumberFormatException nfe)
         {
            throw new BuildException(
               m_bnoFile+" contains a non integer build number: "+buildNumberString,
               nfe
            );
         }

      }
      catch (IOException ioe)
      {
         throw new BuildException("Error while reading "+m_bnoFile, ioe);
      }
      
   }
}
