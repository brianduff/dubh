// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: MessageProvider.java,v 1.5 2001-02-11 02:51:01 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
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


package org.dubh.apps.newsagent.nntp;

import java.util.*;
import java.io.*;
import javax.swing.tree.*;
import javax.swing.JProgressBar;
/**
 * MessageProvider abstract interface: Must be implemented by objects which
 * provide messages in some way.
 * <LI>0.1 [03/02/98]: Initial Revision
 * <LI>0.2 [03/03/98]: Changed getHeaders methods to return Vectors.
 * <LI>1.0 [20/03/98]: Added argumentless getHeaders and getBody with one arg
 *   so that Folder works properly (there is no newsgroup hierarchy in
 *   a Folder). This is a major interface change.
 * <LI>1.1 [30/03/98]: Added markMessageRead and deleteMessage. Changed
 *   getHeaders so that it returns a TreeModel.
 * <LI>1.2 [01/04/98]: Added ensureConnected() method, so that some providers
 *   can make sure they have an active network connection. Also added the
 *   getProviderName() method.
 * <LI>1.3 [03/04/98]: Added the ability to use a ProgressMonitor, for long
 *   operations. Added getHeaderCount.
 * <LI>1.4 [05/04/98]: Changed from ProgressMonitor to ProgressDialog (modal)
 * <LI>1.5 [08/04/98]: Changed from ProgressDialog to JProgressBar :)
 @author Brian Duff
 @version 1.5 [08/04/98]
 */
public interface MessageProvider {
       /**
        * getHeaders returns a tree of headers
        * available from the provider.
        */
       TreeModel getHeaders() throws IOException, NNTPServerException;
       TreeModel getHeaders(int max) throws IOException, NNTPServerException;
       TreeModel getHeaders(JProgressBar pm) throws IOException, NNTPServerException;
       TreeModel getHeaders(JProgressBar pm, int max) throws IOException, NNTPServerException;
       /**
        * Returns a count of the number of headers available on the provider.
        */
       int getHeaderCount();
       /**
        * retrieves the specified message body, given it's header
        */
       MessageBody getBody(MessageHeader mhead) throws IOException, NNTPServerException;

       /**
        * marks a specified message as "read".
        */
       void setMessageRead(MessageHeader mhead, boolean read);

       /**
        * Deletes a specified message permanently from the provider.
        */
       void deleteMessage(MessageHeader mhead);

       /**
        * Makes the provider connect if necessary.
        @return false if the provider was unable to connect.
        */
       boolean ensureConnected();

       /**
        * Returns a name that this message provider can be identified by. Will
        * be displayed on the user interface to let the user know where messages
        * are coming from.
        */
       String getProviderName();
}