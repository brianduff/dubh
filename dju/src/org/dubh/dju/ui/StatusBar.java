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

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

/**
 * Displays a status bar containing a text message and optionally an icon and
 * progress bar. By default the icon and progress bar aren't visible.
 *
 * Version History: <UL>
 * <LI>0.1 [02/04/98]: Initial Revision
 * <LI>0.2 [07/04/98]: Added support for a progress meter
 * <LI>0.3 [20/04/98]: Gave status bar some extra padding
 * <LI>0.4 [08/06/98]: Moved to Dubh Utils (from NewsAgent)
 * <LI>0.5 [17/06/98]: Added get-methods so the class can be Java Bean-ed
 *</UL>
 @author Brian Duff
 @version 0.5 [17/06/98]
 */
public class StatusBar extends JPanel {
  JLabel labStatus = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JProgressBar progressBar = new JProgressBar();

  public StatusBar() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    this.setSize(new Dimension(564, 23));
    labStatus.setText("Status Bar");
    this.setLayout(gridBagLayout1);
    this.add(labStatus, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 5, 2), 0, 0));
    this.add(progressBar, new GridBagConstraints2(1, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
   progressBar.setVisible(false);
  }

  /**
   * Sets the text to be used in the status bar.
   */
  public void setText(String text) {
   labStatus.setText(text);
  }

  /**
   * Gets the text
   */
  public String getText() {
     return labStatus.getText();
  }

  /**
   * Sets the icon to be used in the status bar. This is displayed
   * to the left of the text by default.
   */
  public void setIcon(Icon icon) {
   labStatus.setIcon(icon);
  }

  /**
   * Gets the icon
   */
  public Icon getIcon() {
     return labStatus.getIcon();
  }

  /**
   * Clears the text in the status bar
   */
  public void clearText() {
   setText("");
  }

  /**
   * Set whether the progress bar on the right of the status bar is
   * visible.
   @param visible true to show the progress bar
   */
  public void setProgressVisible(boolean visible) {
   progressBar.setVisible(visible);
   invalidate();
   validate();
   //pack();
   repaint();
  }

  /**
   * Get whether the progress bar is currently visible.
   @return true if the progress bar is currently visible.
   */
  public boolean isProgressVisible() {
   return progressBar.isVisible();
  }

  /**
   * Sets the value of the progress bar
   @param value an integer describing the current progress of the operation
   */
  public void setProgress(int value) {
   progressBar.setValue(value);
  }

  /**
   * Gets the value of the progress bar
   @return an integer value
   */
  public int getProgress() {
   return progressBar.getValue();
  }

  /**
   * Set the maximum value of the progress bar
   */
  public void setMaximumProgress(int max) {
   progressBar.setMaximum(max);
  }

  public int getMaximumProgress() {
     return progressBar.getMaximum();
  }

  /**
   * Set the minimum value of the progress bar
   */
  public void setMinimumProgress(int min) {
   progressBar.setMinimum(min);
  }

  public int getMinimumProgress() {
     return progressBar.getMinimum();
  }

  /**
   * Gets the progress bar component
   */
  public JProgressBar getProgressBar() {
     return progressBar;
  }

  /**
   * Get the minimum size of this component
   */
  public Dimension getMinimumSize() {
   return getPreferredSize();
  }

  /**
   * Get the preferred size of this component
   */
  public Dimension getPreferredSize() {
   return new Dimension(getSize().width, 35);
  }

  

}