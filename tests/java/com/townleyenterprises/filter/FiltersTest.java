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
// File:	FiltersTest.java
// Created:	Sun Nov 28 16:39:52 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * This class provides more substantial filter tests which
 * link all of the other bits together.
 *
 * @version $Id: FiltersTest.java,v 1.1 2004/11/28 20:06:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class FiltersTest extends BaseFilterTest
{
	public FiltersTest(String testname)
	{
		super(testname);
	}

	public void testSort()
	{
		SortSpecification[] sort = {
			new SortSpecification("make")
		};

		List results = Filters.filter(_cars, _black, sort);
		assertEquals(3, results.size());
		assertEquals("Camaro", ((Car)results.get(0)).getModel());
		assertEquals("black", ((Car)results.get(0)).getColor());
		assertEquals("Testarossa", ((Car)results.get(1)).getModel());
		assertEquals("black", ((Car)results.get(1)).getColor());
		assertEquals("W2", ((Car)results.get(2)).getModel());
		assertEquals("black", ((Car)results.get(2)).getColor());

	}

	public void testSortDesc()
	{
		SortSpecification[] sort = {
			new SortSpecification("make", SortOrder.DESCENDING)
		};

		List results = Filters.filter(_cars, _black, sort);
		assertEquals(3, results.size());
		assertEquals("Camaro", ((Car)results.get(2)).getModel());
		assertEquals("black", ((Car)results.get(2)).getColor());
		assertEquals("Testarossa", ((Car)results.get(1)).getModel());
		assertEquals("black", ((Car)results.get(1)).getColor());
		assertEquals("W2", ((Car)results.get(0)).getModel());
		assertEquals("black", ((Car)results.get(0)).getColor());

	}

	public void testSortEmptyCollection()
	{
		List results = Filters.filter(Collections.EMPTY_LIST, _black, new SortSpecification[0]);
		assertEquals(Collections.EMPTY_LIST, results);
	}

	public void testComplexSortedFilter()
	{
		LogicalOrFilter color = new LogicalOrFilter();
		color.addFilter(_red);
		color.addFilter(_black);
		LogicalAndFilter and = new LogicalAndFilter();
		and.addFilter(_speed);
		and.addFilter(color);

		SortSpecification[] sort = {
			new SortSpecification("make"),
			new SortSpecification("model")
		};

		List results = Filters.filter(_cars, and, sort);
		assertEquals(6, results.size());
		assertEquals("Camaro", ((Car)results.get(0)).getModel());
		assertEquals("black", ((Car)results.get(0)).getColor());
		assertEquals("Corvette", ((Car)results.get(1)).getModel());
		assertEquals("red", ((Car)results.get(1)).getColor());
		assertEquals("Zedar", ((Car)results.get(2)).getModel());
		assertEquals("red", ((Car)results.get(2)).getColor());
		assertEquals("Testarossa", ((Car)results.get(3)).getModel());
		assertEquals("red", ((Car)results.get(3)).getColor());
		assertEquals("Testarossa", ((Car)results.get(4)).getModel());
		assertEquals("black", ((Car)results.get(4)).getColor());
		assertEquals("W2", ((Car)results.get(5)).getModel());
		assertEquals("black", ((Car)results.get(5)).getColor());

		// test the toString()
		assertEquals("( ( com.townleyenterprises.filter.Car.topSpeed >= 100 ) && ( ( com.townleyenterprises.filter.Car.color = 'red' ) || ( com.townleyenterprises.filter.Car.color = 'black' ) ) )", and.toString());
	}

	public void testSortNoMatch()
	{
		SortSpecification[] sort = {
			new SortSpecification("make", SortOrder.DESCENDING)
		};

		List results = Filters.filter(_cars, _green, sort);
		assertEquals(0, results.size());
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(FiltersTest.class);
	}
}
