// $Id: DAVUtils.java,v 1.1.1.1 2000-09-17 17:38:11 briand Exp $
// Copyright (c) 2000 Brian Duff Dubh
package org.dubh.jdav;

import com.sun.xml.tree.NamespaceScoped;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.ProtocolException;
import java.net.URL;

import org.dubh.jdav.WebDAVURLConnection;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Various utilities for using WebDAV.
 * <P>
 * @author Brian Duff
 */
public class DAVUtils extends Object
{
   public static final String DAV_NAMESPACE="DAV:";

   /**
    * Is the specified XML namespace scoped object valid in the DAV namespace?
    */
   public static boolean isDAVNode(NamespaceScoped n, String propertyType)
   {
      return (DAV_NAMESPACE.equals(n.getNamespace()) &&
              propertyType.equals(n.getLocalName()));
   }

   /**
    * Takes out a write lock on the specified resource and
    * returns a lock token. The timeout is set to "infinite"
    */
   public static DAVLockToken lockResource(URL url, boolean isExclusive,
      String owner)
      throws DAVException
   {
      try
      {
         WebDAVURLConnection conn = new WebDAVURLConnection(url);
         conn.setDoOutput(true);
         conn.setRequestMethod("LOCK");
         conn.setRequestProperty("Timeout", "Infinite");

         PrintWriter p = new PrintWriter(conn.getOutputStream());

         // Should really use the parser to generate this.
         p.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
         p.println("<D:lockinfo xmlns:D=\"DAV:\">");
         p.println("  <D:lockscope>" +
            (isExclusive ? "<D:exclusive />" : "<D:shared />") +
            "</D:lockscope>"
         );
         p.println("  <D:locktype><D:write /></D:locktype>");
         p.println("  <D:owner>");
         p.println(owner);
         p.println("  </D:owner>");
         p.println("</D:lockinfo>");
         p.close();

         conn.connect();

         // If we get a 200 response, the lock succeeded.
         if (conn.getResponseCode() != 200)
         {
            conn.disconnect();
            throw new DAVException(url,
               conn.getResponseCode(), conn.getResponseMessage()
            );
         }

         DAVProperties prop = new DAVProperties(parseXML(conn.getInputStream()));
         conn.disconnect();
         int propCount = prop.getPropertyCount();

         DAVLockDiscovery lockDiscovery = null;

         for (int i=0; i < propCount; i++)
         {
            DAVProperty thisProp = prop.getProperty(i);
            if ("lockdiscovery".equals(thisProp.getName()))
            {
               lockDiscovery = (DAVLockDiscovery)thisProp.getValue();
               break;
            }
         }

         if (lockDiscovery == null)
         {
            throw new DAVException("Expected lockdiscovery, not found");
         }
         return findMyLock(lockDiscovery.getActiveLocks(), owner, isExclusive);

      }
      catch (Exception ioe)
      {
         if (ioe instanceof DAVException)
         {
            throw (DAVException) ioe;
         }

         System.out.println(ioe);
         throw new DAVException(url, ioe);
      }
   }

   /**
    * Parse the contents of an input stream, which must contain valid XML.
    * Returns the document node.
    */
   public static Node parseXML(InputStream is)
      throws SAXParseException, SAXException, ParserConfigurationException,
             IOException
   {

      DocumentBuilderFactory dbfFactory =
         DocumentBuilderFactory.newInstance();

      DocumentBuilder dbBuilder = dbfFactory.newDocumentBuilder();

      // Lets parse the choices file
      Document doc = dbBuilder.parse(is);

      doc.getDocumentElement().normalize();

      return doc.getDocumentElement();

   }

   /**
    * Given a list of all active locks on a resource, find the lock
    * belonging to the specified owner with the specified type
    *
    * @param allLocks all active locks on a resource
    * @param owner the owner of the lock to search for
    * @param isExclusive if the lock is exclusive
    * @returns an instance of DAVLockToken if the lock was found, or null
    *          if a lock matching the specified criteria could not be located.
    */
   private static DAVLockToken findMyLock(DAVActiveLock[] allLocks,
      String owner, boolean isExclusive)
   {
      // This owner stuff is a bit dodgy.
      for (int i=0; i < allLocks.length; i++)
      {
         DAVActiveLock al = allLocks[i];
         DAVLockScope.DAVLockScopeType lsType = al.getLockScope().getType();

         if ((isExclusive && lsType == DAVLockScope.EXCLUSIVE)
         ||  (!isExclusive && lsType == DAVLockScope.SHARED))
         {
            // The scope matches. What about the owner?
            DAVOwner theOwner = al.getOwner();
            String ownerData = ((CharacterData)theOwner.getNode().getFirstChild()).getData();
            if (owner.trim().equals(ownerData.trim()))
            {
               // OK, we've found a match
               return al.getLockToken();
            }
         }
      }

      // We don't seem to have found a matching lock token, so just return
      // null
      return null;
   }

   /**
    * Unlocks a resource.
    */
   public static void unlockResource(URL url, DAVLockToken tok)
      throws DAVException
   {
      try
      {
         // Just pick the first token.
         DAVHref[] uris = tok.getTokenURIs();
         
         WebDAVURLConnection conn = new WebDAVURLConnection(url);
         conn.setRequestMethod("UNLOCK");
         conn.setRequestProperty("Lock-Token", "<"+uris[0].getURI()+">");

         conn.connect();

         if (conn.getResponseCode() > 299)
         {
            conn.disconnect();
            throw new DAVException(url,
               conn.getResponseCode(),
               conn.getResponseMessage()
            );
         }
         conn.disconnect();

      }
      catch (Exception e)
      {
         if (e instanceof DAVException)
         {
            throw (DAVException)e;
         }
         throw new DAVException(url, e);
      }
   }


   public static void dumpStream(InputStream is)
      throws IOException
   {
      BufferedReader br = new BufferedReader(new InputStreamReader(
         is
      ));

      String line = br.readLine();
      while (line != null)
      {
         System.out.println(line);
         line = br.readLine();
      }
      br.close();

   }
}

