package org.gjt.abies;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;


public class VectorListModel extends AbstractListModel
{

	private ArrayList array = new ArrayList();

	public VectorListModel()
	{
	}

	public synchronized void clear()
	{
		int top = array.size();
		if ( top > 0 )
		{
			array.clear();
			fireIntervalRemoved(this,0,top-1);
		}
	}

	public synchronized void addAll(Collection c)
	{
		int bottom = array.size();
		int top = array.size() + c.size();
		if ( top != bottom )
		{
			array.addAll(c);
			fireIntervalAdded(this,bottom,top-1);
		}
	}

	public int getSize()
	{
		return array.size();
	}

	public Object getElementAt(int i)
	{
		return array.get(i);
	}

	public synchronized void replaceWith( Collection c )
	{
		clear();
      addAll(c);
	}

	public synchronized void compareAndReplace( Collection c )
	{
		if ( !array.equals(c) )
			replaceWith(c);
	}

}