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
// Title:	Car.java
// Created: 	Sun Nov 28 14:43:03 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.filter;

import java.math.BigDecimal;

/**
 * This class supports the unit tests for the filter package.
 *
 * @version $Id: Car.java,v 1.1 2004/11/28 20:06:27 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class Car
{
	public Car(String make, String model, BigDecimal price,
		int topSpeed, String color)
	{
		_make = make;
		_model = model;
		_price = price;
		_topSpeed = new Integer(topSpeed);
		_color = color;
	}

	public String getMake() { return _make; }
	public String getModel() { return _model; }
	public BigDecimal getPrice() { return _price; }
	public Integer getTopSpeed() { return _topSpeed; }
	public String getColor() { return _color; }

	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(_make);
		buf.append(" ");
		buf.append(_model);
		buf.append(" ");
		buf.append(_color);
		buf.append(" ");
		buf.append("$");
		buf.append(_price);
		buf.append(" ");
		buf.append(_topSpeed);

		return buf.toString();

	}

	private final String _make;
	private final String _model;
	private final BigDecimal _price;
	private final Integer _topSpeed;
	private final String _color;
}

