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
// File:	ValidationEvent.java
// Created:	Wed Jul 28 10:43:18 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

import java.util.EventObject;

/**
 * This event is the basis for all of the validation failure events.
 * It provides a way to consume the event and detect if the failure
 * has already been corrected in addition to a link to the form
 * holding the data to be validated.
 *
 * @version $Id: ValidationEvent.java,v 1.1 2004/07/28 10:13:40 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public abstract class ValidationEvent extends EventObject
{
	/**
	 * The constructor initializes the instance with the event
	 * sender.
	 *
	 * @param sender the validator instance which caused the
	 * failure
	 */

	public ValidationEvent(Object sender, Form form)
	{
		super(sender);
		_consumed = false;
		_form = form;
	}

	/**
	 * Consume the event and mark it so that other listeners in
	 * the chain can detect if they have to do anything.
	 */

	public void consume()
	{
		_consumed = true;
	}

	/**
	 * Check if the event has been consumed.
	 */

	public boolean isConsumed()
	{
		return _consumed;
	}

	/**
	 * Retrieve a reference to the form.
	 */

	public Form getForm()
	{
		return _form;
	}

	/** our consumption status */
	private boolean _consumed;

	/** track the form so that listeners can change the data */
	private Form	_form;
}
