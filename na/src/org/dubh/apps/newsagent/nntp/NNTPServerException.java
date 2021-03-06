// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NNTPServerException.java,v 1.5 2001-02-11 02:51:01 briand Exp $
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


/**
 * Represents any exception which might occur due to an NNTP Protocol error.
 * Subclassed by specific exceptions.
 *
 */
public class NNTPServerException extends Exception {

  public NNTPServerException() {
         super();
  }

  public NNTPServerException(String s) {
         super(s);
  }

}

/**
 * NNTPServerDiscontinuedException
 * Corresponds to status code 400 from the NNTP Server.
 */
class NNTPServerDiscontinuedException extends NNTPServerException {
       public NNTPServerDiscontinuedException() {
              super();
       }
       public NNTPServerDiscontinuedException(String s) {
              super(s);
       }
}

/**
 * NNTPServerCommandError
 * Corresponds to status code 500 or 501 from the NNTP Server.
 */
class NNTPServerCommandException extends NNTPServerException {
      public NNTPServerCommandException() {
             super();
      }
      public NNTPServerCommandException(String s) {
             super(s);
      }
}

/**
 * NNTPServerPermissionException
 * Corresponds to status code 502 from the NNTP Server.
 */
class NNTPServerPermissionException extends NNTPServerException {
      public NNTPServerPermissionException() {
             super();
      }
      public NNTPServerPermissionException(String s) {
             super(s);
      }
}

/**
 * NNTPServerFaultException
 * Corresponds to status code 503 from the NNTP Server.
 */
class NNTPServerFaultException extends NNTPServerException {
      public NNTPServerFaultException() {
             super();
      }
      public NNTPServerFaultException(String s) {
             super(s);
      }
}

/**
 * NNTPServerBadArticleException
 * Corresponds to status codes 420, 421, 422, 423 and 430 from the NNTP Server.
 */
class NNTPServerBadArticleException extends NNTPServerException {
      public NNTPServerBadArticleException() {
             super();
      }
      public NNTPServerBadArticleException(String s) {
             super(s);
      }
}

/**
 * NNTPServerBadNewsgroupException
 * Corresponds to status code 411, 412
 */
class NNTPServerBadNewsgroupException extends NNTPServerException {
      public NNTPServerBadNewsgroupException() {
             super();
      }
      public NNTPServerBadNewsgroupException(String s) {
             super(s);
      }
}

/**
 * NNTPServerArticleRefusedException
 * Corresponds to status codes 435, 436 and 437, 440, 441. A posted article was rejected
 * by the server.
 */
class NNTPServerArticleRefusedException extends NNTPServerException {
      public NNTPServerArticleRefusedException() {
             super();
      }
      public NNTPServerArticleRefusedException(String s) {
             super(s);
      }
}



/**
 * NNTPServerInternalException
 * Unknown catchall exception. The string holds more information on the error.
 */
class NNTPServerInternalException extends NNTPServerException {
      public NNTPServerInternalException() {
             super();
      }
      public NNTPServerInternalException(String s) {
             super(s);
      }
}


class TestException extends RuntimeException {
      public TestException() {
             super();
      }
}
