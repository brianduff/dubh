// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ResourceMaker.java,v 1.3 1999-03-22 23:37:18 briand Exp $
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
package dubh.utils.nls;

// Core Java Imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ListResourceBundle;
import java.util.Enumeration;
import java.util.Date;

// Swing Imports

// Dubh Utils Imports

// FESI Imports

/**
 * <p>
 * Program that will convert .properties files into java source files that
 * contain an instance of ListResourceBundle.
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 1.0.01) [27/Oct/1998]
 */
public class ResourceMaker 
{

   /************************************************************************
   *** PUBLIC STATIC CONSTANTS
   *************************************************************************/
  
   
   /************************************************************************
   *** PRIVATE / PROTECTED VARIABLES
   *************************************************************************/
   private static final String VALID_EXTENSION = "properties";
   private static final String ERR_BADEXTENSION = 
      "The file extension is not "+VALID_EXTENSION;
   private static final String ERR_FILENOTFOUND = 
      "The input file couldn't be found: ";
   private static final String ERR_CANTREAD = 
      "I don't have permission to read from the input file: ";
   private static final String ERR_IO = 
      "I can't read the input properties file.";
      

   /************************************************************************
   *** CONSTRUCTORS
   *************************************************************************/

   /**
    * Constructor comments.
    */
   public ResourceMaker()
   {
   }



   /************************************************************************
   *** PUBLIC INTERFACE
   *************************************************************************/

   /**
    * Main program. You should pass in the filename of the .properties
    * file you want to convert as the first parameter.
    */
   public static void main(String[] args)
   {
      if (args.length < 1) showUsage();      
      
      if (!args[0].endsWith(VALID_EXTENSION))
         terminalError(ERR_BADEXTENSION);
      
      ResourceMaker maker = new ResourceMaker();
      String pkg = (args.length == 2 ? args[1] : null);
      try
      {
         File inFile = new File(args[0]);
         File outFile = new File(
            maker.stripFileExtension(inFile.getAbsolutePath()) + ".java"
         );
         maker.writeSource(inFile, pkg, outFile);
      }
      catch (FileNotFoundException fnf)
      {
         terminalError(ERR_FILENOTFOUND + args[0]);   
      }
      catch (SecurityException secex)
      {
         terminalError(ERR_CANTREAD + args[0]);
      }
      catch (IOException iox)
      {
         terminalError(ERR_IO);
      }
      
   }

   /**
    * Generate the ListResourceBundle source file for the given file. The
    * result is returned in a string.
    */
   public String getSource(File f, String packageName)
      throws FileNotFoundException,
             SecurityException,
             IOException
   {
      StringWriter sOut = new StringWriter();
      PrintWriter output = new PrintWriter(sOut);
      String name = stripFileExtension(f.getName());
      
      doBundle(output, name, packageName, f);
      
      return sOut.getBuffer().toString();
   }
   
   public void writeSource(File f, String packageName, File destFile)
      throws IOException
   {
      PrintWriter out = new PrintWriter(new FileWriter(destFile));
      
      doBundle(out, stripFileExtension(f.getName()), packageName, f);   
   }
   
   

   /************************************************************************
   *** PRIVATE / PROTECTED INTERFACE
   *************************************************************************/
   
   protected void doBundle(PrintWriter output, String name, String pkg, File in)
      throws IOException
   {
      PropertyResourceBundle bundle;
      bundle = new PropertyResourceBundle(new FileInputStream(in));
   
      writeHeader(output, name, pkg);
      
      Enumeration keys = bundle.getKeys();
      
      while (keys.hasMoreElements())
      {
         String thisKey = (String) keys.nextElement();
         writeItem(output, thisKey, bundle.getString(thisKey));
      }
      writeFooter(output);     
      output.flush();
      output.close();
   }
   
   /**
    * Strip the extension from a filename and return its name
    */
   protected String stripFileExtension(String name)
   {
      return(name.substring(0, name.lastIndexOf('.')));
   }
   
   /**
    * Write an item
    */
   protected void writeItem(PrintWriter p, String key, String value)
   {
      p.println("      {\""+key+"\", \""+value+"\"},");
   }
   
   protected void writeFooter(PrintWriter p)
   {
      p.println("   };");
      p.println("}");
   }
   
   /**
    * Write the header
    */
   protected void writeHeader(PrintWriter p, String fileName, String packageName)
   {
      p.println("/*");
      p.println(" * NLS ListBundle for "+fileName);
      p.println(" *");
      p.println(" * DO NOT EDIT THIS FILE");
      p.println(" * It was automatically generated from "+fileName+
                     "."+VALID_EXTENSION+" on "+(new Date()).toString());
      p.println(" */");
      p.println();
      if (packageName != null)
      {
         p.println("package "+packageName+";");
      }
      //
      // Imports
      //
      p.println();
      p.println("import java.util.ListResourceBundle;");
      p.println();
      p.println("/**");
      p.println(" * Automatically generated NLS resources.");
      p.println(" */");
      p.println("public class "+fileName+" extends ListResourceBundle");
      p.println("{");
      p.println("   /**");
      p.println("    * Get bundle contents");
      p.println("    */");
      p.println("   public Object[][] getContents()");
      p.println("   {");
      p.println("      return m_contents;");
      p.println("   }");
      p.println();
      p.println("   /**");
      p.println("    * NLS Properties start here. Localise these.");
      p.println("    */");
      p.println("    static final Object[][] m_contents = {");
   }
   
   /**
    * Dump a friendly "That's not how you use this class" message.
    */
   protected static void showUsage()
   {
      System.err.println("Usage:");
      System.err.println("ResourceMaker <filename>.properties");
      System.err.print("\n\rConverts the file into a <filename>.java file ");
      System.err.print("in the same directory\n\r");
      System.exit(1);
   }
   
   /**
    * Dump an error and say bye
    */
   protected static void terminalError(String message)
   {
      System.err.println("ResourceMaker error: ");
      System.err.println(message);
      System.exit(1);
   }


   /************************************************************************
   *** INNER CLASSES
   *************************************************************************/

}