//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003, Andrew S. Townley
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
// Title:	LogicalNotFilter.java
// Created: 	Tue May 13 20:09:31 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.Iterator;

/**
 * This class provides an implementation of the Filter interface that
 * implements the NOT operation.
 *
 * @version $Id: LogicalNotFilter.java,v 1.2 2003/06/08 19:58:31 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class LogicalNotFilter implements Filter
{
	/**
	 * The constructor takes the argument of the filter to negate.
	 *
	 * @param filter the filter
	 */

	public LogicalNotFilter(Filter filter)
	{
		_filter = filter;
	}

	/**
	 * This method actually performs the operation that will
	 * determine if the parameter object should be included in the
	 * "result" or not.
	 *
	 * @param o the object to be tested
	 * @return true if the object should be included in the
	 * 	result; false if the object should not
	 */

	public boolean doFilter(Object o)
	{
		return !(_filter.doFilter(o));
	}

	/**
	 * This method returns the filter to be negated.
	 */

	public Filter getFilter()
	{
		return _filter;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer("( !(");
		buf.append(_filter.toString());
		buf.append(" ) )");

		return buf.toString();
	}

	private final Filter	_filter;
}
