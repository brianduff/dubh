// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPCommands.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
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

package dubh.mail.nntp;

/**
 * Command strings for NNTP. These are unlikely to ever change, 
 * but using static constants reduces errors caused by typos
 * in hard coded strings.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPCommands.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
 */
class NNTPCommands
{
   public static final String
      QUIT                  = "QUIT",
      GROUP                 = "GROUP",
      LIST                  = "LIST",
      NEXT                  = "NEXT",
      NEWGROUPS             = "NEWGROUPS",
      HEAD                  = "HEAD",
      BODY                  = "BODY",
      XOVER                 = "XOVER",
      DATE                  = "DATE",
      POST                  = "POST",
      NEWNEWS               = "NEWNEWS",
      AUTHINFO_USER         = "AUTHINFO_USER",
      AUTHINFO_PASS         = "AUTHINFO_PASS";
}


//
// $Log: not supported by cvs2svn $
//