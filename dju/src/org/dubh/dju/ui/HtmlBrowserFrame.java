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

package dubh.utils.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.accessibility.*;
import java.awt.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;
import java.awt.event.*;


/**
 * A simple web browser with back / forward capabilities.
 *<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [??/06/98]: Initial Revision
 * <LI>0.2 [03/07/98]: Removed status bar and made text field non-editable.
 *   Changed internal layout slightly to make the frame easier to use in
 *   a subclass.
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.2 [03/07/98]
 */
public class HtmlBrowserFrame extends JFrame {

  protected HtmlPanel panHtml;
//  protected StatusBar status;
  protected JPanel  toolbar;
  protected JButton cmdBack;
  protected JButton cmdForward;
  protected JTextField tfURL;
  protected Vector history;
  protected JPanel mainPanel;
  protected int historyIndex = -1;
  protected int historyStop  = -1;

  public HtmlBrowserFrame() {
     this("");
  }

  public HtmlBrowserFrame(String title) {
     super(title);
     getContentPane().setLayout(new BorderLayout());
     mainPanel = new JPanel();
     mainPanel.setLayout(new BorderLayout());
     panHtml = new HtmlPanel();
     panHtml.addHyperlinkListener(new JumpListener());
//     status = new StatusBar();
     toolbar = new JPanel();
     history = new Vector();
     tfURL = new JTextField();
     toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
     cmdBack = new JButton("<");
     cmdForward = new JButton(">");
     cmdBack.setEnabled(false);
     cmdForward.setEnabled(false);
     tfURL.setPreferredSize(new Dimension(250,20));
     toolbar.add(cmdBack);
     toolbar.add(cmdForward);
     toolbar.add(tfURL);
     cmdBack.addActionListener(new BackListener());
     cmdForward.addActionListener(new ForwardListener());
     //status.setText("");
     mainPanel.add("North", toolbar);
     mainPanel.add("Center", panHtml);
//     mainPanel.add("South", status);
     getContentPane().add(mainPanel, BorderLayout.CENTER);
  }

  public void setURL(URL url) {
     panHtml.setURL(url);
     addToHistory(url);
     tfURL.setText(url.toString());
  }

  public static void main(String args[]) {
     URL myUrl;
     try {
        myUrl = new URL("file://"+System.getProperty("user.dir") + File.separator + "test.html");
        HtmlBrowserFrame hbf = new HtmlBrowserFrame("Test Frame");
        hbf.pack();
        hbf.setVisible(true);
        hbf.setURL(ClassLoader.getSystemResource("userdoc/index.html"));
     } catch (MalformedURLException ex) {
        System.err.println("Malformed URL Exception: "+"file://"+System.getProperty("user.dir")+ File.separator + "test.html");
     }
  }

  private void addToHistory(URL url) {
     historyIndex++;
     historyStop = historyIndex;
     if (historyIndex == history.size()) {
        history.addElement(url);
     } else {
        history.setElementAt(url, historyIndex);
     }
     cmdForward.setEnabled(false);
     cmdBack.setEnabled(true);
     toolbar.repaint();
    // printHistory();
  }

  private void printHistory() {
     System.out.println("---");
     int i=0;
     Enumeration enum = history.elements();
     while (enum.hasMoreElements()) {
        if (i == historyIndex) System.out.print("-->");
        if (i == historyStop) System.out.print("!");
        System.out.println(enum.nextElement().toString());
        i++;
     }
  }

// EVENT CLASSES

  class BackListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        if (historyIndex >= 1) {
           panHtml.setURL((URL)history.elementAt(historyIndex - 1));
           tfURL.setText(((URL)history.elementAt(historyIndex -1)).toString());
           historyIndex --;
           cmdForward.setEnabled(true);
           if (historyIndex == 0) cmdBack.setEnabled(false);
           toolbar.repaint();
        }
      //  printHistory();
     }
  }

  class ForwardListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        if (historyIndex < historyStop) {
           panHtml.setURL((URL)history.elementAt(historyIndex + 1));
           tfURL.setText(((URL)history.elementAt(historyIndex +1)).toString());
           historyIndex ++;
           cmdBack.setEnabled(true);
           if (historyIndex == historyStop) cmdForward.setEnabled(false);
           toolbar.repaint();
        }
        printHistory();
     }
  }

  class JumpListener implements HyperlinkListener {
     public void hyperlinkUpdate(HyperlinkEvent e) {
        /*
         * When the user clicks on a hyperlink, add one to historyIndex,
         * set historyStop = historyIndex. If historyIndex > history.size(),
         * addElement(url), otherwise setElementAt(historyIndex, url).
         * .. disable the forward button, and enable the back button 
         */
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
           addToHistory(e.getURL());
           tfURL.setText(e.getURL().toString());
        }
     }
  }

}

/**
 * Package level class implementing an Html enabled panel, from the Swing
 * 1.02 examples directory, with some mods
 */
  class HtmlPanel extends JPanel implements HyperlinkListener {
     JEditorPane html;

     public HtmlPanel() {
       try {

        setLayout(new BorderLayout());
        html = new JEditorPane();
        html.setEditable(false);
        html.addHyperlinkListener(this);
        JScrollPane scroller = new JScrollPane();
        JViewport vp = scroller.getViewport();
        vp.add(html);
        vp.setBackingStoreEnabled(true);
        add(scroller, BorderLayout.CENTER);
       } catch (Exception e) {
        System.err.println("Error in browser");
       }

     }

     /**
      * Manually set the URL
      @param url a URL to jump to
      */
     public void setURL(URL url) {
        try {
           html.setPage(url);
        } catch (IOException e) {
           //
        }
     }

     /**
      * Add a hyperlink listener to listen out for when the user enters,
      * exits and activates a hyperlink
      */
     public void addHyperlinkListener(HyperlinkListener listener) {
        html.addHyperlinkListener(listener);
     }

     /**
      * Removes a hyperlink listener
      */
     public void removeHyperlinkListener(HyperlinkListener listener) {
        html.removeHyperlinkListener(listener);
     }

     /**
      * Notification of a change relative to a
      * hyperlink.
      */
     public void hyperlinkUpdate(HyperlinkEvent e) {
        HyperlinkEvent.EventType type = e.getEventType();
        if (type == HyperlinkEvent.EventType.ACTIVATED)
            linkActivated(e.getURL());
     }

    /**
     * Follows the reference in an
     * link.  The given url is the requested reference.
     * By default this calls <a href="#setPage">setPage</a>,
     * and if an exception is thrown the original previous
     * document is restored and a beep sounded.  If an
     * attempt was made to follow a link, but it represented
     * a malformed url, this method will be called with a
     * null argument.
     *
     * @param u the URL to follow
     */
     protected void linkActivated(URL u) {
         Cursor c = html.getCursor();
         Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
         html.setCursor(waitCursor);
         SwingUtilities.invokeLater(new PageLoader(u, c));
     }

     public Dimension getMinimumSize() {
        return new Dimension(100, 500);
     }

     public Dimension getPreferredSize() {
        return getMinimumSize();
     }

    /**
     * temporary class that loads synchronously (although
     * later than the request so that a cursor change
     * can be done).
     */
     class PageLoader implements Runnable {
        URL url;
        Cursor cursor;
        PageLoader(URL u, Cursor c) {
            url = u;
            cursor = c;
         }

        public void run() {
            if (url == null) {
               // restore the original cursor
               html.setCursor(cursor);

               Container parent = html.getParent();
               parent.repaint();
            } else {
               Document doc = html.getDocument();
               try {
                  html.setPage(url);
               } catch (IOException ioe) {
                  html.setDocument(doc);
                  getToolkit().beep();
               } finally {
                  // schedule the cursor to revert after
                  // the paint has happended.
                  url = null;
                  SwingUtilities.invokeLater(this);
               }
            }
         }
     }

  }
