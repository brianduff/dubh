/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please note that this software is not in any way endorsed by
 * Oracle Corporation
 * Version History:
 *  FV   DUV    Date          Who    What
 *  ======================================================================
 *  0.0  0.0.01 [22/Nov/1998] BD     Initial Revision
 *
 */

package dubh.utils.misc;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;

/**
 * <p>
 * Represents a product's name, version and copyright information.
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 0.0.01) [22/Nov/1998]
 */
public class Version implements Serializable, ReadOnlyVersion
{
   protected final static String SHORT_FORMAT = "{3}.{4}.{5} (build {6})";
   protected final static String LONG_FORMAT  = "{0} {3}.{4}.{5} (build {6} [{2}] - {7}) {1}";


   protected int m_major;
   protected int m_minor;
   protected int m_micro;
   
   protected int m_build;
   
   protected String m_buildLabel;
   
   protected String m_productName;
   protected String m_productCopyright;
   protected Date   m_releaseDate;
   
   public Version()
   {
      m_buildLabel="unknown";
      m_releaseDate = new Date();
      m_productName = "Unknown Product";
      m_productCopyright = "(C) 1999 Brian Duff";
      m_major = 0;
      m_minor = 0;
      m_micro = 0;
      m_build = 0;
   }
   
   public void setMajorVersion(int i) { m_major = i; }
   public int  getMajorVersion()      { return m_major; }
   
   public void setMinorVersion(int i) { m_minor = i; }
   public int  getMinorVersion()      { return m_minor; }
   
   public void setMicroVersion(int i) { m_micro = i; }
   public int  getMicroVersion()      { return m_micro; }
   
   public void setBuildNumber(int i)  { m_build = i; }
   public int  getBuildNumber()       { return m_build; }
   
   public void setBuildLabel(String s){ m_buildLabel = s; }
   public String getBuildLabel()      { return m_buildLabel; }
   
   public void setProductName(String s) { m_productName = s; }
   public String getProductName()       { return m_productName; }
   
   public void setProductCopyright(String s) { m_productCopyright = s; }
   public String getProductCopyright()       { return m_productCopyright; }
   
   public void setReleaseDate(Date d)        { m_releaseDate = d; }
   public Date getReleaseDate()              { return m_releaseDate; }

   /**
    * Get a description of the version in a given format. The format can
    * contain any characters; the following placeholders are replaced with
    * the relevant version information:
    * <table>
    * <tr><td>{0}</td><td>Product Name</td></tr>
    * <tr><td>{1}</td><td>Product Copyright</td></tr>
    * <tr><td>{2}</td><td>Product Release Date</td></tr>
    * <tr><td>{3}</td><td>Product Major Version</td></tr>
    * <tr><td>{4}</td><td>Product Minor Version</td></tr>
    * <tr><td>{5}</td><td>Product Micro Version</td></tr>
    * <tr><td>{6}</td><td>Product Build Number</td></tr>
    * <tr><td>{7}</td><td>Product Build Label</td></tr>
    * </table>
    * E.g. a format string of <code>"{0} version {3}.{4}.{5} (build {6} - {7}) {1}"</code>
    * might result in an output string of <code>"NewsAgent version 1.0.1 (build 23 - optimized) (C) 1998 Brian Duff"</code>
    */
   public String getVersionDescription(String format)
   {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
      return MessageFormat.format(format, new Object[]
         { m_productName, m_productCopyright, df.format(m_releaseDate), 
           new Integer(m_major), new Integer(m_minor), new Integer(m_micro), 
           new Integer(m_build), m_buildLabel
         }
      );
   }
   
   /**
    * Get a description of the version in the default short format.
    * This is <code>{3}.{4}.{5} (build {6})</code>
    * @see getVersionDescription
    */
   public String getShortDescription()
   {
      return getVersionDescription(SHORT_FORMAT);
   }
   
   /**
    * Get a description of the version in the default long format.
    * This is <code>{0} version {3}.{4}.{5} (build {6} [{2}] - {7}) {1}</code>
    */
   public String getLongDescription()
   {
      return getVersionDescription(LONG_FORMAT);
   }
   
   public static void main(String[] args)
   {
      Version test = new Version();
      test.setProductName("NewsAgent");
      test.setProductCopyright("(C) 1998 Brian Duff");
      test.setBuildLabel("Optimized Development");
      test.setBuildNumber(235);
      test.setMajorVersion(1);
      test.setMinorVersion(0);
      test.setMicroVersion(1);
      test.setReleaseDate(new Date());
      
      System.out.println(test.getLongDescription());
      System.out.println(test.getShortDescription());
   
   
   }
   
}