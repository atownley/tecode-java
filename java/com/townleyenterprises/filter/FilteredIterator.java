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
// Title:	FilteredIterator.java
// Created: 	Tue May 13 20:14:43 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.util.Iterator;

/**
 * This class provides a decorator to a regular Java iterator that can
 * perform filtering.
 *
 * @version $Id: FilteredIterator.java,v 1.2 2004/01/25 19:19:35 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 * @since 2.0
 */

public class FilteredIterator implements Iterator 
{
	/**
	 * The constructor takes a regular iterator and the filter
	 * object that should be applied.
	 *
	 * @param iterator a regular Iterator
	 * @param filter a Filter
	 */

	public FilteredIterator(Iterator iterator, Filter filter)
	{
		_iterator = iterator;
		_filter = filter;

		if(iterator.hasNext())
		{
			_next = iterator.next();

			_hasnext = _filter.doFilter(_next);
			while(!_hasnext && _iterator.hasNext())
			{
				_next = _iterator.next();
				_hasnext = _filter.doFilter(_next);
			}
		}
		else
		{
			_hasnext = false;
			_next = null;
		}
	}

	public boolean hasNext()
	{
		return _hasnext;
	}

	public Object next()
	{
		if(!_hasnext)
			return null;

		Object tmp = _next;

		// the figure out if there's another one
		_hasnext = false;
		while(!_hasnext && _iterator.hasNext())
		{
			_next = _iterator.next();
			_hasnext = _filter.doFilter(_next);
		}

		return tmp;
	}

	public void remove() throws UnsupportedOperationException,
				IllegalStateException
	{
		_iterator.remove();
	}

	/** our iterator */
	private final Iterator	_iterator;

	/** our filter */
	private final Filter	_filter;

	/** the next object */
	private Object		_next;

	/** private flag */
	private boolean		_hasnext = false;
}
