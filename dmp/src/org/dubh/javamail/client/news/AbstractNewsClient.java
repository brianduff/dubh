// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: AbstractNewsClient.java,v 1.3 2001-02-11 02:52:48 briand Exp $
//   Copyright (C) 1999 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1999 - 2001 Brian Duff
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


package org.dubh.javamail.client.news;

import java.util.ArrayList;
import java.util.Date;

import org.dubh.javamail.client.StoreClient;

/**
 * This is a test implementation of a news client that doesn't really
 * do much, but can be used to verify that the store code is working
 * properly.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: AbstractNewsClient.java,v 1.3 2001-02-11 02:52:48 briand Exp $
 */
public abstract class AbstractNewsClient implements NewsClient
{
   /**
    * When using getListNewsgroups(), an array list of objects
    * implementing this interface are returned.
    */
   protected class DefaultGroupInfo implements NewsClient.GroupInfo
   {
      private String  m_strGroupName;
      private int    m_lLow;
      private int    m_lHigh;
      private boolean m_bPostOK;

      public DefaultGroupInfo(String groupName, int loMark, int hiMark,
         boolean postingOK)
      {
         m_strGroupName = groupName;
         m_lLow = loMark;
         m_lHigh = hiMark;
         m_bPostOK = postingOK;
      }


      public String getGroupName()
      {
         return m_strGroupName;
      }

      public int getLowWaterMark()
      {
         return m_lLow;
      }

      public int getHighWaterMark()
      {
         return m_lHigh;
      }

      public boolean isPostingAllowed()
      {
         return m_bPostOK;
      }
   }


   /**
    * Find out what date the server thinks it is. This method is optional
    * an is allowed to return null.  The default implementation is to just
    * return todays date.
    *
    * @return a date object representing the date the server thinks it is.
    */
   public Date getDate()
      throws NewsClientException
   {
      return new Date();
   }

   public ArrayList getOverviewFormat()
      throws NewsClientException
   {
      return null;
   }


   public String getGroupDescription(String grpName)
      throws NewsClientException
   {
      return null;
   }


   public ArrayList getOverview(long lowRange, long hiRange)
      throws NewsClientException
   {
      return null;
   }


}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  2000/06/14 21:33:01  briand
// Added support for progress monitoring. Numerous fixes & upgrades.
//
// Revision 1.1  2000/02/22 23:47:35  briand
// News client implementation initial revision.
//
//
