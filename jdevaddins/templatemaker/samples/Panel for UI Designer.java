#*
(This is a Velocity comment, and will not appear in the output file)

A sample Velocity template that is used to create a JPanel subclass in
a format that the JDeveloper UI designer can grok.

This is pretty much the same as choosing panel from File->New..., but
the flexibility of the template engine means you can customize this
template to include copyright information or other standard features
etc.

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

import javax.swing.JPanel;

/**
 * ${oracleide.destination.nameWithoutExtension} panel
 */
public class ${oracleide.destination.nameWithoutExtension} extends JPanel
{
  public ${oracleide.destination.nameWithoutExtension}()
  {
    try
    {
      jbInit();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception
  {
    this.setLayout( null );
  }
}
