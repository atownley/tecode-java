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
// File:	BaseFilterTest.java
// Created:	Sun Nov 28 16:39:52 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;

/**
 * Common base class for the filter tests.
 *
 * @version $Id: BaseFilterTest.java,v 1.1 2004/11/28 20:06:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class BaseFilterTest extends TestCase
{
	public BaseFilterTest(String testname)
	{
		super(testname);
	}

	public void setUp()
	{
		_cars.clear();
		_camaro = new Car("Chevrolet", "Camaro",
				new BigDecimal("33000.00"),
				180, "black");
		_corvette = new Car("Chevrolet", "Corvette",
				new BigDecimal("65000.00"),
				180, "red");
		_cars.add(_camaro);
		_cars.add(_corvette);
		_cars.add(new Car("Ferarri", "Testarossa", 
			new BigDecimal("150000.00"), 200, "red"));
		_cars.add(new Car("Ferarri", "Testarossa",
			new BigDecimal("180000.00"), 200, "black"));
		_cars.add(new Car("Vector", "W2", 
			new BigDecimal("210000.00"), 220, "black"));
		_cars.add(new Car("Chevrolet", "Corvette",
			new BigDecimal("62000.00"), 190, "yellow"));
		_cars.add(new Car("Chevrolet", "Zedar",
			new BigDecimal("100000.00"), 100, "red"));
		_cars.add(new Car("Chevrolet", "Nova",
			new BigDecimal("17000.00"), 80, "red"));
		
		_red = new StringFilter(Car.class, "color", "red", false);
		_black = new StringFilter(Car.class, "color", "black", false);
		_green = new StringFilter(Car.class, "color", "green", false);
		_blue = new StringFilter(Car.class, "color", "blue", false);
		_yellow = new StringFilter(Car.class, "color", "yellow", false);
		_speed = new QueryFilter(Car.class, "topSpeed",
				QueryOperator.GE, new Integer(100));
	}

	public void testSetUp()
	{
		assertEquals(8, _cars.size());
	}

	StringFilter	_red = null;
	StringFilter	_black = null;
	StringFilter	_green = null;
	StringFilter	_blue = null;
	StringFilter	_yellow = null;
	QueryFilter	_speed = null;
	Car		_camaro = null;
	Car		_corvette = null;
	Collection	_cars = new ArrayList();
}
