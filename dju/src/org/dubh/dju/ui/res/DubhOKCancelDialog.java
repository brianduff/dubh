// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: DubhOKCancelDialog.java,v 1.5 2001-02-11 02:52:12 briand Exp $
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
 * NLS resources for DubhOKCancelDialog
 * </p>
 * @author Brian Duff
 */
public class DubhOKCancelDialog extends ListResourceBundle
{
   public static final Object[][] m_contents =
   {
      {"OKCancelPanel.cmdOK.text", "OK"},
      {"OKCancelPanel.cmdOK.toolTipText", "Click to dismiss this dialog and accept changes"},
      {"OKCancelPanel.cmdCancel.text", "Cancel"},
      {"OKCancelPanel.cmdCancel.toolTipText", "Click to dismiss this dialog and discard changes"}
   };

   public final Object[][] getContents()
   {
      return m_contents;
   }
}