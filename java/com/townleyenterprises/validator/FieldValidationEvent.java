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
// File:	FieldValidationEvent.java
// Created:	Wed Jul 28 10:43:42 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

/**
 * This event is fired whenever a field validation has failed.  It is
 * intended to provide enough information to allow the listener to
 * react to the validation failure and correct it if possible.
 *
 * @version $Id: FieldValidationEvent.java,v 1.2 2004/08/11 16:21:39 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class FieldValidationEvent extends ValidationEvent
{
	/**
	 * The constructor initializes the instance with all the
	 * relevant failure information.
	 *
	 * @param sender the validator instance which caused the
	 * failure
	 * @param form the form instance containing the field
	 * @param field the name of the field
	 * @param value the value which failed validation
	 */

	public FieldValidationEvent(Object sender, Form form,
				String field, Object value)
	{
		super(sender, form);
		_field = field;
		_value = value;
	}

	/**
	 * This method returns the field name of the given form.
	 */

	public String getField()
	{
		return _field;
	}

	/**
	 * This method returns the raw field falue which triggered the
	 * validation failure.
	 */

	public Object getValue()
	{
		return _value;
	}

	/**
	 * This method returns the field value as a string rather than
	 * an object.
	 */

	public String getText()
	{
		return _value.toString();
	}

	/** the field name */
	private final String	_field;

	/** the field value */
	private final Object	_value;
}
