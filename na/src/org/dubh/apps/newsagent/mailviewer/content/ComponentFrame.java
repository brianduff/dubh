// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: ComponentFrame.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.mailviewer.content;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * Frame to display a single component.
 * @author Brian Duff
 * @version $Id: ComponentFrame.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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