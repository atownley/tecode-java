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
// File:	BasicFormValidator.java
// Created:	Wed Jul 28 11:56:02 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.util.Locale;

/**
 * This class is a bean which provides a basic implementation of a 
 * form validator which will perform the validations in the correct
 * order.
 *
 * @version $Id: BasicFormValidator.java,v 1.1 2004/07/29 18:35:32 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public class BasicFormValidator implements FormValidator
{
	/**
	 * Basic constructor
	 */

	public BasicFormValidator()
	{
	}

	/**
	 * This method is used to determine the mode of the form
	 * validator.  If it returns true, the validator will attempt
	 * to perform all of the registered validations once and
	 * notify the listeners of any failures.  If it returns false,
	 * the validation operation will stop after the first
	 * validation failure.
	 *
	 * @return true if all fields will be validated; false if not
	 */

	public boolean getValidateAll()
	{
		return _validateAll;
	}

	/**
	 * This method is used to set the validation mode of the form.
	 * Two modes are supported:  the first will validate all of
	 * the fields on the form (the default), and the second will
	 * only validate until the first validation failure occurrs.
	 *
	 * @param val the value of the flag
	 */

	public void setValidateAll(boolean val)
	{
		_validateAll = val;
	}

	public void validate(Form form) throws Exception
	{
		validate(form, Locale.getDefault());
	}

	public void validate(Form form, Locale locale) throws Exception
	{
	}

	public void addFieldValidator(String name, FieldValidator validator)
	{
	}

	public void removeFieldValidator(FieldValidator validator)
	{
	}

	public void addFieldSetValidator(FieldSetValidator validator)
	{
	}

	public void removeFieldSetValidator(FieldSetValidator validator)
	{
	}

	public FieldValidator[] getFieldValidators()
	{
		return new FieldValidator[0];
	}

	public FieldSetValidator[] getFieldSetValidators()
	{
		return new FieldSetValidator[0];
	}

	// define the listener support
	
	public void addFormValidationListener(FormValidationListener listener)
	{
	}

	public void removeFormValidationListener(FormValidationListener listener)
	{
	}

	public FormValidationListener[] getFormValidationListeners()
	{
		return new FormValidationListener[0];
	}

	private boolean _validateAll = true;
}
