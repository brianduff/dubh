// $Id: GrabDependencies.java,v 1.3 2000-08-23 23:55:58 briand Exp $
package org.dubh.tool.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copydir;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

/**
 * This class parses an XML file in the module's root directory which
 * contains details of the dependencies of the module. If those dependencies
 * are present, this task does nothing. Otherwise, it figures out what
 * needs to be pulled across into the lib directory from other built modules.
 *
 * This task also sets the classpath property, which will include all JAR
 * files in the project's lib.
 *
 * N.b. This doesn't help with the build order: the automated build must know
 * itself what order to build things in (perhaps this could be determined by
 * a separate tool)
 * <P>
 * @author Brian Duff
 */
public class GrabDependencies extends Task
{
   private static final String DEPENDENCIES_FILE = "dependencies.xml";

   /**
    * Run the task.
    */
   public void execute()
      throws BuildException
   {
      Project p = getProject();
      String moduleName = p.getProperty("cvsmodule");

      if (moduleName == null || moduleName.trim().length() == 0)
      {
         throw new BuildException(
            "Unable to determine module name. Please set cvsmodule in build.xml"
         );
      }

      File f = getDependencyFile(moduleName);

      if (f == null)
      {
         log(moduleName+" has no dependencies.");
         getProject().setProperty("classpath", "");
         return;
      }

      DependencyInfo[] deps = readDependencies(f);

      for (int i=0; i < deps.length; i++)
      {
         // The dependency might already exist.
         File depJar = new File(moduleName+"lib"+File.separator+deps[i].module+"-"+deps[i].version+".jar");

         if (depJar.exists())
         {
            log("Dependency "+deps[i].module+" "+deps[i].version+" already available in lib.");
         }
         else
         {
            log("Copying dependency "+deps[i].module+" "+deps[i].version+"...");
            Copydir cd = (Copydir) getProject().createTask("copydir");
            cd.setSrc(deps[i].module+File.separator+"lib"); // Bad, lib might not be right for that project.
            cd.setDest(getProject().getProperty("jardir"));
            cd.setIncludes("**/*.jar,**/*.zip");
            cd.execute();
         }
         
      }

      createClasspath(moduleName);
   }

   private void createClasspath(String modName)
   {
      File f = new File(modName+File.separator+"lib");
      File[] allLib = f.listFiles();
      StringBuffer cp = new StringBuffer();
      for (int i=0; i < allLib.length; i++)
      {
         cp.append(f.getAbsolutePath());
         if (i < allLib.length-1)
         {
            cp.append(":");
         }
      }
      getProject().setProperty("classpath", cp.toString());
   }

   /**
    * Get the dependency file for the module. Returns null if there is no
    * file (i.e. there are no dependencies).
    */
   private File getDependencyFile(String module)
      throws BuildException
   {
      String name = module+File.separator+DEPENDENCIES_FILE;

      File f = new File(name);

      if (!f.exists())
      {
         return null;
      }

      if (!f.canRead())
      {
         throw new BuildException("Dependency file for "+module+
            " exists, but cannot be read (file is "+name+")"
         );
      }

      return f;
   }

   /**
    * Parse a dependencies file and return an array of DependencyInfo
    * objects
    */
   private DependencyInfo[] readDependencies(File file)
      throws BuildException
   {
      try
      {
         DocumentBuilderFactory dbfFactory =
            DocumentBuilderFactory.newInstance();

         DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();

         // Lets parse the choices file
         Document doc = dbBuilder.parse(file);

         doc.getDocumentElement().normalize();

         NodeList dependencies = doc.getDocumentElement().
            getElementsByTagName("dependency");
         ArrayList depList = new ArrayList();
         for (int i=0; i < dependencies.getLength(); i++)
         {
            Node n = dependencies.item(i);
            NamedNodeMap attribs = n.getAttributes();
            String module = attribs.getNamedItem("module").getNodeValue();
            String version = attribs.getNamedItem("version").getNodeValue();

            depList.add(new DependencyInfo(module, version));
         }

         DependencyInfo[] array = new DependencyInfo[depList.size()];
         depList.toArray(array);
         return array;

      }
      catch (SAXParseException spe)
      {
         throw new BuildException("Parse Error in "+file.getName(), spe);
      }
      catch (SAXException se)
      {
         Exception x = se.getException();
         throw new BuildException("XML Parser failed in "+file.getName(),
            ((x == null) ? se : x)
         );
      }
      catch (ParserConfigurationException pce)
      {
         throw new BuildException("Parser configuration error", pce);
      }
      catch (IOException ioe)
      {
         throw new BuildException(
            "Failed to read from dependencies file "+file.getName(), ioe);
      }
  }


   class DependencyInfo
   {
      public String module;
      public String version;
      public DependencyInfo(String module, String version)
      {
         this.module = module;
         this.version = version;
      }
   }
}

 