/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Oracle9i JDeveloper release 9.0.3
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.toolmanager;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * A component that draws a raised 3d effect horizontal bar. This is used often
 * in Oracle Look and Feel compliant dialogs to clarify component groupings
 *
 * @author Brian Duff
 */
public final class BarSeparator extends JComponent
{
  private final static Dimension PREFERRED_SIZE = new Dimension( 0, 3 );

  public void paintComponent( final Graphics g )
  {
    final int width = getSize().width;
    g.setColor(
      UIManager.getDefaults().getColor( "controlHighlight" )    // NOTRANS
    );
    g.drawLine( 0, 0, width-1, 0 );
    g.setColor(
      UIManager.getDefaults().getColor( "controlLtHighlight" )  // NOTRANS
    );
    g.drawLine( 0, 1, width-1, 1 );
    g.setColor(
      UIManager.getDefaults().getColor( "controlShadow" )       // NOTRANS
    );
    g.drawLine( 0, 2, width-1, 2 );

  }

  public Dimension getPreferredSize()
  {
    return PREFERRED_SIZE;
  }

  public Dimension getMinimumSize()
  {
    return PREFERRED_SIZE;
  }

}