// $Id: DAVHref.java,v 1.1.1.1 2000-09-17 17:38:13 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;

/**
 * Represents the Href XML element for WebDAV defined in RFC2518. The content
 * of an href element is a URI.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVHref
{
   String m_uri;

   /**
    * Construct an href with the specified URI
    */
   public DAVHref(String uri)
   {
      m_uri = uri;
   }

   /**
    * Construct an href using the specified DOM element's PCDATA section as
    * the URI
    */
   public DAVHref(Node n)
   {
      m_uri = ((CharacterData)n.getFirstChild()).getData();
   }

   public String getURI()
   {
      return m_uri;
   }
}

 