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
// File:	MapConfigSupplier.java
// Created:	Sun Dec 26 15:23:51 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.IOException;
import java.util.Properties;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a ConfigSupplier adapter for maps.
 *
 * @version $Id: MapConfigSupplier.java,v 1.2 2004/12/27 23:18:54 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class MapConfigSupplier implements ConfigSupplier
{
	/**
	 * This constructor takes the name of the application
	 * and the map to use.
	 *
	 * @param name the application name
	 * @param map the map instance
	 */

	public MapConfigSupplier(String name, Map map)
	{
		_name = name;
		_map = map;
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
		return _map.keySet();
	}

	/**
	 * This method retrieves the value for the given key.
	 *
	 * @param key the key name
	 * @return the value as a string
	 */

	public String get(String key)
	{
		return (String)_map.get(key);
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
		_map.put(key, value);
	}

	/**
	 * This method is used to convert the contents of the instance
	 * to a Java Properties object.  This conversion is necessary
	 * for easy interoperation with existing Java APIs.
	 *
	 * @return the settings as a Properties object
	 */

	public Properties getProperties()
	{
		Properties props = new Properties();
		props.putAll(_map);

		return props;
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
		throw new UnsupportedOperationException();
	}

	/** the application name */
	private final String 	_name;

	/** the map instance */
	private Map		_map;
}
