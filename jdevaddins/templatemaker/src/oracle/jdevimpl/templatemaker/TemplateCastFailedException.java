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

/**
 * Exception class thrown by TemplateCaster.castTemplate() when the cast fails
 *
 * @author Brian.Duff@oracle.com
 * @see oracle.jdevimple.templatemaker.TemplateCaster
 */
public class TemplateCastFailedException extends Exception
{
  private final Throwable m_baseCause;

  public TemplateCastFailedException( final String message )
  {
    super( message );
    m_baseCause = null;
  }

  public TemplateCastFailedException( final String message, 
    final Throwable baseCause )
  {
    super( message );
    m_baseCause = baseCause;
  }

  public Throwable getBaseCause()
  {
    return m_baseCause;
  }
}