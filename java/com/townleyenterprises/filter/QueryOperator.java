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
// Title:	QueryOperator.java
// Created: 	Sat May 17 13:52:59 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.io.Serializable;

/**
 * This class enumerates the operators supported by the QueryFilter.
 * The names of the operators are influenced by those supported by the
 * Unix <code>test</code> command.
 *
 * @version $Id: QueryOperator.java,v 1.1 2003/06/07 18:42:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public final class QueryOperator implements Comparable, Serializable
{
	private QueryOperator(String name)
	{
		_name = name;
	}

	public String toString()
	{
		return _name;
	}

	public int compareTo(Object o)
	{
		QueryOperator s = (QueryOperator)o;
		return _name.compareTo(s._name);
	}

	/** the name */
	private final String _name;

	/** greater than (&gt;) */
	public static final QueryOperator GT = new QueryOperator(">");

	/** less than (&lt;) */
	public static final QueryOperator LT = new QueryOperator("<");

	/** greater than or equal to (&gt;=) */
	public static final QueryOperator GE = new QueryOperator(">=");

	/** less than or equal to (&lt;=) */
	public static final QueryOperator LE = new QueryOperator("<=");

	/** equal to (=) */
	public static final QueryOperator EQ = new QueryOperator("=");

	/** not equal to (!=) */
	public static final QueryOperator NE = new QueryOperator("!=");
}
