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
// File:	FormValidator.java
// Created:	Mon Jul 26 21:13:21 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.util.Locale;

/**
 * This interface defines the validation functionality of a form as a
 * whole.  Each form has three different levels of validation which is
 * possible:
 * <ul>
 * <li>The validation of the entire form</li>
 * <li>The validation of sets of fields on the form</li>
 * <li>The validation of individual fields on the form</li>
 * </ul>
 * <p>
 * Based on previous projects, this kind of granularity is necessary
 * to fully encapsulate all of the possible kinds of form validation
 * done in the real world.
 * </p>
 * <p>
 * The default order of the validations will be the following unless
 * explicitly changed (and documented) by implementing classes.
 * <ol>
 * <li>All individual field values</li>
 * <li>All field sets</li>
 * <li>The entire form</li>
 * </ol>
 * </p>
 *
 * @version $Id: FormValidator.java,v 1.1 2004/07/28 10:13:40 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public interface FormValidator
{
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

	boolean getValidateAll();

	/**
	 * This method is used to set the validation mode of the form.
	 * Two modes are supported:  the first will validate all of
	 * the fields on the form (the default), and the second will
	 * only validate until the first validation failure occurrs.
	 */

	void setValidateAll();

	void validate(Form form) throws Exception;

	void validate(Form form, Locale locale) throws Exception;

	void addFieldValidator(String name, FieldValidator validator);

	void removeFieldValidator(FieldValidator validator);

	void addFieldSetValidator(FieldSetValidator validator);

	void removeFieldSetValidator(FieldSetValidator validator);

	FieldValidator[] getFieldValidators();

	FieldSetValidator[] getFieldSetValidators();

	// define the listener support
	
	void addFormValidationListener(FormValidationListener listener);

	void removeFormValidationListener(FormValidationListener listener);

	FormValidationListener[] getFormValidationListeners();
}
