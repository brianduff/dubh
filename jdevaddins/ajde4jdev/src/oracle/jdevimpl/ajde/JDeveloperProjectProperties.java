package oracle.jdevimpl.ajde;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aspectj.ajde.ProjectProperties;

import oracle.ide.Ide;
import oracle.ide.addin.View;
import oracle.ide.model.Container;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;
import oracle.ide.model.Node;
import oracle.ide.model.NodeFactory;
import oracle.ide.model.Project;
import oracle.ide.net.URLFileSystem;
import oracle.jdeveloper.model.JProject;
import oracle.jdeveloper.model.JavaNode;
import oracle.jdeveloper.model.JavaSourceNode;
import oracle.jdeveloper.runner.RunConfiguration;

/**
 * Project properties for aspectj for JDeveloper
 *
 * @author Brian.Duff@oracle.com
 */
public class JDeveloperProjectProperties extends ProjectProperties
{

  private Project getCurrentProject()
  {
    return Ide.getActiveProject();
  }

  private RunConfiguration getRunConfiguration()
  {
    JProject project = (JProject) getCurrentProject();
    return (RunConfiguration)project.getActiveConfigDataByName( 
        RunConfiguration.DATA_KEY 
      );  
  }

  private void addChildren( final Container parent, final List list )
  {
    if ( parent == null )
    {
      return;
    }
    Iterator i = parent.getChildren();
    while ( i.hasNext() )
    {
      Element e = (Element) i.next();
      if ( e instanceof JavaSourceNode )
      {
        // Urr.. don't know what type of item the caller expects in the list
        list.add(
          URLFileSystem.getPlatformPathName( ((JavaSourceNode)e).getURL() )
        );
      }

      if ( e instanceof Container )
      {
        addChildren( (Container)e, list );
      }
    }
    
  }

// ----------------------------------------------------------------------------
// ProjectProperties overrides
// ----------------------------------------------------------------------------

  public boolean isGlobalMode()
  {
    return false;
  }

  public String getProjectName()
  {
    if ( getCurrentProject() != null )
    {
      return getCurrentProject().getShortName();
    }
    return "";
  }

  public String getClassToRun()
  {
    if ( getCurrentProject() instanceof JProject )
    {
      // Always use the projects "default run target" for now.
      // the logic should probably be similar to that in o.j.runner.JRunProcess
      // (i.e. it should pay attention to the "always run default run target"
      // and context)

      URL runTargetURL = runConfig.getTargetURL();

      if ( runTargetURL != null )
      {
        Node n = NodeFactory.findOrCreate( runTargetURL );
        if ( n instanceof JavaSourceNode )
        {
          // This could be wrong, because:
          //  -  The run target could be a static inner class
          //  -  The run target could be a class that's not in the 
          //     sourcepath, and hence is not a JavaSourceNode
          String pkgName = ((JavaSourceNode)n).getPackage();
          if ( pkgName != null )
          {
            return pkgName + URLFileSystem.getName( n.getURL() );
          }
        }
      }
    }
  }

  public void setClassToRun( final String mainClass )
  {
    // <groan>
  }

  public String getWorkingDir()
  {
    return getOuputPath() + File.separatorChar + "ajworkingdir";
  }

  public String getProjectDir()
  {
    // Umm..
    if ( getCurrentProject() != null )
    {
      return URLFileSystem.getPlatformPathName(
        URLFileSystem.getParent( getCurrentProject().getURL() )
      );
    }
    return "";
  }

  public String getOutputPath()
  {
    if ( getCurrentProject() instanceof JProject )
    {
      URL u = 
        ((JProject)getCurrentProject()).getActiveConfiguration().getOutputDirectory();
      if ( !URLFileSystem.exists( u ) )
      {
        URLFileSystem.mkdirs( u );
      }

      return URLFileSystem.getPlatformPathName( u );
    }
  }

  public String getVMParams()
  {
    // Yeych.
    return "-"+getRunConfiguration().getVMName() + " " +
      getRunConfiguration().getJavaOptions();
  }

  public String getRunParams()
  {
    return getRunConfiguration().getProgramArguments();
  }

  public List getProjectFiles()
  {
    List files = new ArrayList();
    addChildren( getCurrentProject(), files );

    return files;
  }

  public String getProjectSourcePath()
  {
    // OK, jdev often has multiple sourcepath entries because of libraries, but
    // aspectj seems to not support this... fine. we'll just return the first
    // sp entry..
    if ( getCurrentProject() instanceof JProject )
    {
      return URLFileSystem.getPlatformPathName(
        ((JProject)getCurrentProject()).getSourcePath().getEntries()[0]
      );
    }
    return "";
  }

  public String getClasspath()
  {
    if ( getCurrentProject() instanceof JProject )
    {
      String classPath = 
        ((JProject)getCurrentProject()).getRunClassPath().toString();

      try
      {
        classPath += File.pathSeparatorChar + 
          new File( Ide.getLibDirectory() + "/ext", "aspectjrt.jar" ).getCanonicalPath();
      }
      catch ( IOException ioe )
      {
      }
      return classPath;
    }
    return "";
  }

  public String getBootClasspath()
  {
    // Hrrm. 
    return "";
  }

  public String getPropertiesFilePath()
  {
    return getOutputPath() + File.separator + FILE_NAME;
  }
  
}