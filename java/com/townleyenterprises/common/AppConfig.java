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
// File:	AppConfig.java
// Created:	Tue Oct 22 18:28:05 IST 2002
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.common;

import java.io.IOException;
import java.util.Properties;

/**
 * This file provides a generalized mechanism for centralizing access
 * to application configuration information.
 *
 * @version $Id: AppConfig.java,v 1.2 2003/06/08 21:57:37 atownley Exp $
 * @author <a href="mailto:adz1092@netscape.net">Andrew S. Townley</a>
 */

public final class AppConfig
{
	// prevent instantiation
	private AppConfig() {}

	/**
	 * This method is used to register an ConfigSupplier with the
	 * class.  The ConfigSupplier tells the AppConfig where to
	 * load the application configuration information.
	 *
	 * @exception IOException
	 * 	if there is a problem accessing the configuration
	 * 	information.
	 */

	public static void registerAppSupplier(ConfigSupplier s)
				throws IOException
	{
		supplier = s;

		String appName = s.getAppName();
		StringBuffer buf = new StringBuffer("/");
		buf.append(appName.toLowerCase());
		buf.append(".properties");
		
		String prefs = buf.toString();
		String defprefs = prefs.substring(1);

//		System.out.println("**** prefs:  " + prefs);
//		System.out.println("**** defprefs:  " + defprefs);

		props = new ConfigLoader(s.getClass(), defprefs, prefs);
	}

	/**
	 * This method is used to register a config supplier for the
	 * persistence information.
	 */

	public static void registerPersistenceSupplier(ConfigSupplier s)
	{
		persistenceSupplier = s;
	}

	/**
	 * This method returns the appropriate property value.
	 *
	 * @param name the parameter key
	 * @return the property value
	 */

	public static String get(String name)
	{
		if(props == null)
		{
			new RuntimeException("AppConfig not initialized!  Please register a ConfigSupplier.");
		}
		
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

	public static String get(String prefix, String name)
	{
		if(props == null)
		{
			new RuntimeException("AppConfig not initialized!  Please register a ConfigSupplier.");
		}
		
		return props.getProperty(prefix, name);
	}

	/**
	 * This method is used to return a reference to the
	 * persistence configuration supplier.
	 *
	 * @return ConfigSupplier instance
	 */

	public static ConfigSupplier getPersistenceConfigSupplier()
	{
		return persistenceSupplier;
	}

	/**
	 * This method allows access to all of the property values so
	 * that external code can be configured from the unified
	 * properties.
	 *
	 * @return a copy of the properties
	 */

	public static Properties getProperties()
	{
		return props.getProperties();
	}

	/** this is our global list of properties */
	private static ConfigLoader	props = null;

	/** this is our config supplier */
	private static ConfigSupplier	supplier = null;
	
	/** this is our config supplier for the persistence layer */
	private static ConfigSupplier	persistenceSupplier = null;
}
