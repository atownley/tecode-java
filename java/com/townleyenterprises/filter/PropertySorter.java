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
// Title:	PropertySorter.java
// Created: 	Sat May 17 14:48:06 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.Comparator;
import com.townleyenterprises.common.PropertyProxy;

/**
 * This class sorts Comparable objects and orders them based on the
 * specified SortSpecifications.
 *
 * @version $Id: PropertySorter.java,v 1.1 2003/06/07 18:42:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public final class PropertySorter extends PropertyProxy
		implements Comparator
{
	/**
	 * The constructor specifies the class of the objects to be
	 * sorted and the collation specifications to be used.
	 *
	 * @param klass the class of the objects to be sorted
	 * @param spec the sort specification
	 */

	public PropertySorter(Class klass, SortSpecification[] spec)
	{
		super(klass);

		_props = spec;
	}

	public int compare(Object rhs, Object lhs)
	{
		int rc = -1;

		for(int i = 0; i < _props.length; ++i)
		{
			Object rval = getPropertyValue(_props[i].getProperty(), rhs);
			Object lval = getPropertyValue(_props[i].getProperty(), lhs);

			if(rval instanceof Comparable &&
					lval instanceof Comparable)
			{
				rc = ((Comparable)rval).compareTo(lval);
			}
			else
			{
				if((rval != null && rval != null)
						&& rval.equals(lval))
				{
					rc = 0;
				}
				else if(rval == null && lval == null)
				{
					rc = 0;
				}
			}

			if(SortOrder.DESCENDING.equals(_props[i].getOrder()) && rc != 0)
			{
				rc = 0 - rc;
			}

			// if the values are equal, then
			// continue until they aren't
			
			if(rc != 0)
				break;
		}

		return rc;
	}
	
	private SortSpecification[] 	_props;
}
