// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: OperationProgressAdapter.java,v 1.5 2001-02-11 02:52:10 briand Exp $
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

package org.dubh.dju.event;
/**
 * An OperationProgressEvent is fired by a lengthy operation. This is useful
 * for when you want to use a progress bar or similar way of indicating that
 * an operation is progressing normally.<P>
 * OperationProgressAdapter provides default (empty) implementations for each
 * method of OperationProgressListener.
 * <B>Revision History:</B><UL>
 * <LI>0.1 [29/06/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [29/06/98]
 @see OperationProgressListener
 */
public abstract class OperationProgressAdapter implements OperationProgressListener {

  public void setMinimum(OperationProgressEvent e) {}
  public void setMaximum(OperationProgressEvent e) {}
  public void setProgress(OperationProgressEvent e) {}
  public void setDescription(OperationProgressEvent e) {}
  public void finished(OperationProgressEvent e) {}


}