/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
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

package dubh.utils.help;


/**
 * A node in the help contents tree.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [03/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [03/07/98]
 */
class HelpContentsNode {
  protected String m_title;
  protected String m_class;
  protected String m_url;

  /**
   * Construct a Help Contents node.
   @param title the title of the node
   @param cls the class of node (see ContentsParser)
   @param url the url that the node points to (relative to the classpath)
   */
  public HelpContentsNode(String title, String cls, String url) {
     setTitle(title);
     setCls(cls);
     setURL(url);
  }

  public String getTitle() { return m_title; }
  public void setTitle(String t) { m_title = t; }

  public String getCls() { return m_class; }
  public void setCls(String c) { m_class = c; }

  public String getURL() { return m_url; }
  public void setURL(String u) { m_url = u; }

  public String toString() { return getTitle(); }
}