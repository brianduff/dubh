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

import oracle.javatools.editor.language.LanguageModule;
import oracle.javatools.editor.language.BaseStyle;
import oracle.javatools.editor.language.LanguageSupport;
import oracle.javatools.editor.language.StyleRegistry;

import org.dubh.jdev.language.python.PythonStyles;
import org.dubh.jdev.language.python.PythonLanguageSupport;

/**
 * Language module for the python language for the javatools editor
 * 
 * @author Brian.Duff@oracle.com
 */
public class PythonLanguageModule extends LanguageModule
{
  private static final String moduleName = "Python";

  private static final String[] supportedFileTypes = 
  {
    "py"
  };

  // Simply constructing this class is all that is required to register the
  // python language module with the IDE's editor framework. The superclass
  // (LanguageModule) does the work of actually registering this module.

// ----------------------------------------------------------------------------
// LanguageModule implementation
// ----------------------------------------------------------------------------

  /**
   * Creates a new LanguageSupport instance for editing python
   * 
   * @return a new LanguageSupport instance
   */
  public LanguageSupport createLanguageSupport()
  {
    return new PythonLanguageSupport();
  }

  /**
   * Register styles for this language with the style registry.
   * 
   * @param registry the style registry.
   */
  public void registerStyles( StyleRegistry registry )
  {
    PythonStyles styles = new PythonStyles( registry );
  }

  /**
   * Get a sample of this language
   */
  public String getContentSample()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "# Spam-cooking program" );
    buffer.append( "\n" );    
    buffer.append( "# Fetch the function sleep" );
    buffer.append( "from time import sleep" );
    buffer.append( "\n" );
    buffer.append( "print \"Please start cooking the spam. (I'll be back in 3 minutes.)\"" );
    buffer.append( "\n" );
    buffer.append( "# Wait for 3 minutes (that is, 3*60 seconds)...");
    buffer.append( "sleep(180)");
    buffer.append( "\n" );
    buffer.append( "print \"I'm baaack :)\"");
    buffer.append( "\n" );
    buffer.append( "# How hot is hot enough?");
    buffer.append( "hot_enough = 50" );
    buffer.append( "\n" );
    buffer.append( "temperature = input(\"How hot is the spam? \")" );
    buffer.append( "while temperature < hot_enough:");
    buffer.append( "  print \"Not hot enough... Cook it a bit more...\"");
    buffer.append( "  sleep(30)" );
    buffer.append( "  temperature = input(\"OK. How hot is it now? \")");
    buffer.append( "\n" );
    buffer.append( "print \"It's hot enough - You're done!\"");

    return buffer.toString();
    
  }

  /**
   * Get the names of all styles for this language
   * 
   * @return an array of style names
   */
  public String[] getStyleNames()
  {
    return PythonStyles.getStyleNames();
  }

  /**
   * Get the file types supported by this language.
   * 
   * @return an array of filetypes supported by this language
   */
  public String[] getSupportedFileTypes()
  {
    return supportedFileTypes;
  }

  /**
   * Get the presentation name of this language
   * 
   */
  public String getPresentationName()
  {
    return moduleName;
  }
}