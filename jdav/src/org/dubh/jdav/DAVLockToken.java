// $Id: DAVLockToken.java,v 1.1.1.1 2000-09-17 17:38:14 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.tree.NamespaceScoped;

/**
 * This represents the lock token associated with a lock. It contains one
 * or more opaque lock token URIs which all refer to the same lock.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org</a>)
 */
public class DAVLockToken
{
   private DAVHref[] m_tokenURIs;

   /**
    * Creates a lock token object based on a W3C DOM node.
    */
   public DAVLockToken(Node lockTokenNode)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)lockTokenNode;
      if (!DAVUtils.isDAVNode(ns, "locktoken"))
      {
         throw new DAVException(
            "Expected locktoken, got: "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      NodeList children = lockTokenNode.getChildNodes();

      List l = new ArrayList();

      for (int i=0; i < children.getLength(); i++)
      {
         Node thisChild = children.item(i);
         if (thisChild instanceof CharacterData)
         {
            continue;
         }
         
         NamespaceScoped nsKid = (NamespaceScoped)thisChild;
         if (!DAVUtils.isDAVNode(nsKid, "href"))
         {
            throw new DAVException(
               "Expected href, got: "+nsKid.getNamespace()+":"+nsKid.getLocalName()
            );
         }
         l.add(new DAVHref(thisChild));
      }

      m_tokenURIs = new DAVHref[l.size()];
      l.toArray(m_tokenURIs);
   }


   /**
    * Get all URI lock tokens associated with this resource
    */
   public DAVHref[] getTokenURIs()
   {
      return m_tokenURIs;
   }

}

 