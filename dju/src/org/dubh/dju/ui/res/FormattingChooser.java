// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormattingChooser.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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


package org.dubh.dju.ui.res;

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