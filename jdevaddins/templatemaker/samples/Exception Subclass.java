#*
(This is a Velocity comment, and will not appear in the output file)

A sample Velocity template that is used to create a subclass of 
Exception. 

Author: Brian.Duff@oracle.com
*#
/*
 * ${oracleide.destination.name}
 *
 * (C) Acme, Inc
 */
#if( ${oracleide.destination.packageName} )
package ${oracleide.destination.packageName};
#end

/**
 * ${oracleide.destination.nameWithoutExtension}
 */
public class ${oracleide.destination.nameWithoutExtension} extends Exception
{

  /**
   * Construct an instance of ${oracleide.destination.nameWithoutExtension}
   */
  public ${oracleide.destination.nameWithoutExtension}()
  {
    super();
  }
  
  /**
   * Construct an instance of ${oracleide.destination.nameWithoutExtension},
   * specifying a detail message.
   *
   * @param message the message
   */
  public ${oracleide.destination.nameWithoutExtension}( final String message )
  {
    super( message );
  }

}