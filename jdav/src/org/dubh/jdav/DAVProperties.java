
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import com.sun.xml.tree.NamespaceScoped;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.CharacterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of WebDAV properties.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVProperties
{
   private DAVProperty[] m_allProperties;

   /**
    * Construct a set of DAVProperties from a DAV prop XML element
    */
   public DAVProperties(Node propNode)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)propNode;
      if (!DAVUtils.isDAVNode(ns, "prop"))
      {
         throw new DAVException(
            "Expecting prop, got: "+ns.getNamespace()+":"+ns.getLocalName()
         );
      }

      NodeList l = propNode.getChildNodes();

      List propList = new ArrayList();
      
      for (int i=0; i < l.getLength(); i++)
      {
         Node n = l.item(i);

         if (n instanceof CharacterData)
         {
            // Skip character data... (is this the right thing to do?)
            continue;
         }
         NamespaceScoped nsnode = (NamespaceScoped)n;
         DAVProperty theProperty = null;
         
         // Deal with all known WebDAV properties defined in RFC 2518
         if (DAVUtils.DAV_NAMESPACE.equals(nsnode.getNamespace()))
         {
            String tagName = nsnode.getLocalName();

            if ("creationdate".equals(tagName))
            {
               theProperty = new DAVTextProperty("creationdate", n);
            }
            else if ("displayname".equals(tagName))
            {
               theProperty = new DAVTextProperty("displayname", n);
            }
            else if ("getcontentlanguage".equals(tagName))
            {
               theProperty = new DAVTextProperty("getcontentlanguage", n);
            }
            else if ("getcontenttype".equals(tagName))
            {
               theProperty = new DAVTextProperty("getcontenttype", n);
            }
            else if ("getcontentlength".equals(tagName))
            {
               theProperty = new DAVTextProperty("getcontentlength", n);
            }
            else if ("getetag".equals(tagName))
            {
               theProperty = new DAVTextProperty("getetag", n);
            }
            else if ("getlastmodified".equals(tagName))
            {
               theProperty = new DAVTextProperty("getlastmodified", n);
            }
            else if ("lockdiscovery".equals(tagName))
            {
               theProperty = new DAVLockDiscovery(n);
            }
         }
         if (theProperty == null)
         {
            theProperty = new DAVNodeProperty(n);
         }

         propList.add(theProperty);
         
      }

      m_allProperties = new DAVProperty[propList.size()];
      propList.toArray(m_allProperties);
      
   }

   /**
    * Get the number of properties contained in this properties object.
    */
   public int getPropertyCount()
   {
      return m_allProperties.length;
   }

   /**
    * Get the property at the specified position in the list of properties
    */
   public DAVProperty getProperty(int i)
   {
      return m_allProperties[i];
   }

   class DAVTextProperty extends DAVProperty
   {
      private String m_value;
      private String m_key;

      public DAVTextProperty(String key, Node n)
      {
         m_key = key;
         NodeList children = n.getChildNodes();

         CharacterData cd = (CharacterData)children.item(0);
         m_value = cd.getData();
      }

      public Object getValue()
      {
         return m_value;
      }

      public String getName()
      {
         return m_key;
      }
   }

   class DAVNodeProperty extends DAVProperty
   {
      private Node m_value;
      private String m_key;

      public DAVNodeProperty(Node n)
      {
         NamespaceScoped ns = (NamespaceScoped)n;
         String nameSpace = ns.getNamespace();
         if (nameSpace != null)
         {
            m_key = nameSpace+":"+ns.getLocalName();
         }
         else
         {
            m_key = ns.getLocalName();
         }

         m_value = n;
      }

      public Object getValue()
      {
         return m_value;
      }

      public String getName()
      {
         return m_key;
      }
   }


}

 