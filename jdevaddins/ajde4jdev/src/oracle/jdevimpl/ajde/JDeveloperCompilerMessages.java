package oracle.jdevimpl.ajde;

import org.aspectj.ajde.compiler.CompilerMessages;

import oracle.ide.layout.ViewId;
import oracle.ide.log.LogPage;
import oracle.ide.log.LogPipe;
import oracle.ide.log.DefaultLogPage;

/**
 * Implementation of CompilerMessages for JDeveloper
 */
public class JDeveloperCompilerMessages extends DefaultLogPage
  implements CompilerMessages
{
// TODO: Make this more sophisticated... and probably encapsulate rather than
// extend...

  private static final String LOG_PAGE_ID = "AJC_MESSAGES";
  private PrintWriter m_writer;

  public JDeveloperCompilerMessages()
  {
    super(
      new ViewId( LOG_PAGE_ID, "AJC Messages"), null, true
    );
  }

// -----------------------------------------------------------------------------
// CompilerMessages implementation
// -----------------------------------------------------------------------------
  public void addMessage(String message, Object icon, String file, int line)
  {
    m_writer.println( message );
  }

  public void clearMessages()
  {
    clearAll();
  }
}