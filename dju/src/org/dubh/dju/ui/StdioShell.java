// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StdioShell.java,v 1.3 1999-03-22 23:37:19 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//   No licensing information available; this was demonstration code
// ---------------------------------------------------------------------------
//   Original Author: Todd Courtois
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.ui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
* A class for displaying a stdin and stdout.
* Useful when your platform doesn't have stdin/stdout,
* or you want to launch multiple i/o windows.
* Taken from Java Networking & Communications 1.1
@author Todd Courtois
@version unknown
*/
public class StdioShell extends JFrame implements UserTypingTarget
{
public boolean fDebugOn = true; //toggles debugging
public boolean fEchoOn = true; //toggles local echo of user input

protected QuickTerm fDisplayArea;
protected CmdTextArea fInputField;
protected BorderLayout fUberLayoutMgr;
protected JScrollPane fScrollPane;

protected PrintWriter fOutputStream;
protected PipedInputStream fResultStream;

/**
* Constructor
* @param title The title to be displayed on the window
* @param srcStream An input stream from which we can pull data to display
* @param maxWidth The maximum width of the window
* @param maxHeight max height of the window
*/
public StdioShell(String title, InputStream srcStream,
                    int maxWidth, int maxHeight)
{

   try {
      PipedOutputStream tempOutStream = new PipedOutputStream();
      fOutputStream = new PrintWriter(tempOutStream);
      fResultStream = new PipedInputStream(tempOutStream);
   }
   catch (IOException ioEx) {
      System.err.println(
          "StdioShell stream allocation failed: " + ioEx);
      return;
   }

      setTitle(title);

      fUberLayoutMgr = new BorderLayout(0,0);
      this.getContentPane().setLayout(fUberLayoutMgr);

   fDisplayArea = new QuickTerm(24,80, srcStream);
     fDisplayArea.setBorder(null);
     fScrollPane = new JScrollPane(fDisplayArea);
      getContentPane().add("Center",fScrollPane);

      fInputField = new CmdTextArea(this, 2, 80);
      getContentPane().add("South",fInputField);

}//StdioShell

/**
* @param srcStream The input stream to use for display
*/
public void setDisplaySourceStream(InputStream srcStream)
{
   fDisplayArea.setInputStream(srcStream);

}//setDisplaySourceStream

/**
* Provide an InputStream that contains the user input...
*/
public InputStream getInputStream()
{
   return (InputStream)fResultStream;
}//getInputStream

/**
* This accepts a string from the user input field
* @param cmdStr The user input string.
*/
public void acceptUserCommand(String cmdStr)
{

    if (fDebugOn) System.out.println(cmdStr);

   if (fOutputStream != null) {
      fOutputStream.println(cmdStr);
      if (fEchoOn) fDisplayArea.appendText("--->" + cmdStr);
   }
}//acceptUserCommand



/**
* A class that allows multiline input with carriage-return termination
* as well as some convenient command keys.
*/
  class CmdTextArea extends JTextArea implements KeyListener
  {
  //the receiver for all the user's typing
  protected UserTypingTarget fTargetShell;

  /*
  * @param targetShell The parent user interface.
  * @param rows The number of text rows to provide
  * @param cols The number of text columns to provide
  */
  public CmdTextArea( UserTypingTarget targetShell,
                    int rows, int cols)
  {
      super(rows, cols);//tell TextArea our size
      fTargetShell = targetShell;
     addKeyListener(this);
     setWrapStyleWord(true);
     setLineWrap(true);
  }//CmdTextArea


  public void keyReleased(KeyEvent e) {;}

  public void keyPressed(KeyEvent e) {
     if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        fTargetShell.acceptUserCommand(this.getText());
        //clear out the input field...

        this.setText("");
     }
  }

  public void keyTyped(KeyEvent e) { ; }


  }/* class CmdTextArea */


         public static void main(String args[]) {
 try {


         //this gives us a way to pull data from the console



         FileInputStream fileSrcStream = new FileInputStream("C:\\input.dat");





         //need to hand an input stream and an output stream to StdioShell

         StdioShell fStdioShell = new StdioShell("Hello1 User!",fileSrcStream, 80, 25);



        fStdioShell.fEchoOn = true; //so the user text will display in the console

         fStdioShell.setSize(new Dimension(430, 400));

        fStdioShell.pack();

         fStdioShell.setVisible(true);





      }

      catch (IOException e) {

         System.err.println("blew up: " + e);



      }





   }

}//class StdioShell

/**
* An interface used to pass back user input
*/
interface UserTypingTarget
{
    public void acceptUserCommand(String cmdString);

}