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

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple list model with support for moving items up and down
 */
class SimpleListModel extends AbstractListModel
{
  private final List m_delegate = new ArrayList();

  public int getSize()
  {
    return m_delegate.size();
  }

  public Object getElementAt( int i )
  {
    return m_delegate.get( i );
  }

  public void addElement( Object o )
  {
    m_delegate.add( o );
    super.fireIntervalAdded( this, getSize()-1, getSize()-1 );
  }

  public void removeElement( Object o )
  {
    int idx = m_delegate.indexOf( o );
    m_delegate.remove( idx );
    super.fireIntervalRemoved( this, idx, idx );
  }

  public void removeAll()
  {
    int oldSize = getSize();
    if ( oldSize == 0 )
    {
      return;
    }
    m_delegate.clear();
    super.fireIntervalRemoved( this, 0, oldSize-1 );
  }

  public void changed( final int index )
  {
    super.fireContentsChanged( this, index, index );
  }

  public void moveUp( final int index )
  {
    if ( index > 0 )
    {
      final Object o = m_delegate.remove( index );
      super.fireIntervalRemoved( this, index, index );
      m_delegate.add( index - 1, o );
      super.fireIntervalAdded( this, index-1, index-1 );
    }
  }

  public void moveDown( final int index )
  {
    if ( index < getSize() - 1 )
    {
      final Object o = m_delegate.remove( index );
      super.fireIntervalRemoved( this, index, index );
      m_delegate.add( index + 1, o );
      super.fireIntervalAdded( this, index+1, index+1 );
    }
  }

  public void insertAt( int index, Object o )
  {
    m_delegate.add( index, o );
    super.fireIntervalAdded( this, index, index );
  }

}