#*
(This is a Velocity comment, and will not appear in the output file)

A sample Velocity template that is used to create an implementation
of the JDeveloper Addin interface and subclass the BaseController.
This is useful as the basis of addins which add menu items.

Author: Brian.Duff@oracle.com
*#
/*
 * ${oracleide.destination.name}
 *
 * (C) Acme, Inc
 */
#if ( ${oracleide.destination.packageName} )
package ${oracleide.destination.packageName};
#end

import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Context;

/**
 * ${oracleide.destination.nameWithoutExtension}
 */
public class ${oracleide.destination.nameWithoutExtension} extends BaseController
  implements Addin
{

// ---------------------------------------------------------------------------
// Addin implementation
// ---------------------------------------------------------------------------

  public void initialize()
  {
    // TODO: Initialize your addin
  }
  
  public void shutdown()
  {
    // TODO: Shutdown your addin
  }
  
  public boolean canShutdown()
  {
    return true;
  }
  
  public float version()
  {
    return 0.1f;
  
  }
  
  public float ideVersion()
  {
    return Ide.getVersion();
  }

// ---------------------------------------------------------------------------
// BaseController overrides
// ---------------------------------------------------------------------------

  public boolean handleEvent( IdeAction action, Context ctx )
  {
    // TODO: check the context and do menu item actions
    return super.handleEvent( action, ctx );
  }
  
  public boolean update( IdeAction action, Context ctx )
  {
    // TODO: Check action enablement
    return super.update( action, ctx );
  }

}