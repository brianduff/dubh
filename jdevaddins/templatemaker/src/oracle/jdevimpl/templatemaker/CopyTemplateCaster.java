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

import java.io.IOException;
import java.net.URL;

import oracle.ide.addin.Context;
import oracle.ide.net.URLFileSystem;

/**
 * Simple TemplateCaster implementation that simply copies the template to
 * the output URL
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
class CopyTemplateCaster implements TemplateCaster
{

// ----------------------------------------------------------------------------
// TemplateCaster interface
// ----------------------------------------------------------------------------


  public void castTemplate( final Context context, 
      final URL template, final URL outputURL )
    throws TemplateCastFailedException
  {
    try
    {
      URLFileSystem.copy( template, outputURL );    
    }
    catch ( IOException ioe )
    {
      throw new TemplateCastFailedException(
        "An i/o exception occurred while copying the template", 
        ioe
      );
    }
  }
}