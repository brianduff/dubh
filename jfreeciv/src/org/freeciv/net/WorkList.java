package org.freeciv.net;
// not sure this is the right package.

import org.freeciv.common.CommonConstants;
import org.freeciv.util.Enum;

/**
 * Worklist structure
 *
 * @author Brian Duff, Ben Mazur
 * @since 1.10.0
 */
public class WorkList implements CommonConstants
{
  // enum worklist_elem_flag
  public static final int WEF_END = Enum.start();	/* element is past end of list */
  public static final int WEF_UNIT = Enum.get(); /* element specifies a unit to be built */
  public static final int WEF_IMPR = Enum.get(); /* element specifies an improvement to be built */
  public static final int WEF_LAST = Enum.get(); /* leave this last */
  
  private boolean m_isValid;
  private String m_name;
  private int[] m_efs;
  private int[] m_ids;
  /**
   * Constructs an empty worklist with default values
   */
  public WorkList() 
  {
    m_isValid = true;
    m_name = "a worklist";
    m_efs = new int[ MAX_LEN_WORKLIST ];
    m_ids = new int[ MAX_LEN_WORKLIST ];
    for( int i = 0; i < MAX_LEN_WORKLIST; i++ )
    {
      m_efs[ i ] = WEF_END;
      m_ids[ i ] = 0;
    }
  }
  public WorkList( boolean isValid, String name, int[] efs, int[] ids ) 
  {
    m_isValid = isValid;
    m_name = name;
    m_efs = efs;
    m_ids = ids;
  }
  public boolean isValid()
  {
    return m_isValid;
  }
  public void setValid( boolean valid )
  {
    m_isValid = valid;
  }
  public String getName()
  {
    return m_name;
  }
  public void setName( String s )
  {
    m_name = s;
  }
  public int[] getIds()
  {
    return m_ids;
  }
  public void setIds( int[] i )
  {
    m_ids = i;
  }
  public int[] getEfs()
  {
    return m_efs;
  }
  public void setEfs( int[] i )
  {
    m_efs = i;
  }
  public boolean isEmpty()
  {
    return ( m_ids == null || m_ids.length == 0 );
  }
}
