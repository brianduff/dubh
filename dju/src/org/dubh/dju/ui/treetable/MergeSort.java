// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MergeSort.java,v 1.4 2001-02-11 02:52:12 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package org.dubh.dju.ui.treetable;
/**
 * An implementation of MergeSort, needs to be subclassed to provide a
 * comparator.
 *
 * @version %I% %G%
 *
 * @author Scott Violet
 */
public abstract class MergeSort extends Object {
    protected Object           toSort[];
    protected Object           swapSpace[];

    public void sort(Object array[]) {
   if(array != null && array.length > 1)
   {
       int             maxLength;

       maxLength = array.length;
       swapSpace = new Object[maxLength];
       toSort = array;
       this.mergeSort(0, maxLength - 1);
       swapSpace = null;
       toSort = null;
   }
    }

    public abstract int compareElementsAt(int beginLoc, int endLoc);

    protected void mergeSort(int begin, int end) {
   if(begin != end)
   {
       int           mid;

       mid = (begin + end) / 2;
       this.mergeSort(begin, mid);
       this.mergeSort(mid + 1, end);
       this.merge(begin, mid, end);
   }
    }

    protected void merge(int begin, int middle, int end) {
   int           firstHalf, secondHalf, count;

   firstHalf = count = begin;
   secondHalf = middle + 1;
   while((firstHalf <= middle) && (secondHalf <= end))
   {
       if(this.compareElementsAt(secondHalf, firstHalf) < 0)
      swapSpace[count++] = toSort[secondHalf++];
       else
      swapSpace[count++] = toSort[firstHalf++];
   }
   if(firstHalf <= middle)
   {
       while(firstHalf <= middle)
      swapSpace[count++] = toSort[firstHalf++];
   }
   else
   {
       while(secondHalf <= end)
      swapSpace[count++] = toSort[secondHalf++];
   }
   for(count = begin;count <= end;count++)
       toSort[count] = swapSpace[count];
    }
}