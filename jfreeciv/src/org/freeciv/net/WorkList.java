package org.freeciv.net;
// not sure this is the right package.

/**
 * Worklist structure
 *
 * @author Brian Duff
 * @since 1.10.0
 */
public class WorkList {
  private boolean m_isValid;
  private String m_name;
  private int[] m_ids;

  public WorkList(boolean isValid, String name, int[] ids)
  {
    m_isValid = isValid;
    m_name = name;
    m_ids = ids;
  }

  public boolean isValid()
  {
    return m_isValid;
  }

  public void setValid(boolean valid)
  {
    m_isValid = valid;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(String s)
  {
    m_name = s;
  }

  public int[] getIds()
  {
    return m_ids;
  }

  public void setIds(int[] i)
  {
    m_ids = i;
  }

  public boolean isEmpty()
  {
    return (m_ids == null || m_ids.length == 0);
  }

}















