//----------------------------------------------------------------------------
//   NewsAgent: Java Newsreader
//   Newsgroup.java
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
import javax.mail.*;
import javax.mail.event.*;

/**
 * JavaMail folder for newsgroups.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version 0.0
 */
public class Newsgroup extends Folder {

	String name;
	int count = -1;
	int first = -1;
	int last = -1;
	boolean postingAllowed = false;
	boolean subscribed = false;

	boolean open = false;

	Article[] articles;
	Date checkpoint;

	/**
	 * Constructor.
	 */
	protected Newsgroup(Store store, String name) {
		super(store);
		this.name = name;
	}

	/**
	 * Constructor.
	 */
	protected Newsgroup(Store store, String name, int count, int first, int last) {
		super(store);
		this.name = name;
		this.count = count;
		this.first = first;
		this.last = last;
	}

	/**
	 * Returns the name of this folder.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the full name of this folder.
	 */
	public String getFullName() {
		return getName();
	}

	/**
	 * Returns the type of this folder.
	 */
	public int getType() throws MessagingException {
		return HOLDS_MESSAGES;
	}

	/**
	 * Indicates whether this folder exists.
	 */
	public boolean exists() throws MessagingException {
		if (open) return true;
		try {
			open(READ_ONLY);
			close(false);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * Indicates whether this folder contains any new articles.
	 */
	public boolean hasNewMessages() throws MessagingException {
		return getNewMessageCount()>0;
	}

	/**
	 * Opens this folder.
	 */
	public void open(int mode) throws MessagingException {
		if (open)
			throw new MessagingException("Newsgroup is already open");
		if (mode!=READ_ONLY)
			throw new MessagingException("Newsgroup is read-only");
		((NNTPStore)store).open(this);
		open = true;
		notifyConnectionListeners(ConnectionEvent.OPENED);
		
	}

	/**
	 * Closes this folder.
	 */
	public void close(boolean expunge) throws MessagingException {
		if (!open)
			throw new MessagingException("Newsgroup is not open");
        if (expunge) expunge();
		((NNTPStore)store).close(this);
		open = false;
		notifyConnectionListeners(ConnectionEvent.CLOSED);
	}

	/**
	 * Expunges this folder.
	 */
	public Message[] expunge() throws MessagingException {
        throw new MessagingException("Newsgroup is read-only");	
	}

	/**
	 * Indicates whether this folder is open.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Returns the permanent flags for this folder.
	 */
	public Flags getPermanentFlags() { return new Flags(); }
	
	/**
	 * Returns the number of articles in this folder.
	 */
	public int getMessageCount() throws MessagingException {
		return getMessages().length;
	}

	/**
	 * Returns the articles in this folder.
	 */
	public Message[] getMessages() throws MessagingException {
		if (!open)
			throw new MessagingException("Newsgroup is not open");
        NNTPStore s = (NNTPStore)store;
		if (articles==null)
			articles = s.getArticles(this);
		else { // check for new articles
			Article[] nm = s.getNewArticles(this, checkpoint);
			if (nm.length>0) {
				Article[] m2 = new Article[articles.length+nm.length];
				System.arraycopy(articles, 0, m2, 0, articles.length);
				System.arraycopy(nm, 0, m2, articles.length, nm.length);
				articles = m2;
			}
		}
		checkpoint = new Date();
        return articles;
	}
	
	/**
	 * Returns the specified article in this folder.
	 * Since NNTP articles are not stored in sequential order,
	 * the effect is just to reference articles returned by getMessages().
	 */
	public Message getMessage(int msgnum) throws MessagingException {
		return getMessages()[msgnum-1];
	}
	
	/**
	 * NNTP folders are read-only.
	 */
	public void appendMessages(Message articles[]) throws MessagingException {
		throw new MessagingException("Folder is read-only");
	}

	/**
	 * Does nothing.
	 */
	public void fetch(Message articles[], FetchProfile fetchprofile) throws MessagingException {
	}

	// -- These must be overridden to throw exceptions --

	/**
	 * NNTP folders can't have parents.
	 */
	public Folder getParent() throws MessagingException {
		throw new MessagingException("Newsgroup can't have a parent");
	}

	/**
	 * NNTP folders can't contain subfolders.
	 */
	public Folder[] list(String s) throws MessagingException {
		throw new MessagingException("Newsgroups can't contain subfolders");
	}

	/**
	 * NNTP folders can't contain subfolders.
	 */
	public Folder getFolder(String s) throws MessagingException {
		throw new MessagingException("Newsgroups can't contain subfolders");
	}

	/**
	 * NNTP folders can't contain subfolders.
	 */
	public char getSeparator() throws MessagingException {
		throw new MessagingException("Newsgroups can't contain subfolders");
	}

	/**
	 * NNTP folders can't be created, deleted, or renamed.
	 */
	public boolean create(int i) throws MessagingException {
		throw new MessagingException("Newsgroups can't be created");
	}

	/**
	 * NNTP folders can't be created, deleted, or renamed.
	 */
	public boolean delete(boolean flag) throws MessagingException {
		throw new MessagingException("Newsgroups can't be deleted");
	}

	/**
	 * NNTP folders can't be created, deleted, or renamed.
	 */
	public boolean renameTo(Folder folder) throws MessagingException {
		throw new MessagingException("Newsgroups can't be renamed");
	}

}