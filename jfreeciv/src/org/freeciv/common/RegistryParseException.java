package org.freeciv.common;

/**
 * Exception thrown when Registry is unable to parse a registry format file
 *
 * @see org.freeciv.common.Registry
 * @author Brian Duff
 */
public final class RegistryParseException extends Exception
{
  public RegistryParseException( String message )
  {
    super( message );
  }
}