// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ProgressMonitorSupport.java,v 1.2 2001-02-11 02:52:11 briand Exp $
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


package org.dubh.dju.progress;


/**
 * A class that supports ProgressMonitors should implement this interface.
 * It defines a single operation which allows another class to add a
 * ProgressMonitor to the class providing progress monitor support.
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 * @version $Id: ProgressMonitorSupport.java,v 1.2 2001-02-11 02:52:11 briand Exp $
 */
public interface ProgressMonitorSupport
{
   /**
    * Set a progress monitor on your class. You should notify the progress
    * monitor when the progress of any long operation in your class
    * changes. The progress updates will be displayed to the user periodically.
    */
   public void setProgressMonitor(ProgressMonitor pm);
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  2000/06/14 21:27:50  briand
// Initial revision.
//
//
