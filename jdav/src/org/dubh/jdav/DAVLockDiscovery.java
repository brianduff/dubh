
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.CharacterData;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.sun.xml.tree.NamespaceScoped;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents the results of a WebDAV lock discovery.
 * <P>
 * @author Brian Duff
 */
public class DAVLockDiscovery extends DAVProperty
{

   private DAVActiveLock[] m_activeLocks;

   /**
    * Construct a WebDAV lock discovery from an input stream. The input stream
    * should provide an XML document with lock discovery information.
    *
    */
   public DAVLockDiscovery(Node n)
      throws DAVException
   {
      NamespaceScoped ns = (NamespaceScoped)n;
      if (!DAVUtils.isDAVNode(ns, "lockdiscovery"))
      {
         throw new DAVException(
            "Expected lockdiscovery, got: "+ns.getNamespace()+":"+
               ns.getLocalName()
         );
      }

      // All the children of lockdiscovery must be activelock elements.
      NodeList children = n.getChildNodes();

      m_activeLocks = new DAVActiveLock[children.getLength()];

      List l = new ArrayList();
      for (int i=0; i < children.getLength(); i++)
      {
         Node nKid = children.item(i);
         if (!(nKid instanceof CharacterData))
         {
            l.add(new DAVActiveLock(nKid));
         }

      }
      m_activeLocks = new DAVActiveLock[l.size()];
      l.toArray(m_activeLocks);
   }

   public DAVActiveLock[] getActiveLocks()
   {
      return m_activeLocks;
   }

   public String getName()
   {
      return "lockdiscovery";
   }

   public Object getValue()
   {
      return this;
   }
}

 