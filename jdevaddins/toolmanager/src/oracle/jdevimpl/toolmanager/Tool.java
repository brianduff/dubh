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

import java.io.File;
import java.net.URL;

import oracle.bali.share.nls.StringUtils;

import oracle.ide.addin.Context;
import oracle.ide.runner.SimpleProcess;

/**
 * A tool is an external program accessible from the IDE Tools menu
 *
 * @author Brian.Duff@oracle.com
 */
public final class Tool
{
  /**
   * This value for EnableType indicates that the tool is always enabled
   */
  public static final int ET_ALWAYS_ENABLED = 0;
  /**
   * This value for EnableType indicates that the tool is enabled when a node
   * is selected or an editor is active
   */
  public static final int ET_ENABLED_ON_NODE = 1;
  /**
   * This value for EnableType indicates that the tool is enabled when the
   * regular expression in the EnableRegularExpression property matches
   * the path of the selected node
   */
  public static final int ET_ENABLED_ON_REGEXP = 2;

  private String m_title;
  private String m_command;
  private String m_workingDirectory;
  private ToolArgument[] m_arguments;
  private URL m_iconURL;
  private String m_toolTip;
  private boolean m_hasToolbarButton;
  private boolean m_hasContextMenuItem;
  private boolean m_hasCodeContextMenuItem;
  private boolean m_hasToolsMenuItem = true;
  private int m_enableType = ET_ALWAYS_ENABLED;
  private String m_enableRegularExpression;


  public Tool()
  {
    // Set the default icon up to be a resource inside the classpath.
    setIconURL(
      getClass().getResource( "tool.gif" )      // NOTRANS
    );
  }

  /**
   * Copy constructor
   * 
   * @param t a tool to copy. This instance will not hold any references to
   *    t when the constructor returns.
   */
  public Tool( final Tool t )
  {
    setTitle( t.getTitle() );
    setCommand( t.getCommand() );
    setWorkingDirectory( t.getWorkingDirectory() );
    setEnableRegularExpression( t.getEnableRegularExpression() );
    setEnableType( t.getEnableType() );
    setCodeEditorContextMenuItem( t.isCodeEditorContextMenuItem() );
    setContextMenuItem( t.isContextMenuItem() );
    setToolbarButton( t.isToolbarButton() );
    setToolsMenuItem( t.isToolsMenuItem() );
    setIconURL( t.getIconURL() );
    setToolTip( t.getToolTip() );

    ToolArgument[] args = t.getArguments();
    m_arguments = new ToolArgument[ args.length ];
    for ( int i=0; i < args.length; i++ )
    {
      m_arguments[ i ] = args[ i ];
    }
  }

  public String toString()
  {
    if ( getTitle() == null || getTitle().trim().length() == 0 )
    {
      return ToolManagerArb.getString( ToolManagerArb.UNTITLED_TOOL );
    }
    return getTitle();
  }


  /**
   * The icon URL, if any, is used in the toolbar or menu item this tool
   * represents. May be null.
   *
   */
  public URL getIconURL()
  {
    return m_iconURL;
  }

  /**
   * The mnemonic is attached to the menu item
   */
  public void setIconURL( final URL iconURL )
  {
    m_iconURL = iconURL;
  }

  /**
   * Does this tool have a toolbar button?
   */
  public boolean isToolbarButton()
  {
    return m_hasToolbarButton;
  }

  /**
   * Does this tool have a toolbar button?
   */
  public void setToolbarButton( final boolean hasToolbarButton )
  {
    m_hasToolbarButton = hasToolbarButton;
  }

  /**
   * Does this tool have a context menu item?
   */
  public boolean isContextMenuItem()
  {
    return m_hasContextMenuItem;
  }

  /**
   * Does this tool have a context menu item?
   */
  public void setContextMenuItem( final boolean hasContextMenuItem )
  {
    m_hasContextMenuItem = hasContextMenuItem;
  }

  public boolean isCodeEditorContextMenuItem()
  {
    return m_hasCodeContextMenuItem;
  }

  public void setCodeEditorContextMenuItem( final boolean has )
  {
    m_hasCodeContextMenuItem = has;
  }

  public boolean isToolsMenuItem()
  {
    return m_hasToolsMenuItem;
  }

  public void setToolsMenuItem( final boolean has )
  {
    m_hasToolsMenuItem = has;
  }

  /**
   * The items associated with a tool can become disabled depending on the 
   * enable type. For ET_ALWAYS_ENABLED (the default), the tool is always
   * enabled. For ET_ENABLED_ON_NODE, the tool is enabled provided there is 
   * at least one element in the current Context. For ET_ENABLED_ON_REGEXP,
   * a regular expression must be set using the setEnableRegularExpression()
   * method. If the selected element's file system path matches this 
   * expression, the tool is enabled.
   */
  public int getEnableType()
  {
    return m_enableType;
  }


  /**
   * The items associated with a tool can become disabled depending on the 
   * enable type. For ET_ALWAYS_ENABLED (the default), the tool is always
   * enabled. For ET_ENABLED_ON_NODE, the tool is enabled provided there is 
   * at least one element in the current Context. For ET_ENABLED_ON_REGEXP,
   * a regular expression must be set using the setEnableRegularExpression()
   * method. If the selected element's file system path matches this 
   * expression, the tool is enabled.
   */
  public void setEnableType( final int enableType )
  {
    m_enableType = enableType;
  }

  /**
   * The regular expression used if EnableType is ET_ENABLED_ON_REGEXP.
   *
   * @see #setEnableType( int )
   */
  public String getEnableRegularExpression()
  {
    return m_enableRegularExpression;
  }

  /**
   * The regular expression used if EnableType is ET_ENABLED_ON_REGEXP.
   *
   * @see #setEnableType( int )
   */
  public void setEnableRegularExpression( final String expression )
  {
    m_enableRegularExpression = expression;
  }

  /**
   * A tool may have an associated tool tip. This is used for toolbar items
   */
  public String getToolTip()
  {
    return m_toolTip;
  }

  /**
   * A tool may have an associated tool tip. This is used for toolbar items
   */
  public void setToolTip( final String toolTip )
  {
    m_toolTip = toolTip;
  }
  

  /**
   * The title is displayed in the Tools menu for this tool
   */
  public String getTitle()
  {
    return m_title;
  }

  /**
   * The title is displayed in the Tools menu for this tool
   */
  public void setTitle( final String title )
  {
    m_title = title;
  }

  /**
   * The command is the (argumentless) OS command used to run this tool
   */
  public String getCommand()
  {
    return m_command;
  }

  /**
   * The command is the (argumentless) OS command used to run this tool
   */
  public void setCommand( final String command )
  {
    m_command = command;
  }

  /**
   * The working directory is used to specify where the command is run from.
   */
  public String getWorkingDirectory()
  {
    return m_workingDirectory;
  }

  /**
   * The working directory is used to specify where the command is run from.
   */
  public void setWorkingDirectory( final String workingDirectory )
  {
    m_workingDirectory = workingDirectory;
  }

  /**
   * The arguments that are passed into the tool when invoked
   */
  public ToolArgument[] getArguments()
  {
    return m_arguments;
  }

  /**
   * The working directory is used to specify where the command is run from.
   */
  public void setArguments( final ToolArgument[] arguments )
  {
    m_arguments = arguments;
  }

  /**
   * Invoke the tool in the specified context
   *
   * @param ctx the context in which to invoke the tool
   */
  void invoke( final Context ctx )
  {
    final int argCount = (getArguments() == null) ? 0 : getArguments().length;
    final String[] cmdarray = new String[ argCount + 1 ];
    cmdarray[0] = getCommand();
    for ( int i=0; i < argCount; i++ )
    {
      cmdarray[ i+1 ] = getArguments()[i].getValue( ctx );
    }
    // Use the Run Manager to invoke the command.
    final SimpleProcess sp = new SimpleProcess( cmdarray );
    sp.setLabel( StringUtils.stripMnemonic( getTitle() ) );
    if ( getWorkingDirectory() != null )
    {
      final String expanded = ToolManager.getArgumentRegistry().expandMonikers( 
        getWorkingDirectory(), ctx
      );
      File f = new File( expanded );
      if ( f.exists() && f.isDirectory() )
      {
        sp.setWorkingDirectory( f );
      }
    }

    sp.exec();
    // Simpleprocess never throws any exceptions back out. It probably should
    // because we can't do anything if the process fails to start and it's
    // never reported to the user.

  }
}