// $Id: DAVLockScope.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.NamespaceScoped;

/**
 * Represents the scope of a WebDAV lock. RFC2518 defines two possible values
 * for a lock scope: exclusive or shared.
 *
 * Because there are a finite number of types of lock scope, this class is a
 * factory class. You can't construct  an instance of this class directly, but
 * if you call the getInstance() method, you will receive an instance of the
 * class.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVLockScope
{
   private DAVLockScopeType m_myType;

   private static DAVLockScope s_exclusiveInstance;
   private static DAVLockScope s_sharedInstance;

   /**
    * If getType() returns this value, the scope is exclusive.
    */
   public static final DAVLockScopeType EXCLUSIVE = new DAVLockScopeType();

   /**
    * If getType() returns this value, the scope is shared
    */
   public static final DAVLockScopeType SHARED = new DAVLockScopeType();

   /**
    * This is used to provide a typed enumeration of lock scope types.
    */
   public static class DAVLockScopeType
   {
      private DAVLockScopeType() {}
   }

   /**
    * Only this class is allowed to instantiate itself.
    */
   private DAVLockScope(DAVLockScopeType type)
   {
      m_myType = type;
   }

   /**
    * Get the type of this lock scope. This will be one of the static
    * DAVLockScopeType enumerators in this class
    * @return SHARED or EXCLUSIVE
    */
   public DAVLockScopeType getType()
   {
      return m_myType;
   }

   /**
    * Get an instance of DAVLockScope based on the contents of an XML node.
    *
    * @param n a DOM Node object, which must have the "DAV::lockscope" type
    * @throws org.dubh.jdav.DAVException if the supplied XML node is invalid
    */
   public static DAVLockScope getInstance(Node n)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)n;
      if (!DAVUtils.isDAVNode(ns, "lockscope"))
      {
         throw new DAVException(
            "Expecting lockscope, got "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      // Should only be one child, but we'll be nice and just ignore any we
      // don't understand.
      NodeList children = n.getChildNodes();
      for (int i=0; i < children.getLength(); i++)
      {
         Node thisNode = children.item(i);
         NamespaceScoped nsKid = (NamespaceScoped)thisNode;

         if (DAVUtils.isDAVNode(nsKid, "exclusive"))
         {
            return getExclusiveInstance();
         }
         else if (DAVUtils.isDAVNode(nsKid, "shared"))
         {
            return getSharedInstance();
         }
      }
      throw new DAVException(
         "Unable to determine the scope of a lock"
      );
   }

   /**
    * Get an instance of DAVLockScope that represents an exclusive lock
    *
    * @returns an instance of this class for which getType() == EXCLUSIVE
    */
   public static DAVLockScope getExclusiveInstance()
   {
      if (s_exclusiveInstance == null)
      {
         s_exclusiveInstance = new DAVLockScope(EXCLUSIVE);
      }
      return s_exclusiveInstance;
   }

   /**
    * Get an instance of DAVLockScope that represents a shared lock
    *
    * @returns an instance of this class for which getType() == SHARED
    */
   public static DAVLockScope getSharedInstance()
   {
      if (s_sharedInstance == null)
      {
         s_sharedInstance = new DAVLockScope(SHARED);
      }
      return s_sharedInstance;
   }
}

 