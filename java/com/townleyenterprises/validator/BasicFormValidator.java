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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * This class is a bean which provides a basic implementation of a 
 * form validator which will perform the validations in the correct
 * order.
 *
 * @version $Id: BasicFormValidator.java,v 1.2 2004/08/11 16:21:06 atownley Exp $
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
	 * validator.  If it returns <code>false</code>, the
	 * validator will attempt to perform all of the registered
	 * validations once and notify the listeners of any failures.
	 * If it returns <code>true</code>, the validation operation
	 * will stop after the first validation failure.
	 *
	 * @return false if all fields will be validated; true if not
	 */

	public boolean getAbortOnFailure()
	{
		return _abortOnFail;
	}

	/**
	 * This method is used to set the validation mode of the form.
	 * Two modes are supported:  the first will validate all of
	 * the fields on the form (the default), and the second will
	 * only validate until the first validation failure occurs.
	 *
	 * @param val the value of the flag
	 */

	public void setAbortOnFailure(boolean val)
	{
		_abortOnFail = val;
	}

	/**
	 * This method simply deferrs to the {@link
	 * #validate(Form,Locale)} method.
	 */

	public void validate(Form form) throws Exception
	{
		validate(form, Locale.getDefault());
	}

	/**
	 * This method performs the validations in the default order
	 * according to the order specified in the interface.  If any
	 * of the child validators fail (regardless of the setting of
	 * the <code>abortOnFailure</code> property, this method
	 * throws an exception.
	 */

	public void validate(Form form, Locale locale) throws Exception
	{
//		_formValid = true;
//		String[] fvals = form.getValues();
//
//		// validate the individual field values
//		Set keys = _fieldValidators.keySet();
//		for(Iterator i = keys.iterator(); i.hasNext();)
//		{
//			String key = (String)i.next();
//			List list = getFieldValidators(key, false);
//			validateFieldValues(fvals, list);
//		}
	}

	public void addFieldValidator(String name, FieldValidator validator)
	{
		List list = getFieldValidators(name, true);
		if(!list.contains(validator))
		list.add(validator);
		_rlookup.put(validator, list);
	}

	public void removeFieldValidator(FieldValidator validator)
	{
		List list = (List)_rlookup.get(validator);
		if(list == null)
			return;
		
		list.remove(validator);
	}

	public void addFieldSetValidator(FieldSetValidator validator)
	{
		if(!_fieldSetValidators.contains(validator))
			_fieldSetValidators.add(validator);
	}

	public void removeFieldSetValidator(FieldSetValidator validator)
	{
		_fieldSetValidators.remove(validator);
	}

	public FieldValidator[] getFieldValidators()
	{
		if(_fieldValidators.isEmpty())
			return EMPTY_FIELD_VALIDATORS;

		ArrayList al = new ArrayList();
		Collection lists = _fieldValidators.values();
		for(Iterator il = lists.iterator(); il.hasNext();)
		{
			List list = (List)il.next();
			al.addAll(list);
		}

		return (FieldValidator[])al.toArray(new FieldValidator[al.size()]);
	}

	public FieldSetValidator[] getFieldSetValidators()
	{
		if(_fieldSetValidators.isEmpty())
			return EMPTY_FIELD_SET_VALIDATORS;

		return (FieldSetValidator[])_fieldSetValidators.toArray(new FieldSetValidator[_fieldSetValidators.size()]);
	}

	// define the listener support
	
	public void addValidationListener(ValidationListener listener)
	{
		if(!_listeners.contains(listener))
			_listeners.add(listener);
	}

	public void removeValidationListener(ValidationListener listener)
	{
		_listeners.remove(listener);
	}

	public ValidationListener[] getValidationListeners()
	{
		return EMPTY_LISTENERS;
	}

	private List getFieldValidators(String name, boolean create)
	{
		List list = (List)_fieldValidators.get(name);
		if(list == null && create)
		{
			list = new ArrayList();
			_fieldValidators.put(name, list);
		}
		else
		{
			list = Collections.EMPTY_LIST;
		}

		return list;
	}

	private void validateFieldValues(Form form, String field,
				String[] values, List validators)
	{
		for(Iterator i = validators.iterator(); i.hasNext();)
		{
		}
	}

	/** keep a single copy of our empty listener array */
	private static final ValidationListener[]	EMPTY_LISTENERS = new ValidationListener[0];

	/** keep a single copy of our empty field set validator array */
	private static final FieldSetValidator[]	EMPTY_FIELD_SET_VALIDATORS = new FieldSetValidator[0];

	/** keep a single copy of our empty field validator array */
	private static final FieldValidator[]		EMPTY_FIELD_VALIDATORS = new FieldValidator[0];

	/** our list of field set validators */
	private ArrayList	_fieldSetValidators = new ArrayList();

	/** our map of field validators */
	private HashMap		_fieldValidators = new HashMap();

	/** our reverse lookup so that we can unhook things */
	private HashMap		_rlookup = new HashMap();

	/** our listeners */
	private ArrayList	_listeners = new ArrayList();

	/** set validation mode */
	private boolean		_abortOnFail = false;

	/** track our validation state */
	private boolean		_formValid = true;
}
