// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormattingChooser.java,v 1.4 1999-11-11 21:24:36 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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

package org.javalobby.dju.ui.res;

import java.util.ListResourceBundle;

/**
 * <p>
 * NLS resources for FormattingChooser
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.0) [12/Dec/1998]
 */
public class FormattingChooser extends ListResourceBundle
{
   public static final Object[][] m_contents = 
   {
      {"FormattingChooser.panFont.lblFont.text", "Font"},
      {"FormattingChooser.panFont.lblFont.displayedMnemonic", "F"},
      {"FormattingChooser.panFont.lblStyle.text", "Font Style"},
      {"FormattingChooser.panFont.lblStyle.displayedMnemonic", "y"},
      {"FormattingChooser.panFont.lblSize.text", "Size"},
      {"FormattingChooser.panFont.lblSize.displayedMnemonic", "S"},    
      {"FormattingChooser.panFont.lblColor.text", "Colour"},
      {"FormattingChooser.panFont.lblColor.displayedMnemonic", "C"},
      {"FormattingChooser.panSample.borderText", "Preview"},
      {"FormattingChooser.panSample.lblSample.text", "Sample Text"}
   };
   
   public final Object[][] getContents()
   {
      return m_contents;
   }
}