// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: SendAgentMessage.java,v 1.5 2001-02-11 02:50:59 briand Exp $
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

package org.dubh.apps.newsagent.agent;
/**
 * A reply from a SendAgent.
 * Version History: <UL>
 * <LI>0.1 [14/04/98]: Initial Revision
 * <LI>0.2 [07/06/98]: Added support for warnings and errors.
 *</UL>
 @author Brian Duff
 @version 0.2 [07/06/98]
 */
public class SendAgentMessage {

 /** if there are errors from an agent, the message can't be sent */
 protected boolean m_wasError;
 /** if there are warnings from an agent, the message can only be sent upon
  *  user confirmation
  */
 protected boolean m_wasWarning;
 /** whether the header changed */
 protected boolean m_headerChanged;
 /** whether the body changed */
 protected boolean m_bodyChanged;
 /** The message */
 protected String m_message;


 /**
  * Construct a SendAgentMessage, in which the header didn't change, the
  * body didn't change, and there were no problems with the message.
  */
 public SendAgentMessage() {
   m_wasError = false;
   m_wasWarning = false;
   m_headerChanged = false;
   m_bodyChanged = false;
   m_message = null;
 }

 /**
  * Construct a SendAgentMessage, in which the header or body might have
  * changed, but there were no problems.
  @param didHeaderChange true if this agent changed the header
  @param didBodyChange true if this agent changed the body
  */
 public SendAgentMessage(boolean didHeaderChange, boolean didBodyChange) {
   m_wasError = false;
   m_wasWarning = false;
   m_headerChanged = didHeaderChange;
   m_bodyChanged = didBodyChange;
   m_message = null;
 }

 /**
  * Construct a SendAgentMessage, in which the header or body might have
  * changed, and where there were possibly problems (warnings)
  @param didHeaderChange true if the agent changed the header
  @param didBodyChange   true if the agent changed the body
  @param wasWarning        true if there was a problem with the message.
  @param msgWarning       The error message, if there was an error   ( or null)
  */
 public SendAgentMessage(boolean didHeaderChange, boolean didBodyChange,
                         boolean wasWarning, String msgWarning) {
   m_wasWarning = wasWarning;
   m_wasError = false;
   m_headerChanged = didHeaderChange;
   m_bodyChanged = didBodyChange;
   m_message = msgWarning;
 }

 /**
  * Construct a SendAgentMessage, in which the header or body haven't
  * changed, and where there were possibly problems.
  @param wasWarning        true if there was a problem with the message.
  @param msgWarning        The error message, if there was an error   ( or null)
  */
 public SendAgentMessage(boolean wasWarning, String msgWarning) {
   m_wasWarning = wasWarning;
   m_wasError   = false;
   m_headerChanged = false;
   m_bodyChanged = false;
   m_message = msgWarning;
 }

 /**
  * Set this message to be an error message. An error message is stronger
  * than a warning message, in that an outgoing message which has errors
  * cannot be sent until the errors have been corrected.
  @param b true sets this message to an error.
  */
 public void setErrorMessage(boolean b) {
  m_wasError = b;
  m_wasWarning = false;
 }

 /**
  * Set this message to be an error message. An error message is stronger
  * than a warning message, in that an outgoing message which has errors
  * cannot be sent until the errors have been corrected.
  @param b true sets this message to an error.
  @param msg the message to display
  */
 public void setErrorMessage(boolean b, String msg) {
  setErrorMessage(b);
  m_message = msg;
 }


 /**
  * Get whether there was an error
  */
 public boolean isErrorMessage() {
   return m_wasError;
 }

 /**
  * Get whether there was a warning
  */
 public boolean isWarningMessage() {
  return m_wasWarning;
 }

 /**
  * Get whether the header changed.
  */
 public boolean isHeaderChanged() {
   return m_headerChanged;
 }

 /**
  * Get whether the body changed
  */
 public boolean isBodyChanged() {
   return m_bodyChanged;
 }

 /**
  * Get the error message. This corresponds to both error and warning messages,
  * since a SendAgentMessage can only be one or the other (and not both).
  */
 public String getErrorMessage() {
   return m_message;
 }

}