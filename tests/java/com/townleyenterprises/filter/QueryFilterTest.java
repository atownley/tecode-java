//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2004, Andrew S. Townley
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
// File:	QueryFilterTest.java
// Created:	Sun Nov 28 19:39:03 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.math.BigDecimal;

/**
 * @version $Id: QueryFilterTest.java,v 1.1 2004/11/28 20:06:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class QueryFilterTest extends BaseFilterTest
{
	public QueryFilterTest(String testname)
	{
		super(testname);
	}

	public void testQueryFilterNE()
	{
		QueryFilter qf = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.NE,
				new Integer(200));

		assertEquals(true, qf.doFilter(_camaro));
	}

	public void testQueryFilterLT()
	{
		QueryFilter qf = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.LT,
				new Integer(200));
		QueryFilter qf2 = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.LT,
				new Integer(180));

		assertEquals(true, qf.doFilter(_camaro));
		assertEquals(false, qf2.doFilter(_camaro));
	}

	public void testQueryFilterGT()
	{
		QueryFilter qf = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.GT,
				new Integer(200));
		QueryFilter qf2 = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.GT,
				new Integer(100));

		assertEquals(false, qf.doFilter(_camaro));
		assertEquals(true, qf2.doFilter(_camaro));
	}

	public void testQueryFilterLE()
	{
		QueryFilter qf = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.LE,
				new Integer(180));
		QueryFilter qf2 = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.LE,
				new Integer(179));

		assertEquals(true, qf.doFilter(_camaro));
		assertEquals(false, qf2.doFilter(_camaro));
	}

	public void testQueryFilterGE()
	{
		QueryFilter qf = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.GE,
				new Integer(180));
		QueryFilter qf2 = new QueryFilter(Car.class,
				"topSpeed", QueryOperator.GE,
				new Integer(181));

		assertEquals(true, qf.doFilter(_camaro));
		assertEquals(false, qf2.doFilter(_camaro));
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(QueryFilterTest.class);
	}
}
