// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: MessageBody.java,v 1.5 2001-02-11 02:51:01 briand Exp $
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