package org.freeciv.common;

// bad import
import org.freeciv.client.Constants;

import org.freeciv.net.Packet;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The superclass for all the game object factories in freeciv. This
 * provides some useful utilities for retrieving objects. Subclasses should
 * implement the doCreate() method.
 *
 * @author Brian.Duff@dubh.org
 */
abstract class AbstractGameObjectFactory implements GameObjectFactory
{
  // Should try to enhance this by not fixing the array size.
  private GameObject[] m_objects;
  private Factories m_factories;

  AbstractGameObjectFactory(Factories parent)
  {
    m_factories = parent;
    m_objects = new GameObject[getMaximumNumberOfObjects()];
  }

  public final Factories getParent()
  {
    return m_factories;
  }

  public final GameObject findById( int id )
  {
    GameObject go = null;
    try
    {
      go = m_objects[id];
    }
    catch (ArrayIndexOutOfBoundsException aiobe)
    {
    }

    if (go == null)
    {
      throw new IllegalArgumentException("Unrecognized gameobject id: "+id);
    }
    return go;
  }

  /**
   * Does the object with the specified id exist?
   *
   * @param id the object to look up
   * @return true if it exists, false otherwise
   */
  public final boolean doesExist(int id)
  {
    try
    {
      findById(id);
      return true;
    }
    catch (IllegalArgumentException iae)
    {
    }
    return false;
  }
  
  public final GameObject create(int id)
  {
    GameObject o = doCreate();
    m_objects[id] = o;
    return o;
  }

  public final GameObject create(Packet p)
  {
    GameObject o = doCreate();
    o.initFromPacket( p );
    m_objects[o.getId()] = o;
    return o;
  }
  
  public final void destroy( GameObject o )
  {
    m_objects[o.getId()] = null;
  }

  /**
   * Get the maximum number of objects supported by this factory. The
   * default returns MAX_NUM_ITEMS. You should override this and return a
   * smaller number to be efficient.
   */
  protected int getMaximumNumberOfObjects()
  {
    return Constants.MAX_NUM_ITEMS;
  }
  
  protected abstract GameObject doCreate();
}