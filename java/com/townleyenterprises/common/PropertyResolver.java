//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2004, Andrew S. Townley
// All rights reserved.
// 
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 
//     * Redistributions of source code must retain the above
//     copyright notice, this list of conditions and the following
//     disclaimer.
// 
//     * Redistributions in binary form must reproduce the above
//     copyright notice, this list of conditions and the following
//     disclaimer in the documentation and/or other materials provided
//     with the distribution.
// 
//     * Neither the names Andrew Townley and Townley Enterprises,
//     Inc. nor the names of its contributors may be used to endorse
//     or promote products derived from this software without specific
//     prior written permission.  
// 
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.
//
// File:	PropertyResolver.java
// Created:	Wed Jan 28 19:29:32 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Properties;

/**
 * This is a decorator class for a java.util.Properties object to
 * provide an extra method for locating properties with a specified
 * prefix.
 *
 * @version $Id: PropertyResolver.java,v 1.1 2004/01/28 19:42:39 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class PropertyResolver
{
	/**
	 * The constructor initializes the instance with the reference
	 * to the properties.
	 *
	 * @param properties the Properties to decorate
	 */

	public PropertyResolver(Properties properties)
	{
		_props = properties;
	}

	/**
	 * This method is just a pass-through to the
	 * java.util.Properties.getProperties method.
	 *
	 * @param key the property key
	 * @return the property value or null if not present
	 */

	public String get(String key)
	{
		return _props.getProperty(key);
	}

	/**
	 * This method provides a way to get compound properties,
	 * joined by the <code>'.'</code> character.
	 *
	 * @param prefix the prefix for this property
	 * @param key the base property key
	 * @return the property value or null if not present
	 */

	public String get(String prefix, String key)
	{
		return _props.getProperty(prefix.concat(".").concat(key));
	}

	/** the properties object */
	private final Properties	_props;
}
