/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package dubh.apps.newsagent.agent;

/**
 * A reply from a ListAgent.
 * Version History: <UL>
 * <LI>0.1 [28/04/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [28/04/98]
 */
public class ListAgentMessage {

 /** whether to delete the message from the list of messages */
 protected boolean m_wasDelete;
 /** whether the header changed */
 protected boolean m_headerChanged;
 /** whether the font or colour changed */
 protected boolean m_displayChanged;
 /** whether to display a message to the user */
 protected boolean m_userMessage;
 /** The message */
 protected String m_message;


 /**
  * Construct a ListAgentMessage, in which nothing changed.
  */
 public ListAgentMessage() {
   m_wasDelete = false;
   m_headerChanged = false;
   m_displayChanged = false;
   m_userMessage = false;
   m_message = "";
 }

 /**
  * Construct a ListAgentMessage
  @param didDelete whether the message is to be deleted
  @param didChange whether the contents of the header changed
  @param didChangeDisplay whether the appearance (font or colour) of the message
     was changed
  @param isUserMessage whether this reply should display a message to the user
  @param message the message to display, or an empty string.
  */
 public ListAgentMessage(boolean didDelete, boolean didChange,
             boolean didChangeDisplay, boolean isUserMessage, String message) {
   m_wasDelete = didDelete;
   m_headerChanged = didChange;
   m_displayChanged = didChangeDisplay;
   m_userMessage = isUserMessage;
   m_message = message;
 }

 /**
  * Is this reply a deletion?
  @return true if the header is to be deleted
  */
 public boolean isDelete() {
   return m_wasDelete;
 }

 /**
  * Did the message header contents change?
  */
 public boolean isHeaderChanged() {
   return m_headerChanged;
 }

 /**
  * Did the appearance of the message change?
  */
 public boolean isAppearanceChange() {
   return m_displayChanged;
 }

 /**
  * Is this reply a message to display to the user?
  */
 public boolean isUserMessage() {
   return m_userMessage;
 }

 /**
  * Get the message to display to the user.
  */
 public String getUserMessage() {
   return m_message;
 }

}
