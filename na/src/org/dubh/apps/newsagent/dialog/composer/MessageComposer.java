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
package dubh.apps.newsagent.dialog.composer;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import java.io.*;
import dubh.utils.misc.*;
import dubh.utils.ui.*;

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.IUpdateableClass;
import dubh.apps.newsagent.dialog.ErrorReporter;
import dubh.apps.newsagent.dialog.preferences.OptionsFrame;
import dubh.apps.newsagent.nntp.NNTPServer;
import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;
import dubh.apps.newsagent.nntp.Newsgroup;
import dubh.apps.newsagent.nntp.NNTPServerException;


/**
 * User interface for composing messages to post.
 * <LI>0.1 [03/04/98]: Initial Revision.
 * <LI>0.2 [04/04/98]: Changed to use resStrings.
 * <LI>0.3 [05/04/98]: Added actions for popup menu. Added a toolbar. Converted
 *     to a JFrame
 * <LI>0.4 [07/04/98]: Added reply capability
 * <LI>0.5 [20/04/98]: Checked for References: header before assuming one
 *     exists (woops).
 * <LI>0.6 [09/05/98]: Fixed button order
 * <LI>0.7 [06/06/98]: Added dubh utils import for StringUtils
 * <LI>0.8 [08/06/98]: Added Send Agent support. Fixed bug where dialogue was
 *     dismissed when an error occurred sending the message.
 * <LI>0.9 [10/06/98]: Added support for all the new properties introduced in
 *    Version 1.02 (quoted text prefix, header and selected text quoting)
 * <LI>0.10 [11/06/98]: Added ComposerHeaderDisplay and removed old
 *      implementation.
 * <LI>0.11 [13/06/98]: Having tested NNTPComposerHeaderDisplay, I've removed
 *      the old implementation from this class.
 * <LI>0.12 [30/06/98]: Change to toolbar / popup menu handling - now using
 *      Dubh Utils JMenuBarResource.
 * <LI>0.13 [02/07/98]: This is the first version of MessageComposer to support
 *      The IUpdateableClass interface.
 * <LI>0.14 [09/11/98]: Gave the Post button an action listener again (whoops)
 @author Brian Duff
 @version 0.14 [09/11/98]
 */
public class MessageComposer extends JFrame implements IUpdateableClass {

  public int getMajorClassVersion() { return 0; }
  public int getMinorClassVersion() { return 14; }

  private static final String DEFAULT_INCLUDE = "In message {message-id}, {x-na-realname} wrote:";
  private static final String DEFAULT_PREFIX = "> ";

  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel panComposition = new JPanel();
  private NNTPComposerHeaderDisplay headers;
  private JTextArea taEditor = new JTextArea();
  private JScrollPane scrollEditor = new JScrollPane(taEditor);

  private JPopupMenu popupCommands;
  private JToolBar toolbarCommands;

  private NNTPServer m_server;
  private Newsgroup m_newsgroup;
  private MessageHeader m_replyto;
  private JPanel panButtons = new JPanel();
  private FlowLayout flowLayout1 = new FlowLayout(FlowLayout.RIGHT);
  private JButton cmdPost = new JButton();
  private JButton cmdCancel = new JButton();
  private JMenuBarResource m_menus;




  /**
   * Construct a message composition dialog for an original posting.
   */
  public MessageComposer(NNTPServer s, Newsgroup g) {
     headers = new NNTPComposerHeaderDisplay(this);
     m_server = s;
     m_newsgroup = g;
     m_replyto = null;
     initMenus();
     taEditor.addMouseListener(new PopupMouseAdapter());
     try {
        jbInit();
     } catch (Exception e) {
        e.printStackTrace();
     }
     addNewsgroup(g);
  }

  protected void initMenus() {
     /*
      * Create a new JMenuBarResource and get the toolbar and popup menu from it
      */
     m_menus = new JMenuBarResource("Menus", "mbComposer", this);
     popupCommands = m_menus.getPopupMenu("popupComposer");
     toolbarCommands = m_menus.getToolBar("popupComposer");
     toolbarCommands.setFloatable(false);
  }

  /**
   * Construct a message composition dialog for a reply to a given message.
   */
  public MessageComposer(NNTPServer s, Newsgroup g, MessageHeader replyto,
   MessageBody replybody) {
     this(s, g, replyto, replybody.toString());
   }

  /**
   * Construct a message composition dialogue for a reply to a given message,
   * only quoting the specified String in the reply.  You should only call this
   * constructor  if
   *  the user preference newsagent.send.IncludeBehaviour is set to "selected".
   @param s the server to post to
   @param g the newsgroup to post to
   @param replyto the message header that this message is a reply to
   @param quote text to be quoted in the reply.
   */
  public MessageComposer(NNTPServer s, Newsgroup g, MessageHeader replyto,
     String quote) {
        this(s, g);
        m_replyto = replyto;
        setMessageQuoted(quote);
        try {
           setSubjectReply(replyto.getFieldValue("Subject"));
        } catch (IllegalArgumentException e) {}
  }

  /**
   * Construct a message composition dialogue for a reply to a given message,
   * with no quoted text.
   */
  public MessageComposer(NNTPServer s, Newsgroup g, MessageHeader replyto) {
     this(s, g);
     m_replyto = replyto;
          try {
           setSubjectReply(replyto.getFieldValue("Subject"));
        } catch (IllegalArgumentException e) {}
  }

  /**
   * Sets the message text to a quoted string. Uses a user defined prepend
   * string for each line, and a description at the top.
   */
  private void setMessageQuoted(String message) {
     String prefix;

     if (message != null) {
        if (message.trim().length() > 0) {
           insertQuoteHeader(GlobalState.getPreference("newsagent.send.IncludeHeading", DEFAULT_INCLUDE));

           prefix = GlobalState.getPreference("newsagent.send.IncludePrefix", DEFAULT_PREFIX);
           taEditor.append("\n"+StringUtils.prefixString(message, prefix));
        }
     }
  }

  /**
   * Inserts the "In message blah..." text at the top of quotes. The m_replyto
   * field is used to fill in the {...} text in the quoteTemplate, and the
   * result is placed into the editor. The special field x-na-realname is
   * replaced with the real name of the original sender.
   */
  private void insertQuoteHeader(String quoteTemplate) {
     String[] tokens;
     boolean isHeader;

     tokens = StringUtils.getTokens(quoteTemplate, "{}");
     /*
      * Now tokens holds an array that alternates message headers with ordinary
      * text. If the string starts with a '{', the first token is a header,
      * otherwise it is normal text.
      */
      isHeader = (quoteTemplate.charAt(0) == '{');
      for (int i=0; i<tokens.length; i++) {
        if (isHeader) {
           try {
              if (tokens[i].equalsIgnoreCase("x-na-realname"))
                 taEditor.append(m_replyto.getRealName());
              else
                 taEditor.append(m_replyto.getFieldValue(tokens[i]));
           } catch (IllegalArgumentException e) {
              Debug.println("IllegalArgumentException "+e);
              taEditor.append("(unknown header: "+tokens[i]+")");
           }
        } else
           taEditor.append(tokens[i]);
        // Alternate
        isHeader = !isHeader;
      }
     

  }

  /**
   * Sets the Subject text field to Re: ...
   @param s a subject to prepend Re: to. If the subject already starts with Re:,
     it is left unchanged.
   */
  private void setSubjectReply(String s) {
     String newSubject;

     try {
        if (s.substring(0,3).toLowerCase().equals("re:"))
           newSubject = s;
        else
           newSubject = "Re: "+s;
     } catch (StringIndexOutOfBoundsException e) {
        newSubject = "Re: "+s;
     }
     headers.setHeaderValue("Subject", newSubject);
  }

  /**
   * Adds a newsgroup to the Newsgroup: header.
   */
  private void addNewsgroup(Newsgroup n) {
     String existing = headers.getHeaderValue("Newsgroups");
     if (existing.trim().length() == 0) {
        headers.setHeaderValue("Newsgroups", n.getName());
     } else {
        headers.setHeaderValue("Newsgroups", existing + ", "+n.getName());
     }
  }

  /**
   * Show the dialog, check id too.
   @deprecated
   */
  public void show() {
   super.show();
   checkIdentity();
  }

  /**
   * Checks the headers prior to posting a message
   */
  private boolean checkHeaders(MessageHeader h) {
     // Check there is a Subject header
     if (h.getFieldValue("Subject").trim().length() == 0) {
        ErrorReporter.error("MessageComposer.NeedSubject");
        return false;
     }
     // Check there is a newsgroups header
     if (h.getFieldValue("Newsgroups").trim().length() == 0) {
        ErrorReporter.error("MessageComposer.NeedNewsgroups");
        return false;
     }
     return true;
  }

  /**
   * Carbon copies the message to a file, and removes the Fcc header field
   @param header the message header
   @param body the message body
   */
  private void fccMessage(MessageHeader header, MessageBody body) {
     String fccFileName = header.getFieldValue("Fcc");
     File   fccFile     = new File(fccFileName);
     PrintWriter fccWriter;
     header.removeField("Fcc");    // NOTE: This might not work ("" is not a ref)
     try {
        if (fccFile.exists()) {
           // open file for appending
           fccWriter = new PrintWriter(new FileWriter(fccFileName, true));
        } else {
           fccWriter = new PrintWriter(new FileWriter(fccFile));
        }
        fccWriter.println(header.toString());
        fccWriter.println("---");
        fccWriter.println(body.toString());
        fccWriter.flush();
        fccWriter.close();
     } catch (IOException e) {
        Debug.println("IO Exception while Fcc'ing message: "+e);
        ErrorReporter.error("MessageComposer.FccIOError");

     }
  }

  /**
   * Posts the message.
   @return true if the message was posted successfully, false otherwise.
   */
  private boolean postMessage() {


     MessageBody b = new MessageBody(taEditor.getText());
     MessageHeader h = headers.getMessageHeader();
     if (!h.hasField("From"))
        h.setField("From", GlobalState.getPreference("newsagent.identity.email") +
           " (" + GlobalState.getPreference("newsagent.identity.realname") + ")");
     h.setField("Organization", GlobalState.getPreference("newsagent.identity.organisation"));
     if (GlobalState.getBoolPreference("newsagent.send.AddNewsAgentHeaders", true)) {
        h.setField("X-Mailer", GlobalState.xmailer);
        h.setField("X-Mailer-URL", GlobalState.appURL);
     }
     if (m_replyto!=null) {
        if (m_replyto.hasField("References"))
           h.setField("References", m_replyto.getFieldValue("References")+" "+
                              m_replyto.getFieldValue("Message-Id"));
        else
           h.setField("References", m_replyto.getFieldValue("Message-Id"));
     }
     if (!checkHeaders(h)) {
        return false;
     }
     GlobalState.getStorageManager().connectIfNeeded(m_server);
     try {
        if (GlobalState.getAgentManager().callSendAgents(h, b)) {
           // Fcc if the header exists
           if (h.hasField("Fcc"))
              fccMessage(h, b);
           m_server.postArticle(h, b);
           return true;
        } else
           return false;
     } catch (NNTPServerException nntp) {
        GlobalState.getStorageManager().nntpException(nntp,
        GlobalState.getResString("Action.Posting"), m_server);
        return false;
     } catch (Exception e) {
        ErrorReporter.debug("Unhandled Exception when posting:"+e);
        return false;
     }
  }

  /**
   * Checks whether the user has set up their identity options. If not, ask
   * if they want to view the identity options dialog box now.
   @return true if the users identity was already set. If false is returned,
     any pending message should *not* be sent yet, because the users identity
     was not set, or is still being edited.
   */
  private boolean checkIdentity() {

   String em = GlobalState.getPreference("newsagent.identity.email", "");
   if (em.equals("")) {
     if (ErrorReporter.yesNo("MessageComposer.NoIdentity", new String[] {GlobalState.appName})) {
       // user wants to edit their identity options.
        OptionsFrame opt = new OptionsFrame(OptionsFrame.TAB_IDENTITY);
        opt.showAtStoredLocation("preferences");
     }
     // whatever the case, don't send the message until the user does something
     return false;
   }
   // if we get here, the identity was ok.
   return true;

  }


  private void jbInit() throws Exception{
  //   headers = new NNTPComposerHeaderDisplay(this);
     this.setTitle(GlobalState.getResString("MessageComposer.MessageComposer"));

     panComposition.setLayout(new GridBagLayout());
     this.getContentPane().setLayout(borderLayout1);
     this.getContentPane().add(panComposition, BorderLayout.CENTER);
     panComposition.add(
        headers,
        new GridBagConstraints2(
           0, 0, 1, 1,
           1.0, 0.0,
           GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
           new Insets(5, 0, 5, 0),
           0,0
        )
     );
     panComposition.add(
        scrollEditor,
        new GridBagConstraints2(
           0, 1, 1, 1,
           0.0, 1.0,
           GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
           new Insets(5, 5, 5, 5),
           0, 200
        )
     );
    this.getContentPane().add(panButtons, BorderLayout.SOUTH);
    this.getContentPane().add(toolbarCommands, BorderLayout.NORTH);

    panButtons.add(cmdPost, null);
    panButtons.add(cmdCancel, null);
    cmdPost.addActionListener(getAction("compSend"));
//    GlobalState.getRes().initButton(cmdPost, "MessageComposer.Post");
    cmdPost.setText("+Post+");    // NLS
     
     cmdCancel.setText(GlobalState.getResString("GeneralCancel"));
    cmdCancel.addActionListener(getAction("compCancel"));
    panButtons.setLayout(flowLayout1);
    taEditor.setFont(new Font("Monospaced", Font.PLAIN, 12));
    taEditor.setLineWrap(true);
    taEditor.setColumns(78);
    taEditor.setWrapStyleWord(true);
    taEditor.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
  }

  void treeMouseEvent(MouseEvent e) {
     if (e.isPopupTrigger()) {
         doPopupMenu(e.getX(), e.getY());
     }
  }

  private void doPopupMenu(int x, int y) {
     /* Adjust the x and y co-ordinates depending on the position of the
     * ScrollPane. It's annoying that we have to do this. */
     int mx = x - scrollEditor.getHorizontalScrollBar().getValue();
     int my = y - scrollEditor.getVerticalScrollBar().getValue();
     popupCommands.show(this, mx, my);
  }

// Private methods implementing actions

 /**
  * Utility for retrieving a named action from the action table.
  */
 private Action getAction(String actionName) {
   return m_menus.getAction(actionName);
 }

 public void compCut() {
   taEditor.cut();
 }

 public void compCopy() {
   taEditor.copy();
 }

 public void compPaste() {
   taEditor.paste();
 }

 public void compInsertSig() {
     Debug.println("compInsertSig: Not yet implemented");
 }

 public void compInsertFile() {
     Debug.println("compInsertFile: Not yet implemented");
 }

 /**
  * The user decided to cancel message composition.
  */
 public void compCancel() {
    setVisible(false);
    headers.unregister();
    dispose();
 }

 /**
  * User wants to post the message.
  */
 public void compSend() {
    if (checkIdentity()) {
     if (postMessage()) {
        // Only remove the dialogue if the post was successful.
        setVisible(false);
        headers.unregister();
        dispose();

     }
    }
 }


 class PopupMouseAdapter implements java.awt.event.MouseListener {

  public void mouseClicked(MouseEvent e) {
     treeMouseEvent(e);
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {
     treeMouseEvent(e);
  }

  public void mouseReleased(MouseEvent e) {
     treeMouseEvent(e);
  }

 }


}