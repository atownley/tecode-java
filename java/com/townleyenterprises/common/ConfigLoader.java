//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2004, Andrew S. Townley
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
// File:	ConfigLoader.java
// Created:	Wed Oct 23 10:53:01 IST 2002
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class provides the main mechanism of loading configuration
 * files so that I don't have to have the exact same code in more than
 * one place.
 * <p>
 * <em>
 * <strong>
 * NOTE:  Though this class has not been deprecated, it probably isn't
 * nearly as useful as it used to be.  All of the hierarchical loading
 * of properties/settings/resources has been refactored to use the
 * {@link OverrideManager} class instead.  This class may dissappear
 * at some point, depending on usage patterns and further thought by
 * the development team.
 * </strong>
 * </em>
 * </p>
 *
 * @version $Id: ConfigLoader.java,v 1.7 2004/12/28 21:45:34 atownley Exp $
 * @author <a href="mailto:adz1092@yahoo.com">Andrew S. Townley</a>
 */

// FIXME:  this should really use the new 1.4 preferences stuff

public class ConfigLoader
{
	/**
	 * The constructor takes the primary and secondary locations
	 * of the properties to be loaded.  Properties located in the
	 * secondary location will override properties in the primary
	 * location.
	 *
	 * @param primaryloc the primary location (usually, just the
	 *	 name of the properties file)
	 * @param secondaryloc the secondary location (usually, the
	 * 	same name prefixed with a '/' so that it can be placed
	 * 	in the classes directory of the servlet container)
	 * @exception IOException
	 * 	if there was a problem accessing the properties
	 */

	public ConfigLoader(Class cls, String primaryloc, String secondaryloc)
			throws IOException
	{
		klass = cls;
		primary = primaryloc;
		secondary = secondaryloc;

		props = new Properties();
//		System.err.println(cls.getResource(primaryloc));
		InputStream is = cls.getResourceAsStream(primaryloc);
		if(is != null)
		{
			props.load(is);
		}

		// now, get the "overrides";
		try
		{
			// as they may not exist, we need to trap the
			// IOException we might get
//			System.err.println(cls.getResource(secondaryloc));
			is = cls.getResourceAsStream(secondaryloc);
			if(is != null)
			{
				props.load(is);
			}
		}
		catch(IOException e)
		{
			System.err.println(Strings.format("fUnableToAccessFile",
				new Object[] { secondaryloc }));
		}

		// temporary debugging
//		props.list(System.err);
	}

	/**
	 * This method returns the appropriate property value.
	 *
	 * @param name the parameter key
	 * @return the property value
	 */

	public String getProperty(String name)
	{
		return props.getProperty(name);
	}

	/**
	 * Alternative method that provides a prefix in addition to
	 * the specific parameter to retrieve.
	 * 
	 * @param prefix the prefix
	 * @param name the actual param name
	 * @return the parameter value
	 */

	public String getProperty(String prefix, String name)
	{
		StringBuffer buf = new StringBuffer(prefix);
		buf.append(".");
		buf.append(name);

		return getProperty(buf.toString());
	}

	/**
	 * This method allows access to all of the property values so
	 * that external code can be configured from the unified
	 * properties.
	 *
	 * @return a copy of the properties
	 */

	public Properties getProperties()
	{
		return (Properties)props.clone();
	}

	/**
	 * This method is used to return the keys managed by this
	 * loader.
	 *
	 * @return the keys as a set
	 * @since 3.0
	 */

	public Set getKeys()
	{
		Set s = new TreeSet();

		for(Enumeration e = props.propertyNames(); e.hasMoreElements();)
		{
			s.add(e.nextElement());
		}

		return s;
	}

	/**
	 * This method prints information about this configuration
	 * loader instance.
	 */

	public String toString()
	{
		StringBuffer buf = new StringBuffer("[");
		buf.append(getClass().getName());
		buf.append(": primary = '");
		buf.append(klass.getPackage().getName().replaceAll("\\.", "/"));
		buf.append("/");
		buf.append(primary);
		buf.append("'; secondary = '");
		buf.append(secondary);
		buf.append("' ]");

		return buf.toString();
	}

	/**
	 * Checks for equality based on the values of the constructor
	 * attributes.
	 */

	public boolean equals(Object o)
	{
		if(o == null)
			return false;

		if(!getClass().getName().equals(o.getClass().getName()))
			return false;

		ConfigLoader loader = (ConfigLoader)o;
		return loader.klass.equals(klass) 
				&& loader.primary.equals(primary)
				&& loader.secondary.equals(secondary);
	}

	/** this is our global list of properties */
	private Properties	props = null;

	/** the class used as the base for the configuration */
	private Class		klass = null;

	/** the primary location for the properties */
	private String		primary = null;

	/** the secondary location for the properies */
	private String		secondary = null;
}
