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
// File:	FieldSetValidator.java
// Created:	Mon Jul 26 21:06:49 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.util.Locale;
import java.util.Map;

/**
 * This interface defines a mechanism for the validation of an
 * arbitrary set of fields on a form.  This is mostly used for
 * an atomic validation operation on more than one field in the form.
 * The main uses of this would be things like making sure that there
 * was a state selected if the country was USA, Australia or Canada or
 * making sure that the sum of the net and the tax was the total of an
 * order form.
 *
 * @version $Id: FieldSetValidator.java,v 1.1 2004/07/28 10:13:40 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public interface FieldSetValidator
{
	/**
	 * This method validates the given map of field names and
	 * values according to the implementation-defined validation
	 * rules.
	 *
	 * @param values the Map of values validated by this validator
	 * @exception FieldSetValidationException
	 * 	if the validation fails
	 */

	void validate(Map values) throws FieldSetValidationException;

	/**
	 * This method validates the given map of field names and
	 * values according to the implementation-defined validation
	 * rules and the specified locale.
	 *
	 * @param values the Map of values validated by this validator
	 * @param locale the locale to be used other than the default
	 * @exception FieldSetValidationException
	 * 	if the validation fails
	 */

	void validate(Map values, Locale locale)
				throws FieldSetValidationException;
	
	/**
	 * This method is used to retrieve the names of the fields to
	 * be validated by this validator.
	 *
	 * @return the field names as a string array
	 */

	String[] getFieldNames();
}
