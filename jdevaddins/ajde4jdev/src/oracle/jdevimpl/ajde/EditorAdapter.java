package oracle.jdevimpl.ajde;

import javax.swing.JPopupMenu;

import org.aspectj.ajde.editor.AjdeEditor;

import oracle.ide.Ide;
import oracle.ide.addin.Context;
import oracle.ide.addin.View;
import oracle.ide.cmd.SaveAllCommand;
import oracle.ide.editor.Editor;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.jdeveloper.ceditor.CodeEditor;
import oracle.jdeveloper.ceditor.CodeEditorGutter;

/**
 * Subclass of the AspectJ AjdeEditor for JDeveloper
 */
public class EditorAdapter extends AjdeEditor
{
  private static final String GUTTER_COLUMN = "AJDE Advice";

  /**
   * Seek to the specified line of the specified file, optionally highlighting
   * the specified line.
   */
  private void seekToSourceLine( final String file, final int lineNumber,
    final boolean highlight )
  {
    // Add to project?
    Ide.getEditorManager().openDefaultEditorInFrame(
      URLFactory.newFileURL( file )
    );
    showSourceLine( lineNumber, highlight );
  }


// ----------------------------------------------------------------------------
// AjdeEditor implementation
// ----------------------------------------------------------------------------

  public void showSourceLine(
    final String filePath, final int lineNumber, final boolean highlight)
  {
    seekToSourceLine( filePath, lineNumber, highlight );
  }

  public void showSourceLine( final int lineNumber, final boolean highlight)
  {
    final Editor theEditor = Ide.getEditorManager().getCurrentEditor();
    if ( theEditor instanceof CodeEditor )
    {
      ((CodeEdior)editor).gotoLine( line, 0, highlight );
    }
  }

  public void showSourcelineAnnotation(
    final String filePath, final int lineNumber, final JPopupMenu annotation)
  {
    showSourceLine( filePath, lineNumber, false );
    
    final Editor theEditor = Ide.getEditorManager().getCurrentEditor();
    if ( theEditor instanceof CodeEditor )
    {
      CodeEditorGutter gutter = ((CodeEditor)theEditor).getGutter();
      gutter.addColumn( GUTTER_COLUMN, 16, true );
      gutter.addGutterMark( GUTTER_COLUMN, lineNumber, 
        JDevIcons.getInstance().getRelationAdviceForwardIcon(), null );

      // **** JDEV Limitation *****
      // I think we need to popup a context menu on the gutter mark here? 
      // No way to do this in JDev (ok, maybe we could add a gutter click
      // listener and pop it up on the left button. Yeuch..)
    }
  }

  public String getCurrFile()
  {
    View v  = Ide.getMainWindow().getLastActiveView();
    Context ctx = (view == null ? null : view.getContext());
    if ( ctx != null )
    {
      Element e = ctx.getElement();
      if ( e instanceof Locatable )
      {
        return URLFileSystem.getPlatformPathName(
          ((Locatable)e).getURL()
        );
      }
    }
    return null;
  }

  public void saveContents() throws IOException
  {
    try
    {
      // Same as IDE save all command?
      SaveAllCommand.saveAll();
    }
    catch (Exception e )
    {
      throw new IOException( e.getMessage() );
    }
  }

  public void pasteToCaretPos(String text)
  {
    // Not implemented
  }

  public JPanel getPanel()
  {
    return null;  // ?
  }


  
}