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
// File:	ConfigRegistry.java
// Created:	Sun Dec 26 11:27:38 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides an implementation of the Registry
 * pattern for a VM-wide configuration.
 *
 * @version $Id: ConfigRegistry.java,v 1.1 2004/12/26 20:35:18 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class ConfigRegistry
{
	/**
	 * This method is used to register a configuration
	 * supplier.
	 *
	 * @param supplier the supplier to register
	 */

	public static void registerSupplier(ConfigSupplier supplier)
	{
		AppConfig ac = getConfig(supplier.getAppName());
		ac.registerConfigSupplier(supplier);
	}

	/**
	 * This method is used to retrieve a reference to the
	 * specified AppConfig instance.
	 *
	 * @param name the config name
	 * @return the
	 */

	public static AppConfig getConfig(String name)
	{
		AppConfig ac = (AppConfig)_map.get(name);
		if(ac == null)
		{
			ac = new AppConfig(name);
			synchronized(_map)
			{
				_map.put(name, ac);
			}
		}

		return ac;
	}

	/**
	 * This method returns a reference to the default
	 * system configuration provider.  In this case, it is
	 * a reference to the global system properties.
	 *
	 * @return the system properties as a ConfigSupplier
	 */

	public static ConfigSupplier getSystemConfig()
	{
		return _sysconf;
	}

	private ConfigRegistry() {}

	/** make the system properties available */
	private static ConfigSupplier	_sysconf = new SystemConfigSupplier();

	/** our map of suppliers */
	private static final Map	_map = new HashMap();
}
