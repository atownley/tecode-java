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
// File:	StringFilterTest.java
// Created:	Sun Nov 28 16:39:52 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.math.BigDecimal;

/**
 * Tests for various parts of the filter package.  Main reason
 * for the broader coverage is that some things don't make
 * sense to test individually.
 *
 * @version $Id: StringFilterTest.java,v 1.1 2004/11/28 20:06:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public final class StringFilterTest extends BaseFilterTest
{
	public StringFilterTest(String testname)
	{
		super(testname);
	}

	public void testStringFilter()
	{
		StringFilter sf = new StringFilter(Car.class,
				"make", "Chevrolet", false);

		assertEquals(true, sf.doFilter(_camaro));
	}

	public void testStringFilterIgnoreCase()
	{
		StringFilter sf = new StringFilter(Car.class,
				"make", "cHeVrOlEt", true);

		assertEquals(true, sf.doFilter(_corvette));
	}

	public void testStringFilterNullTest()
	{
		StringFilter sf = new StringFilter(Car.class,
				"make", null, true);

		assertEquals(false, sf.doFilter(_corvette));
	}

	public void testStringFilterNullValue()
	{
		Car car = new Car(null, "Camaro",
				new BigDecimal("33000.00"),
				180, "black");
		StringFilter sf = new StringFilter(Car.class,
				"make", null, true);

		assertEquals(true, sf.doFilter(car));
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(StringFilterTest.class);
	}
}
