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
// Title:	Filters.java
// Created: 	Sat May 17 16:18:44 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class provides helper filter operations for collections to
 * both filter and optionally sort the results.
 *
 * @version $Id: Filters.java,v 1.1 2003/06/07 18:42:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public class Filters
{
	/** don't want instances */
	private Filters() {}

	/**
	 * This method is used to filter a given collection with the
	 * specified filter.
	 *
	 * @param collection the collection to filter
	 * @param filter the filter to be applied
	 * @return the results of the filter as a collection
	 */

	public static Collection filter(Collection collection, Filter filter)
	{
		ArrayList list = new ArrayList();
		
		FilteredIterator i = new FilteredIterator(collection.iterator(), filter);
		while(i.hasNext())
		{
			list.add(i.next());
		}

		return list;
	}

	/**
	 * This method will filter the given collection and then
	 * return a sorted list of the results.  The sort
	 * specification is provided to order the objects based on the
	 * properties and order indicated.
	 *
	 * @param collection the collection to filter
	 * @param filter the filter to be applied
	 * @param sort the sort specifications to apply
	 * @return the results of the filter as a List
	 */

	public static List filter(Collection collection, Filter filter,
				SortSpecification[] sort)
	{
		List list = (List)filter(collection, filter);

		if(list.size() == 0)
		{
			return list;
		}

		Class klass = list.get(0).getClass();
		Collections.sort(list, new PropertySorter(klass, sort));
		return list;
	}
}
