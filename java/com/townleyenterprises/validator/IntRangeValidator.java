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
// File:	IntRangeValidator.java
// Created:	Wed Jul 28 15:21:15 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.io.Serializable;
import java.util.Locale;

/**
 * This validator takes a minimum and maximum integer value to be used
 * for validation purposes.
 *
 * @version $Id: IntRangeValidator.java,v 1.2 2004/08/11 16:22:11 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class IntRangeValidator implements FieldValidator
{
	/**
	 * This method sets the maximum integer value for the range.
	 *
	 * @param val the int value
	 * @param inclusive true to mean this value is included in the
	 * range; false otherwsie
	 */

	public void setMax(int val, boolean inclusive)
	{
		_checkmax = true;
		_max = val;
		_maxinc = inclusive;
	}

	/**
	 * This method sets the minimum integer value for the range.
	 *
	 * @param val the int value
	 * @param inclusive true to mean this value is included in the
	 * range; false otherwsie
	 */

	public void setMin(int val, boolean inclusive)
	{
		_checkmin = true;
		_min = val;
		_mininc = inclusive;
	}

	/**
	 * This method validates the given value based on the
	 * implementation-defined validation rules.
	 *
	 * @exception Exception
	 * 	if the validation fails
	 */

	public void validate(Object value) throws Exception
	{
		validate(value, Locale.getDefault());
	}

	/**
	 * This method validates the given value based on the
	 * implementation-defined validation rules and the specified
	 * locale.
	 *
	 * @exception Exception
	 * 	if the validation fails
	 */

	public void validate(Object value, Locale locale)
				throws Exception
	{
		int x = 0;

		if(value instanceof Integer)
		{
			x = ((Integer)value).intValue();
		}
		else if(value instanceof String)
		{
			x = Integer.parseInt((String)value);
		}
		else
		{
			throw new IllegalArgumentException();
		}

		int min = _min;
		int max = _max;
		String op = null;
		
		if(_mininc)
		{
			min = min--;
		}

		if(_maxinc)
		{
			max = max++;
		}

		if(_checkmax && (x > max))
		{
			op = (_maxinc ? ">=" : ">");
			throw new Exception(Strings.format("sIntRangeValidationFailed", new Object[] { value, op, new Integer(_max) } ));
		}

		if(_checkmin && (x < min))
		{
			op = (_mininc ? "<=" : "<");
			throw new Exception(Strings.format("sIntRangeValidationFailed", new Object[] { value, op, new Integer(_min) } ));
		}
	}

	private int 	_max;
	private int 	_min;
	private boolean	_mininc;
	private boolean	_maxinc;
	private boolean _checkmax = false;
	private boolean _checkmin = false;
}
