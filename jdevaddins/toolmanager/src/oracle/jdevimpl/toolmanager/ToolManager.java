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

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.IdeConstants;
import oracle.ide.addin.ActiveViewEvent;
import oracle.ide.addin.ActiveViewListener;
import oracle.ide.addin.Addin;
import oracle.ide.addin.Context;
import oracle.ide.addin.Controller;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.View;
import oracle.ide.addin.ViewSelectionEvent;
import oracle.ide.addin.ViewSelectionListener;
import oracle.ide.marshal.xml.Object2Dom;
import oracle.ide.net.URLFactory;
import oracle.ide.util.Assert;
import oracle.ide.resource.IdeIcons;
import oracle.ide.keyboard.DefaultKeyStrokeContext;
import oracle.ide.keyboard.KeyStrokeMap;
import oracle.ide.model.Locatable;
import oracle.ide.net.URLFileSystem;
import oracle.ide.navigator.NavigatorWindow;

import oracle.bali.ewt.dialog.JEWTDialog;
import oracle.bali.share.nls.StringUtils;

import oracle.xml.parser.v2.XMLDocument;

import oracle.jdevimpl.toolmanager.argument.FileName;
import oracle.jdevimpl.toolmanager.argument.Path;
import oracle.jdevimpl.toolmanager.argument.FileNameNoExtension;
import oracle.jdevimpl.toolmanager.argument.PackageName;
import oracle.jdevimpl.toolmanager.argument.FullClassName;
import oracle.jdevimpl.toolmanager.argument.ProjectClasspath;
import oracle.jdevimpl.toolmanager.argument.FullURL;
import oracle.jdevimpl.toolmanager.argument.ProjectSourcepath;
import oracle.jdevimpl.toolmanager.argument.ProjectHTMLRoot;
import oracle.jdevimpl.toolmanager.argument.OutputDirectory;
import oracle.jdevimpl.toolmanager.argument.Prompt;
import oracle.jdevimpl.toolmanager.argument.DefaultSourcepath;
import oracle.jdevimpl.toolmanager.argument.IDEInstallDir;
import java.awt.MediaTracker;

import oracle.jdeveloper.ceditor.CodeEditor;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * The tool manager addin provides support for user configurable items in the
 * Tools menu which can be used to invoke external commands. It installs an
 * Edit Tools... menu item to the Tools menu.
 * <p>
 * The tools are persisted in the tools.xml system file. On startup, this file
 * is depersisted and menu items are created under Edit Tools... for each
 * available tool. At runtime, the edit tools dialog can be used to
 * alter the available tools. On shutdown, the tools are persisted back into
 * tools.xml
 * <p>
 * The tool manager provides flexible support for argument types. For
 * details see the ToolArgumentRegistry class.
 *
 * @see oracle.jdevimpl.toolmanager.ToolArgumentRegistry
 * @author Brian.Duff@oracle.com
 */
public final class ToolManager extends BaseController implements Addin,
  ContextMenuListener, ActiveViewListener, ViewSelectionListener
{
  private static final float MY_VERSION = 0.4f;
  private static final String TOOLS_FILE = "tools.xml";   // NOTRANS
  private static final String TOOL_KEY = "oracle.jdevimpl.extras.tooledit.Tool";  // NOTRANS

  private static ToolArgumentRegistry m_registry;
  private List m_tools;
  private List m_menuItems;
  private List m_toolBarItems;
  private ToolKeyStrokeContext m_keyStrokeContext;

  private IdeAction m_editAction;



  public List getTools()
  {
    return m_tools;
  }

  public static ToolArgumentRegistry getArgumentRegistry()
  {
    return m_registry;
  }

  public boolean canShutdown()
  {
    return true;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

  /**
   * Register available arguments
   */
  private void registerArguments()
  {
    // All implementations of ToolArgument (other than TextArgument) should
    // be registered here. In the future, I guess we could publish the API
    // for ToolArgument and allow customers to plug in their own types of
    // argument, but maybe that's going over the top a bit...
    m_registry.register( new FileName() );
    m_registry.register( new Path() );
    m_registry.register( new FileNameNoExtension() );
    m_registry.register( new PackageName() );
    m_registry.register( new FullClassName() );
    m_registry.register( new ProjectClasspath() );
    m_registry.register( new FullURL() );
    m_registry.register( new ProjectSourcepath() );
    m_registry.register( new ProjectHTMLRoot() );
    m_registry.register( new OutputDirectory() );
    m_registry.register( new DefaultSourcepath() );
    m_registry.register( new Prompt() );
    m_registry.register( new IDEInstallDir() );
  }

  /**
   * Load tools from the persisted tools file
   */
  private void loadTools()
  {

    final File f = new File( Ide.getSystemDirectory() + TOOLS_FILE );
    if ( !f.exists() )
    {
      m_tools = new ArrayList();

      return;
    }

    try
    {
      final Object2Dom o2d = Object2Dom.newInstance();
      URL u = URLFactory.newFileURL( f );
      o2d.setDocumentURL( u );

      m_tools = (List) o2d.toObject( u );

    }
    catch (IOException ioe)
    {
      Assert.println(
        ToolManagerArb.format( ToolManagerArb.EXCEPTION_READING, f )
      );
      Assert.printStackTrace( ioe );

      m_tools = new ArrayList();
    }

    installKeystrokes();
  }

  private void saveTools()
  {
    if ( m_tools == null )
    {
      return;
    }
    final File f = new File( Ide.getSystemDirectory() + TOOLS_FILE );

    final Object2Dom o2d = Object2Dom.newInstance();
    o2d.setDocumentURL( URLFactory.newFileURL( f ) );

    XMLDocument xmlDoc = o2d.toDocument( m_tools, "tools" );    // NOTRANS

    try
    {
      PrintWriter pw = new PrintWriter( new FileWriter( f ) );
      xmlDoc.print( pw );
      pw.close();
    }
    catch (IOException ioe )
    {
      Assert.println(
        ToolManagerArb.format( ToolManagerArb.EXCEPTION_SAVING, f )
      );
      Assert.printStackTrace( ioe );
    }



  }

  private IdeAction getIdeActionForTool( Tool t )
  {
    final String strippedTitle = StringUtils.stripMnemonic( t.getTitle() );
    IdeAction action = IdeAction.get(
      Ide.newCmd( strippedTitle ), null, strippedTitle
    );
    action.setController( this );
    action.putValue( TOOL_KEY, t );
    if ( t.getIconURL() != null )
    {
      ImageIcon image = new ImageIcon( t.getIconURL() );
      if ( image.getImageLoadStatus() == MediaTracker.COMPLETE )
      {
        action.putValue( IdeAction.SMALL_ICON,
          new ImageIcon( t.getIconURL() )
        );
      }
      else
      {
        action.putValue(
          IdeAction.SMALL_ICON, IdeIcons.getIcon( IdeIcons.BLANK_ICON )
        );
      }
    }
    else
    {
      action.putValue( IdeAction.SMALL_ICON, IdeIcons.getIcon( IdeIcons.BLANK_ICON ) );
    }
    action.putValue( IdeAction.NAME, strippedTitle );
    if ( t.getToolTip() != null )
    {

      action.putValue( IdeAction.SHORT_DESCRIPTION, t.getToolTip() );
    }
    action.putValue( IdeAction.MNEMONIC, new Integer(
      StringUtils.getMnemonicKeyCode( t.getTitle() )
    ));


    return action;
  }

  /**
   * Create all the menus
   */
  private void createMenus()
  {
    m_menuItems = new ArrayList();
    for ( int i=0; i < m_tools.size(); i++ )
    {
      Tool t = (Tool)m_tools.get( i );

      if ( t.isToolsMenuItem() )
      {

        int prefPos = Ide.getMenubar().getIndexOfCommandId(
          Ide.getMainWindow().Tools, IdeConstants.IDE_SETTINGS_CMD_ID
        );

        IdeAction action = getIdeActionForTool( t );

        JMenuItem item = Ide.getMenubar().createMenuItem( action );
        m_menuItems.add( item );
        Ide.getMenubar().insert( item, Ide.getMainWindow().Tools, prefPos-1 );
      }
    }
  }

  /**
   * Create items in the toolbar for all Tools which are invocable in that
   * context
   */
  private void createToolBarItems()
  {
    m_toolBarItems = new ArrayList();
    boolean addSeparator = true;

    for ( int i=0; i < m_tools.size(); i++ )
    {
      Tool t = (Tool) m_tools.get( i );
      if ( t.isToolbarButton() ) 
      {
        // Add a separator before the first toolbar button.
        if ( addSeparator )
        {
          JComponent c = new JToolBar.Separator();
          Ide.getToolbar().add( c );
          m_toolBarItems.add( c );
        
          addSeparator = false;
        }
      
        IdeAction action = getIdeActionForTool( t );
        m_toolBarItems.add( Ide.getToolbar().add( action ) );
      }
    }
    Ide.getToolbar().validate();
    Ide.getToolbar().repaint();

  }

  private void removeAllToolBarItems()
  {
    if ( m_toolBarItems == null )
    {
      return;
    }

    Iterator items = m_toolBarItems.iterator();
    while ( items.hasNext() )
    {
      Ide.getToolbar().remove( (Component)items.next() );
    }
  }

  private void removeAllMenus()
  {
    if ( m_menuItems == null )
    {
      return;
    }
    Iterator items = m_menuItems.iterator();
    while ( items.hasNext() )
    {
      Ide.getMainWindow().Tools.remove( (JMenuItem)items.next() );
    }
  }

  public void initialize()
  {
    m_registry = new ToolArgumentRegistry();
    registerArguments();

    Object2Dom.registerConverter( new ToolArgument2Dom( m_registry ) );
    loadTools();


    // We can't add toolbar items straight away, because the IDE delays
    // creation of toolbar buttons until all addins have loaded. If we added
    // them now, they'd appear very early on in the toolbar, then jump to
    // the end when the user edited tools.

    // Instead, listen for the main window becoming visible, then add the
    // toolbar buttons.

    // This code _requires_ build 9.0.2.7.55 or later, because of changes
    // made to the way lib/ext addins are initialized (the fix for bug
    // 2191311: THIRD PARTY ADDIN INSTALLER: UNEXPECTED DIFFERENCES IN INITIALIZE 
    // )
    Ide.getMainWindow().addWindowListener(
      new WindowAdapter()
      {
        public void windowOpened( WindowEvent we )
        {
          // The configurable tools block goes above the IDE Preferences menu item
          // (which must always be at the bottom of the menu)
          int prefPos = Ide.getMenubar().getIndexOfCommandId(
            Ide.getMainWindow().Tools, IdeConstants.IDE_SETTINGS_CMD_ID
          );


          String cmd = ToolManagerArb.getString(
          ToolManagerArb.EDIT_TOOLS_MENU_TITLE );
          m_editAction = IdeAction.get( Ide.newCmd(cmd), null, cmd );
          m_editAction.setController( ToolManager.this );
          JMenuItem editItem = Ide.getMenubar().createMenuItem( m_editAction );
          Ide.getMainWindow().Tools.insertSeparator( prefPos - 1 );
          Ide.getMenubar().insert( editItem, Ide.getMainWindow().Tools, prefPos  );
          createMenus();
          createToolBarItems();
          Ide.getMainWindow().removeWindowListener( this );
          Ide.getMainWindow().addActiveViewListener( ToolManager.this );
          View v = Ide.getMainWindow().getLastActiveView();
          if ( v != null )
          {
            updateActionEnablement( v.getContext() );
            v.addViewSelectionListener( ToolManager.this );
          }
        }
      }
    );

    Ide.getNavigatorManager().addContextMenuListener( this, null );
    Ide.getEditorManager().getContextMenu().addContextMenuListener( this );




  }

  public void shutdown()
  {
    saveTools();
    m_registry = null;
    m_tools = null;
    m_menuItems = null;

    Ide.getNavigatorManager().removeContextMenuListener( this );
    Ide.getEditorManager().getContextMenu().removeContextMenuListener( this );
    Ide.getMainWindow().removeActiveViewListener( this );

    // Object2Dom has a reference to a couple of things we can't remove...
  }

  public float version()
  {
    return MY_VERSION;
  }

  private void editTools()
  {
    final ToolPanel tp = new ToolPanel();
    tp.setToolManager( this );

    final JEWTDialog dlg = JEWTDialog.createDialog(
      Ide.getMainWindow(),
      ToolManagerArb.getString( ToolManagerArb.EDIT_TOOLS_DIALOG_TITLE ),
      JEWTDialog.BUTTON_OK + JEWTDialog.BUTTON_CANCEL
    );

    dlg.setContent( tp );
    dlg.setPreferredSize( 600, 400 );
    dlg.setResizable( true );

    dlg.addVetoableChangeListener( new VetoableChangeListener() {
      public void vetoableChange( PropertyChangeEvent evt )
        throws PropertyVetoException
      {
        if (dlg.isDialogClosingEvent( evt ) )
        {
          if ( !tp.doValidationCheck() )
          {
            throw new PropertyVetoException("", evt); // NOTRANS
          }
        }
      }
    });

    if ( dlg.runDialog() )
    {
      removeAllMenus();
      removeAllToolBarItems();
      deinstallKeystrokes();

      m_tools = tp.getTools();
      if ( m_tools.size() > 0 )
      {
        createMenus();
        createToolBarItems();
        installKeystrokes();
      }
    }
  }


  /**
   * Install a keystroke context into the IDE. This allows accelerators
   * to be assigned to tools from the Accelerators page in preferences.
   */
  private void installKeystrokes()
  {
    m_keyStrokeContext = new ToolKeyStrokeContext();
    Ide.getKeyStrokeContextRegistry().addContext( m_keyStrokeContext );
  }

  private void deinstallKeystrokes()
  {
    if ( m_keyStrokeContext != null )
    {
      Ide.getKeyStrokeContextRegistry().removeContext( m_keyStrokeContext );
    }
  }

  private class ToolKeyStrokeContext extends DefaultKeyStrokeContext
  {
    public String getName()
    {
      return "Tool Manager";    // NLS?
    }

    public Set getAllActions( boolean global )
    {
      if ( global )
      {
        HashSet al = new HashSet( m_tools.size() );
        for ( int i=0; i < m_tools.size(); i++ )
        {
          al.add( getIdeActionForTool( (Tool) m_tools.get( i ) ) );
        }
        return al;
      }
      return null;
    }

    protected void addGlobalKeyStrokes( KeyStrokeMap map )
    {

    }

  }


  public boolean handleEvent( IdeAction action, Context ctx )
  {
    if ( action == m_editAction )
    {
      editTools();
    }

    Object o = action.getValue( TOOL_KEY );
    if ( o instanceof Tool )
    {
      final Tool tl = (Tool)o;
      tl.invoke( ctx );
      return true;
    }

    return false;
  }

  public boolean update(IdeAction action, Context context)
  {
    Object o = action.getValue( TOOL_KEY );

    if ( o instanceof Tool )
    {
      action.setEnabled( isToolEnabledInContext( (Tool)o, context ) );
      return true;
    }

    return false;
  }

  public void checkCommands( Context context, Controller activeController )
  {
    // This never gets called. Hmm.
  }

  private void updateActionEnablement( Context context )
  {
    if ( m_tools != null )
    {
      for ( int i=0 ; i < m_tools.size() ; i++ )
      {
        Tool t = (Tool) m_tools.get( i );
        if ( t.isToolbarButton() )
        {
          getIdeActionForTool( t ).setEnabled(
            isToolEnabledInContext( t, context )
          );
        }
      }
    }
  }

  private boolean isToolEnabledInContext( Tool tool, Context ctx )
  {


    if ( tool.getEnableType() == tool.ET_ALWAYS_ENABLED )
    {
      return true;
    }
    else if ( tool.getEnableType() == tool.ET_ENABLED_ON_NODE )
    {
      // Sigh.
      if ( ctx.getView() instanceof CodeEditor )
      {
        return (ctx.getDocument() != null);
      }

      return (ctx.getElement() instanceof Locatable);
    }
    else if ( tool.getEnableType() == tool.ET_ENABLED_ON_REGEXP )
    {
      if ( ctx != null && ( ctx.getDocument() != null || ctx.getElement() != null ) )
      {
        URL url = null;
        if ( ctx.getView() instanceof CodeEditor && ctx.getDocument() != null )
        {
          url = ctx.getDocument().getURL();
        }
        else
        {
          if ( ctx.getElement() instanceof Locatable )
          {
            url = ((Locatable)ctx.getElement()).getURL();
          }
        }

        if ( url != null )
        {

          String path = URLFileSystem.getPlatformPathName( url );

          try
          {
            RE regexp = new RE( tool.getEnableRegularExpression() );
            return regexp.match( path );
          }
          catch (RESyntaxException res)
          {
            return false;
          }
        }
      }
    }
    return false;
  }


// ContextMenuListener interface

  /**
   * Called just before the context menu is popping up.
   *
   * @param context the current view context.
   */
  public void poppingUp(ContextMenu popup)
  {
    boolean sepAdded = false;
    if ( m_tools != null )
    {
      for ( int i=0; i < m_tools.size(); i++ )
      {
        Tool t = (Tool)m_tools.get( i );

        boolean isRightContext =
          (t.isContextMenuItem() && popup.getContext().getView() instanceof NavigatorWindow) ||
          (t.isCodeEditorContextMenuItem() && popup.getContext().getView() instanceof CodeEditor );

        if ( isRightContext && isToolEnabledInContext( t, popup.getContext() ) )
        {
          if ( !sepAdded )
          {
            popup.addSeparator();
            sepAdded = true;
          }
          IdeAction action = getIdeActionForTool( t ) ;
          popup.add( popup.createMenuItem( action ) );
          update( action, popup.getContext() );
        }
      }
    }
  }

  public void poppingDown(ContextMenu popup)
  {

  }

  /**
   * Called when the user double clicks on an item that has a popup menu.
   * Only one listener should return true from this menu.
   *
   * @param context the current context
   */
  public boolean handleDefaultAction(Context context)
  {
    return false;
  }

// ActiveViewListener implementation

  public void activeViewChanged(final ActiveViewEvent e)
  {
    if ( e == null ) return;
    // When the active view changes, we remove ourselves as a listener from
    // the former view and add ourselves to the new view.
    if ( e.getOldView() != null )
    {
      e.getOldView().removeViewSelectionListener( this );
    }
    if ( e.getNewView() != null )
    {
      e.getNewView().addViewSelectionListener( this );
      updateActionEnablement( e.getNewView().getContext() );
    }
    // BUGBUG if there is no view, the enablement will be wrong.

  }

// ViewSelectionListener implementation

  public void viewSelectionChanged( final ViewSelectionEvent vse )
  {
    // When the view selection changes, update the action enablement. This is
    // so that the toolbar items enable properly.
    updateActionEnablement( vse.getView().getContext() );
  }

}