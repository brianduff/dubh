// $Id: DAVActiveLock.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.CharacterData;

import com.sun.xml.tree.NamespaceScoped;

/**
 * This class represents an active WebDAV lock
 * <P>
 * @author Brian Duff
 */
public class DAVActiveLock
{
   private DAVLockScope m_lockScope;
   private DAVLockType m_lockType;
   private DAVDepth m_depth;
   private DAVOwner m_owner;
   private DAVTimeout m_timeout;
   private DAVLockToken m_lockToken;
   

   /**
    * Construct an active WebDAV lock using the specified XML Node as the
    * active lock base node. All properties will be retireved from the children
    * of this node
    */
   public DAVActiveLock(Node activeLockNode)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)activeLockNode;
      if (!DAVUtils.isDAVNode(ns, "activelock"))
      {
         throw new DAVException(
            "Expecting activelock, got "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      NodeList children = activeLockNode.getChildNodes();
      for (int i=0; i < children.getLength(); i++)
      {
         Node n = children.item(i);

         if (n instanceof CharacterData)
         {
            continue;
         }

         NamespaceScoped nsKid = (NamespaceScoped)n;

         if (DAVUtils.isDAVNode(nsKid, "lockscope"))
         {
            setLockScope(DAVLockScope.getInstance(n));
         }
         else if (DAVUtils.isDAVNode(nsKid, "locktype"))
         {
            setLockType(DAVLockType.getInstance(n));
         }
         else if (DAVUtils.isDAVNode(nsKid, "depth"))
         {
            setDepth(DAVDepth.getInstance(n));
         }
         else if (DAVUtils.isDAVNode(nsKid, "owner"))
         {
            setOwner(new DAVOwner(n));
         }
         else if (DAVUtils.isDAVNode(nsKid, "timeout"))
         {
            setTimeout(new DAVTimeout(n));
         }
         else if (DAVUtils.isDAVNode(nsKid, "locktoken"))
         {
            setLockToken(new DAVLockToken(n));
         }
         else
         {
            // Need to complain here?
         }
      }
      // Should we check that the mandatories are present?/
   }

   public DAVActiveLock(DAVLockScope lockScope, DAVLockType lockType,
      DAVDepth depth)
   {
      setLockScope(lockScope);
      setLockType(lockType);
      setDepth(depth);
   }

   public void setLockScope(DAVLockScope scope)
   {
      m_lockScope = scope;
   }

   public DAVLockScope getLockScope()
   {
      return m_lockScope;
   }

   public void setLockType(DAVLockType type)
   {
      m_lockType = type;
   }

   public DAVLockType getLockType()
   {
      return m_lockType;
   }

   public DAVDepth getDepth()
   {
      return m_depth;
   }

   public void setDepth(DAVDepth depth)
   {
      m_depth = depth;
   }

   public DAVOwner getOwner()
   {
      return m_owner;
   }

   public void setOwner(DAVOwner owner)
   {
      m_owner = owner;
   }

   public DAVTimeout getTimeout()
   {
      return m_timeout;
   }

   public void setTimeout(DAVTimeout timeout)
   {
      m_timeout = timeout;
   }

   public DAVLockToken getLockToken()
   {
      return m_lockToken;
   }

   public void setLockToken(DAVLockToken lockToken)
   {
      m_lockToken = lockToken;
   }

}

 