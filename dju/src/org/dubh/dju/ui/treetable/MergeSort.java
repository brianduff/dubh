// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MergeSort.java,v 1.2 1999-03-22 23:37:19 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//
//  Copyright 1997, 1998 by Sun Microsystems, Inc.,
//  901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
//  All rights reserved.
//
//  This software is the confidential and proprietary information
//  of Sun Microsystems, Inc. ("Confidential Information").  You
//  shall not disclose such Confidential Information and shall use
//  it only in accordance with the terms of the license agreement
//  you entered into with Sun.
//
// ---------------------------------------------------------------------------
//   Original Author: Philip Milne, Sun Microsystems
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.ui.treetable;
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