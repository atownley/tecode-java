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
// File:	CountingIterator.java
// Created:	Sat May 17 13:37:23 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.Iterator;

/**
 * This class provides a very simple counting decorator for the
 * java.util.Iterator class.  It is useful for restricting the amount
 * of data retrieved without actually having to track this in
 * application code.
 *
 * @version $Id: CountingIterator.java,v 1.3 2004/07/28 10:33:58 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class CountingIterator implements Iterator
{
	/**
	 * The constructor initializes the decorator with the number
	 * of elements which should be returned.
	 *
	 * @param i the iterator to decorate
	 * @param c the number of elements to return
	 */

	public CountingIterator(Iterator i, int c)
	{
		_iterator = i;
		_elements = c;
	}

	public boolean hasNext()
	{
		if((_elements == -1) || (_count < _elements))
			return _iterator.hasNext();

		return false;
	}

	public Object next()
	{
		++_count;
		return _iterator.next();
	}

	public void remove() throws UnsupportedOperationException,
				IllegalStateException
	{
		_iterator.remove();
	}

	private final Iterator	_iterator;
	private final int	_elements;
	private int		_count;
}
