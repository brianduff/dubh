// $Id: DAVOwner.java,v 1.1.1.1 2000-09-17 17:38:12 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.Node;

/**
 * Represents the owner of a lock. RFC2518 allows any XML elements to be
 * included as children of the owner element, so we can't place any
 * restrictions on the type of data contained in the owner. For this reason,
 * this class is just a wrapper round a W3C DOM Node object.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVOwner
{
   private Node m_node;
   
   /**
    * Construct a DAVOwner object for the specified DOM Node
    */
   public DAVOwner(Node ownerNode)
   {
      // Should validate here?
      m_node = ownerNode;
   }

   /**
    * Get the DOM Node for this owner
    */
   public Node getNode()
   {
      return m_node;
   }
}

 