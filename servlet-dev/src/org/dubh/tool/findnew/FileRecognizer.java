// ---------------------------------------------------------------------------
//   Dubh Servlets and Tools
//   $Id: FileRecognizer.java,v 1.1.1.1 2001-06-03 05:07:02 briand Exp $
//   Copyright (C) 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 2001 Brian Duff
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

package org.dubh.tool.findnew;

import java.io.File;

/**
 * The FileRecognizer interface is implemented by objects that know how to
 * "parse" documents in a document tree and return information about them.
 *
 * @author Brian.Duff@oracle.com
 */
public interface FileRecognizer
{
   /**
    * If this recognizer recognizes the specified file, it should return
    * true.
    *
    * @param f a file
    * @return true if this recognizer recognizes f
    */
   public boolean isRecognizedFile(File f);

   /**
    * The recognizer should return information on the specified
    * file, which is guaranteed to be a file that the recognizer has
    * previously returned true for from isRecognizedFile(File).
    *
    * @param f a file
    * @return a FileInformation implementation with information about
    *    the specified file.
    */
   public FileInformation getInformation(File f);
}