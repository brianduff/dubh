// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: NNTPServerException.java,v 1.3 1999-06-01 00:39:12 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package dubh.apps.newsagent.nntp;


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
