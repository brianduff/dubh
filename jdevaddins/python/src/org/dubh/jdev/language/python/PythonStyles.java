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

import oracle.javatools.editor.language.BaseStyle;
import oracle.javatools.editor.language.BuiltInStyles;
import oracle.javatools.editor.language.StyleRegistry;

/**
 * Contains constants and styles for the python language
 * 
 * @author Brian.Duff@oracle.com
 */
public class PythonStyles 
{
  public static final String PYTHON_COMMENT_STYLE = "python-comment-style";
  public static final String PYTHON_KEYWORD_STYLE = "python-keyword-style";
  public static final String PYTHON_STRING_STYLE = "python-string-style";
  public static final String PYTHON_NUMBER_STYLE = "python-number-style";
  public static final String PYTHON_IDENTIFIER_STYLE = "python-identifier-style";


  public static final String[] STYLE_NAMES = new String[] 
  {
    PYTHON_COMMENT_STYLE,
    PYTHON_KEYWORD_STYLE,
    PYTHON_STRING_STYLE,
    PYTHON_NUMBER_STYLE,
    PYTHON_IDENTIFIER_STYLE
  };

  public static BaseStyle pythonPlainStyle;
  public static BaseStyle pythonCommentStyle;
  public static BaseStyle pythonKeywordStyle;
  public static BaseStyle pythonStringStyle;
  public static BaseStyle pythonNumberStyle;
  public static BaseStyle pythonIdentifierStyle;

  
  public PythonStyles( StyleRegistry styleRegistry )
  {
    reloadStyles( styleRegistry );
  }

  public static String[] getStyleNames()
  {
    return STYLE_NAMES;
  }

  public void reloadStyles( StyleRegistry styleRegistry )
  {
    BuiltInStyles builtInStyles = new BuiltInStyles( styleRegistry );

    pythonPlainStyle = builtInStyles.plainStyle;

    pythonCommentStyle = styleRegistry.lookupStyle( PYTHON_COMMENT_STYLE );
    if ( pythonCommentStyle == null )
    {
      pythonCommentStyle = styleRegistry.createStyle(
        PYTHON_COMMENT_STYLE, "Python comments", BuiltInStyles.BUILTIN_COMMENT_STYLE
      );
    }

    pythonKeywordStyle = styleRegistry.lookupStyle( PYTHON_KEYWORD_STYLE );
    if ( pythonKeywordStyle == null )
    {
      pythonKeywordStyle = styleRegistry.createStyle(
        PYTHON_KEYWORD_STYLE, "Python keywords", BuiltInStyles.BUILTIN_KEYWORD_STYLE
      );
    }
    
    pythonStringStyle = styleRegistry.lookupStyle( PYTHON_STRING_STYLE );
    if ( pythonStringStyle == null )
    {
      pythonStringStyle = styleRegistry.createStyle(
        PYTHON_STRING_STYLE, "Python strings", BuiltInStyles.BUILTIN_STRING_STYLE
      );
    }

    pythonNumberStyle = styleRegistry.lookupStyle( PYTHON_NUMBER_STYLE );
    if ( pythonNumberStyle == null )
    {
      pythonNumberStyle = styleRegistry.createStyle(
        PYTHON_NUMBER_STYLE, "Python numeric literals", BuiltInStyles.BUILTIN_NUMBER_STYLE
      );
    }

    pythonIdentifierStyle = styleRegistry.lookupStyle( PYTHON_IDENTIFIER_STYLE );
    if ( pythonIdentifierStyle == null )
    {
      pythonIdentifierStyle = styleRegistry.createStyle(
        PYTHON_IDENTIFIER_STYLE, "Python identifiers", BuiltInStyles.BUILTIN_IDENTIFIER_STYLE
      );
    }    
    
  }
  
}