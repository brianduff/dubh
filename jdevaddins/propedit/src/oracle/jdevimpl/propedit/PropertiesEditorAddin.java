package oracle.jdevimpl.propedit;

import oracle.ide.Ide;
import oracle.ide.addin.Addin;

/**
 * The properties editor addin provides a GUI for editing properties files
 * that possibly span several locales.
 *
 * @author Brian.Duff@oracle.com
 */
public class PropertiesEditorAddin implements Addin
{


// Addin interface

  public void initialize()
  {

  }

  public boolean canShutdown()
  {
    return true;
  }

  public void shutdown()
  {
    
  }

  public float version()
  {
    return 0.1f;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }
  

}