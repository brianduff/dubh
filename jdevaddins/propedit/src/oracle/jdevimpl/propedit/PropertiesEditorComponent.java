package oracle.jdevimpl.propedit;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A UI component that is used to edit properties files
 *
 * @author Brian.Duff@oracle.com
 */
public class PropertiesEditorComponent extends JPanel
{
  private final JTable m_table = new JTable();

  public PropertiesEditorComponent()
  {
    setLayout( new BorderLayout() );
    add( new JScrollPane( m_table ), BorderLayout.CENTER );
  }
}