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
// File:	AppConfig.java
// Created:	Sun Dec 26 11:23:42 GMT 2004
//
//////////////////////////////////////////////////////////////////////

package com.townleyenterprises.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.townleyenterprises.common.OverrideManager;
import com.townleyenterprises.common.OverrideNode;
import com.townleyenterprises.common.OverrideStrategy;
import com.townleyenterprises.common.UseLastOverrideStrategy;

/**
 * This class is used to group all of the settings for a given
 * application into a common place.  It is an evolution of the
 * {@link com.townleyenterprises.common.AppConfig} class and
 * should be used in place of it for all new code.
 * <p>
 * Like the previous version, this version allows the system property
 * values to override any value specified in the application's
 * configuration settings.  There are many, many useful reasons for
 * this.  This version does <em>not</em> provide a mechanism to change
 * the behavior.
 * </p>
 *
 * @version $Id: AppConfig.java,v 1.2 2004/12/27 23:18:54 atownley Exp $
 * @author <a href="mailto:atownley@users.sourceforge.net">Andrew S. Townley</a>
 * @since 3.0
 */

public final class AppConfig implements ConfigSupplier
{
	/**
	 * The constructor takes the name of the application
	 * which is managed by these settings.
	 *
	 * @param name the application name
	 */

	public AppConfig(String name)
	{
		_name = name;
	}

	/**
	 * This method is used to register a config supplier
	 * with this application.  If the supplier's name does
	 * not match the application name, the supplier will
	 * not be added.
	 *
	 * @param supplier the supplier to add
	 */

	public void registerConfigSupplier(ConfigSupplier supplier)
	{
		if(supplier.getAppName() != null 
				&& !(_name.equals(supplier.getAppName())))
		{
			return;
		}

		_manager.manage(supplier);
		_suppliers.add(supplier);
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
		return _manager.getKeys();
	}

	/**
	 * This method retrieves the value for the given key.
	 *
	 * @param key the key name
	 * @return the value as a string
	 */

	public String get(String key)
	{
		// FIXME:  what do we do in the case where the value
		// is changed somewhere else?
		String s = ConfigRegistry.getSystemConfig().get(key);
		if(s == null)
			return (String)_manager.get(key);

		return s;
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
		// FIXME:  need to figure out way to handle
		// permission checks
		_manager.put(key, value);
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
		
		for(Iterator i = _suppliers.iterator(); i.hasNext(); )
		{
			ConfigSupplier cs = (ConfigSupplier)i.next();
			props.putAll(cs.getProperties());
		}

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
	 * 	if the load fails
	 */

	public void load() throws IOException
	{
		for(Iterator i = _suppliers.iterator(); i.hasNext(); )
		{
			ConfigSupplier cs = (ConfigSupplier)i.next();
			cs.load();
		}
	}

	/**
	 * This method causes the properties to be saved to
	 * their original source.
	 *
	 * @exception IOException
	 * 	if the save fails
	 * @exception UnsupportedOperationException
	 * 	if the properties are read-only
	 */

	public void save() throws IOException, 
			UnsupportedOperationException
	{
		for(Iterator i = _suppliers.iterator(); i.hasNext(); )
		{
			ConfigSupplier cs = (ConfigSupplier)i.next();
			cs.save();
		}
	}

	/**
	 * This method is used to specify the node reading strategy.
	 *
	 * @param strategy the strategy
	 */

	public void setReadResolutionStrategy(OverrideStrategy strategy)
	{
		_manager.setReadResolver(strategy);
	}

	/**
	 * This method is used to specify the node writing strategy.
	 *
	 * @param strategy the strategy
	 */

	public void setWriteResolutionStrategy(OverrideStrategy strategy)
	{
		_manager.setWriteResolver(strategy);
	}

	/** this is the name of the application */
	private final String 		_name;

	/** the override manager we're using */
	private final CustomManager	_manager = new CustomManager();

	/** the list of suppliers being managed by the instance */
	private final List		_suppliers = new ArrayList();

	/////////////////////////////////////////////////////////////
	
	/**
	 * This class is used to allow us access to the
	 * OverrideNode we need to have for setting the
	 * configuration values.
	 */

	private class CustomManager extends OverrideManager
	{
		/**
		 * Create and initialize the settings we
		 * require.
		 */

		public CustomManager()
		{
			setReadResolver(new UseLastOverrideStrategy());
			setWriteResolver(new DefaultWriteStrategy());
		}

		protected Object getValue(Object key, Object obj)
		{
			if(obj instanceof ConfigSupplier)
			{
				return ((ConfigSupplier)obj).get(key.toString());
			}

			return null;
		}

		protected void setValue(Object key, Object obj, Object value)
		{
			if(obj instanceof ConfigSupplier)
			{
				((ConfigSupplier)obj).put(key.toString(),
						value.toString());
			}
		}

		protected Set getKeys(Object object)
		{
			if(object instanceof ConfigSupplier)
			{
				return ((ConfigSupplier)object).getKeys();
			}

			return null;
		}
	}

	/////////////////////////////////////////////////////////////
	
	/**
	 * This class provides a default strategy for writing new
	 * values to the configuration.  Essentially, it captures all
	 * writes to the last provider added.
	 */

	private class DefaultWriteStrategy
			extends UseLastOverrideStrategy
	{
		public OverrideNode resolve(Object key, List list)
		{
			if(list == null)
			{
				return new OverrideNode() {
					public Object get()
					{
						return _suppliers.get(_suppliers.size() - 1);
					}
				};
			}

			return super.resolve(key, list);
		}
	}
}
