//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003, Andrew S. Townley
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
// File:	PropertyProxy.java
// Created:	Sat May 17 13:41:32 IST 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.util.HashMap;
import java.lang.reflect.Method;

/**
 * This class provides a simple way to dynamically access property
 * values similar to the way it is handled in the Jakarta Struts
 * package.
 *
 * @version $Id: PropertyProxy.java,v 1.2 2003/06/08 21:57:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public class PropertyProxy
{
	/**
	 * The constructor takes the class which will be accessed.
	 *
	 * @param klass the java class
	 */

	public PropertyProxy(Class klass)
	{
		// locate all of the accessor once
		Method[] ms = klass.getMethods();
		
		for(int i = 0; i < ms.length; ++i)
		{
			if(ms[i].getName().startsWith("get"))
			{
				_methods.put(ms[i].getName().toLowerCase(),ms[i]);
			}
		}
		
		_klass = klass;
	}

	/**
	 * This method will retrieve the named property value for the
	 * specified object.  The object must be of the same class as
	 * the class specified in the constructor.
	 *
	 * @param property the case-insensitive name of the property
	 * 	to retrieve from the object
	 * @param o the object to access
	 */

	public Object getPropertyValue(String property, Object o)
	{
		Object rez;
		String name = "get" + property;
		Method method = (Method)_methods.get(name.toLowerCase());
		if(method == null)
			throw new RuntimeException("error:  property accessor '" + name + "' not found in '" + _klass + "'");

		if(!(_klass.equals(o.getClass())))
			throw new RuntimeException("error:  attempt to access property '" + property + "' of " + _klass.toString() + " on object of " + o.getClass() + ":  " + o.toString());
		
		try
		{
			rez = method.invoke(o, new Object[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

		return rez;
	}

	/**
	 * Retrieves the class of the subject of this proxy instance.
	 *
	 * @return the Java class
	 */

	public Class getSubjectClass()
	{
		return _klass;
	}

	private final Class	_klass;
	private HashMap  	_methods = new HashMap();
}

