// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: StoreClient.java,v 1.2 2001-02-11 02:52:48 briand Exp $
//   Copyright (C) 1999 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1999 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
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


package org.dubh.javamail.client;



/**
 * A store client is just a marker interface that objects should implement if
 * they provide a client implementation of a protocol which is used for a
 * javamail store.
 *
 * TODO: More info once other classes have been written.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: StoreClient.java,v 1.2 2001-02-11 02:52:48 briand Exp $
 */
public interface StoreClient
{
   // There are no methods on this interface; it's just a "marker".
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  2000/02/22 23:48:13  briand
// Client layer for javamail. Initial revision.
//
//
