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
// Title:	StringFilter.java
// Created: 	Sun Jun  8 20:19:32 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

/**
 * This is a specialized instance of the QueryFilter class which
 * directly supports filtering Strings.  It will perform case
 * sensitive or case insensitive string comparisons with the target
 * objects, depending on how the object is configured when it is
 * created.
 *
 * @version $Id: StringFilter.java,v 1.2 2004/01/25 19:19:48 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public class StringFilter extends QueryFilter
{
	/**
	 * The constructor takes the property and the string value
	 * which should be compared.
	 *
	 * @param klass the class of the object to be filtered
	 * @param property the property name
	 * @param value the string value
	 * @param ignorecase controls case sensitivity of the
	 * 	comparison operation
	 */

	public StringFilter(Class klass, String property,
				String value, boolean ignorecase)
	{
		super(klass, property, QueryOperator.EQ, value);
		_ignorecase = ignorecase;
	}

	public boolean doFilter(Object o)
	{
		String s = (String)getPropertyValue(getProperty(), o);
		String val = (String)getValue();

		// checks for null
		if(s == null && val == null)
		{
			return true;
		}
		else if((s == null && val != null) ||
				(s != null && val == null))
		{
			return false;
		}

		if(_ignorecase)
		{
			return s.equalsIgnoreCase(val);
		}

		return s.equals(val);
	}

	public boolean getIgnoreCase()
	{
		return _ignorecase;
	}

	private final boolean _ignorecase;
}
