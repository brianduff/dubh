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
package dubh.apps.newsagent.mailviewer.content;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * Frame to display a single component.
 * @author Brian Duff
 * @version $Id: ComponentFrame.java,v 1.1 1999-10-17 17:03:27 briand Exp $
 */
public class ComponentFrame extends JFrame 
{
    
   /**
   * creates the frame
   * @param what   the component to display
   */
   public ComponentFrame(Component what) 
   {
	   this(what, "Component Frame");
   }

   /**
   * creates the frame with the given name
   * @param what   the component to display
   * @param name   the name of the Frame
   */
   public ComponentFrame(Component what, String name) 
   {
      super(name);

	   // make sure that we close and dispose ourselves when needed
	   setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	   // default size of the frame
	   setSize(700,600);

	   // we want to display just the component in the entire frame
	   if (what != null) 
      {
	      getContentPane().add("Center", what);
	   }
   }
}