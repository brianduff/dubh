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

import java.awt.MediaTracker;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.Observer;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.gallery.GalleryElement;
import oracle.ide.gallery.GalleryFolder;
import oracle.ide.gallery.ObjectGallery;
import oracle.ide.gallery.ObjectGalleryAddin;
import oracle.ide.model.Document;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.util.Assert;

import oracle.jdevimpl.templatemaker.velocity.VelocityTemplateCaster;

/**
 * TemplateMaker provides support for creating new files from templates 
 * in the IDE and creating templates from existing files.
 *
 * It adds a "Save as Template..." menu item to the File menu and the
 * context menu of all saveable nodes. 
 *
 * It adds a category to the New Object Gallery called "Templates" which 
 * contains all user defined templates. Choosing one of these templates results
 * in a dialog which prompts for a path or directory and filename for the
 * new file based on the selected template.
 *
 * It adds a top level navigator node called Templates, which can be used to
 * edit and delete previously defined template files from within the IDE.
 *
 * Different "TemplateCaster" implementations can be plugged in. The simplest
 * TemplateCaster simply copies the template to the new file location. The
 * far more powerful VelocityTemplateCaster provides the full power of the
 * Apache Jakarta Velocity template engine.
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class TemplateMaker extends BaseController
  implements Addin, ContextMenuListener, Observer
{

  /**
   * The name of the template directory. This is a subdirectory of the 
   * IDE user home directory (i.e. at the same level as mywork and
   * system)
   */
  private final static String TEMPLATE_DIR_NAME = "templates";

  public final static URL TEMPLATE_DIR_URL = 
    URLFactory.newFileURL(
      new File( Ide.getUserHomeDirectory(), TEMPLATE_DIR_NAME )
    );

  private IdeAction m_saveAsTemplateAction;
  private GalleryFolder m_templatesGalleryFolder;
  private TemplatesFolder m_templatesFolder;

  private TemplateCaster m_caster;

  private static final Icon s_templateIcon;

  static
  {
    ImageIcon i = new ImageIcon( 
      TemplateMaker.class.getResource( "template.png" )
    );
    if ( i.getImageLoadStatus() == MediaTracker.COMPLETE )
    {
      s_templateIcon = i;
    }
    else
    {
      s_templateIcon = null;
    }
  }

  /**
   * Create all actions and main menu items used by this addin
   */
  private void createActionsAndMenus()
  {
    m_saveAsTemplateAction = IdeAction.get(
      Ide.newCmd( "TemplateMaker.SaveAsTemplate" ), "Save as Template...", 
      new Integer( 0 )
    );
    m_saveAsTemplateAction.setController( this );

    // Insert this item into the File menu.
    Ide.getMenubar().insert( 
      Ide.getMenubar().createMenuItem( m_saveAsTemplateAction ), 
      Ide.getMainWindow().File,
      Ide.getMainWindow().SaveAs
    );
  }

  /**
   * Is the specified context a valid context for the Save as Template
   * menu item? A valid context is not null and has an element which 
   * is locatable ( has a URL ). TemplateNodes are also invalid contexts.
   *
   * @param context the context
   * @return true if this is a valid context for the Save as Template menu
   *    item
   */
  private boolean isValidSaveContext( final Context context )
  {
    return context != null && context.getElement() instanceof Locatable &&
      !(context.getElement() instanceof TemplateNode) &&
      ((Locatable)context.getElement()).getURL() != null &&
      !URLFileSystem.isDirectoryPath( ((Locatable)context.getElement()).getURL() );
  }

  /**
   * Prompt the user to enter a name for a saved template.
   *
   * @param context the context to get the element from
   */
  private void saveAsTemplate( final Context context )
  {
    final URL url = ((Document)context.getElement()).getURL();
    final String fileName = URLFileSystem.getFileName( url );
    final int lastDot = fileName.lastIndexOf( '.' );
    final String shortName = fileName.substring( 0, lastDot );
    final String extension = URLFileSystem.getSuffix( url );  
  
    TextPromptDialog dlg = new TextPromptDialog();
    dlg.setDialogTitle( "Save as Template" );
    dlg.setPrompt( "Enter a name for the template:" );
    dlg.setPromptMnemonic( 'E' );
    dlg.setText( shortName );

    if ( dlg.showDialog( Ide.getMainWindow() ) )
    {
      doSaveTemplate( (Document)context.getElement(), 
        dlg.getText() + extension );
    }
  }

  /**
   * Actually save a template
   *
   * @param doc the document to save
   * @param templateName the filename of the template
   */
  private boolean doSaveTemplate( final Document doc, final String templateName )
  {
    if ( !URLFileSystem.exists( getTemplateDirectory() ) )
    {
      if ( !URLFileSystem.mkdirs( getTemplateDirectory() ) )
      {
        alert(
          "Unable to create the templates directory in JDEV_USER_HOME. Check that you have permission to write to this directory."
        );
        return false;
      }
    }

    String baseName = stripExtension( templateName );

    // Check if a template already exists with this name. We check the name
    // **without** extension, because that's what appears in the object
    // gallery.
    Iterator allURLs = getTemplateURLs();
    while ( allURLs.hasNext() )
    {
      URL thisURL = (URL) allURLs.next();
      String name = stripExtension( 
        URLFileSystem.getFileName( thisURL )
      );
      if ( 
        URLFileSystem.isLocalFileSystemCaseSensitive() ? 
          baseName.equals( name )  :  baseName.equalsIgnoreCase( name ) )
      {
        // Ask the user if they want to overwrite the existing template.
        if ( !ask( "A template with the name \""+baseName+"\" already exists. Do you want to overwrite the existing template?") )
        {
          return true;
        }
      }
    }

    boolean isDirty = doc.isDirty();
    URL oldURL = doc.getURL();

    doc.setURL( URLFactory.newURL( getTemplateDirectory(), templateName ) );
    doc.markDirty( true );

    try
    {
      doc.save(true);
      // Interesting. The IDE seems to keep hold of the template URL until
      // it exits. Not sure why. I've tried doc.close(), but it didn't help :(
      // This means you can't delete templates that have just been created..
      // BUGBUG..
      addGalleryItem( doc.getURL(), true );
      m_templatesFolder.addTemplate( doc.getURL() );
    }
    catch (IOException ioe)
    {
      alert(
        "Failed to write template file."
      );
    }
    finally
    {
      // We want to do this even if a RuntimeException occurs, otherwise
      // the node ends up getting "accidentally" renamed :)
      doc.setURL( oldURL );
      doc.markDirty( isDirty );
    }



    return true;

  }

  /**
   * Utility method to strip the extension of a filename.
   *
   * @param base the base string with or without an extension
   * @return the filename of the URL without path or suffix
   */
  private String stripExtension( final String base )
  {
    int lastDot = base.lastIndexOf( '.' );
    if ( lastDot == -1 )
    {
      return base;
    }
    return base.substring( 0, lastDot );
  }

  /**
   * Iterate the URLs of all templates in the templates directory if it
   * exists.
   *
   * @return an iterator of URLs. May be empty.
   */
  private Iterator getTemplateURLs()
  {

    if ( !URLFileSystem.exists( getTemplateDirectory() ) )
    {
      return Collections.EMPTY_LIST.iterator(); 
    }
    
    return Arrays.asList( 
      URLFileSystem.list( getTemplateDirectory() )
    ).iterator();
  }

  /**
   * Get the template directory. 
   *
   * @return the template directory
   */
  private URL getTemplateDirectory()
  {
    return TEMPLATE_DIR_URL;
  } 
  
  /**
   * Utility to display an alert in the neat OLAF style
   */
  private void alert( final String message )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_ALERT
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( message );
    md.runDialog();
  }

  /** 
   * Ask the user a question.
   *
   * @param question the question to ask
   * @return true if the user responded Yes, false otherwise
   */
  private boolean ask( final String question )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_CONFIRMATION
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( question );
    return md.runDialog();
  }


  /**
   * Populate the object gallery with the templates category and items for
   * each template in the templates directory.
   */
  private void populateGallery()
  {
    ObjectGalleryAddin galleryAddin = (ObjectGalleryAddin) 
      Ide.getAddinManager().getAddin( 
        ObjectGalleryAddin.class
      );

    if ( galleryAddin == null )
    {
      Assert.println( 
        "The object gallery appears to be missing. You will be unable to create new files from templates"
      );
      return;
    }

    m_templatesGalleryFolder = new GalleryFolder( "Templates" );
    galleryAddin.getGallery().getModel().add( m_templatesGalleryFolder );

    Iterator galleryItemIterator = getTemplateURLs();

    while( galleryItemIterator.hasNext() )
    {
      addGalleryItem( (URL)galleryItemIterator.next(), false );
    }
  }

  /**
   * Add an item to the Templates gallery folder for the template at the
   * specified location.
   *
   * @param url the url of the template for the gallery
   * @param checkDuplicates whether to check for duplicates
   */
  private void addGalleryItem( final URL url, final boolean checkDuplicates )
  {
    String name = stripExtension(URLFileSystem.getFileName( url ));
    TemplateWizard wiz = new TemplateWizard( url, name );

    // Set the caster for the template. N.b. it's theoretically possible to 
    // use a different caster for each template, but there's no way to 
    // specify this in the UI (or store the preference for that matter) at 
    // the moment.
    wiz.setTemplateCaster( m_caster );

    if ( checkDuplicates )
    {
      // Check all existing gallery elements, make sure an item with this name
      // doesn't already exist.
      Iterator templates = m_templatesGalleryFolder.getChildren();
      while ( templates.hasNext() )
      {
        GalleryElement thisElement = (GalleryElement) templates.next();
        if ( name.equals( thisElement.getName() ) )
        {
          return;
        }
      }
    }

    // If we get here, the gallery item doesn't exist, so create it.
    GalleryElement element = new GalleryElement( wiz, name, null, 
      s_templateIcon );
    m_templatesGalleryFolder.add( element );
  }

  private void registerTemplatesNode()
  {
    // The IDE persists any top level nodes, which is a bit weird. Before we
    // construct our node and add it, we need to check to see if it already 
    // exists.
    Iterator systemKids = Ide.getSystem().getChildren();
    while ( systemKids.hasNext() )
    {
      Element e = (Element) systemKids.next();
      if ( e instanceof TemplatesFolder )
      {
        m_templatesFolder = (TemplatesFolder) e;
        // No need to do anything.
        return;
      }
    }
    m_templatesFolder = new TemplatesFolder();
    // Ok, this is the first time the addin has ever been initialized. 
    // construct a new FileSystemElement and add it to system.
    Ide.getSystem().add( m_templatesFolder );
    UpdateMessage.fireChildAdded( Ide.getSystem(), m_templatesFolder );
  }  
  
// ----------------------------------------------------------------------------
// Addin implementation 
// ----------------------------------------------------------------------------

  public void initialize()
  {
    // We always use the velocity template caster. Change this line to
    //
    // m_caster = new CopyTemplateCaster();
    //
    // to use simple template casting which just copies the template file
    // without parsing it through Velocity.
    m_caster = new VelocityTemplateCaster();
    createActionsAndMenus();
    Ide.getNavigatorManager().addContextMenuListener( this, null );
    populateGallery();
    registerTemplatesNode();

    m_templatesFolder.attach( this );
  }

  public void shutdown()
  {
    Ide.getNavigatorManager().removeContextMenuListener( this );

    m_caster = null;
  }

  public boolean canShutdown()
  {
    return true;
  }

  public float version()
  {
    return 0.2f;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

// ----------------------------------------------------------------------------
// ContextMenuListener implementation
// ----------------------------------------------------------------------------


  public boolean handleDefaultAction( Context context )
  {
    return false;
  }

  public void poppingUp( ContextMenu contextMenu )
  {
    if ( isValidSaveContext( contextMenu.getContext() ) )
    {
      contextMenu.add( contextMenu.createMenuItem( m_saveAsTemplateAction ) );
    }
  }

  public void poppingDown( ContextMenu contextMenu )
  {

  }

// ----------------------------------------------------------------------------
// BaseController overrides
// ----------------------------------------------------------------------------


  public boolean update( final IdeAction action, final Context context )
  {
    if ( action == m_saveAsTemplateAction )
    {
      m_saveAsTemplateAction.setEnabled(isValidSaveContext( context ));
      return true;
    }

    return super.update( action, context );

  }

  public boolean handleEvent( final IdeAction action, final Context context )
  {
    if ( action == m_saveAsTemplateAction && isValidSaveContext( context ) )
    {
      saveAsTemplate( context );
      return true;
    }

    return super.handleEvent( action, context );
  }
  
// ----------------------------------------------------------------------------
// Observer implementation
// ----------------------------------------------------------------------------

  public void update( Object changed, UpdateMessage change )
  {
    if ( changed == m_templatesFolder && 
      change.getMessageID() == change.CHILD_REMOVED )
    {
      // A template was removed (either from the file system or the IDE)
      // through the templates navigator. We need to remove it from the 
      // object gallery too..
      ArrayList removeGalleryItems = new ArrayList( 
        change.getRemoveObjects().size()
      );
      Iterator deleted = change.getRemoveObjects().iterator();      
      while ( deleted.hasNext() )
      {
        Iterator templates = m_templatesGalleryFolder.getChildren();
        while ( templates.hasNext() )
        {
          GalleryElement thisElement = (GalleryElement) templates.next();
          URL thisURL = ((TemplateWizard)thisElement.getWizard()).getURL();

          if ( thisURL.equals( ((TemplateNode)deleted.next()).getURL() ) )
          {
            removeGalleryItems.add( thisElement );   
          }
        }
      }

      Iterator i = removeGalleryItems.iterator();
      while ( i.hasNext() )
      {
        m_templatesGalleryFolder.remove( (GalleryElement) i.next() );
      }
    }
  }

}