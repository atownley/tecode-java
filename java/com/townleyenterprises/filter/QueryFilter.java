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
// Title:	QueryFilter.java
// Created: 	Sat May 17 13:48:20 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.io.Serializable;
import com.townleyenterprises.common.PropertyProxy;

/**
 * This class provides an implementation of the Query Object design
 * pattern for the filtering mechanism which may be used for lazy
 * evaluation of the filter criteria.  This class is the basis for
 * using Filters with database systems as they can be easily
 * translated into SQL.
 *
 * @version $Id: QueryFilter.java,v 1.8 2004/12/04 17:28:16 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public class QueryFilter extends PropertyProxy
		implements Filter, Serializable
{
	/**
	 * The constructor initializes the query filter.  Each
	 * QueryFilter is fixed once it is constructed.
	 *
	 * @param klass the class of the object to be filtered
	 * @param property the property name based on the object being
	 * 	queried
	 * @param op the QueryOperator to be used
	 * @param val the Comparable to be used for comparison (must
	 */

	public QueryFilter(Class klass, String property,
				QueryOperator op, Comparable val)
	{
		super(klass);
		_prop = property;
		_op = op;
		_value = val;
	}

	/**
	 * @deprecated As of the 3.0 release, the {@link
	 * #execute} method should be used to more accurately
	 * reflect the relationship to the GoF Command
	 * pattern.  This method will be removed in a future
	 * version of the library.
	 */

	public boolean doFilter(Object o)
	{
		return execute(o);
	}

	public boolean execute(Object o)
	{
		boolean result = false;
		Comparable rez = (Comparable)getPropertyValue(_prop, o);
		if(rez == null)
			return false;

		int rc = rez.compareTo(_value);

		if(QueryOperator.LT.equals(_op))
		{
			result = (rc < 0);
		}
		else if(QueryOperator.GE.equals(_op))
		{
			result = (rc >= 0);
		}
		else if(QueryOperator.GT.equals(_op))
		{
			result = (rc > 0);
		}
		else if(QueryOperator.LE.equals(_op))
		{
			result = (rc <= 0);
		}
		else if(QueryOperator.EQ.equals(_op))
		{
			result = (rc == 0);
		}
		else if(QueryOperator.NE.equals(_op))
		{
			result = (rc != 0);
		}

		return result;
	}

	public String getProperty()
	{
		return _prop;
	}

	public QueryOperator getOperator()
	{
		return _op;
	}

	public Comparable getValue()
	{
		return _value;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer("( ");
		buf.append(getSubjectClass().getName());
		buf.append(".");
		buf.append(getProperty());
		buf.append(" ");
		
		QueryOperator op = getOperator();
		if(op == null)
		{
			buf.append(getOperatorString());
		}
		else
		{
			buf.append(op.toString());
		}
		
		buf.append(" ");
		Object val = getValue();
		if(val instanceof String)
		{
			buf.append("'");
			buf.append(val);
			buf.append("'");
		}
		else
		{
			buf.append(val);
		}
		buf.append(" )");

		return buf.toString();
	}

	/**
	 * This method may be overridden by custom query filter
	 * instances to return a string which describes the test they
	 * are performing if it is not one of the standard operators.
	 *
	 * @return the operator string
	 */
	
	protected String getOperatorString()
	{
		return _op.toString();
	}

	/** our property value */
	private final String		_prop;

	/** our operator value */
	private final QueryOperator	_op;

	/** our object value */
	private final Comparable	_value;
}
