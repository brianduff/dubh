package org.freeciv.common;

import org.freeciv.net.Packet;

/**
 * There are a number of factories which create objects and can retrieve
 * objects by id. These factories implement the GameObjectFactory interface
 *
 * @author Brian.Duff@dubh.org
 */
public interface GameObjectFactory
{
  /**
   * Find the object with the specified id
   *
   * @param id the id of the object to find
   * @return a GameObject with the specified id
   * @throws java.lang.IllegalArgumentException if no gameobject exists with
   *    the specified id. Unlike c, you should not call this method to determine
   *    if an object already exists and rely on the return value being null.
   *    Instead use the doesExist() method.
   */
  public GameObject findById( int id );

  /**
   * Does the object with the specified id exist?
   *
   * @param id the id to look up
   * @return true if the specified item exists
   */
  public boolean doesExist( int id );

  /**
   * Create a new, uninitialized object
   *
   * @param id the id of the new object
   * @return a new GameObject
   */
  public GameObject create(int id);

  /**
   * Create a new object based on the contents of a packet.
   */
  public GameObject create(Packet p);
  
  /**
   * Destroy an object.
   *
   * @param o an object. All references to this object will be removed
   *    from the factory. The caller should release any other references to
   *    the object to allow the garbage collector to dispose of it.
   */
  public void destroy( GameObject o );

  /**
   * Get the Factories object this factory belongs to
   */
  public Factories getParent();
}
