package oracle.jdevimpl.ajde;

import org.aspectj.ajde.IdeManager;

import oracle.ide.Ide;


/**
 * Implementation of the AspectJ IdeManager interface for Oracle9i JDeveloper.
 *
 * @author Brian.Duff@oracle.com
 */
public class JDeveloperManager implements IdeManager
{

  private final ProjectProperties m_projectProperties = 
    new JDeveloperProjectProperties();

  public JDeveloperManager()
  {
    TopManager.initAjde( this, 
      new EditorAdapter(), new JDeveloperCompilerMessages(),
      m_projectProperties, false
    );
  }

// ----------------------------------------------------------------------------
// IdeManager implementation
// ----------------------------------------------------------------------------

  public void setStatusText(String text)
  {
    Assert.println( "setStatusText("+text+") not yet implemented" );
  }

  public void setEditorStatusText(String text)
  {
    // NOOP
  }

  public void build()
  {

  }

  public String getDefaultConfigFile()
  {

  }

  public void editConfigFile(String configFile)
  {

  }

  public void run()
  {

  }

  public void saveAll()
  {

  }

  public Icons getIcons()
  {

  }

  public List getConfigFiles()
  {

  }

  public void mountConfigFile(String configFile)
  {

  }

  public void removeConfigFile(String configFile)
  {

  }

  public void showMessages()
  {

  }

  public void hideMessages()
  {

  }

  public String getDumpFilePath()
  {

  }

  public JFrame getRootFrame()
  {
    return Ide.getMainWindow();
  }
}