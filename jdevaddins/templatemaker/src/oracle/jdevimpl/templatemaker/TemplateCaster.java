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
 * The Original Code is TemplateMaker addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */


package oracle.jdevimpl.templatemaker;

import java.net.URL;

import oracle.ide.addin.Context;

/**
 * TemplateCaster provides a pluggable mechanism for generating files
 * from templates in a way that does not depend on the content of the input
 * or output file or the mechanism used to convert one to the other.
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public interface TemplateCaster 
{
  /**
   * Cast the specified template into the specified output
   *
   * @param template the URL of the template to use
   * @param outputURL the output URL
   */
  void castTemplate( final Context context, 
      final URL template, final URL outputURL )
    throws TemplateCastFailedException;
}