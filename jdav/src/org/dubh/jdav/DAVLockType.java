// $Id: DAVLockType.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.NamespaceScoped;

/**
 * Represents the type of a WebDAV lock. RFC2518 defines only one possible
 * value for this: write
 *
 * Because there are a finite number of types of lock, this class is a
 * factory class. You can't construct  an instance of this class directly, but
 * if you call the getInstance() method, you will receive an instance of the
 * class.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVLockType
{
   // This may seem overly cumbersome for a single instance, but we're
   // hedging our bets for new types of lock in the future.

   private DAVLockTypeType m_myType;

   private static DAVLockType s_writeInstance;

   /**
    * If getType() returns this value, the type is write.
    */
   public static final DAVLockTypeType WRITE = new DAVLockTypeType();


   /**
    * This is used to provide a typed enumeration of lock types.
    */
   public static class DAVLockTypeType
   {
      private DAVLockTypeType() {}
   }

   /**
    * Only this class is allowed to instantiate itself.
    */
   private DAVLockType(DAVLockTypeType type)
   {
      m_myType = type;
   }

   /**
    * Get the type of this lock. This will be one of the static
    * DAVLockTypeType enumerators in this class
    * @return WRITE
    */
   public DAVLockTypeType getType()
   {
      return m_myType;
   }

   /**
    * Get an instance of DAVLockType based on the contents of an XML node.
    *
    * @param n a DOM Node object, which must have the "DAV::locktype" type
    * @throws org.dubh.jdav.DAVException if the supplied XML node is invalid
    */
   public static DAVLockType getInstance(Node n)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)n;
      if (!DAVUtils.isDAVNode(ns, "locktype"))
      {
         throw new DAVException(
            "Expecting locktype, got "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      // Should only be one child, but we'll be nice and just ignore any we
      // don't understand.
      NodeList children = n.getChildNodes();
      for (int i=0; i < children.getLength(); i++)
      {
         Node thisNode = children.item(i);
         NamespaceScoped nsKid = (NamespaceScoped)thisNode;

         if (DAVUtils.isDAVNode(nsKid, "write"))
         {
            return getWriteInstance();
         }
      }
      throw new DAVException(
         "Unable to determine the type of a lock"
      );
   }

   /**
    * Get an instance of DAVLockType that represents a write lock
    *
    * @returns an instance of this class for which getType() == WRITE
    */
   public static DAVLockType getWriteInstance()
   {
      if (s_writeInstance == null)
      {
         s_writeInstance = new DAVLockType(WRITE);
      }
      return s_writeInstance;
   }
}


