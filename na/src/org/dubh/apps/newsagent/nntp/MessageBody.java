// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MessageBody.java,v 1.3 1999-06-01 00:39:12 briand Exp $
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

import java.io.*;
/**
 * MessageBody - the body of an NNTP news message.
 * <LI>0.1 [03/03/98]: Initial Revision.
 * <LI>0.2 [09/06/98]: Added toString()
 @author Brian Duff
 @version 0.2 [09/06/98]: Initial Revision
 */
public class MessageBody implements Serializable {
       private String m_text;

       /**
        * Constructs a new, blank messagebody
        */
       public MessageBody() {
         m_text = "";
       }

       /**
        * Constructs a new messagebody with the specified text
        @param initialText the text of the body
        */
       public MessageBody(String initialText) {
         m_text = initialText;
       }

       /**
        * Constructs a new message body, assuming it is a reply to the specified
        * header. This often means inserting "In <msg-id>, <user> wrote:" or
        * something similar at the top of the message. Nb. The original message
        * is NOT quoted: a call to insertQuoted must be made manually.
        @param hd The header of the original message
        */
       public MessageBody(MessageHeader hd) {
       }

       /**
        * Retrieves the text contents of this message.
        @returns a String containing the message text
        */
       public String getText() {
                  return m_text;
       }

       /**
        * Retrieves the text contents of this message
        @see getText()
        */
       public String toString() {
        return getText();
       }

       /**
        * Sets the text of the message.
        @param newText The text to use for the message
        */
       public String setText(String newText) {
              return m_text = newText;
       }

       /**
        * Inserts text into the message starting at the specified character
        * position
        @param insText text to be inserted
        @param pos position of inserted text. 0 indicates the start of the
        message.
        */
       public void insertText(String insText, int pos) {
         StringBuffer b = new StringBuffer(m_text);
        b.insert(pos, insText);
        m_text = b.toString();
       }

       /**
        * Inserts text into the message, as "quoted" text. This normally means
        * that each line is preceeded by a > character.
        @param insText text to be inserted
        @param pos position of inserted text, 0 indicates start of message
        */
       public void insertQuoted(String insText, int pos) {
         // Nb. Will need to check for line wrapping eventually :)
        StringBuffer b = new StringBuffer(m_text);
        b.insert(pos, insText);
        m_text = b.toString();
       }

}