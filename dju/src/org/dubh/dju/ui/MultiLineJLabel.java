// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MultiLineJLabel.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;


/**
 * A MultiLineJLabel displays a string containing \n characters. Partially
 * lifted from pg 180-183 of Java In a Nutshell 2nd Edition.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [10/05/98]: Initial Revision
 * </UL>
 @author Brian Duff
 @version 0.1 [09/05/98]
 */
public class MultiLineJLabel extends Component {
  protected String label;
  protected int margin_width;
  protected int margin_height;
  protected int alignment;
  public static final int LEFT = 0, CENTER = 1, RIGHT = 2;

  protected int num_lines;
  protected String[] lines;
  protected int[] line_widths;
  protected int max_width;
  protected int line_height;
  protected int line_ascent;
  protected boolean measured = false;

  public MultiLineJLabel(String label, int margin_width, int margin_height,
     int alignment) {

     this.label = label;
     this.margin_width = margin_width;
     this.margin_height = margin_height;
     this.alignment = alignment;
     JLabel temp = new JLabel("Hello");
     super.setFont(temp.getFont());
     super.setForeground(temp.getForeground());
    // super.setFont(new Font("Dialog", Font.PLAIN, 10));
    // super.setForeground(Color.black);
     //this.setOpaque(true);
     //this.setDoubleBuffered(false);
     newLabel();


  }

  public MultiLineJLabel(String label) {
     this(label, 0, 0, LEFT);
  }

  public MultiLineJLabel() {
     this("");
  }

  /**
   * Set the label to use
   */
  public void setText(String text) {
     this.label = text;
     newLabel();
     measured = false;
     repaint();
  }

  /**
   * Set the font to use. The default font is that used by a JLabel.
   @param f a font
   */
  public void setFont(Font f) {
     super.setFont(f);
     measured = false;
     repaint();
  }

  /**
   * Set the foreground colour to use. The default is that used by a JLabel.
   @param c a Color
   */
  public void setColor(Color c) {
     super.setForeground(c);
     repaint();
  }

  /**
   * Set the alignment
   @param a one of the alignment constants
   */
  public void setAlignment(int a) { alignment = a; repaint(); }

  /**
   * Set the margin width
   */
  public void setMarginWidth(int a) { margin_width = a; repaint(); }

  /**
   * Set the margin height
   */
  public void setMarginHeight(int a) { margin_height = a; repaint(); }

  /**
   * Get the text
   */
  public String getText() { return label; }

  /**
   * Get the alignment
   */
  public int getAlignment() { return alignment; }

  /**
   * Get the margin width
   */
  public int getMarginWidth() { return margin_width; }

  /**
   * Get the margin height
   */
  public int getMarginHeight() { return margin_height; }

  public Dimension getPreferredSize() {
     if (!measured) measure();
     return new Dimension(max_width + 2*margin_width,
              num_lines * line_height + 2*margin_height);
  }

  public Dimension getMinimumSize() { return getPreferredSize(); }

  public void paint(Graphics g) {
     int x, y;

     Dimension size = this.getSize();

     if (!measured) measure();
     y = line_ascent + (size.height - num_lines * line_height)/2;
     for (int i=0; i< num_lines; i++, y += line_height) {
        switch (alignment) {
           default:
           case LEFT:     x = margin_width; break;
           case CENTER:   x = (size.width - line_widths[i])/2; break;
           case RIGHT:    x = size.width - margin_width - line_widths[i]; break;
        }
        g.setFont(super.getFont());
        g.setColor(super.getForeground());
        g.drawString(lines[i], x, y);
     }
  }

  protected synchronized void newLabel() {
     StringTokenizer t = new StringTokenizer(label, "\n");
     num_lines = t.countTokens();
     lines = new String[num_lines];
     line_widths = new int[num_lines];
     for (int i=0; i<num_lines; i++) lines[i] = t.nextToken();
  }

  protected synchronized void measure() {
     //LineMetrics fm = getFont().getLineMetrics

     //
     // TODO: FontMetrics are deprecated as of Java 2. Can't figure
     // out how to get font metrics (lots of added complication, from
     // the look of things... tsk)
     //

     FontMetrics fm = this.getToolkit().getFontMetrics(this.getFont());
     line_height = fm.getHeight();
     line_ascent = fm.getAscent();
     max_width = 0;
     for (int i=0; i < num_lines; i++) {
        line_widths[i] = fm.stringWidth(lines[i]);
        if (line_widths[i] > max_width) max_width = line_widths[i];
     }
     measured = true;
  }

}