// $Id: DAVProperty.java,v 1.1.1.1 2000-09-17 17:38:14 briand Exp $
// Copyright (c) 2000 Brian Duff 
package org.dubh.jdav;

/**
 * Represents a property of a resource in WebDAV. This is an abstract supertype:
 * properties are actually one of the standard WebDAV property types or
 * a DAVUserDefinedProperty, which can be any old DOM Node.
 * <P>
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org<a>)
 */
public abstract class DAVProperty
{
   public abstract String getName();

   public abstract Object getValue();
}

 