//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Andrew S. Townley
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
// File:	CountingIteratorTest.java
// Created:	Thu Jan 22 22:31:55 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * Basic unit tests for the CountingIterator class
 *
 * @version $Id: CountingIteratorTest.java,v 1.1 2004/01/25 19:39:18 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class CountingIteratorTest extends TestCase
{
	public CountingIteratorTest(String testname)
	{
		super(testname);
	}

	public void testHasNextZeroCountNonzeroList()
	{
		List list = new ArrayList();
		for(int i = 0; i < 10; ++i)
		{
			list.add(new Integer(i));
		}

		CountingIterator ci = new CountingIterator(list.iterator(), 0);
		assertEquals(false, ci.hasNext());
	}

	public void testHasNextZeroCountZeroList()
	{
		List list = new ArrayList();
		CountingIterator ci = new CountingIterator(list.iterator(), 0);
		assertEquals(false, ci.hasNext());
	}

	public void testHasNextNonzeroCountZeroList()
	{
		List list = new ArrayList();
		CountingIterator ci = new CountingIterator(list.iterator(), 5);
		assertEquals(false, ci.hasNext());
	}

	public void testHasNextNonzeroCountRestrictedList()
	{
		final int count = 5;
		List list = new ArrayList();
		for(int i = 0; i < 10; ++i)
		{
			list.add(new Integer(i));
		}

		CountingIterator ci = new CountingIterator(list.iterator(), count);
		int items = 0;
		while(ci.hasNext())
		{
			ci.next();
			++items;
		}
		assertEquals(count, items);
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(CountingIteratorTest.class);
	}
}
