// $Id: DAVDepth.java,v 1.1.1.1 2000-09-17 17:38:12 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.NamespaceScoped;

/**
 * Represents the depth XML element for WebDAV. RFC2518 defines three possible
 * values for the depth element: 0 1 or Infinity.
 *
 * Because there are a finite number of depth values, this class is a
 * factory class. You can't construct  an instance of this class directly, but
 * if you call the getInstance() method, you will receive an instance of the
 * class.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVDepth
{
   // This may seem overly cumbersome for a single instance, but we're
   // hedging our bets for new types of lock in the future.

   private DAVDepthType m_myType;

   private static DAVDepth s_zeroInstance;
   private static DAVDepth s_oneInstance;
   private static DAVDepth s_infinityInstance;

   /**
    * If getType() returns this value, the depth is zero.
    */
   public static final DAVDepthType ZERO = new DAVDepthType();

   /**
    * If getType() returns this value, the depth is one.
    */
   public static final DAVDepthType ONE = new DAVDepthType();

   /**
    * If getType() returns this value, the depth is infinity.
    */
   public static final DAVDepthType INFINITY = new DAVDepthType();

   /**
    * This is used to provide a typed enumeration of depth values
    */
   public static class DAVDepthType
   {
      private DAVDepthType() {}
   }

   /**
    * Only this class is allowed to instantiate itself.
    */
   private DAVDepth(DAVDepthType type)
   {
      m_myType = type;
   }

   /**
    * Get the value of this depth. This will be one of the static
    * DAVDepthType enumerators in this class
    * @return ZERO, ONE or INFINITY
    */
   public DAVDepthType getType()
   {
      return m_myType;
   }

   /**
    * Get an instance of DAVDepth based on the contents of an XML node.
    *
    * @param n a DOM Node object, which must have the "DAV::depth" type
    * @throws org.dubh.jdav.DAVException if the supplied XML node is invalid
    */
   public static DAVDepth getInstance(Node n)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)n;
      if (!DAVUtils.isDAVNode(ns, "depth"))
      {
         throw new DAVException(
            "Expecting depth, got "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      Node nKid = n.getFirstChild();
      String data = ((CharacterData)nKid).getData();

      if ("0".equals(data))
      {
         return getZeroInstance();
      }
      else if ("1".equals(data))
      {
         return getOneInstance();
      }
      else if ("infinity".equals(data))
      {
         return getInfinityInstance();
      }
      else
      {
         throw new DAVException("Unkown depth: "+data);
      }
   }

   /**
    * Get an instance of DAVDepth that represents 0 
    *
    * @returns an instance of this class for which getType() == ZERO
    */
   public static DAVDepth getZeroInstance()
   {
      if (s_zeroInstance == null)
      {
         s_zeroInstance = new DAVDepth(ZERO);
      }
      return s_zeroInstance;
   }

   /**
    * Get an instance of DAVDepth that represents 1
    *
    * @returns an instance of this class for which getType() == ONE
    */
   public static DAVDepth getOneInstance()
   {
      if (s_oneInstance == null)
      {
         s_oneInstance = new DAVDepth(ONE);
      }
      return s_oneInstance;
   }

   /**
    * Get an instance of DAVDepth that represents infinity
    *
    * @returns an instance of this class for which getType() == INFINITY
    */
   public static DAVDepth getInfinityInstance()
   {
      if (s_infinityInstance == null)
      {
         s_infinityInstance = new DAVDepth(INFINITY);
      }
      return s_infinityInstance;
   }      
}

 
