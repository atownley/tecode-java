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
// File:	FieldSetValidationException.java
// Created:	Wed Jul 28 11:07:46 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.util.Map;

/**
 * This exception is thrown when a field set validator fails.
 *
 * @version $Id: FieldSetValidationException.java,v 1.2 2004/07/29 18:36:38 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class FieldSetValidationException extends Exception
{
	/**
	 * The constructor initializes the instance with all the
	 * relevant failure information.
	 *
	 * @param fields the map of fields in the field set
	 * @param msg the custom message
	 * @param throwable the cause of the failure
	 */

	public FieldSetValidationException(Map fields,
				String msg, Throwable throwable)
	{
		super(msg, throwable);
		_fields = fields;
	}

	public FieldSetValidationException(Map fields, String msg)
	{
		this(fields, msg, null);
	}

	public FieldSetValidationException(Map fields)
	{
		this(fields,
			Strings.get("sFieldSetValidationFailed"), null);
	}

	public Map getFields()
	{
		return _fields;
	}

	/** the field map */
	private final Map	_fields;
}
