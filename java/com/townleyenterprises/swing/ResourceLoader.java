//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2003-2004, Andrew S. Townley
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
// File:	ResourceLoader.java
// Created:	Wed Nov 12 14:06:28 GMT 2003
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.swing;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides a way to access Java resource strings from a
 * location relative to a specific class.
 * <p>
 * This class supports a system property which will control how
 * missing resources are reported.  The property is:
 *
 * <ul>
 * <li>te-common.swing.resourceloader.showmissingresources - set to
 * <code>true</code> or <code>yes</code> to display messages about
 * missing resources</li>
 * </ul>
 * </p>
 *
 * @version $Id: ResourceLoader.java,v 1.5 2004/07/28 10:33:59 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

public class ResourceLoader implements ResourceProvider
{
	/**
	 * The constructor takes an object to use as the basis for
	 * finding resources.
	 *
	 * @param obj the object to provide the base resource location
	 */

	public ResourceLoader(Object obj)
	{
		this(obj.getClass());
	}

	/**
	 * This constructor takes a class to use as the basis for
	 * finding resources.
	 *
	 * @param cls the class providing the base resource location
	 */

	public ResourceLoader(Class cls)
	{
		this(cls, "strings");
	}

	/**
	 * This version of the constructor is used to load resource
	 * settings from an arbitrarily named resource property file.
	 *
	 * @param cls the class providing the base resource location
	 * @param name the name of the resources
	 */

	public ResourceLoader(Class cls, String name)
	{
		_klass = cls;
		String pkgName = _klass.getPackage().getName();
		_name = pkgName + ".resources." + name;

		_resources = ResourceBundle.getBundle(_name,
					Locale.getDefault());
	}

	/**
	 * This method is used to find a given resource string.
	 *
	 * @param key the resource string key
	 * @return the string for the key
	 */

	public String getString(String key)
	{
		String s = null;

		try
		{
			s = _resources.getString(key);
		}
		catch(MissingResourceException e)
		{
			String p = System.getProperty("te-common.swing.resourceloader.showmissingresources");
			if(p != null && (p.toLowerCase().charAt(0) == 'y'
					|| p.toLowerCase().charAt(0) == 't'))
			{
				System.err.println(getClass().getName() + ":  unable to find resource for key = '" + e.getKey() + "' in bundle named:  '" + _name + "'");
//				e.printStackTrace();
			}
		}

		return s;
	}

	/**
	 * This method is used to get a direct reference to the resource
	 * bundle.
	 *
	 * @return the resource bundle
	 */

	public ResourceBundle getResourceBundle()
	{
		return _resources;
	}

	/**
	 * Provide some clue as to what we're doing and who we are.
	 */

	public String toString()
	{
		StringBuffer buf = new StringBuffer("[ResourceLoader (");
		buf.append(hashCode());
		buf.append("): klass='");
		buf.append(_klass);
		buf.append("'; name='");
		buf.append(_name);
		buf.append("' ]");

		return buf.toString();
	}

	/** a reference to the class */
	private Class			_klass;

	/** save a reference to our resource bundle name */
	private String			_name;

	/** the resource bundle to be accessed */
	private ResourceBundle		_resources;
}
