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
 * The Original Code is Python Addin for Oracle9i JDeveloper.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.jdev.language.python;

import oracle.javatools.editor.language.AbstractLanguageSupport;
import oracle.javatools.editor.language.BraceProvider;
import oracle.javatools.editor.language.DocumentRenderer;
import oracle.javatools.editor.language.WordLocator;
import org.dubh.jdev.language.python.PythonDocumentRenderer;

/**
 * Provides support for editing python text files
 * 
 * @author Brian.Duff@oracle.com
 */
public class PythonLanguageSupport extends AbstractLanguageSupport
{

// ----------------------------------------------------------------------------
// AbstractLanguageSupport implementation
// ----------------------------------------------------------------------------

  protected DocumentRenderer createDocumentRenderer()
  {
    return new PythonDocumentRenderer( this );
  }

}