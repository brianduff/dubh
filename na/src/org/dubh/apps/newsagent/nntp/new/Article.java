//----------------------------------------------------------------------------
//   NewsAgent: Java Newsreader
//   CRLFInputStream.java
//
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
//
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
//
// Revision History:
//   File  DJU    Date       Who    Changes
//   ----  ---    ----       ---    -------
//   0.0   1.1.0  5 Feb 1999 BD     Initial Revision
//
//----------------------------------------------------------------------------

package dubh.apps.newsagent.nntp;

import java.io.*;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * The message class implementing the NNTP mail protocol.
 *
 * @author dog@dog.net.uk
 * @version 0.2
 */
public class Article extends MimeMessage {

	/**
	 * The unique message-id of this message.
	 */
	protected String messageId;

    // Whether the headers for this article were retrieved using xover.
	boolean xoverHeaders = false;

	/**
	 * Creates an NNTP message with the specified article number.
	 */
	protected Article(Newsgroup folder, int msgnum) throws MessagingException {
		super(folder, msgnum);
	}

	/**
	 * Creates an NNTP message with the specified message-id.
	 */
	protected Article(Newsgroup folder, String messageId) throws MessagingException {
		super(folder, 0);
		this.messageId = messageId;
	}

    // Adds headers retrieved by an xover call to this article.
	void addXoverHeaders(InternetHeaders headers) {
		if (headers==null) {
			this.headers = headers;
			xoverHeaders = true;
		}
	}

	/**
	 * Returns the message content.
	 */
	public Object getContent() throws IOException, MessagingException {
		if (content==null) content = ((NNTPStore)folder.getStore()).getContent(this);
		return super.getContent();
	}

	/**
	 * Returns the from address.
	 */
	public Address[] getFrom() throws MessagingException {
        NNTPStore s = (NNTPStore)folder.getStore();
		if (xoverHeaders && !s.validateOverviewHeader("From")) headers = null;
		if (headers==null) headers = s.getHeaders(this);

		Address[] a = getAddressHeader("From");
		if (a==null) a = getAddressHeader("Sender");
		return a;
	}

	/**
	 * Returns the recipients' addresses for the specified RecipientType.
	 */
	public Address[] getRecipients(RecipientType type) throws MessagingException {
		NNTPStore s = (NNTPStore)folder.getStore();
		if (xoverHeaders && ! s.validateOverviewHeader(getHeaderKey(type))) headers = null;
		if (headers==null) headers = s.getHeaders(this);
		
		if (type==RecipientType.NEWSGROUPS) {
			String key = getHeader("Newsgroups", ",");
			if (key==null) return null;
			return NewsAddress.parse(key);
		} else {
			return getAddressHeader(getHeaderKey(type));
		}
	}

	/**
	 * Returns all the recipients' addresses.
	 */
	public Address[] getAllRecipients() throws MessagingException {
		if (headers==null || xoverHeaders) headers = ((NNTPStore)folder.getStore()).getHeaders(this);
		
		return super.getAllRecipients();
	}

	/**
	 * Returns the reply-to address.
	 */
	public Address[] getReplyTo() throws MessagingException {
        NNTPStore s = (NNTPStore)folder.getStore();
		if (xoverHeaders && !s.validateOverviewHeader("Reply-To")) headers = null;
		if (headers==null) headers = s.getHeaders(this);

		Address[] a = getAddressHeader("Reply-To");
		if (a==null) a = getFrom();
		return a;
	}

	/**
	 * Returns the subject line.
	 */
	public String getSubject() throws MessagingException {
        NNTPStore s = (NNTPStore)folder.getStore();
		if (xoverHeaders && !s.validateOverviewHeader("Subject")) headers = null;
		if (headers==null) headers = s.getHeaders(this);

		return super.getSubject();
	}

	/**
	 * Returns the sent date.
	 */
	public Date getSentDate() throws MessagingException {
        NNTPStore s = (NNTPStore)folder.getStore();
		if (xoverHeaders && !s.validateOverviewHeader("Date")) headers = null;
		if (headers==null) headers = s.getHeaders(this);

		return super.getSentDate();
	}

	/**
	 * Returns the received date.
	 */
	public Date getReceivedDate() throws MessagingException {
		if (headers==null || xoverHeaders) headers = ((NNTPStore)folder.getStore()).getHeaders(this);
		
		return super.getReceivedDate();
	}

	/**
	 * Returns an array of addresses for the specified header key.
	 */
	protected Address[] getAddressHeader(String key) throws MessagingException {
		String header = getHeader(key, ",");
		if (header==null) return null;
		try {
			return InternetAddress.parse(header);
		} catch (AddressException e) {
            String message = e.getMessage();
			if (message!=null && message.indexOf("@domain")>-1)
				try {
					return parseAddress(header, ((NNTPStore)folder.getStore()).getHostName());
				} catch (AddressException e2) {
					throw new MessagingException("Invalid address: "+header, e);
				}
			throw e;
		}
	}

	/**
	 * Makes a pass at parsing internet addresses.
	 */
	protected Address[] parseAddress(String in, String defhost) throws AddressException {
        Vector v = new Vector();
		for (StringTokenizer st = new StringTokenizer(in, ","); st.hasMoreTokens(); ) {
            String s = st.nextToken().trim();
			try {
				v.addElement(new InternetAddress(s));
			} catch (AddressException e) {
				int index = s.indexOf('>');
				if (index>-1) { // name <address>
					StringBuffer buffer = new StringBuffer();
					buffer.append(s.substring(0, index));
					buffer.append('@');
					buffer.append(defhost);
					buffer.append(s.substring(index));
					v.addElement(new InternetAddress(buffer.toString()));
				} else {
					index = s.indexOf(" (");
					if (index>-1) { // address (name)
						StringBuffer buffer = new StringBuffer();
						buffer.append(s.substring(0, index));
						buffer.append('@');
						buffer.append(defhost);
						buffer.append(s.substring(index));
						v.addElement(new InternetAddress(buffer.toString()));
					} else // address
						v.addElement(new InternetAddress(s+"@"+defhost));
				}

			}
		}
        Address[] a = new Address[v.size()]; v.copyInto(a);
		return a;
	}

	/**
	 * Returns the header key for the specified RecipientType.
	 */
	protected String getHeaderKey(RecipientType type) throws MessagingException {
		if (type==RecipientType.TO)
			return "To";
		if (type==RecipientType.CC)
			return "Cc";
		if (type==RecipientType.BCC)
			return "Bcc";
		if (type==RecipientType.NEWSGROUPS)
			return "Newsgroups";
		throw new MessagingException("Invalid recipient type: "+type);
	}

	// -- Need to override these since we are read-only --

	/**
	 * NNTP messages are read-only.
	 */
	public void setFrom(Address address) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void addFrom(Address a[]) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setRecipients(javax.mail.Message.RecipientType recipienttype, Address a[]) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void addRecipients(javax.mail.Message.RecipientType recipienttype, Address a[]) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setReplyTo(Address a[]) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setSubject(String s, String s1) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setSentDate(Date date) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setDisposition(String s) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setContentID(String s) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setContentMD5(String s) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setDescription(String s, String s1) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

	/**
	 * NNTP messages are read-only.
	 */
	public void setDataHandler(DataHandler datahandler) throws MessagingException {
		throw new IllegalWriteException("Article is read-only");
	}

}