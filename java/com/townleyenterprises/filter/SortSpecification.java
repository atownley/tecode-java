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
// Title:	SortSpecification.java
// Created: 	Sat May 17 14:15:15 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.io.Serializable;

/**
 * This class encapsulates a collating specification, allowing sorting
 * by arbitrary property values.
 *
 * @version $Id: SortSpecification.java,v 1.1 2003/06/07 18:42:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public final class SortSpecification implements Serializable
{
	/**
	 * The constructor takes the name of the property to sort
	 * in ascending order.
	 *
	 * @param property the property name to be sorted
	 */

	public SortSpecification(String property)
	{
		_property = property;
		_order = SortOrder.ASCENDING;
	}

	/**
	 * The constructor takes the name of the property to sort
	 * and the order which should be applied.
	 *
	 * @param property the property name to be sorted
	 * @param order the sort order
	 */

	public SortSpecification(String property, SortOrder order)
	{
		_property = property;
		_order = order;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer(_property);
		buf.append(" ");
		buf.append(_order.toString());

		return buf.toString();
	}

	public int compareTo(Object o)
	{
		SortSpecification s = (SortSpecification)o;
		int rc = _property.compareTo(s._property);
		if(rc != 0)
			return rc;

		return _order.compareTo(s._order);
	}

	public String getProperty()
	{
		return _property;
	}

	public SortOrder getOrder()
	{
		return _order;
	}

	private final String 	_property;
	private final SortOrder	_order;
}
