// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: ClientRegistry.java,v 1.1 2000-02-22 23:48:13 briand Exp $
//   Copyright (C) 1999, 2000  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors: 
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package org.javalobby.javamail.client;

import java.util.Hashtable;

/**
 * The client registry determines which client implementation is used
 * for a particular protocol.
 *
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: ClientRegistry.java,v 1.1 2000-02-22 23:48:13 briand Exp $
 */
public final class ClientRegistry
{
   private static Hashtable m_hashStore = new Hashtable();
   private static Hashtable m_hashTransport = new Hashtable();

   /**
    * Make this class non instantiable
    */
   private ClientRegistry() {}

   /**
    * Use introspection to get a new instance of the correct class for
    * the specified protocol
    */
   private static final Object getNewInstance(Hashtable hash, String protocol)
   {
      String className = (String)hash.get(protocol);
      if (className == null)
      {
         return null;
      }

      try
      {
         Class c = Class.forName(className);
         return c.newInstance();
      }
      catch(Throwable thr)
      {
         System.err.println("ClientRegistry couldn't instantiate "+className);
         thr.printStackTrace();
      }
      return null;
   }

   /**
    * This method can be used to determine which client is to be used for a
    * specified protocol. The protocol must be usable as a javamail transport.
    * This method will instantiate a brand new transport client and return it.
    *
    * @param protocol The name of the protocol. Must not be null.
    * @return An object which is a transport client. May return null if
    *    no client has been set for the specified protocol..
    */
   public static final TransportClient getTransportClient(String protocol)
   {
      return (TransportClient)getNewInstance(m_hashTransport, protocol);
   }

   /**
    * This method can be used to determine which client is to be used for a
    * specified protocol. The protocol must be usable as a javamail store
    *
    * @param protocol The name of the protocol. Must not be null.
    * @return An object which is a store client. May return null if
    *    no client has been set for the specified protocol.
    */
   public static final StoreClient getStoreClient(String protocol)
   {
      return (StoreClient)getNewInstance(m_hashStore, protocol);
   }

   /**
    * This method should be used to set the client to be used for a particular
    * transport protocol. You should call this early.
    *
    * @param protocol The name of the protocol. Must not be null.
    * @param className A class name which is a transport client. Can be null, which
    *   unregisters any existing client.
    */
   public static final void setTransportClient(String protocol,
      String className)
   {
      if (className == null)
      {
         m_hashTransport.remove(protocol);
      }
      else
      {
         m_hashTransport.put(protocol, className);
      }
   }


   /**
    * This method should be used to set the client to be used for a particular
    * store protocol. You should call this early.
    *
    * @param protocol The name of the protocol. Must not be null.
    * @param className A class which is a store client. Can be null, which
    *   unregisters any existing client.
    */
   public static final void setStoreClient(String protocol,
      String className)
   {
      if (className == null)
      {
         m_hashStore.remove(protocol);
      }
      else
      {
         m_hashStore.put(protocol, className);
      }

   }

}


//
// $Log: not supported by cvs2svn $
//
