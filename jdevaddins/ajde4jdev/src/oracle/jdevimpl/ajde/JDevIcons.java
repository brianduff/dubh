package oracle.jdevimpl.ajde;

import oracle.ide.resource.IdeIcons;
import oracle.ide.exception.SingletonClassException;
import oracle.jdeveloper.compiler.CompArb;



// Naughty Imports (for icons)
import oracle.jdevimpl.java.JavaArb;
import oracle.jdevimpl.runner.run.RunArb;
import oracle.jdevimpl.runner.debug.DbgArb;
// End of Naughty imports

import org.aspectj.ajde.Icons;

/**
 * Subclass of Icons for JDeveloper
 *
 * @author Brian.Duff@oracle.com
 */
public class JDevIcons extends Icons
{
  private static final JDevIcons s_instance = new JDevIcons();

  private JDevIcons()
  {
    if ( s_instance != null )
    {
      throw new SingletonClassException();
    }
  }

  public Icon getBuildIcon()
  {
    return CompArb.getIcon( CompArb.BUILD_PROJECT_ICON );
  }

  public Icon getDebugIcon()
  {
    return DbgArb.getIcon( DbgArb.DEBUG_PROJECT_MENUITEM_ICON );
  }

  public Icon getExecuteIcon()
  {
    return RunArb.getIcon( RunArb.RUN_SELECTION_MENUITEM_ICON );
  }

  public Icon getImportsIcon()
  {
    return JavaArb.getIcon( JavaArb.IMPORT_ICON );
  }

  public Icon getFileIcon()
  {
    return IdeIcons.getIcon( IdeIcons.JAVA_ICON );
  }

  public Icon getFileClassIcon()
  {
    return getFileIcon();
  }

  public Icon getFileAspectIcon()
  {
    return getFileIcon();
  }

  public Icon getFileLstIcon()
  {
    return IdeIcons.getIcon( IdeIcons.FILE_NODE_ICON );
  }

  public Icon getPackageIcon()
  {
    return IdeIcons.getIcon( IdeIcons.PACKAGE_ICON );
  }

  public Icon getProjectIcon()
  {
    return IdeIcons.getIcon( IdeIcons.PROJECT_ICON );
  }

  public Icon getClassPrivateIcon()
  {
    return JavaArb.getIcon( JavaArb.CLASS_ICON );
  }

  public Icon getClassPackageIcon()
  {
    return getClassPrivateIcon();
  }

  public Icon getClassProtectedIcon()
  {
    return getClassPrivateIcon();
  }
  public Icon getClassPublicIcon()
  {
    return getClassPrivateIcon();
  }

  public Icon getInterfacePrivateIcon()
  {
    return JavaArb.getIcon( JavaArb.INTERFACE_ICON );
  }

  public Icon getInterfacePackageIcon()
  {
    return getInterfacePrivateIcon();
  }

  public Icon getInterfaceProtectedIcon()
  {
    return getInterfacePrivateIcon();
  }

  public Icon getInterfacePublicIcon()
  {
    return getInterfacePrivateIcon();
  }

  public Icon getFieldPrivateIcon()
  {
    return JavaArb.getIcon( JavaArb.FIELD_ICON  );
  }

  public Icon getFieldPackageIcon()
  {
    return getFieldPrivateIcon();
  }

  public Icon getFieldProtectedIcon()
  {
    return getFieldPrivateIcon();
  }

  public Icon getFieldPublicIcon()
  {
    return getFieldPrivateIcon();
  }

  public Icon getMethodPrivateIcon()
  {
    return JavaArb.getIcon( JavaArb.METHOD_ICON );
  }

  public Icon getMethodPackageIcon()
  {
    return getMethodPrivateIcon();
  }

  public Icon getMethodProtectedIcon()
  {
    return getMethodPrivateIcon();
  }

  public Icon getMethodPublicIcon()
  {
    return getMethodPrivateIcon();
  }
  

  
}