package org.freeciv.client;
/**
 * The client has a number of factories which create objects and can retrieve
 * objects by id. These factories implement the ClientObjectFactory interface
 *
 * @author Brian.Duff@dubh.org
 */
public interface ClientObjectFactory
{
  /**
   * Find the client object with the specified id
   *
   * @param id the id of the object to find
   * @return a ClientObject with the specified id, or null if no such
   *    object exists
   */
  public ClientObject findById( int id );
  /**
   * Create a new, uninitialized client object
   *
   * @return a new ClientObject
   */
  public ClientObject create();
  /**
   * Destroy a client object.
   *
   * @param o a client object. All references to this object will be removed
   *    from the factory. The caller should release any other references to
   *    the object to allow the garbage collector to dispose of it.
   */
  public void destroy( ClientObject o );
}
