// $Id: DAVTimeout.java,v 1.1.1.1 2000-09-17 17:38:14 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;

import com.sun.xml.tree.NamespaceScoped;

/**
 * Represents a timeout associated with a lock
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public class DAVTimeout
{
   private TimeType m_timeType;
   
   /**
    * Constructs a lock timeout based on a DOM XML Node.
    */
   public DAVTimeout(Node n)
      throws DAVException
   {
      // The PCDATA of the node must be compatible with timetype.
      m_timeType = TimeType.getInstance(((CharacterData)n.getFirstChild()).getData());
   }
}

 