package oracle.jdevimpl.openfiles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import oracle.ide.Ide;
import oracle.ide.IdeContext;
import oracle.ide.IdeUIManager;
import oracle.ide.addin.Context;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorInfo;
import oracle.ide.editor.EditorListener;
import oracle.ide.model.Element;

/**
 * The OpenFiles UI component is a simple JList containing items for each open
 * editor in the IDE. 
 *
 * This class implements EditorListener and listens for editors being
 * opened, closed, activated and deactivated. When these events occur, the
 * list and the list selection are updated.
 *
 * @author Brian.Duff@oracle.com
 */
class OpenFiles implements EditorListener
{
  private JList m_list;
  private Component m_component;
  private boolean m_isActive = true;

  OpenFiles()
  {
    m_list = new JList() {
      public String getToolTipText( MouseEvent me )
      {
        int index = locationToIndex( me.getPoint() );
        if ( index >= 0 )
        {
          return 
            ((Editor)m_list.getModel().getElementAt( index )).getContext().getDocument().getURL().toString();
        }

        return null;
      }
    };
    m_list.setModel( new OpenFilesListModel() );
    m_list.setCellRenderer( new EditorListRenderer() );
    m_list.addMouseListener( new EditorListMouseListener() );
    m_list.setToolTipText( " " );  // Have to do this to get tooltips to appear
    m_component = new JScrollPane( m_list );
  }

  /**
   * Set whether the component is active. Only used so the list cell renderer
   * can use the correct selection color when the component does not have
   * focus.
   *
   * @param isActive whether the view this component is in is currently active
   */
  void setActive( final boolean isActive )
  {
    m_isActive = isActive;
  }
  

  /**
   * Get the actual UI component
   *
   * @return a UI component
   */
  Component getUI()
  {
    return m_component;
  }

  /**
   * Get the context. This delegates through to the editor context and
   * will return null if there is no selection.
   */
  Context getContext()
  {
    Editor e = (Editor) m_list.getSelectedValue();
    if ( e != null )
    {
      return e.getContext();
    }

    return null;
  }

  private void activateEditor( int index )
  {
    Editor e = (Editor) m_list.getModel().getElementAt( index );
    // Is there a better way to do this??? If not, bug the addin api
    Ide.getEditorManager().activateEditor( 
      (EditorInfo)Ide.getEditorManager().getEditorFrame( e )
    );
  }

  private void showPopupMenu( MouseEvent event )
  {
    // Show the navigator's context menu here, because it's actually quite
    // useful.

    // A bit horrible. Editors use documents and the navigator uses a selection.
    // So the context menu ends up being a bit different from the 
    // navigator's.
    // TODO: pass multi selection through.
    IdeContext newCtx = new IdeContext( getContext() );
    newCtx.setSelection( new Element[] { getContext().getDocument() } );
    newCtx.setEvent( event );
    
    Ide.getNavigatorManager().getSystemNavigator().getContextMenu().
      show( newCtx );
  }
  

  // EditorListener implementation

  public void editorActivated( final Editor editor )
  {
    final int index =
      ((OpenFilesListModel)m_list.getModel()).find( editor );
    if ( index >= 0 )
    {
      m_list.setSelectedIndex( index );
    }
  }

  public void editorClosed( final Editor editor )
  {
    ((OpenFilesListModel)m_list.getModel()).remove( editor );
    m_list.setSelectedIndex( -1 );
  }

  public void editorDeactivated( final Editor editor )
  {
    // NOOP
  }

  public void editorOpened( final Editor editor )
  {
    final int index = 
      ((OpenFilesListModel)m_list.getModel()).add( editor );
    m_list.setSelectedIndex( index );
    
  } 

// Inner classes

  /**
   * Listen for double clicks ( editor activate ) and popup triggers.
   */
  private class EditorListMouseListener extends MouseAdapter
  {
    public void mouseClicked( MouseEvent me )
    {
      int index = m_list.locationToIndex( me.getPoint() );
      if ( index < 0 )
      {
        return;
      }
      activateEditor( index );
    }
   

    public void mouseReleased( MouseEvent me )
    {
      if ( me.isPopupTrigger() )
      {
        int index = m_list.locationToIndex( me.getPoint() );
        if ( index >= 0 )
        {
          // We only change the selection if the index under the mouse is
          // not already selected. This is to avoid clearing multi selections.
          if ( !m_list.isSelectedIndex( index ) )
          {
            m_list.setSelectedIndex( index );
          }
          // And then show the popup menu
          showPopupMenu( me );
        }
      }
    }
  }

  /**
   * List renderer
   */
  private class EditorListRenderer extends DefaultListCellRenderer
  {
    private final Color m_inactiveBackground;
    private final Color m_inactiveText;

    private EditorListRenderer()
    {
      m_inactiveBackground = UIManager.getColor(
        IdeUIManager.TREE_INACTIVE_SELECTION_BACKGROUND_KEY
      );
      m_inactiveText = UIManager.getColor(
        IdeUIManager.TREE_INACTIVE_SELECTION_FOREGROUND_KEY
      );
    }
  
    public Component getListCellRendererComponent( JList list, Object value, 
      int index, boolean isSelected, boolean cellHasFocus )
    {
      Component c = super.getListCellRendererComponent( list, value, index,
        isSelected, cellHasFocus
      );

      if( value instanceof Editor )
      {
        setText(
          ((Editor)value).getContext().getDocument().getShortLabel()
        );
        setIcon(
          ((Editor)value).getTabIcon()
          // ((Editor)value).getContext().getDocument().getIcon()
        );
      }

      // Paint a different selection color when the dockable does not have
      // focus, like the other dockables in the IDE
      if ( isSelected && !m_isActive )
      {
        if ( m_inactiveBackground != null && m_inactiveText != null )
        {
          this.setBackground( m_inactiveBackground );
          this.setForeground( m_inactiveText );
        }
      }
      
      return c;
    }
  }

  /**
   * A comparator for sorting which compares the title label of two
   * Editor objects.
   *
   * Must only be used to sort collections in which all the items in the
   * collection implement Editor
   *
   */
  private static class EditorNameComparator implements Comparator
  {
    private final Comparator m_delegate = Collator.getInstance();

    public int compare( Object o1, Object o2 )
    {
      return m_delegate.compare( 
        ((Editor)o1).getTitleLabel(), 
        ((Editor)o2).getTitleLabel()
      );
    }
  }

  /**
   * A list model that sorts itself as items are added
   */
  private static class OpenFilesListModel extends AbstractListModel
  {
    private final List m_delegate = new ArrayList();
    private final Comparator m_comparator = 
      new EditorNameComparator();

    /**
     * Add an editor to the list at the correct position to keep the list
     * sorted.
     *
     * @param editor the editor to add
     * @return the index the editor was added at, or the index of the 
     *    editor if it is already in the list.
     */
    private int add( final Editor editor )
    {
      // Very useful collections method. We can determine whether the editor
      // is already in the list and if not, where it should be inserted in order
      // to keep the list sorted.
      int index = Collections.binarySearch(
        m_delegate, editor, m_comparator
      );

      if ( index >= 0 )
      {
        // An editor *with the same display name* is already in the list.
        // it may or may not be the same actual element. Let's check.
        if ( !editor.equals( m_delegate.get( index ) ) )
        {
          // Only if it's *not* the same element, we insert it after the
          // element with the same name.
          m_delegate.add( ++index, editor );
          fireIntervalAdded( this, index, index );
          
          return index;
        }
      }

      // According to the javadoc of Collections.binarySearch, the index is
      // (-(insertion point) - 1)
      final int insertionPoint = Math.abs( index+1 );
      m_delegate.add( insertionPoint, editor );
      fireIntervalAdded( this, insertionPoint, insertionPoint );
        
      return insertionPoint;

    
    }

    /**
     * Remove an editor
     */
    private void remove( final Editor editor )
    {
      final int index = m_delegate.indexOf( editor );
      if ( index >= 0 )
      {
        m_delegate.remove( editor );
        fireIntervalRemoved( this, index, index );
      }
    }

    /**
     * Find the specified element in the list. If it could not be found, 
     * return -1.
     */
    private int find( final Editor ed )
    {
      return Collections.binarySearch( m_delegate, ed );
    }


    /**
     * Get the index of an editor in the list
     *
     * @param editor the editor to look up
     * @return the index of the element, or -1 if it's not in the list
     */
    private int getIndexOf( final Editor editor )
    {
      return m_delegate.indexOf( editor );
    }

    // AbstractListModel implementation

    public int getSize()
    {
      return m_delegate.size();
    }

    public Object getElementAt( final int index )
    {
      return m_delegate.get( index );
    }



  }

}