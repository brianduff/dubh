// ---------------------------------------------------------------------------
//   Dubh Servlets and Tools
//   $Id: FileInformation.java,v 1.1.1.1 2001-06-03 05:07:02 briand Exp $
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
 * The FileInformation interface describes information about a particular
 * file.
 *
 * @author Brian.Duff@oracle.com
 */
public interface FileInformation
{
   /**
    * The description of a file
    *
    * @return a string description of the file. This can be null.
    */
   public String getDescription();

   /**
    * The name of a file's author.
    *
    * @return a string containing the author of the file. This can
    *    be null.
    */
   public String getAuthor();

   /**
    * The title of a file.
    *
    * @return a string containing the title of the file. This can
    *    be null.
    */
   public String getTitle();

   /**
    * The revision of a file
    *
    * @return a string containing the revision of the file. This can
    *    be null.
    */
   public String getRevision();
}