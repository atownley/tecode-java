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
// File:	FieldValidationException.java
// Created:	Mon Jul 26 21:01:36 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.text.MessageFormat;

/**
 * This exception is thrown when a field validator fails.
 *
 * @version $Id: FieldValidationException.java,v 1.1 2004/07/28 10:13:40 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class FieldValidationException extends Exception
{
	/**
	 * The constructor initializes the instance with all the
	 * relevant failure information.
	 *
	 * @param field the name of the field
	 * @param value the value which failed validation
	 * @param msg the custom message
	 * @param throwable the cause of the failure
	 */

	public FieldValidationException(String field, Object value,
				String msg, Throwable throwable)
	{
		super(msg, throwable);
		_field = field;
		_value = value;
	}

	public FieldValidationException(String field, Object value,
				String msg)
	{
		this(field, value, msg, null);
	}

	public FieldValidationException(String field, Object value)
	{
		this(field, value, MessageFormat.format("Validation of field {0} failed for value {1}", new Object[] { field, value }), null);
	}

	public String getField()
	{
		return _field;
	}

	public Object getValue()
	{
		return _value;
	}

	/** the field name */
	private final String	_field;

	/** the field value */
	private final Object	_value;
}
