//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003-2004, Andrew S. Townley
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
// File:	PropertyParser.java
// Created:	Thu Jan 22 18:20:34 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is a static utility class to perform parsing of property
 * values to primitive types.
 *
 * @version $Id: PropertyParser.java,v 1.3 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class PropertyParser
{
	/**
	 * The default constructor assumes the properties to parse come
	 * from the System class.
	 */

	public PropertyParser()
	{
		this(System.getProperties());
	}

	/**
	 * This constructor takes the properties containing the value
	 * to parse.
	 *
	 * @param props the properties
	 */

	public PropertyParser(Properties props)
	{
		_props = props;
	}

	/**
	 * This method is a more forgiving parser for boolean values.
	 * It accepts anything starting with <code>'y'</code> or
	 * <code>'t'</code> as true and anything else as false.
	 *
	 * @param name the name of the property to find
	 * @return the parsed boolean value or false if the property
	 * was not found
	 */

	public boolean booleanValue(String name)
	{
		String p = _props.getProperty(name);
		if(p != null && p.length() > 0 
				&& (p.toLowerCase().charAt(0) == 'y'
				|| p.toLowerCase().charAt(0) == 't'))
		{
			return true;
		}

		return false;
	}

	/** the properties we're using */
	private final Properties	_props;
}
