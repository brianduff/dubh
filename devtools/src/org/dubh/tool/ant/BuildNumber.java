// $Id: BuildNumber.java,v 1.4 2000-08-20 20:43:28 briand Exp $
package org.dubh.tool.ant;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

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

   private static final String BUILD_NUMBER = "BUILDNUMBER";

   /**
    * The count file is used to determine the current build number and is
    * updated to reflect the new build number.
    */
   public void setCountfile(String bnoFile)
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
         BufferedReader br = new BufferedReader(new FileReader(m_bnoFile));
         String line = br.readLine();
         if (line == null)
         {
            line = "0";
         }

         line = line.trim();
         // Try parsing the line into an integer.
         try
         {
            int buildNumber = Integer.parseInt(line);
            buildNumber++;
            getProject().setProperty(BUILD_NUMBER, ""+buildNumber);

            // Close the input file and write it back out.
            br.close();

            try
            {
               PrintWriter pw = new PrintWriter(new FileWriter(m_bnoFile));
               pw.println(""+buildNumber);
               pw.close();
            }
            catch (IOException ioe)
            {
               throw new BuildException("Error while writing "+m_bnoFile, ioe);
            }
         }
         catch (NumberFormatException nfe)
         {
            br.close();
            throw new BuildException(
               m_bnoFile+" contains a non integer build number: "+line,
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
