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
// Title:	SubstringFilter.java
// Created: 	Sat May 17 13:48:20 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

/**
 * This class extends StringFilter to allow substring checks to
 * determine if the value is contained in the value of the object to
 * which it is applied.  It supports both case sensitive and case
 * insensitive searching.
 *
 * @version $Id: SubstringFilter.java,v 1.4 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public class SubstringFilter extends StringFilter
{
	/**
	 * The constructor takes the property and the string value
	 * which should be compared.  Using this constructor will
	 * result in case sensitive comparisons.
	 *
	 * @param klass the class of the object to be filtered
	 * @param property the property name
	 * @param value the string value
	 */

	public SubstringFilter(Class klass, String property,
				String value)
	{
		super(klass, property, value, false);
	}

	/**
	 * The constructor takes the property and the string value
	 * which should be compared.
	 *
	 * @param klass the class of the object to be filtered
	 * @param property the property name
	 * @param value the string value
	 * @param ignorecase controls search case sensitivity
	 */

	public SubstringFilter(Class klass, String property,
				String value, boolean ignorecase)
	{
		super(klass, property, value, ignorecase);
	}

	public boolean doFilter(Object o)
	{
		String s = (String)getPropertyValue(getProperty(), o);
		String val = (String)getValue();

		if(s == null && val == null)
		{
			return true;
		}
		else if((s == null && val != null) ||
				(s != null && val == null))
		{
			return false;
		}

		if(getIgnoreCase())
		{
			s = s.toLowerCase();
			val = val.toLowerCase();
		}

		if(s.indexOf(val) != -1)
			return true;

		return false;
	}

	protected String getOperatorString()
	{
		return "LIKE";
	}
}
