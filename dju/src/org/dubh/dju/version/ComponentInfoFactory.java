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
 * This class contains static methods that can be used to vend ComponentInfo
 * instances.
 * <P>
 * @author Brian Duff
 */
public final class ComponentInfoFactory
{
   /**
    * Get the component info for a specific package. This uses the support
    * in java.lang.Package for getting version information from the
    * manifest.
    *
    * @param pkg a package to get component info for
    * @return a ComponentInfo implementation, initialized from the version
    *    information from the specified package.
    */
   public final static ComponentInfo getComponentInfo(Package pkg)
   {
      return new JARPackageComponentInfo(pkg);
   }
}

 