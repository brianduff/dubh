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
import java.net.URL;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.IdeSubject;
import oracle.ide.addin.Observer;
import oracle.ide.addin.Subject;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.model.DefaultFolder;
import oracle.ide.model.ElementAttributes;
import oracle.ide.net.URLFileSystem;

/**
 * IDE system navigator top level folder which contains all defined templates.
 * This provides convenient access to templates in order to modify or delete
 * them. 
 *
 * @author Brian.Duff@oracle.com
 */
public final class TemplatesFolder extends DefaultFolder
    implements Subject
{
  private final IdeSubject m_delegateSubject = new IdeSubject();
  private final Icon m_icon;

  public TemplatesFolder()
  {
    getAttributes().set( ElementAttributes.NAVIGABLE );  
    URL u = TemplateMaker.TEMPLATE_DIR_URL;

    if ( URLFileSystem.exists( u ) )
    {
      URL[] childURLs = URLFileSystem.list( u );

      for ( int i=0; i < childURLs.length; i++ )
      {
        u = childURLs[ i ];
        add( new TemplateNode( u ) );
      }
    }

    ImageIcon icon = 
      new ImageIcon( getClass().getResource( "templatesfolder.png" ) );
    if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE )
    {
      m_icon = icon;
    }
    else
    {
      m_icon = null;
    }
  }

  /**
   * Called by TemplateMaker when the user creates a new template. Adds
   * the specified template to the list, and fires an update event
   */
  void addTemplate( URL templateURL )
  {
    // Check the node doesn't already exist.
    Iterator kids = getChildren();
    while ( kids.hasNext() )
    {
      TemplateNode tn = (TemplateNode) kids.next();
      if ( templateURL.equals( tn.getURL() ) )
      {
        return;
      }
    }

    TemplateNode newNode = new TemplateNode( templateURL );
    UpdateMessage.fireChildAdded( this, newNode );
  }

// ----------------------------------------------------------------------------
// DefaultFolder overrides
// ----------------------------------------------------------------------------

  public String getShortLabel()
  {
    return "Templates";
  }

  public Icon getIcon()
  {
    if ( m_icon != null )
    {
      return m_icon;
    }
    return super.getIcon();
  }

// ----------------------------------------------------------------------------
// Subject implementation
// ----------------------------------------------------------------------------

  public void attach(Observer observer)
  {
    m_delegateSubject.attach( observer );
  }


  public void detach(Observer observer)
  {
    m_delegateSubject.detach( observer );
  }

  public void notifyObservers(Object subject, UpdateMessage change)
  {
    m_delegateSubject.notifyObservers( subject, change );
  }  
}