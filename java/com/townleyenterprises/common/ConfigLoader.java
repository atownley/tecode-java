//////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2002-2003, Andrew S. Townley
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

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class provides the main mechanism of loading configuration
 * files so that I don't have to have the exact same code in more than
 * one place.
 *
 * @version $Id: ConfigLoader.java,v 1.1 2003/06/07 18:42:30 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
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
			System.out.println("Warning:  unable to access file (" + secondaryloc + ")");
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

	/** this is our global list of properties */
	private Properties	props = null;
}
