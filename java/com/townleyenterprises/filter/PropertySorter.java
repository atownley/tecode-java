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
// Title:	PropertySorter.java
// Created: 	Sat May 17 14:48:06 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.text.Collator;
import java.text.CollationKey;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import com.townleyenterprises.common.PropertyProxy;

/**
 * This class sorts Comparable objects and orders them based on the
 * specified SortSpecifications.  The approximate performance
 * figures for a one and two property sort on a 1.4GHz Intel
 * Pentium M running at 600MHz under Linux and the Sun
 * 1.4.2_05 JVM were as follows:
 * <table width="60%" cellspacing="0" cellpadding="3">
 * <tr>
 * <th align="right">
 * Items
 * </th>
 * <th align="right">
 * Natural Order
 * </th>
 * <th align="right">
 * Locale Order
 * </th>
 * </tr>
 * <tr>
 * <td align="right">
 * 189 
 * </td>
 * <td align="right">
 * 0.090s
 * </td>
 * <td align="right">
 * 0.196s
 * </td>
 * </tr>
 * <tr>
 * <td align="right">
 * 1,512
 * </td>
 * <td align="right">
 * 0.200s
 * </td>
 * <td align="right">
 * 0.317s
 * </td>
 * </tr>
 * <tr>
 * <td align="right">
 * 3,024
 * </td>
 * <td align="right">
 * 0.821s
 * </td>
 * <td align="right">
 * 0.433s
 * </td>
 * </tr>
 * <tr>
 * <td align="right">
 * 48,384
 * </td>
 * <td align="right">
 * 4.580s
 * </td>
 * <td align="right">
 * 5.339s
 * </td>
 * </tr>
 * <tr>
 * <td align="right">
 * 96,768
 * </td>
 * <td align="right">
 * 9.391s
 * </td>
 * <td align="right">
 * 10.846s
 * </td>
 * </tr>
 * <tr>
 * <td align="right">
 * 193,536
 * </td>
 * <td align="right">
 * 12.509s
 * </td>
 * <td align="right">
 * 12.338s
 * </td>
 * </tr>
 * </table>
 *
 * @version $Id: PropertySorter.java,v 1.4 2004/12/06 00:56:35 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 2.0
 */

public final class PropertySorter extends PropertyProxy
		implements Comparator
{
	/**
	 * The constructor specifies the class of the objects to be
	 * sorted and the collation specifications to be used.
	 * <p>
	 * Using this version of the constructor, the values
	 * will be sorted in their <em>natural collation
	 * sequence</em> rather than based on a specific
	 * locale.
	 * </p>
	 *
	 * @param klass the class of the objects to be sorted
	 * @param spec the sort specification
	 */

	public PropertySorter(Class klass, SortSpecification[] spec)
	{
		super(klass);

		_props = spec;
	}

	/**
	 * The constructor specifies the class of the objects to be
	 * sorted and the collation specifications and a
	 * locale to be used for determining the collation
	 * order of string properties.
	 *
	 * @param klass the class of the objects to be sorted
	 * @param locale the Locale object to be used for
	 * 	string comparisons
	 * @param spec the sort specification
	 * @since 3.0
	 */

	public PropertySorter(Class klass, Locale locale, 
			SortSpecification[] spec)
	{
		super(klass);

		_props = spec;
		_collator = Collator.getInstance(locale);
	}

	public int compare(Object rhs, Object lhs)
	{
		int rc = -1;

		for(int i = 0; i < _props.length; ++i)
		{
			Object rval = getPropertyValue(_props[i].getProperty(), rhs);
			Object lval = getPropertyValue(_props[i].getProperty(), lhs);

			// special case if we have a non-null
			// collator instance
			if(_collator != null)
			{
				rval = checkKey(rval);
				lval = checkKey(lval);
			}

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

	/**
	 * This method is used to clear the hash map in order
	 * to optimize the garbage collection for the JVM.  It
	 * is not required that this method be called, but it
	 * may or may not make a difference to the overall
	 * memory footprint of your application.
	 *
	 * @since 3.0
	 */

	public void reset()
	{
		_map.clear();
	}

	/**
	 * This method takes care of generating collation keys
	 * for strings so that the next time around, the
	 * comparison will be quicker.
	 *
	 * @param foo the object to check
	 * @return the object if it isn't a string, otherwise,
	 * the collation key for the string
	 *
	 * @since 3.0
	 */

	private Object checkKey(Object foo)
	{
		Object rval = null;

		CollationKey key = (CollationKey)_map.get(foo);
		if(key == null && foo instanceof String)
		{
			key = _collator.getCollationKey((String)foo);
			_map.put(foo, key);
			rval = key;
		}
		else if(key == null)
		{
			rval = foo;
		}
		else
		{
			rval = key;
		}

		return rval;
	}

	private SortSpecification[] 	_props;
	private Collator		_collator = null;
	private Map			_map = new HashMap();
}
