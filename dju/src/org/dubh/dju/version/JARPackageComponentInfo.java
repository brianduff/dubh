// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JARPackageComponentInfo.java,v 1.1 2001-02-11 15:38:22 briand Exp $
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
package org.dubh.dju.version;

/**
 * An implementation of the ComponentInfo interface that gets information for
 * a package from a JAR manifest. This is the recommended way to get information
 * for the Java runtime environment, for instance.
 *
 * @author Brian Duff
 */
class JARPackageComponentInfo extends AbstractComponentInfo
{
   /**
    * Construct a JARPackageComponentInfo instance for a named package
    *
    * @param pkgName the full name of a package to get version information
    *    from.
    */
   JARPackageComponentInfo(String pkgName)
   {
      this(Package.getPackage(pkgName));
   }

   JARPackageComponentInfo(Package pck)
   {
      if (pck == null)
      {
         setName("Unknown");
         setShortVersion("");
         return;
      }
      
      String specVendor = pck.getImplementationVendor();
      String specTitle = pck.getImplementationTitle();
      String specVersion = pck.getImplementationVersion();

      if (specVendor != null)
      {
         setVendor(specVendor);
      }
      else
      {
         setVendor("");
      }

      if (specVersion != null)
      {
         setShortVersion(specVersion);
         setLongVersion(specVersion);
      }
      else
      {
         setShortVersion("");
         setLongVersion("");
      }

      // If ImplementationTitle is not defined,
      if (specTitle == null)
      {
         // Try SpecificationTitle instead.
         specTitle = pck.getSpecificationTitle();
         if (specTitle == null)
         {
            // Failing that, just use the last part of the package name
            int lastDot = pck.getName().lastIndexOf('.');
            specTitle = pck.getName().substring(lastDot+1);
         }
      }
      setName(specTitle);
   }
}

 