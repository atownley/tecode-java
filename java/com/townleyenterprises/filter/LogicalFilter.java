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
// Title:	LogicalFilter.java
// Created: 	Wed May  7 17:57:59 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This package class provides the base operation common to all logical
 * comparison filters within the system.
 *
 * @version $Id: LogicalFilter.java,v 1.5 2004/12/04 17:28:16 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public abstract class LogicalFilter implements Filter
{
	/**
	 * The constructor is used simply to provide an operator
	 * string description which is used in the toString() method.
	 *
	 * @param op the operator string which should be displayed
	 */

	protected LogicalFilter(String op)
	{
		_opstr = op;
	}

	/**
	 * Adds a new filter to this compound filter.
	 *
	 * @param f the new filter
	 */

	public void addFilter(Filter f)
	{
		_filters.add(f);
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

	public abstract boolean execute(Object o);

	/**
	 * This method actually performs the operation that will
	 * determine if the parameter object should be included in the
	 * "result" or not.
	 *
	 * @param o the object to be tested
	 * @return true if the object should be included in the
	 * 	result; false if the object should not
	 * @deprecated  As of the 3.0 release, the {@link
	 * #execute} method shold be used instead.  This
	 * method will be removed in a future version of the
	 * library.
	 */

	public boolean doFilter(Object o)
	{
		return execute(o);
	}

	/**
	 * This method returns the number of filters in the compound
	 * filter.
	 *
	 * @return the filter count
	 */

	public int getFilterCount()
	{
		return _filters.size();
	}

	/**
	 * This method is used to clear all of the filters in this
	 * instance.
	 */

	public void clear()
	{
		_filters.clear();
	}

	/**
	 * Returns an iterator over the filters in this filter.
	 *
	 * @return an Iterator
	 */

	public Iterator iterator()
	{
		return _filters.iterator();
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer("(");

		for(Iterator i = iterator(); i.hasNext();)
		{
			buf.append(" ");
			buf.append(i.next());
			if(i.hasNext())
			{
				buf.append(" ");
				buf.append(_opstr);
			}
		}

		buf.append(" )");

		return buf.toString();
	}

	/** our operator string */
	private final String		_opstr;

	/** the list of filters to be executed over the object */
	protected final ArrayList	_filters = new ArrayList();
}
