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
 * The Original Code is Filesystem Browser addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.fsbrowser;

import java.util.Iterator;

import oracle.ide.IdeSubject;
import oracle.ide.addin.Observer;
import oracle.ide.addin.Subject;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.model.DefaultFolder;
import oracle.ide.model.ElementAttributes;


/**
 * AbstractLazyElement waits until getChildren() is called before populating
 * its children.
 *
 * @author Brian.Duff@oracle.com
 */
abstract class AbstractLazyElement extends DefaultFolder
  implements Subject
{
  private boolean m_isOpen;
  private final IdeSubject m_delegateSubject = new IdeSubject();

  protected AbstractLazyElement()
  {
    // Allow the "Display in new Navigator" command to work for this node
    getAttributes().set( ElementAttributes.NAVIGABLE );
    m_isOpen = false;
  }

  /**
   * Subclasses can use this to query if the node has been opened. This returns
   * true if getChildren() has been called.
   */
  protected final boolean isOpen()
  {
    return m_isOpen;
  }

  /**
   * Subclasses should implement this and use add() to add their children to
   * the folder.
   */
  protected abstract void loadChildren();

// DefaultFolder overrides

  /**
   * Override this to lazily populate
   */
  public final Iterator getChildren()
  {
    if ( !m_isOpen )
    {
      loadChildren();
      m_isOpen = true;
    }
    return super.getChildren();
  }  

  /**
   * Close the node and inform observers that the node has been collapsed
   */
  protected void close()
  {
    super.removeAll();
    UpdateMessage.fireObjectClosed( this );
    m_isOpen = false;
  }

  void refresh()
  {
    close();
    getChildren();

    UpdateMessage.fireObjectOpened( this );
    UpdateMessage.fireChildrenAdded( this, _children );
  }
  

// Subject implementation

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