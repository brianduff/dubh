// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JTextFieldFixedHeight.java,v 1.1 1999-06-01 00:17:34 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
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
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.ui;

import java.awt.*;
import javax.swing.*;

/**
 * A JTextField that has a fixed height and doesn't expand veritcally.
 * This makes it usable with vertical BoxLayouts.
 *
 * @author Brian Duff
 * @version $Id: JTextFieldFixedHeight.java,v 1.1 1999-06-01 00:17:34 briand Exp $
 */
public class JTextFieldFixedHeight extends JTextField 
{
   public JTextFieldFixedHeight()
   {
      super();
      setMaximumSize(new Dimension(getMaximumSize().width, getPreferredSize().height));
   }
}

//
// $Log: not supported by cvs2svn $
//