#*
(This is a Velocity comment, and will not appear in the output file)

A sample Velocity template that is used to create an implementation
of the JDeveloper Addin interface.

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
import oracle.ide.addin.Addin;

/**
 * ${oracleide.destination.nameWithoutExtension}
 */
public class ${oracleide.destination.nameWithoutExtension} implements Addin
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

}