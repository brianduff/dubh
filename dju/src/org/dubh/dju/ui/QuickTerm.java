// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: QuickTerm.java,v 1.3 1999-03-22 23:37:18 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//   No licensing information available; this was demonstration code
// ---------------------------------------------------------------------------
//   Original Author: Todd Cortois
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.ui;

import java.awt.*;
import java.io.*;
import javax.swing.*;


/**
* A class that displays text input in a scrolling view.
* Taken from Java Networking & Communications 1.1
@author Todd Courtois
@version 0.1
*
* 0.1: BD: Changed to use BufferedReader for 1.1 compatibility.
*/
public class QuickTerm extends JTextArea implements Runnable
{

public boolean fDebugOn = false;

//the maximum number of screens of characters to buffer
public int fMaxNumScreens = 10;
//the size of the scrollback buffer
public int fMaxCharCount;
//the number of characters currently buffered
protected int fTotalCharCount = 0;
//the number of columns of characters to display
protected int fNumCols = 80;

//the stream from which to read input data to be displayed
protected BufferedReader fInputStream;
//the thread that drives our Runnable interface
protected Thread fTicklerThread;
//flag that tells us to stop displaying
protected boolean fContinueDisplay = true;


/*
* @param rows The number of rows to display
* @param cols The number of columns to display
* @param srcStream The InputStream from which to read
*   the text data to be displayed.
*/
public QuickTerm(int rows, int cols, InputStream srcStream)
{
    super("",rows,cols);
    fNumCols = cols;
    //set up a monospace font
    super.setFont(new Font("Courier", Font.PLAIN, 12));

    //calculate the size of our character buffer
    fMaxCharCount = rows * (cols * fMaxNumScreens);

   setInputStream(srcStream);

    //turn off editing so the user can't type over the display
    this.setEditable(false);

    //kickstart our thread
   fTicklerThread = new Thread(this);
   fTicklerThread.start();
}//QuickTerm

/**
* set the input stream for the terminal to read from
*/
public void setInputStream(InputStream srcStream)
{
   if (srcStream != null) {
      try {
         fInputStream = new BufferedReader(new InputStreamReader(srcStream));
      }
      catch (Exception constructEx) {
         System.err.println("constructEx: " + constructEx);
         return;
      }
   }
   else fInputStream = null;

}//setInputStream

/*
* Take a string and blast it onto the display
* @param str The String to append to the display.
*/
public synchronized void appendText(String str)
{
    if (fDebugOn) System.out.println("appendText: " + str);

    String newStr = "";
    int newStrLen = str.length();

    //are we appending more than one line?
    int numLines = (newStrLen / fNumCols) +
        (((newStrLen % fNumCols) > 0) ? 1 : 0);
    if (numLines > 1) {
        //perform necessary wrapping
        int offset = 0;
        String tempStr;
        for (int idx = 0; idx < numLines; idx++) {
            int newOffset = fNumCols*(idx + 1) - 1;
            if (newOffset > (newStrLen -1)) newOffset = (newStrLen -1);
            tempStr = str.substring(offset,newOffset);
            newStr += "\n" + tempStr;
            offset = newOffset;
        }
        newStrLen = newStr.length();
    }
    else {
        //if just one line, then prepend "\n" and display
        newStr = "\n" + str;
        newStrLen += 1;
    }

    //if we've exceeded our scrollback buffer size...
    if ((newStrLen + fTotalCharCount) >  fMaxCharCount) {
        //remove as many characters from the top as
        //we're about to append to the bottom
        super.replaceRange("",0,newStrLen);
        fTotalCharCount -= newStrLen;
    }
    //now use TextArea's method to place text in the display
    super.append(newStr);
    fTotalCharCount += newStrLen;

} //append text


/**
* Method that continuously updates the screen display
*/
public void run()
{
   while (fContinueDisplay) {

      try {
         if (fInputStream != null) {
             String tmpStr = null;
             //read all available lines
             while (
                 //this blocks waiting for a whole line
                 (tmpStr = fInputStream.readLine()) != null) {
               this.appendText(tmpStr);
            }
         }
         //after reading all we can, give up time to other tasks
         try {fTicklerThread.sleep(50);}
         catch (InterruptedException iEx) {};
      }
      catch (Exception runEx) {
         System.err.println("runEx: " + runEx);
         fContinueDisplay = false;
      }
   }
}//run


} /* class QuickTerm */