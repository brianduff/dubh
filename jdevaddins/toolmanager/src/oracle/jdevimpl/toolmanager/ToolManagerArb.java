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
 * The Original Code is Oracle9i JDeveloper release 9.0.3
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.toolmanager;

import java.awt.Image;

import java.util.ResourceBundle;
import java.text.MessageFormat;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import oracle.ide.util.ArrayResourceBundle;
import oracle.ide.util.ResourceManager;


public class ToolManagerArb extends ArrayResourceBundle 
{
  // Compatability with JDeveloper 4.XX and lower
  private static final String nullString = "(null)";


  private static final ArrayResourceBundle resources = 
(ArrayResourceBundle) ResourceBundle.getBundle(ToolManagerArb.class.getName());


  /**
   * Gets the object associated with the specified resource id.
   * By convention all LRB's should implement a getObj static method
   * similar to the one below.
   */
  public static Object getObj(int key) {
    return ResourceManager.getObj(resources, key);
  }


  /**
   * Gets the string associated with the specified resource id.
   * By convention all LRB's should implement a getString static method
   * similar to the one below.
   */
  public static String getString(int key) {
    return ResourceManager.getStr(resources, key);
  }


  /**
   * Gets the character associated with the specified resource id.
   * By convention all LRB's should implement a getChar static method
   * similar to the one below.
   */
  public static char getChar(int key) {
    return ResourceManager.getChar(resources, key);
  }


  public static Integer getInteger(int key) {
    return ResourceManager.getInteger(resources, key);
  }


  public static char getMnemonic(int key) {
    return ResourceManager.getMnemonic(resources, key);
  }


  public static KeyStroke getAccelerator(int key) {
    return ResourceManager.getAccelerator(resources, key);
  }


  public static Icon getIcon(int key) {
    return ResourceManager.getIcon(resources, key);
  }


  public static Image getImage(int key) {
    return ResourceManager.getImage(resources, key);
  }

  public static String format(int index, Object[] params)
  {
    for (int i=0; i<params.length; i++)
    {
      if (params[i]==null)
        params[i] = nullString;
    }
    String result =  MessageFormat.format(getString(index), params);
    for (int i=0; i<params.length; i++)
    {
      if  (params[i] == nullString)
        params[i]=null;
    }
    return result;
  }
  public static String format(int index, Object param1)
  {
    return MessageFormat.format(getString(index),
      new Object[] { param1==null?nullString:param1 });
  }
  public static String format(int index, Object param1, Object param2)
  {
    return MessageFormat.format(getString(index),
      new Object[] { param1==null?nullString:param1,
                     param2==null?nullString:param2 });
  }
  public static String format(int index, Object param1, Object param2, Object param3)
  {
    return MessageFormat.format(getString(index),
      new Object[] { param1==null?nullString:param1,
                     param2==null?nullString:param2,
                     param3==null?nullString:param3 });
  }

  /** Gets the localizable objects */
  protected Object[] getContents() {
    return contents;
  }


  /** Identifies keys to access localizable object */
  public static final int UNTITLED_TOOL = 0;
  public static final int EXCEPTION_READING = 1;
  public static final int EXCEPTION_SAVING = 2;
  public static final int EDIT_TOOLS_MENU_TITLE = 3;
  public static final int EDIT_TOOLS_MENU_MNEMONIC = 4;
  public static final int EDIT_TOOLS_DIALOG_TITLE = 5;
  public static final int MUST_PROVIDE_A_CAPTION = 6;
  public static final int MUST_SPECIFY_AN_EXEC = 7;
  public static final int BAD_RUN_DIRECTORY = 8;
  public static final int DUPLICATE_CAPTIONS = 9;
  public static final int TOOLS_LABEL = 10;
  public static final int TOOLS_MNEMONIC = 11;
  public static final int MENU_CAPTION_LABEL = 12;
  public static final int MENU_CAPTION_MNEMONIC = 13;
  public static final int EXECUTABLE_LABEL = 14;
  public static final int EXECUTABLE_MNEMONIC = 15;
  public static final int RUN_IN_DIRECTORY_LABEL = 16;
  public static final int RUN_IN_DIRECTORY_MNEMONIC = 17;
  public static final int ARGUMENTS_LABEL = 18;
  public static final int ARGUMENTS_MNEMONIC = 19;
  public static final int BROWSE_EXECUTABLE_LABEL = 20;
  public static final int BROWSE_EXECUTABLE_MNEMONIC = 21;
  public static final int BROWSE_DIRECTORY_LABEL = 22;
  public static final int BROWSE_DIRECTORY_MNEMONIC = 23;
  public static final int MOVE_ARG_UP_TOOLTIP = 24;
  public static final int MOVE_ARG_DOWN_TOOLTIP = 25;
  public static final int MOVE_TOOL_UP_TOOLTIP = 26;
  public static final int MOVE_TOOL_DOWN_TOOLTIP = 27;
  public static final int REMOVE_TOOL_LABEL = 28;
  public static final int ADD_TOOL_LABEL = 29;
  public static final int ADD_ARG_LABEL = 30;
  public static final int REMOVE_ARG_LABEL = 31;
  public static final int ADD_TEXT_ARG_LABEL = 32;
  public static final int CHOOSE_EXECUTABLE_TITLE = 33;
  public static final int CHOOSE_DIRECTORY_TITLE = 34;
  public static final int TEXT_ARGUMENT_DIALOG_TITLE = 35;
  public static final int TEXT_ARGUMENT_LABEL = 36;
  public static final int TEXT_ARGUMENT_LABEL_MNEMONIC = 37;
  public static final int INSERT_BUTTON_LABEL = 38;
  public static final int INSERT_BUTTON_MNEMONIC = 39;
  public static final int TOOL_ARGUMENT_DIALOG_TITLE = 40;
  public static final int NEW_TOOL_TITLE_TEMPLATE = 41;
  public static final int DEFAULT_SOURCEPATH_NAME = 42;
  public static final int DEFAULT_SOURCEPATH_DESCRIPTION = 43;
  public static final int FILE_NAME_NAME = 44;
  public static final int FILE_NAME_DESCRIPTION = 45;
  public static final int FILE_NAME_NO_EXTENSION_NAME = 46;
  public static final int FILE_NAME_NO_EXTENSION_DESCRIPTION = 47;
  public static final int FULL_CLASS_NAME_NAME = 48;
  public static final int FULL_CLASS_NAME_DESCRIPTION = 49;
  public static final int FULL_URL_NAME = 50;
  public static final int FULL_URL_DESCRIPTION = 51;
  public static final int OUTPUT_DIRECTORY_NAME = 52;
  public static final int OUTPUT_DIRECTORY_DESCRIPTION = 53;
  public static final int PACKAGE_NAME_NAME = 54;
  public static final int PACKAGE_NAME_DESCRIPTION = 55;
  public static final int PATH_NAME = 56;
  public static final int PATH_DESCRIPTION = 57;
  public static final int PROJECT_CLASSPATH_NAME = 58;
  public static final int PROJECT_CLASSPATH_DESCRIPTION = 59;
  public static final int PROJECT_HTML_ROOT_NAME = 60;
  public static final int PROJECT_HTML_ROOT_DESCRIPTION = 61;
  public static final int PROJECT_SOURCEPATH_NAME = 62;
  public static final int PROJECT_SOURCEPATH_DESCRIPTION = 63;
  public static final int PROMPT_NAME = 64;
  public static final int PROMPT_DESCRIPTION = 65;
  public static final int PROMPT_DIALOG_LABEL_TEXT = 66;
  public static final int PROMPT_DIALOG_LABEL_MNEMONIC = 67;
  public static final int PROMPT_DIALOG_TITLE = 68;
  public static final int DETAILS_PANEL_TITLE = 69;
  public static final int DISPLAY_PANEL_TITLE= 70;
  public static final int ICON_LOCATION_LABEL=71;
  public static final int ICON_LOCATION_MNEMONIC=72;
  public static final int BROWSE_ICON_BUTTON_LABEL=73;
  public static final int BROWSE_ICON_MNEMONIC=74;
  public static final int TOOLTIP_LABEL=75;
  public static final int TOOLTIP_MNEMONIC=76;
  public static final int BAD_ICON_URL=77;
  public static final int CHOOSE_ICON_LOCATION=78;
  public static final int INVOCATION_PANEL_TITLE=79;
  public static final int ENABLEMENT_PANEL_TITLE=80;
  public static final int MUST_SELECT_A_DISPLAY=81;
  public static final int WHERE_INVOKE_FROM_LABEL=82;
  public static final int TOOLS_MENU_CHECKBOX_LABEL=83;
  public static final int TOOLS_MENU_CHECKBOX_MNEMONIC=84;
  public static final int NAV_CTX_CHECKBOX_LABEL=85;
  public static final int NAV_CTX_CHECKBOX_MNEMONIC=86;
  public static final int CODE_CTX_CHECKBOX_LABEL=87;
  public static final int CODE_CTX_CHECKBOX_MNEMONIC=88;
  public static final int TOOLBAR_CHECKBOX_LABEL=89;
  public static final int TOOLBAR_CHECKBOX_MNEMONIC=90; 
  public static final int MUST_ENTER_A_REGEXP=91;
  public static final int BAD_REGEXP=92;
  public static final int CHOOSE_WHEN_ENABLED_LABEL=93;
  public static final int ALWAYS_RADIO_LABEL=94;
  public static final int ALWAYS_RADIO_MNEMONIC=95;
  public static final int ACTIVE_NODE_RADIO_LABEL=96;
  public static final int ACTIVE_NODE_RADIO_MNEMONIC=97;
  public static final int REGEXP_RADIO_LABEL=98;
  public static final int REGEXP_RADIO_MNEMONIC=99;  
  public static final int MENU_MNEMONIC_LABEL=100;
  public static final int MENU_MNEMONIC_MNEMONIC=101;
  public static final int PREVIEW_LABEL=102;
  public static final int PREVIEW_MNEMONIC=103;  
  public static final int IDE_INSTALL_DIR_NAME=104;
  public static final int IDE_INSTALL_DIR_DESCRIPTION=105;

  /** Array containing localizable objects */
  private static final Object[] contents =
  {
      "(untitled)",  // UNTITLED_TOOL
      "Exception occurred while reading {0}",  // EXCEPTION_READING
      "Exception occurred while saving {0}",  // EXCEPTION_SAVING
      "Edit Tools...",  // EDIT_TOOLS_MENU_TITLE
      "E",  // EDIT_TOOLS_MENU_MNEMONIC
      "Edit Tools",  // EDIT_TOOLS_DIALOG_TITLE
      "A menu caption must be provided for each tool",  // MUST_PROVIDE_A_CAPTION
      "An executable must be specified for the {0} tool",  // MUST_SPECIFY_AN_EXEC
      "The run directory specified for the {0} tool does not exist or is not a directory. Specify a valid run directory or leave blank to use the current directory.",  // BAD_RUN_DIRECTORY
      "Two or more tools have the same caption \'\'{0}\'\'. Each tool must have a different menu caption.",  // DUPLICATE_CAPTIONS
      "Tools:",  // TOOLS_LABEL
      "T",  // TOOLS_MNEMONIC
      "Menu Caption (use & for mnemonic):",  // MENU_CAPTION_LABEL
      "M",  // MENU_CAPTION_MNEMONIC
      "Executable:",  // EXECUTABLE_LABEL
      "E",  // EXECUTABLE_MNEMONIC
      "Run in Directory:",  // RUN_IN_DIRECTORY_LABEL
      "R",  // RUN_IN_DIRECTORY_MNEMONIC
      "Arguments:",  // ARGUMENTS_LABEL
      "A",  // ARGUMENTS_MNEMONIC
      "Browse...",  // BROWSE_EXECUTABLE_LABEL
      "B",  // BROWSE_EXECUTABLE_MNEMONIC
      "Browse...",  // BROWSE_DIRECTORY_LABEL
      "o",  // BROWSE_DIRECTORY_MNEMONIC
      "Move the selected argument up",  // MOVE_ARG_UP_TOOLTIP
      "Move the selected argument down",  // MOVE_ARG_DOWN_TOOLTIP
      "Move the selected tool up",  // MOVE_TOOL_UP_TOOLTIP
      "Move the selected tool down",  // MOVE_TOOL_DOWN_TOOLTIP
      "Remove",  // REMOVE_TOOL_LABEL
      "Add",  // ADD_TOOL_LABEL
      "Add...",  // ADD_ARG_LABEL
      "Remove",  // REMOVE_ARG_LABEL
      "Add Text...",  // ADD_TEXT_ARG_LABEL
      "Select an Executable",  // CHOOSE_EXECUTABLE_TITLE
      "Select a Run Directory",  // CHOOSE_DIRECTORY_TITLE
      "Tool Text Argument",  // TEXT_ARGUMENT_DIALOG_TITLE
      "Enter the text argument:",  // TEXT_ARGUMENT_LABEL
      "E",  // TEXT_ARGUMENT_LABEL_MNEMONIC
      "Insert",  // INSERT_BUTTON_LABEL
      "I",  // INSERT_BUTTON_MNEMONIC
      "Tool Argument",  // TOOL_ARGUMENT_DIALOG_TITLE
      "Untitled Tool {0}",  // NEW_TOOL_TITLE_TEMPLATE
      "Default project sourcepath",  // DEFAULT_SOURCEPATH_NAME
      "The first sourcepath component for the current project context (the default location of new classes in JDeveloper)",  // DEFAULT_SOURCEPATH_DESCRIPTION
      "File name",  // FILE_NAME_NAME
      "The file name of the selected node (e.g. \"MyFile.java\")",  // FILE_NAME_DESCRIPTION
      "File name without extension",  // FILE_NAME_NO_EXTENSION_NAME
      "The file name of the selected node without its extension if any (e.g. \"MyFile\")",  // FILE_NAME_NO_EXTENSION_DESCRIPTION
      "Full class name",  // FULL_CLASS_NAME_NAME
      "For java and jsp nodes, the fully qualified class name, including the package (e.g. \"oracle.jdeveloper.tool.MyClass\")",  // FULL_CLASS_NAME_DESCRIPTION
      "URL",  // FULL_URL_NAME
      "The full URL of the selected node (e.g. \"http://my.org/dav/file.txt\")",  // FULL_URL_DESCRIPTION
      "Project output directory",  // OUTPUT_DIRECTORY_NAME
      "The output directory of the active configuration in the current project context",  // OUTPUT_DIRECTORY_DESCRIPTION
      "Package name",  // PACKAGE_NAME_NAME
      "For java and jsp nodes, the full package name of the class (e.g. \"oracle.jdeveloper.tool\")",  // PACKAGE_NAME_DESCRIPTION
      "File path",  // PATH_NAME
      "The full absolute path of the selected node (e.g. \"/usr/home/smith/myfile.txt\")",  // PATH_DESCRIPTION
      "Project classpath",  // PROJECT_CLASSPATH_NAME
      "The full classpath of the current project context",  // PROJECT_CLASSPATH_DESCRIPTION
      "Project HTML root",  // PROJECT_HTML_ROOT_NAME
      "The HTML root directory of the current project context",  // PROJECT_HTML_ROOT_DESCRIPTION
      "Full project sourcepath",  // PROJECT_SOURCEPATH_NAME
      "The full sourcepath of the current project context, which includes the sourcepath of any libraries",  // PROJECT_SOURCEPATH_DESCRIPTION
      "Prompt",  // PROMPT_NAME
      "A prompt will appear requesting a value for this argument when the tool is run",  // PROMPT_DESCRIPTION
      "Tool Argument Value:",  // PROMPT_DIALOG_LABEL_TEXT
      "T",  // PROMPT_DIALOG_LABEL_MNEMONIC
      "Invoke Tool",  // PROMPT_DIALOG_TITLE
      "Details", //DETAILS_PANEL_TITLE
      "Display", // DISPLAY_PANEL_TITLE
      "Icon Location:",
      "I",
      "Browse...",
      "B",
      "ToolTip Text:",
      "T",      
      "The icon specified for the {0} tool could not be read. A valid readable icon should be provided, or leave the field blank for no icon.",
      "Choose Tool Icon",
      "Invocation",
      "Enablement",
      "No invocation method is selected for the {0} tool. At least one item must be selected.",
      "Choose where this tool can be invoked from",
      "Tools Menu",
      "T",
      "Navigator Context Menu",
      "N",
      "Code Editor Context Menu",
      "C",
      "Toolbar",
      "o",
      "The enablement for the {0} tool is set to use a regular expression. A regular expression must be provided for this tool",
      "The regular expression provided for the {0} tool is invalid:\n\n{1}",
      "Choose when this tool is enabled",
      "Always",
      "A",
      "When a node is selected or editor is active",
      "n",
      "When selected filename matches regular expression:",
      "r",
      "Menu Mnemonic:",
      "M",
      "Preview:",
      "P",
      "IDE install directory",
      "The directory in which the IDE is installed",
  };
}
