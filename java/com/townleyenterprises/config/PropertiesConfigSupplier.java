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
// File:	PropertiesConfigSupplier.java
// Created:	Sun Dec 26 14:35:41 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class provides a ConfigSupplier interface for 
 * generic properties instances.  The properties file should
 * be named using <em>appname</em><code>.properties</code>
 * where the various constructors can find it.
 *
 * @version $Id: PropertiesConfigSupplier.java,v 1.1 2004/12/26 20:35:18 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class PropertiesConfigSupplier implements ConfigSupplier
{
	/**
	 * This constructor takes the name of the application.
	 *
	 * @param name the application name
	 * @exception IOException
	 * 	if the properties cannot be loaded
	 */

	public PropertiesConfigSupplier(String name)
			throws IOException
	{
		this(name, null);
	}

	/**
	 * This constructor takes the name of the application
	 * and the path at which the properties can be
	 * located.
	 *
	 * @param name the application name
	 * @param path the path for the properties
	 * @exception IOException
	 * 	if the properties cannot be loaded
	 */

	public PropertiesConfigSupplier(String name, String path)
			throws IOException
	{
		String pname = name.concat(".properties");
		if(path != null)
		{
			pname = path.concat("/").concat(pname);
		}

		_name = name;
		_propfile = new File(pname);

		load();
	}

	/**
	 * This method returns the name used to access the
	 * settings.  Normally, it is set when the instance is
	 * created.
	 * 
	 * @return the application name
	 */

	public String getAppName()
	{
		return _name;
	}

	/**
	 * This method returns the set of keys in the
	 * given supplier.
	 *
	 * @return the keys as a set
	 */

	public Set getKeys()
	{
		Set s = new TreeSet();

		for(Enumeration e = _props.propertyNames(); e.hasMoreElements();)
		{
			s.add(e.nextElement());
		}

		return s;
	}

	/**
	 * This method retrieves the value for the given key.
	 *
	 * @param key the key name
	 * @return the value as a string
	 */

	public String get(String key)
	{
		return _props.getProperty(key);
	}

	/**
	 * This method is used to set the value for the given
	 * key.
	 *
	 * @param key the key name
	 * @param value the value for the key
	 * @exception UnsupportedOperationException
	 * 	if the property is not writable
	 */

	public void put(String key, String value)
			throws UnsupportedOperationException
	{
		_props.setProperty(key, value);
	}

	/**
	 * This method determines if the instance of the
	 * supplier supports case-sensitive key lookups.
	 *
	 * @return true if the key names are case-sensitive;
	 * false if not
	 */

	public boolean isCaseSensitive()
	{
		return true;
	}

	/**
	 * This method causes the properties to be (re)loaded
	 * from their original source.
	 *
	 * @exception IOException
	 * 	if the read operation fails
	 */

	public void load() throws IOException
	{
		_props = new Properties();
		_props.load(new FileInputStream(_propfile));
	}

	/**
	 * This method causes the properties to be saved to
	 * their original source.
	 *
	 * @exception IOException
	 * 	if the write operation fails
	 * @exception UnsupportedOperationException
	 * 	if the properties are read-only
	 */

	public void save() throws IOException,
				UnsupportedOperationException
	{
		if(_propfile.canWrite())
		{
			_props.store(new FileOutputStream(_propfile),
				"# properties for " + _name);
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}

	/** the application name */
	private final String 	_name;

	/** the property file name */
	private final File	_propfile;

	/** the properties instance */
	private Properties	_props;
}
