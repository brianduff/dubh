// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ProgressMonitor.java,v 1.1 2000-06-14 21:27:50 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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

package org.javalobby.dju.progress;


/**
 * A progress monitor is an object that is interested in the progress of
 * a potentially long operation. Typically a known class will contain methods
 * that may be long operations. This known class will implement
 * ProgressMonitorSupport. Another class which is calling methods on the
 * known class will call the setProgressMonitor() method on the known class
 * to set an object that will be notified of progress updates while the
 * method is being executed. The monitor can then display progress UI to the
 * user, or indicate progress in some other way.
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 * @version $Id: ProgressMonitor.java,v 1.1 2000-06-14 21:27:50 briand Exp $
 */
public interface ProgressMonitor
{
   /**
    * The long operation will call this method to notify the progress monitor
    * the minimum progress value for the operation. This method should only
    * be called if the operation is of a known length.
    */
   void setMinimum(int minProgress);

   /**
    * The long operation will call this method to notify the progress monitor
    * the maximum progress value for the operation. This method should only
    * be called if the operation is of a known length.
    */
   void setMaximum(int maxProgress);

   /**
    * The long operation will periodically call this method to indicate the
    * current progress through the operation. This value should always be
    * between or equal to the minimum and maximum progress values set with
    * the setMinimum() and setMaximum() methods. It should only be called
    * if the long operation is of a known length.
    */
   void setProgress(int progressValue);

   /**
    * The long operation may optionally periodically call this method to
    * indicate to the user the current progress. This method ought to be used
    * in long operations which are of an indeterminate length, but can
    * indicate their progress by other means. It can also be used for fixed
    * length operations however.
    */
   void setMessage(String progressMessage);

   /**
    * The long operation should call this method before it starts to indicate
    * whether it is a fixed length operation (i.e. the operation will take a
    * known number of steps to complete), or is of indeterminate length (i.e.
    * the number of steps involved in the operation is unknown or
    * can't be determined). This method will affect the way in which progress
    * is indicated to the user.
    */
   void setFixedLengthTask(boolean isFixedLength);

   /**
    * The long operation should call this method with a true argument to
    * indicate that is has completed successfully.
    */
   void setFinished(boolean isFinished);

   /**
    * The long operation should call this method on a regular basis to
    * determine whether the operation has been cancelled. If this method ever
    * returns true, the long operation should immediately abort itself, and
    * ideally, revert things back to the state they were in when the operation
    * started.
    */
   boolean isAborted();
}


//
// $Log: not supported by cvs2svn $
// 
