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
// File:	Form.java
// Created:	Mon Jul 26 21:02:13 IST 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.validator;

/**
 * This interface represents a generalized form.  The form can be
 * anything from an HTML web page form to a set of controls displayed
 * on a Swing JPanel.  The abstraction works in both cases and also
 * supports field values as indexed properties.
 *
 * @version $Id: Form.java,v 1.2 2004/07/29 18:36:38 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 * @since 3.0
 */

public interface Form
{
	/**
	 * This method returns all of the field values of the named
	 * field.
	 *
	 * @param name the name of the field
	 * @return the values as a object array
	 */

	Object[] getValues(String name);

	/**
	 * This method allows the setting of all of the field values
	 * for the named field at once.
	 *
	 * @param name the name of the field
	 * @param values the values array
	 */

	void setValues(String name, Object[] values);

	/**
	 * This method returns the field value at the specified index.
	 *
	 * @param name the name of the field
	 * @param idx the index to retrieve
	 * @return the value of the field at the specified index
	 * @exception IndexOutOfBoundsException
	 * 	if the index is not within the bounds of the field
	 * 	values.
	 */

	Object getValue(String name, int idx)
				throws IndexOutOfBoundsException;

	/**
	 * This overloaded method is used to optimize the retrieval of
	 * the first value in forms which don't have indexed field
	 * values.
	 *
	 * @param name the field name
	 * @return the field value
	 */

	Object getValue(String name);

	/**
	 * This method sets the field value at the specified index.
	 *
	 * @param name the name of the field
	 * @param idx the index to retrieve
	 * @param value the value of the field at the specified index
	 * @exception IndexOutOfBoundsException
	 * 	if the index is not within the bounds of the field
	 * 	values.
	 */

	void setValue(String name, int idx, Object value)
				throws IndexOutOfBoundsException;

	/**
	 * This overloaded method is used to optimize the setting of
	 * the first value in forms which don't have indexed field
	 * values.
	 *
	 * @param name the field name
	 * @param value the field value
	 */

	void setValue(String name, Object value);
}
