#*
(This is a Velocity comment, and will not appear in the output file)

A sample Velocity template that is used to create a Java class. This template
demonstrates the use of properties provided by the IDE at the time the
template is used to generate a new file.

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

/**
 * ${oracleide.destination.nameWithoutExtension}
 */
public class ${oracleide.destination.nameWithoutExtension}
{

  /**
   * Construct an instance of ${oracleide.destination.nameWithoutExtension}
   */
  public ${oracleide.destination.nameWithoutExtension}()
  {
  
  }

}