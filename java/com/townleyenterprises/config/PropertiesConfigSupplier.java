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
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.townleyenterprises.trace.BasicTrace;

/**
 * This class provides a ConfigSupplier interface for 
 * generic properties instances.  The properties file should
 * be named using <em>appname</em><code>.properties</code>
 * where the various constructors can find it.
 *
 * @version $Id: PropertiesConfigSupplier.java,v 1.2 2004/12/27 23:19:43 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class PropertiesConfigSupplier implements ConfigSupplier
{
	/**
	 * This constructor is used to load the properties as
	 * resources relative to the specified class.
	 *
	 * @param name the application name
	 * @param klass the class name to be used
	 * @exception IOException
	 * 	if the load fails
	 */

	public PropertiesConfigSupplier(String name, Class klass)
			throws IOException
	{
		this(name, klass, null);
	}

	/**
	 * This constructor is used to load the properties as
	 * resources relative to the specified class, but from a
	 * specific location on the classpath.
	 *
	 * @param name the application name
	 * @param klass the class name to be used
	 * @param path the path relative to the location of the class
	 * @exception IOException
	 * 	if the load fails
	 */

	public PropertiesConfigSupplier(String name, 
				Class klass, String path)
			throws IOException
	{
		final String[] pnames = { "name", "klass", "path" };
		_trace.methodStart("PropertiesConfigSupplier", pnames,
				new Object[] { name, klass, path });

		try
		{
			_name = name;
			_propfile = name.concat(".properties");
			if(path != null)
			{
				_propfile = path.concat(_propfile);
			}
			_klass = klass;

			load();

			_trace.methodReturn();
		}
		catch(IOException e)
		{
			throw (IOException)_trace.methodThrow(e, true);
		}
		finally
		{
			_trace.methodExit();
		}
	}

	/**
	 * This constructor allows an absolute path name to be used to
	 * specify the location of the properties file.  This version
	 * of the constructor should be used only if you always wish
	 * to look in a specific location for the file.
	 * <p>
	 * Unlike the other versions of the constructor, this version
	 * makes no assumptions about the name of the properties file.
	 * It should be fully specified using the path argument.
	 * </p>
	 *
	 * @param name the application name
	 * @param path the path on the filesystem.  If not an absolute
	 * path, it is relative to the directory where the JVM was
	 * started.
	 * @exception IOException
	 * 	if the load fails
	 */

	public PropertiesConfigSupplier(String name, String path)
			throws IOException
	{
		final String[] pnames = { "name", "path" };
		_trace.methodStart("PropertiesConfigSupplier", pnames,
				new Object[] { name, path });

		try
		{
			_name = name;
			_propfile = path;
			_klass = null;

			load();
		}
		catch(IOException e)
		{
			throw (IOException)_trace.methodThrow(e, true);
		}
		finally
		{
			_trace.methodExit();
		}
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
	 * This method is used to convert the contents of the instance
	 * to a Java Properties object.  This conversion is necessary
	 * for easy interoperation with existing Java APIs.
	 *
	 * @return the settings as a Properties object
	 */

	public Properties getProperties()
	{
		return _props;
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
		_trace.methodStart("load");

		try
		{
			_trace.tprintln(5, "propfile = '" + _propfile + "'");
			_props = new Properties();

			if(_klass != null)
			{
				_props.load(_klass.getResourceAsStream(_propfile));
			}
			else
			{
				_props.load(new FileInputStream(_propfile));
			}

			_trace.methodReturn();
		}
		catch(IOException e)
		{
			throw (IOException)_trace.methodThrow(e, true);
		}
		finally
		{
			_trace.methodExit();
		}
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
		_trace.methodStart("save");

		try
		{
			if(_klass != null)
			{
				throw (UnsupportedOperationException)_trace.methodThrow(new UnsupportedOperationException(), true);
			}
			else
			{
				_props.store(new FileOutputStream(_propfile),
					"# Properties written at " + new Date());
			}
			_trace.methodReturn();
		}
		catch(IOException e)
		{
			throw (IOException)_trace.methodThrow(e, true);
		}
		finally
		{
			_trace.methodExit();
		}
	}

	/** the class used to define the locations */
	private final Class		_klass;

	/** the application name */
	private final String 		_name;

	/** the property file name */
	private String			_propfile;

	/** the properties instance */
	private Properties		_props;

	/** trace instance */
	private static BasicTrace	_trace = new BasicTrace("PropertiesConfigSupplier");
}
